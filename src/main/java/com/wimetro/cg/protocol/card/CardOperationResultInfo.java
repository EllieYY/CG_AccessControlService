package com.wimetro.cg.protocol.card;

import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.protocol.message.OperationResult;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

import java.util.List;

/**
 * @title: CardOperationInfo
 * @author: Ellie
 * @date: 2023/05/26 10:40
 * @description: 卡片操作信息:删除和失败返回
 **/
@Data
public class CardOperationResultInfo extends OperationResult {
    @CmdProp(index = 0, len = 4, enCodec = "intToHexStr", deCodec = "bytesToInt")
    private int length;

    @CmdProp(index = 1, len = 9, enCodec = "listToHexStr", deCodec = "bytesToListCardStr", len_rely = "length")
    private List<String> cardList;
}
