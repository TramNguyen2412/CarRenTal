package util;

import java.sql.*;

public class DatabaseUtil {
    private static Connection connection;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName(DatabaseConfig.DRIVER);
                connection = DriverManager.getConnection(
                    DatabaseConfig.URL, 
                    DatabaseConfig.USERNAME, 
                    DatabaseConfig.PASSWORD
                );
            } catch (ClassNotFoundException e) {
                throw new SQLException("Oracle JDBC Driver not found", e);
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}