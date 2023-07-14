package com.taotao.weiyunmall.ware.vo;

import lombok.Data;

/**
 * @version 1.0
 * @quther 孤郁
 */
@Data
public class PurchaseItemDoneVo {
    //itemId:1,status:4,reason:""
    private Long itemId;

    private Integer status;

    private String reason;
}
