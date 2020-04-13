package com.fd.repository;

import com.fd.entity.Emp;
import com.fd.entity.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

//泛型   <操作对象类型,序列化主键的类型>
public interface VideoRepository extends ElasticsearchRepository<Video,String> {


    List<Video> findByTitle(String name);
}
