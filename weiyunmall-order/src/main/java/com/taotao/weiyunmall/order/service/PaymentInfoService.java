package com.taotao.weiyunmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.common.utils.PageUtils;
import com.taotao.weiyunmall.order.entity.PaymentInfoEntity;

import java.util.Map;

/**
 * 支付信息表
 *
 * @author guyu
 * @email ljt10086123@gmail.com
 * @date 2023-05-30 10:39:01
 */
public interface PaymentInfoService extends IService<PaymentInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

