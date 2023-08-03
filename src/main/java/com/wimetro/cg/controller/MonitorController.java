package com.wimetro.cg.controller;

import com.wimetro.cg.model.ScpInfo;
import com.wimetro.cg.model.ScpListInfo;
import com.wimetro.cg.model.card.ScpTimeSetAddParam;
import com.wimetro.cg.model.device.DeviceAddParam;
import com.wimetro.cg.model.device.DoorOperationParam;
import com.wimetro.cg.model.device.ParamRead;
import com.wimetro.cg.model.response.DeviceResopnseType;
import com.wimetro.cg.model.result.ResultBean;
import com.wimetro.cg.model.result.ResultBeanUtil;
import com.wimetro.cg.service.CardManageService;
import com.wimetro.cg.service.DeviceManageService;
import com.wimetro.cg.util.NetUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @title:
 * @author: Ellie
 * @date: 2023/03/20 11:08
 * @description: 监控页面接口
 **/
@RestController
@Slf4j
@RequestMapping("/cg/device/monitor")
@Api(tags = "设备监控接口")
public class MonitorController {
    private final DeviceManageService deviceManageService;
    private final CardManageService cardManageService;
    public MonitorController(DeviceManageService deviceManageService, CardManageService cardManageService) {
        this.deviceManageService = deviceManageService;
        this.cardManageService = cardManageService;
    }


    @ApiOperation(value = "启动-重发连接参数")
    @RequestMapping(value = "/connect", method = {RequestMethod.POST})
    public ResultBean<String> deviceConnect(@RequestBody DeviceAddParam deviceAddParam) {
        String ip = deviceAddParam.getIp();
        String sn = deviceAddParam.getSn();
        String mac = deviceAddParam.getMac();
        // 连通性测试
        if (!NetUtil.ipDetection(ip, 3)) {
            log.error("ping不通");
            return ResultBeanUtil.makeResp(9001, "设备ping不通:" + ip);
        }

        // udp设置TcpParam，检测设置成功
        if (!deviceManageService.tcpParamSetting(ip, mac, sn)) {
            log.error("[tcp参数设置失败]");
            return ResultBeanUtil.makeResp(9002, "tcp参数设置失败:" + ip);
        }

        return ResultBeanUtil.makeOkResp();
    }

    @ApiOperation(value = "初始化")
    @RequestMapping(value = "/init", method = {RequestMethod.POST})
    public ResultBean<String> deviceInit(@RequestBody ScpInfo scpInfo) {
        DeviceResopnseType result = deviceManageService.deviceInit(scpInfo.getSn());
        return ResultBeanUtil.makeResp(result.getCode(), result.getMsg());
    }

    @ApiOperation(value = "参数下载")
    @RequestMapping(value = "/setting", method = {RequestMethod.POST})
    public ResultBean<String> deviceSetting(@RequestBody ScpInfo scpInfo) {
        deviceManageService.deviceInit(scpInfo.getSn());
        return ResultBeanUtil.makeOkResp("命令已下发");
    }

    @ApiOperation(value = "取消卡片")
    @RequestMapping(value = "/card/clear", method = {RequestMethod.POST})
    public ResultBean<String> cardClear(@RequestBody ScpInfo scpInfo) {
        DeviceResopnseType result = cardManageService.cardClear(scpInfo.getSn());
        return ResultBeanUtil.makeResp(result.getCode(), result.getMsg());
    }


    @ApiOperation(value = "下载卡片")
    @RequestMapping(value = "/card/add", method = {RequestMethod.POST})
    public ResultBean<String> cardAdd(@RequestBody ScpInfo scpInfo) {

        cardManageService.cardListAdd(Arrays.asList(scpInfo.getSn()));

        return ResultBeanUtil.makeOkResp("命令下发中");
    }

    @ApiOperation(value = "提取事件")
    @RequestMapping(value = "/events/extract", method = {RequestMethod.POST})
    public ResultBean<String> extractEvents(@RequestBody ScpInfo scpInfo) {
        // TODO:
        return ResultBeanUtil.makeCustomErrResp("暂未实现");
    }

    @ApiOperation(value = "开门")
    @RequestMapping(value = "/door/open", method = {RequestMethod.POST})
    public ResultBean<String> openDoor(@RequestBody DoorOperationParam param) {
        DeviceResopnseType result = deviceManageService.doorOperation(param, true);
        return ResultBeanUtil.makeResp(result.getCode(), result.getMsg());
    }

    @ApiOperation(value = "关门")
    @RequestMapping(value = "/door/close", method = {RequestMethod.POST})
    public ResultBean<String> closeDoor(@RequestBody DoorOperationParam param) {
        DeviceResopnseType result = deviceManageService.doorOperation(param, false);
        return ResultBeanUtil.makeResp(result.getCode(), result.getMsg());
    }
}
