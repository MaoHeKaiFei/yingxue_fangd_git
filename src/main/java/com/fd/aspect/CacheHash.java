package com.fd.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.util.Set;

@Configuration
@Aspect
public class CacheHash {

    @Autowired
    RedisTemplate redisTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;


   // @Around("@annotation(com.fd.annotation.AddCache)")
    public Object addCache(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("添加缓存");
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //将大key进行序列化
        redisTemplate.setKeySerializer(stringRedisSerializer);
        //将小key进行序列化
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        StringBuilder sb = new StringBuilder();

        //key,value   类型
        //key 类的全限定名+方法名+参数名（实参）
        //value :缓存的数据  String

        //KEY  Hash<Key,value>
        // 类全限定名   (方法名+参数名（实参）,数据)

        //获取类的全限定名
        String className = joinPoint.getTarget().getClass().getName();

        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        sb.append(methodName);
        //获取参数
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            sb.append(arg);
        }
        //拼接key   小key 方法名+参数
        String key = sb.toString();


        //取出key
        Boolean aBoolean = redisTemplate.opsForHash().hasKey(className, key);

        HashOperations hashOperations = redisTemplate.opsForHash();
        Object result =null;
        //去Redis判断key是否存在
        if(aBoolean){
            //存在  缓存中有数据  取出数据返回结果
            result = hashOperations.get(className,key);
        }else{
            //不存在   缓存中没有   放行方法得到结果
            result = joinPoint.proceed();

            //拿到返回结果  加入缓存
            hashOperations.put(className,key,result);
        }
        return result;
    }

    //@After("@annotation(com.fd.annotation.DelCahe)")
    public void fulshRedis(JoinPoint joinPoint){

        //清空缓存
        //获取类的全限定名
        String className = joinPoint.getTarget().getClass().getName();

        //删除该类下所有的数据
        redisTemplate.delete(className);

    }

}


