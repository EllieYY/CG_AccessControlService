package com.wimetro.cg.controller;

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

    @ApiOperation(value = "C/G个人授权-非排序区")
    @RequestMapping(value = "/add/person", method = {RequestMethod.POST})
    public ResultBean<String> cardAdd(@RequestBody CardAdd card) {
        cardManageService.cardAdd(card.getCardNoList());
        return ResultBeanUtil.makeOkResp();
    }

    @ApiOperation(value = "C/G单张卡删除")
    @RequestMapping(value = "/delete/person", method = {RequestMethod.POST})
    public ResultBean<String> cardDelete(@RequestBody CardAdd card) {
        cardManageService.cardDelete(card.getCardNoList());
        return ResultBeanUtil.makeOkResp();
    }

    @ApiOperation(value = "C/G批量授权或删除-排序区")
    @RequestMapping(value = "/add/batch", method = {RequestMethod.POST})
    public ResultBean<String> cardListAdd(@RequestBody CardBatchAdd cardBatchAdd) {
        cardManageService.cardListAdd(cardBatchAdd.getSnList());
        return ResultBeanUtil.makeOkResp();
    }


}
