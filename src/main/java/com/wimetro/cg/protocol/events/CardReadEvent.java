package com.wimetro.cg.protocol.events;

import com.wimetro.cg.protocol.message.OperationResult;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

import java.util.Date;

/**
 * @title: CardReadEvent
 * @author: Ellie
 * @date: 2023/06/02 16:45
 * @description: 读卡信息
 **/
@Data
public class CardReadEvent extends OperationResult implements DeviceEvent {
    @CmdProp(index = 0, len = 9, deCodec = "bytesToHexStr")
    private String cardNo;

    @CmdProp(index = 1, len = 6, deCodec = "bytesToBcdDate")
    private Date time;

    @CmdProp(index = 2, len = 1, deCodec = "bytesToInt")
    private int doorNo;

    @CmdProp(index = 3, len = 1, deCodec = "bytesToInt")
    private int state;

    @Override
    public void sendMq(String sn) {

    }
}
