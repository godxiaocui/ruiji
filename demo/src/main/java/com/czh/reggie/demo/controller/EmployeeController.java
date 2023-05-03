package com.czh.reggie.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czh.reggie.demo.common.R;
import com.czh.reggie.demo.pojo.Employee;
import com.czh.reggie.demo.service.EmployeeService;
import jdk.nashorn.internal.ir.CallNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     *  登陆操作
     * @param employee
     * @return
     */
    // 前端的值是一个post请求，封装是一个json，所以我们要用对象接受
    // 同时需要一个RequestBody 正常封装json
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody  Employee employee) {
    /**
     * 1. 将页面提交的password进行解密
     * 2.根据name查询数据库
     * 3.查询不到返回失败
     * 4.密码对比，密码不对返回登录失败
     * 5.查看员工状态，禁用则返回员工已经用
     * 6.登陆成功返回成功，员工id存入session
     */
        System.out.println(employee);
    // 1. 将页面提交的password进行解密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
     // 2.   根据name 查询数据库
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        // 姓名是唯一约束，所以可以直接插
        log.info(employee.getName());
        Employee one = employeeService.getOne(employeeLambdaQueryWrapper);
     // 3.查询不到返回失败
        if (null == one){
            return R.error("登陆失败");
        }
       //  4.密码对比，密码不对返回登录失败
        if (!one.getPassword().equals(password)){
            return R.error("登陆失败");
        }
        // 5.查看员工状态，禁用则返回员工已经用
        if (one.getStatus()==0){
            return R.error("账号警用");
        }
        // 6.登陆成功返回成功，员工id存入session
        request.getSession().setAttribute("employee",one.getId());
        return R.success(one);
    }

    /**
     * 退出操作
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        // 1. 清楚session中的id
        request.getSession().removeAttribute("employee");
        return  R.success("退出成功") ;
    }

    /**
     * 修改操作
     * 1. 接受前端参数
     * 2. 设置初始密码，进行md5加密,string需要改成getbyte
     * 3. 设置初始时间
     * 4. 获取创建人id
     * 5. 登陆数据库
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("Saving employee+{}",employee);
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());
        Long id = (Long)request.getSession().getAttribute("employee");
        //employee.setCreateUser(id);
        //employee.setUpdateUser(id);
        boolean save = employeeService.save(employee);
        return R.success("创建成功");
    }
    /**
     * 分页查询
     * 1, 构造分页构造器
     * 2. 条件构造器
     *  a. 过滤条件
     *  b, 排序条件
     *3.运行一下分页查询
     *
     */
    @GetMapping("/page")
    public  R<Page> listEmployee(Integer page, Integer pageSize,String name){
        log.info("page+{}, pageSize+{},name+{}",page,pageSize,name);
        // 构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        // 条件构造器
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        employeeLambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo, employeeLambdaQueryWrapper);
        return  R.success(pageInfo);
    }
    /**
     * 修改操作(根据id修改信息)
     * 1. 设置跟新时间
     * 2. 设置跟的人
     * 3. long类型的数据，json数据回损失精度
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        Long id=(Long)request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(id);
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    /**
     * 编辑员工信息
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}