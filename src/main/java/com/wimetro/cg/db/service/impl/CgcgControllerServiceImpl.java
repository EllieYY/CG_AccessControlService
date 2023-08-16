package com.wimetro.cg.db.service.impl;

import com.wimetro.cg.db.entity.CgcgController;
import com.wimetro.cg.db.mapper.CgcgControllerMapper;
import com.wimetro.cg.db.service.CgcgControllerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimetro.cg.model.device.DeviceShadow;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ellie
 * @since 2023-08-16
 */
@Service
public class CgcgControllerServiceImpl extends ServiceImpl<CgcgControllerMapper, CgcgController> implements CgcgControllerService {

    // 查找认证设备信息
    public List<DeviceShadow> getValidDevice() {
        return this.baseMapper.selectValidDevice();
    }

}
