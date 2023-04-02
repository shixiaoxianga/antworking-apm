package com.antworking.test;

public class TheadTest {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getId());
        System.out.println();
        System.out.println(Thread.currentThread().getThreadGroup().getName());
    }
}
