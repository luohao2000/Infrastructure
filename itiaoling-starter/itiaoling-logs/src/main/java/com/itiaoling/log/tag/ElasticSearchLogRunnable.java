package com.itiaoling.log.tag;

import java.util.Map;


public abstract class ElasticSearchLogRunnable implements Runnable {

    private Map<String, String> defaultIndexTags;
    private Map<String, String> customIndexTags;
    private Map<String, String> storedTags;

    public ElasticSearchLogRunnable() {
        this.defaultIndexTags = ElasticSearchTagHolder.defaultIndexTags.getTags();
        this.customIndexTags = ElasticSearchTagHolder.customIndexTags.getTags();
//        this.storedTags = ElasticSearchTagHolder.storedTags.getTags();
    }

    @Override
    public void run() {
        try {
            if (defaultIndexTags != null) {
                ElasticSearchTagHolder.defaultIndexTags.putTags(this.defaultIndexTags);
            }
            if (customIndexTags != null) {
                ElasticSearchTagHolder.customIndexTags.putTags(this.customIndexTags);
            }

//            doRun();
        } finally {
            ElasticSearchTagHolder.defaultIndexTags.clearTags();
            ElasticSearchTagHolder.customIndexTags.clearTags();

        }
    }

    public abstract void doRun() throws Exception;
}
