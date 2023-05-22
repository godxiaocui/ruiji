package com.czh.reggie.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.reggie.demo.dao.ShoppingCartDao;
import com.czh.reggie.demo.pojo.ShoppingCart;
import com.czh.reggie.demo.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartDao, ShoppingCart>  implements ShoppingCartService {
}
