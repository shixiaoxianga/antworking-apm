package com.antworking.core.collect;

import com.antworking.apm.model.collect.AwCollectTraceData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public interface AwCollectPack {
    void addData(AwCollectTraceData.Data data);
    AwCollectTraceData.Data getData(String nodeId);

    void finish(String nodeId);
    Integer addOrder();
    String getTraceId();

    void setData(AwCollectTraceData.Data data, String nodeId);
}
