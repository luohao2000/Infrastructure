package com.itiaoling.log.tag;


import com.google.common.collect.ImmutableMap;
import org.springframework.core.NamedThreadLocal;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class ElasticSearchTagHolder {

    private final ThreadLocal<Map<String, String>> tags;

    private ElasticSearchTagHolder(int initialCapacity, String name) {
        //this.tags = new NamedInheritableThreadLocal<Map<String, String>>(name) {
        this.tags = new NamedThreadLocal<Map<String, String>>(name) {
            @Override
            protected Map<String, String> initialValue() {
                // InheritableThreadLocal 的时候会有并发问题
                return new ConcurrentHashMap<>(initialCapacity);
            }
        };
    }

    /**
     * 可被日志索引搜索
     * 不会被日志所清理存在于整个线程的生命周期
     * 不可变
     */
    public final static ElasticSearchTagHolder defaultIndexTags;

    /**
     * 可被日志索引搜索
     * 会被日志所清理存在于下一次log之前
     */
    public final static ElasticSearchTagHolder customIndexTags = new ElasticSearchTagHolder(16, "customIndexTags");

    static {
        defaultIndexTags = new ElasticSearchTagHolder(8, "defaultIndexTags") {
            @Override
            public Map<String, String> getTags() {
                Map<String, String> tagsMap = super.tags.get();
                // ENV
                // IDC
                // IP
                // APP Name

                return ImmutableMap.copyOf(tagsMap);
            }
        };
    }




    public Map<String, String> getTags() {
        return Collections.unmodifiableMap(tags.get());
    }

    public String getTag(String key) {
        return tags.get().get(key);
    }

    public void putTag(String key, String value) {
        if (key == null || key.isEmpty()) {
            return;
        }
        this.tags.get().put(key, emptyIfNull(value));
    }

    public void putTags(Map<String, String> tags) {
        if (tags != null && !tags.isEmpty()) {
            this.tags.get().putAll(tags);
        }
    }

    public void removeTag(String key) {
        this.tags.get().remove(key);
    }

    public void clearTags() {
        // this.tags.get().clear();
        this.tags.remove();
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static String emptyIfNull(String original) {
        return original == null ? "" : original;
    }

}
