package cn.tripman.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.util.List;

public class QueryUtil {

    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private static final ThreadLocal<Connection> THREAD_LOCAL = new ThreadLocal<>();

    public static void getConnection(DbProperties dbProperties) throws Exception {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass(dbProperties.getDriverEnum().getValue());
        ds.setJdbcUrl(dbProperties.getUrl());
        ds.setUser(dbProperties.getUserName());
        ds.setPassword(dbProperties.getPassword());
        THREAD_LOCAL.set(ds.getConnection());
    }

    public static <T> List<T> query(String sql, Class<T> tClass) throws Exception {
        if (THREAD_LOCAL.get() == null) {
            throw new Exception(" conn error ");
        }
        BeanListHandler<T> beanListHandler = new BeanListHandler(tClass);
        return QUERY_RUNNER.query(THREAD_LOCAL.get(), sql, beanListHandler);
    }
}
