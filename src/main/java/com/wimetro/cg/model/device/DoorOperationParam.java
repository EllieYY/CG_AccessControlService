package com.wimetro.cg.model.device;

import com.wimetro.cg.protocol.port.DoorOpenCloseOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @title: DoorOperation
 * @author: Ellie
 * @date: 2023/08/03 15:06
 * @description: 开关门
 **/
@Data
@ApiModel(value = "开关门参数")
public class DoorOperationParam {
    @ApiModelProperty(value = "控制器编号")
    private String sn;

    @ApiModelProperty(value = "要操作的端口号列表，端口号1~4")
    private List<Integer> doorList;

    public DoorOpenCloseOperation toOperation() {
        DoorOpenCloseOperation operation = new DoorOpenCloseOperation();
        for (Integer doorNo:doorList) {
            switch (doorNo) {
                case 1:
                    operation.setPort1(1);
                    break;
                case 2:
                    operation.setPort2(1);
                    break;
                case 3:
                    operation.setPort3(1);
                    break;
                case 4:
                    operation.setPort4(1);
                    break;
                default:
                    break;
            }
        }

        return operation;
    }
}
