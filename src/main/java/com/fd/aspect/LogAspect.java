package com.fd.aspect;



import com.fd.annotation.AddLog;
import com.fd.annotation.DelCahe;
import com.fd.dao.LogMapper;
import com.fd.entity.Admin;
import com.fd.entity.Log;
import com.fd.util.UUIDUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@Aspect
@Configuration
public class LogAspect {

    @Autowired
    LogMapper logMapper;

    @Resource
    HttpSession session;


    //@Around("@annotation(com.fd.annotation.AddLog)")
    public Object addLogs(ProceedingJoinPoint joinPoint){

        //谁   时间   操作   是否成功
        Admin admin = (Admin) session.getAttribute("admin");

        //时间
        /*Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formats = format.format(date);*/

        //操作   哪个方法
        String methodName = joinPoint.getSignature().getName();

        //获取方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //获取方法上的注解
        AddLog addLog = method.getAnnotation(AddLog.class);

        //获取注解中属性的值   value
        String methodDes= addLog.value();


        //放行方法
        try {
            Object proceed = joinPoint.proceed();

            String message="success";

            Log log = new Log();
            log.setId(UUIDUtil.getUUID());
            log.setDate(new Date());
            log.setStatus(message);
            log.setOperation(methodName);
            log.setAdminname(admin.getUsername());

           // Log log = new Log(UUID.randomUUID().toString(),admin.getUsername(),new Date(),methodDes+"("+methodName+")",message);
            System.out.println("数据库插入"+log);
            logMapper.insert(log);

            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            String message="error";
            return null;
        }
    }

    //@Around("execution(* com.baizhi.zcn.serviceImpl.*.*(..) ) && !execution(* com.baizhi.zcn.serviceImpl.*.query*(..))")
    public Object addLog(ProceedingJoinPoint joinPoint){

        //谁   时间   操作   是否成功
        Admin admin = (Admin) session.getAttribute("admin");

        //时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formats = format.format(date);

        //操作   哪个方法
        String methodName = joinPoint.getSignature().getName();

        //放行方法
        try {
            Object proceed = joinPoint.proceed();

            String message="success";

            Log log = new Log();

            log.setId(UUIDUtil.getUUID());
            log.setAdminname(admin.getUsername());
            log.setOperation(methodName);
            log.setStatus(message);
            log.setDate(date);

            logMapper.insert(log);

            System.out.println("管理员："+admin+"--时间："+formats+"--操作："+methodName+"--状态："+message);

            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            String message="error";
            return null;
        }
    }

}
