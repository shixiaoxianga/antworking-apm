package com.antworking.logger;

public class AwLogSysOut extends AbstractAwLog {
    Class<?> clazz;

    public AwLogSysOut(Class<?> clazz) {
        this.clazz = clazz;
    }


    public void print(String str, Object... args) {
        str = str.replace("{}", "%s");
        System.out.printf(str + "\n", args);
    }

    @Override
    public void doInfo(String str, Object... args) {
        print(str, args);
    }

    @Override
    public void doWarn(String str, Object... args) {
        print(str, args);
    }

    @Override
    public void doDebug(String str, Object... args) {
        print(str, args);
    }

    @Override
    public void doError(Throwable e, String str, Object... args) {
        print(str, args);
        e.printStackTrace();
    }

    @Override
    public void doError(String str, Object... args) {
        print(str, args);
    }
}
