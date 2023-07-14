package com.taotao.weiyunmall.order.dao;

import com.taotao.weiyunmall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author guyu
 * @email ljt10086123@gmail.com
 * @date 2023-05-30 10:39:01
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
