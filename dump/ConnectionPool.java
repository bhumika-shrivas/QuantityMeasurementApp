package com.app.quantitymeasurement.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * ConnectionPool backed by HikariCP.
 * Provides application-wide DataSource and graceful shutdown hook.
 */
public final class ConnectionPool {

    private static final HikariDataSource dataSource;

    static {
        HikariDataSource ds = null;
        try {
            String url = ApplicationConfig.getProperty("db.url");
            String username = ApplicationConfig.getProperty("db.username");
            String password = ApplicationConfig.getProperty("db.password");
            String driver = ApplicationConfig.getProperty("db.driver");
            String poolSize = ApplicationConfig.getProperty("db.pool.size");

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            if (username != null) config.setUsername(username);
            if (password != null) config.setPassword(password);
            if (driver != null) config.setDriverClassName(driver);
            if (poolSize != null) {
                try { config.setMaximumPoolSize(Integer.parseInt(poolSize)); } catch (NumberFormatException ignored) {}
            }

            ds = new HikariDataSource(config);

            // Ensure schema exists by executing schema.sql using a fresh connection
            InputStream in = ConnectionPool.class.getClassLoader().getResourceAsStream("db/schema.sql");
            if (in != null) {
                String sql = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
                try (Connection c = ds.getConnection(); Statement stmt = c.createStatement()) {
                    for (String s : sql.split(";")) {
                        String trimmed = s.trim();
                        if (!trimmed.isEmpty()) stmt.execute(trimmed);
                    }
                }
            }

            // register shutdown hook to close the datasource
            HikariDataSource finalDs = ds;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (finalDs != null && !finalDs.isClosed()) finalDs.close();
                } catch (Exception ignored) {}
            }));

        } catch (Exception e) {
            if (ds != null) {
                try { ds.close(); } catch (Exception ignore) {}
            }
            throw new RuntimeException("Failed to initialize HikariCP datasource", e);
        }
        dataSource = ds;
    }

    private ConnectionPool() { /* utility class */ }

    public static Connection getConnection() throws java.sql.SQLException {
        return dataSource.getConnection();
    }

    /**
     * Release connection back to the pool (close returns it to HikariCP)
     */
    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            try { connection.close(); } catch (Exception ignored) {}
        }
    }

    public static int getAvailableConnections() {
        // Hikari does not expose exact available connections easily; return -1 as unknown
        return -1;
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}