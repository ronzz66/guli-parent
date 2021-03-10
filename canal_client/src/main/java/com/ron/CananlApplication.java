package com.ron;


import com.ron.client.CanalClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class CananlApplication implements CommandLineRunner {

    //调用客户端监听
    @Resource
    private CanalClient canalClient;
    public static void main(String[] args) {
        SpringApplication.run(CananlApplication.class);
    }


    @Override
    public void run(String... args) throws Exception {
        canalClient.run();//项目启动执行客户端监听
    }
}
