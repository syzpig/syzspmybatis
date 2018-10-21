package com.syz.spring.utils;

import java.util.Map;

/**
 *最后模拟spring 的AnnotationConfigApplicationContext类  简单模拟
 */
public class AnnotationConfigApplicationContext {

    //创建默认的构造方法用来在初始化该类时去执行doScan  ，然后getBean方法中的list就会有值
    public AnnotationConfigApplicationContext() {
        ScanUtil.doScan();
    }

    //这里我们只模拟一种方法，通过类名获取
    public Object getBean(String clazzName){
        for (Map<String,Object> map:ScanUtil.list) {
           Object object = map.get(clazzName);
           if (object!=null){
               return object;
           }
        }
        return null;
    }
}
