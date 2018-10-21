package com.syz.spring.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan("com")
//@ImportResource("classpath:spring.xml") //这种方式是注解加xml方式配置spring
public class Config {
}
