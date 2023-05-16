package com.czh.reggie.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.reggie.demo.common.CustomException;
import com.czh.reggie.demo.dao.SetmealDao;
import com.czh.reggie.demo.dto.SetmealDto;
import com.czh.reggie.demo.pojo.Setmeal;
import com.czh.reggie.demo.pojo.SetmealDish;
import com.czh.reggie.demo.service.SetmealDishService;
import com.czh.reggie.demo.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealDao,Setmeal> implements SetmealService{
@Autowired
   private SetmealDishService setmealDishService;
    /**
     * 新增套餐同时保存套餐和菜品的关联关系，操作多张表需要加事务注解
     * 1. 保存套餐信息，操作setmeal表，执行insert操作
     * 2. 保存套餐和菜品的关联关系表，操作setmeal_dish表，执行insert操作
     * @param setmealDto
     */

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //存套餐信息，操作setmeal表，执行insert操作
        this.save(setmealDto);
        // 保存套餐和菜品的关联关系表
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //list里增加dishid,foreach的箭头函数
        // 每个菜品口味增加id
        setmealDishes.forEach(setmealDish->setmealDish.setSetmealId(setmealDto.getId()));

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐同时删除套餐和菜品的关联数据
     * 1. 查询套餐状态，确定套餐是否在售的状态
     * 2. 如果不能删除，抛出一个业务异常
     * 3. 如果可以删除，先删除套餐的信息(数组的方法)
     * 4. 删除关系表中的数据
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 1. 查询套餐状态，确定套餐是否在售的状态,查询数组直接用in
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId, ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(setmealLambdaQueryWrapper);
        //   2. 如果不能删除，抛出一个业务异常
        if (count>0){
            throw new CustomException("套餐再售卖不能删除");
        }
        // 3. 如果可以删除，先删除套餐的信息(必须是主键才能使用)
        this.removeByIds(ids);
        // 4. 删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getDishId,ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);


    }
    /**
     * 停售功能,同时修改套餐和菜品的关联数据
     * 1. 停售菜品
     * 2. 停售套餐和菜品的关联数据
     */
    @Override
    @Transactional
    public void stopStatus(List<Long> ids) {
        //
        UpdateWrapper<Setmeal> setmealUpdateWrapper = new UpdateWrapper<>();
        setmealUpdateWrapper.lambda()
                .set(Setmeal::getStatus,0)
                .in(Setmeal::getId,ids);
        this.update(setmealUpdateWrapper);

    }

    @Override
    public void startStatus(List<Long> ids) {
        UpdateWrapper<Setmeal> setmealUpdateWrapper = new UpdateWrapper<>();
        setmealUpdateWrapper.lambda()
                .set(Setmeal::getStatus,1)
                .in(Setmeal::getId,ids);
        this.update(setmealUpdateWrapper);
    }
}
