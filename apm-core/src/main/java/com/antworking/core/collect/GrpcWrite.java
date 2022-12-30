package com.antworking.core.collect;

import com.antworking.model.collect.CollectDataBaseModel;
import com.antworking.util.JsonUtil;

import java.util.List;

public class GrpcWrite implements IWrite {

    private final IWrite write;

    public GrpcWrite(IWrite write) {
        this.write = write;
    }

    @Override
    public Integer order() {
        return 0;
    }

    @Override
    public void write(List<CollectDataBaseModel> models) {
        if (isWriteEnable()) {
            // TODO: 2022/12/30 grpc通信
            System.out.println(JsonUtil.toJsonString(models));
            if (write != null) {
                write.write(models);
            }
        }
    }

    @Override
    public boolean isWriteEnable() {
        return true;
    }
}
