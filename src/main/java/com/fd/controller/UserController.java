
package com.fd.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.fd.dao.UserMapper;
import com.fd.entity.User;
import com.fd.service.UserService;
import com.fd.util.AliyunSendPhoneUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @ResponseBody//以json格式返回
    @RequestMapping("showAll")
    public List<User> selectAll(){
        List<User> users = userService.queryAllUser();
        return users;
    }

    @ResponseBody
    @RequestMapping("selectByPage")
    public Map<String,Object> queryByPage(Integer page,Integer rows){
        Map<String, Object> map = userService.selectByPage(page, rows);
        return map;
    }

    @ResponseBody//以json格式返回
    @RequestMapping("edit")
    public String  editUser(User user,String oper,String id){
        System.out.println(user);
        String s=null;
        if(oper.equals("add")){
             s = userService.addUser(user);

        }

        if(oper.equals("edit")){
            userService.updateUser(user);
        }

        if(oper.equals("del")){
            userService.deleteUser(id);
        }
        return s;
    }

    @RequestMapping("uploadUser")
    public void uploadUser(MultipartFile headImg, String id, HttpServletRequest request) {
            userService.uploadImage(headImg,id,request);
    }


    @RequestMapping("sendPhoneCode")
    @ResponseBody
    public String sendPhoneCode(String phone){

        //获取随机数
        String random = AliyunSendPhoneUtil.getRandom(6);

        System.out.println("存储验证码："+random);

        //发送验证码
        String message = AliyunSendPhoneUtil.sendCode(phone, random);

        System.out.println(message);
        return message;
    }

    //用户导出
    @RequestMapping("output")
    public void output(){

        List<User> users = userMapper.selectAll();

        System.out.println(users);

        //导出的参数：标题 工作表名
        ExportParams params = new ExportParams("用户信息", "用户");
        //配置工作表参数：导出参数对象 导出数据对象（类型） 倒数数据集合
        Workbook workbook = ExcelExportUtil.exportExcel(params,User.class,users);

        try {
            //指定导出位置和文件名
            workbook.write(new FileOutputStream(new File("D://output//userPoi.xls")));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
