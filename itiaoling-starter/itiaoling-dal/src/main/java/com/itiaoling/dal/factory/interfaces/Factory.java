package com.itiaoling.dal.factory.interfaces;

import com.itiaoling.dal.domains.DataAccessKey;

/**
 * 工厂接口
 *
 * @author charles
 * @date 2023/10/25
 */
public interface Factory<T> {

    /**
     * 创建
     *
     * @param key      键值
     * @param strategy 策略
     * @see com.itiaoling.dal.factory.DalFactoryStrategy
     * @return T
     */
    T build(DataAccessKey key, FactoryStrategy<T> strategy);

}
