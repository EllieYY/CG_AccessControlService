package com.wimetro.cg.common.keepalive;


import com.wimetro.cg.protocol.message.OperationResult;
import lombok.Data;

@Data
public class KeepaliveOperationResult extends OperationResult {

    private final long time;

}
