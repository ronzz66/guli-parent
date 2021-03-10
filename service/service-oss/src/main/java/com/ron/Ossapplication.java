package com.ron;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源自动配置，防止自动加载未配置的数据源
@ComponentScan("com.ron")
@EnableDiscoveryClient
public class Ossapplication {
    public static void main(String[] args) {
        SpringApplication.run(Ossapplication.class);
    }
}
