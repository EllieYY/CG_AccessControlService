package com.wimetro.cg.model.card;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @title: ScpCardInfo
 * @author: Ellie
 * @date: 2023/08/02 11:19
 * @description:
 **/
@Data
@AllArgsConstructor
public class ScpCardInfo {
    private String sn;
    private List<String> cardList;
}
