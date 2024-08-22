package com.antworking.plugin.springmvc;

import com.antworking.model.collect.MethodDescribeModel;
import com.antworking.utils.JsonUtil;

import java.util.List;
import java.util.Map;

/**
 * @author XiangXiaoWei
 * date 2022/6/17
 */
public class SpringmvcReqDescribeModel extends MethodDescribeModel {


    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}