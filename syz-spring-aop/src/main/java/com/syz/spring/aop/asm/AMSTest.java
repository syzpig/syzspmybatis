package com.syz.spring.aop.asm;

import com.syz.spring.aop.util.SyzClassLoader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;


/**
 * asm技术 用于 生成一个class文件，但是他不是一个具体的对象，而是一个二进制字节码内容的文件
 *
 */
public class AMSTest {
    /**
     * 生成一个类的方法
     */
    static ClassWriter createClassWriter(String className) {
        //操作类  生成一个class文件，但是他不是一个具体的对象，而是一个二进制内容的文件
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        //声明一个类，使用JDK1.8版本，public的类，父类是UserDao,没有实现任何接口
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null, "com/syz/spring/aop/asm/UserDao", null);
        //初始化一个无参的构造器
        MethodVisitor constructor = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        constructor.visitVarInsn(Opcodes.AALOAD, 0);
        //执行父类的init的初始化
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/syz/spring/aop/asm/UserDao", "<init>", "()V", false);
        //从当前方法返回void
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(1, 1);
        constructor.visitEnd();
        return cw;


    }
    /**
     *给类创建一个方法
     */
    static byte[] createVoidMethod(String className, String message) throws Exception {
        //注意，这里需要把classname里面的.改成/，如com.asm.Test改成com/asm/Test
        ClassWriter cw = createClassWriter(className.replace(".", "/"));
        //创建run方法
        //（）V 表示函数，无参数，无返回值   创建一个run方法
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "run", "()V", null, null);
        //先获取一个java.io.PrintStream对象
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        //将int float 或String型常量值从常量池中推送至栈顶（此处将message字符串从常量池中推送至栈顶[输出内容]）
        mv.visitLdcInsn(message);
        //执行println方法（执行的是参数为字符串，无返回值的println函数）
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V",false );
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        return cw.toByteArray();
        /**
         *这段代码是创建一个如下这个方法
         * public void run(){
         *      System.out.println("aa");
         * }
         */
    }
    /**
     *测试生成类
     */
  /*  public static void main(String[] args) {
        //测试生成一个类AsmDynamic
        ClassWriter cw = createClassWriter("com/syz/spring/aop/asm/AsmDynamic");
        //创建好类之后创建方法
        try {
            createVoidMethod("com/syz/spring/aop/asm/AsmDynamic","holle syz");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //这里返回的cw只是生成了一个类并没有在内存中存在，并没有字节码，
        byte[] clazzByte = cw.toByteArray();   //需要创建这个类的字节码  就是返回的事个class文件，他并不是在磁盘存在的，
        //字节码有了，怎么变成一个类的呢
        SyzClassLoader syzClassLoader = new SyzClassLoader();
        Class clazz = syzClassLoader.generatorClassByName("com.syz.spring.aop.asm.AsmDynamic", clazzByte);
       //AsmDynamic这个类实际是不存在的，我们看看内存中是否会存在这个类
        System.out.println(clazz.getSimpleName());
       //以及拿出父类的名字看看是否是UserDao,因为我们在
        //生成这个类createClassWriter方法让他继承了UserDao类
        System.out.println(clazz.getSuperclass().getSimpleName());
        //运行发现内存中是存在这个类的，然而我们的idea中项目中并没有这个类，这就是一种无形得代理，一种更高级的代理，因为这个文件都没有，字节码都没有
        //asm工作原理：帮我们生成一段自字节码最终生成一个类出来，那么有类就能new一个对象出来。
    }*/
    /**
     *测试生成类方法
     */
    public static void main(String[] args) {
        Object object = null;
        SyzClassLoader syzClassLoader = new SyzClassLoader();
        try {
            Class clazz = syzClassLoader.generatorClassByName("com.syz.spring.aop.asm.AsmDynamic", createVoidMethod("com/syz/spring/aop/asm/AsmDynamic","holle syz"));
            object = clazz.newInstance();  //把这个对象new出来
            clazz.getDeclaredMethod("run").invoke(object);//通过反射调用这个方法  invoke(object)这个是反射执行到这个object对象去
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* 总结：
    cglib它底层运用的是asm这个开源技术，然后呢动态生成字节码。
    动态代理与cglib的区别：
    cglib是直接生成一个字节码，直接把字节码生成到内存当中,就是把字节直接加载到内存中
    动态代理是先生成你要的类，然后在用jdk调用编译器去生成class文件（也就是jdk是先生成一个java文件，然后通过调用各种技术
    ，把java生成一个class文件，然后加载到内存当中）
    性能区别：cglib性能效率远远大于jdk动态代理
    */
}
