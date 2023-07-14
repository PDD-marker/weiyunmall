package com.taotao.weiyunmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.common.utils.PageUtils;
import com.taotao.weiyunmall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author guyu
 * @email ljt10086123@gmail.com
 * @date 2023-05-30 10:39:01
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

