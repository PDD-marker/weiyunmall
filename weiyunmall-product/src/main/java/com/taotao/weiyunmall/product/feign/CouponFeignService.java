package com.taotao.weiyunmall.product.feign;

import com.taotao.common.to.SkuReductionTo;
import com.taotao.common.to.SpuBoundTo;
import com.taotao.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @version 1.0
 * @quther 孤郁
 */
@FeignClient("weiyunmall-coupon")
public interface CouponFeignService {

    /**
     * 1.CouponFeignService.saveSpuBounds(spuBoundTo)
     *      1.)将这个对象转为JSON数据
     *      2.)springCloud在注册中心找到改服务weiyunmall-coupon，给服务发送请求，
     *      将上一步转的对象，放在请求体
     *      3.)对方服务收到请求，请求体的JSON数据
     *      (@RequestBody ) 将请求体里的对象转换
     * 只要json数据模型是兼容的，双方服务无需使用同一个类型
     *
     * @param spuBoundTo
     * @return
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);


}
