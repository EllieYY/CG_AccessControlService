package com.wimetro.cg.model.card;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @title: ScpTimeSetAddParam
 * @author: Ellie
 * @date: 2023/05/17 09:51
 * @description:
 **/
@Data
@ApiModel(value = "新增时间组参数")
public class ScpTimeSetAddParam {
    @ApiModelProperty(value = "控制器时间组对应列表")
    private List<ScpTimeSetInfo> list;
}
