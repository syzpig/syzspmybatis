package com.syz.springmvc.controller;


import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *定义controller层   也就是springmvc的控制器
 *
 * 用法：
 * 1.怎么让spring知道这个controller呢？也就是spring认为他是一个controller（控制器）
 * controller控制器的配置有两种：1.使用xml  2.使用注解
 *
 * 从流程图看，用户的请求最先到达DispatcherServlet，这个是spring已经提供好了，所以直接配置就可以用
 * 从远码看DispatcherServlet最终继承httpServlet，所以可以认为DispatcherServlet就是一个servlet,因此就可以当servlet用法使用，那么
 * servlet的使用，就要去web.xml等xml中去做配置了。不做配置这个servlet是找不到了。所以在web.xml.如项目：web.xml
 *
 * 下面演示xml方式：
 * 1.首先实现Controller类，这个可以发现这个类最先请求的事前端控制器，这个控制器最终继承的是httpServlet
 * 2.所以在web.xml指定这个controller，用于拦截。
 * 3.添加配置spring配置文件
 */
public class BeanController implements Controller {

    @Override
    public ModelAndView handleRequest(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse) throws Exception {
        System.out.printf("BeanController runn ..........");
        return null;
    }
}
