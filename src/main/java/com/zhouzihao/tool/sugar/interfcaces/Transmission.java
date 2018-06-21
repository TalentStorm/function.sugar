package com.zhouzihao.tool.sugar.interfcaces;

import java.util.function.Function;

/**
 * 变换配置项(变速器)
 * @param <Q>
 * @param <R>
 * @since 1.8
 * Created by zhouzihao on 2018/6/15.
 */
public interface Transmission<Q,R> {

    /**
     * 处理入参转化
     * @return
     */
    Function<Object,Q> dealRequestObject();

    /**
     * 处理出参转化
     * @return
     */
    Function<R,Object> dealResultObject();


    String serviceName();

    String methodName();

    /**
     * 返回调用方法的实例
     * @return
     */
    Object objectBean();
}
