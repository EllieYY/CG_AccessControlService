package com.wimetro.acs.protocol.scp;

import com.wimetro.acs.protocol.message.OperationResult;
import com.wimetro.acs.util.ProtocolFiledUtil.CmdProp;
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
