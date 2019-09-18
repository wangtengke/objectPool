package com.wtk.objectPool.objectPool;

import com.wtk.objectPool.objectPool.Pool;

import java.util.concurrent.TimeUnit;

public interface BlockingPool<T> extends Pool<T> {
    T get();

    T get(long time, TimeUnit timeUnit) throws InterruptedException;
}
