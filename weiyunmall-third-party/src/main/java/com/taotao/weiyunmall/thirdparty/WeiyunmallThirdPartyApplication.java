package com.taotao.weiyunmall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class WeiyunmallThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeiyunmallThirdPartyApplication.class, args);
    }

}
