package com.wimetro.cg.protocol.card;

import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: CardOperationInfo
 * @author: Ellie
 * @date: 2023/05/26 10:40
 * @description: 卡片批量操作信息，含起始序号和数量
 **/
@Data
public class CardBatchOperationInfo extends Operation {
    @CmdProp(index = 0, len = 4, enCodec = "intToHexStr", deCodec = "bytesToInt")
    private int startIndex;

    @CmdProp(index = 1, len = 4, enCodec = "intToHexStr", deCodec = "bytesToInt")
    private int number;

    @CmdProp(index = 2, enCodec = "listToHexStr", deCodec = "bytesToListCardStr", len_rely = "length")
    private List<String> cardList = new ArrayList<>();
}
