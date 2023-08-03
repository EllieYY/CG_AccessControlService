package com.wimetro.cg.controller;

import com.wimetro.cg.model.device.CGPortInfo;
import com.wimetro.cg.model.ScpListInfo;
import com.wimetro.cg.model.card.ScpTimeSetAddParam;
import com.wimetro.cg.model.device.CGDeviceTcpInfo;
import com.wimetro.cg.model.device.DeviceAddParam;
import com.wimetro.cg.model.device.DeviceBasicInfo;
import com.wimetro.cg.model.device.ParamRead;
import com.wimetro.cg.model.result.ResultBean;
import com.wimetro.cg.model.result.ResultBeanUtil;
import com.wimetro.cg.netty.runner.NettyUdpServer;
import com.wimetro.cg.protocol.TcpParamOperationResult;
import com.wimetro.cg.service.DeviceManageService;
import com.wimetro.cg.util.NetUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @title: DeviceConfig
 * @author: Ellie
 * @date: 2023/03/20 11:08
 * @description:
 **/
@RestController
@Slf4j
@RequestMapping("/cg/device/manage")
@Api(tags = "设备管理接口")
public class DeviceManageController {
    private final DeviceManageService deviceManageService;
    private final NettyUdpServer udpServer;

    public DeviceManageController(DeviceManageService deviceManageService, NettyUdpServer udpServer) {
        this.deviceManageService = deviceManageService;
        this.udpServer = udpServer;
    }


    @ApiOperation(value = "CG设备搜索")
    @RequestMapping(value = "/search", method = {RequestMethod.POST})
    public ResultBean<List<CGDeviceTcpInfo>> deviceSearchRequest() {
        List<CGDeviceTcpInfo> deviceList = deviceManageService.deviceSearch();
        return ResultBeanUtil.makeOkResp(deviceList);
    }

    @ApiOperation(value = "CG设备添加")
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public ResultBean<DeviceBasicInfo> deviceAdd(@RequestBody DeviceAddParam deviceAddParam) {
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

        DeviceBasicInfo deviceBasicInfo = deviceManageService.deviceAdd(sn, ip, mac);

        // TODO:
        return ResultBeanUtil.makeOkResp(deviceBasicInfo);
    }

    @ApiOperation(value = "C/G子设备信息查询")
    @RequestMapping(value = "/portInfo", method = {RequestMethod.POST})
    public ResultBean<List<CGPortInfo>> getPortInfo(@RequestBody ParamRead paramRead) {
        return ResultBeanUtil.makeOkResp(deviceManageService.getPortInfo(paramRead.getSn()));
    }

    @ApiOperation(value = "C/G参数读取Tcp参数")
    @RequestMapping(value = "/read/tcpParam", method = {RequestMethod.POST})
    public ResultBean<TcpParamOperationResult> tcpParamRead(@RequestBody ParamRead paramRead) {
        return ResultBeanUtil.makeOkResp(deviceManageService.getTcpParam(paramRead.getSn()));
    }


    @ApiOperation(value = "控制器时间组新增")
    @RequestMapping(value = "/timeSet/add", method = {RequestMethod.POST})
    public ResultBean<String> timesetAdd(@RequestBody ScpTimeSetAddParam timeSetInfo) {
        deviceManageService.timesetAdd(timeSetInfo);
        return ResultBeanUtil.makeOkResp();
    }

    @ApiOperation(value = "控制器时间组删除")
    @RequestMapping(value = "/timeSet/delete", method = {RequestMethod.POST})
    public ResultBean<String> timesetDelete(@RequestBody ScpListInfo scpList) {
        deviceManageService.timesetDelete(scpList.getSnList());
        return ResultBeanUtil.makeOkResp();
    }


    @ApiOperation(value = "测试")
    @RequestMapping(value = "/test/broadcast", method = {RequestMethod.POST})
    public ResultBean<List<CGDeviceTcpInfo>> broadcast() {

//        deviceManageService.tcpParamSetting("");


        return ResultBeanUtil.makeOkResp();
    }

}
