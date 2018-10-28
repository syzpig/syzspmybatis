package com.syz.spring.aop.dao;

import org.springframework.stereotype.Component;

@Component("dao")  //指定这个类在spring中的类名  取时则是通过这个方式去取  因为通过实现spring不能再通过类名去拿了
public class JDKdlDaoImpl implements JDKdlDao{
    public final void query(){
        System.out.println("spring-query-logic");
    }

    public void update(){
        System.out.println("spring-update-logic");
    }
}
