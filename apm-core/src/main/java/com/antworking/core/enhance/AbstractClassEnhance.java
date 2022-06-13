package com.antworking.core.enhance;

import com.antworking.core.matchers.AbstractMethodMatchers;

/**
 * @author XiangXiaoWei
 * date 2022/6/11
 */
public abstract class AbstractClassEnhance {


    public abstract boolean isStatic();

    public abstract boolean isInterface();

    public abstract String getClassName();

    public abstract AbstractMethodMatchers getMethodMatchers();
}
