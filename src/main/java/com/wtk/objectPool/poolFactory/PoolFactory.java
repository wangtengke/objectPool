package com.wtk.objectPool.poolFactory;

import com.wtk.objectPool.Base;
import com.wtk.objectPool.blockingPool.BoundedBlockingPool;
import com.wtk.objectPool.objectFactory.ObjectFactory;
import com.wtk.objectPool.objectPool.Pool;
import com.wtk.objectPool.objectPool.Validator;

import java.util.concurrent.TimeUnit;

/**
 * @program: objectPool
 * @description:
 * @author: wangtengke
 * @create: 2019-09-17
 **/
public final class PoolFactory<T> {
    private PoolFactory(){}

    /**
     *
     * @param coreSize
     * @param maxSize
     * @param keepAliveTime
     * @param unit
     * @param validator
     * @param objectFactory
     * @param <T>
     * @return
     */
    public static <T> Pool<T> newBoundedBlockingPool(int coreSize, int maxSize, long keepAliveTime, TimeUnit unit, Validator<T> validator, ObjectFactory<T> objectFactory){
        return new BoundedBlockingPool<T>(coreSize, maxSize, keepAliveTime, unit, validator, objectFactory);
    }

}
