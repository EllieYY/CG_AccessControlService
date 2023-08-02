package com.wimetro.cg.protocol.card;

import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @title: CardInfo
 * @author: Ellie
 * @date: 2023/04/18 15:53
 * @description:
 **/
@Data
public class CardInfo extends Operation {
    /**
     *  身份证号    8byte
     *  二维码     5byte
     *  ic\id     3~4byte
     */
    @CmdProp(index = 0, len = 9, enCodec = "intToHexStr")
    private int cardNo;

    // BCD码
    @CmdProp(index = 1, len = 4, enCodec = "fullHex")
    private String pwd;

    // BCD码，2099-12-31 23:59:59 ==> 9912312359
    @CmdProp(index = 2, len = 5, enCodec = "fullHex")
    private String validDate;

    // 从左到右，对应1~4号门
    @CmdProp(index = 3, len = 4, enCodec = "intToHexStr")
    private int timeTz;

    // 0 - 无权限 65535-不受限制
    @CmdProp(index = 4, len = 2, enCodec = "intToHexStr")
    private int validCount;

    /** 权限+特权：从 右至左对 应1~4号门，权限-门的有效状态
     * bit   特权
     * 0~2   0 - 普通卡
     *       1 - 首卡
     *       2 - 常开
     *       3 - 巡更
     *       4 - 防盗设置卡
     *  3    节假日受限制
     */
    @CmdProp(index = 5, len = 1, enCodec = "intToHexStr")
    private int expAlvl;

    // 0 - 正常  1 - 挂失  2 - 黑名单
    @CmdProp(index = 6, len = 1, enCodec = "intToHexStr")
    private int status;

    // 从左到右对应32个节假日编号，1~32，启用1，不启用0
    @CmdProp(index = 7, len = 4, enCodec = "intToHexStr")
    private int holiday;

    /** 出入标志，从左至右，每两位代表一个门
     *  值     含义
     *  0、3   出入有效
     *  1      入有效
     *  2      出有效
     */
    @CmdProp(index = 8, len = 1, enCodec = "intToHexStr")
    private int inoutFlag;

    // 设备维护，填默认值即可
    @CmdProp(index = 9, len = 6, defaultValue = "FFFFFFFFFFFF")
    private String cardReadTime = "FFFFFFFFFFFF";

}
