spring 2.5之后支持springconfig  实现零配置。以前支持xml配置

springAOP因此支持多种风格主要xml和AspectJ（其实在spring中叫做javaconfig  他是以javaconfig去实现的）等，spring是以AspectJ
代码的风格去实现的，spring去借鉴Aspectj的代码风格去实现，容易理解，因为xml会比较难理解。因此spring借鉴以AspectJ代码风格
去实现。其实spring自己也写了一个实现方式，但比较复杂，因此spring后面去点掉了。AspectJ有实现aop的技术和样子，但是spring仅仅
借助参考 了AspectJ实现方式也就是样子，并没有实现AspectJ的技术，spring觉得他长得好看，比较好用直接拿过来用了,但是
只是使用它的风格，并没有使用它的aop实现技术。spring自己实现的AOP功能。可以认为Aspect技术比spring还牛逼。因此使用时，要引入Aspect架包。
这里实现下AspectJ风格的方式去实现。

官网：https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html