package com.antworking.core.factory;

import com.antworking.core.agent.AntWorkingAgentBuild;
import com.antworking.core.agent.DefaultAntWorkingAgentBuild;

/**
 * @author AXiang
 * date 2022/12/29 13:47
 */
public enum AntWorkingFactory {

    INSTANCE;

    public AntWorkingAgentBuild factoryAWBuild() {
        return DefaultAntWorkingAgentBuild.INSTANCE;
    }


}
