package com.antworking.core.factory;

import com.antworking.core.agent.AntWorkingAgentBuild;
import com.antworking.core.agent.DefaultAntWorkingAgentBuild;
import com.antworking.core.collect.MysqlWrite;
import com.antworking.core.collect.IWrite;

/**
 * @author AXiang
 * date 2022/12/29 13:47
 */
public enum AntWorkingFactory {

    INSTANCE;

    private IWrite write = new MysqlWrite(null);
    public AntWorkingAgentBuild factoryAWBuild() {
        return DefaultAntWorkingAgentBuild.INSTANCE;
    }

    public IWrite writeFactory() {
        // TODO: 2022/12/30 实例化
        return write;
    }


}
