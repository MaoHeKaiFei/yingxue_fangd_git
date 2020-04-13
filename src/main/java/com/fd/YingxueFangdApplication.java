package com.fd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tk.mybatis.spring.annotation.MapperScan;

//tk下的mapperScan
@MapperScan("com.fd.dao")
@org.mybatis.spring.annotation.MapperScan("com.fd.dao")
@SpringBootApplication
public class YingxueFangdApplication {

    public static void main(String[] args) {
        SpringApplication.run(YingxueFangdApplication.class, args);
    }

}
