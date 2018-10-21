package com.syz.test;

import com.syz.config.AppConfig;
import com.syz.service.BaseUserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {
    public static void main(String[] args) {
        //首先初始化spring环境
        AnnotationConfigApplicationContext annotationConfigApplicationContext
                = new AnnotationConfigApplicationContext(AppConfig.class); //传入配合文件类
        //然后通过这个annotationConfigApplicationContext bean把servic拿出来
        BaseUserService baseUserService = annotationConfigApplicationContext.getBean(BaseUserService.class);
        baseUserService.query();
    }
}
