package com.wimetro.acs.common.keepalive;


import com.wimetro.acs.protocol.message.OperationResult;
import lombok.Data;

@Data
public class KeepaliveOperationResult extends OperationResult {

    private final long time;

}
