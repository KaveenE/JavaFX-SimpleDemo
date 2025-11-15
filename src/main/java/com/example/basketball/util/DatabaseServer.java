package com.example.basketball.util;

import org.h2.tools.RunScript;
import org.h2.tools.Server;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseServer {

    private static Server tcpServer;
    private static Server webServer;   // for the console
    private static boolean initialized = false;
    private static final Properties props = new Properties();

    static {
        try (InputStream is = JDBCUtil.class.getResourceAsStream("/config/db.properties")) {
            if (is == null) throw new IOException("db.properties not found");
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load DB config", e);
        }
    }

    private DatabaseServer() {}

    public static synchronized void initIfNeeded() {
        if (initialized) return;

        try (Connection conn = JDBCUtil.getConnection();
             InputStream is = DatabaseServer.class.getResourceAsStream("/init.sql");
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)){

            if (is == null) {
                throw new IllegalStateException("init.sql not found on classpath");
            }

            RunScript.execute(conn, reader);
            System.out.println("Database initialized from init.sql");
            initialized = true;

        } catch (SQLException e) {
            // If table already exists → ignore
            if (e.getMessage().contains("already exists")) {
                System.out.println("Database already initialized");
                initialized = true;
            } else {
                throw new RuntimeException("Failed to initialize database", e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read init.sql", e);
        }
    }

    /** Start TCP server (and optional web console) */
    public static synchronized void start() {
        if (tcpServer != null && tcpServer.isRunning(false)) {
            System.out.println("H2 TCP server already running");
            return;
        }

        try {

            String tcpPort = props.getProperty("tcp.port", "9092");
            tcpServer = Server.createTcpServer(
                    "-tcp",
                    "-tcpPort",
                    tcpPort,
                    "-tcpAllowOthers").start();
            System.out.println("H2 TCP server started: " + tcpServer.getURL());

            String webPort = props.getProperty("web.port", "8082");
            webServer = Server.createWebServer("-web",
                    "-webPort",
                    webPort).start();
            System.out.println("H2 Console → http://localhost:8082");

            // Auto‑open browser (works on Windows/macOS/Linux with a desktop)
            try {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(String.join("http://localhost:",
                        webPort)));
            } catch (Exception ignored) {}

        } catch (SQLException e) {
            throw new RuntimeException("Cannot start H2 server", e);
        }
    }

    /** Graceful shutdown */
    public static synchronized void stop() {
        if (webServer != null) { webServer.stop(); webServer = null; }
        if (tcpServer != null) { tcpServer.stop();  tcpServer = null; }
        System.out.println("H2 servers stopped");
    }
}
