package com.wimetro.cg.protocol.port;

import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

/**
 * @title: ReaderByteOperation
 * @author: Ellie
 * @date: 2023/08/16 15:05
 * @description: 读卡器字节数配置
 **/
@Data
public class ReaderByteOperation extends Operation {
    @CmdProp(index = 0, len = 1, enCodec = "intToHexStr", deCodec = "bytesToInt")
    private int port1 = 2;

    @CmdProp(index = 1, len = 1, enCodec = "intToHexStr", deCodec = "bytesToInt")
    private int port2 = 2;

    @CmdProp(index = 2, len = 1, enCodec = "intToHexStr", deCodec = "bytesToInt")
    private int port3 = 2;

    @CmdProp(index = 3, len = 1, enCodec = "intToHexStr", deCodec = "bytesToInt")
    private int port4 = 2;
}
