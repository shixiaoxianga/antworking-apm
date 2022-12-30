package com.antworking.core.collect;

import com.antworking.model.collect.CollectDataBaseModel;

import java.util.List;

public interface IWrite {

    Integer order();

    void write(List<CollectDataBaseModel> models);

    // TODO: 2022/12/30 根据配置文件来
    boolean isWriteEnable();
}
