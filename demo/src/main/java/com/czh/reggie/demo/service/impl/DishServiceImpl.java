package com.czh.reggie.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.reggie.demo.dao.DishDao;
import com.czh.reggie.demo.dto.DishDto;
import com.czh.reggie.demo.pojo.Dish;
import com.czh.reggie.demo.pojo.DishFlavor;
import com.czh.reggie.demo.service.DishFlavorService;
import com.czh.reggie.demo.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements DishService {
    /**
     * 实现新增菜品保留口味
     *
     * j加入事务控制
     * @param dto
     */
    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    @Transactional
    public void saveDishFlavour(DishDto dto) {
        // 先保存到菜品信息到菜品表，父类的方法
        this.save(dto);

        // 保存菜品口味主要是flavor的json数组到dishflavor
        // 1. dishid不在flavr中需要讲dishid也保存到flavor中
        Long id = dto.getId();//菜品id
        List<DishFlavor> flavors = dto.getFlavors();// 菜品口味
        // 每个菜品口味增加id
        flavors.forEach(flavor->flavor.setDishId(id));
        // 讲一个集合进行保存
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    public DishDto getByIdWithDishFlavour(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        // 查询当前的值，同时在flavour中查询
        LambdaQueryWrapper<DishFlavor> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(dishLambdaQueryWrapper);
        dishDto.setFlavors(list);
        return dishDto;

    }


}
