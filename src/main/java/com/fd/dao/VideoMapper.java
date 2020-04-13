package com.fd.dao;

import com.fd.entity.Category;
import com.fd.entity.Video;
import com.fd.entity.VideoExample;
import java.util.List;

import com.fd.vo.CateVo;
import com.fd.vo.VideoVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface VideoMapper extends Mapper<Video> {
    List<VideoVo>  queryByReleaseTime();


    List<VideoVo>  queryByLikeVideoName(String content);

    List<VideoVo> queryByVideoDetail(String videoId,String cateId,String userId);

    List<CateVo> queryAllCategory();

    List<VideoVo> queryCateVideoList(String cateId);

    List<VideoVo> queryByUserMoving(String userId);

    List<VideoVo> queryByUserVideo(String userId);
}