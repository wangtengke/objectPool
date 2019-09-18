package com.wtk.objectPool.objectPool;

import java.util.concurrent.TimeUnit;

/**
 *
 * @param <T> the type of object
 */
public interface Pool<T> {
    /**
     * get object from pool
     * @return object
     */
    T get();

    T get(long time, TimeUnit timeUnit) throws InterruptedException;
    /**
     * return object to pool
     * @param t
     */
    void release(T t);

    /**
     * shutdown pool
     */
    void shutdown();
}
