package com.uniajc.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Lee config.properties (raíz del proyecto) y entrega conexiones JDBC. */
public class ConexionDatabase {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = new FileInputStream("config.properties")) {
            PROPS.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("No se pudo leer config.properties: " + e.getMessage());
        }
    }

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(
                PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"),
                PROPS.getProperty("db.password"));
    }
}
