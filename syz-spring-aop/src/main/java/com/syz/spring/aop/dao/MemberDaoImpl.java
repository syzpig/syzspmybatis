package com.syz.spring.aop.dao;

import org.springframework.stereotype.Component;

@Component//让spring管理这个类
public class MemberDaoImpl {
    public void query(){
        System.out.println("spring-query-logic");
    }
}
