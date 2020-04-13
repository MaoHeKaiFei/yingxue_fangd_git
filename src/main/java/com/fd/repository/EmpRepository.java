package com.fd.repository;

import com.fd.entity.Emp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


//泛型   <操作对象类型,序列化主键的类型>
public interface EmpRepository extends ElasticsearchRepository<Emp,String> {


}
