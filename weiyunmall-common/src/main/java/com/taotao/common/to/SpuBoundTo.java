package com.taotao.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @quther 孤郁
 */
@Data
public class SpuBoundTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
