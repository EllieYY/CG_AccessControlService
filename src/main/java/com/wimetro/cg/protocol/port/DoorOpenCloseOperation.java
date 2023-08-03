package com.wimetro.cg.protocol.port;

import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

/**
 * @title: DoorOpenCloseOperation
 * @author: Ellie
 * @date: 2023/08/03 15:16
 * @description: 开关门操作
 **/
@Data
public class DoorOpenCloseOperation extends Operation {
    @CmdProp(index = 0, len = 1, deCodec = "bytesToInt")
    private int port1 = 0;

    @CmdProp(index = 1, len = 1, deCodec = "bytesToInt")
    private int port2 = 0;

    @CmdProp(index = 2, len = 1, deCodec = "bytesToInt")
    private int port3 = 0;

    @CmdProp(index = 3, len = 1, deCodec = "bytesToInt")
    private int port4 = 0;

}
