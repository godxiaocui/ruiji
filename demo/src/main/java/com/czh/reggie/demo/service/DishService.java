package com.czh.reggie.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czh.reggie.demo.dto.DishDto;
import com.czh.reggie.demo.pojo.Dish;

public interface DishService extends IService<Dish> {
    /**
     * 同时向菜品表和口味表添加数据
     */
    public void saveDishFlavour(DishDto dto);
    // id查询
    public  DishDto getByIdWithDishFlavour(Long id);
    // 同时更新两个
    public void updateDishFlavour(DishDto dto);
}
