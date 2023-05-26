package com.wimetro.cg.model.card;

import com.wimetro.cg.protocol.card.TimeSlot;
import com.wimetro.cg.protocol.card.TimeSlotOfDay;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @title: ScheduleGroupInfo
 * @author: Ellie
 * @date: 2023/04/23 09:59
 * @description: 时间组info
 **/
@Data
public class ScheduleGroupInfo {
    private int groupId;

    private List<ScheduleInfo> scheduleList;

    // TODO:添加转换方式，转换成设备需要的格式

    /**
     * 设备需要的list，周一到周天，每天八个时间段
     * @return
     */
    public List<TimeSlotOfDay> toDeviceInfo() {
        int weekDays = 7;
        List<TimeSlotOfDay> weekList = new ArrayList<>(weekDays);
        for (int i = 0; i < weekDays; i++) {
            weekList.add(new TimeSlotOfDay());
        }

        for (ScheduleInfo info:scheduleList) {
            Date start = info.getBegin_time();
            Date end = info.getEnd_time();
            if (Objects.isNull(start) || Objects.isNull(end)) {
                continue;
            }

            int dayIndex = info.getDayIndex();
            for (int i = 0; i < weekDays; i++) {
                int check = (int)Math.pow(2, i);
                if ((dayIndex & check) > 0) {
                    TimeSlotOfDay day = weekList.get(i);
                    day.add(new TimeSlot(start, end));
                    weekList.set(i, day);
                }
            }
        }

        for (int index = 0; index < weekDays; index++) {
            TimeSlotOfDay day = weekList.get(index);
            day.fixedSize();
            weekList.set(index, day);
        }

        return weekList;
    }


}
