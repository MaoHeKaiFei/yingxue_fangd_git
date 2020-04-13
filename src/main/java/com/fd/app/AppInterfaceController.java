package com.fd.app;



import com.fd.entity.Video;
import com.fd.result.CommonResult;
import com.fd.service.UserService;
import com.fd.service.VideoService;
import com.fd.util.AliyunSendPhoneUtil;
import com.fd.vo.CateVo;
import com.fd.vo.UserVo;
import com.fd.vo.VideoVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("app")
public class AppInterfaceController {

    @Resource
    UserService userService;

   @Resource
    VideoService videoService;

    @RequestMapping("getPhoneCode")
    public CommonResult getPhoneCodes(String phone){
        String random = AliyunSendPhoneUtil.getRandom(6);
        String s = AliyunSendPhoneUtil.sendCode(phone, random);

        System.out.println("phone_code"+phone+"/"+random+s);

        if(s.equals("发送成功")){
            return new CommonResult().success(s,phone);
        }else {
            return new CommonResult().failed(s);
        }
    }


    /*根据时间排序查询所有视频数据*/
    @RequestMapping("queryByReleaseTime")
    public CommonResult queryByReleaseTime(){
        try {
            List<VideoVo> videoVos = videoService.queryVideoByDescTime();
            System.out.println("首页"+videoVos);
            return new CommonResult().success("查询成功",videoVos);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed("查询失败","[]");
        }

    }


    /*首页根据用户输入的内容进行检索*/
    @RequestMapping("queryByLikeVideoName")
    public CommonResult queryByLikeVideoName(String content){
        try {
            List<VideoVo> videoVos = videoService.queryByLikeVideoName(content);
            return new CommonResult().success("查询成功",videoVos);
        } catch (Exception e) {
            e.printStackTrace();
            return  new CommonResult().failed("查询失败","[]");
        }
    }


    /*用户发布视频*/
    @RequestMapping("save")
    public CommonResult save(Video video,String userId){
        try {
            video.setUserId(userId);
            videoService.addVideo(video);

            System.out.println(video+"\t"+userId);
            return new CommonResult().success("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  new CommonResult().failed("添加失败",null);
        }
    }


    /*.浏览视频信息 后台管理员登陆接口*/
    @RequestMapping("queryByVideoDetail")
    public CommonResult queryByVideoDetail(String videoId,String cateId,String userId){

        try {
            List<VideoVo> videoVos = videoService.queryByVideoDetail(videoId, cateId, userId);
            return new CommonResult().success("查询成功",videoVos);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed("查询失败",null);
        }
    }


    /*查询所有分类包含一级类别和二级类别*/
    @RequestMapping("queryAllCategory")
    public CommonResult queryAllCategory(){
        try {
            List<CateVo> cateVos = videoService.queryAllCategory();
            return new CommonResult().success("查询成功",cateVos);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed("查询失败",null);
        }
    }

    /*后台管理员登陆接口 .查询二级分类视频接口*/
    @RequestMapping("queryCateVideoList")
    public CommonResult queryCateVideoList(String cateId){
        try {
            List<VideoVo> videoVos = videoService.queryCateVideoList(cateId);
            return new CommonResult().success("查询成功",videoVos);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed("查询失败",null);
        }
    }


    /*查看该登录用户关注其他用户发布的视频*/
    @RequestMapping("queryByUserMoving")
    public CommonResult queryByUserMoving(String userId){
        try {
            List<VideoVo> videoVos = videoService.queryByUserMoving(userId);
            return new CommonResult().success("查询成功",videoVos);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed("查询失败",null);
        }
    }

    /*展示登录用户部分信息*/
    @RequestMapping("queryByUserDetail")
    public CommonResult queryByUserDetail(String userId){
        try {
            List<UserVo> userVos = userService.queryByUserDetail(userId);
            return new CommonResult().success("查询成功",userVos);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed("查询失败",null);
        }
    }

    /*查看用户自己发布的视频*/
    @RequestMapping("queryByUserVideo")
    public CommonResult queryByUserVideo(String userId){
        try {
            List<VideoVo> videoVos = videoService.queryByUserVideo(userId);
            return new CommonResult().success("查询成功",videoVos);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed("查询失败",null);
        }
    }
}
