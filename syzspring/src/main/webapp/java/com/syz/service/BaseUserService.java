package com.syz.service;

import com.syz.dao.BaseUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseUserService {
    @Autowired
    private BaseUserDao baseUserDao;

    public void query() {
        System.out.println(baseUserDao.query());
    }
}
