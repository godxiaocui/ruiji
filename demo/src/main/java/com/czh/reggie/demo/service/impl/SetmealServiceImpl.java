package com.czh.reggie.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.reggie.demo.dao.SetmealDao;
import com.czh.reggie.demo.pojo.Setmeal;
import com.czh.reggie.demo.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealDao,Setmeal> implements SetmealService{
}
