package com.wimetro.cg.db.mapper;
import java.util.List;

import com.wimetro.cg.model.device.DeviceShadow;
import org.apache.ibatis.annotations.Param;

import com.wimetro.cg.db.entity.CgcgController;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ellie
 * @since 2023-08-16
 */
@Mapper
public interface CgcgControllerMapper extends BaseMapper<CgcgController> {
    List<DeviceShadow> selectValidDevice();

    // 根据设备类型查找门数量
    int selectDoorCountByDeviceType(@Param("deviceType") String deviceType);
}
