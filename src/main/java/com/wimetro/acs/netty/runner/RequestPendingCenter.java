package com.wimetro.acs.netty.runner;


import com.wimetro.acs.netty.handler.dispatcher.OperationResultFuture;
import com.wimetro.acs.protocol.message.DeviceMessage;
import com.wimetro.acs.protocol.message.MessageBody;
import com.wimetro.acs.protocol.message.OperationResult;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RequestPendingCenter {
    private static final Map<Integer, OperationResultFuture> map = new ConcurrentHashMap<>();

    public static OperationResultFuture add(int streamId){
        OperationResultFuture future = new OperationResultFuture();
        map.put(streamId, future);
        return future;
    }

    public static void set(int streamId, DeviceMessage operation){
        try {
            OperationResultFuture operationResultFuture = map.get(streamId);
            if (operationResultFuture != null) {
                operationResultFuture.setSuccess(operation);
            }
        } finally {
            map.remove(streamId);
        }
     }


}
