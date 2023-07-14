package com.taotao.weiyunmall.product.dao;

import com.taotao.weiyunmall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author guyu
 * @email ljt10086123@gmail.com
 * @date 2023-05-27 10:27:06
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
