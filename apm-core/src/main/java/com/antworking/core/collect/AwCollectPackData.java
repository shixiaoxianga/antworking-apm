package com.antworking.core.collect;

import com.antworking.apm.model.collect.AwCollectTraceData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AwCollectPackData {
   private AwCollectTraceData.Data data;
   private Integer order;
   private Integer stack;
}
