package com.taotao.weiyunmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.common.utils.PageUtils;
import com.taotao.weiyunmall.product.entity.BrandEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author guyu
 * @email ljt10086123@gmail.com
 * @date 2023-05-27 10:27:06
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);

    List<BrandEntity> getBrandsByIds(List<Long> brandId);

}

