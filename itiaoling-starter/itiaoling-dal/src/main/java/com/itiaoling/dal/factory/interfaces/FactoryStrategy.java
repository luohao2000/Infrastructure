package com.itiaoling.dal.factory.interfaces;

import com.itiaoling.dal.domains.DataAccessKey;

/**
 * 工厂策略
 *
 * @author charles
 * @date 2023/10/26
 */
public interface FactoryStrategy<T> {
    /**
     * 评估
     *
     * @param factory T
     * @return boolean
     */
    boolean evaluate(Factory<T> factory);

    /**
     * 初始化
     *
     * @param key 键值
     * @return T
     */
    T init(DataAccessKey key);
}
