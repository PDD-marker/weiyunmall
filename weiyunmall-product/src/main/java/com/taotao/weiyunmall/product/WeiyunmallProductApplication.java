package com.taotao.weiyunmall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 使用MyBatis-Plus的逻辑删除
 * 1.配置全局逻辑删除规则
 * 2.实体类字段上加上@TableLogic注解
 *
 *
 * 引入Redis缓存
 *
 * 引入Redisson作为分布式锁功能框架
 *
 */
@EnableCaching
@EnableFeignClients(basePackages = "com.taotao.weiyunmall.product.feign")
@MapperScan("com.taotao.weiyunmall.product.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class WeiyunmallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeiyunmallProductApplication.class, args);
    }


}
