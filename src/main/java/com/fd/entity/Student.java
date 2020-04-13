package com.fd.entity;


import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {


    @Excel(name = "ID")
    private String id;

    @Excel(name = "姓名")
    private String username;

    @Excel(name = "头像",type = 2,width = 20)
    private String headImg;
}
