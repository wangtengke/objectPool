package com.wtk.objectPool.objectPool;

import com.wtk.objectPool.Base;

public interface Validator<T> {
    /**
     *  whether object is valid
     * @param t
     * @return
     */
    public boolean isValid(T t);

    /**
     * clear object
     * @param t
     */
    public void invalidate(Base<T> t);
}
