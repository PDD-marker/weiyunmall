package com.taotao.weiyunmall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.taotao.weiyunmall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @version 1.0
 * @quther 孤郁
 */
@Data
public class AttrGroupWithAttrsVo {
//            "attrGroupId": 1,
//            "attrGroupName": "主体",
//            "sort": 0,
//            "descript": "主体",
//            "icon": "dd",
//            "catelogId": 225,
//            "attrs":
    /**
     * 分组id
     */
    @TableId
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
