//import java.sql.Connection;
//import java.sql.SQLException;
//import util.DatabaseUtil;
//
//public class TestConnection {
//    public static void main(String[] args) {
//        System.out.println("Đang kiểm tra kết nối đến CSDL Oracle...");
//        
//        try {
//            Connection conn = DatabaseUtil.getConnection();
//            
//            if (conn != null && !conn.isClosed()) {
//                System.out.println("✓ KẾT NỐI THÀNH CÔNG!");
//                System.out.println("- URL: " + DatabaseUtil.URL);
//                System.out.println("- Username: " + DatabaseUtil.USERNAME);
//                System.out.println("- Thời gian kết nối: " + new java.util.Date());
//            }
//            
//            DatabaseUtil.closeConnection();
//            System.out.println("✓ Đã đóng kết nối");
//            
//        } catch (SQLException e) {
//            System.out.println("✗ LỖI KẾT NỐI: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}