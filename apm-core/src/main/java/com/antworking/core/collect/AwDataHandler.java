package com.antworking.core.collect;

import com.antworking.apm.model.AwNodeEnum;
import com.antworking.apm.model.collect.AwCollectTraceData;
import com.antworking.core.apply.AwSysInfo;
import com.antworking.util.UuidUtil;

import java.util.Arrays;

public class AwDataHandler {


    public static AwCollectTraceData.Data initData() {
        return AwCollectTraceData
                .Data
                .newBuilder()
                .setTraceId(null)
                .setAppName(null)
                .setNodeId(UuidUtil.getId())
                .setParentTraceId(null)
                .setNodeGroup(null)
                .setNodeName(null)
                .setStartTime(System.currentTimeMillis())
                .setMethodName(null)
                .setMethodClazz(null)
                .setMethodArgs(null)
                .setOrder(0)
                .setThreadName(Thread.currentThread().getName())
                .build();
    }

    public static AwCollectTraceData.Data initData(Object[] mArgs,
                                                   Class<?> mClazz,
                                                   String method,
                                                   AwNodeEnum nodeEnum,
                                                   String nodeId
                                                   ) {
        return AwCollectTraceData
                .Data
                .newBuilder()
                .setTraceId(AwCollectManager.getTraceId())
                .setAppName(AwSysInfo.get().getAppName())
                .setNodeId(nodeId)
                .setParentTraceId("")
                .setNodeGroup(nodeEnum.getGroup())
                .setNodeName(nodeEnum.getNode())
                .setStartTime(System.currentTimeMillis())
                .setMethodName(method)
                .setMethodClazz(mClazz.getName())
                .setMethodArgs(Arrays.toString(mArgs))
                .setOrder(AwCollectManager.get().addOrder())
                .setThreadName(Thread.currentThread().getName())
                .build();
    }

}
