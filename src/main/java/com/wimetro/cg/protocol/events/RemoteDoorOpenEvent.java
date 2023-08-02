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
public class RemoteDoorOpenEvent extends OperationResult {
    @CmdProp(index = 0, len = 1, deCodec = "bytesToInt")
    private int doorNo;

    @CmdProp(index = 1, len = 6, deCodec = "bytesToBcdDate")
    private Date time;

    @CmdProp(index = 2, len = 1, deCodec = "bytesToInt")
    private int state;
}
