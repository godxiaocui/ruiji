package com.czh.reggie.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czh.reggie.demo.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;
/**
 * mp的操作，是一个接口
 * 步骤1. 接口需要继承BaseMapper
 * 步骤2. BaseMapper需要指定一个范型。就是我们之前定义的实体类
 * 步骤3. 加上mapper注解
 */
@Mapper
public interface EmployeeDao extends BaseMapper<Employee> {
}
