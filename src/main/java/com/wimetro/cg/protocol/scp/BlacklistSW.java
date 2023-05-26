package com.wimetro.cg.protocol.scp;

import com.wimetro.cg.protocol.message.OperationResult;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
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
