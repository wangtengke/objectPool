package com.wtk.objectPool.blockingPool;

import com.wtk.objectPool.Base;
import com.wtk.objectPool.objectFactory.ObjectFactory;
import com.wtk.objectPool.objectPool.AbstractPool;
import com.wtk.objectPool.objectPool.BlockingPool;
import com.wtk.objectPool.objectPool.Validator;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: objectPool
 * @description:
 * @author: wangtengke
 * @create: 2019-09-17
 **/
public final class BoundedBlockingPool<T> extends AbstractPool<T> implements BlockingPool<T>{
    public final static AtomicInteger count = new AtomicInteger(0);
    private int coreSize;
    private int maxSize;
    private long keepAliveTime;
    private TimeUnit unit;
    public BlockingDeque<Base<T>> objects;
    private Validator<T> validator;
    private ObjectFactory<T> objectFactory;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private ExecutorService checkTimeout = Executors.newSingleThreadExecutor();
    private volatile boolean shutdownCalled;
    public BoundedBlockingPool(int coreSize, int maxSize, long keepAliveTime, TimeUnit unit, Validator<T> validator, ObjectFactory<T> objectFactory){
        if (coreSize < 0 ||
                maxSize <= 0 ||
                maxSize < coreSize ||
                keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (validator == null || objectFactory == null)
            throw new NullPointerException();

        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.keepAliveTime = unit.toMillis(keepAliveTime);
        this.validator = validator;
        this.objectFactory = objectFactory;
        objects = new LinkedBlockingDeque<Base<T>>();
        initObjects();
        this.shutdownCalled = false;
    }

    private void initObjects() {
        for (int i = 0; i < coreSize; i++) {
            objects.add(new Base(System.currentTimeMillis(),objectFactory.createNew()));
            count.incrementAndGet();
        }
        checkTimeout.execute(new Runnable() {
            @Override
            public void run() {
                long cur = System.currentTimeMillis();
                while(true) {
//                    long cur = System.currentTimeMillis();
                    if(System.currentTimeMillis()-cur<1000) {
                        continue;
                    }
                    cur = System.currentTimeMillis();
                    while(!objects.isEmpty() && System.currentTimeMillis() - objects.getLast().getTime()>keepAliveTime) {
                        objects.pollLast();
                        int c = count.decrementAndGet();
                        System.out.println(objects.size());
                        System.out.println("can use count: " + c);
                    }
                    if(count.get()<coreSize) {
                        for (int i = 0; i < coreSize-count.get(); i++) {
                            objects.add(new Base(System.currentTimeMillis(),objectFactory.createNew()));
                        }
                    }
                    System.out.println("object's size: "+objects.size());

                }
            }
        });
    }

    protected void handleInvalidReturn(T t) {

    }

    protected void returnPool(T t) {
        if(validator.isValid(t)) {
            executor.submit(new ObjectReturner(objects, t));
        }
    }

    protected boolean isValid(T t) {
        return validator.isValid(t);
    }

    public synchronized T get(long time, TimeUnit timeUnit) throws InterruptedException {
        if(!shutdownCalled) {
            T t = null;
            try {
                if(count.get()<coreSize) {
                    System.out.println("cur object size: " + count.get() + "  coreSize:" + coreSize);

                    for (int i = 0; i < coreSize - count.get(); i++) {
                        objects.add(new Base(System.currentTimeMillis(),objectFactory.createNew()));
                    }

                }
                if(objects.size()==0) {
                    if(count.get()<maxSize) {
                        objects.add(new Base(System.currentTimeMillis(),objectFactory.createNew()));
                        count.incrementAndGet();
                        System.out.println("cur object size: " + count.get() + "  maxSize" + maxSize);
                    }
                    else {
                        System.out.println("cur size greater than maxSize!!!");

                    }
                }
                Base base = objects.poll(time, timeUnit);
                return (T) base.getT();
            }catch (Exception e) {
                Thread.currentThread().interrupt();
                System.out.println("poll timeout");
            }
            return t;
        }
        throw new IllegalStateException("Object pool is already shutdown");

    }

    public T get() {
        if(!shutdownCalled) {
            T t = null;
            try {
                if(count.get()<coreSize) {
                    synchronized (this) {
                        for (int i = 0; i < coreSize - count.get(); i++) {
                            objects.add(new Base(System.currentTimeMillis(),objectFactory.createNew()));
                        }
                    }
                }
                Base base = objects.take();
                return (T)base.getT();
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("poll timeout");
            }
            return t;
        }
        throw new IllegalStateException("Object pool is already shutdown");
    }

    public void shutdown() {
        shutdownCalled = true;
        executor.shutdownNow();
        clearResources();
    }

    private void clearResources() {
        for(Base t: objects) {
            validator.invalidate(t);
        }
    }

    private class ObjectReturner<E> implements Callable {
        private BlockingDeque blockingQueue;
        private E e;
        public ObjectReturner(BlockingDeque blockingQueue, E e) {
            this.blockingQueue = blockingQueue;
            this.e = e;
        }

        public Void call() throws Exception {
            while(true) {
                try {
                    blockingQueue.put(new Base<>(System.currentTimeMillis(),e));
                    break;
                }catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return null;
        }
    }
}
