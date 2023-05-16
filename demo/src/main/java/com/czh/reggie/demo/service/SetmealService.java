package com.czh.reggie.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czh.reggie.demo.dto.SetmealDto;
import com.czh.reggie.demo.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐同时删除套餐和菜品的关联数据
     */
    public void removeWithDish(List<Long> ids);
    /**
     * 停售功能,同时修改套餐和菜品的关联数据
     */
    public void stopStatus(List<Long> ids);

    /**
     * 开售功能,同时修改套餐和菜品的关联数据
     */
    public void startStatus(List<Long> ids);
}
