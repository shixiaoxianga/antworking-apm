package com.antworking.core.factory;

import com.antworking.core.agent.AntWorkingAgentBuild;
import com.antworking.core.agent.DefaultAntWorkingAgentBuild;
import com.antworking.core.collect.GrpcWrite;
import com.antworking.core.collect.IWrite;
import com.antworking.core.enhance.EnhanceStatement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * @author AXiang
 * date 2022/12/29 13:47
 */
public enum AntWorkingFactory {

    INSTANCE;

    public AntWorkingAgentBuild factoryAWBuild() {
        return DefaultAntWorkingAgentBuild.INSTANCE;
    }

    public IWrite writeFactory() {
        // TODO: 2022/12/30 实例化
        return new GrpcWrite(null);
    }


}
