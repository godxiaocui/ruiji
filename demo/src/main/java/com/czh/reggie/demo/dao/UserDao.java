package com.czh.reggie.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czh.reggie.demo.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {
}
