package com.uniajc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConexionDatabase {
    /**
     * Crea una conexión nueva usando los valores de config.properties.
     * 
     * @return Conexión JDBC lista para usar y cerrar por quien la solicitó.
     * @throws SQLException si no se puede leer la configuración o conectar.
     */
    public static Connection getConnection() throws SQLException {
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new SQLException("No se pudo leer config.properties.", e);
        }

        String url = getRequiredProperty(properties, "URL");
        String user = getRequiredProperty(properties, "USERNAME");
        String password = getRequiredProperty(properties, "PASSWORD");

        return DriverManager.getConnection(url, user, password);
    }

    private static String getRequiredProperty(Properties properties, String key) throws SQLException {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new SQLException("Falta la propiedad requerida " + key + " en config.properties.");
        }
        return value;
    }
}
