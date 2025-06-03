package dao;

import model.KhachHangDoanhThu;
import model.XeDoanhThu;
import java.util.concurrent.atomic.AtomicInteger;

import java.sql.*;
import java.util.*;
import oracle.jdbc.OracleTypes;
import util.DatabaseUtil;
public class ThongKeDAO {
    // Lấy số liệu tổng quan
   private static boolean reportViewLocked = false;
    private static Connection lockedConnection = null; 
    
    private static int isolationLevel = Connection.TRANSACTION_READ_COMMITTED; // Mặc định
    public static void setIsolationLevel(int level) {
        isolationLevel = level;
        // Không reset kết nối ở đây để giữ nguyên phiên xem báo cáo nếu đang mở
    }


    public Map<String, Number> getTongQuan() {
        Map<String, Number> result = new HashMap<>();
        Connection conn = null;
        CallableStatement cstmt = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "{call sp_ThongKeTongQuan(?, ?, ?, ?)}";
            cstmt = conn.prepareCall(sql);
            
            cstmt.registerOutParameter(1, java.sql.Types.INTEGER); // TongSoXe
            cstmt.registerOutParameter(2, java.sql.Types.INTEGER); // TongSoKhachHang
            cstmt.registerOutParameter(3, java.sql.Types.INTEGER); // TongSoHopDong
            cstmt.registerOutParameter(4, java.sql.Types.DOUBLE);  // TongDoanhThu
            
            cstmt.execute();
            
            // Lấy kết quả từ OUT parameters
            result.put("tongSoXe", cstmt.getInt(1));
            result.put("tongSoKhachHang", cstmt.getInt(2));
            result.put("tongSoHopDong", cstmt.getInt(3));
            result.put("tongDoanhThu", cstmt.getDouble(4));
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (cstmt != null) cstmt.close();
                // Không đóng connection ở đây
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    // Lấy dữ liệu doanh thu theo tháng trong năm
    public Map<Integer, Double> getDoanhThuTheoThang(int nam) {
        Map<Integer, Double> result = new HashMap<>();
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            // Khởi tạo tất cả các tháng với giá trị 0
            for (int i = 1; i <= 12; i++) {
                result.put(i, 0.0);
            }
            
            String sql = "{call sp_BaoCaoDoanhThuNam(?, ?)}";
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, nam);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            
            cstmt.execute();
            
            // Lấy ResultSet từ OUT parameter
            rs = (ResultSet) cstmt.getObject(2);
            
            // Cập nhật giá trị từ kết quả query
            while (rs.next()) {
                int thang = rs.getInt("Thang");
                double doanhThu = rs.getDouble("TongDoanhThu");
                result.put(thang, doanhThu);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (cstmt != null) cstmt.close();
                // Không đóng connection ở đây
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    // Lấy dữ liệu doanh thu theo khách hàng trong năm
    public List<KhachHangDoanhThu> getDoanhThuTheoKhachHang(int nam) {
        List<KhachHangDoanhThu> result = new ArrayList<>();
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "{call sp_BaoCaoDoanhThuKhachHang(?, ?)}";
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, nam);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            
            cstmt.execute();
            
            // Lấy ResultSet từ OUT parameter
            rs = (ResultSet) cstmt.getObject(2);
            
            while (rs.next()) {
                KhachHangDoanhThu item = new KhachHangDoanhThu();
                item.setMaKH(rs.getString("MaKH"));
                item.setHoTen(rs.getString("HoTen"));
                item.setSoHopDong(rs.getInt("SoHopDong"));
                item.setDoanhThu(rs.getDouble("TongDoanhThu"));
                result.add(item);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (cstmt != null) cstmt.close();
                // Không đóng connection ở đây
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    // Lấy dữ liệu doanh thu theo xe trong năm
    public List<XeDoanhThu> getDoanhThuTheoXe(int nam) {
        List<XeDoanhThu> result = new ArrayList<>();
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "{call sp_BaoCaoDoanhThuXe(?, ?)}";
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, nam);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            
            cstmt.execute();
            
            // Lấy ResultSet từ OUT parameter
            rs = (ResultSet) cstmt.getObject(2);
            
            while (rs.next()) {
                XeDoanhThu item = new XeDoanhThu();
                item.setMaXe(rs.getString("MaXe"));
                item.setTenXe(rs.getString("TenXe"));
                item.setBienSo(rs.getString("BienSo"));
                item.setSoLuotThue(rs.getInt("SoLuotThue"));
                item.setDoanhThu(rs.getDouble("DoanhThu"));
                result.add(item);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (cstmt != null) cstmt.close();
                // Không đóng connection ở đây
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }
    

     //Phương thức lấy connection hợp lệ
//    private Connection getValidConnection() throws SQLException {
//      
//        return DatabaseUtil.getConnection();
//    }
//    
//    private Connection getValidConnection() throws SQLException {
//        if (reportViewLocked && lockedConnection != null && !lockedConnection.isClosed()) {
//            // Nếu đang trong chế độ "xem báo cáo", trả về connection cũ
//            return lockedConnection;
//        }
//
//        // Ngược lại lấy connection mới
//        Connection conn = DatabaseUtil.getConnection();
//
//        // Thiết lập TRANSACTION_READ_COMMITTED để cho phép phantom read
//        if (reportViewLocked) {
//            conn.setAutoCommit(false);
//          //  conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//          //  conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
//            
//            lockedConnection = conn;
//        }
//
//        return conn;
//    }
//    private Connection getValidConnection() throws SQLException {
//        if (reportViewLocked && lockedConnection != null && !lockedConnection.isClosed()) {
//            return lockedConnection;
//        }
//
//        // Nếu chưa có kết nối hoặc đã đóng, tạo mới
//        Connection conn = DatabaseUtil.getConnection();
//        if (reportViewLocked) {
//            conn.setAutoCommit(false);
//            conn.setTransactionIsolation(isolationLevel);
//            lockedConnection = conn;
//        }
//        return conn;
//    }
    
    private Connection getValidConnection() throws SQLException {
        if (reportViewLocked && lockedConnection != null && !lockedConnection.isClosed()) {
            System.out.println("==== REUSING EXISTING CONNECTION: " + lockedConnection.hashCode() + " ====");
            System.out.println("==== WITH ISOLATION LEVEL: " + 
                (lockedConnection.getTransactionIsolation() == Connection.TRANSACTION_SERIALIZABLE ? 
                "SERIALIZABLE" : "READ_COMMITTED") + " ====");

            return lockedConnection;
        } else {
            Connection conn = DatabaseUtil.getConnection();
            if (reportViewLocked) {
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(isolationLevel);
                System.out.println("==== CREATED NEW CONNECTION: " + conn.hashCode() + " ====");
                System.out.println("==== SET ISOLATION LEVEL: " + 
                    (isolationLevel == Connection.TRANSACTION_SERIALIZABLE ? 
                    "SERIALIZABLE" : "READ_COMMITTED") + " ====");
                lockedConnection = conn;
            }
            return conn;
        }
        

    }


    public static void startReportView() {
    reportViewLocked = true;
    try {
        // Đóng kết nối cũ nếu có
        if (lockedConnection != null) {
            try { lockedConnection.close(); } catch (Exception e) {}
            lockedConnection = null;
        }
        
        // Tạo kết nối mới và thiết lập isolation level
        lockedConnection = DatabaseUtil.getConnection();
        lockedConnection.setAutoCommit(false);
        lockedConnection.setTransactionIsolation(isolationLevel);
        
        System.out.println("==== startReportView: NEW CONNECTION " + lockedConnection.hashCode() + " ====");
        System.out.println("==== WITH ISOLATION LEVEL: " + 
            (isolationLevel == Connection.TRANSACTION_SERIALIZABLE ? 
            "SERIALIZABLE" : "READ_COMMITTED") + " ====");
            
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    // Phương thức kết thúc "xem báo cáo" - gọi khi thoát tab thống kê
    public static void endReportView() {
        reportViewLocked = false;
        try {
            if (lockedConnection != null) {
                lockedConnection.close();
                lockedConnection = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Đã kết thúc chế độ xem báo cáo");
    }
    public static int getIsolationLevel() {
        return isolationLevel;
    }

}