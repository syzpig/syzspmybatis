package com.syz.spring.aop.test;

import com.syz.spring.aop.config.Appconfig;
import com.syz.spring.aop.dao.MemberDaoImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 测试MemberDaoImpl类加上@Component注解是否被spring管理
 */
public class Test {
    public static void main(String[] args) {
        //AnnotationConfigApplicationContext这个类可以理解为，通过注解Annotation来配置应用程序的上下文
        //还有一种就是xml
        ApplicationContext ac = new AnnotationConfigApplicationContext(Appconfig.class);//Appconfig.class加入这个就代表spring容器初始化完成
        MemberDaoImpl memberDao = ac.getBean(MemberDaoImpl.class);//从spring容器中拿出我们需要的bean
        memberDao.query();
        //这里就是spring构建的基本环境，就是这么简单，只不过网上被复杂化了。
    }
}
