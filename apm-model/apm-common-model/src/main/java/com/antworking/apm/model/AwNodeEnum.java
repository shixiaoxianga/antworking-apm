package com.antworking.apm.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AwNodeEnum {
    SPRING_MAC("SpringMvc", "DispatchServlet"),
    HTTP_CLIENT("HttpClient", "InternalHttpClient");
    private final String group;
    private final String node;

    public String getGroup() {
        return group;
    }

    public String getNode() {
        return node;
    }
}
