package com.antworking.core.classload;

import com.antworking.common.ConstantAw;
import com.antworking.core.plugin.PluginManager;
import com.antworking.core.tools.AwPathManager;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import com.antworking.utils.FileUtil;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.antworking.core.plugin.PluginManager.jars;

/**
 * @author XiangXiaoWei
 * date 2022/6/29
 */
public class AntWorkingClassLoad extends ClassLoader {
    public final static AntWorkingClassLoad INSTANCE = new AntWorkingClassLoad();
    private static final AwLog log = LoggerFactory.getLogger(AntWorkingClassLoad.class);

    public AntWorkingClassLoad(ClassLoader parent) {
//        super(parent);
    }

    public AntWorkingClassLoad() {
    }

    @Override
    public InputStream getResourceAsStream(String clazzPath) {
        for (PluginManager.Jar jar : PluginManager.jars) {
            if (jar.jarFile.getJarEntry(clazzPath) != null) {
                try {
                    URL url = new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + clazzPath);
                    return url.openStream();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.getResourceAsStream(clazzPath);
    }

    @Override
    protected URL findResource(String name) {
        for (PluginManager.Jar jar : PluginManager.jars) {
            JarEntry entry = jar.jarFile.getJarEntry(name);
            if (entry != null) {
                try {
                    return new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + name);
                } catch (MalformedURLException ignored) {
                }
            }
        }
        return null;
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        List<URL> allResources = new LinkedList<>();
        for (PluginManager.Jar jar : PluginManager.jars) {
            JarEntry entry = jar.jarFile.getJarEntry(name);
            if (entry != null) {
                allResources.add(new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + name));
            }
        }

        final Iterator<URL> iterator = allResources.iterator();
        return new Enumeration<URL>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public URL nextElement() {
                return iterator.next();
            }
        };
    }

//    @Override
//    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//        if(!name.startsWith("com.antworking.")){
//            return super.loadClass(name, resolve);
//        }
//        String clazzPath = name.replace(".", "/").concat(".class");
//        for (PluginManager.Jar jar : PluginManager.jars) {
//            if (jar.jarFile.getJarEntry(clazzPath) != null) {
//                try {
//                    byte[] data = getBytes( jar.sourceFile.getAbsolutePath(),clazzPath);
//                    return defineClass(name, data, 0, data.length);
//                } catch (Exception e) {
//                    log.error("加载class失败",e);
//                }
//
//            }
//        }
//        //core加载
//
//
//        try {
//            byte[] data = getBytes(AwPathManager.getAwPath()+File.separator+"apm-agent-1.0.0.jar",clazzPath);
//            return defineClass(name, data, 0, data.length);
//        } catch (Exception e) {
//            log.error("加载class失败",e);
//        }
//        return null;
//    }
//
//    @Override
//    public Class<?> loadClass(String name) throws ClassNotFoundException {
//        if(!name.startsWith("com.antworking.")){
//            return super.loadClass(name);
//        }
//        String clazzPath = name.replace(".", "/").concat(".class");
//        for (PluginManager.Jar jar : PluginManager.jars) {
//            if (jar.jarFile.getJarEntry(clazzPath) != null) {
//                try {
//                    byte[] data = getBytes( jar.sourceFile.getAbsolutePath(),clazzPath);
//                    return defineClass(name, data, 0, data.length);
//                } catch (Exception e) {
//                    log.error("加载class失败",e,e.toString());
//                }
//
//            }
//        }
//        //core加载
//
//
//        try {
//            byte[] data = getBytes(AwPathManager.getAwPath()+File.separator+"apm-agent-1.0.0.jar",clazzPath);
//            return defineClass(name, data, 0, data.length);
//        } catch (Exception e) {
//            log.error("加载class失败:{}",e.toString(),e);
//        }
//        return null;
//    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String clazzPath = name.replace(".", "/").concat(".class");
        for (PluginManager.Jar jar : PluginManager.jars) {
            if (jar.jarFile.getJarEntry(clazzPath) != null) {
                try {
                    byte[] data = getBytes( jar.sourceFile.getAbsolutePath(),clazzPath);
                    return defineClass(name, data, 0, data.length);
                } catch (Exception e) {
                    log.error("加载class失败",e);
                }

            }
        }
        //core加载


        try {
            byte[] data = getBytes(AwPathManager.getAwPath()+File.separator+"apm-agent-1.0.0.jar",clazzPath);
            return defineClass(name, data, 0, data.length);
        } catch (Exception e) {
            log.error("加载class失败",e);
        }
        return null;
    }

    private static byte[] getBytes(String jarPath,String clazzPath) throws IOException {
        URL url = new URL("jar:file:" + jarPath + "!/" + clazzPath);
        byte[] data;
        try (final BufferedInputStream is = new BufferedInputStream(
                url.openStream()); final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int ch;
            while ((ch = is.read()) != -1) {
                baos.write(ch);
            }
            data = baos.toByteArray();
        }
        return data;
    }


}
