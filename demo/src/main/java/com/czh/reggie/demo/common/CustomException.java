package com.czh.reggie.demo.common;

/**
 * 自定业务异常
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
