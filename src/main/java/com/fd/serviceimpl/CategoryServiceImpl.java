package com.fd.serviceimpl;

import com.fd.annotation.AddCache;
import com.fd.annotation.AddLog;
import com.fd.annotation.DelCahe;
import com.fd.dao.CategoryMapper;
import com.fd.entity.Category;
import com.fd.entity.CategoryExample;
import com.fd.entity.User;
import com.fd.service.CategoryService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;



    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Map<String, Object> queryFirstCate(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<>();
        //封装数据
        //总条数   records
        CategoryExample example = new CategoryExample();
        //查询条件
        example.createCriteria().andLevelsEqualTo("1");


        Integer records = categoryMapper.selectCountByExample(example);
        map.put("records",records);
        //总页数   total   总条数/每页展示条数  是否有余数
        Integer total = records % rows==0? records/rows:records/rows+1;
        map.put("total",total);

        //当前页   page
        map.put("page",page);

        //数据     rows
        //参数  忽略条数,获取几条
        RowBounds rowBounds = new RowBounds((page-1)*rows,rows);
        //根据条件进行分页查询
        List<Category> list = categoryMapper.selectByExampleAndRowBounds(example, rowBounds);

        map.put("rows",list);

        return map;

    }

    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Map<String, Object> queryTowCate(Integer page, Integer rows,String parentId) {
        HashMap<String, Object> map = new HashMap<>();

        //查询条件
        CategoryExample example = new CategoryExample();
        example.createCriteria().andParentIdEqualTo(parentId).andLevelsEqualTo("2");

        //总条数   records
        Integer records = categoryMapper.selectCountByExample(example);
        //总页数   total   总条数/每页展示条数  是否有余数
        Integer total = records % rows==0? records/rows:records/rows+1;
        //参数  起始条,获取几条
        RowBounds rowBounds = new RowBounds((page-1)*rows,rows);

        List<Category> categories = categoryMapper.selectByExampleAndRowBounds(example, rowBounds);

        map.put("records",records);
        map.put("total",total);
        //当前页   page
        map.put("page",page);
        map.put("rows",categories);
        return map;
    }


    @DelCahe
    @AddLog(value = "修改分类")
    @Override
    public void updateCate(Category category) {
        categoryMapper.updateByPrimaryKeySelective(category);
    }


    @DelCahe
    @AddLog(value = "添加类别")
    @Override
    public void addCate(Category category,String parentId) {
        if (category.getLevels().equals("1")){
            category.setId(UUID.randomUUID().toString().replace("-",""));
            category.setParentId("0");
            //添加一级类别
            categoryMapper.insertSelective(category);
        }else {
            //添加二级类别
            category.setParentId(parentId);
            category.setId(UUID.randomUUID().toString().replace("-",""));
            categoryMapper.insertSelective(category);
        }


    }


    @DelCahe
    @AddLog(value = "删除类别")
    @Override
    public HashMap<String,Object> deleteCate(Category category) {
        HashMap<String, Object> map = new HashMap<>();
        //判断删除的是什么类别
        Category cate = categoryMapper.selectOne(category);
        System.out.println(cate);

        //一级类别 判断是否有二级类别 有 返回提示信息 不能删除  ==判断引用地址 equals比较内容
        if (cate.getLevels().equals("1")){
            // 是一级类别
            CategoryExample example = new CategoryExample();
            example.createCriteria().andParentIdEqualTo(category.getId());

            int count = categoryMapper.selectCountByExample(example);

            if (count==0){
                //没有直接删除
                categoryMapper.deleteByPrimaryKey(category);
                map.put("status","200");
                map.put("message","删除成功");
            }else {
                //有二级类别
                map.put("status","400");
                map.put("message","该类别下有子类别");
            }
        }else {
            //二级类别
            categoryMapper.deleteByPrimaryKey(cate);
            map.put("status","400");
            map.put("message","二级已删");
        }
        return map;
    }
}
