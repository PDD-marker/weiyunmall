package com.taotao.weiyunmall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableFeignClients
@EnableDiscoveryClient //开启注册中心
@SpringBootApplication
public class WeiyunmallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeiyunmallWareApplication.class, args);
    }

}
