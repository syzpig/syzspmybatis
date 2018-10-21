package com.syz.spring.utils;

import java.io.File;

/**
 * 这个类模拟spring 扫描类的工具类
 */
public class ScanUtil {

    public static void doScan() {
        //这个扫描方法第一步首先要包名。然后根据包名得到类所在的文件路径
        //先默认个死路径，后面改成动态。就是com之前的路径是固定的，可以配置死，之后的就是扫描包对应的路径，把点改成斜杠，就可以获取到下面的路径
        String packagerPath = "D:\\syzSpringCloudProject\\spring-source-code\\syzspmybatis\\syzspringioc\\src\\main\\java\\com\\syz\\spring";
        //现在扫描这个com.syz.spring下的所有文件
        File file = new File(packagerPath);
        String[] chilFile = file.list();//按理说是使用递归，spring使用的就是递归，因为这个包下可能有好多级。这里就一级，所以用list简化
        for (String fileName : chilFile) {
           // System.out.println(fileName);  //这获取的是文件名称，接下来再次获取对应文件名称
            File file1 = new File(packagerPath + "\\" + fileName);
            //按理说这里还有判断还路径是否是目录，等判断，这里省略掉了，可看源码
            String[] clazzFileName = file1.list();
            for (String clazzName : clazzFileName) {
                // 找到获取的文件后把点去掉，截取得到类名
                clazzName = clazzName.substring(0, clazzName.indexOf("."));
                //得到类名，开始进行实例化
                try {
                    Object springObject = Class.forName("com.syz.spring." + fileName + "." + clazzName).newInstance();//获取到名称路径后实例化
                    System.out.println(springObject);
                    //但之前说过spring是只有加了注解的才进行实例化。所以接下来我们要判断是否添加注解才进行实例化
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        ScanUtil.doScan();
    }
}

