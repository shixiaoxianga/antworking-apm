package com.antworking.core.collect;

import com.antworking.apm.model.collect.AwCollectTraceData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AwCollectPack {
    public String traceId;
    public AwCollectTraceData.Data data;
}
