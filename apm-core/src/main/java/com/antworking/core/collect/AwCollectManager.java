package com.antworking.core.collect;

import com.antworking.apm.model.collect.AwCollectTraceData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AwCollectManager {

    private final static ThreadLocal<List<AwCollectPack>> collects = new ThreadLocal<>();

    private final static ThreadLocal<Integer> tOrder = new ThreadLocal<>();

    private final static Object lock = new Object();

    public static void add(AwCollectPack pack) {
        synchronized (lock) {
            List<AwCollectPack> packs = get();
            if (packs == null) {
                packs = new LinkedList<>();
                packs.add(pack);
                collects.set(packs);
            } else {
                packs.add(pack);
            }
        }
    }

    public static List<AwCollectPack> get() {
        return collects.get();
    }

    public static AwCollectPack get(String traceId) {
        List<AwCollectPack> packs = collects.get();
        for (AwCollectPack pack : packs) {
            if (pack.traceId.equals(traceId)) {
                return pack;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        add(new AwCollectPack("1", null));
        add(new AwCollectPack("2", null));
        add(new AwCollectPack("3", null));
        System.out.println(get("2"));
        System.out.println(get("4"));
    }

    public static int getOrder() {
        return tOrder.get() + 1;
    }

    public static int addOrder() {
        Integer order = tOrder.get();
        if (order == null) order = 0;
        order++;
        tOrder.set(order);
        return order;
    }
}
