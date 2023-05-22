package com.czh.reggie.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.reggie.demo.dao.OrderDetailDao;
import com.czh.reggie.demo.pojo.OrderDetail;
import com.czh.reggie.demo.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailDao, OrderDetail> implements OrderDetailService {
}
