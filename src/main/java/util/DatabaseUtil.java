//package util;
//
//import java.sql.*;
//
//public class DatabaseUtil {
//    private static Connection connection;
//    
// 
//    // Phương thức synchronized để đảm bảo an toàn cho đa luồng
//    public static synchronized Connection getConnection() throws SQLException {
//        try {
//            if (connection == null || connection.isClosed()) {
//                System.out.println("Creating new database connection");
//                try {
//                    Class.forName(DatabaseConfig.DRIVER);
//                    connection = DriverManager.getConnection(
//                        DatabaseConfig.URL, 
//                        DatabaseConfig.USERNAME, 
//                        DatabaseConfig.PASSWORD
//                    );
//                } catch (ClassNotFoundException e) {
//                    throw new SQLException("Oracle JDBC Driver not found", e);
//                }
//            }
//            return connection;
//        } catch (SQLException e) {
//            System.err.println("Error getting connection: " + e.getMessage());
//            throw e;
//        }
//    }
//    
//    // Phương thức để khởi tạo lại kết nối khi gặp lỗi
//    public static synchronized void reconnect() throws SQLException {
//        closeConnection(); // Đóng kết nối cũ nếu còn tồn tại
//        connection = null; // Đặt về null để tạo kết nối mới
//        getConnection(); // Tạo kết nối mới
//        System.out.println("Successfully reconnected to database");
//    }
//    
//     public static synchronized void closeConnection() {
//            try {
//                if (connection != null && !connection.isClosed()) {
//                    connection.close();
//                    System.out.println("Database connection closed");
//                }
//            } catch (SQLException e) {
//                System.err.println("Error closing connection: " + e.getMessage());
//            }
//        }
//
//        // Phương thức kiểm tra kết nối hợp lệ
//        public static synchronized boolean isConnectionValid() {
//            try {
//                return connection != null && !connection.isClosed() && connection.isValid(5); // timeout 5 giây
//            } catch (SQLException e) {
//                System.err.println("Error checking connection: " + e.getMessage());
//                return false;
//            }
//        }
//    }


//
//

//
//
package util;

import java.sql.*;

public class DatabaseUtil {
    // Thêm biến connection được sử dụng lại
    private static Connection sharedConnection = null;
    
    // Vẫn giữ tên và tham số y nguyên - Lấy connection từ shared pool hoặc tạo mới nếu cần
    public static Connection getConnection() throws SQLException {
        try {
          
            // Nếu chưa có connection hoặc connection đã đóng, tạo mới
            if (sharedConnection == null || sharedConnection.isClosed()) {
                Class.forName(DatabaseConfig.DRIVER);
                sharedConnection = DriverManager.getConnection(
                    DatabaseConfig.URL, 
                    DatabaseConfig.USERNAME, 
                    DatabaseConfig.PASSWORD
                );
                System.out.println("Đã tạo kết nối mới đến database");
            }
           
            return sharedConnection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC Driver không tìm thấy", e);
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối: " + e.getMessage());
            throw e;
        }
    }
    
    // Phương thức mới - Khi cần một connection mới hoàn toàn (cho transaction riêng biệt)
    public static Connection getNewConnection() throws SQLException {
        try {
            Class.forName(DatabaseConfig.DRIVER);
            Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, 
                DatabaseConfig.USERNAME, 
                DatabaseConfig.PASSWORD
            );
            System.out.println("Đã tạo kết nối MỚI và ĐỘC LẬP đến database");
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC Driver không tìm thấy", e);
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối: " + e.getMessage());
            throw e;
        }
    }
    
    // Vẫn giữ nguyên tên và tham số - Phương thức để khởi tạo lại kết nối khi gặp lỗi
    public static Connection reconnect(Connection oldConnection) throws SQLException {
        closeConnection(oldConnection); // Đóng kết nối cũ nếu còn tồn tại
        
        // Nếu đây là shared connection, cập nhật biến static
        if (oldConnection == sharedConnection) {
            sharedConnection = getNewConnection();
            return sharedConnection;
        } else {
            // Nếu không phải shared connection, chỉ tạo connection mới
            return getNewConnection();
        }
    }
    
    // Vẫn giữ nguyên tên và tham số - Đóng kết nối
    public static void closeConnection(Connection conn) {
        try {
            // Chỉ đóng khi không phải shared connection
            if (conn != null && !conn.isClosed() && conn != sharedConnection) {
                conn.close();
                System.out.println("Đã đóng kết nối database");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đóng kết nối: " + e.getMessage());
        }
    }

    // Vẫn giữ nguyên tên và tham số - Thiết lập isolation level
    public static void setTransactionIsolation(Connection conn, int level) throws SQLException {
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(level);
    }
    
    // Vẫn giữ nguyên tên và tham số - Kiểm tra kết nối hợp lệ
    public static boolean isConnectionValid(Connection conn) {
        try {
            return conn != null && !conn.isClosed() && conn.isValid(5); // timeout 5 giây
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra kết nối: " + e.getMessage());
            return false;
        }
    }
}