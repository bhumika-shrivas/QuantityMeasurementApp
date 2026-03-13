package com.app.quantitymeasurement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Collectors;

public class ConnectionPool {

    private static final Queue<Connection> pool = new ArrayDeque<>();
    private static int maxPoolSize;

    static {
        try {
            String url = ApplicationConfig.getProperty("db.url");
            String username = ApplicationConfig.getProperty("db.username");
            String password = ApplicationConfig.getProperty("db.password");
            String driver = ApplicationConfig.getProperty("db.driver");

            maxPoolSize = Integer.parseInt(
                    ApplicationConfig.getProperty("db.pool.size"));

            Class.forName(driver);

            for (int i = 0; i < maxPoolSize; i++) {
                Connection connection =
                        DriverManager.getConnection(url, username, password);
                pool.add(connection);
            }

            // Run schema.sql to ensure tables exist (execute with a fresh connection)
            try (Connection c = DriverManager.getConnection(url, username, password);
                 Statement stmt = c.createStatement()) {
                InputStream in = ConnectionPool.class.getClassLoader().getResourceAsStream("db/schema.sql");
                if (in != null) {
                    String sql = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
                    // Split by semicolon to handle multiple statements if present
                    for (String s : sql.split(";")) {
                        String trimmed = s.trim();
                        if (!trimmed.isEmpty()) {
                            stmt.execute(trimmed);
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize connection pool", e);
        }
    }

    public static synchronized Connection getConnection() {
        if (pool.isEmpty()) {
            throw new RuntimeException("No available database connections");
        }
        return pool.poll();
    }

    public static synchronized void releaseConnection(Connection connection) {
        if (connection != null) {
            pool.offer(connection);
        }
    }

    public static int getAvailableConnections() {
        return pool.size();
    }
}