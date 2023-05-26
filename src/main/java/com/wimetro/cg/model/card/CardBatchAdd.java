package com.wimetro.cg.model.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @title: CardAdd
 * @author: Ellie
 * @date: 2023/04/18 14:50
 * @description:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "批量加卡参数")
public class CardBatchAdd {
    @ApiModelProperty(value = "控制器sn列表")
    private List<String> snList;
}
