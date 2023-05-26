package com.wimetro.cg.protocol;

import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

/**
 * @title: NoBodyOperation
 * @author: Ellie
 * @date: 2023/04/06 16:26
 * @description: 请求体为端口号的命令
 **/
@Data
public class PortOperation extends Operation {
    @CmdProp(index = 0, len = 1, enCodec = "intToHexStr")
    private int port;
}
