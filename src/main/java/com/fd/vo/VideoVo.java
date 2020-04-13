package com.fd.vo;

import com.alibaba.druid.wall.Violation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoVo {

    private String id;
    private String videoTitle; //视频标题
    private String description; //简介
    private String path; //路径
    private String cover; //封面
    private Date uploadTime; //上传时间
    private Integer likeCount; //点赞数
    private String cateName; //分类名
    private String userPicImg;//user头像


    private String categoryId;
    private String userId;
    private  String userName;

    private Integer playCount;
    private boolean isAttention;

    private List<VideoVo> videoList;




}
