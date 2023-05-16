package com.czh.reggie.demo.dto;

import com.czh.reggie.demo.pojo.Dish;
import com.czh.reggie.demo.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

    public DishDto(String categoryName, Integer copies) {
        this.categoryName = categoryName;
        this.copies = copies;
    }

    public DishDto() {
    }
}
