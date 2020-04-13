package com.fd.controller;


import com.fd.entity.Category;
import com.fd.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;


    @ResponseBody
    @RequestMapping("firstCate")
    public Map<String,Object> getFirstCate(Integer page,Integer rows){
        Map<String, Object> map = categoryService.queryFirstCate(page, rows);
        return map;
    }


    @ResponseBody
    @RequestMapping("twoCate")
    public Map<String,Object> getTwoCate(Integer page,Integer rows,String parentId){
        Map<String, Object> map = categoryService.queryTowCate(page, rows, parentId);
        //根据一级类别查询二级类别  select all where id=一级类别
        return map;
    }

    @ResponseBody//以json格式返回
    @RequestMapping("edit")
    public Object editUser(Category category, String oper, String parentId){
        HashMap<String, Object> map = new HashMap<>();
        if(oper.equals("add")){
            categoryService.addCate(category,parentId);
        }

        if(oper.equals("edit")){
            categoryService.updateCate(category);
        }

        if(oper.equals("del")){
            map=categoryService.deleteCate(category);
            Object message = map.get("message");
        }
        return map;

    }


}
