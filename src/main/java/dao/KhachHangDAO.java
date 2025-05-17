
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.KhachHang;
import util.DatabaseUtil;

public class KhachHangDAO {
    
    // Phương thức kiểm tra kết nối và khôi phục nếu cần
    private Connection getValidConnection() throws SQLException {
        Connection conn = DatabaseUtil.getConnection();
        
        // Kiểm tra kết nối còn hợp lệ không
        if (!conn.isValid(2)) { // timeout 2 giây
            System.out.println("Connection invalidated, reconnecting...");
            DatabaseUtil.reconnect();
            conn = DatabaseUtil.getConnection();
        }
        
        return conn;
    }
    
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> danhSachKH = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT * FROM KHACHHANG ORDER BY MAKH";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MAKH"));
                kh.setMaTK(rs.getString("MATK"));
                kh.setTongTienNo(rs.getDouble("TONGTIENNO"));
                kh.setHoTen(rs.getString("HOTEN"));
                kh.setSdt(rs.getString("SDT"));
                kh.setEmail(rs.getString("EMAIL"));
                kh.setCccd(rs.getString("CCCD"));
                kh.setDiaChi(rs.getString("DIACHI"));
                
                danhSachKH.add(kh);
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllKhachHang: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getAllKhachHang");
                    DatabaseUtil.reconnect();
                    return getAllKhachHang(); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            // Đóng các tài nguyên nhưng KHÔNG đóng kết nối
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return danhSachKH;
    }
    
    public KhachHang getKhachHangByMa(String maKH) {
        KhachHang kh = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT * FROM KHACHHANG WHERE MAKH = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maKH);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                kh = new KhachHang();
                kh.setMaKH(rs.getString("MAKH"));
                kh.setMaTK(rs.getString("MATK"));
                kh.setTongTienNo(rs.getDouble("TONGTIENNO"));
                kh.setHoTen(rs.getString("HOTEN"));
                kh.setSdt(rs.getString("SDT"));
                kh.setEmail(rs.getString("EMAIL"));
                kh.setCccd(rs.getString("CCCD"));
                kh.setDiaChi(rs.getString("DIACHI"));
            }
        } catch (SQLException e) {
            System.err.println("Error in getKhachHangByMa: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getKhachHangByMa");
                    DatabaseUtil.reconnect();
                    return getKhachHangByMa(maKH); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return kh;
    }
    
    public String addKhachHang(KhachHang kh) {
        String maKH = generateMaKH();
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "INSERT INTO KHACHHANG (MAKH, MATK, TONGTIENNO, HOTEN, SDT, EMAIL, CCCD, DIACHI) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maKH);
            pstmt.setString(2, kh.getMaTK());
            pstmt.setDouble(3, kh.getTongTienNo());
            pstmt.setString(4, kh.getHoTen());
            pstmt.setString(5, kh.getSdt());
            pstmt.setString(6, kh.getEmail());
            pstmt.setString(7, kh.getCccd());
            pstmt.setString(8, kh.getDiaChi());
            
            int rows = pstmt.executeUpdate();
            
            if (rows > 0) {
                return maKH;
            }
        } catch (SQLException e) {
            System.err.println("Error in addKhachHang: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in addKhachHang");
                    DatabaseUtil.reconnect();
                    return addKhachHang(kh); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return null;
    }
    
    public boolean updateKhachHang(KhachHang kh) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "UPDATE KHACHHANG SET MATK = ?, TONGTIENNO = ?, HOTEN = ?, SDT = ?, " +
                         "EMAIL = ?, CCCD = ?, DIACHI = ? WHERE MAKH = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, kh.getMaTK());
            pstmt.setDouble(2, kh.getTongTienNo());
            pstmt.setString(3, kh.getHoTen());
            pstmt.setString(4, kh.getSdt());
            pstmt.setString(5, kh.getEmail());
            pstmt.setString(6, kh.getCccd());
            pstmt.setString(7, kh.getDiaChi());
            pstmt.setString(8, kh.getMaKH());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error in updateKhachHang: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in updateKhachHang");
                    DatabaseUtil.reconnect();
                    return updateKhachHang(kh); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    public boolean deleteKhachHang(String maKH) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "DELETE FROM KHACHHANG WHERE MAKH = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maKH);
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error in deleteKhachHang: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in deleteKhachHang");
                    DatabaseUtil.reconnect();
                    return deleteKhachHang(maKH); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    public List<KhachHang> searchKhachHang(String keyword) {
        List<KhachHang> danhSachKH = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT * FROM KHACHHANG WHERE UPPER(MAKH) LIKE ? OR UPPER(HOTEN) LIKE ? " +
                         "OR SDT LIKE ? OR UPPER(EMAIL) LIKE ? OR CCCD LIKE ?";
            
            pstmt = conn.prepareStatement(sql);
            String searchParam = "%" + keyword.toUpperCase() + "%";
            pstmt.setString(1, searchParam);
            pstmt.setString(2, searchParam);
            pstmt.setString(3, searchParam);
            pstmt.setString(4, searchParam);
            pstmt.setString(5, searchParam);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MAKH"));
                kh.setMaTK(rs.getString("MATK"));
                kh.setTongTienNo(rs.getDouble("TONGTIENNO"));
                kh.setHoTen(rs.getString("HOTEN"));
                kh.setSdt(rs.getString("SDT"));
                kh.setEmail(rs.getString("EMAIL"));
                kh.setCccd(rs.getString("CCCD"));
                kh.setDiaChi(rs.getString("DIACHI"));
                
                danhSachKH.add(kh);
            }
        } catch (SQLException e) {
            System.err.println("Error in searchKhachHang: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in searchKhachHang");
                    DatabaseUtil.reconnect();
                    return searchKhachHang(keyword); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return danhSachKH;
    }
    
    public boolean isPhoneNumberExists(String sdt, String excludeMaKH) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT COUNT(*) FROM KHACHHANG WHERE SDT = ?";
            
            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                sql += " AND MAKH != ?";
            }
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sdt);
            
            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                pstmt.setString(2, excludeMaKH);
            }
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error in isPhoneNumberExists: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in isPhoneNumberExists");
                    DatabaseUtil.reconnect();
                    return isPhoneNumberExists(sdt, excludeMaKH); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return false;
    }
    
    public boolean isEmailExists(String email, String excludeMaKH) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT COUNT(*) FROM KHACHHANG WHERE UPPER(EMAIL) = UPPER(?)";
            
            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                sql += " AND MAKH != ?";
            }
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            
            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                pstmt.setString(2, excludeMaKH);
            }
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error in isEmailExists: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in isEmailExists");
                    DatabaseUtil.reconnect();
                    return isEmailExists(email, excludeMaKH); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return false;
    }
    
    public boolean isCCCDExists(String cccd, String excludeMaKH) {
        if (cccd == null || cccd.isEmpty()) {
            return false;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT COUNT(*) FROM KHACHHANG WHERE CCCD = ?";
            
            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                sql += " AND MAKH != ?";
            }
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cccd);
            
            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                pstmt.setString(2, excludeMaKH);
            }
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error in isCCCDExists: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in isCCCDExists");
                    DatabaseUtil.reconnect();
                    return isCCCDExists(cccd, excludeMaKH); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return false;
    }
    
    // Tạo mã khách hàng mới
    private String generateMaKH() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT 'KH' || LPAD(NVL(MAX(TO_NUMBER(SUBSTR(MAKH, 3))), 0) + 1, 6, '0') AS MAKH " +
                         "FROM KHACHHANG";
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                return rs.getString("MAKH");
            }
        } catch (SQLException e) {
            System.err.println("Error in generateMaKH: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in generateMaKH");
                    DatabaseUtil.reconnect();
                    return generateMaKH(); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return "KH000001"; // Mặc định nếu không có khách hàng nào
    }
}