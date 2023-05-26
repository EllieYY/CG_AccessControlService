package com.wimetro.cg.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2023-05-23
 */
@Getter
@Setter
@TableName("d_schedules_group_detail")
@ApiModel(value = "DSchedulesGroupDetail对象", description = "")
public class DSchedulesGroupDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("schedules_group_id")
    private Integer schedulesGroupId;

    @TableField("schedules_id")
    private Integer schedulesId;

    @TableField("status")
    private String status;

    @TableField("remark")
    private String remark;


}
