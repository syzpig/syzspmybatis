package com.syz.spring.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *我们要判断是否添加注解才进行实例化
 *首先要模拟spring注解
 */
@Target(ElementType.TYPE)//表示注解要用到什么地方去
@Retention(RetentionPolicy.RUNTIME)//表示生命周期
public @interface Component {
}


//建好注解之后把他加到service层。来模拟sprig用法