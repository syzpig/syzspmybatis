package com.syz.springmvc.framework.mvcframework.annotation;

import java.lang.annotation.*;

/**
 *Created by syz on 2018/10/31 9:53
 */
@Target(ElementType.TYPE)  //添加注解的作用域范围  作用于类
@Retention(RetentionPolicy.RUNTIME) //添加他的生命周期，表示他在运行阶段去作用
@Documented  //然后配置他是一个可见的
public @interface SyzController {
    String value() default "";  //给注解添加默认值
}
