package com.czh.reggie.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czh.reggie.demo.common.R;
import com.czh.reggie.demo.dto.DishDto;
import com.czh.reggie.demo.pojo.Category;
import com.czh.reggie.demo.pojo.Dish;
import com.czh.reggie.demo.service.CategoryService;
import com.czh.reggie.demo.service.DishFlavorService;
import com.czh.reggie.demo.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 保存菜品
     * @param dto
     * @return
     */

@PostMapping
    public R<String>  save(@RequestBody  DishDto dto){
        dishService.saveDishFlavour(dto);
        return  R.success("新增菜品成功") ;
    }

    /**
     * 分页查询
     * 难点多表的联查，有个字段再别的表里，join 开销比较大
     * 步骤
     * 1. 先按照正常的分页查询语句进行查询
     * 2. 在定义的新的dto的表里将之前的Page除了records拷贝进去，其中某几列里面没有数据在新的表里
     * 3. 将原来dto的page信息里的records进行数据替换，foreach函数
     * 4. 先将dish拷贝进dto，在循环里在查询某个值，对dto的字段进行赋值
     * @param page
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        // 正常分页查询
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<Dish>();
        queryWrapper.like(name!=null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);
        //foreach
        //     flavors.forEach(flavor->flavor.setDishId(id));
        // 对象拷贝,第三个参数是 records不拷贝某个属性
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> results=new ArrayList<>();
        // 遍历赋值
        for (Dish item:records
             ) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            // 查询到category对象里的name
             if(category!=null) {
                 String categoryName=category.getName();
                 dishDto.setCategoryName(categoryName);
             }

            results.add(dishDto);
        }
        dishDtoPage.setRecords(results);

        return  R.success(dishDtoPage);
    }

    /**
     * 修改信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto byIdWithDishFlavour = dishService.getByIdWithDishFlavour(id);
        return  R.success(byIdWithDishFlavour);

    }

    /**
     * 修改菜品
     * @param dto
     * @return
     */

    @PutMapping
    public R<String>  update(@RequestBody  DishDto dto){
        dishService.saveDishFlavour(dto);
        return  R.success("新增菜品成功") ;
    }

}
