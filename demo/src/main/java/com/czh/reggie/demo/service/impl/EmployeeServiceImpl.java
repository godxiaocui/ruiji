package com.czh.reggie.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.reggie.demo.dao.EmployeeDao;
import com.czh.reggie.demo.pojo.Employee;
import com.czh.reggie.demo.service.EmployeeService;
import org.springframework.stereotype.Service;


@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao,Employee> implements EmployeeService{
}
