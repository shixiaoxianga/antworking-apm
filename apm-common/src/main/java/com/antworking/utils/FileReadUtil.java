package com.antworking.utils;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileReadUtil {

    public static String readResourcesFile(String fileName){
        final InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        try {
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            return new String(bytes);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }

}
