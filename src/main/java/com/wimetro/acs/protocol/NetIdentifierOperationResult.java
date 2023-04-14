package com.wimetro.acs.protocol;

import com.wimetro.acs.protocol.message.Operation;
import com.wimetro.acs.protocol.message.OperationResult;
import com.wimetro.acs.util.ProtocolFiledUtil.CmdProp;
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
public class NetIdentifierOperationResult extends OperationResult {
    @CmdProp(index = 0, len = 2, enCodec = "intToHexStr", deCodec = "bytesToInt")
    private int netIdentifier;
}
