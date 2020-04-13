package com.fd.service;

import com.fd.entity.Category;

import java.util.HashMap;
import java.util.Map;

public interface CategoryService {

    Map<String,Object> queryFirstCate(Integer page,Integer rows);

    Map<String,Object> queryTowCate(Integer page,Integer rows,String parentId);

    void updateCate(Category category);

    void addCate(Category category,String parentId);

    HashMap<String,Object> deleteCate(Category category);
 }

