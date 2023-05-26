package com.wimetro.cg.protocol.scp;

import com.wimetro.cg.protocol.message.OperationResult;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

/**
 * @title: RecodeSaveType
 * @author: Ellie
 * @date: 2023/04/14 09:12
 * @description: 记录存储方式
 **/
@Data
public class RecodeSaveType extends OperationResult {
    @CmdProp(index = 0, len = 1, deCodec = "bytesToInt")
    private int type;
}
