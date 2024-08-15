package com.antworking.core.tools;

import com.antworking.common.ConstantAw;
import com.antworking.core.classload.AntWorkingClassLoad;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;

import java.sql.*;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class JdbcHelper {
    private static final AwLog log = LoggerFactory.getLogger(JdbcHelper.class);

    private   String DB_URL = "jdbc:mysql://localhost:3306/mydatabase";
    private   String USER = "root";
    private   String PASS = "password";

    private static final Lock lock = new ReentrantLock();
    public static  Connection conn ;
    public JdbcHelper(){

    }
    public JdbcHelper(String DB_URL, String USER, String PASS) {
        this.DB_URL = DB_URL;
        this.USER = USER;
        this.PASS = PASS;
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取数据库连接
     *
     * @return 数据库连接
     * @throws SQLException 如果连接失败
     */
    public  Connection getConnection() throws SQLException {
        lock.lock();
        try {
            if(JdbcHelper.conn == null){
                Properties properties = new Properties();
                properties.put("user", USER);
                properties.put("password", PASS);
                Driver driver = (Driver) Class.forName(ConstantAw.PACKAGE_NAME_SPACE + ".com.mysql.cj.jdbc.Driver",
                        true,
                        AntWorkingClassLoad.INSTANCE).newInstance();
                Connection connection = driver.connect(
                        DB_URL,
                        properties);
                JdbcHelper.conn = connection;
                return connection;
            }
            return JdbcHelper.conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }


    }

    /**
     * 执行 SQL 查询
     *
     * @param sql SQL 查询语句
     * @param params 参数列表
     * @return 结果集
     * @throws SQLException 如果执行失败
     */
    public ResultSet executeQuery(String sql, Object... params)  {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            // 设置参数
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            rs = stmt.executeQuery();

            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行 SQL 更新
     *
     * @param sql SQL 更新语句
     * @param params 参数列表
     * @return 影响的行数
     * @throws SQLException 如果执行失败
     */
    public int executeUpdate(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            // 设置参数
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 关闭资源
     *
     * @param conn 数据库连接
     * @param stmt Statement 对象
     * @param rs ResultSet 对象
     */
    public void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}