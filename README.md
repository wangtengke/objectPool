# 项目需求
![damend]()

# 概要设计
1.对象池策略
![objectpool]()
2.对象空闲时间超时清除策略
![reject]()
# 接口设计
- **实现ObjectFactory接口**
```java
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
```
根据自己的需要的对象去实现新建对象方法

例子：
```java
public class JDBCConnectionFactory implements ObjectFactory<Connection> {
    private String connectionURL;
    private String userName;
    private String password;

    public JDBCConnectionFactory(String driver, String connectionURL, String userName, String password) {
        super();
        try {
            Class.forName(driver);
        }catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to find driver in classpath ", e);
        }
        this.connectionURL = connectionURL;
        this.userName = userName;
        this.password = password;

    }
    @Override
    public Connection createNew() {
        try {
            return DriverManager.getConnection(connectionURL, userName, password);
        }catch (SQLException e) {
            throw new IllegalArgumentException("Uable to create new connection", e);
        }
    }
}
```


-**实现Validator接口** 
```java
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
```

实现判断对象是否合法，以及将对象清空

例子:
```java
public final class JDBCConnectionValidator implements Validator<Connection> {
    @Override
    public boolean isValid(Connection connection) {
        if(connection==null) return false;
        try {
            return !connection.isClosed();
        }catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void invalidate(Base<Connection> t) {
        try {
            t.getT().close();
        }catch (SQLException e) {

        }
    }

}
```

- 新建一个Pool<T> pool,得到一个对象池
```java
public final class PoolFactory<T> {
    private PoolFactory(){}

    /**
     * 
     * @param coreSize 
     * @param maxSize
     * @param keepAliveTime
     * @param unit
     * @param validator
     * @param objectFactory
     * @param <T>
     * @return
     */
    public static <T> Pool<T> newBoundedBlockingPool(int coreSize, int maxSize, long keepAliveTime, TimeUnit unit, Validator<T> validator, ObjectFactory<T> objectFactory){
        return new BoundedBlockingPool<T>(coreSize, maxSize, keepAliveTime, unit, validator, objectFactory);
    }

}
```
# 使用说明
 1.在pom中引入包
 ```java
    <groupId>com.wtk</groupId>
    <artifactId>objectPool</artifactId>
    <version>1.0-SNAPSHOT</version>
 ```
 
 2.实现ObjectFactory和Validator两个接口
 ```java
    public class StringBufferFactory implements ObjectFactory<StringBuffer> {
    @Override
    public StringBuffer createNew() {
        return new StringBuffer();
    }
}
 ```
 ```java
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
 ```
 3.创建一个对象池并测试
 
 例子: 创建一个StrinigBuffer对象池
 ```java
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
 ```