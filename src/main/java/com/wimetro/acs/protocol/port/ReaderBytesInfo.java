package com.wimetro.acs.protocol.port;

import com.wimetro.acs.protocol.message.OperationResult;
import com.wimetro.acs.util.ProtocolFiledUtil.CmdProp;
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
    private int reader0Byte;

    @CmdProp(index = 1, len = 1, deCodec = "bytesToInt")
    private int reader1Byte;

    @CmdProp(index = 2, len = 1, deCodec = "bytesToInt")
    private int reader2Byte;

    @CmdProp(index = 3, len = 1, deCodec = "bytesToInt")
    private int reader3Byte;
}
