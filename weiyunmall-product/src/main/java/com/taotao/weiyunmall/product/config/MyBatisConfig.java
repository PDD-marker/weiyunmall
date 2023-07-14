package com.taotao.weiyunmall.product.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @version 1.0
 * @quther 孤郁
 */
@Configuration
@EnableTransactionManagement //开启事务
@MapperScan("com.taotao.weiyunmall.product.dao")
public class MyBatisConfig {
    //引入分页插件
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor(DbType.H2);
        //设置请求的页面大于页后操作，true调回到首页，false继续请求，默认为false
        innerInterceptor.setOverflow(true);
        //设置最大单页限制数量，默认500条，-1不受限制
        innerInterceptor.setMaxLimit(-1L);

        interceptor.addInnerInterceptor(innerInterceptor);
        return interceptor;
    }

//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return configuration -> configuration.setUseDeprecatedExecutor(false);
//    }
}
