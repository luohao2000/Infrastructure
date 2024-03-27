package com.itiaoling.starter.autoconfig;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CommonAutoConfigureTest {


    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ItiaolingSpringBootAutoConfigureEntry.class);

        String[] bdn = context.getBeanDefinitionNames();
        System.out.println(bdn);
    }
}
