package com.wimetro.cg.protocol.message;

import com.wimetro.cg.common.Constants;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdPropParam;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import com.wimetro.cg.util.ToolConvert;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * @title: Message
 * @author: Ellie
 * @date: 2022/02/10 11:28
 * @description:
 **/
@Data
@Slf4j
public abstract class Message<T extends MessageBody> {

    private static final String MSG_SPLITTER = ";";
    private static final int MSG_LENGTH_FIELD_LENGTH = 4;
    private static final int MSG_TYPE_SPLITTER = 1000;

    private MessageHeader messageHeader;
    private T messageBody;
    private InetSocketAddress senderAddress;
    private InetSocketAddress targetAddress;    // 发送目标的地址

    public T getMessageBody(){
        return messageBody;
    }

    public void encode(ByteBuf byteBuf) {
        // 报文体编码
        String bodyHexStr = encodeMsgBody(messageBody);
        // 计算报文体长度
        int bodyLength = (bodyHexStr.length() + 1) / 2;
        messageHeader.setLength(bodyLength);
        // 报文头
        String msgHeadStr = messageHeader.encode();
        String msgHexStr = msgHeadStr + bodyHexStr;

//        log.info("befor check, {}", msgHexStr);

        // 校验
        byte[] msgBytes = ToolConvert.hexStrToBytes(msgHexStr);
        byte checkFlag = sumCheck(msgBytes);
        msgHexStr += ToolConvert.bytesToHexStr(new byte[]{checkFlag});

//        log.info("after check, {}", msgHexStr);

        // 转码 + 开始结束标志
        msgHexStr = ToolConvert.convertMsgReq(msgHexStr);

        log.info("[encode] => {} - {}", targetAddress, msgHexStr);

        byteBuf.writeBytes(ToolConvert.hexStrToBytes(msgHexStr));
    }

    public static String encodeMsgBody(MessageBody messageBody) {
        List<CmdPropParam> sParams = getSortedParams(messageBody);

        StringBuilder retSb = new StringBuilder();
        for (CmdPropParam sParam : sParams) {
            retSb.append(sParam.getHexVal());
        }

        return retSb.toString();
    }

    public abstract Class<T> getMessageBodyDecodeClass(int opcode);

    public boolean decode(Object msg) {
        if (msg == null) {
            return false;
        }

        // 长度检查
        byte[] data = (byte[]) msg;
        if (data.length <= MessageHeader.HEAD_LEN) {
            return false;
        }

        String hexCmd = ToolConvert.bytesToHexStr(data);

//        log.info("转码前 {}", hexCmd);
        // 转码
        if (hexCmd.contains("7f01") || hexCmd.contains("7f02")) {
            hexCmd = ToolConvert.convertMsgRsq(hexCmd);
            data = ToolConvert.hexStrToBytes(hexCmd);
        }

        // 校验
        int length = data.length - 1;
        byte checkFlag = data[length];
        byte sumCheck = sumCheck(ToolConvert.getSource(data, 0, length));
        if (sumCheck != checkFlag)  {
            log.info("[decode] 校验失败 {} => [{}]", senderAddress, hexCmd);
            return false;
        }

        // 解析
        int streamId = ToolConvert.bytesToInt(ToolConvert.getSource(data, 0, 4));
        String sn = ToolConvert.bytesToStr(ToolConvert.getSource(data, 4, 16));
        String pwd = ToolConvert.bytesToHexStr(ToolConvert.getSource(data, 20, 4));
        int msgCode = ToolConvert.bytesToInt(ToolConvert.getSource(data, 24, 3));
        int bodyLength = ToolConvert.bytesToInt(ToolConvert.getSource(data, 27, 4));
        byte[] bodyData = ToolConvert.getSource(data, 31, bodyLength);

        MessageHeader header = new MessageHeader();
        header.setStreamId(streamId);
        header.setDeviceSN(sn);
        header.setDevicePwd(pwd);
        header.setMsgCode(msgCode);
        header.setLength(bodyLength);
        this.messageHeader = header;

        // 过滤广播消息
        if (msgCode == Constants.CODE_BROADCAST) {
            return false;
        }


        T body = decodeMsgBody(msgCode, bodyData);
        this.messageBody = body;

        log.info("[decode] <= {} - {} - {}", senderAddress, messageHeader.toString(), messageBody.toString());

        return true;
    }

