package com.wimetro.cg.db.service.impl;

import com.wimetro.cg.db.entity.DCgcgEmployeeDoor;
import com.wimetro.cg.db.mapper.DCgcgEmployeeDoorMapper;
import com.wimetro.cg.db.service.DCgcgEmployeeDoorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimetro.cg.model.card.ScpTimeSetInfo;
import org.springframework.stereotype.Service;

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
}
