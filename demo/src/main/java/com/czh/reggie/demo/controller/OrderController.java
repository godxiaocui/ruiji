package com.czh.reggie.demo.controller;

import com.czh.reggie.demo.common.R;
import com.czh.reggie.demo.pojo.Orders;
import com.czh.reggie.demo.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;
@PostMapping("/submit" )
    public R<String> submit(@RequestBody Orders orders){
    ordersService.order(orders);
        return R.success("successfully");
    }

}
