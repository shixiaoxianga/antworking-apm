package com.antworking.core.comm;

import com.antworking.apm.model.collect.RouterMessage;

public abstract class AwAbstractComm implements AwComm {
    @Override
    public Object execute(RouterMessage.Rm routerMessage) {
        return doExecute(routerMessage);
    }

    abstract Object doExecute(RouterMessage.Rm routerMessage);
}
