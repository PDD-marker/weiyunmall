package com.taotao.weiyunmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.common.utils.PageUtils;
import com.taotao.weiyunmall.product.entity.SkuInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * sku信息
 *
 * @author guyu
 * @email ljt10086123@gmail.com
 * @date 2023-05-27 10:27:06
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);

    List<SkuInfoEntity> getSkusBySpuId(Long spuId);
}

