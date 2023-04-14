package com.wimetro.acs.util.ProtocolFiledUtil;

import com.wimetro.acs.common.Constants;

import java.lang.annotation.*;

/**
 * @title: CmdProp
 * @author: Ellie
 * @date: 2023/02/16 15:10
 * @description:
 **/
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CmdProp {
    // 字段在消息体中的字节索引 - 必填
    int index();

    String defaultValue() default "0";

    // 数值转换需要
    int len() default 1;

    // 0表示末尾补齐，1 表示前面补齐
    int sort() default 1;

    String deCodec() default Constants.ENCODER_TO_STR;
    String enCodec() default Constants.ENCODER_TO_STR;


}
