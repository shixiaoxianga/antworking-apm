package com.antworking.apm.tps.httpclient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalModel {
    private String protocolVersion;
    private String method;
    private String uri;

    private String code;
    private String repHeader;
    private String reqHeader;
}
