package com.wimetro.cg.common.device;

import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import com.wimetro.cg.protocol.message.Operation;
import lombok.Data;

/**
 * @title: CommonOperation
 * @author: Ellie
 * @date: 2022/02/10 16:10
 * @description: 报文体部分透传
 **/
@Data
public class CommonOperationReSameBody extends Operation {
    @CmdProp(index = 1)
    private String context;

//    @Override
//    public OperationResult execute() {
//        CommonOperationResult response = new CommonOperationResult(context);
//        return response;
//    }
}
