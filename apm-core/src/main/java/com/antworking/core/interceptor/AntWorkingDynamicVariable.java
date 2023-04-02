package com.antworking.core.interceptor;

import com.antworking.apm.model.collect.AwCollectTraceData;

public interface AntWorkingDynamicVariable {
    void set(AwCollectTraceData.Data o);

    AwCollectTraceData.Data get();

    void write(AwCollectTraceData.Data data);
}
