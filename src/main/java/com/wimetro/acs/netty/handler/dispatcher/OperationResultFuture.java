package com.wimetro.acs.netty.handler.dispatcher;

import com.wimetro.acs.protocol.message.DeviceMessage;
import com.wimetro.acs.protocol.message.MessageBody;
import com.wimetro.acs.protocol.message.OperationResult;
import io.netty.util.concurrent.DefaultPromise;

public class OperationResultFuture extends DefaultPromise<DeviceMessage> {
}
