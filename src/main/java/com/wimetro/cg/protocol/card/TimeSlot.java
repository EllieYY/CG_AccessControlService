package com.wimetro.cg.protocol.card;

import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.util.DateUtil;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @title: TimeSlot
 * @author: Ellie
 * @date: 2023/05/09 09:06
 * @description: 时间段
 **/
@Data
@NoArgsConstructor
public class TimeSlot extends Operation {
    @CmdProp(index = 0, len = 2, enCodec = "date2hmStr")
    private Date start;

    @CmdProp(index = 1, len = 2, enCodec = "date2hmStr")
    private Date end;

    public TimeSlot(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "start=" + DateUtil.dateFormat(start, "HH:mm") +
                ", end=" + DateUtil.dateFormat(end, "HH:mm") +
                '}';
    }
}

