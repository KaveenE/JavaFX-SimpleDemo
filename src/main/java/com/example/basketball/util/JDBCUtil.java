// src/main/java/com/example/basketball/util/JDBCUtil.java
package com.example.basketball.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtil {

    private static final Properties props = new Properties();

    static {
        try (InputStream is = JDBCUtil.class.getResourceAsStream("/config/db.properties")) {
            if (is == null) throw new IOException("db.properties not found");
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load DB config", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url  = props.getProperty("jdbc.url");
        String user = props.getProperty("jdbc.user");
        String pass = props.getProperty("jdbc.password");
        return DriverManager.getConnection(url, user, pass);
    }
}