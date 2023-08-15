package com.wimetro.cg.model.card;

import com.wimetro.cg.protocol.card.CardInfo;
import com.wimetro.cg.util.DateUtil;
import com.wimetro.cg.util.ToolConvert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @title: CardDbInfo
 * @author: Ellie
 * @date: 2023/06/01 11:08
 * @description: 卡信息查询结果
 **/
@Slf4j
@Data
public class CardDbInfo implements Comparable<CardDbInfo> {
    private String cardNo;      // 卡号
    private Date validDate;     // 有效期
    private int state;          // 卡状态
    private String pwd;         // 密码
    private int doorCount;   // 有效次数
    private int holiday;        // 节假日有效位,32组节假日是否生效
    private String deviceSn;   // 设备sn
    private List<CardDoorInfo> doorInfoList;    // 门信息

    // 转成报文所需的对象
    public CardInfo toOperation() {
        CardInfo result = new CardInfo();
        int iCardNo = Integer.valueOf(cardNo);
        result.setCardNo(iCardNo);
        result.setValidDate(DateUtil.dateFormat(validDate, "yyMMddHHmm"));
        result.setStatus(state);
        result.setPwd(pwd);
        result.setValidCount(doorCount);
        result.setHoliday(holiday);

        if (doorInfoList.isEmpty()) {
            result.setTimeTz(0);
            result.setExpAlvl(0);
            result.setInoutFlag(0);

            return result;
        }

        // 按端口（门编号）降序排序
        Collections.sort(doorInfoList);

        // 按门处理
        int expAlvl = 0;
        int inoutFlag = 0;
        int tzId = 0;
        String validFlag = "";
        int weight = 1;    // 按位计算权值
        for (CardDoorInfo doorInfo:doorInfoList) {
            int doorNo = doorInfo.getDoorNo();
            if (doorNo < 3) {
                tzId += (doorInfo.getTimeSetId() << ((3 - doorNo) * 8));
            } else {
                tzId += doorInfo.getTimeSetId();
            }
            inoutFlag += weight * doorInfo.getInOutFlag();

            // 有效性是从右至左，依次为1~4门
            validFlag += (doorInfo.isDoorValid() ? "1" : "0");
            expAlvl += weight * doorInfo.getSpecialPermssion();

            weight = weight * 2;
        }

        int doorValid = ToolConvert.binToInt(validFlag);
        expAlvl += ((doorValid & 0x0F) << 4);

        result.setInoutFlag(inoutFlag);
        result.setTimeTz(tzId);
        result.setExpAlvl(expAlvl);

        return result;
    }

    // 数据检测和修复
    public int dataCheck() {
        int size = doorInfoList.size();
        if (size > 4) {
            log.error("门数量超定义：{}", this.toString());
            return 1;
        } else {
            // 记录有效门编号
            List<Integer> validDoorNo = new ArrayList<>();
            for (CardDoorInfo doorInfo:doorInfoList) {
                int doorNo = doorInfo.getDoorNo();
                if (doorNo > 3 || doorNo < 0) {
                    log.error("门编号无效： {}", this.toString());
                    continue;
                }
                validDoorNo.add(doorNo);
            }

            // 编号重复判断
            List<Integer> distValidDoorNo = validDoorNo.stream().distinct().collect(Collectors.toList());
            if (distValidDoorNo.size() != validDoorNo.size()) {
                log.error("存在重复门编号: {}", this.toString());
                return 2;
            }

            // 数据填充
            for (int i = 0; i < 4; i++) {
                if (!validDoorNo.contains(i)) {
                    CardDoorInfo item = new CardDoorInfo();
                    item.setDoorNo(i);
                    item.setTimeSetId(0);
                    item.setSpecialPermssion(0);
                    item.setInOutFlag(0);
                    item.setDoorValid(false);
                    doorInfoList.add(item);
                }
            }
        }

        return 0;
    }


    // 卡号升序
    @Override
    public int compareTo(CardDbInfo o) {
        return Integer.valueOf(this.cardNo) - Integer.valueOf(o.cardNo);
    }
}
