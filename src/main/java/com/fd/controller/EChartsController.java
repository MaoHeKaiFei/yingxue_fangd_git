package com.fd.controller;


import com.fd.entity.City;
import com.fd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("eCharts")
public class EChartsController {

    @Autowired
    UserService userService;

    //通过条形统计图展示男每月的下载量
    // select month(create_date),COUNT(id) FROM yx_user where sex='女' GROUP BY month(create_date)
    @RequestMapping("userBar")
    public Map<String, Object> queryByBar(){
       HashMap<String, Object> map = new HashMap<>();
        //将月份封装成集合
        map.put("title", Arrays.asList("1月","2月","3月","4月","5月","6月"));
        map.put("boys", Arrays.asList(100,200,500,700,200,600));
        map.put("girls", Arrays.asList(10,20,50,70,20,60));

        return map;

    }


    //select city name,count(id) value FROM yx_user where sex='女' GROUP BY city
    @RequestMapping("userMap")
    public Map<String,Object> queryByMap(){
/*
        HashMap<String, Object> map = new HashMap<>();

        //根据性别分组查询  where=男  group by="cityName"
        ArrayList<City> boysCities = new ArrayList<>();
        boysCities.add(new City("北京",500));
        boysCities.add(new City("河南",800));
        boysCities.add(new City("湖南",300));
        boysCities.add(new City("湖北",600));
        boysCities.add(new City("山东",100));

        map.put("boy",boysCities);


        ArrayList<City> girlsCities = new ArrayList<>();
        girlsCities.add(new City("黑龙江",400));
        girlsCities.add(new City("吉林",800));
        girlsCities.add(new City("山西",700));
        girlsCities.add(new City("重庆",600));
        girlsCities.add(new City("海南",500));


        map.put("girl",girlsCities);*/

        Map<String, Object> map = userService.queryBoyAndGirlByMap();

        System.out.println(map);
        return map;


    }


}
