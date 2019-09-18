package com.wtk.objectPool.poolFactory;

import com.wtk.objectPool.blockingPool.BoundedBlockingPool;
import com.wtk.objectPool.objectPool.Pool;
import org.junit.Test;

import java.sql.Connection;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class PoolFactoryTest {
    public static void main(String[] args) {
        Pool<StringBuffer> pool = PoolFactory.newBoundedBlockingPool(5, 10, 1000, TimeUnit.MILLISECONDS, new StringBufferValidator(), new StringBufferFactory());
        try {
                StringBuffer stringBuffer1 = pool.get(1000, TimeUnit.MILLISECONDS);
                StringBuffer stringBuffer2 = pool.get(1000, TimeUnit.MILLISECONDS);
                StringBuffer stringBuffer3 = pool.get(1000, TimeUnit.MILLISECONDS);
                StringBuffer stringBuffer4 = pool.get(1000, TimeUnit.MILLISECONDS);
                StringBuffer stringBuffer5 = pool.get(1000, TimeUnit.MILLISECONDS);
                StringBuffer stringBuffer6 = pool.get(1000, TimeUnit.MILLISECONDS);
                stringBuffer6.append("hello");
                pool.release(stringBuffer4);
                pool.release(stringBuffer5);
                pool.release(stringBuffer6);
            long cur = System.currentTimeMillis();
            while(true) {
//                    long cur = System.currentTimeMillis();
                if (System.currentTimeMillis() - cur > 10) {
                    break;
                }
            }
            StringBuffer stringBuffer7 = pool.get(1000, TimeUnit.MILLISECONDS);
            System.out.println("stringBuffer7"+stringBuffer7.toString());
            StringBuffer stringBuffer8 = pool.get(1000, TimeUnit.MILLISECONDS);
            System.out.println("stringBuffer8"+stringBuffer8.toString());
            StringBuffer stringBuffer9 = pool.get(1000, TimeUnit.MILLISECONDS);
            System.out.println("stringBuffer9"+stringBuffer9.toString());
            StringBuffer stringBuffer10 = pool.get(1000, TimeUnit.MILLISECONDS);
            System.out.println("stringBuffer10"+stringBuffer10.toString());
            StringBuffer stringBuffer11 = pool.get(1000, TimeUnit.MILLISECONDS);
            System.out.println("stringBuffer11"+stringBuffer11.toString());

            System.out.println("----------------finish!");
            cur = System.currentTimeMillis();
            while(true) {
//                    long cur = System.currentTimeMillis();
                if (System.currentTimeMillis() - cur > 2000) {
                    break;
                }
            }
            System.out.println(BoundedBlockingPool.count);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void newBoundedBlockingPool() {
////        Pool<Connection> pool = PoolFactory.newBoundedBlockingPool(10, 20, 1000, TimeUnit.MILLISECONDS, new JDBCConnectionValidator(),new JDBCConnectionFactory("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8","root",""));
//        Pool<StringBuffer> pool = PoolFactory.newBoundedBlockingPool(10, 20, 1000, TimeUnit.MILLISECONDS, new StringBufferValidator(), new StringBufferFactory());
//        try {
//            StringBuffer stringBuffer = pool.get(1000, TimeUnit.MILLISECONDS);
//            stringBuffer.append("hello!");
//            System.out.println(stringBuffer.toString());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}