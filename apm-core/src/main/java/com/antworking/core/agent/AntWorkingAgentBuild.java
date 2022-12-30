package com.antworking.core.agent;

import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

/**
 * @author AXiang
 * date 2022/12/29 13:45
 */
public interface AntWorkingAgentBuild {
    public AgentBuilder ignore(AgentBuilder agentBuilder);

   public AgentBuilder listener(AgentBuilder agentBuilder);


   public AgentBuilder inject(AgentBuilder agentBuilder,Instrumentation instrumentation);

    AgentBuilder applyPlugin(AgentBuilder agentBuilder);

    AgentBuilder strategy(AgentBuilder agentBuilder);
}
