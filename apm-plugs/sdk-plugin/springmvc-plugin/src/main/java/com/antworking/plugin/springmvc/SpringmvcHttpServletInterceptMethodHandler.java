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

        try {
//            if (params == null || params.length == 0 || params[0] == null) {
//                return;
//            }
//
//            Object[] paramsArray = (Object[])params[0];
//            Object[] safeParams = new Object[paramsArray.length];
//
//            for (int i = 0; i < paramsArray.length; i++) {
//                Object param = paramsArray[i];
//                if (param == null) {
//                    safeParams[i] = null;
//                    continue;
//                }
//
//                String className = param.getClass().getName();
//
//                // 排除已知可能导致序列化问题的类型
//                if (className.equals("org.apache.catalina.core.ApplicationHttpRequest") ||
//                    className.equals("org.apache.catalina.connector.ResponseFacade") ||
//                    className.equals("org.apache.catalina.connector.RequestFacade") ||
//                    className.equals("javax.servlet.http.HttpServletRequest") ||
//                    className.equals("javax.servlet.http.HttpServletResponse") ||
//                    className.contains("org.springframework") ||
//                    className.contains("com.alibaba.druid") ||
//                    className.contains("java.io.BufferedReader") ||
//                    className.contains("java.io.Reader") ||
//                    className.contains("java.io.InputStream") ||
//                    className.contains("io.undertow.servlet.spec") ||
//                    className.contains("org.springframework.web.multipart.MultipartFile") ||
//                    param instanceof Exception) {
//
//                    // 对于不安全的类型，只保存类名和简单信息
//                    safeParams[i] = "类型: " + className;
//                } else {
//                    // 对于可能安全的类型，尝试安全序列化
//                    try {
//                        // 简单类型和常见安全类型可以直接序列化
//                        if (param instanceof String || param instanceof Number ||
//                            param instanceof Boolean || param instanceof Character ||
//                            param instanceof Enum || param.getClass().isPrimitive()) {
//                            safeParams[i] = param;
//                        } else {
//                            // 对于复杂类型，只返回类型信息和toString结果（限制长度避免过大）
//                            String toString = param.toString();
//                            if (toString.length() > 100) {
//                                toString = toString.substring(0, 100) + "...";
//                            }
//                            safeParams[i] = "类型: " + className + ", 值: " + toString;
//                        }
//                    } catch (Throwable e) {
//                        // 如果出现任何异常，只返回类名
//                        safeParams[i] = "类型: " + className + " (无法获取值)";
//                    }
//                }
//            }
            
            // 过滤掉空值
//            List<Object> nonNullParams = Arrays.stream(safeParams)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//
//            if (nonNullParams.isEmpty()) {
//                return;
//            }
            
//            springmvc.setParam(nonNullParams.toArray());
        } catch (Throwable e) {
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
