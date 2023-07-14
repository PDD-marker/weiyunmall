package com.taotao.weiyunmall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 1.服务注册发现
 * 2.配置yml文件（Nacos地址和当前应用名称）
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WeiyunmallGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeiyunmallGatewayApplication.class, args);
    }

}
