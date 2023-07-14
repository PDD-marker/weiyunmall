package com.taotao.weiyunmall.ware.feign;

import com.taotao.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version 1.0
 * @quther 孤郁
 */
@FeignClient("weiyunmall-product")
public interface ProductFeignService {
    /**
     * 1)、让所有请求都过一下网关
     * @FeignClient("weiyunmall-gateway")
     * 请求路径带上/api
     * 2)、直接给后台服务指定处理
     * @param id
     * @return
     */
    //远程获取某一个sku的信息
    @RequestMapping("/product/skuinfo/info/{id}")
    public R info(@PathVariable("id") Long id);
}
