package com.syz.spring.aop.util;
/**
 *jvm把java中的字节生成一个class对象出来，就是ClassLoader，因此我们这里重写，这个方法自己实现
 */
public class SyzClassLoader extends ClassLoader {
    //创建方法调用父类中的方法
    public Class generatorClassByName(String clazzName, byte b[]) {
       return super.defineClass(clazzName, b, 0, b.length);
        //通过这个类名与字节生成一个类出来
    }
}
