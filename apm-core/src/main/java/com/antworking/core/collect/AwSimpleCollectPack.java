package com.antworking.core.collect;

import com.antworking.apm.model.AwRouterEnum;
import com.antworking.apm.model.collect.AwCollectTraceData;
import com.antworking.apm.model.collect.RouterMessage;
import com.antworking.core.factory.AntWorkingFactory;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import com.antworking.util.JsonUtil;
import com.antworking.util.UuidUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public enum AwSimpleCollectPack implements AwCollectPack {
    INSTANT;
    private final AwLog log = LoggerFactory.getLogger(AwSimpleCollectPack.class);
    private final static ThreadLocal<Map<String, AwCollectPackData>> collects = new ThreadLocal<>();
    private final static ThreadLocal<Integer> stack = new ThreadLocal<>();
    private final static ThreadLocal<Integer> tOrder = new ThreadLocal<>();

    private final static ThreadLocal<String> tTraceId = new ThreadLocal<>();

    private final static Object lock = new Object();

    @Override
    public void addData(AwCollectTraceData.Data data) {
        synchronized (lock) {
            Map<String, AwCollectPackData> dataMap = get();
            if (dataMap == null) {
                stack.set(0);
                tTraceId.set(data.getTraceId());
                dataMap = new HashMap<>();
            }
            AwCollectPackData packData = new AwCollectPackData();
            packData.setData(data);
            packData.setOrder(data.getOrder());
            stack.set(stack.get() + 1);
            packData.setStack(stack.get());
            dataMap.put(data.getNodeId(), packData);
            collects.set(dataMap);
        }
    }
//    public void getStack(){
//        Integer _stack = stack.get();
//        if(_stack==null){
//            stack.set(0);
//        }
//    }


    @Override
    public AwCollectTraceData.Data getData(String nodeId) {
        return get().get(nodeId).getData();
    }

    @Override
    public void finish(String nodeId) {
        String data = JsonUtil.toJsonString(getData(nodeId));
        log.info(data);
        stack.set(stack.get() - 1);
        if (stack.get() == 0) {
            collects.get().clear();
            stack.remove();
            collects.remove();
            tOrder.remove();
            tTraceId.remove();
        }

        RouterMessage.Rm router = RouterMessage.Rm.newBuilder()
                .setContent((data))
                .setKind("api")
                .setRoute(AwRouterEnum.COLLECT.getRoute())
                .build();
        AntWorkingFactory.INSTANCE.factoryAwComm().execute(router);
    }

    @Override
    public Integer addOrder() {
        Integer order = tOrder.get();
        if (order == null) order = 0;
        order++;
        tOrder.set(order);
        return order;
    }

    @Override
    public String getTraceId() {
        String id = tTraceId.get();
        if (id == null) {
            String nId = UuidUtil.getId();
            tTraceId.set(nId);
            return nId;
        }
        return id;
    }

    @Override
    public void setData(AwCollectTraceData.Data data, String nodeId) {
        AwCollectPackData packData = get().get(nodeId);
        packData.setData(data);
    }

    public Map<String, AwCollectPackData> get() {
        return collects.get();
    }
}
