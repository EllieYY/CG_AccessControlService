package com.wimetro.cg.db.mapper;
import java.util.List;

import com.wimetro.cg.model.card.ScpTimeSetInfo;
import org.apache.ibatis.annotations.Param;

import com.wimetro.cg.db.entity.DCgcgEmployeeDoor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ellie
 * @since 2023-05-26
 */
@Mapper
public interface DCgcgEmployeeDoorMapper extends BaseMapper<DCgcgEmployeeDoor> {
    ScpTimeSetInfo selectSchedulesGroupIdByDeviceSn(@Param("deviceSn") String deviceSn);
}
