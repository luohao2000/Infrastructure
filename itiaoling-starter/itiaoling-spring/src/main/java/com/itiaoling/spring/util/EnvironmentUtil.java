package com.itiaoling.spring.util;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * @author charles
 */
public class EnvironmentUtil implements EnvironmentAware {
    private static Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        EnvironmentUtil.environment = environment;
    }

    public static Environment getEnvironment() {
        return environment;
    }
}