package com.antworking.core.plugin;

import com.antworking.common.AwConstant;
import com.antworking.core.classload.AntWorkingClassLoad;
import com.antworking.core.enhance.AwEnhanceStatement;
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
    public static final List<PluginJar> jars = new ArrayList<>();
    public static final List<PluginInst> pluginInst = new LinkedList<>();

    public static void scanPlugin() {
        // TODO: 2023/1/3 处理main方法运行的时候获取路径
        String apmFillPath = new File(AntWorkingClassLoad.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParentFile().getParent();
        String absolutePath = apmFillPath + FileUtil.getPlatFormSlash() + AwConstant.APP_FOLDER;
//        String jarPath = absolutePath + FileUtil.getPlatFormSlash() + AwConstant.PLUGIN_FOLDER;
        String jarPath = "/Users/xiangzhongwei/development/my-project/java-code/antworking-apm/antworking-apm/plugin";
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
                URL url = new URL("jar:file:" + jarFile.getAbsolutePath() + "!/" + "antworking.properties");
                put(jarFile);
                Properties properties = new Properties();
                InputStream inputStream = url.openStream();
                properties.load(inputStream);
                PluginInst inst = new PluginInst();
                String[] clazzArr = properties.get(AwConstant.AW_ENHANCE_STATEMENT).toString().split(",");
                for (String clazz : clazzArr) {
                    Class<?> aClass = AntWorkingClassLoad.INSTANCE.loadClass(clazz);
                    inst.getStatements().add((AwEnhanceStatement) aClass.newInstance());
                }
                pluginInst.add(inst);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void put(File jarFile) {
        try {
            jars.add(new PluginJar(new JarFile(jarFile), jarFile));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


}

