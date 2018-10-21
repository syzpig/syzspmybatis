package com.syz.spring.utils;

import com.syz.spring.anno.Autowired;
import com.syz.spring.anno.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个类模拟spring 扫描类的工具类
 */
public class ScanUtil {
    public static List<Map<String, Object>> list = new ArrayList<>();

    public static void doScan() throws IllegalAccessException {
        //这个扫描方法第一步首先要包名。然后根据包名得到类所在的文件路径
        //先默认个死路径，后面改成动态。就是com之前的路径是固定的，可以配置死，之后的就是扫描包对应的路径，把点改成斜杠，就可以获取到下面的路径
        String packagerPath = "D:\\syzSpringCloudProject\\spring-source-code\\syzspmybatis\\syzspringioc\\src\\main\\java\\com\\syz\\spring";
        //现在扫描这个com.syz.spring下的所有文件
        File file = new File(packagerPath);
        String[] chilFile = file.list();//按理说是使用递归，spring使用的就是递归，因为这个包下可能有好多级。这里就一级，所以用list简化
        for (String fileName : chilFile) {
            // System.out.println(fileName);  //这获取的是文件名称，接下来再次获取对应文件名称
            File file1 = new File(packagerPath + "\\" + fileName);
            //按理说这里还有判断还路径是否是目录,是否是java文件等等，等判断，这里省略掉了，可看源码
            String[] clazzFileName = file1.list();
            for (String clazzName : clazzFileName) {
                // 找到获取的文件后把点去掉，截取得到类名
                clazzName = clazzName.substring(0, clazzName.indexOf("."));
                Object obj;
                //得到类名，开始进行实例化
                try {
                    /*Object springObject = Class.forName("com.syz.spring." + fileName + "." + clazzName).newInstance();//获取到名称路径后实例化
                    System.out.println(springObject);*/
                    //但之前说过spring是只有加了注解的才进行实例化。所以接下来我们要判断是否添加注解才进行实例化
                    //因此上面就不能直接new，要先拿到这个类进行判断，在进行初始化
                    Class clazz = Class.forName("com.syz.spring." + fileName + "." + clazzName);//获取到名称路径后实例化
                    //然后判断或者的这个类是否加上注解Component类,如果加了。则new
                    if (clazz.isAnnotationPresent(Component.class)) {
                        obj = clazz.newInstance();
                        System.out.println(obj.getClass().getName());
                        //接下来模拟springIOC,容器，把new的对象放入spring容器中就是list<Map<String,Object>>中,下面就简单模拟，用个map接，取，先不放list中
                        Map<String, Object> map = new HashMap<>();
                        map.put(clazz.getSimpleName(), obj);
                        list.add(map);
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


        //加入上面都已初始化，开始进行Autowired  注入操作
        //spring的自动装配有几种？ 自动装配是自动注入的其中一种方式，面试常问自动装配几种方式的底层实现原理、区别
        //有四五种，一般是byType,byName
        for (Map<String, Object> temp : list) {
            for (String clazzName : temp.keySet()) {
                Object tempObj = temp.get(clazzName);//这时候拿到的就是spring IOC容器中的真正对象
                //第二步，判断对象中是否需要注入属性
                //也就是判断属性中是否加了Autowired 注解
                //因此通过反射获取属性
                Class tempClazz = tempObj.getClass();
                //这里要得到所有属性，因为我们不知道要得到什么具体属性，所以要得到所以然后判断
                Field[] files = tempClazz.getDeclaredFields();
                for (Field temField : files) {
                    //判断是否加入Autowired注解
                    if (temField.isAnnotationPresent(Autowired.class)) {
                        //如果有，则先获取这个属性的类型，然后找到对应的对象，从而进行注入
                        //这里默认使用自动注入中自动装配方式的bytype的方式注入的判断，如果是byname,我们就知道名字了，然后就可以找到对象类型。因为，按byname形式时，
                        //注入的时候会先指定一个对象的名字。这里是匹配名字，如果名字不对，就匹配不上。如果实在xml配置bean时会指定一个name，
                        //就是这个name，首先这个name（bean）要与要注入的对象id一直，然后就是根据这个将要注入的对象中的需要注入属性的set方法，
                        // 如果set方法名字不对应则，注入不上；如果是按bytype的话，只要类型匹配就行注入成功，跟get,set没关系。
                        //当一个类中拥有多个相同的类型时，就要用byname。
                        //因此这里按byteType
                        //首先获取类型
                        String targetName = temField.getType().getSimpleName();
                        for (Map<String,Object> temp1:list) {
                            for (String clazzName1 : temp1.keySet()) {
                                //判断两个类型是否一致，判断两个对象是否一致，可以根据类的比较和名字比较
                                if(temp1.get(clazzName1).getClass().getSimpleName().equals(targetName)){
                                    //如果相同，我就注入进来 注入到哪呢？就是上面的temField上
                                    temField.setAccessible(true);
                                    temField.set(tempObj,temp1.get(clazzName)); //从哪注入到哪
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        try {
            ScanUtil.doScan();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

