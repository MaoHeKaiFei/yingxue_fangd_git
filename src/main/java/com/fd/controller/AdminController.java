package com.fd.controller;

import com.fd.entity.Admin;
import com.fd.service.AdminService;
import com.fd.util.ImageCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * (Admin)表控制层
 *
 * @author makejava
 * @since 2020-03-26 19:50:36
 */
@Controller
@RequestMapping("admin")
public class AdminController {
    /**
     * 服务对象
     */
    @Autowired
    private AdminService adminService;

    /**
     * 通过主键查询单条数据
     */

    @ResponseBody//以json格式返回
    @RequestMapping("login")
    public  Map<String, Object>  selectOne(String enCode,Admin admin) {
        System.out.println(enCode+"     "+admin);
        Map<String, Object> map = adminService.adminLogin(admin, enCode);
        return map;

    }


    @RequestMapping("getImage")
    public void getAdminImage(HttpServletResponse response,HttpSession session){
        //使用工具类获取验证码
        String code = ImageCodeUtil.getSecurityCode();
        session.setAttribute("code",code);
        //将验证码渲染在图片上
        BufferedImage image = ImageCodeUtil.createImage(code);
        //将生产成的验证码响应到游览器上 设置响应类型
        response.setContentType("image/png");
        //输出图片（获取输出流）
        ServletOutputStream os=null;
        try {
            os = response.getOutputStream();
            ImageIO.write(image, "png", os);
            os.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @RequestMapping("loginOut")
    public String loginOut(HttpSession session){
        //安全退出时需要清除缓存
        session.removeAttribute("admin");
        return "redirect:login/login.jsp";
    }

}