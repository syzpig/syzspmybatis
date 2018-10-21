该模块是模拟spring IoC底层源码实现

这个spring ioc容器其实就是一个list加map  -->List<Map<String,Obj>>
那么spring容器中的对象是那来的呢？那是spring在一启动时去扫描指定包中的对象
---》（@ComponentScan("com") //这是扫描spring所需要的类）通过这个注解扫面指定包，把这个包中所有
加了@Component的类 都会被加载扫描到。 比如service包中加了@Service这个注解效果也是一样的。
总而言之就是：只要扫描到的包中的类加了@Component注解的类，spring都会去加载这个类去new一个出来放入
到spring容器中。我们本项目模块就模拟这个过程。
spring用来扫描包的源码方式是doScan，大家可以先bedug查看源码。
思路：
由spring 扫描源码可得，要想把对象实例化，首先要拿到这个对象。
而一般实例化对象方式有四种，克隆、反序列化、new、反射；这里只有反射可以用。因为序列化首先要把对象反序列化
，new首先要知道是哪个具体对象。克隆一样。而反射则可以根据扫面路径，来获取具体的文件物理路径，从而获取对象名以及包名
进而就可以通过反射获取对象的实例。
例如：我们首先可以通过包名获取到文件
D:\syzSpringCloudProject\spring-source-code\syzspmybatis\syzspring\target\classes\com\syz\service\BaseUserService.class

然后就可以得到文件名BaseUserService.class，
接下来就可以去掉.class，得到文件名
最后扫描通路径中的包路径名（@ComponentScan("com") ）
拿到com然后把上面的路径往后截取。并把斜杠替换成点
最终得到：com.syz.service.BaseUserService
最后通过反射
Class.form("com.syz.service.BaseUserService").newInstance();获取实例
然后把它放入map中
这就是springIOC大致的原理

二、spring ioc的自动装配，原理跟实例化对象一样，也是找找注解然后进行注入
但是找到了要注入的类之后，怎么注入，注入什么？ 注入对象
就是给带有注解的属性的类型对应的对象，注入进来，那么这个对象在哪里呢？
也就是在ioc容器中
但是，存在一个问题，就是在当循环到注入的对象之后，ioc容器还没被执行，或者ioc中不存在要注入的对象，则呢办？
spring源码是使用递归实现的，但这里我们先用个简单的方式，
就是先把带注解的类进行初始化，在找带Autowired注解类进行注入(setter)，后面需要就直接注入，
当然还有其他解决方式，目前先用最戳的，后面在一点点往spring源码优化。 现在进入ScanUtils类进行修改
