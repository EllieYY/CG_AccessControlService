package com.wimetro.cg.protocol.port;

import com.wimetro.cg.model.device.CGPortInfo;
import com.wimetro.cg.protocol.message.OperationResult;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @title: PortInfo
 * @author: Ellie
 * @date: 2023/04/11 15:57
 * @description:
 **/
@Data
public class PortInfo extends OperationResult {
    @CmdProp(index = 0, len = 1, deCodec = "bytesToInt")
    private int relayState0;

    @CmdProp(index = 1, len = 1, deCodec = "bytesToInt")
    private int relayState1;

    @CmdProp(index = 2, len = 1, deCodec = "bytesToInt")
    private int relayState2;

    @CmdProp(index = 3, len = 1, deCodec = "bytesToInt")
    private int relayState3;

    @CmdProp(index = 4, len = 1, deCodec = "bytesToInt")
    private int mode0;

    @CmdProp(index = 5, len = 1, deCodec = "bytesToInt")
    private int mode1;

    @CmdProp(index = 6, len = 1, deCodec = "bytesToInt")
    private int mode2;

    @CmdProp(index = 7, len = 1, deCodec = "bytesToInt")
    private int mode3;

    @CmdProp(index = 8, len = 1, deCodec = "bytesToInt")
    private int strikeState0;

    @CmdProp(index = 9, len = 1, deCodec = "bytesToInt")
    private int strikeState1;

    @CmdProp(index = 10, len = 1, deCodec = "bytesToInt")
    private int strikeState2;

    @CmdProp(index = 11, len = 1, deCodec = "bytesToInt")
    private int strikeState3;

    @CmdProp(index = 12, len = 1, deCodec = "bytesToInt")
    private int readerAlarm0;

    @CmdProp(index = 13, len = 1, deCodec = "bytesToInt")
    private int readerAlarm1;

    @CmdProp(index = 14, len = 1, deCodec = "bytesToInt")
    private int readerAlarm2;

    @CmdProp(index = 15, len = 1, deCodec = "bytesToInt")
    private int readerAlarm3;

    @CmdProp(index = 16, len = 1, deCodec = "bytesToInt")
    private int scpAlarm;

    @CmdProp(index = 17, len = 1, deCodec = "bytesToInt")
    private int relayLogicState0;
    @CmdProp(index = 18, len = 1, deCodec = "bytesToInt")
    private int relayLogicState1;
    @CmdProp(index = 19, len = 1, deCodec = "bytesToInt")
    private int relayLogicState2;
    @CmdProp(index = 20, len = 1, deCodec = "bytesToInt")
    private int relayLogicState3;
    @CmdProp(index = 21, len = 1, deCodec = "bytesToInt")
    private int relayLogicState4;
    @CmdProp(index = 22, len = 1, deCodec = "bytesToInt")
    private int relayLogicState5;
    @CmdProp(index = 23, len = 1, deCodec = "bytesToInt")
    private int relayLogicState6;
    @CmdProp(index = 24, len = 1, deCodec = "bytesToInt")
    private int relayLogicState7;

    @CmdProp(index = 25, deCodec = "bytesToInt")
    private int lockState0;

    @CmdProp(index = 26, deCodec = "bytesToInt")
    private int lockState1;

    @CmdProp(index = 27, deCodec = "bytesToInt")
    private int lockState2;

    @CmdProp(index = 28, deCodec = "bytesToInt")
    private int lockState3;

    @CmdProp(index = 29, deCodec = "bytesToInt")
    private int monitoredState;

    // 门内人数
    @CmdProp(index = 30, len = 20, deCodec = "bytesToHexStr")
    private String inDoorNum;

    @CmdProp(index = 50, deCodec = "bytesToInt")
    private int guardState;

    public List<CGPortInfo> toCGPortInfo(int doorCount, ReaderBytesInfo readerBytesInfo, RelayOutMode relayOutMode) {
        List<CGPortInfo> portInfoList = new ArrayList<>();
        if (doorCount > 0) {
            CGPortInfo port0 = new CGPortInfo();
            port0.setPort(0);
            port0.setRelayState(relayState0);
            port0.setMode(mode0);
            port0.setStrikeState(strikeState0);
            setReaderAlarm(readerAlarm0, port0);
            port0.setLockState(lockState0);

            if (Objects.nonNull(readerBytesInfo)) {
                port0.setReaderBytes(readerBytesInfo.getReader0Byte());
            }
            if (Objects.nonNull(relayOutMode)) {
                port0.setRelayMode(relayOutMode.getMode0());
            }

            portInfoList.add(port0);

        }

        if (doorCount > 1) {
            CGPortInfo port1 = new CGPortInfo();
            port1.setPort(1);
            port1.setRelayState(relayState1);
            port1.setMode(mode1);
            port1.setStrikeState(strikeState1);
            setReaderAlarm(readerAlarm1, port1);
            port1.setLockState(lockState1);

            if (Objects.nonNull(readerBytesInfo)) {
                port1.setReaderBytes(readerBytesInfo.getReader1Byte());
            }
            if (Objects.nonNull(relayOutMode)) {
                port1.setRelayMode(relayOutMode.getMode1());
            }

            portInfoList.add(port1);

        }

        if (doorCount > 2) {
            CGPortInfo port2 = new CGPortInfo();
            port2.setPort(2);
            port2.setRelayState(relayState2);
            port2.setMode(mode2);
            port2.setStrikeState(strikeState2);
            setReaderAlarm(readerAlarm2, port2);
            port2.setLockState(lockState2);

            if (Objects.nonNull(readerBytesInfo)) {
                port2.setReaderBytes(readerBytesInfo.getReader2Byte());
            }
            if (Objects.nonNull(relayOutMode)) {
                port2.setRelayMode(relayOutMode.getMode2());
            }

            portInfoList.add(port2);

        }

        if (doorCount > 3) {
            CGPortInfo port3 = new CGPortInfo();
            port3.setPort(3);
            port3.setRelayState(relayState3);
            port3.setMode(mode3);
            port3.setStrikeState(strikeState3);
            setReaderAlarm(readerAlarm3, port3);
            port3.setLockState(lockState3);

            if (Objects.nonNull(readerBytesInfo)) {
                port3.setReaderBytes(readerBytesInfo.getReader3Byte());
            }
            if (Objects.nonNull(relayOutMode)) {
                port3.setRelayMode(relayOutMode.getMode3());
            }

            portInfoList.add(port3);
        }

        return portInfoList;
    }

    private void setReaderAlarm(int alram, CGPortInfo port) {
        port.setInvalidCardAlarm((alram & 0x01));
        port.setStrikeAlarm((alram & 0x02));
        port.setForcedOpenAlarm((alram & 0x03));
        port.setHeldOpenAlarm((alram & 0x08));
        port.setBlacklistAlarm((alram & 0x10));
        port.setTamperReaderAlarm((alram & 0x20));
    }
}
