package com.fd.entity;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.annotation.Documented;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;



@Document(indexName = "yingx",type = "emp")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Emp implements Serializable {


    @Id  //映射_id
    @Excel(name = "ID")
    private String id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer ="ik_max_word" )
    @Excel(name = "名字")
    private String username;

    @Field(type = FieldType.Integer)
    @Excel(name = "年龄")
    private Integer age;


    @Field(type=FieldType.Date)
    @Excel(name = "生日",format ="yyyy-MM-dd",width = 20)
    private Date bir;


}
