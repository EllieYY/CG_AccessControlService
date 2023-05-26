package com.wimetro.cg.util;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @title: IdUtil
 * @author: Ellie
 * @date: 2023/03/20 10:48
 * @description:
 **/
@Component
public class IdUtil {
    /** 网络标识范围 */
    private final int MAX = 100000;
    private final int MIN = 100;
    private int netIdentifier;    // 设备搜索使用的网络标识

    /** 网络标识获取 */
    public int getNetIdentifier() {
        return netIdentifier;
    }
    public int newNetIdentifier() {
        netIdentifier = IdUtil.randomRangeInt(MIN, MAX);
        return netIdentifier;
    }


    public static int randomRangeInt(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

}
