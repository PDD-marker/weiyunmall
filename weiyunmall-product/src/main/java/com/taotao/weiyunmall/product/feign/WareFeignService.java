package com.taotao.weiyunmall.product.feign;

import com.taotao.common.to.SkuHasStockVo;
import com.taotao.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @version 1.0
 * @quther 孤郁
 */
@FeignClient("weiyunmall-ware")
public interface WareFeignService {
    //为了解决返回值类型的问题

    /**
     * 1. R设计的时候可以加上泛型
     * 2. 直接返回想要的数据类型
     * 3. 自己封装解析结果
     * @param skuIds
     * @return
     */
    @PostMapping("/ware/waresku/hasstock")
    R getSkusHasStock(List<Long> skuIds);
}
