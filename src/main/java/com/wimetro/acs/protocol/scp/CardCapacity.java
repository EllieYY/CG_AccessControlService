package com.wimetro.acs.protocol.scp;

import com.wimetro.acs.protocol.message.OperationResult;
import com.wimetro.acs.util.ProtocolFiledUtil.CmdProp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @title: CardCapacity
 * @author: Ellie
 * @date: 2023/04/10 09:49
 * @description:
 **/
@ApiModel(value = "卡容量信息")
@Data
public class CardCapacity extends OperationResult {
    @ApiModelProperty(value = "排序区卡容量总数")
    @CmdProp(index = 0, len = 4, deCodec = "bytesToLong")
    private Long sortedCardTotal;

    @ApiModelProperty(value = "排序区已使用卡容量")
    @CmdProp(index = 1, len = 4, deCodec = "bytesToLong")
    private Long sortedCardUsed;

    @ApiModelProperty(value = "非排序区卡容量总数")
    @CmdProp(index = 2, len = 4, deCodec = "bytesToLong")
    private Long unsortedCardTotal;

    @ApiModelProperty(value = "非排序区已使用卡容量")
    @CmdProp(index = 3, len = 4, deCodec = "bytesToLong")
    private Long unsortedCardUsed;
}
