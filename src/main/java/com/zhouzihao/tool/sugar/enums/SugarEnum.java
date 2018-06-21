package com.zhouzihao.tool.sugar.enums;

/**
 * 糖模式
 * Created by zhouzihao on 2018/6/15.
 */
public enum SugarEnum {

    /**
     * 模式一 使用预定义的方案
     */
    Sugar_Mode_1(1,"sugarMode1"),

    /**
     * 模式二 使用自定义模式
     */
    Sugar_Mode_2(2,"sugarMode2"),
    /**
     * 模式识别失败
     */
    Sugar_Mode_Bad(-1,"sugarModeBad");

    private Integer code;
    private String name;

    SugarEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
