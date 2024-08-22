package com.antworking.core;

import com.antworking.core.interceptor.AbstractConstructorMethodInterceptHandler;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class InheritanceScanner {

    public static List<Class<?>> findSubClassesOf(Class<AbstractConstructorMethodInterceptHandler> superClass, String... packageNames) {
        List<Class<?>> subclasses = new ArrayList<>();

        // 遍历指定包
        for (String packageName : packageNames) {
            try {
                // 获取包下所有类的URL
                Enumeration<URL> urls = ClassLoader.getSystemResources(packageName.replace(".", "/"));
                
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    
                    if (url.toString().startsWith("jar:")) { // jar包内扫描
                        JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String className = entry.getName();
                            if (className.endsWith(".class") && className.startsWith(packageName.replace(".", "/"))) {
                                className = className.substring(0, className.length() - 6).replace("/", ".");
                                tryLoadAndAdd(superClass, className, subclasses);
                            }
                        }
                    } else { // 普通文件系统路径下扫描
                        File directory = new File(url.getFile());
                        File[] files = directory.listFiles((dir, name) -> name.endsWith(".class"));
                        if (files != null) {
                            for (File file : files) {
                                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                                tryLoadAndAdd(superClass, className, subclasses);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to scan classes", e);
            }
        }

        return subclasses;
    }

    private static void tryLoadAndAdd(Class<AbstractConstructorMethodInterceptHandler> superClass, String className, List<Class<?>> subclasses) {
        try {
            Class<?> loadedClass = Class.forName(className);
            if (superClass.isAssignableFrom(loadedClass) && !superClass.equals(loadedClass)) {
                subclasses.add(loadedClass);
            }
        } catch (ClassNotFoundException e) {
            // 如果找不到类，则忽略

        }
    }

    public static void main(String[] args) throws Exception {
        List<Class<?>> subclasses = findSubClassesOf(AbstractConstructorMethodInterceptHandler.class,
                "com.antworking.core.upgrade.mvc");
        for (Class<?> subclass : subclasses) {
            System.out.println(subclass.getName());
        }
    }
}