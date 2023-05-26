package com.wimetro.cg.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Ellie
 * @since 2023-05-26
 */
@Getter
@Setter
@TableName("d_cgcg_employee_door")
@ApiModel(value = "DCgcgEmployeeDoor对象", description = "")
public class DCgcgEmployeeDoor implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "auto_id", type = IdType.AUTO)
    private Integer autoId;

    @TableField("employee_no")
    private String employeeNo;

    @TableField("card_no")
    private String cardNo;

    @TableField("dept_id")
    private Integer deptId;

    @TableField("employee_group_id")
    private Integer employeeGroupId;

    @TableField("schedules_group_id")
    private Integer schedulesGroupId;

    @TableField("controller_id")
    private Integer controllerId;

    @TableField("door_no")
    private Integer doorNo;

    @TableField("in_out_flag")
    private Integer inOutFlag;

    @TableField("special_permssion")
    private Integer specialPermssion;

    @TableField("create_by")
    private String createBy;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_by")
    private String updateBy;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("device_sn")
    private String deviceSn;


}
