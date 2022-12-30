package com.antworking.logger;

public abstract class AbstractAwLog implements AwLog {

    public abstract void doInfo(String str, Object... args);

    public abstract void doWarn(String str, Object... args);

    public abstract void doDebug(String str, Object... args);

    public abstract void doError(Throwable e, String str, Object... args);

    public abstract void doError(String str, Object... args);

    @Override
    public void info(String message) {
        if (isInfoEnabled()) {
            doInfo(message);
        }
    }

    @Override
    public void info(String message, Object... args) {
        if (isInfoEnabled()) {
            doInfo(message, args);
        }
    }

    @Override
    public void warn(String message) {
        if (isWarnEnabled()) {
            doWarn(message);
        }
    }

    @Override
    public void warn(String message, Object... args) {
        if (isWarnEnabled()) {
            doWarn(message, args);
        }
    }

    @Override
    public void error(String message) {
        if (isErrorEnabled()) {
            doError(message);
        }
    }

    @Override
    public void error(String message, Object... args) {
        if (isWarnEnabled()) {
            doError(message, args);
        }
    }

    @Override
    public void error(Throwable t, String message, Object... args) {
        if (isWarnEnabled()) {
            doError(t, message, args);
        }
    }

    @Override
    public void debug(String message) {
        if (isDebugEnabled()) {
            doDebug(message);
        }
    }

    @Override
    public void debug(String message, Object... args) {
        if (isDebugEnabled()) {
            doDebug(message, args);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }
}
