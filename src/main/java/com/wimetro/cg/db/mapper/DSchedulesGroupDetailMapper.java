package com.wimetro.cg.db.mapper;
import java.util.List;

import com.wimetro.cg.model.card.ScheduleGroupInfo;
import org.apache.ibatis.annotations.Param;

import com.wimetro.cg.db.entity.DSchedulesGroupDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ellie
 * @since 2023-05-23
 */
@Mapper
public interface DSchedulesGroupDetailMapper extends BaseMapper<DSchedulesGroupDetail> {
    List<ScheduleGroupInfo> selectAllBySchedulesGroupId(@Param("list") List<Integer> schedulesIdList);
}
