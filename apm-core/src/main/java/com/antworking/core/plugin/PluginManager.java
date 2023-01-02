package com.antworking.core.plugin;

import com.antworking.common.ConstantAw;
import com.antworking.core.classload.AntWorkingClassLoad;
import com.antworking.core.enhance.EnhanceStatement;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import com.antworking.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;

public enum PluginManager {
    INSTANCE;
    private static final AwLog log = LoggerFactory.getLogger(PluginManager.class);
    public static final List<PluginManager.Jar> jars = new ArrayList<>();
    public static final List<Class<?>> enhanceStatement = new LinkedList<>();

    public static void scanPlugin() {
        String absolutePath = System.getProperty("user.dir") + FileUtil.getPlatFormSlash() + ConstantAw.APP_FOLDER;
        String jarPath = absolutePath + FileUtil.getPlatFormSlash() + ConstantAw.PLUGIN_FOLDER;
        log.info("plugin path:{}", jarPath);
        final File file = new File(jarPath);
        final File[] files = file.listFiles();
        if (Objects.isNull(files)) {
            log.warn("Plugin not found");
            return;
        }
        for (File jarFile : files) {
            try {
                log.info("loaded plugin file name:{}", jarFile.getName());
                URL url = new URL("jar:file:" + jarFile.getAbsolutePath() + "!/" + "plugins.antworking.properties");
                put(jarFile);
                Properties properties = new Properties();
                InputStream inputStream = url.openStream();
                properties.load(inputStream);
                String val = (String) properties.get(EnhanceStatement.class.getName());
                String[] clazzArr = val.split(",");
                for (String clazz : clazzArr) {
                    Class<?> aClass = AntWorkingClassLoad.INSTANCE.loadClass(clazz);
                    enhanceStatement.add(aClass);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void put(File jarFile) {
        try {
            jars.add(new Jar(new JarFile(jarFile), jarFile));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static class Jar {
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

        public Jar(JarFile jarFile, File sourceFile) {
            this.jarFile = jarFile;
            this.sourceFile = sourceFile;
        }
    }
}

