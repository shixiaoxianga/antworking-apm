package com.antworking.core.collect;

import com.antworking.core.factory.AntWorkingFactory;
import com.antworking.model.collect.CollectDataBaseModel;
import com.antworking.model.core.AppNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author AXiang
 * date 2022/12/30 15:08
 */
public class AwCollectManager {

    private final static ThreadLocal<List<CollectDataBaseModel>> collectData = new ThreadLocal<>();
    private final static ThreadLocal<String> traceIdLocal = new ThreadLocal<>();



    public static String getTraceId(){
        String traceId = AwCollectManager.traceIdLocal.get();
        if(Objects.isNull(traceId)){
            traceIdLocal.set(UUID.randomUUID().toString());
            return traceIdLocal.get();
        }
        return traceId;
    }
    /**
     * describe：当前线程不存在节点则创建当前线程对象，若以创建返回false
     *
     * @author AXiang
     * date 2022/12/30 15:15
     */
    public static boolean create(CollectDataBaseModel model) {
        synchronized (collectData) {
            boolean b = get() == null;
            if (b) {
                List<CollectDataBaseModel> models = new ArrayList<>();
                models.add(model);
                collectData.set(models);
            }
            return b;
        }
    }
    public static boolean createOrAdd(CollectDataBaseModel model) {
        synchronized (collectData) {
            if (get() == null) {
                List<CollectDataBaseModel> models = new ArrayList<>();
                models.add(model);
                collectData.set(models);
            }else {
                get().add(model);
            }
            return collectData.get() == null;
        }
    }

    /**
     * describe：当前线程不存在节点的话直接写入，存在则追加
     *
     * @author AXiang
     * date 2023/1/3 09:47
     */
    public static void put(CollectDataBaseModel model) {
        if (get() == null) {
            List<CollectDataBaseModel> models = new ArrayList<>();
            models.add(model);
            write(models);
        } else {
            get().add(model);
        }
    }

    public static List<CollectDataBaseModel> get() {
        return collectData.get();
    }

    public static boolean isExist() {
        return collectData.get() != null;
    }

    public static void remove() {
        synchronized (collectData) {
            collectData.remove();
        }
    }
    public static void removeTraceId() {
        synchronized (traceIdLocal) {
            traceIdLocal.remove();
        }
    }

    private static void write(List<CollectDataBaseModel> models) {
        remove();
        removeTraceId();
        AntWorkingFactory.INSTANCE.writeFactory().write(models);
    }

    /**
     * describe：web结束使用
     *
     * @author AXiang
     * date 2022/12/31 23:36
     */
    public static void write() {
        List<CollectDataBaseModel> models = get();
        remove();
        AntWorkingFactory.INSTANCE.writeFactory().write(models);
    }

    /**
     * describe：非web节点提交使用
     *
     * @author AXiang
     * date 2022/12/31 23:36
     */
    public static void put() {
        List<CollectDataBaseModel> models = get();
        if (models.size() == 1) {
            write(models);
        }
    }
    public static void finish() {
        List<CollectDataBaseModel> models = get();
        write(models);
    }

    public static CollectDataBaseModel getNode(String nodeName) {
        List<CollectDataBaseModel> list = get();
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getAppNode().getFrame().equals(nodeName)) {
                return list.get(i);
            }
        }
        return null;
    }
}
