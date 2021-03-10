package com.ron.eduservice;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ron")//扫描service-base的配置，如果配置文件不在此模块中则需要添加此注解
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
public class EduApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class);
    }
}
