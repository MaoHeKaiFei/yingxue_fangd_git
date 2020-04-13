package com.fd.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * (Admin)实体类
 *
 * @author makejava
 * @since 2020-03-26 19:50:35
 */
@Table(name = "admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements Serializable {
    private static final long serialVersionUID = -64191032140346370L;

    @ExcelIgnore
    @Id
    private String id;

    @Excel(name = "username",needMerge = true)
    private String username;

    @Excel(name = "password",needMerge = true)
    private String password;


    @ExcelCollection(name = "员工表")
    private List<Emp> emps;


}