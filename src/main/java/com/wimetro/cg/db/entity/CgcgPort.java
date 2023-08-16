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
 * @since 2023-08-16
 */
@Getter
@Setter
@TableName("cgcg_port")
@ApiModel(value = "CgcgPort对象", description = "")
public class CgcgPort implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("all_id")
    private Integer allId;

    @TableField("device_type_id")
    private Integer deviceTypeId;

    @TableField("device_scope_id")
    private Integer deviceScopeId;

    @TableField("device_name")
    private String deviceName;

    @TableField("controller_id")
    private Integer controllerId;

    @TableField("port_no")
    private Integer portNo;

    @TableField("open_5_flag")
    private Integer open5Flag;

    @TableField("open_door_time")
    private Integer openDoorTime;

    @TableField("phy_relay_status")
    private Integer phyRelayStatus;

    @TableField("door_open_type")
    private Integer doorOpenType;

    @TableField("gate_magnet_open")
    private Integer gateMagnetOpen;

    @TableField("illegal_card_alarm")
    private Integer illegalCardAlarm;

    @TableField("gate_magnet_alarm")
    private Integer gateMagnetAlarm;

    @TableField("force_alarm")
    private Integer forceAlarm;

    @TableField("open_over_time")
    private Integer openOverTime;

    @TableField("black_card_alarm")
    private Integer blackCardAlarm;

    @TableField("reader_tamper_alarm")
    private Integer readerTamperAlarm;

    @TableField("door_lock")
    private Integer doorLock;

    @TableField("force_alarm_used_flag")
    private Integer forceAlarmUsedFlag;

    @TableField("force_alarm_model")
    private Integer forceAlarmModel;

    @TableField("force_alarm_password")
    private String forceAlarmPassword;

    @TableField("illegal_card_flag")
    private Integer illegalCardFlag;

    @TableField("check_in_out_state")
    private Integer checkInOutState;

    @TableField("server_id")
    private Integer serverId;

    @TableField("server_ip")
    private String serverIp;

    @TableField("con_status")
    private String conStatus;

    @TableField("kq_flag")
    private String kqFlag;

    @TableField("reader_byte")
    private Integer readerByte;

    @TableField("relay_type")
    private Integer relayType;

    @TableField("status")
    private String status;

    @TableField("open_direction")
    private String openDirection;

    @TableField("device_sn")
    private String deviceSn;


}
