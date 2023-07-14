package com.taotao.common.exception;

/**
 * @version 1.0
 * @quther 孤郁
 */

import org.omg.CORBA.UNKNOWN;

/**
 * 由五位组成，前俩位表示业务场景，后三位表示错误码
 * 错误码列表
 * 10：通用
 *      001：参数格式校验
 * 11：商品
 * 12：订单
 * 13：购物车
 * 14：物流
 *
 */
public enum BizCodeEnume {
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    VAILD_EXCEPTION(10001,"数据校验格式校验失败");

    private int code;
    private String message;
    private BizCodeEnume(int code,String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
