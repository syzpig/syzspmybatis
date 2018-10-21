package com.syz.spring.utils;

import com.syz.spring.service.IndexService;
import com.syz.spring.utils.AnnotationConfigApplicationContext;

/**
 * 测试模拟spring 的类 以及用法
 */
public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        IndexService indexService = (IndexService) annotationConfigApplicationContext.getBean("IndexService");
        System.out.println(indexService);
    }
}
