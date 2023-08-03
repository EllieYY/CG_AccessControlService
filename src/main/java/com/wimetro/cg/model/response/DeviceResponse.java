package com.wimetro.cg.model.response;

import com.wimetro.cg.protocol.message.OperationResult;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @title: DeviceResponse
 * @author: Ellie
 * @date: 2023/08/03 09:07
 * @description: 设备响应
 **/
@Data
@AllArgsConstructor
public class DeviceResponse {
    /**
     * 0 - 正常
     * 其他，失败 —— result = null;
     */
    private DeviceResopnseType code;
    private OperationResult result;
}
