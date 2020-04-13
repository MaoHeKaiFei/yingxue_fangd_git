package com.fd.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

//指定实体类映射数据库的表名
@Table(name = "yx_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {
    @Id
    private String id;

    private String cateName;

    private String levels;

    private String parentId;


}