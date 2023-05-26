package com.wimetro.cg.protocol;

import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @title: TcpParam
 * @author: Ellie
 * @date: 2023/03/17 14:17
 * @description:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class TcpParamOperation extends Operation {
    @CmdProp(index = 0, len = 6, enCodec = "mac2hexStr")
    private String mac;

    @CmdProp(index = 1, len = 4, enCodec = "ipToHex")
    private String ip;

    @CmdProp(index = 2, len = 4, enCodec = "ipToHex")
    private String mask;

    @CmdProp(index = 3, len = 4, enCodec = "ipToHex")
    private String gateway;

    @CmdProp(index = 4, len = 4, enCodec = "ipToHex")
    private String dns;

    @CmdProp(index = 5, len = 4, enCodec = "ipToHex")
    private String altDns;

    @CmdProp(index = 6, len = 1, enCodec = "intToHexStr")
    private int tcpMode;

    @CmdProp(index = 7, len = 2, enCodec = "intToHexStr")
    private int tcpPort;

    @CmdProp(index = 8, len = 2, enCodec = "intToHexStr")
    private int udpPort;

    @CmdProp(index = 9, len = 2, enCodec = "intToHexStr")
    private int targetPort;

    @CmdProp(index = 10, len = 4, enCodec = "ipToHex")
    private String targetIp;

    @CmdProp(index = 11, len = 1, enCodec = "intToHexStr")
    private int autoIp;

    @CmdProp(index = 12, len = 99, enCodec = "strToHexStr")
    private String domain;

}
