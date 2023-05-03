package com.czh.reggie.demo.common;

/**
 * 基于threadlocal的工具类，用户保存和获取当前的登陆用户id
 * 作用范围是某一线程之内
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal=new ThreadLocal<Long>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return  threadLocal.get();
    }
}
