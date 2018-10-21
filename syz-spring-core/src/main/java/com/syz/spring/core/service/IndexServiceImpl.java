package com.syz.spring.core.service;

import com.syz.spring.core.dao.IndexDaoImpl;

public class IndexServiceImpl {

    IndexDaoImpl indexDao;  //注入这个dao
    public void setIndexDao(IndexDaoImpl indexDao) {
        this.indexDao = indexDao;
    }

    public void index(){
        System.out.println("IndexServiceImpl-----------");
        indexDao.index();
    }
}
