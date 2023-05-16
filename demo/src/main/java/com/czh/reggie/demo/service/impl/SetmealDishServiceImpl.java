package com.czh.reggie.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.reggie.demo.dao.SetmealDishDao;
import com.czh.reggie.demo.pojo.SetmealDish;
import com.czh.reggie.demo.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j

public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishDao , SetmealDish> implements SetmealDishService {
}
