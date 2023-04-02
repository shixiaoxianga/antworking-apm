package com.antworking.core.comm;

import com.antworking.apm.model.collect.RouterMessage;
/**
 * describe：通信
 * @author AXiang
 * date 2023/4/2 08:47
 */
public interface AwComm {
    Object execute(RouterMessage.Rm routerMessage);
}
