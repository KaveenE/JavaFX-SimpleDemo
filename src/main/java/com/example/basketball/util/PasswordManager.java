// src/main/java/com/example/basketball/util/PasswordManager.java
package com.example.basketball.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PasswordManager {
    private static final String CONFIG_FILE = "/config/application.properties";
    private static final Properties properties = new Properties();

    static {
        try (InputStream is = PasswordManager.class.getResourceAsStream(CONFIG_FILE)) {
            if (is == null) {
                throw new IOException("Cannot find " + CONFIG_FILE);
            }
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application configuration", e);
        }
    }

    public static String getPassword() {
        return properties.getProperty("login.password");
    }
}