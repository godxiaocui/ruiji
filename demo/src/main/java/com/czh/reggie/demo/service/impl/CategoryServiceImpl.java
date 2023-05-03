package com.czh.reggie.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.reggie.demo.common.CustomException;
import com.czh.reggie.demo.dao.CategoryDao;
import com.czh.reggie.demo.pojo.Category;
import com.czh.reggie.demo.pojo.Dish;
import com.czh.reggie.demo.pojo.Setmeal;
import com.czh.reggie.demo.service.CategoryService;
import com.czh.reggie.demo.service.DishService;
import com.czh.reggie.demo.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {
    /**
     * 根据id重写
     * @param id
     */
    @Autowired
    private DishService dishService;
    @Autowired
    public SetmealService setmealService;
    @Override
    public void remove(Long id) {
        // 查看分类是否关联菜品，如果关联跑出异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
         dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
         if (dishService.count(dishLambdaQueryWrapper)>0){
                // 抛出异常
             throw new CustomException("关联菜品不能删除");
         }
        // 查看分类是否关联套餐，如果关联跑出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        if(setmealService.count(setmealLambdaQueryWrapper)>0){
            throw new CustomException("关联套餐不能删除");
        }
        // 都不满足就直接删除，调用父类的方法
        super.removeById(id);

    }
}
