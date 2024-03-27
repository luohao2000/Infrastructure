package com.itiaoling.log;

import com.google.common.collect.Maps;
import com.itiaoling.json.JsonUtil;
import com.itiaoling.log.spi.LogProvider;
import com.itiaoling.log.spi.LogProviderManager;


import java.util.HashMap;
import java.util.Map;

public class LogProviderManagerTest {




    public void testJsonUtilToJson(){
        LogProvider logger =  LogProviderManager.get("10.238.239.200:9092");

        Map<String,String> maps = new HashMap<>();
        maps.put("app","idm-account");
        maps.put("uri","/ping");


        for (int i = 0; i < 100000; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            logger.log("mp-idm","test  " + i,maps,maps );
        }


    }
}
