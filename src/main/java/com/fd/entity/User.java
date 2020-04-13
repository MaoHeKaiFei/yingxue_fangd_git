package com.fd.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

//指定实体类映射数据库的表名
@Table(name = "yx_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {


    @Excel(name = "ID",width = 20)
    @Id
    private String id;

    @Excel(name = "用户名")
    private String username;

   @Excel(name = "电话")
    private String phone;

    @Excel(name = "头像",type = 2,width = 20)
    private String headImg;

    @Excel(name = "签名")
    private String sign;

    @Excel(name = "微信")
    private String wechat;

    @Excel(name = "状态")
    private String status;

    @Excel(name = "创建时间",format = "yyyy-MM-dd",width = 40)
    private Date createDate;

    private String city;

    private String sex;

    @Transient
    private Integer videoNum;
    @Transient
    private Integer fansNum;
    @Transient
    private Integer score;

}