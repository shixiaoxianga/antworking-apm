package com.antworking.core.start;

public abstract class AwAbstractApplicationStart implements AwApplicationStart {
    @Override
    public void init() {
        doInit();
    }

    @Override
    public void run() {
        doRun();
    }


    public abstract void doInit();

    public abstract void doRun();

}
