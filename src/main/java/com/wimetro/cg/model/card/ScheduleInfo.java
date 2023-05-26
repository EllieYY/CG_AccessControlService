package com.wimetro.cg.model.card;

import lombok.Data;

import java.util.Date;

/**
 * @title: ScheduleInfo
 * @author: Ellie
 * @date: 2023/04/23 09:57
 * @description: 时间组信息
 **/
@Data
public class ScheduleInfo {
    private int schedules_id;
    private Date begin_time;
    private Date end_time;
    private int mon_flag;
    private int tues_flag;
    private int wed_flag;
    private int thu_flag;
    private int fri_flag;
    private int sat_flag;
    private int sun_flag;


    public int getDayIndex() {
        int index = 0;
        if (mon_flag > 0) {
            index += 0x01;
        }
        if (tues_flag > 0) {
            index += 0x02;
        }
        if (wed_flag > 0) {
            index += 0x04;
        }
        if (thu_flag > 0) {
            index += 0x08;
        }
        if (fri_flag > 0) {
            index += 0x10;
        }
        if (sat_flag > 0) {
            index += 0x20;
        }
        if (sun_flag > 0) {
            index += 0x40;
        }

        return index;
    }
}
