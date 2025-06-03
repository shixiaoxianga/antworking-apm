package com.antworking.core.tools;

import com.antworking.common.ConstantAw;
import com.antworking.core.classload.AntWorkingClassLoad;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;

import java.sql.*;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class JdbcHelper {
    private static final AwLog log = LoggerFactory.getLogger(JdbcHelper.class);
    private static final long CONNECTION_VALIDATION_TIMEOUT = 1; // 连接验证超时(秒)

    private String DB_URL = "jdbc:mysql://localhost:3306/mydatabase";
    private String USER = "root";
    private String PASS = "password";

    private static final Lock lock = new ReentrantLock();
    private static volatile Connection conn; // 使用volatile保证可见性

    public JdbcHelper() {}

    public JdbcHelper(String DB_URL, String USER, String PASS) {
        this.DB_URL = DB_URL;
        this.USER = USER;
        this.PASS = PASS;
        initConnectionKeeper();
    }

    /**
     * 初始化连接保活定时任务
     */
    private void initConnectionKeeper() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        // 调整为每30秒执行一次保活（原60分钟太长）
        executor.scheduleAtFixedRate(this::keepAlive, 30, 30, TimeUnit.SECONDS);
    }

    /**
     * 连接保活操作
     */
    private void keepAlive() {
        try {
            Connection currentConn = getConnection();
            if (currentConn != null) {
                try (Statement stmt = currentConn.createStatement()) {
                    stmt.executeQuery("SELECT 1");
                    log.debug("Connection keep-alive successful");
                }
            }
        } catch (Exception e) {
            log.error("Connection keep-alive failed", e);
            // 保活失败时重置连接
            resetConnection();
        }
    }

    /**
     * 获取数据库连接（带有效性检查）
     */
    public Connection getConnection() throws SQLException {
        if (isConnectionValid()) {
            return conn;
        }

        lock.lock();
        try {
            // 双重检查锁定
            if (!isConnectionValid()) {
                closeConnectionSilently();
                conn = createNewConnection();
                log.info("Created new database connection");
            }
            return conn;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 检查连接是否有效
     */
    private boolean isConnectionValid() {
        if (conn == null) return false;

        try {
            return !conn.isClosed() && conn.isValid((int) CONNECTION_VALIDATION_TIMEOUT);
        } catch (SQLException e) {
            log.error("Connection validation failed", e);
            return false;
        }
    }

    /**
     * 创建新连接
     */
    private Connection createNewConnection() throws SQLException {
        Properties properties = new Properties();
        properties.put("user", USER);
        properties.put("password", PASS);
        properties.put("autoReconnect", "true"); // 启用自动重连
        properties.put("maxReconnects", "3");    // 最大重试次数

        try {
            Driver driver = (Driver) Class.forName(
                    ConstantAw.PACKAGE_NAME_SPACE + ".com.mysql.cj.jdbc.Driver",
                    true,
                    AntWorkingClassLoad.INSTANCE).newInstance();
            return driver.connect(DB_URL, properties);
        } catch (Exception e) {
            throw new SQLException("Driver initialization failed", e);
        }
    }

    /**
     * 静默关闭连接
     */
    private void closeConnectionSilently() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                log.debug("Error closing old connection", ex);
            } finally {
                conn = null;
            }
        }
    }

    /**
     * 重置连接
     */
    private void resetConnection() {
        lock.lock();
        try {
            closeConnectionSilently();
            conn = createNewConnection();
            log.info("Recreated database connection after failure");
        } catch (SQLException e) {
            log.error("Connection reset failed", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 执行SQL查询
     */
    public ResultSet executeQuery(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            log.error("APM query error: {}", sql, e);
            // 查询失败时尝试重置连接
            if (e.getMessage().contains("inactivity")) {
                resetConnection();
            }
        } finally {
            // 只关闭语句和结果集，不关闭连接
            closeResources(null, stmt, rs);
        }
        return null;
    }

    /**
     * 执行SQL更新
     */
    public int executeUpdate(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("APM update error: {}", sql, e);
            // 更新失败时尝试重置连接
            if (e.getMessage().contains("inactivity")) {
                resetConnection();
            }
            return 0;
        } finally {
            closeResources(null, stmt, null);
        }
    }

    /**
     * 关闭资源（不关闭连接）
     */
    public void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        closeResultSet(rs);
        closeStatement(stmt);
        // 注意：不关闭传入的连接
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.debug("Error closing result set", e);
            }
        }
    }

    private void closeStatement(PreparedStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.debug("Error closing statement", e);
            }
        }
    }
}