package com.fd.controller;

import com.fd.dao.VideoMapper;
import com.fd.entity.Video;
import com.fd.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("video")
public class VideoController {


    @Autowired
    VideoService videoService;

    @ResponseBody//返回json格式
    @RequestMapping("selectByPage")
    public Map<String ,Object> showVideos(Integer page,Integer rows){
        Map<String, Object> map = videoService.queryAllVideo(page, rows);

        System.out.println(map);
        return map;
    }


    @ResponseBody
    @RequestMapping("edit")
    public Object editVideo(Video video,String oper,String id){
        System.out.println("测试"+video);
        String s=null;
        if(oper.equals("add")){
            s = videoService.addVideo(video);
        }

        if(oper.equals("edit")){
                videoService.updateVideo(video);
        }

        if(oper.equals("del")){
            HashMap<String, Object> map = videoService.deleteVideo(video);
            return map;
        }
        return s;
    }


    @RequestMapping("uploadVideo")
    public void uploadUser(MultipartFile path, String id, HttpServletRequest request) {
        videoService.uploadVideo(path,id,request);
    }


    @ResponseBody//返回json格式
    @RequestMapping("querySearch")
    public List<Video> querySearch(String content){

        List<Video> videos = videoService.querySearch(content);
        System.out.println(videos);
        return videos;
    }
}
