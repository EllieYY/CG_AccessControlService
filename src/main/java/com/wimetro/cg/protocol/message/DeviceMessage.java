package com.wimetro.cg.protocol.message;


/**
 * @title: DeviceRequestMessage
 * @author: Ellie
 * @date: 2023/03/10 14:15
 * @description:
 **/
public class DeviceMessage extends Message<OperationResult> {

    @Override
    public Class getMessageBodyDecodeClass(int opcode) {
        return OperationType.fromOpCode(opcode).getOperationClazz();
    }

//    public int getResponseOperationCode(int opcode) {
//        return OperationType.fromOpCode(opcode).getReOpCode();
//    }

    public DeviceMessage(){}

    public DeviceMessage(int opCode, OperationResult operation){
        MessageHeader messageHeader = new MessageHeader();
//        messageHeader.setMsgType(opCode);
        this.setMessageHeader(messageHeader);
        this.setMessageBody(operation);
    }
}
