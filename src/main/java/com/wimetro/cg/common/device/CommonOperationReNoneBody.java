package com.wimetro.cg.common.device;

import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;

/**
 * @title: CommonOperationWithoutBody
 * @author: Ellie
 * @date: 2022/03/01 11:04
 * @description: 响应部分不包含报文体
 **/
public class CommonOperationReNoneBody extends Operation {
    @CmdProp(index = 1)
    private String context;

//    @Override
//    public OperationResult execute() {
//        CommonOperationResult response = new CommonOperationResult("");
//        return response;
//    }
}
