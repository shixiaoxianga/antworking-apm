package com.antworking.core.classload;

import com.antworking.common.ConstantAgent;
import com.antworking.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author XiangXiaoWei
 * date 2022/6/29
 */
public class AntWorkingClassLoad extends ClassLoader {
    private static final Logger log = LoggerFactory.getLogger(AntWorkingClassLoad.class);
    public static final List<Jar> jars = new ArrayList<>();
    //增强类
    public static final List<Class<?>> plugins = new LinkedList<>();

    public AntWorkingClassLoad(ClassLoader parent) {
        super(parent);
    }

    public AntWorkingClassLoad() {
    }

    public static void put(File jarFile) {
        try {
            jars.add(new Jar(new JarFile(jarFile), jarFile));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public InputStream getResourceAsStream(String clazzPath) {
        for (Jar jar : jars) {
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
        for (Jar jar : jars) {
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
        for (Jar jar : jars) {
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
        for (Jar jar : jars) {
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



    public static void scanPlugin() {
        String absolutePath = new File(AntWorkingClassLoad.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParentFile().getAbsolutePath();
        String jarPath = absolutePath + FileUtil.getPlatFormSlash() + ConstantAgent.PLUGIN_FOLDER;
        log.info("plugin path:{}", jarPath);
        final File file = new File(jarPath);
        final File[] files = file.listFiles();
        for (File jarFile : files) {
            try {
                URL url = new URL("jar:file:" + jarFile.getAbsolutePath() + "!/" + "plugins.antworking");
                InputStream inputStream = url.openStream();
                byte[] b = new byte[inputStream.available()];
                inputStream.read(b);
                String clazz = new String(b);
                AntWorkingClassLoad.put(jarFile);
                AntWorkingClassLoad classLoad = new AntWorkingClassLoad();
                Class<?> aClass = classLoad.loadClass(clazz);
                plugins.add(aClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class Jar {
        private JarFile jarFile;
        private File sourceFile;

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
