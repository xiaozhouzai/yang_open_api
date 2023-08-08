package com.yangapi.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yangapi.project.mapper")
public class YangApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(YangApiApplication.class, args);
    }

}
