package com.wtk.objectPool.objectFactory;

/**
 * create new object
 */
public interface ObjectFactory<T> {

    /**
     * crete new object
     * @return T
     */
    public abstract T createNew();
}
