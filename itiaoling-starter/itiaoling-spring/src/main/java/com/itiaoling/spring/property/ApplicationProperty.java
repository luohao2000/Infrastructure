package com.itiaoling.spring.property;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.beans.ConstructorProperties;


/**
 * @author gary
 */
public class ApplicationProperty {

    @Value("${spring.application.name:}")
    private String applicationName;



    public String getApplicationName(){
        return applicationName;
    }

}
