package com.taotao.weiyunmall.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class WeiyunmallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeiyunmallCouponApplication.class, args);
    }

}
