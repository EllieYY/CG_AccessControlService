package com.wimetro.cg.service;

import com.wimetro.cg.model.device.DeviceShadow;
import com.wimetro.cg.protocol.TcpParamOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @title: DeviceCenter
 * @author: Ellie
 * @date: 2023/03/31 13:49
 * @description:
 **/
@Slf4j
@Service
public class DeviceCenter {
    /** 设备 sn:DeviceShadow */
    static private Map<String, DeviceShadow> deviceMap = new ConcurrentHashMap<>();

    /** 设备列表维护 */
    public static void addDevice(String sn, String pwd, TcpParamOperation operation, int netIdentifier) {
        if (deviceMap.containsKey(sn)) {
            // 更新netIdentifier
            DeviceShadow deviceShadow = deviceMap.get(sn);
            deviceShadow.setNetIdentifier(netIdentifier);
            deviceMap.put(sn, deviceShadow);
            log.info("[device map] add - 设备 {} 已存在", sn);
        } else {
            DeviceShadow deviceShadow = new DeviceShadow(sn, pwd, operation, false, netIdentifier);
            deviceMap.put(sn, deviceShadow);
            log.info("[device map] add - 设备 {} 已添加", sn);
        }
    }

    /**
     * 获取设备信息
     * @param sn
     * @return
     */
    public static DeviceShadow getDevice(String sn) {
        return deviceMap.get(sn);
    }

    public static void registerDevice(String sn, String pwd, TcpParamOperation operation) {
        if (deviceMap.containsKey(sn)) {
            DeviceShadow deviceShadow = deviceMap.get(sn);
            deviceShadow.setRegistered(true);
            deviceShadow.setTcpParam(operation);
            deviceMap.put(sn, deviceShadow);
        } else {
            DeviceShadow deviceShadow = new DeviceShadow(sn, pwd, operation, true, 0);
            deviceShadow.setTcpParam(operation);
            deviceMap.put(sn, deviceShadow);
        }
        log.info("[device map] register - 设备 {} 注册成功", sn);
    }

    public static void deleteDevice(String sn) {
        // TODO:连接断开

        if (deviceMap.containsKey(sn)) {
            deviceMap.remove(sn);
        }

        log.info("[device map] delete - {}", sn);

    }

    /**
     * 获取当前设备搜索结果，同时删除之前搜索出来但未被添加的设备
     * @param netIdentifier
     * @return
     */
    public static List<DeviceShadow> getUnRegistedDevice(int netIdentifier) {
        List<DeviceShadow> result = new ArrayList<>();
        Iterator<String> iterator = deviceMap.keySet().iterator();

        log.info("{}", deviceMap);

        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            DeviceShadow deviceItem = deviceMap.get(key);
            if (deviceItem.getNetIdentifier() == netIdentifier) {
                result.add(deviceItem);
            }
        }

        return result;
    }

}
