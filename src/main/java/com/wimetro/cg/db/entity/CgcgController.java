package com.wimetro.cg.db.entity;

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
 * @since 2023-08-16
 */
@Getter
@Setter
@TableName("cgcg_controller")
@ApiModel(value = "CgcgController对象", description = "")
public class CgcgController implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("all_id")
    private Integer allId;

    @TableField("device_type_id")
    private Integer deviceTypeId;

    @TableField("device_scope_id")
    private Integer deviceScopeId;

    @TableField("device_name")
    private String deviceName;

    @TableField("device_ip")
    private String deviceIp;

    @TableField("subnet_mask")
    private String subnetMask;

    @TableField("gateway")
    private String gateway;

    @TableField("device_port")
    private Integer devicePort;

    @TableField("device_mac")
    private String deviceMac;

    @TableField("device_sn")
    private String deviceSn;

    @TableField("conn_password")
    private String connPassword;

    @TableField("firmware_version")
    private String firmwareVersion;

    @TableField("fix_address")
    private String fixAddress;

    @TableField("product_type")
    private String productType;

    @TableField("record_save_mode")
    private Integer recordSaveMode;

    @TableField("fire_alarm_par")
    private Integer fireAlarmPar;

    @TableField("bandit_alarm_par")
    private Integer banditAlarmPar;

    @TableField("reader_interval")
    private Integer readerInterval;

    @TableField("data_check_type")
    private Integer dataCheckType;

    @TableField("buzzer_flag")
    private Integer buzzerFlag;

    @TableField("smoke_alarm_type")
    private Integer smokeAlarmType;

    @TableField("apb_type")
    private Integer apbType;

    @TableField("card_expiration_prompt_flag")
    private Integer cardExpirationPromptFlag;

    @TableField("card_count")
    private Integer cardCount;

    @TableField("sequence_card")
    private Integer sequenceCard;

    @TableField("card_count_online")
    private Integer cardCountOnline;

    @TableField("sequence_card_online")
    private Integer sequenceCardOnline;

    @TableField("balck_alarm_flag")
    private Integer balckAlarmFlag;

    @TableField("controller_demolish_flag")
    private Integer controllerDemolishFlag;

    @TableField("police_alarm_status")
    private Integer policeAlarmStatus;

    @TableField("steal_alarm_status")
    private Integer stealAlarmStatus;

    @TableField("fire_alarm_status")
    private Integer fireAlarmStatus;

    @TableField("smog_alarm_status")
    private Integer smogAlarmStatus;

    @TableField("controller_demolish_alarm_status")
    private Integer controllerDemolishAlarmStatus;

    @TableField("server_id")
    private Integer serverId;

    @TableField("server_ip")
    private String serverIp;

    @TableField("con_status")
    private String conStatus;

    @TableField("last_time")
    private LocalDateTime lastTime;

    @TableField("status")
    private String status;

    @TableField("device_operation_days")
    private Integer deviceOperationDays;

    @TableField("formatting_times")
    private Integer formattingTimes;

    @TableField("watchdog_reset_times")
    private Integer watchdogResetTimes;

    @TableField("power_supply_type")
    private Integer powerSupplyType;

    @TableField("device_temperature")
    private Double deviceTemperature;

    @TableField("poweron_time")
    private LocalDateTime poweronTime;

    @TableField("voltage")
    private Double voltage;

    @TableField("protocol_type")
    private Integer protocolType;

    @TableField("reg_flag")
    private Integer regFlag;


}
