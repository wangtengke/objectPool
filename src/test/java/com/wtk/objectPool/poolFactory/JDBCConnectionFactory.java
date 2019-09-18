package com.wtk.objectPool.poolFactory;

import com.wtk.objectPool.Base;
import com.wtk.objectPool.objectFactory.ObjectFactory;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @program: objectPool
 * @description:
 * @author: wangtengke
 * @create: 2019-09-17
 **/
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
