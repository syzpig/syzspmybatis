package com.syz.spring.core.test;

import com.syz.spring.core.config.Config;
import com.syz.spring.core.service.IndexServiceImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class test {
    public static void main(String[] args) {
        /*//在spring中是不能直接new这个IndexServiceImpl对象的，因为new出来就不受spring管理了，他不在spring容器中了
        // ，因此使用spring方式，提供的重要的类
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:spring.xml");//这一步其实是初始化spring环境或者是上下文
        //就是有了这段代码，就可以做spring进行开发了。就是开始提供spring环境。执行这个代码就是初始化spring环境
        IndexServiceImpl indexService = (IndexServiceImpl) beanFactory.getBean("service");
        //这是spring要求我们实现的方式。
        indexService.index();*/


        //2零配置实现方式
        AnnotationConfigApplicationContext annotationConfigApplicationContext =new AnnotationConfigApplicationContext(Config.class);
        IndexServiceImpl indexService = (IndexServiceImpl) annotationConfigApplicationContext.getBean("service");
        indexService.index();
    }
}
