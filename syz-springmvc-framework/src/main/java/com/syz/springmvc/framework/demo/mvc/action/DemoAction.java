package com.syz.springmvc.framework.demo.mvc.action;

import com.syz.springmvc.framework.demo.service.IDemoService;
import com.syz.springmvc.framework.mvcframework.annotation.SyzAutowired;
import com.syz.springmvc.framework.mvcframework.annotation.SyzController;
import com.syz.springmvc.framework.mvcframework.annotation.SyzRequestMapping;
import com.syz.springmvc.framework.mvcframework.annotation.SyzRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SyzController
@SyzRequestMapping("demo")
public class DemoAction {
    @SyzAutowired
    private IDemoService iDemoService;

    @SyzRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response, @SyzRequestParam("name") String name) {
        String result = iDemoService.get(name);
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SyzRequestMapping("/add")
    public void add(HttpServletRequest request,
                    HttpServletResponse response,
                    @SyzRequestParam("a") Integer a,
                    @SyzRequestParam("b") Integer b) {
        try {
            response.getWriter().write(a + "+" + b + "=" + (a + b));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SyzRequestMapping("/remove")
    public void remove(HttpServletResponse response, HttpServletRequest request, @SyzRequestParam("id") Integer id) {

    }
}
