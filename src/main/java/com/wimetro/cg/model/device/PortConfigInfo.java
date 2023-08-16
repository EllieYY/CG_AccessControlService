package com.wimetro.cg.model.device;

import lombok.Data;

/**
 * @title: PortConfigInfo
 * @author: Ellie
 * @date: 2023/08/16 14:52
 * @description: 端口配置参数
 **/
@Data
public class PortConfigInfo {
    private int portNo;
    private int readerByte;   // 读卡器字节数
}
