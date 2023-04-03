package com.antworking.core.collect;

import com.antworking.apm.model.collect.AwCollectTraceData;
import com.antworking.util.UuidUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AwCollectManager {

    private final static AwCollectPack pack = AwSimpleCollectPack.INSTANT;


    public static AwCollectPack get() {
        return pack;
    }

    public static String getTraceId() {
        return pack.getTraceId();
    }


}
