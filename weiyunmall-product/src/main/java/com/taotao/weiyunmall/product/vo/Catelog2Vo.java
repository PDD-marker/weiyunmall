package com.taotao.weiyunmall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version 1.0
 * @quther 孤郁
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Catelog2Vo {
    private String catalog1Id; //1级父分类

    private List<Catelog3Vo> catalog3List; //三级子分类

    private String id;

    private String name;

    //
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Catelog3Vo{
        private String catalog2Id;

        private String id;

        private String name;
    }
}
