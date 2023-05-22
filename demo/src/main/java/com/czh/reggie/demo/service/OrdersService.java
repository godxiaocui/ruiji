package com.czh.reggie.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czh.reggie.demo.pojo.Orders;

public interface OrdersService extends IService<Orders> {
    /**
     * 用户下单
     * 1. 获取用户id
     * 2。用户购物车信息
     * 3。 像订单表插入数据
     * 4。 订单明细表多条数据
     * 5。 清空数据
     */
    public void order(Orders orders);
}
