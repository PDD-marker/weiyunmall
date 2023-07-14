package com.taotao.weiyunmall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients(basePackages = "com.taotao.weiyunmall.member.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class WeiyunmallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeiyunmallMemberApplication.class, args);
    }

}
