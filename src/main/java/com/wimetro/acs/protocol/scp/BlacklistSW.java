package com.wimetro.acs.protocol.scp;

import com.wimetro.acs.protocol.message.OperationResult;
import com.wimetro.acs.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

/**
 * @title: BlacklistSW
 * @author: Ellie
 * @date: 2023/04/14 09:36
 * @description: 黑名单开关
 **/
@Data
public class BlacklistSW extends OperationResult {
    @CmdProp(index = 0, len = 1, deCodec = "bytesToInt")
    private int sw;
}
