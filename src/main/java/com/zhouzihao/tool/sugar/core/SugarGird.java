package com.zhouzihao.tool.sugar.core;

import com.zhouzihao.tool.sugar.enums.SugarEnum;
import com.zhouzihao.tool.sugar.interfcaces.Decomposer;

import java.nio.ByteBuffer;

import static com.zhouzihao.tool.sugar.enums.SugarDecomposer.Head;

/**
 * 糖格(exp解析)
 * Created by zhouzihao on 2018/6/15.
 */
public class SugarGird {

    private ByteBuffer buffer;

    private Decomposer decomposer;

    private String head;

    private String sugarTag;

    private String serviceName;

    private String methodeName;

    private String params;

    private String functionResult;

    private SugarEnum mode = SugarEnum.Sugar_Mode_1;

    public SugarGird(String exps) {
        //todo 去掉两边的空格
        buffer = ByteBuffer.allocate(exps.length()).put(exps.getBytes());
        //设置buffer为初始状态
        buffer.clear();
        decomposer = Head;
        //这里分析处理 使用状态机
        while (decomposer.process(this));
    }

    //todo 手动创建表达式
    private SugarGird(){}

    public static SugarGird SugarGirdBuilder(){
        return new SugarGird();
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public Decomposer getDecomposer() {
        return decomposer;
    }

    public void setDecomposer(Decomposer decomposer) {
        this.decomposer = decomposer;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getSugarTag() {
        return sugarTag;
    }

    public void setSugarTag(String sugarTag) {
        this.sugarTag = sugarTag;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getFunctionResult() {
        return functionResult;
    }

    public void setFunctionResult(String functionResult) {
        this.functionResult = functionResult;
    }

    public SugarEnum getMode() {
        return mode;
    }

    public void setMode(SugarEnum mode) {
        this.mode = mode;
    }

    public String getMethodeName() {
        return methodeName;
    }

    public void setMethodeName(String methodeName) {
        this.methodeName = methodeName;
    }
}
