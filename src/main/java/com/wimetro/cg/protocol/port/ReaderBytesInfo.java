package com.wimetro.cg.protocol.port;

import com.wimetro.cg.protocol.message.OperationResult;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

/**
 * @title: ReaderBytesInfo
 * @author: Ellie
 * @date: 2023/04/13 14:35
 * @description: 读卡器字节数
 **/
@Data
public class ReaderBytesInfo extends OperationResult {
    @CmdProp(index = 0, len = 1, deCodec = "bytesToInt")
    private int reader0Byte = 2;

    @CmdProp(index = 1, len = 1, deCodec = "bytesToInt")
    private int reader1Byte = 2;

    @CmdProp(index = 2, len = 1, deCodec = "bytesToInt")
    private int reader2Byte = 2;

    @CmdProp(index = 3, len = 1, deCodec = "bytesToInt")
    private int reader3Byte = 2;
}
