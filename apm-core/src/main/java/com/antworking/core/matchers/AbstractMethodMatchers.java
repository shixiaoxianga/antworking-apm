package com.antworking.core.matchers;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * @author XiangXiaoWei
 * date 2022/6/11
 */
public abstract class AbstractMethodMatchers {

    public abstract ElementMatcher<? super MethodDescription> buildMethodMatchers();

}
