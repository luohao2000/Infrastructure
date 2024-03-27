package com.itiaoling.dal.factory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.itiaoling.dal.domains.DataAccessKey;
import com.itiaoling.dal.factory.interfaces.Factory;
import com.itiaoling.dal.factory.interfaces.FactoryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 抽象工厂
 *
 * @author charles
 * @date 2023/10/26
 */
public abstract class AbstractFactory<T> implements Factory<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractFactory.class);

    /**
     * 内置缓存 map 防止重复创建
     */
    protected static final Cache<String, Object> CACHE = CacheBuilder.newBuilder()
            .initialCapacity(2 << 4)
            .maximumSize(2 << 8)
            .build();

    @Override
    @SuppressWarnings("unchecked")
    public T build(DataAccessKey key, FactoryStrategy<T> strategy) {
        try {
            return (T) CACHE.get(key.joinDot(), rebuild(key, strategy));
        } catch (Exception e) {
            LOG.error("Itiaoling Dal build error", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 新建并缓存
     *
     * @param key      键值
     * @param strategy 策略
     * @return T
     */
    protected Callable<T> rebuild(DataAccessKey key, FactoryStrategy<T> strategy) {
        if (!strategy.evaluate(this)) {
            LOG.info("Itiaoling Dal rebuild error for evaluate , not suitable strategy for factory");
            throw new RuntimeException("Itiaoling Dal rebuild error for evaluate , not suitable strategy for factory");
        }

        return () -> {
            T newInstance = strategy.init(key);
            CACHE.put(key.joinDot(), newInstance);
            return newInstance;
        };
    }
}

