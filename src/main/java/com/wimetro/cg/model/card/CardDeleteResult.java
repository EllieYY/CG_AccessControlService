package com.wimetro.cg.model.card;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @title: CardDeleteResult
 * @author: Ellie
 * @date: 2023/08/03 10:30
 * @description: 卡片删除结果
 **/
@Data
@ApiModel(value = "卡片删除失败信息")
public class CardDeleteResult {
    @ApiModelProperty(value = "")
    private List<ScpCardInfo> failedCardList;
}
