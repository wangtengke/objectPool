package com.wtk.objectPool.poolFactory;

import com.wtk.objectPool.Base;
import com.wtk.objectPool.objectPool.Validator;

/**
 * @program: objectPool
 * @description:
 * @author: wangtengke
 * @create: 2019-09-17
 **/
public class StringBufferValidator implements Validator<StringBuffer> {
    @Override
    public boolean isValid(StringBuffer stringBuffer) {
        if(stringBuffer==null) return false;
        return true;
    }

    @Override
    public void invalidate(Base<StringBuffer> t) {
        t = null;
    }
}
