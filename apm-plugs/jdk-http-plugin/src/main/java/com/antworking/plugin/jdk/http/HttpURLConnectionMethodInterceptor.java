package com.antworking.plugin.jdk.http;

import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class HttpURLConnectionMethodInterceptor {
  private static final Logger log = LoggerFactory.getLogger(HttpURLConnectionMethodInterceptor.class);

/*  @Advice.OnMethodEnter
  public static void enter(@Advice.This Object target,
                           @Advice.AllArguments Object[] args) throws Throwable {
    log.info((String) target);
    log.info(Arrays.toString(args));
  }*/
  @Advice.OnMethodEnter
  public static void enter() throws Throwable {
    log.info("test http");
  }

  @Advice.OnMethodExit
  public static void exit() throws Throwable {

  }
}