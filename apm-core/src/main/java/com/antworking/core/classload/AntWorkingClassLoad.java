package com.antworking.core.classload;

import com.antworking.core.plugin.PluginJar;
import com.antworking.core.plugin.PluginManager;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;


/**
 * @author XiangXiaoWei
 * date 2022/6/29
 */
public class AntWorkingClassLoad extends ClassLoader {
    public final static AntWorkingClassLoad INSTANCE = new AntWorkingClassLoad();
    private static final AwLog log = LoggerFactory.getLogger(AntWorkingClassLoad.class);

    public AntWorkingClassLoad(ClassLoader parent) {
        super(parent);
    }

    public AntWorkingClassLoad() {
    }

    @Override
    public InputStream getResourceAsStream(String clazzPath) {
        for (PluginJar jar : PluginManager.jars) {
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
        for (PluginJar jar : PluginManager.jars) {
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
        for (PluginJar jar : PluginManager.jars) {
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

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String clazzPath = name.replace(".", "/").concat(".class");
        for (PluginJar jar : PluginManager.jars) {
            if (jar.jarFile.getJarEntry(clazzPath) != null) {
                try {
                    URL url = new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + clazzPath);
                    byte[] data;
                    try (final BufferedInputStream is = new BufferedInputStream(
                            url.openStream()); final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                        int ch;
                        while ((ch = is.read()) != -1) {
                            baos.write(ch);
                        }
                        data = baos.toByteArray();
                    }
                    return defineClass(name, data, 0, data.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return null;
    }


}
