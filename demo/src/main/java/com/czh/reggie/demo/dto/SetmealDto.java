package com.czh.reggie.demo.dto;

import com.czh.reggie.demo.pojo.Setmeal;
import com.czh.reggie.demo.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
