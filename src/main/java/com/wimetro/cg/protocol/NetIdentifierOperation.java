package com.wimetro.cg.protocol;

import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: DeviceSearch
 * @author: Ellie
 * @date: 2023/03/22 16:01
 * @description:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NetIdentifierOperation extends Operation {
    @CmdProp(index = 0, len = 2, enCodec = "intToHexStr", deCodec = "bytesToInt")
    private int netIdentifier;
}
