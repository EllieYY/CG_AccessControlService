package com.wimetro.cg.model.card;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @title: CardAdd
 * @author: Ellie
 * @date: 2023/04/24 09:55
 * @description:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "授权参数")
public class CardAdd {
    @ApiModelProperty(value = "卡号列表")
    private List<String> cardNoList;

    @ApiModelProperty(value = "设备sn")
    private String sn;
}
