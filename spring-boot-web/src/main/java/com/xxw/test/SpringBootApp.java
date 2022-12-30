package com.xxw.test;

import com.xxw.test.controller.IndexController;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan("com.xxw.test.dao")
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class SpringBootApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class,args);
    }
}
