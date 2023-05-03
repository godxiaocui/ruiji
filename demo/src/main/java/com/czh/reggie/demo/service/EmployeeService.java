package com.czh.reggie.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czh.reggie.demo.pojo.Employee;

/**
 * mp 提供的的service
 * 是用mp的时候需要继承iService接口，同时也指定范型
 */
public interface EmployeeService extends IService<Employee> {
}
