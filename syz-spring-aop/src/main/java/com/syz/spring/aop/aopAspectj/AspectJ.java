package com.syz.spring.aop.aopAspectj;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 *构建aop切面  看进行看管官网
 */
@Aspect
@Component   //官网上说实现切面要把切面对象放入spring容器中，让视频日宁管理，否则会报错
public class AspectJ {
    /**
     *创建切点  这这种编程风格下官网都有切点实现的语法。
     * 他主要是同个一个方法去实现，但这个方法是没有任何意义的，他只是一个载体。就是能够让我门去写切点的注解
     *  方法实现如下：添加实现注解@Pointcut里面放入spring常用语法execution  就是它能够找到我们返回的任意类型
     */
    @Pointcut("execution(* com.syz.spring.aop.dao.*.*(..))") //就是给这个包下所有的方法都加一个切点
    public void pointCut(){

    }

    /**
     *添加一个通知   在执行前通知，在注解里要添加一个切面方法，然后通知后通知到这个方法里面所代表的的切点上面去
     */
    @Before("pointCut()")
    public void befor(){
        System.out.println("aop before............");
    }

}
