package com.wtk.objectPool.poolFactory;

import com.wtk.objectPool.objectFactory.ObjectFactory;

/**
 * @program: objectPool
 * @description:
 * @author: wangtengke
 * @create: 2019-09-17
 **/
public class StringBufferFactory implements ObjectFactory<StringBuffer> {
    @Override
    public StringBuffer createNew() {
        return new StringBuffer();
    }
}
