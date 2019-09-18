package com.wtk.objectPool.objectPool;

/**
 * @program: objectPool
 * @description:
 * @author: wangtengke
 * @create: 2019-09-17
 **/
public abstract class AbstractPool<T> implements Pool<T>{

    public final void release(T t) {
        if(isValid(t)) {
            returnPool(t);
        }
        else {
            handleInvalidReturn(t);
        }
    }

    protected abstract void handleInvalidReturn(T t);

    protected abstract void returnPool(T t);

    protected abstract boolean isValid(T t);
}
