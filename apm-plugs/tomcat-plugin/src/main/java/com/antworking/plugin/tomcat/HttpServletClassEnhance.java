package com.antworking.plugin.tomcat;

import com.antworking.common.ConstantNode;
import com.antworking.core.enhance.AbstractClassEnhance;
import com.antworking.core.tools.CollectionModelTools;
import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.method.MethodDescribeModel;
import com.antworking.model.base.tomcat.TomcatReqDescribeModel;
import com.google.gson.Gson;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;

public class HttpServletClassEnhance extends AbstractClassEnhance {


    private final String CLASS_NAME="javax.servlet.http.HttpServlet";
    TomcatReqDescribeModel tomcatModel = new TomcatReqDescribeModel();
    HttpServletRequestAdapter request ;
    HttpServletResponseAdapter response  ;

    @Override
    public void init() {

    }

    @Override
    public String interceptorClass() {
        return null;
    }

    @Override
    public ElementMatcher<? super MethodDescription> buildMethodMatchers() {
        return ElementMatchers.named("service").and(ElementMatchers.isProtected());
    }

    @Override
    public ElementMatcher<? super TypeDescription> buildTypeMatchers() {
        return ElementMatchers.named(CLASS_NAME);
    }

    @Override
    public BaseCollectModel invokeMethodBefore(Class<?> clazz, Method method, Object[] args, BaseCollectModel model, MethodDescribeModel methodDescribeModel) {
        model = CollectionModelTools.INSTANCE.linkStartCreateBaseCollectModel(model, method, args, clazz, methodDescribeModel);
        Object req = args[0];
        Object resp = args[1];
        request = new HttpServletRequestAdapter(req);
        response  = new HttpServletResponseAdapter(resp);

        tomcatModel.setMethodName(request.getMethod());
        tomcatModel.setClientIp(request.getClientIp());
        tomcatModel.setParamType(new Gson().toJson(request.getParameterMap()));
        tomcatModel.setReqUri(request.getRequestURI());
        tomcatModel.setReqUrl(request.getRequestURL());
        methodDescribeModel.setData(tomcatModel);
        return model;
    }

    @Override
    public Object invokeMethodAfter(Class<?> clazz, Method method, Object[] args, Object result, BaseCollectModel model,MethodDescribeModel methodDescribeModel) {
        tomcatModel.setRepCode(response.getResponseCode());
        model.setNode(ConstantNode.TOMCAT);
        model.setCrux(true);
        CollectionModelTools.INSTANCE.totalEnd(null, model, clazz, args, method);
        return result;
    }

    @Override
    public void invokeMethodException(Class<?> clazz, Method method, Object[] args, Throwable e, BaseCollectModel model,MethodDescribeModel methodDescribeModel) {
        CollectionModelTools.INSTANCE.totalEnd(e, model, clazz, args, method);
    }
}
