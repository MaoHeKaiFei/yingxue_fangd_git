package com.fd.service;

import com.fd.entity.Log;

import java.util.Map;

public interface LogService {


    void deleteLog(String id);

    Map<String ,Object> queryAllByPage(Integer page, Integer rows);

    void addLog(Log log);

    void  updateLog(Log log);
}
