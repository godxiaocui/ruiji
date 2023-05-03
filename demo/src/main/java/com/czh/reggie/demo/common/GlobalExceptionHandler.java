package com.czh.reggie.demo.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局的异常捕获
 * 1.@ControllerAdvice AOP 注解
 *       拦截含有这些注解的范围 annotations = {RestController.class}
 * 2. @ResponseBody
 *       最终返回json数据，所哟要用ResponseBody注解
 */
@ControllerAdvice(annotations = {RestController.class,Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 1. 先加注解@ExceptionHandler,作用在定义异常是什么
     * 2. 截取异常信息,的用户名
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public  R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String s = split[2]+"已存在";
            return R.error(s);
        }
        return R.error("为止错误");
    }
}
