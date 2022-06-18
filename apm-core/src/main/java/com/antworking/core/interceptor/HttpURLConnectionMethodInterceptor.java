package com.antworking.core.interceptor;

import net.bytebuddy.asm.Advice;

public class HttpURLConnectionMethodInterceptor {
  @Advice.OnMethodEnter
  public static void enter() throws Throwable {
    System.out.println("---[BEGIN] HttpURLConnectionMethodInterceptor");
  }

  @Advice.OnMethodExit
  public static void exit() throws Throwable {
    System.out.println("---[END] HttpURLConnectionMethodInterceptor");
  }
}