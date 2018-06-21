package com.zhouzihao.tool.sugar.enums;

import com.zhouzihao.tool.sugar.core.SugarGird;
import com.zhouzihao.tool.sugar.interfcaces.Decomposer;

import java.util.Objects;

/**
 * 糖分解者
 * 处理表达式状态机
 * Created by zhouzihao on 2018/6/15.
 */
public enum SugarDecomposer implements Decomposer {

    Head{
        @Override
        public boolean process(SugarGird sugarGird) {
            if (sugarGird.getBuffer().remaining() < 5){
                sugarGird.setDecomposer(Bad);
                return true;
            }
            //read header
            byte[] headeByte = new byte[5];
            sugarGird.getBuffer().get(headeByte);
            String headString = new String(headeByte);
            if ( "sugar".equals(headString) || "Sugar".equals(headString)){
                sugarGird.setHead(headString);
                sugarGird.setDecomposer(ParamStrar);
                return true;
            }
            sugarGird.setDecomposer(Bad);
            return true;
        }
    },
    ParamStrar{//参数开始
        @Override
        public boolean process(SugarGird sugarGird) {
            if (sugarGird.getBuffer().remaining() < 1){
                sugarGird.setDecomposer(Bad);
                return true;
            }
            byte[] test = new byte[1];
            sugarGird.getBuffer().get(test);
            Character startChar = (char)test[0];
//            Character startChar = sugarGird.getBuffer().getChar();
            if(new Character('\b').equals(startChar)){
                //这里状态没有变不用设置
                return true;
            }
            if(new Character('(').equals(startChar)){
                sugarGird.setDecomposer(Service);
                return true;
            }
            return false;
        }
    },
    Service{//服务名开始
        @Override
        public boolean process(SugarGird sugarGird) {
            if (sugarGird.getBuffer().remaining() < 1){
                sugarGird.setDecomposer(Bad);
                return true;
            }
            byte[] test = new byte[1];
            sugarGird.getBuffer().get(test);
            Character serviceChar = (char)test[0];
//            Character serviceChar = sugarGird.getBuffer().getChar(1);
            //if is #  inter method
            if (new Character('#').equals(serviceChar)){
                sugarGird.setDecomposer(Methode);
                return true;
            }
            if (Objects.isNull(sugarGird.getServiceName())){
                sugarGird.setServiceName(String.valueOf(serviceChar));
            }else {
                sugarGird.setServiceName(sugarGird.getServiceName() + String.valueOf(serviceChar));
            }
            //这里状态没有变不用设置的
            return true;
        }
    },
    Methode{//
        @Override
        public boolean process(SugarGird sugarGird) {
            if (sugarGird.getBuffer().remaining() < 1){
                sugarGird.setDecomposer(Bad);
                return true;
            }
            byte[] test = new byte[1];
            sugarGird.getBuffer().get(test);
            Character serviceChar = (char)test[0];
//            Character serviceChar = sugarGird.getBuffer().getChar(1);
            //if is ,  inter params
            if (new Character(',').equals(serviceChar)){
                sugarGird.setSugarTag(sugarGird.getServiceName()
                        +"#"
                + sugarGird.getMethodeName());
                sugarGird.setDecomposer(Params);
                return true;
            }
            if (Objects.isNull(sugarGird.getMethodeName())){
                sugarGird.setMethodeName(String.valueOf(serviceChar));
            }else {
                sugarGird.setMethodeName(sugarGird.getMethodeName() + String.valueOf(serviceChar));
            }
            return true;
        }
    },
    Params{//传递参数
        @Override
        public boolean process(SugarGird sugarGird) {
            if (sugarGird.getBuffer().remaining() < 1){
                sugarGird.setDecomposer(Bad);
                return true;
            }
            byte[] test = new byte[1];
            sugarGird.getBuffer().get(test);
            Character paramchar = (char)test[0];
//            Character paramchar = sugarGird.getBuffer().getChar(1);
            //判断转义字符
            if (new Character('\\').equals(paramchar)){
                //转义字符不读这字符直接都下一个字符
                sugarGird.setDecomposer(Escape);
                return true;
            }
            if (new Character(',').equals(paramchar)){
                sugarGird.setDecomposer(End);
                //todo 返回函数
                return true;
            }
            if(new Character(')').equals(paramchar)){
                sugarGird.setDecomposer(ResultFunction);
                return true;
            }
            if (Objects.isNull(sugarGird.getParams())){
                sugarGird.setParams(String.valueOf(paramchar));
            }else {
                sugarGird.setParams(sugarGird.getParams() + String.valueOf(paramchar));
            }
            return true;
        }
    },
    Escape{//转义字符
        @Override
        public boolean process(SugarGird sugarGird) {
            byte[] test = new byte[1];
            sugarGird.getBuffer().get(test);
            Character paramchar = (char)test[0];
//            Character paramchar = sugarGird.getBuffer().getChar(1);
            if (Objects.isNull(sugarGird.getParams())){
                sugarGird.setParams(String.valueOf(paramchar));
            }else {
                sugarGird.setParams(sugarGird.getParams() + String.valueOf(paramchar));
            }
            sugarGird.setDecomposer(Params);
            return false;
        }
    },
    ResultFunction{//返回映射函数
        @Override
        public boolean process(SugarGird sugarGird) {
            //TODO 实现解析代码
            sugarGird.setDecomposer(ParamEnd);
            return true;
        }
    },
    ParamEnd{//参数结束
        @Override
        public boolean process(SugarGird sugarGird) {
            //暂时什么都不处理直接结束
            sugarGird.setDecomposer(End);
            return false;
        }
    },
    Bad{
        @Override
        public boolean process(SugarGird sugarGird) {
            //todo 处理异常
            return false;
        }
    },
    End{
        @Override
        public boolean process(SugarGird sugarGird) {
            //todo 处理异常
            return false;
        }
    };
}
