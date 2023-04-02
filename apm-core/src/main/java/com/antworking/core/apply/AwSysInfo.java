package com.antworking.core.apply;

import com.antworking.apm.model.collect.AwSystemMessage;

public class AwSysInfo {

    private final static AwSystemMessage.SysData data;

    static {
        data = AwSystemMessage.SysData.newBuilder().build();
    }

    public static AwSystemMessage.SysData get() {
        return data;
    }
}
