package com.wimetro.acs.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;

/**
 * @title: NetUtil
 * @author: Ellie
 * @date: 2023/03/31 15:14
 * @description:
 **/
@Component
@Slf4j
public class NetUtil {
    /** 获取本机ip */
    public static InetAddress getLocalHostExactAddress() {
        try {
            InetAddress candidateAddress = null;

            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface iface = networkInterfaces.nextElement();
                // 该网卡接口下的ip会有多个，也需要一个个的遍历，找到自己所需要的
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = inetAddrs.nextElement();
                    // 排除loopback回环类型地址（不管是IPv4还是IPv6 只要是回环地址都会返回true）
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了 就是我们要找的
                            // ~~~~~~~~~~~~~绝大部分情况下都会在此处返回你的ip地址值~~~~~~~~~~~~~
                            return inetAddr;
                        }

                        // 若不是site-local地址 那就记录下该地址当作候选
                        if (candidateAddress == null) {
                            candidateAddress = inetAddr;
                        }

                    }
                }
            }

            // 如果出去loopback回环地之外无其它地址了，那就回退到原始方案吧
            return candidateAddress == null ? InetAddress.getLocalHost() : candidateAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getLocalHostIp() {
        InetAddress localIp = getLocalHostExactAddress();
        return localIp.getHostAddress();
    }


    /**
     * @param ipAddress 待检测IP地址
     * @param timeout   检测超时时间
     * @return
     */
    public static Boolean ipDetection(String ipAddress, Integer timeout) {
        // 当返回值是true时，说明host是可用的，false则不可。
        boolean status = false;
        try {
            status = InetAddress.getByName(ipAddress).isReachable(timeout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * 通过socket检测ip:port是否能够通信
     *
     * @param ipAddress
     * @param port
     * @param timeout
     * @return
     */
    public static Boolean ipDetection(String ipAddress, Integer port, Integer timeout) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(InetAddress.getByName(ipAddress), port), timeout);
        } catch (IOException e) {
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    /**
     * @param ipAddress  待检测IP地址
     * @param port       待检测port
     * @param retryCount 重试次数
     * @param timeout    检测超时时间（超时应该在3钞以上）
     * @param detectionFlag   标志位 0检测IP  1检测IP:PORT
     * @return
     */
    public static Boolean retryIPDetection(String ipAddress, Integer port, Integer retryCount, Integer timeout, Integer detectionFlag) {
        // 当返回值是true时，说明host是可用的，false则不可。
        boolean status = false;
        Integer tryCount = 1;

        //重试机制
        while (tryCount <= retryCount && status == false) {
            if (detectionFlag.equals(0)) {
                status = ipDetection(ipAddress, timeout);
            } else {
                status = ipDetection(ipAddress, port, timeout);
            }
            if (status == false) {
                log.info("第[" + tryCount + "]次连接 " + ipAddress + ":" + port + " 失败！");
                tryCount++;
                continue;
            }
            log.info("连接 " + ipAddress + ":" + port + " 成功！");
            return true;
        }
        return false;
    }
}
