package com.wimetro.acs.protocol.port;

import com.wimetro.acs.protocol.message.OperationResult;
import com.wimetro.acs.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

/**
 * @title: ForcedInfo
 * @author: Ellie
 * @date: 2023/04/13 14:29
 * @description: 胁迫报警信息
 **/
@Data
public class ForcedInfo extends OperationResult {
    @CmdProp(index = 0, len = 1, deCodec = "bytesToInt")
    private int port;

    @CmdProp(index = 1, len = 1, deCodec = "bytesToInt")
    private int valid;

    @CmdProp(index = 2, len = 4, deCodec = "bytesToHexStr")
    private String password;

    @CmdProp(index = 6, len = 1, deCodec = "bytesToInt")
    private int mode;
}
