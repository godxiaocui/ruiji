package com.czh.reggie.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czh.reggie.demo.common.R;
import com.czh.reggie.demo.dto.SetmealDto;
import com.czh.reggie.demo.pojo.Category;
import com.czh.reggie.demo.pojo.Setmeal;
import com.czh.reggie.demo.service.CategoryService;
import com.czh.reggie.demo.service.SetmealDishService;
import com.czh.reggie.demo.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    /**
     * 保存套餐
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto ){
        setmealService.saveWithDish(setmealDto);
        return R.success("成功");
    }
    /**
     * 分页查询
     * setmealDto
     * Request URL: http://localhost:8080/setmeal/page?page=1&pageSize=10&name=123
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        // 正常分页查询
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(name != null,Setmeal::getName,name);
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo,setmealLambdaQueryWrapper);
        // 多余字段需要拷贝完在查
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        ArrayList<SetmealDto> setmealDtos = new ArrayList<>();
        for (Setmeal item : records)
             {
                 SetmealDto setmealDto1 = new SetmealDto();
                 BeanUtils.copyProperties(item,setmealDto1);
                 Long categoryId = item.getCategoryId();
                 Category byId = categoryService.getById(categoryId);
                 // 查询到有值
                 if (byId!=null){
                     String categoryName=byId.getName();
                     setmealDto1.setCategoryName(categoryName);
                 }
                 setmealDtos.add(setmealDto1);
             }
        setmealDtoPage.setRecords(setmealDtos);


        return R.success(setmealDtoPage);
    }

    /**
     * 删除套餐，前端传入的 http://localhost:8080/setmeal？id=123213213213321,123213213213
     * 1. 前端传入多个id可以用数组或者list接受，需要加RequestParam 注解
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("套餐数据删除成功");

    }

    /**
     * 停售功能
     */
    @PostMapping("/status/0")
    public R<String> stopSales(@RequestParam  List<Long> ids){
        setmealService.stopStatus(ids);
        return R.success("停售成功");
    }

    @PostMapping("/status/1")
    public R<String> startSales(@RequestParam  List<Long> ids){
        setmealService.startStatus(ids);
        return R.success("开启成功");
    }
    /**
     *
     *
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        setmealLambdaQueryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(setmealLambdaQueryWrapper);
        return R.success(list);
    }
}
