package com.antworking.logger;

/**
 * @author AXiang
 * date 2022/12/29 14:05
 */
public interface AwLog {

    void info(String message);

    void info(String message, Object... args);

    void warn(String message);

    void warn(String message, Object... args);

    void error(String message);

    void error(String message, Object... args);

    void error(Throwable t, String message, Object... args);

    void debug(String message);

    void debug(String message, Object... args);

    boolean isDebugEnabled();

    boolean isInfoEnabled();

    boolean isErrorEnabled();

    boolean isWarnEnabled();
}
