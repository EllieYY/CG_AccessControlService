package com.wimetro.cg.protocol.message;

import com.wimetro.cg.protocol.TcpParamOperationResult;
import com.wimetro.cg.protocol.card.TimeSet;
import com.wimetro.cg.protocol.common.*;
import com.wimetro.cg.protocol.port.*;
import com.wimetro.cg.protocol.scp.*;

import java.util.function.Predicate;

/**
 * @title: OperationType
 * @author: Ellie
 * @date: 2022/02/10 14:03
 * @description:
 **/
public enum OperationType {
    DEVICE_SEARCH(0x31FE00, 0x01FE00, 0x00, TcpParamOperationResult.class),
    TCP_PARAM(0x310600, 0x010600, 0x010601, TcpParamOperationResult.class),

    /** 控制器信息查询 */
    DEVICE_VERSION(0x310800, 0x010800, 0x00, DeviceVersion.class),
    OPERATING_PARAM(0x310900, 0x010900, 0x00, OperatingParam.class),
    FUNCTION_PARAM(0x310AFF, 0x010AFF, 0x00, FuncationParam.class),

    RECORD_TYPE(0x310A81, 0x010A81, 0x010A01, RecodeSaveType.class),
    CARD_CAPACITY(0x370100, 0x070100,0x00, CardCapacity.class),

    BL_SWITCH(0x311201, 0x011201,0x011200, BlacklistSW.class),
    TAMPER_ALARM_SWITCH(0x311801, 0x011801,0x011800, TamperAlarmSW.class),

    /** 端口信息查询 */
    PORT_INFO(0x310E00, 0x010E00, 0x00, PortInfo.class),
    READER_BYTES(0x330100, 0x030100, 0x030101, ReaderBytesInfo.class),
    RELAY_OUT_INFO(0x330200, 0x030200, 0x030201, RelayOutMode.class),
    FORCED_PWD_INFO(0x330B01, 0x030B01, 0x030B00, ForcedInfo.class),
    HELD_ON_TIME(0x330800, 0x030800, 0x030801, HeldOnTime.class),
    HELD_ON_INFO(0x330F00, 0x030F00, 0x030F01, HeldOnInfo.class),

    /** 时间组信息 */
    TIME_SET(0x360200, 0x060200, 0x060300, TimeSet.class),



    /** common 预定义命令 */
    CONNECT_CONFIRM(0x192300, 0x00, 0x00, ConnectConfirm.class),
    CONNECT_TEST(0x192200, 0x00, 0x00, ConnectTest.class),
    RESP_OK(0x210100, 0x00, 0x00, OkMessage.class),
    RESP_PWD_ERR(0x210200, 0x00, 0x00, PwdErrorMessage.class),
    RESP_CHECK_ERR(0x210300, 0x00, 0x00, CheckErrorMessage.class),
    RESP_IP_ERR(0x210400, 0x00, 0x00, IpErrorMessage.class);

    private int opCode;
    private int readCode;
    private int settingCode;
    private Class<? extends OperationResult> operationClazz;

    OperationType(int opCode, int readCode, int setCode, Class<? extends OperationResult> operationClazz) {
        this.opCode = opCode;
        this.readCode = readCode;
        this.settingCode = setCode;
        this.operationClazz = operationClazz;
    }

    public int getOpCode(){
        return opCode;
    }

    public int getReadCode(){
        return readCode;
    }

    public int getSettingCode() {
        return settingCode;
    }

    public Class<? extends MessageBody> getOperationClazz() {
        return operationClazz;
    }

    public static OperationType fromOpCode(int type) {
        return getOperationType(requestType -> requestType.opCode == type);
    }

    public static OperationType fromOperation(OperationResult operation){
        return getOperationType(requestType -> requestType.operationClazz == operation.getClass());
    }

    private static OperationType getOperationType(Predicate<OperationType> predicate){
        OperationType[] values = values();
        for (OperationType operationType : values) {
            if(predicate.test(operationType)){
                return operationType;
            }
        }

        throw new AssertionError("no found type");
    }

    public static boolean isProtocolOpCode(int opCode) {
        OperationType[] values = values();
        for (OperationType operationType : values) {
            if(operationType.opCode == opCode){
                return true;
            }
        }
        return false;
    }
}
