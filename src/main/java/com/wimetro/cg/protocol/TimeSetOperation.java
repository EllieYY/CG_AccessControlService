package com.wimetro.cg.protocol;

import com.wimetro.cg.protocol.card.TimeSlotOfDay;
import com.wimetro.cg.protocol.message.Message;
import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: TimeSetOperation
 * @author: Ellie
 * @date: 2023/05/22 08:50
 * @description:
 **/
@Data
public class TimeSetOperation extends Operation {
    @CmdProp(index = 0, len = 1, enCodec = "intToHexStr")
    private int index;   // 序号

    @CmdProp(index = 0, len = 0xE0, enCodec = "listToHexStr")
    private List<String> timesetList;

    public TimeSetOperation(int index, List<TimeSlotOfDay> dayList) {
        this.index = index;

        this.timesetList = new ArrayList<>();
        for (TimeSlotOfDay day:dayList) {
            String msg = Message.encodeMsgBody(day);
            timesetList.add(msg);
        }
    }
}
