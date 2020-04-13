package com.fd.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;

@Configuration
@Aspect
public class CacheString {

    @Autowired
    RedisTemplate redisTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    //@Around("execution(* com.fd.serviceimpl.*.query*(..))")
    public Object addCache(ProceedingJoinPoint joinPoint) throws Throwable {
        //序列化解决乱码
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        StringBuilder sb = new StringBuilder();

        //key value
        //获取全限定名
        String className = joinPoint.getTarget().getClass().getName();

        //获取方法名
        String monthName = joinPoint.getSignature().getName();
        sb.append(className);
        sb.append(monthName);
        //或取参数
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            sb.append(arg);
        }
        //拼接k
        String key = sb.toString();

        /*判断redis中是否存在当前key
        存在 直接在缓存中获取
        不存在 保存缓存返回结果*/
        Boolean aBoolean = redisTemplate.hasKey(key);

        Object o=null;
        ValueOperations valueOperations = redisTemplate.opsForValue();
        if (aBoolean){
             o= valueOperations.get(key);
        }else {
            //放行方法得到结果
             o = joinPoint.proceed();
             //添加缓存
            valueOperations.set(key,o);

        }

        return o;
    }

    //@After("execution(* com.fd.serviceimpl.*.*(..)) && !execution(* com.fd.serviceimpl.*.query*(..))")
    public void fulshRedis(JoinPoint joinPoint){

        //类的全限定名
        String className = joinPoint.getTarget().getClass().getName();

        //获取所有的key
        Set keys = redisTemplate.keys("*");
        for (Object key : keys) {
           if(key.toString().contains(className)){
               stringRedisTemplate.delete(key.toString());
           }
        }


    }

}


