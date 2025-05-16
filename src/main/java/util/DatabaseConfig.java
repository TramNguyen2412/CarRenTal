package util;

public class DatabaseConfig {
    // Mỗi người sẽ tùy chỉnh file này theo môi trường của mình
    // File này nên được thêm vào .gitignore để không push lên repository
    public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    public static final String USERNAME = "TESTDOAN";
    public static final String PASSWORD = "abc123";
    public static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
}