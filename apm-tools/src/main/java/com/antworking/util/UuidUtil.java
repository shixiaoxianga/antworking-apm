package com.antworking.util;

import java.util.UUID;

public class UuidUtil {
    public static String getId(){
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        System.out.println(getId());
        System.out.println(getId());
        System.out.println(getId());
    }
}
