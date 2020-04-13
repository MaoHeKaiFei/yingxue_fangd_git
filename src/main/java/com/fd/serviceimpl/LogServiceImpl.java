package com.fd.serviceimpl;

import com.fd.annotation.AddCache;
import com.fd.dao.LogMapper;
import com.fd.entity.Log;
import com.fd.entity.LogExample;
import com.fd.service.LogService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Transactional
@Service
public class LogServiceImpl implements LogService {
    @Autowired
    LogMapper logMapper;


    @Override
    public void deleteLog(String id) {

    }


    @Override
    public Map<String, Object> queryAllByPage(Integer page, Integer rows) {

        HashMap<String , Object> map = new HashMap<>();
        // 创建 Example对象 负责条件查询
        Example example = new Example(Log.class);

        map.put("page",page);
        //分页查询： 参数： 起始页,获取几条数据
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        //设置分页的逻辑
        //总条数
        int records = logMapper.selectCountByExample(example);
        System.out.println(records);
        map.put("records",records);
        //总页数
        Integer total= records%rows==0? records/rows:records/rows+1;

        List<Log> logs = logMapper.selectByExampleAndRowBounds(example, rowBounds);
        System.out.println(logs);

        //总页数
        map.put("total",total);
        map.put("rows",logs);

        return map;
    }

    @Override
    public void addLog(Log log) {

    }

    @Override
    public void updateLog(Log log) {

    }
}
