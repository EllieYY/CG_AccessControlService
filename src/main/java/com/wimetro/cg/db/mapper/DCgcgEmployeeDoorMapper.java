package com.wimetro.cg.db.mapper;
import java.util.List;

import com.wimetro.cg.model.card.CardDbInfo;
import com.wimetro.cg.model.card.ScpCardInfo;
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

    // 根据卡号查找卡授权信息
    List<CardDbInfo> selectByCardNo(@Param("list") List<String> cardList);

    // 根据卡号找卡所在的控制器
    List<ScpCardInfo> selectDeviceSnByCardNo(@Param("list") List<String> cardList);

    // 根据控制器查找卡授权信息
    List<CardDbInfo> selectCardsByDeviceSn(@Param("deviceSn") String deviceSn);


}
