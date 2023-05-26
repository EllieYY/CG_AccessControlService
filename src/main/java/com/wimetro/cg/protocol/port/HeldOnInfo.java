package com.wimetro.cg.protocol.port;

import com.wimetro.cg.protocol.message.OperationResult;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

/**
 * @title: HeldOnInfo
 * @author: Ellie
 * @date: 2023/04/23 15:35
 * @description: 出门开关参数
 **/
@Data
public class HeldOnInfo extends OperationResult {
    @CmdProp(index = 0, len = 1, deCodec = "bytesToInt")
    private int port;

    @CmdProp(index = 1, len = 1, deCodec = "bytesToInt")
    private int valid;

    @CmdProp(index = 2, len = 1, deCodec = "bytesToInt")
    private int mode5seconds;   // 允许常开默认关闭

    @CmdProp(index = 3, len = 224, deCodec = "bytesToHexStr")
    private String timeInfo;
}
