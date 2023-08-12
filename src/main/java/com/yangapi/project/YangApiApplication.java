package com.yangapi.project;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yangapi.project.mapper")
@EnableDubbo
public class YangApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(YangApiApplication.class, args);
    }

}
