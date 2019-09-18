package com.wtk.objectPool.poolFactory;

import com.wtk.objectPool.Base;
import com.wtk.objectPool.objectPool.Validator;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @program: objectPool
 * @description:
 * @author: wangtengke
 * @create: 2019-09-17
 **/
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
