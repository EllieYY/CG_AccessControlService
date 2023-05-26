package com.wimetro.cg.protocol.card;

import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.protocol.message.OperationResult;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

import java.util.List;

/**
 * @title: TimeSetClear
 * @author: Ellie
 * @date: 2023/05/09 09:01
 * @description: 读取所有开门时段
 **/
@Data
public class TimeSet extends OperationResult {
    @CmdProp(index = 0, len = 2)
    private int index;

    @CmdProp(index = 0, len = 0xe0)
    private List<TimeSlotOfDay> dayList;
}
