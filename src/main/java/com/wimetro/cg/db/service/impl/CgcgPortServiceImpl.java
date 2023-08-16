package com.wimetro.cg.db.service.impl;

import com.wimetro.cg.db.entity.CgcgPort;
import com.wimetro.cg.db.mapper.CgcgPortMapper;
import com.wimetro.cg.db.service.CgcgPortService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimetro.cg.model.device.PortConfigInfo;
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
public class CgcgPortServiceImpl extends ServiceImpl<CgcgPortMapper, CgcgPort> implements CgcgPortService {
    public List<PortConfigInfo> getReaderBytes(String sn) {
        return this.baseMapper.selectReaderByteByDeviceSn(sn);
    }
}
