package com.syz.config;

import jdk.nashorn.internal.objects.annotations.Constructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * 这个类主要是来做配置用的    代码实现xml配置spring与mybatis整合
 */
@Configuration //因此添加此注解
@MapperScan("com.syz.dao")//搜索找到bean，以至于可以bean注入 .这是扫描mybatis所需要的类
@ComponentScan("com") //这是扫描spring所需要的类
public class AppConfig {
    //http://www.mybatis.org/spring/zh/index.html由spring整合mybatis官网可得
    // 第一步需要在配置文件xml中配置sqlsessionfactory,这里主张在代码中实现
    @Bean
    @Autowired   //这个dataSouce我们不会传进来，是让spring注入传件来
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    //在官网可得值在配置上面的bean之前需要一个数据源
    // 因此先获取配置SqlSessionFactory 的datasource数据源
    @Bean   //是这个DataSource放入spring容器，可以是上面自动注入进去
    public DataSource dataSource() {
        //利用spring自带的数据源
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        //配置数据库用户密码等的
        driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        driverManagerDataSource.setUsername("root");
        driverManagerDataSource.setPassword("root");
        driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/admin_study");
        return driverManagerDataSource;
    }
}
