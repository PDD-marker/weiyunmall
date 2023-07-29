package com.taotao.weiyunmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.common.utils.PageUtils;
import com.taotao.weiyunmall.product.entity.AttrEntity;
import com.taotao.weiyunmall.product.vo.AttrGroupRelationVo;
import com.taotao.weiyunmall.product.vo.AttrRespVo;
import com.taotao.weiyunmall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author guyu
 * @email ljt10086123@gmail.com
 * @date 2023-05-27 10:27:06
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] attrGroupRelationVos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    /**
     * 在指定的所有属性集合里面，挑出检索属性
     * @param attrIds
     * @return
     */
    List<Long> selectSearchAttrIds(List<Long> attrIds);
}

