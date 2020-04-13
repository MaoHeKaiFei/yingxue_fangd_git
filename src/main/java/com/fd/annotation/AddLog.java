package com.fd.annotation;

import java.lang.annotation.*;


@Target({ElementType.METHOD})  //在方法上使用
@Retention(RetentionPolicy.RUNTIME) //运行时生效
public @interface AddLog {

    String value();

    String name() default "";
}
