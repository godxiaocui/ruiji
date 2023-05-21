package com.czh.reggie.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czh.reggie.demo.common.R;
import com.czh.reggie.demo.pojo.User;
import com.czh.reggie.demo.service.UserService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/login")
    public R<String> login(@RequestBody Map map, HttpServletRequest request ){
        // 获取手机号
        String phone = map.get("phone").toString();
        // 判断手机号是否是新用户
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone,phone);
        User one = userService.getOne(userLambdaQueryWrapper);
        // 是新用户自动注册
        if (null == one) {
            one =new User();
            one.setPhone(phone);
            one.setStatus(1);
            userService.save(one);

        }
        request.getSession().setAttribute("user",one.getId());
        return R.success("成功");
    }
}
