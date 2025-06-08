package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.KhachHang;
import model.LichSuCongNo;
import util.DatabaseUtil;
public class CongNoDAO {
    //đoạn thêm mới
    // Lấy số liệu tổng quan
    private static boolean reportViewLocked = false;
    private static Connection lockedConnection = null; 
    
    private static int isolationLevel = Connection.TRANSACTION_READ_COMMITTED; // Mặc định
    public static void setIsolationLevel(int level) {
        isolationLevel = level;
        // Không reset kết nối ở đây để giữ nguyên phiên xem báo cáo nếu đang mở
    }
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
        //-----kết thúc đoạn thêm mới-----
 // ...existing code...
    public List<LichSuCongNo> getAllLichSuCongNo() {
        List<LichSuCongNo> list = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getValidConnection();
            String sql = "SELECT ls.*, kh.HoTen as TenKH " +
                         "FROM LICHSUCONGNO ls " +
                         "JOIN KHACHHANG kh ON ls.MaKH = kh.MaKH " +
                         "ORDER BY ls.NgayGiaoDich DESC";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                LichSuCongNo ls = new LichSuCongNo();
                ls.setMaLichSu(rs.getString("MaLichSu"));
                ls.setMaKH(rs.getString("MaKH"));
                ls.setNgayGiaoDich(rs.getDate("NgayGiaoDich"));
                ls.setLoaiGiaoDich(rs.getString("LoaiGiaoDich"));
                ls.setSoTien(rs.getDouble("SoTien"));
                ls.setGhiChu(rs.getString("GhiChu"));
                list.add(ls);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving debt history: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                // Không đóng connection ở đây để giữ transaction nếu cần
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
// ...existing code...

    
    public List<LichSuCongNo> getLichSuCongNoByKhachHang(String maKH) {
        List<LichSuCongNo> list = new ArrayList<>();
        String sql = "SELECT ls.*, kh.HoTen as TenKH " +
                     "FROM LICHSUCONGNO ls " +
                     "JOIN KHACHHANG kh ON ls.MaKH = kh.MaKH " +
                     "WHERE ls.MaKH = ? " +
                     "ORDER BY ls.NgayGiaoDich DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKH);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LichSuCongNo ls = new LichSuCongNo();
                    ls.setMaLichSu(rs.getString("MaLichSu"));
                    ls.setMaKH(rs.getString("MaKH"));
                    ls.setNgayGiaoDich(rs.getDate("NgayGiaoDich"));
                    ls.setLoaiGiaoDich(rs.getString("LoaiGiaoDich"));
                    ls.setSoTien(rs.getDouble("SoTien"));
                    ls.setGhiChu(rs.getString("GhiChu"));
                    list.add(ls);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving debt history by customer: " + e.getMessage());
        }
        
        return list;
    }
    
    public boolean addLichSuCongNo(LichSuCongNo ls) throws SQLException {
    String sql = "INSERT INTO LICHSUCONGNO (MaKH, NgayGiaoDich, LoaiGiaoDich, SoTien, GhiChu) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, ls.getMaKH());
        pstmt.setDate(2, new java.sql.Date(ls.getNgayGiaoDich().getTime()));
        pstmt.setString(3, ls.getLoaiGiaoDich());
        pstmt.setDouble(4, ls.getSoTien());
        pstmt.setString(5, ls.getGhiChu());
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
    }
}
    
