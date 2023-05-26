package com.wimetro.cg.common.device;

import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

/**
 * @title: RegisterOperation
 * @author: Ellie
 * @date: 2022/02/28 11:25
 * @description:
 **/
@Data
public class RegisterOperation extends Operation {
    @CmdProp(index = 1)
    private String context;

//    @Override
//    public OperationResult execute() {
//
//        CommonOperationResult response = new CommonOperationResult("");
//        return response;
//    }
}
