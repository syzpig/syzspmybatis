package com.syz.spring.aop.test;

import com.syz.spring.aop.config.Appconfig;
import com.syz.spring.aop.dao.JDKdlDao;
import com.syz.spring.aop.dao.JDKdlDaoImpl;
import com.syz.spring.aop.dao.MemberDaoImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class JDKTest {
    public static void main(String[] args) {
        //AnnotationConfigApplicationContext这个类可以理解为，通过注解Annotation来配置应用程序的上下文
        //还有一种就是xml
        ApplicationContext ac = new AnnotationConfigApplicationContext(Appconfig.class);//Appconfig.class加入这个就代表spring容器初始化完成
        JDKdlDao memberDao = (JDKdlDao) ac.getBean("dao");//从spring容器中拿出我们需要的bean
        memberDao.query();
        //这里就是JDK动态代理实现方式，
        // 而Cglib则是通过继承去实现，，但是继承有个缺点，对final方法不能重写，因为，当我们执行带
        //final方法的时候，他的切面还是成功了,(这里包含cglib实现原理 通过继承关系)，则说明spring默认用的jdk动态代理，因为
        //jdk动态代理是通过接口实现，所以final对他没有任何影响的
        //但是我们可以强制使用cglib代理  使用注解@EnableAspectJAutoProxy(proxyTargetClass = true)
        //当值为ture时cglib为false是jdk动态代理
    }
}
