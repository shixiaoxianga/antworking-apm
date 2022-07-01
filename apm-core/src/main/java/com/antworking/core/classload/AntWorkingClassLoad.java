package com.antworking.core.classload;

import com.antworking.common.ConstantAgent;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;

/**
 * @author XiangXiaoWei
 * date 2022/6/29
 */
public class AntWorkingClassLoad extends ClassLoader {

    public static final List<Jar> jars = new ArrayList<>();
    //增强类
    public static final List<Class<?>> plugins = new LinkedList<>();

    public static void put(File jarFile) {
        try {
            jars.add(new Jar(new JarFile(jarFile), jarFile));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
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
        String jarPath = new File(System.getProperty("user.dir")).getAbsoluteFile() + "\\" + ConstantAgent.PLUGIN_FOLDER;
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
