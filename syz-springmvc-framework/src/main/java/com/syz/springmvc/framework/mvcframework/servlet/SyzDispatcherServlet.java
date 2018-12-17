package com.syz.springmvc.framework.mvcframework.servlet;

import com.syz.springmvc.framework.mvcframework.annotation.SyzAutowired;
import com.syz.springmvc.framework.mvcframework.annotation.SyzController;
import com.syz.springmvc.framework.mvcframework.annotation.SyzRequestMapping;
import com.syz.springmvc.framework.mvcframework.annotation.SyzService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * 首先首写一个DispatcherServlet
 */
//这里要继承一个HttpServlet，这才能被web容器识别它就是一个servlet;然后还要重写三个方法
public class SyzDispatcherServlet extends HttpServlet {
    private Properties contextConfig = new Properties();
    private List<String> classNames = new ArrayList<>();
    //模拟IOC容器  这里我们默认class类型首字母小写作为key ,在用类的实例作为值
    private Map<String, Object> ioc = new HashMap<>();

    //定一个handlerMapping容器
    private Map<String, Method> handlerMapping = new HashMap<>();

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
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 Exception" + Arrays.toString(e.getStackTrace()));
        }

    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //开始运行阶段
        if (handlerMapping.isEmpty()) {
            return;
        }
        //不为空，我们就拿到他的url
        String url = req.getRequestURI();//这是个绝对路径
        //改为相对路径
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        if (!this.handlerMapping.containsKey(url)) {
            resp.getWriter().write("404 Not Found!!");
            return;
        }
        //然后根据url拿到对应的method对象
        Method method = this.handlerMapping.get(url);
        Map<String, String[]> param = req.getParameterMap();//实参从这里获取
        //找到方法所在的对象  先拿到method所在的类method.getDeclaringClass() 然后反射获取.getSimpleName()  因此这种方式不是高效的，
        String beanName = lowerFirstCase(method.getDeclaringClass().getSimpleName());

        try {
            //第一个参数表示这方法属于哪个对象，第二个参数表示这个方法需要什么实参(这里参数先写死，对应的方法是DemoAction类中的query方法)
            String name = null;
            if (param.containsKey("name")) {
                name = param.get("name")[0];
            }

            method.invoke(ioc.get(beanName), new Object[]{req, resp, name});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        /* super.init(config);*/
        /*这里就是我们核心原理图中初始化的那几个步骤  代码步骤如下：*/
        //第一步、加载配置文件
        //1.先获取配置文件，通过congfig获取web.xml配置的contextConfigLocation参数，获取主配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //第二步、解析配置文件，扫描所有相关的类  就是获取在配置文件中配置的包路径
        doScanner(contextConfig.getProperty("scanPackage"));
        //第三步、初始化所有相关的类，并保存到IOC中
        doInstance();
        //第四步、完成自动化的依赖注入，也就是DI
        doAutowired();
        //第五步、创建HandlerMapping以及将我们的url和method建立对应关系
        initHandlerMapping();
        //第六步、应该是进入到我们的运行阶段  也就是上面的doPost()或者doGet()方法。然后完成我们的反射调用，最后将结果输出到浏览器


        System.out.println("syz spring mvc is init");

    }

    private void initHandlerMapping() {
        //首先也是从ioc容器开始
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //首先拿到class类，拿到类一切都好做
            Class<?> clazz = entry.getValue().getClass();
            //拿到之后 判断他是不是一个ctl类，不是则直接忽略，因为service等类不做出来
            if (!clazz.isAnnotationPresent(SyzController.class)) {
                continue;
            }

            String baseUrl = "";
            if (clazz.isAnnotationPresent(SyzController.class)) {
                //如果拿到,这获取里面值
                //首先判断requestMapper是否有值
                SyzRequestMapping requestMapping = clazz.getAnnotation(SyzRequestMapping.class);
                baseUrl = requestMapping.value();

            }
            //拿到之后，第二步，就是把所有的方法都要扫到
            Method[] methods = clazz.getMethods(); //getMethods获取的公共方法
            //遍历所有方法
            for (Method method : methods) {
                //判断这个方法上面是否加了requestMappering
                if (!method.isAnnotationPresent(SyzRequestMapping.class)) {
                    continue;//没有加则说明不能被请求到
                }
                //存在 则获取他的值
                SyzRequestMapping requestMapping = method.getAnnotation(SyzRequestMapping.class);
                //拼接路径，注意，路径中已经加了“/” 这里有加了不就重了嘛。所以要处理下，这里通过正则来过滤，不管有多少/都匹配为一个
                String url = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("/+", "/");
                handlerMapping.put(url, method);//这里就完成了url与方法的映射

                System.out.println("Mapped" + url + "," + method);
            }
            //到此写完spring的初始化阶段
        }

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

    private void doScanner(String scanPackage) {
        //我们拿到的事包路径，转化为文件路径，然后就可以拿到一个URL（java.url） ，然后就可以转化为一个文件
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        //接下来把拿到的url转成文件
        File classDir = new File(url.getFile());
        //开始获取这个拿到的文件目录下到底有哪些class文件   这里要迭代，通过递归因为可能是文件包含文件
        for (File file : classDir.listFiles()) {
            //递归判断  是否是文件夹
            if (file.isDirectory()) {//如果是文件夹，继续往下递归扫描
                doScanner(scanPackage + "." + file.getName());
            } else {//如果不是，就把他放入我们定义好容器中
                //判断是否是class文件   这里只是简单判断，其实还有其他好多判断，比如子类等，请看源码
                if (!file.getName().contains(".class")) {
                    continue;
                }
                //这里拿到的是个文件  父包加上拿到文件就拿到文件路径，这时拿到的事class文件，所以把class后缀去掉，就拿到了全称,后放入容器中
                String className = (scanPackage + "." + file.getName().replace(".class", "")).trim();
                classNames.add(className);

            }
            //这里我们就把所有包下的所有class文件变成了所有的类路径；那么接下来的工作就是通过反射。把刚刚所有扫面出来的类
            //全部进行初始化，并且放入IOC容器中
        }

    }

    private void doInstance() {
        //判断扫面的包下是否为空 ,
        if (classNames == null) {
            return;
        }
        //接下来开始通过反射把这个扫描出来的所以class类找出来
        for (String className : classNames) {
            //拿出来第一件事就是反射  ,然后就获取到class对象
            try {
                Class<?> clazz = Class.forName(className);
                //这里我们可以拿到这个包下所有的类，成千上万个，但是这里拿到的类，并不是所有的都要实例化的
                //因此，并不是所有的类都是要实例化的  spring，就是通过判断是否加了注解 @service或@Compant
                //这里就是判断我们模拟的spring注解  如果判断到就把他们弄到IOC容器中
                if (clazz.isAnnotationPresent(SyzController.class)) {
                    //首先把类的名字取出来
                    String beanName = clazz.getSimpleName();//这个SimpleName()就是我们要的类名，不包括包名 ，getName()包括整个完整包名的类名
                    //我们把类名首字母小写做为key，因此处理类名
                    beanName = lowerFirstCase(beanName);
                    ioc.put(beanName, clazz.newInstance());
                } else if (clazz.isAnnotationPresent(SyzService.class)) {
                    //service层稍微麻烦点，因为我们service注入dao的接口，而接口不能实现化，因此我们要把接口的实现注入到service中的这个接口中
                    //1.第一种情况 用类名首字母小写     默认的第一中方式，类名首字母小写，这也是spring规定的
                    //2.第二种情况，自定义命名 例如;@SyzService("demoService") 注入的时候自定义命名优先，如果没有自定义命名，则找首字母小写的
                    //这前两种可以做联合判断的
                    //这里我们首先用自定义命名
                    //先把service上的注解的class类取出来.就拿到了service注解的实例
                    SyzService service = clazz.getAnnotation(SyzService.class);
                    //然后这个实例中会有配置的一些值，把它们取出来
                    String beanName = service.value();
                    if ("".equals(beanName.trim())) {  //因为注解中配置了默认值为空串
                        //如果为空，则说明没有自定义  我们就用默认首字母小写来覆盖他
                        beanName = lowerFirstCase(clazz.getSimpleName());
                    }
                    //然后把它的对象搞出来
                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);
                    //3.第三种情况，接口注入实现类 ；接口的实现类怎么给service中注入的接口的实现类情况呢，用接口的全称作为key,用接口的实现类的实例作为值
                    //把接口进行复制 ，因为接口本身不能实例化 ，所以必须把接口发实现类搞出来
                    Class<?>[] interfaces = clazz.getInterfaces(); //拿到这个实现类下的所有接口都拿到
                    //迭代接口
                    for (Class<?> i : interfaces) {
                        //如果一个接口有多个实现类，怎么玩？那么就判断一下抛个异常
                        if (ioc.containsKey(i.getName())) {
                            try {
                                throw new Exception("The beanName is exists");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        //然后把接口的全名称作为key,class的实例作为值
                        ioc.put(i.getName(), instance); //接口是带包名的，这样才能保证接口的唯一性，而且包名本身首字母小写
                    }
                    //到此初始化结束，接下来依赖注入
                } else {
                    continue;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 手写类名大小写字母转换方法
     */
    private String lowerFirstCase(String beanName) {
        char[] chars = beanName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doAutowired() {
        //首先判断容器是否为空
        if (ioc.isEmpty()) {
            return;
        }
        //不为空，把ioc容器中所有类都取出来
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //1.把类的class对象拿出来,再取出所有的字段/属性
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                //这里要判断，字段上有没有加注解，不是所有的字段都能实例化
                if (!field.isAnnotationPresent(SyzAutowired.class)) {
                    continue;  //当前这里还有其他注入方式，这里就以SyzAutowired注解为例
                }
                //如果加了注解，我们把注解取出来
                SyzAutowired autowired = field.getAnnotation(SyzAutowired.class);
                //把注解的值取出来
                String beanName = autowired.value();
                if ("".equals(beanName)) {
                    //如果为空，则说明他默认使用接口或者类名首字母小写
                    //这里先考虑直接用借口注入
                    beanName = field.getType().getName();//这就是接口的全称
                }

                //在做下面的操作前，要给一些private等受保护的字段你进行授权，否则在类或者包外没有权限去进行复制修改
                field.setAccessible(true);//这就是强制复制

                //接下来就是 去ioc容器中把这个对象取出来，然后再把上面这个字段对应的值赋值给这个对象
                try {
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
                //到此di阶段也完成
            }
        }
    }
}
