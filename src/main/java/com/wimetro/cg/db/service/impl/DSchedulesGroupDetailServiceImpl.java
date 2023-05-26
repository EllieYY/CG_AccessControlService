package com.wimetro.cg.db.service.impl;

import com.wimetro.cg.db.entity.DSchedulesGroupDetail;
import com.wimetro.cg.db.mapper.DSchedulesGroupDetailMapper;
import com.wimetro.cg.db.service.DSchedulesGroupDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimetro.cg.model.card.ScheduleGroupInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ellie
 * @since 2023-05-23
 */
@Service
public class DSchedulesGroupDetailServiceImpl extends ServiceImpl<DSchedulesGroupDetailMapper, DSchedulesGroupDetail> implements DSchedulesGroupDetailService {
    public List<ScheduleGroupInfo> getScheduleGroupInfo(List<Integer> groupIdList) {
        return this.baseMapper.selectAllBySchedulesGroupId(groupIdList);
    }
}
