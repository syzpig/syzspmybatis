package com.syz.spring.aop.config;

import com.syz.spring.aop.dao.MemberDaoImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 实现javacongfing技术。就是宅这个类中去初始化bean。类似于xml。表示这个类就是个xml
 * spring官网有解释https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#beans-java
 * 这一1.12. Java-based Container Configuration菜单里
 */
@Configuration
@ComponentScan("com.syz.spring.aop.dao")  //开始扫描包，把类扫面到spring容器中去
@EnableAspectJAutoProxy   /*实现aop功能  与xml配置一样  加个这个注解就可以实现AOP功能*/
public class Appconfig {

}
