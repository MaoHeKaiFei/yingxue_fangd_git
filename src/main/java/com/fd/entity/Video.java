package com.fd.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;




@Document(indexName = "yingxue",type = "video")
@Table(name = "yx_video")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video implements Serializable {


    @Id
    private String id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer ="ik_max_word" )
    private String title;

    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer ="ik_max_word" )
    private String brief;

    @Field(type = FieldType.Keyword)
    private String path;

    @Field(type = FieldType.Keyword)
    private String cover;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @Field(type = FieldType.Date)
    @Column(name = "publish_date")
    private Date publishDate;

    @Field(type = FieldType.Keyword)
    private String categoryId;

    @Field(type = FieldType.Keyword)
    private String groupId;

    @Field(type = FieldType.Keyword)
    private String userId;

    @Field(type = FieldType.Keyword)
    @Transient
    private Integer likeNum;

    @Field(type = FieldType.Keyword)
    @Transient
    private Integer playNum;

    public Video(String id, String title, String brief, String path, String cover, Date publishDate, String categoryId, String groupId, String userId) {
        this.id = id;
        this.title = title;
        this.brief = brief;
        this.path = path;
        this.cover = cover;
        this.publishDate = publishDate;
        this.categoryId = categoryId;
        this.groupId = groupId;
        this.userId = userId;
    }
}