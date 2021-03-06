package cn.tripman.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.util.List;

public class QueryUtil {

    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private static final ThreadLocal<ComboPooledDataSource> THREAD_LOCAL = new ThreadLocal<>();

    public static void getConnection(DbProperties dbProperties) throws Exception {
        if (THREAD_LOCAL.get() != null) {
            return;
        }
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass(dbProperties.getDriverEnum().getValue());
        ds.setJdbcUrl(dbProperties.getUrl());
        ds.setUser(dbProperties.getUserName());
        ds.setPassword(dbProperties.getPassword());
        ds.setInitialPoolSize(5);
        THREAD_LOCAL.set(ds);
    }

    public static <T> List<T> query(String sql, Class<T> tClass) throws Exception {
        if (THREAD_LOCAL.get() == null) {
            throw new Exception(" conn error ");
        }
        BeanListHandler<T> beanListHandler = new BeanListHandler(tClass);
        Connection connection = THREAD_LOCAL.get().getConnection();
        List<T> result = QUERY_RUNNER.query(connection, sql, beanListHandler);
        connection.close();
        return result;
    }

    public static int execute(String sql, Object... params) throws Exception {
        Connection connection = THREAD_LOCAL.get().getConnection();
        int result = QUERY_RUNNER.execute(connection, sql, params);
        connection.close();
        return result;
    }
}
