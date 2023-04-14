package com.wimetro.acs.protocol.scp;

import com.wimetro.acs.protocol.message.OperationResult;
import com.wimetro.acs.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

/**
 * @title: DeviceVersion
 * @author: Ellie
 * @date: 2023/04/14 09:32
 * @description:
 **/
@Data
public class DeviceVersion extends OperationResult {
    @CmdProp(index = 0, len = 2, deCodec = "bytesToStr")
    private String version;

    @CmdProp(index = 1, len = 2, deCodec = "bytesToStr")
    private String version2;
}
