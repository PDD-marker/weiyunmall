package com.taotao.weiyunmall.member.feign;

import com.taotao.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version 1.0
 * @quther 孤郁
 */
//@FeignClient注解用于声明该接口为一个远程客户端，要调用远程服务,括号内参数为要调用的服务名称
@FeignClient("weiyunmall-coupon")
public interface CouponFeignService {
    @RequestMapping("/coupon/coupon/member/list")
    public R membercoupons();
}
