package com.wimetro.cg.db.mapper;
import java.util.List;

import com.wimetro.cg.model.device.PortConfigInfo;
import org.apache.ibatis.annotations.Param;

import com.wimetro.cg.db.entity.CgcgPort;
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
public interface CgcgPortMapper extends BaseMapper<CgcgPort> {
    List<PortConfigInfo> selectReaderByteByDeviceSn(@Param("deviceSn") String deviceSn);

}
