package com.zhouzihao.tool.sugar.config;

import com.zhouzihao.tool.sugar.interfcaces.Transmission;

import java.util.Map;

/**
 * 方法糖配置类(这里的实例化 要用单例模式？)
 * Created by zhouzihao on 2018/6/15.
 */
public class SugarConfig {

    /**
     * 项目转化列表
     */
    private Map<String,Transmission> TransmiMap;

    public Map<String, Transmission> getTransmiMap() {
        return TransmiMap;
    }

    public void setTransmiMap(Map<String, Transmission> transmiMap) {
        TransmiMap = transmiMap;
    }
}
