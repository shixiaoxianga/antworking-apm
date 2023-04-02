package com.antworking.core.interceptor;

import com.antworking.apm.model.AwRouterEnum;
import com.antworking.apm.model.collect.AwCollectTraceData;
import com.antworking.apm.model.collect.RouterMessage;
import com.antworking.core.collect.AwDataHandler;
import com.antworking.core.factory.AntWorkingFactory;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import com.antworking.util.JsonUtil;

public abstract class AbstractAntWorkingDynamicVariable implements AntWorkingDynamicVariable {
    private final AwLog log = LoggerFactory.getLogger(AbstractAntWorkingDynamicVariable.class);

    @Override
    public void set(AwCollectTraceData.Data o) {
        set0(o);
    }

    @Override
    public AwCollectTraceData.Data get() {
        return get0();
    }

    @Override
    public void write(AwCollectTraceData.Data data) {
        log.info(JsonUtil.toJsonString(data));
        RouterMessage.Rm router = RouterMessage.Rm.newBuilder()
                .setContent(JsonUtil.toJsonString(data))
                .setKind("api")
                .setRoute(AwRouterEnum.COLLECT.getRoute())
                .build();
        AntWorkingFactory.INSTANCE.factoryAwComm().execute(router);
    }

    public abstract void set0(AwCollectTraceData.Data o);

    public abstract AwCollectTraceData.Data get0();

}
