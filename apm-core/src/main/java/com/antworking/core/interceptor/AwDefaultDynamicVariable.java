package com.antworking.core.interceptor;

import com.antworking.apm.model.collect.AwCollectTraceData;

public class AwDefaultDynamicVariable extends AbstractAntWorkingDynamicVariable {
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
