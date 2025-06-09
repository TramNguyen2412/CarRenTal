package util;

public class DatabaseConfig {
    // Mỗi người sẽ tùy chỉnh file này theo môi trường của mình
    // File này nên được thêm vào .gitignore để không push lên repository
    public static final String URL = "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";
    public static final String USERNAME = "IS216";
    public static final String PASSWORD = "khai060125";
    public static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
}