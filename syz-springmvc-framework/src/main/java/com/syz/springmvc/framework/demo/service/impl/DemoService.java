package com.syz.springmvc.framework.demo.service.impl;

import com.syz.springmvc.framework.demo.service.IDemoService;
import com.syz.springmvc.framework.mvcframework.annotation.SyzService;

/**
 *核心业务逻辑
 */
@SyzService
public class DemoService implements IDemoService{
    @Override
    public String get(String name) {
        return "my name is "+name;
    }
}
