package com.czh.reggie.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czh.reggie.demo.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜品分类的mp接口
 * 1. 需要加mapper注解
 */
@Mapper
public interface CategoryDao extends BaseMapper<Category> {
}
