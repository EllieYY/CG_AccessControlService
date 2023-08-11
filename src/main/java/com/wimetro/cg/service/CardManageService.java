package com.wimetro.cg.service;

import com.wimetro.cg.common.Constants;
import com.wimetro.cg.db.service.impl.DCgcgEmployeeDoorServiceImpl;
import com.wimetro.cg.model.card.CardDbInfo;
import com.wimetro.cg.model.card.CardDoorInfo;
import com.wimetro.cg.model.card.ScpCardInfo;
import com.wimetro.cg.model.device.CGDeviceTcpInfo;
import com.wimetro.cg.model.response.DeviceResopnseType;
import com.wimetro.cg.model.response.DeviceResponse;
import com.wimetro.cg.netty.runner.NettyTcpServer;
import com.wimetro.cg.protocol.NoBodyOperation;
import com.wimetro.cg.protocol.card.CardBatchOperationInfo;
import com.wimetro.cg.protocol.card.CardClearOperation;
import com.wimetro.cg.protocol.card.CardInfo;
import com.wimetro.cg.protocol.card.CardOperationInfo;
import com.wimetro.cg.protocol.message.Message;
import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.protocol.message.OperationType;
import com.wimetro.cg.util.StringUtil;
import com.wimetro.cg.util.ToolConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @title: CardManageService
 * @author: Ellie
 * @date: 2023/04/18 15:40
 * @description:
 **/
@Slf4j
@Service
public class CardManageService {
    private final NettyTcpServer tcpServer;
    private final DCgcgEmployeeDoorServiceImpl employeeDoorService;

    public CardManageService(NettyTcpServer tcpServer, DCgcgEmployeeDoorServiceImpl employeeDoorService) {
        this.tcpServer = tcpServer;
        this.employeeDoorService = employeeDoorService;
    }

    /** 单张卡片添加-存入非排序区
     */
    @Async
    public void cardAdd(List<String> cardList, String sn) {
//        // 按控制器做聚合
//        List<ScpCardInfo> scpList = employeeDoorService.getScpListByCards(cardList);
//        for (ScpCardInfo scpCardInfo:scpList) {
//            String sn = scpCardInfo.getSn();
//            List<String> scpCards = scpCardInfo.getCardList();

            //#1 根据卡号列表查找
            List<CardDbInfo> cardDbInfoList = employeeDoorService.getCardInfoList(cardList, sn);
            log.info("非排序区授权卡号信息 - {}", cardDbInfoList);

            sendCards(sn, cardDbInfoList, OperationType.CARD_ADD_UNSORT.getSettingCode(), 0);
//        }
    }

    // 授权卡信息发送
    private int sendCards(String sn, List<CardDbInfo> cardDbInfoList, int cardMsgCode, int index) {
        List<String> cardMsg = new ArrayList<>();
        for (CardDbInfo cardDbInfo: cardDbInfoList) {
            // 数据检查
            if (cardDbInfo.dataCheck() != 0) {
                continue;
            }

            //#2 转成报文所需的对象
            CardInfo cardInfo = cardDbInfo.toOperation();
            log.info("加卡信息： {}", cardInfo);

            // 对CardInfo进行编码
            if (cardInfo instanceof Operation) {
                String msg = Message.encodeMsgBody((Operation)cardInfo);
                cardMsg.add(msg);
            }
        }

        // operation组装
        if (cardMsgCode == OperationType.CARD_ADD_SORT.getSettingCode()) {
            CardBatchOperationInfo operation = new CardBatchOperationInfo();
            operation.setStartIndex(index);
            operation.setNumber(cardMsg.size());
            operation.setCardList(cardMsg);

            index += cardMsg.size();

            // 发送
            int responseCode = OperationType.CARD_ADD_SORT.getResponseCode();
            // TODO:授权失败结果查看
            DeviceResponse response = tcpServer.sendDeviceInfo(sn, operation, cardMsgCode, responseCode);
            if (response.getCode() == DeviceResopnseType.SUCCESS) {   // 成功读取到失败授权卡号
                log.info("[授权失败卡号] - {}", response.getResult());
            }
        } else {
            CardOperationInfo operation = new CardOperationInfo();
            operation.setLength(cardMsg.size());
            operation.setCardList(cardMsg);
            log.info("加卡 operation: {}", operation);

            // 发送
            int responseCode = OperationType.CARD_ADD_UNSORT.getResponseCode();
            // TODO:授权失败结果查看
            DeviceResponse response = tcpServer.sendDeviceInfo(sn, operation, cardMsgCode, responseCode);
            if (response.getCode() == DeviceResopnseType.SUCCESS) {   // 成功读取到失败授权卡号
                log.info("[授权失败卡号] - {}", response.getResult());
            }
        }

        return index;
    }

