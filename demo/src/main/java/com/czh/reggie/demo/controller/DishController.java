package com.czh.reggie.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czh.reggie.demo.common.R;
import com.czh.reggie.demo.dto.DishDto;
import com.czh.reggie.demo.pojo.Category;
import com.czh.reggie.demo.pojo.Dish;
import com.czh.reggie.demo.pojo.DishFlavor;
import com.czh.reggie.demo.service.CategoryService;
import com.czh.reggie.demo.service.DishFlavorService;
import com.czh.reggie.demo.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.czh.reggie.demo.common.R.success;

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
@Autowired
    RedisTemplate   redisTemplate;
    /**
     * 保存菜品
     * @param dto
     * @return
     */

@PostMapping
    public R<String>  save(@RequestBody  DishDto dto){
        dishService.saveDishFlavour(dto);
        return  success("新增菜品成功") ;
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
        return  success(byIdWithDishFlavour);

    }

    /**
     * 修改菜品
     * @param dto
     * @return
     */


    @PutMapping
    public R<String> update(@RequestBody DishDto dto){
        dishService.updateDishFlavour(dto);
        return R.success("修改菜品成功");
    }

    /**
     *   http://localhost:8080/dish/list?categoryId=1413384954989060097
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        // categoryId 查询
//        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        dishLambdaQueryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
//        // 只要起售状态为1的
//        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
//        // 排序
//        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
//
//        return  R.success(list);
//    }

    /**
     * 新增redis代码
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtoList = null;

        //动态构造key
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();//dish_1397844391040167938_1

        //先从redis中获取缓存数据
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);

        if(dishDtoList != null){
            //如果存在，直接返回，无需查询数据库
            return R.success(dishDtoList);
        }

        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        //如果不存在，需要查询数据库，将查询到的菜品数据缓存到Redis
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);

        return  R.success(dishDtoList);
    }



}
