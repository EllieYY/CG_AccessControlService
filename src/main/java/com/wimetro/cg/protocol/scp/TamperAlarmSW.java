package com.wimetro.cg.protocol.scp;

import com.wimetro.cg.protocol.message.OperationResult;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

/**
 * @title: TamperAlarmSW
 * @author: Ellie
 * @date: 2023/04/14 10:05
 * @description: 控制板防拆报警开关
 **/
@Data
public class TamperAlarmSW extends OperationResult {
    @CmdProp(index = 0, len = 1, deCodec = "bytesToInt")
    private int sw;
}
