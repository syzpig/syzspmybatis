package com.syz.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 采用注解的方式创建Controller
 * 第一步：添加注解@Controller
 * 第二步：在Spring配置文件中添加开启springMVC注解配置
 * 第三步：添加注解扫描范围   在xml配置了这个标签后，spring可以自动去扫描base-pack下面或者子包下面的java文件，如果扫描到有@Component @Controller@Service等这些注解的类，则把这些类注册为bean
 */
@Controller
public class AnotationController {
    @RequestMapping("test2")
    public ModelAndView handleRequest(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse) throws Exception {
        System.out.printf("AnotationController run ..........");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index.jsp");
        return mv;
    }
}
