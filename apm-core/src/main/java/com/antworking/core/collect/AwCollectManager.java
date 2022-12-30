package com.antworking.core.collect;

import com.antworking.core.factory.AntWorkingFactory;
import com.antworking.model.collect.CollectDataBaseModel;
import com.antworking.model.core.AppNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AXiang
 * date 2022/12/30 15:08
 */
public class AwCollectManager {

    private final static ThreadLocal<List<CollectDataBaseModel>> collectData = new ThreadLocal<>();


    /**
     * describe：创建当前线程对象，若以创建返回false
     *
     * @author AXiang
     * date 2022/12/30 15:15
     */
    public static boolean create(CollectDataBaseModel model) {
        synchronized (collectData) {
            if (get() == null) {
                List<CollectDataBaseModel> models = new ArrayList<>();
                models.add(model);
                collectData.set(models);
            }
            return collectData.get() == null;
        }
    }

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

    private static void write(List<CollectDataBaseModel> models) {
        AntWorkingFactory.INSTANCE.writeFactory().write(models);
    }

    public static void write() {
        List<CollectDataBaseModel> models = get();
        AntWorkingFactory.INSTANCE.writeFactory().write(models);
    }

    public static CollectDataBaseModel getNode(String nodeName) {
        List<CollectDataBaseModel> list = get();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getAppNode().getFrame().equals(nodeName)) {
                return list.get(i);
            }
        }
        return null;
    }
}
