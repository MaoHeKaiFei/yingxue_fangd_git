package com.fd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    private String id;
    private  String username;
    private  String picImg;
    private String phone;
    private String password;
    private String introduction;
    private String sat;
    private String status;
    private String wechat;
    private Date registerDate;
    //学分
    private Double credit;
}
