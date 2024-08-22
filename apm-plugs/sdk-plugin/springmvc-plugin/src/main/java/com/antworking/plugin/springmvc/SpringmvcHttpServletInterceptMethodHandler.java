package com.antworking.plugin.springmvc;

import com.antworking.core.common.ConstantAppNode;
import com.antworking.core.collect.AwCollectManager;
import com.antworking.core.handler.AbstractMethodInterceptHandler;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import com.antworking.model.collect.CollectDataBaseModel;
import com.antworking.model.collect.ErrorDescribeModel;
import com.antworking.utils.JsonUtil;
import com.antworking.utils.TimeUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class SpringmvcHttpServletInterceptMethodHandler extends AbstractMethodInterceptHandler {

    private final AwLog log = LoggerFactory.getLogger(SpringmvcHttpServletInterceptMethodHandler.class);

    @Override
    public void doBefore(Method method, Object[] params, Object instance, Class<?> clazz, Callable<Object> callable) {
        CollectDataBaseModel model = null;
        SpringmvcReqDescribeModel springmvc = new SpringmvcReqDescribeModel();

        model = CollectDataBaseModel.init(true, springmvc, ConstantAppNode.SPRING_MVC,
                Thread.currentThread().getName(),
                AwCollectManager.getTraceId());
        AwCollectManager.createOrAdd(model);


        List<Object> args = Arrays.stream((Object[])params[0]).filter(param ->
                param!=null &&
                !param.getClass().getName().equals("org.apache.catalina.core.ApplicationHttpRequest") &&
                !param.getClass().getName().equals("org.apache.catalina.connector.ResponseFacade") &&
                !param.getClass().getName().equals("org.apache.catalina.connector.RequestFacade") &&
                !param.getClass().getName().equals("javax.servlet.http.HttpServletRequest") &&
                !param.getClass().getName().contains("org.springframework") &&
                !param.getClass().getName().contains("com.alibaba.druid") &&
                !(param instanceof  Exception) &&
                        !param.getClass().getName().equals("javax.servlet.http.HttpServletResponse") &&
                        !param.getClass().getName().contains("org.springframework.web.multipart.MultipartFile")).collect(Collectors.toList());
        try {
            if(args.size() == 0){return;}
            springmvc.setParam(args.stream().map(JsonUtil::toJsonString).toArray());
        }catch (Throwable e){
            log.error("采集MVC参数异常：{}",e.getMessage(),e);
        }

    }

    @Override
    public Object doAfter(Method method, Object[] params, Object instance, Class<?> clazz, Callable<Object> callable, Object result) {
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode.SPRING_MVC.getFrame());
        assert model != null;
        model.setEndTime(TimeUtil.getCurrentTimeNano());
        return null;
    }

    @Override
    public void doCatch(Throwable e, Method method, Object[] params, Object instance, Class<?> clazz, Callable<Object> callable) {
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode.SPRING_MVC.getFrame());

        ErrorDescribeModel errorDescribeModel = new ErrorDescribeModel();
        errorDescribeModel.setClazz(clazz.getName());
        errorDescribeModel.setMessage(e.toString());
        errorDescribeModel.setStacks(e.getStackTrace());
        errorDescribeModel.setTimeStamp(System.currentTimeMillis());
        assert model != null;
        model.setEndTime(TimeUtil.getCurrentTimeNano());
        model.setError(errorDescribeModel);
    }

    @Override
    public void doFinal(Method method, Object[] params, Object instance, Class<?> clazz, Callable<Object> callable) {
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode.SPRING_MVC.getFrame());
        assert model != null;
        model.setEndTime(TimeUtil.getCurrentTimeNano());
        if (AwCollectManager.isExist()) {
            if (!AwCollectManager.get().get(0).isWeb()) {
                AwCollectManager.finish();
            }
        }
    }
}
