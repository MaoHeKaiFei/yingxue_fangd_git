package com.fd.service;

import com.fd.entity.Category;
import com.fd.entity.Video;
import com.fd.vo.CateVo;
import com.fd.vo.VideoVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface VideoService {


    Map<String,Object> queryAllVideo(Integer page,Integer rows);

    String addVideo(Video video);


    HashMap<String , Object> deleteVideo(Video video);


    void updateVideo(Video video);

    void uploadVideo(MultipartFile path, String id, HttpServletRequest request);

    List<VideoVo> queryVideoByDescTime();

    List<VideoVo> queryByLikeVideoName(String content);

    List<VideoVo> queryByVideoDetail(String videoId,String cateId,String userId);

    List<CateVo> queryAllCategory();

    List<VideoVo> queryCateVideoList(String cateId);

    List<VideoVo> queryByUserMoving(String userId);

    List<VideoVo> queryByUserVideo(String userId);

    List<Video> querySearch(String content);
}
