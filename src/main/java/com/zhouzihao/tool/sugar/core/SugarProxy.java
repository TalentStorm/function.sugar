package com.zhouzihao.tool.sugar.core;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhouzihao.tool.sugar.config.SugarConfig;
import com.zhouzihao.tool.sugar.enums.SugarEnum;
import com.zhouzihao.tool.sugar.interfcaces.Transmission;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;

import static com.zhouzihao.tool.sugar.enums.SugarEnum.Sugar_Mode_2;
import static com.zhouzihao.tool.sugar.enums.SugarEnum.Sugar_Mode_Bad;

/**
 * 方法代理糖(测试版)
 * Created by zhouzihao on 2018/6/15.
 */
public class SugarProxy implements ApplicationContextAware{

    private static Gson gson = new Gson();
    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * callMethod
     * @param service
     * @param method
     * @param params
     * @return
     */
    private Object callMethod(
            Object serviceBean,
            String service,
            String method,
            String params
            ){
        Object result = new Object();
        try {
            //先从缓存中取类
            Class<?> serviceClz = Class.forName(service);
            Method[] methods = serviceClz.getMethods();
            Method methodReflect = null;
            for (Method m : methods) {
                if (m.getName().equals(method)) {
                    methodReflect = m;
                    break;
                }
            }
            if (methodReflect == null) {
                System.out.print("methodReflect is null!");
                return result;
            }

            Object callReslut = null;

            Type[] paramTypes = methodReflect.getGenericParameterTypes();//支持泛型

            if (paramTypes.length == 0) {
                callReslut = methodReflect.invoke(serviceBean);
            } else if (paramTypes.length == 1) {
                Object inputParam = gson.fromJson(params, paramTypes[0]);
                callReslut = methodReflect.invoke(serviceBean, inputParam);
            } else {
                return result;
            }
            return callReslut;
        } catch (ClassNotFoundException e) {
//            logger.warn("bean not found!", e);
//            result.setSuccess(false);
//            result.setCode("2");
//            result.setDescription("service not found," + service);
        } catch (BeansException e) {
//            logger.warn("bean not found!", e);
//            result.setSuccess(false);
//            result.setCode("2");
//            result.setDescription("service not found," + service);
        } catch (IllegalAccessException e) {
//            logger.warn("call service error: ", e);
//            result.setSuccess(false);
//            result.setCode("5");
//            result.setDescription("call service error," + service);
        } catch (IllegalArgumentException e) {
//            logger.warn("call service error: ", e);
//            result.setSuccess(false);
//            result.setCode("5");
//            result.setDescription("call service error," + service);
        } catch (InvocationTargetException e) {
//            logger.warn("call service error: ", e);
//            result.setSuccess(false);
//            result.setCode("5");
//            result.setDescription("call service error," + service);
        } catch (JsonSyntaxException e) {
//            logger.warn("call service error: \r\n http input: " + service, e);
//            result.setSuccess(false);
//            result.setCode("6");
//            result.setDescription("input param to json error," + params);
        }

        return result;
    }

    /**
     * 糖方法
     * | 1.模式一（预设模式）：查询exp表达是第一个字段[service]#[method]作为一个整体如果没有存在就进入 模式二
     * ——Note：模式二并没有使用
     * | 2.模式二（自定义模式）: 把service.method(param)作为一个接口查询并使用可缺省的结果转化表达是进行转换
     * @param exp sugar([service]#[method],[param],[function exp of result]) 表达式
     * @return 注意epx表达式错误则返回原来的表达；
     */
    public String sugar(String exp){
        //获取配置
        SugarConfig config = applicationContext.getBean(SugarConfig.class);
        //字符串分析
        SugarGird sugarGird = new SugarGird(exp);
        //获取预设函数配置
        Transmission transmission = null;
        if(Objects.nonNull(config) && Objects.nonNull(config.getTransmiMap()))
            transmission = config.getTransmiMap().get(sugarGird.getSugarTag());
        if (transmission == null && sugarGird.getMode() != Sugar_Mode_Bad ){
            sugarGird.setMode(Sugar_Mode_2);
        }
        //TODO new feature 这里应该可以让transmission接管调用的行为
        if (sugarGird.getMode() == SugarEnum.Sugar_Mode_1){
            Object req = transmission.dealRequestObject().apply(sugarGird.getParams());
            Object result = callMethod(transmission.objectBean(),transmission.serviceName(),transmission.methodName(),gson.toJson(req));
            Object res = transmission.dealResultObject().apply(result);
            return gson.toJson(res);
        }else if(sugarGird.getMode() == Sugar_Mode_2){
            //FIX TRY-CATCH
            //TODO 做结果函数的处理
            return exp;
        }
        //模式识别失败
        return exp;
    }
}