    private T decodeMsgBody(int msgType, byte[] msgBodyData) {
        Class<T> bodyClazz = getMessageBodyDecodeClass(msgType);
        List<Field> allFields = getAllFields(bodyClazz);
        T body = null;
        try {
            body = bodyClazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("创建消息对象失败, e");
        }

        int start = 0;
        int len = 0;
        for (Field field : allFields) {
            if (field == null) {
                continue;
            }

            CmdProp cmdProp = field.getAnnotation(CmdProp.class);
            if (cmdProp == null) {
                continue;
            }

            int idx = cmdProp.index();
            if (allFields.size() <= idx) {
                log.error("消息体参数个数小于{}，解析失败", idx);
                return null;
            } else {
                field.setAccessible(true);
                try {
                    len = cmdProp.len();
                    byte[] src = ToolConvert.getSource(msgBodyData, start, len);
                    String codecMethod = cmdProp.deCodec();
                    Object val = codec(field, src, codecMethod, false, 0, 0);

                    field.set(body, val);
                    start += len;
                } catch (IllegalAccessException e) {
                    log.error("设置字段值失败: {}", field.getName());
                    return null;
                }
            }
        }

        return body;
    }



    private static List<Field> getAllFields(Class cls) {
        Field[] fields = cls.getDeclaredFields();
        Field[] sortedFields = new Field[fields.length];

        // 排序
        for (Field field : fields) {
            CmdProp cmdProp = field.getAnnotation(CmdProp.class);
            if (cmdProp == null) {
                continue;
            }
            int index = cmdProp.index();
            if (index >= 0 && index < fields.length) {
                sortedFields[index] = field;
            }
        }

        List<Field> allFields = new ArrayList<>(Arrays.asList(sortedFields));
        return allFields;
    }

    public static List<CmdPropParam> getSortedParams(MessageBody messageBody) {
        Class cls = messageBody.getClass();
        List<Field> allFields = getAllFields(cls);
        List<CmdPropParam> sParams = new ArrayList<>();
        for (Field field : allFields) {
            if (field == null) {
                continue;
            }

            field.setAccessible(true);
            CmdProp cmdProp = field.getAnnotation(CmdProp.class);
            if (cmdProp != null) {
                try {
                    Object src = field.get(messageBody);
                    String codecMethod = cmdProp.enCodec();

                    String hexStr = (String)codec(field, src, codecMethod, true, cmdProp.sort(), cmdProp.len());

                    sParams.add(new CmdPropParam(cmdProp.index(), cmdProp.defaultValue(), field, hexStr));

//                    log.info("[field] {} - {}", field.getName(), hexStr);

                } catch (IllegalAccessException e) {
                    log.error("属性{}访问异常，消息编码失败", field.getName(), e);
                    return null;
                }
            }
        }

        Collections.sort(sParams);
        return sParams;
    }

    /**
     * 属性值转换
     * @return
     */
    private static Object codec(Field field, Object src, String codecMethod, boolean hexFlag, int sort, int len) {
        Object val;

        // 字符串，无需转换
        if (Constants.ENCODER_TO_STR.equals(codecMethod)) {
            val = src.toString();
            return val;
        }

        // 根据编码方法进行转换
        try {
            if (hexFlag) {
                Method method = ToolConvert.class.getDeclaredMethod(codecMethod, Integer.class, Integer.class,
                        field.getType()); //方法名称，返回长度,原始数值
                val = (String)method.invoke(null, sort, len, src);//方法参数

            } else {
                Method method = ToolConvert.class.getDeclaredMethod(codecMethod, byte[].class);
                val = method.invoke(null, src);
            }

        } catch (NoSuchMethodException e) {
            log.error("未定义解码方法：{}，消息解析失败", codecMethod, e);
            return null;
        } catch (InvocationTargetException e) {
            log.error("调用解码方法{}发生异常，消息解析失败", codecMethod, e);
            return null;
        } catch (IllegalAccessException e) {
            log.error("设置字段值失败");
            return null;
        }

        return val;
    }

    /**
     * 和校验
     * @param data
     * @return
     */
    private byte sumCheck(byte[] data) {
        int length = data.length;
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += (data[i] & 0xff);
        }

        byte sumCheck = (byte)(sum & 0xff);

        return sumCheck;
    }
}
