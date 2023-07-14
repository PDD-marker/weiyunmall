package com.taotao.weiyunmall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @version 1.0
 * @quther 孤郁
 */
@Data
public class PurchaseDoneVo {

    @NotNull
    private Long id; //采购单的id

    private List<PurchaseItemDoneVo> items;

}
