package com.antworking.core.agent;

import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

/**
 * @author AXiang
 * date 2022/12/29 13:45
 */
public interface AntWorkingAgentBuild {
    public void ignore(AgentBuilder agentBuilder);

   public void listener(AgentBuilder agentBuilder);

   public void initialization(AgentBuilder agentBuilder);

   public void inject(AgentBuilder agentBuilder,Instrumentation instrumentation);

    AgentBuilder applyPlugin(AgentBuilder agentBuilder);
}
