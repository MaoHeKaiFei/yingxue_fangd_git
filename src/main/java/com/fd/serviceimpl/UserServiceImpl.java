package com.fd.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.fd.annotation.AddCache;
import com.fd.annotation.AddLog;
import com.fd.annotation.DelCahe;
import com.fd.dao.UserMapper;
import com.fd.entity.City;
import com.fd.entity.Month;
import com.fd.entity.User;
import com.fd.entity.UserExample;
import com.fd.service.UserService;
import com.fd.vo.UserVo;
import io.goeasy.GoEasy;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.DomainEvents;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.util.*;


@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;



    @AddCache
    @Override
    public List<User> queryAllUser() {
        List<User> users = userMapper.selectAll();
        return users;
    }


    @AddCache
    @Override
    public Map<String, Object> selectByPage(Integer page, Integer rows) {
        HashMap<String , Object> map = new HashMap<>();
        //相当于是一个条件，没有条件对所有数据进行分页
        // 创建 Example对象 负责条件查询
        Example example = new Example(User.class);

        //分页查询： 参数： 起始页,获取几条数据
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        //总条数
        User user = new User();
        Integer records=userMapper.selectCount(user);
        //总页数
        Integer total= records%rows==0? records/rows:records/rows+1;
        //查询
        List<User> users = userMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("page",page);
        map.put("total",total);
        map.put("rows",users);
        map.put("records",records);

        return map;
    }


    @DelCahe
    @AddLog(value = "添加用户")
    @Override
    public String addUser(User user) {
        String id=UUID.randomUUID().toString().replace("-","");
        user.setId(id);
        user.setStatus("1");
        user.setCreateDate(new Date());
        userMapper.insert(user);


        Map<String, Object> map = queryBoyAndGirlByMap();

        String s = JSON.toJSONString(map);
        //配置发送消息的必要配置  参数：regionHost,服务器地址,自己的appKey
        GoEasy goEasy = new GoEasy( "http://rest-hangzhou.goeasy.io", "BC-16892684edb14ba8bbbe075fb4290939");

        //配置发送消息  参数:管道名称（自定义）,发送内容
        goEasy.publish("yingxue",s);

        return id;
    }


    @DelCahe
    @AddLog(value = "删除用户")
    @Override
    public void deleteUser(String id) {
        User user = new User();
        user.setId(id);

        userMapper.deleteByPrimaryKey(user);

    }


    @DelCahe
    @AddLog(value = "修改类别")
    @Override
    public void updateUser(User user) {

        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void uploadImage(MultipartFile headImg, String id, HttpServletRequest request) {
        //1.根据相对路径获取绝对路径
        String realPath = request.getSession().getServletContext().getRealPath("/upload/photo");

        File file = new File(realPath);
        if(!file.exists()){
            file.mkdirs();
        }
        //2获取文件名
        String filename = headImg.getOriginalFilename();

        String newName=new Date().getTime()+"-"+filename;
        //文件上传
        try {
            //3.文件上传
            headImg.transferTo(new File(realPath,newName));
            //4.图片修改
            //修改的条件
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(id);

            User user = new User();
            user.setHeadImg(newName); //设置修改的结果

            //修改
            userMapper.updateByExampleSelective(user,example);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @DelCahe
    @AddLog(value = "上传头像")
    @Override
    public void uploadImageAliyun(MultipartFile headImg, String id, HttpServletRequest request) {
        //将文件转为byte数组
        byte[] bytes=null;
        try {
            headImg.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //获取文件名h
        String fileName=headImg.getOriginalFilename();//指定上传文件名
        String newName=new Date().getTime()+fileName;

        //将文件上传到阿里云
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "<yourAccessKeyId>";
        String accessKeySecret = "<yourAccessKeySecret>";
        String bucket="";//存储空间

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);


// 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, newName, new File("<yourLocalFile>"));

// 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
// ObjectMetadata metadata = new ObjectMetadata();
// metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
// metadata.setObjectAcl(CannedAccessControlList.Private);
// putObjectRequest.setMetadata(metadata);

// 上传文件。

        ossClient.putObject(putObjectRequest);
// 关闭OSSClient。
        ossClient.shutdown();

        //图片信息的修改

    }

    @AddCache
    @Override
    public List<UserVo> queryByUserDetail(String userId) {
        List<UserVo> userVos = userMapper.queryByUserDetail(userId);
        return userVos;

    }

    @AddCache
    @Override
    public Map<String, Object> queryBoyAndGirlByMap() {

        List<City> boyCities = userMapper.queryBoyByMap();
       Map<String, Object> map = new HashMap<>();
        map.put("boy",boyCities);

        List<City> girls = userMapper.querygirlByMap();
        map.put("girl",girls);
        return map;
    }


    @AddCache
    @Override
    public Map<String, Object> queryBoyAndGirlByBar() {

        List<Month> boys = userMapper.queryBoyByBar();

        List<Month> girls = userMapper.queryGirlByBar();

        Map<String, Object> map = new HashMap<>();


        return null;
    }


}