public boolean updateLichSuCongNo(LichSuCongNo ls) throws SQLException {
    String sql = "UPDATE LICHSUCONGNO SET MaKH = ?, NgayGiaoDich = ?, LoaiGiaoDich = ?, SoTien = ?, GhiChu = ? WHERE MaLichSu = ?";
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, ls.getMaKH());
        pstmt.setDate(2, new java.sql.Date(ls.getNgayGiaoDich().getTime()));
        pstmt.setString(3, ls.getLoaiGiaoDich());
        pstmt.setDouble(4, ls.getSoTien());
        pstmt.setString(5, ls.getGhiChu());
        pstmt.setString(6, ls.getMaLichSu());
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
    }
}
    
    public boolean deleteLichSuCongNo(String maLichSu) {
        String sql = "DELETE FROM LICHSUCONGNO WHERE MaLichSu = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLichSu);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting debt history: " + e.getMessage());
            return false;
        }
    }
    
    // ...existing code...
        public List<KhachHang> getKhachHangCoCongNo() {
            List<KhachHang> list = new ArrayList<>();
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                conn = getValidConnection();
                String sql = "SELECT * FROM KHACHHANG WHERE TongTienNo > 0 ORDER BY TongTienNo DESC";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    KhachHang kh = new KhachHang();
                    kh.setMaKH(rs.getString("MaKH"));
                    kh.setMaTK(rs.getString("MaTK"));
                    kh.setTongTienNo(rs.getDouble("TongTienNo"));
                    kh.setHoTen(rs.getString("HoTen"));
                    kh.setSdt(rs.getString("SDT"));
                    kh.setEmail(rs.getString("Email"));
                    kh.setCccd(rs.getString("CCCD"));
                    kh.setDiaChi(rs.getString("DiaChi"));
                    list.add(kh);
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving customers with debt: " + e.getMessage());
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    // Không đóng connection ở đây để giữ transaction nếu cần
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
    // ...existing code...
    
    public double getTongCongNoKhachHang(String maKH) {
        String sql = "SELECT TongTienNo FROM KHACHHANG WHERE MaKH = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKH);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("TongTienNo");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving customer debt: " + e.getMessage());
        }
        
        return 0;
    }
    //---//
    public LichSuCongNo getLichSuCongNoByMa(String maLichSu) {
        String sql = "SELECT * FROM LICHSUCONGNO WHERE MaLichSu = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getValidConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maLichSu);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                LichSuCongNo ls = new LichSuCongNo();
                ls.setMaLichSu(rs.getString("MaLichSu"));
                ls.setMaKH(rs.getString("MaKH"));
                ls.setNgayGiaoDich(rs.getDate("NgayGiaoDich"));
                ls.setLoaiGiaoDich(rs.getString("LoaiGiaoDich"));
                ls.setSoTien(rs.getDouble("SoTien"));
                ls.setGhiChu(rs.getString("GhiChu"));
                return ls;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving debt history by id: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                // Không đóng connection ở đây để giữ transaction nếu cần
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    // LichSuCongNoDAO.java
    public boolean updateLichSuCongNoThongTinChung(LichSuCongNo ls) {
    String sql = "UPDATE LichSuCongNo SET MaKH=?, NgayGiaoDich=?, GhiChu=? WHERE MaLichSu=?";
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, ls.getMaKH());
        ps.setDate(2, new java.sql.Date(ls.getNgayGiaoDich().getTime()));
        ps.setString(3, ls.getGhiChu());
        ps.setString(4, ls.getMaLichSu());
        int rows = ps.executeUpdate();
        return rows > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    public List<LichSuCongNo> searchLichSuCongNo(String keyword) {
    List<LichSuCongNo> list = new ArrayList<>();
    String sql = "SELECT * FROM LICHSUCONGNO WHERE " +
                 "UPPER(MaLichSu) LIKE ? OR UPPER(MaKH) LIKE ? OR UPPER(LoaiGiaoDich) LIKE ? OR UPPER(GhiChu) LIKE ?";
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        String search = "%" + keyword.toUpperCase() + "%";
        for (int i = 1; i <= 4; i++) pstmt.setString(i, search);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                LichSuCongNo ls = new LichSuCongNo();
                ls.setMaLichSu(rs.getString("MaLichSu"));
                ls.setMaKH(rs.getString("MaKH"));
                ls.setNgayGiaoDich(rs.getDate("NgayGiaoDich"));
                ls.setLoaiGiaoDich(rs.getString("LoaiGiaoDich"));
                ls.setSoTien(rs.getDouble("SoTien"));
                ls.setGhiChu(rs.getString("GhiChu"));
                list.add(ls);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
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