    /** 单张卡片删除-非排序区
     * 返回删除失败卡号列表
     */
    public List<ScpCardInfo> cardDelete(List<String> cardList, String sn) {
//        // 按控制器做聚合
//        List<ScpCardInfo> scpList = employeeDoorService.getScpListByCards(cardList);
//        log.info("控制器授权卡信息：{}", scpList);

        List<ScpCardInfo> failedCardList = new ArrayList<>();
//        for (ScpCardInfo scpCardInfo:scpList) {
//            String sn = scpCardInfo.getSn();
//            List<String> cards = scpCardInfo.getCardList();
            if (cardList.size() == 0) {
//                continue;
                return failedCardList;
            }

            // 卡号规整 - 9字节
            List<String> reCards = cardList.stream().collect(ArrayList::new, (list, item) -> {
                list.add(ToolConvert.intStrToHexStr(1, 9, item));
            }, ArrayList::addAll);

            log.info("[非排序区卡片删除] 卡号：{}", reCards);

            CardOperationInfo operation = new CardOperationInfo();
            operation.setLength(reCards.size());
            operation.setCardList(reCards);

            DeviceResopnseType retCode = tcpServer.deviceSetting(sn, operation, Constants.CODE_CARD_DELETE);
            if (retCode != DeviceResopnseType.SUCCESS) {
                failedCardList.add(new ScpCardInfo(sn, cardList));
            }
            log.info("[非排序区卡片删除] - {}:{}-{}", sn, retCode.getCode(), retCode.getMsg());
//        }

        return failedCardList;
    }

    /** 批量卡片添加-存入排序区
     */
    @Async
    public void cardListAdd(List<String> scpList, boolean clearAll) {
        for (String sn:scpList) {
            // 是否清除所有区域卡片
            if (clearAll) {
                cardClear(sn);
            }

            // 查找控制器所有卡片
            List<CardDbInfo> cardDbInfoList = employeeDoorService.getScpCardInfoList(sn);
            log.info("卡号信息 - {}", cardDbInfoList);
            if (cardDbInfoList.size() == 0) {
                continue;
            }

            // 开始写入
            NoBodyOperation noBodyOperation = new NoBodyOperation();
            DeviceResopnseType retSatrtCode = tcpServer.deviceSetting(sn, noBodyOperation, Constants.CODE_SORT_CARD_START);
            log.info("[排序区开始写卡] - {}:{}-{}", sn, retSatrtCode.getCode(), retSatrtCode.getMsg());
            if (retSatrtCode != DeviceResopnseType.SUCCESS) {
                continue;
            }

            // 分批写入
            int index = 0;
            List<List<CardDbInfo>> batchCardDbInfo =
                    StringUtil.fixedGrouping(cardDbInfoList, Constants.BATCH_CARD_DATA_COUNT);
            for (List<CardDbInfo> batchCard : batchCardDbInfo) {
                index = sendCards(sn, batchCard, OperationType.CARD_ADD_SORT.getSettingCode(), index);
            }

            // 终止写入
            DeviceResopnseType retStopCode = tcpServer.deviceSetting(sn, noBodyOperation, Constants.CODE_SORT_CARD_END);
            log.info("[排序区终止写卡] - {}:{}-{}", sn, retSatrtCode.getCode(), retSatrtCode.getMsg());
        }

    }



    /**
     * 卡片清除，清除所有区域 = 3
     * @param sn
     * @return
     */
//    @Async
    public DeviceResopnseType cardClear(String sn) {
        CardClearOperation operation = new CardClearOperation(3);
        DeviceResopnseType retCode = tcpServer.deviceSetting(sn, operation, Constants.CODE_CARD_CLEAR);
        log.info("[清空所有区域授权卡] - {}:{}-{}", sn, retCode.getCode(), retCode.getMsg());
        return retCode;
    }


    /**
     * 读取授权卡信息
     * @param sn
     * @return
     */
    public List<String> cardRead(String sn) {

        return new ArrayList<>();
    }


}
