package com.syz.springmvc.framework.mvcframework.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 首先首写一个DispatcherServlet
 */
//这里要继承一个HttpServlet，这才能被web容器识别它就是一个servlet;然后还要重写三个方法
public class SyzDispatcherServlet extends HttpServlet {
    private Properties contextConfig = new Properties();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //DispatcherServlet中的doGet/doPost首先被浏览器调用  本案例统一让他调用doPost
        this.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //第六步  运行阶段；也就是上面的doPost()或者doGet()方法。然后完成我们的反射调用，最后将结果输出到浏览器
        //到这里我们整个架子应该搭建差不多，接下来就是添加加瓦，然后填入进去。因此接下来我们一个一个的逻辑把他实现，一个一个
        //方法把他们实现，使注解拥有他们该有的功能。
        doDispatch();
    }

    private void doDispatch() {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        /* super.init(config);*/
        /*这里就是我们核心原理图中初始化的那几个步骤  代码步骤如下：*/
        //第一步、加载配置文件
        //1.先获取配置文件，通过congfig获取web.xml配置的contextConfigLocation参数，获取主配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //第二步、解析配置文件，扫描所有相关的类
        doScanner();
        //第三步、初始化所有相关的类，并保存到IOC中
        doInstance();
        //第四步、完成自动化的依赖注入，也就是DI
        doAutowired();
        //第五步、创建HandlerMapping以及将我们的url和method建立对应关系
        initHandlerMapping();
        //第六步、应该是进入到我们的运行阶段  也就是上面的doPost()或者doGet()方法。然后完成我们的反射调用，最后将结果输出到浏览器
    }

    private void initHandlerMapping() {
    }

    private void doLoadConfig(String contextConfigLocation) {
        //这个方法主要是加载配置文件 我们的配置文件不是xml，而是一个properties文件因此要配置一个全局Properties对象的配置变量

        //开始从类路径下获取properties文件
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            contextConfig.load(is);//加载读取配置文件
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //到此完成配置文件的加载
    }

    private void doScanner() {
    }

    private void doInstance() {
    }

    private void doAutowired() {
    }
}
