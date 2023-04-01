package com.antworking.core.plugin;

import java.io.File;
import java.util.jar.JarFile;

public class PluginJar {
    public JarFile jarFile;
    public File sourceFile;

    public JarFile getJarFile() {
        return jarFile;
    }

    public void setJarFile(JarFile jarFile) {
        this.jarFile = jarFile;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public PluginJar(JarFile jarFile, File sourceFile) {
        this.jarFile = jarFile;
        this.sourceFile = sourceFile;
    }
}