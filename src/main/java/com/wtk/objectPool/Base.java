package com.wtk.objectPool;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: objectPool
 * @description:
 * @author: wangtengke
 * @create: 2019-09-17
 **/
@Data
@AllArgsConstructor
public class Base<T> {
    public long time;
    public T t;
}
