package org.test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Database {
    private static Connection connection;

    public static void initConnection(String propertiesFilePath) {
        try {
            if(connection == null) {
                Properties properties = new Properties();
                properties.load(new FileInputStream(propertiesFilePath));
                String url = properties.getProperty("url");
                String login = properties.getProperty("login");
                String password = properties.getProperty("password");
                connection = DriverManager.getConnection(url, login, password);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static Connection getConnection (String propertiesFilePath) {
        initConnection(propertiesFilePath);
        return connection;
    }

    public static Connection getConnection () {
        return connection;
    }

    public static void close () {
        if(connection != null) {
            try {
                connection.close();
            }
            catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
