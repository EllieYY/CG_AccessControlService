package com.wimetro.cg.service;

import com.wimetro.cg.common.Constants;
import com.wimetro.cg.db.service.impl.DCgcgEmployeeDoorServiceImpl;
import com.wimetro.cg.model.card.CardDbInfo;
import com.wimetro.cg.model.card.CardDoorInfo;
import com.wimetro.cg.model.card.ScpCardInfo;
import com.wimetro.cg.model.device.CGDeviceTcpInfo;
import com.wimetro.cg.netty.runner.NettyTcpServer;
import com.wimetro.cg.protocol.NoBodyOperation;
import com.wimetro.cg.protocol.card.CardBatchOperationInfo;
import com.wimetro.cg.protocol.card.CardInfo;
import com.wimetro.cg.protocol.card.CardOperationInfo;
import com.wimetro.cg.protocol.message.Message;
import com.wimetro.cg.protocol.message.Operation;
import com.wimetro.cg.protocol.message.OperationType;
import com.wimetro.cg.util.StringUtil;
import com.wimetro.cg.util.ToolConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @title: CardManageService
 * @author: Ellie
 * @date: 2023/04/18 15:40
 * @description:
 **/
@Slf4j
@Service
public class CardManageService {
    private final NettyTcpServer tcpServer;
    private final DCgcgEmployeeDoorServiceImpl employeeDoorService;

    public CardManageService(NettyTcpServer tcpServer, DCgcgEmployeeDoorServiceImpl employeeDoorService) {
        this.tcpServer = tcpServer;
        this.employeeDoorService = employeeDoorService;
    }


    /** 单张卡片添加-存入非排序区
     */
    public void cardAdd(List<String> cardList) {
        // 按控制器做聚合
        List<ScpCardInfo> scpList = employeeDoorService.getScpListByCards(cardList);
        for (ScpCardInfo scpCardInfo:scpList) {
            String sn = scpCardInfo.getSn();
            List<String> scpCards = scpCardInfo.getCardList();

            //#1 根据卡号列表查找
            List<CardDbInfo> cardDbInfoList = employeeDoorService.getCardInfoList(scpCards);
            log.info("卡号信息 - {}", cardDbInfoList);

            sendCards(sn, cardDbInfoList, OperationType.CARD_ADD_UNSORT.getSettingCode(), 0);
        }
    }

    // 授权卡信息发送
    private int sendCards(String sn, List<CardDbInfo> cardDbInfoList, int cardMsgCode, int index) {
        List<String> cardMsg = new ArrayList<>();
        for (CardDbInfo cardDbInfo: cardDbInfoList) {
            // 数据检查
            if (cardDbInfo.dataCheck() != 0) {
                continue;
            }

            //#2 转成报文所需的对象
            CardInfo cardInfo = cardDbInfo.toOperation();
            log.info("加卡信息： {}", cardInfo);

            // 对CardInfo进行编码
            if (cardInfo instanceof Operation) {
                String msg = Message.encodeMsgBody((Operation)cardInfo);
                cardMsg.add(msg);
            }
        }

        // operation组装
        if (cardMsgCode == OperationType.CARD_ADD_SORT.getSettingCode()) {
            CardBatchOperationInfo operation = new CardBatchOperationInfo();
            operation.setStartIndex(index);
            operation.setNumber(cardMsg.size());
            operation.setCardList(cardMsg);

            index += cardMsg.size();
            // 发送
            tcpServer.sendDeviceInfo(sn, operation, cardMsgCode);
        } else {
            CardOperationInfo operation = new CardOperationInfo();
            operation.setLength(cardMsg.size());
            operation.setCardList(cardMsg);
            log.info("加卡 operation: {}", operation);

            // 发送
            tcpServer.sendDeviceInfo(sn, operation, cardMsgCode);
        }

        return index;
    }

    /** 单张卡片删除-非排序区
     */
    public void cardDelete(List<String> cardList) {
        // 按控制器做聚合
        List<ScpCardInfo> scpList = employeeDoorService.getScpListByCards(cardList);
        log.info("控制器授权卡信息：{}", scpList);

        for (ScpCardInfo scpCardInfo:scpList) {
            String sn = scpCardInfo.getSn();
            List<String> cards = scpCardInfo.getCardList();
            if (cards.size() == 0) {
                continue;
            }

            // 卡号规整 - 9字节
            List<String> reCards = cards.stream().collect(ArrayList::new, (list, item) -> {
                list.add(ToolConvert.fullHex(item, 9, 1));
            }, ArrayList::addAll);

            CardOperationInfo operation = new CardOperationInfo();
            operation.setLength(reCards.size());
            operation.setCardList(reCards);

            tcpServer.sendDeviceInfo(sn, operation, Constants.CODE_CARD_DELETE);
        }
    }

    /** 批量卡片添加-存入非排序区
     */
    public void cardListAdd(List<String> scpList) {
        for (String sn:scpList) {
            // 查找控制器所有卡片
            List<CardDbInfo> cardDbInfoList = employeeDoorService.getScpCardInfoList(sn);
            log.info("卡号信息 - {}", cardDbInfoList);

            // 开始写入
            NoBodyOperation noBodyOperation = new NoBodyOperation();
            tcpServer.sendDeviceInfo(sn, noBodyOperation, Constants.CODE_SORT_CARD_START);

            // 分批写入
            int index = 0;
            List<List<CardDbInfo>> batchCardDbInfo =
                    StringUtil.fixedGrouping(cardDbInfoList, Constants.BATCH_CARD_DATA_COUNT);
            for (List<CardDbInfo> batchCard : batchCardDbInfo) {
                index = sendCards(sn, batchCard, OperationType.CARD_ADD_SORT.getSettingCode(), index);
            }

            // 终止写入
            tcpServer.sendDeviceInfo(sn, noBodyOperation, Constants.CODE_SORT_CARD_END);
        }

    }

    /**
     * 读取授权卡信息
     * @param sn
     * @return
     */
    public List<String> cardRead(String sn) {


        return new ArrayList<>();
    }


}