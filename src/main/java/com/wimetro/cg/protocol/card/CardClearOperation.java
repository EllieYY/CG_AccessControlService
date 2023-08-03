package com.wimetro.cg.protocol.card;

import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: CardClearOperation
 * @author: Ellie
 * @date: 2023/08/03 14:20
 * @description: 卡片清除
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardClearOperation extends Operation {
    @CmdProp(index = 0, len = 1, enCodec = "intToHexStr")
    private int aeroCode;
}
