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

    // 门禁设备：根据设备类型查找门数量。 电梯设备不同
    public int getDoorCountByDeviceType(String deviceType) {
        int count = this.baseMapper.selectDoorCountByDeviceType(deviceType);
        count = count < 0 ? 0 : count;
        count = count > 4 ? 4 : count;
        return count;
    }
}
