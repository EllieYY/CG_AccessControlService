package com.wimetro.cg.controller;

import com.wimetro.cg.model.ScpInfo;
import com.wimetro.cg.model.card.CardAdd;
import com.wimetro.cg.model.card.CardBatchAdd;
import com.wimetro.cg.model.result.ResultBean;
import com.wimetro.cg.model.result.ResultBeanUtil;
import com.wimetro.cg.service.CardManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: CardManagerController
 * @author: Ellie
 * @date: 2023/04/18 14:48
 * @description:
 **/
@RestController
@Slf4j
@RequestMapping("/cg/card/manage")
@Api(tags = "卡片管理接口")
public class CardManagerController {
    private final CardManageService cardManageService;

    public CardManagerController(CardManageService cardManageService) {
        this.cardManageService = cardManageService;
    }

    @ApiOperation(value = "非排序区授权--需要先进行数据操作")
    @RequestMapping(value = "/add/person", method = {RequestMethod.POST})
    public ResultBean<String> cardAdd(@RequestBody CardAdd card) {
        log.info("非排序区授权 - {}", card);
        cardManageService.cardAdd(card.getCardNoList(), card.getSn());
        return ResultBeanUtil.makeOkResp();
    }

    @ApiOperation(value = "非排序区卡片删除--不需要查数据库")
    @RequestMapping(value = "/delete/person", method = {RequestMethod.POST})
    public ResultBean<String> cardDelete(@RequestBody CardAdd card) {
        log.info("非排序区卡片删除 - {}", card);
        cardManageService.cardDelete(card.getCardNoList(), card.getSn());
        return ResultBeanUtil.makeOkResp();
    }

    @ApiOperation(value = "排序区授权-清空所有区域+授权--需要先进行数据操作")
    @RequestMapping(value = "/add/batch", method = {RequestMethod.POST})
    public ResultBean<String> cardListAdd(@RequestBody CardBatchAdd cardBatchAdd) {
        // 需清空所有区域
        log.info("排序区授权 - {}", cardBatchAdd);
        cardManageService.cardListAdd(cardBatchAdd.getSnList(), true);
        return ResultBeanUtil.makeOkResp();
    }




}
