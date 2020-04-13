package com.fd.dao;

import com.fd.entity.City;
import com.fd.entity.Month;
import com.fd.entity.User;

import com.fd.vo.UserVo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface UserMapper extends Mapper<User> {
    List<UserVo> queryByUserDetail(String userId);


    List<City> queryBoyByMap();

    List<City> querygirlByMap();


    List<Month> queryBoyByBar();

    List<Month> queryGirlByBar();
}