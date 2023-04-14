package com.wimetro.acs.protocol.port;

import com.wimetro.acs.protocol.message.OperationResult;
import com.wimetro.acs.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

/**
 * @title: RelayParam
 * @author: Ellie
 * @date: 2023/04/13 14:38
 * @description: 继电器输出模式
 **/
@Data
public class RelayOutMode extends OperationResult {
    @CmdProp(index = 0, len = 1, deCodec = "bytesToInt")
    private int mode0;

    @CmdProp(index = 1, len = 1, deCodec = "bytesToInt")
    private int mode1;

    @CmdProp(index = 2, len = 1, deCodec = "bytesToInt")
    private int mode2;

    @CmdProp(index = 3, len = 1, deCodec = "bytesToInt")
    private int mode3;
}
