package com.czh.reggie.demo.service;
import com.czh.reggie.demo.dto.DishDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.*;

@SpringBootTest
class DishServiceTest {
    //定义要用的类
    private DishService dishService;
    // 定义开启mock
    @BeforeEach
    public void setUp() {
        dishService = mock(DishService.class);
    }
    @Test
    void getByIdWithDishFlavour() {
        DishDto dishDto = new DishDto("czh",123);
        int a = 1;
        long b = (long)a;
        when(dishService.getByIdWithDishFlavour(b)).thenReturn(dishDto);
        System.out.println(dishService.getByIdWithDishFlavour(b).getCategoryName());
    }
}