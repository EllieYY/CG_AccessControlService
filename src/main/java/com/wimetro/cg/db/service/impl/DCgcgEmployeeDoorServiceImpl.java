package com.wimetro.cg.db.service.impl;

import com.wimetro.cg.db.entity.DCgcgEmployeeDoor;
import com.wimetro.cg.db.mapper.DCgcgEmployeeDoorMapper;
import com.wimetro.cg.db.service.DCgcgEmployeeDoorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimetro.cg.model.card.CardDbInfo;
import com.wimetro.cg.model.card.ScpCardInfo;
import com.wimetro.cg.model.card.ScpTimeSetInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ellie
 * @since 2023-05-26
 */
@Service
public class DCgcgEmployeeDoorServiceImpl extends ServiceImpl<DCgcgEmployeeDoorMapper, DCgcgEmployeeDoor> implements DCgcgEmployeeDoorService {
    public ScpTimeSetInfo getScpTimeSetList(String sn) {
        return this.baseMapper.selectSchedulesGroupIdByDeviceSn(sn);
    }

    // 查找卡授权信息
    public List<CardDbInfo> getCardInfoList(List<String> cardList) {
        if (cardList.isEmpty()) {
            return new ArrayList<>();
        }
        return this.baseMapper.selectByCardNo(cardList);
    }

    // 根据控制器和卡号全集找卡号子集
    public List<ScpCardInfo> getScpListByCards(List<String> cardList) {
        if (cardList.isEmpty()) {
            return new ArrayList<>();
        }
        return this.baseMapper.selectDeviceSnByCardNo(cardList);
    }

    // 查找控制器下所有授权卡信息
    public List<CardDbInfo> getScpCardInfoList(String sn) {

        return this.baseMapper.selectCardsByDeviceSn(sn);
    }

}
