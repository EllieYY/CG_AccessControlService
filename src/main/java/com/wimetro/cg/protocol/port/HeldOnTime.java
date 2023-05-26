package com.wimetro.cg.protocol.port;

import com.wimetro.cg.protocol.message.OperationResult;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

/**
 * @title: HeldOnTime
 * @author: Ellie
 * @date: 2023/04/23 09:58
 * @description: 开锁输出时长
 **/
@Data
public class HeldOnTime extends OperationResult {
    @CmdProp(index = 0, len = 1, deCodec = "bytesToInt")
    private int port;

    @CmdProp(index = 1, len = 2, deCodec = "bytesToInt")
    private int seconds;
}
