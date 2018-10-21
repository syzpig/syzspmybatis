package com.syz.spring.service;

import com.syz.spring.anno.Autowired;
import com.syz.spring.anno.Component;
import com.syz.spring.dao.IndexDao;

@Component
public class IndexService {

    @Autowired
    private IndexDao indexDao;
    public void index() {
        System.out.println("indexService");
    }

}
