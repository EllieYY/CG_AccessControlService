package com.wimetro.cg.protocol.card;

import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @title: TimeSlotOfDay
 * @author: Ellie
 * @date: 2023/05/09 10:00
 * @description: 一天八个时间段
 **/
@Data
public class TimeSlotOfDay extends Operation {
    @CmdProp(index = 0, len = 32, enCodec = "listObjectToHexStr")
    private List<TimeSlot> timeSlots = new ArrayList<>();

    public void add(TimeSlot timeSlot) {
        timeSlots.add(timeSlot);
    }

    public void fixedSize() {
        int size = timeSlots.size();
        if (size > 8) {
            timeSlots = timeSlots.subList(0, 8);
        } else {
            int emptySize = 8 - size;
            for (int i = 0; i < emptySize; i++) {
                Date now = new Date();
                add(new TimeSlot(now, now));
            }
        }

    }
}
