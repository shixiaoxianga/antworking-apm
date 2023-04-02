package com.antworking.apm.tps.mvc;

import com.antworking.apm.model.collect.AwCollectTraceData;
import com.antworking.core.interceptor.AbstractAntWorkingDynamicVariable;

public class AwDispatchDynamicVariable extends AbstractAntWorkingDynamicVariable {
    private AwCollectTraceData.Data data;
    @Override
    public void set0(AwCollectTraceData.Data data) {
       this.data = data;
    }

    @Override
    public AwCollectTraceData.Data get0() {
        return data;
    }
}
