package com.antworking.apm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AwRouterEnum {
    COLLECT("COLLECT");

    private final String route;
}
