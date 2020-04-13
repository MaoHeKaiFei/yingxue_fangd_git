package com.fd.serviceimpl;

import com.fd.annotation.AddCache;
import com.fd.annotation.AddLog;
import com.fd.entity.Admin;
import com.fd.dao.AdminDao;
import com.fd.service.AdminService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (Admin)表服务实现类
 *
 * @author makejava
 * @since 2020-03-26 19:50:35
 */
@Transactional
@Service()
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDao adminDao;

    @Resource
    HttpSession session;
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */


    @Override
    public Admin queryById(String id) {
        return this.adminDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Admin> queryAllByLimit(int offset, int limit) {
        return this.adminDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param admin 实例对象
     * @return 实例对象
     */
    @Override
    public Admin insert(Admin admin) {
        this.adminDao.insert(admin);
        return admin;
    }

    /**
     * 修改数据
     *
     * @param admin 实例对象
     * @return 实例对象
     */
    @Override
    public Admin update(Admin admin) {
        this.adminDao.update(admin);
        return this.queryById(admin.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.adminDao.deleteById(id) > 0;
    }



    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Map<String,Object> adminLogin(Admin admin,String enCode) {
        //用map存返回信息
        Map<String, Object> map = new HashMap<>();


        //验证验证码
        if (enCode.equals(session.getAttribute("code"))) {
            //验证用户
            Admin queryAdmin = adminDao.queryByUsername(admin);
            if (queryAdmin != null) {
                //验证密码
                if (queryAdmin.getPassword().equals(admin.getPassword())) {
                    session.setAttribute("admin", queryAdmin);
                    map.put("status","200");
                    map.put("message","登陆成功");
                } else {
                    map.put("status", "400");
                    map.put("message", "密码错误");
                }
            } else {
                map.put("status", "400");
                map.put("message", "用户名错误");
            }
        } else {
            map.put("status", "400");
            map.put("message", "验证码错误");
        }
        return map;

    }
}
