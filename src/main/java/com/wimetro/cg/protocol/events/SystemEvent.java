package com.wimetro.cg.protocol.events;

import com.wimetro.cg.common.Constants;
import com.wimetro.cg.model.mq.MqMessage;
import com.wimetro.cg.protocol.message.OperationResult;
import com.wimetro.cg.service.QueueProducer;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

import java.util.Date;

/**
 * @title: CardReadEvent
 * @author: Ellie
 * @date: 2023/06/02 16:45
 * @description: 读卡信息
 **/
@Data
public class SystemEvent extends OperationResult implements DeviceEvent {
    @CmdProp(index = 0, len = 1, deCodec = "bytesToInt")
    private int state;

    @CmdProp(index = 1, len = 6, deCodec = "bytesToBcdDate")
    private Date time;

    @CmdProp(index = 2, len = 1, deCodec = "bytesToHexStr")
    private String backup;

    @Override
    public void sendMq(QueueProducer queueProducer, String sn) {
        MqMessage message = new MqMessage(Constants.EVENT_SYSTEM, sn, 0, time, state, "");
        queueProducer.sendEventMessage(message);
    }
}
