package com.fd.service;

import com.fd.entity.City;
import com.fd.entity.User;
import com.fd.vo.UserVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface UserService {

    //展示所有
    List<User> queryAllUser();

    //分页展示
    Map<String,Object> selectByPage(Integer page,Integer rows);

    String  addUser(User user);

    void  deleteUser(String id);

    void  updateUser(User user);

    void uploadImage(MultipartFile headImg, String id, HttpServletRequest request);

    void uploadImageAliyun(MultipartFile headImg, String id, HttpServletRequest request);

    List<UserVo> queryByUserDetail(String userId);


    Map<String,Object> queryBoyAndGirlByMap();

    Map<String,Object> queryBoyAndGirlByBar();


}
