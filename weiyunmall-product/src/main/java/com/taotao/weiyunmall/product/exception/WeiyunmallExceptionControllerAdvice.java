package com.taotao.weiyunmall.product.exception;

import com.taotao.common.exception.BizCodeEnume;
import com.taotao.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @quther 孤郁
 */
@Slf4j
//@ResponseBody
//@ControllerAdvice(basePackages = "com.taotao.weiyunmall.product.controller")
@RestControllerAdvice(basePackages = "com.taotao.weiyunmall.product.controller")
public class WeiyunmallExceptionControllerAdvice {

    //商品标识数据校验异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handlerValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题{}，异常类型{}",e.getMessage(),e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String,String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach((item) -> {
            //错误的提示信息
            String defaultMessage = item.getDefaultMessage();
            //错误的属性名称
            String field = item.getField();
            map.put(field, defaultMessage);
        });
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(),BizCodeEnume.VAILD_EXCEPTION.getMessage()).put("data",map);
    }

    //全局异常
//    @ExceptionHandler(value = Throwable.class)
//    public R handleException(Throwable throwable) {
//        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(),BizCodeEnume.UNKNOW_EXCEPTION.getMessage());
//    }
}
