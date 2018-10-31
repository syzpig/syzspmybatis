#**400行代码手写实现spring MVC底层代码**

#首先回忆几个spring 面试题：

1.spring bean生命周期？

2.spring bean是不是线程安全？

3.如何看源码？

#spring核心原理可看做三个阶段
#**1.配置阶段** 一般通过xml或注解    
（1）在web.xml配置spring的DispatcherServlet  他是springmvc的唯一的入口   
（2）在配置好之后，在配置spring的主配置文件application.xml  ；这个主配置文件主要是引导web容器怎么样去加载
 spring，从哪里加载。这个配置文件会去配置spring所有需要加载的东西，这里可以作为入口，也可以作为spring详细的
 配置信息。    
（3）然后还会配置url-patten,一般配置成/*或/   表示我们用一个DispatcherServlet去拦截所有的请求；在此之前我们没用spring
之前，这里配置阶段会有成千上万过servlet，每一个servlet都会配置一个url,那么会导致我们的web.xml会非常的臃肿，非常的庞大。而带来
维护的困难。有了spring mvc以后我们就可以用一个DispatcherServlet解决了所以的问题。
这就是spring在配置阶段做的一个优化。    
##配置阶段完成之后我们进入初始化阶段，而初始化是通过DispatcherServlet的中init方法开始     
#**2.spring 初始化阶段**   
**IOC**  
（1）init方法首先会加载我们配置好的主配置文件。然后会去解析这个配置文件到底配置了什么内容。然后解析出来。   
这里一般配置都是包含了我们所有spring bean   
（2）然后会把所有的bean的实例，以及bean与对象之间的关系放入到IOC容器当中。ioc容器其实就是一个map对象而已，准确的说是list<String,Map>一个
集合包含一个map。map中用键值对的形式去存储我们配置文件中的所有的bean；   
（3）然后spring 又提供一个扫面配置；   
（4） 就是把扫面的有个路径包下的所有类进行反射实例化，然后再把实例化的对放入如map当中存储。    
**DI**   
实例化之后进入DI注入阶段：   
DI就是把刚刚存储的对象，以及对象关系，有哪些需要赋值、有哪些需要注入的我们都在这个初始化的DI阶段完成。
所谓的DI注入，其实就是给我们这些对象的属性进行自动化的赋值。这就是DI注入。那么在DI注入赋值的时候有些值是需要我们注入的
有些是不需要我们注入的，还有必须要注入的，那么我们就可以通过注解配置，告诉spring容器，哪些是需要配置，哪些是不需要配置的。
完成这个工程之后我们就是进入了MVC阶段。   
**mvc**   
mvc中有个核心的对象就是handlerMapping对象；这个对象主要就是完成了我们整个的url和Method的一对一的对应关系。通过
这个HandlerMapping保存url和Method之间关系之后。我们在前端用url去请求servlet之后，servlet会去根据这个url去找到这个
对应关系方法，然后再根据反射机制，把这个方法进行invoke，然后再把这个返回值输出出来，通过浏览器在展示。
因此其实handlerMapping就是来存储这个对应关系的。    
#**3.spring 运行阶段**    
这个运行阶段就是在我们浏览器在发起一个请求开始，这个请求呢就是url.那么对应的呢就是我们后面的这个业务逻辑，这个业务逻辑
呢首先会通过handlerMapping找到这个对应的方法。那么这个方法在哪里找到的呢，应该是先进servlet，servlet里面会有一个doGet或者DoPost
方法，在这个方法里面再去调用这个HandlerMapping.去找到他，最后在去invoke.在invoke之后再通过response.writer()
方法，把结果写到浏览器返回，浏览器才能看到我们运行的结果。     
##**这个就是spring整个的运行的过程。**

#开始手写springMVC
 