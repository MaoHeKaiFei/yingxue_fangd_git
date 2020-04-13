package com.fd;


import com.fd.dao.UserMapper;
import com.fd.entity.City;
import com.fd.entity.Month;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class javaTest {


    @Autowired
    UserMapper userMapper;


    @Test
    public void test1(){

        /*
        *
        map.put("title", Arrays.asList("1月","2月","3月","4月","5月","6月"));
        map.put("boys", Arrays.asList(100,200,500,700,200,600));
        map.put("girls", Arrays.asList(10,20,50,70,20,60));*/

        List<Month> boys = userMapper.queryBoyByBar();

        List<Month> girls = userMapper.queryGirlByBar();


    }
}
