package com.netinfo.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringApplicationContext {
    
 private static final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:config/bean.xml");
    

    public static Object getBean(String name) {
        return applicationContext.getBean(name);

    }
    
    public static <T> T getBean(Class<T> clasz) {
        return applicationContext.getBean(clasz);

    }
    
    public static <T> T getBean(String name, Class<T> clasz){
        return applicationContext.getBean(name, clasz);
    }
}
