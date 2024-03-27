package com.itiaoling.log.utils;

/**
 * 运行时工具类
 *
 * @author charles
 * @since 2023/12/20
 */
public class RuntimeUtil {
    /**
     * 是否可以加载
     */
    private static boolean loaded = false;

    /**
     * 应用名称
     */
    private static String appName;

    /**
     * 运行时框架
     */
    private static Framework framework;

    public static Framework getFramework() {
        return framework;
    }

    public static void setFramework(Framework framework) {
        RuntimeUtil.framework = framework;
    }

    public static String getAppName() {
        return appName;
    }

    public static void setAppName(String appName) {
        RuntimeUtil.appName = appName;
    }

    public static boolean springRuntime() {
        return framework == Framework.SPRING && loaded;
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static void setLoaded(boolean loaded) {
        RuntimeUtil.loaded = loaded;
    }

    public enum Framework {
        /**
         * spring framework
         */
        SPRING,
        /**
         * 未知
         */
        ANONYMOUS,
        ;
    }
}
