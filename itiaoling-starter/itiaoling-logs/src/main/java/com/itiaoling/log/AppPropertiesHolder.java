package com.itiaoling.log;



import java.io.InputStream;
import java.util.Properties;


public class AppPropertiesHolder {

    private static Properties prop;

    //即时加载
    static {
        init();
    }

    public static String getAppName() {
        if (prop == null) {
            init();
        }
        return prop.getProperty("app.name", "Default");
    }

    public static void init() {
        if (prop == null) {
            synchronized (AppPropertiesHolder.class) {
                if (prop == null) {
                    try {
                        //读取属性文件app.properties
                        prop = new Properties();
                        InputStream in = AppPropertiesHolder.class.getResourceAsStream("/META-INF/app.properties");
                        // 加载属性列表
                        prop.load(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
