package com.czh.reggie.demo.controller;

import com.czh.reggie.demo.common.R;
import com.czh.reggie.demo.dto.DishDto;
import com.czh.reggie.demo.service.DishFlavorService;
import com.czh.reggie.demo.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

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
}
