package com.wimetro.cg.model.card;

import lombok.Data;

/**
 * @title: CardDoorInfo
 * @author: Ellie
 * @date: 2023/08/01 11:15
 * @description: 数据库卡信息中门信息
 **/
@Data
public class CardDoorInfo implements Comparable<CardDoorInfo> {
    private int doorNo;             // 门号
    private int timeSetId;          // 时间组id
    private int specialPermssion;   // 特权
    private int inOutFlag;          // 出入标志
    private boolean doorValid = true;

    // 按端口降序
    @Override
    public int compareTo(CardDoorInfo o) {
        return o.doorNo - this.doorNo;
    }
}
