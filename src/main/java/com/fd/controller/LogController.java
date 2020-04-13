package com.fd.controller;


import com.fd.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@RequestMapping("log")
@Controller
public class LogController {
    @Autowired
    LogService logService;


    @ResponseBody
    @RequestMapping("selectByPage")
    public Map<String,Object> queryByPage(Integer page,Integer rows){
        Map<String, Object> map = logService.queryAllByPage(page, rows);
        return map;
    }

}
