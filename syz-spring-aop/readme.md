spring 2.5之后支持springconfig  实现零配置。以前支持xml配置

springAOP因此支持多种风格主要xml和AspectJ（其实在spring中叫做javaconfig  他是以javaconfig去实现的）等，spring是以AspectJ
代码的风格去实现的，spring去借鉴Aspectj的代码风格去实现，容易理解，因为xml会比较难理解。因此spring借鉴以AspectJ代码风格
去实现。其实spring自己也写了一个实现方式，但比较复杂，因此spring后面去点掉了。这里实现下AspectJ风格的方式去实现。

官网：https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html