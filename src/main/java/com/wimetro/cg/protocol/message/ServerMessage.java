package com.wimetro.cg.protocol.message;


/**
 * @title: ServerMessage
 * @author: Ellie
 * @date: 2023/03/10 14:15
 * @description:
 **/
public class ServerMessage extends Message<Operation> {

    @Override
    public Class getMessageBodyDecodeClass(int opcode) {
        return OperationType.fromOpCode(opcode).getOperationClazz();
    }

//    public int getResponseOperationCode(int opcode) {
//        return OperationType.fromOpCode(opcode).getReOpCode();
//    }

    public ServerMessage(){}

    public ServerMessage(MessageHeader messageHeader, Operation operation){
        this.setMessageHeader(messageHeader);
        this.setMessageBody(operation);
    }
}
