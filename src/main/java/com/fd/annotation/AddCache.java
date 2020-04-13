package com.fd.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD) //加在方法上
@Retention(RetentionPolicy.RUNTIME) //保留到运行时
public @interface AddCache {
}
