package com.antworking.model.core;

public class AppNode {
    private String version;
    private String frame;
    private Integer id;

    public AppNode(String version, String frame, Integer id) {
        this.version = version;
        this.frame = frame;
        this.id = id;
    }
    public AppNode(Integer id, String frame, String version) {
        this.version = version;
        this.frame = frame;
        this.id = id;
    }
    public AppNode(Integer id, String frame) {
        this.version = null;
        this.frame = frame;
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public AppNode setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getFrame() {
        return frame;
    }

    public AppNode setFrame(String frame) {
        this.frame = frame;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public AppNode setId(Integer id) {
        this.id = id;
        return this;
    }
}
