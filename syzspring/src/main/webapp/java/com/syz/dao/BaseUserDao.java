package com.syz.dao;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BaseUserDao {
    @Select("select * from base_user")
    List<Map<String,Object>> query();
 }
