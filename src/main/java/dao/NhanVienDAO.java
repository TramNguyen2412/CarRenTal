package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.NhanVien;
import util.DatabaseUtil;

public class NhanVienDAO {
    // Phương thức kiểm tra và lấy kết nối hợp lệ
    private Connection getValidConnection() throws SQLException {
        Connection conn = DatabaseUtil.getConnection();
        
        // Kiểm tra kết nối còn hợp lệ không
        if (!conn.isValid(2)) { // timeout 2 giây
          //  DatabaseUtil.reconnect();
            conn = DatabaseUtil.getConnection();
        }
        
        return conn;
    }
    
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> danhSachNV = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT * FROM NHANVIEN ORDER BY HoTen";
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setMaTK(rs.getString("MaTK"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setChucVu(rs.getString("ChucVu"));
                
                danhSachNV.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    DatabaseUtil.reconnect();
//                    return getAllNhanVien(); // Thử lại một lần
//                } catch (SQLException ex) {
//                    // Chỉ ghi log khi không thể kết nối lại
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return danhSachNV;
    }
    
    public NhanVien getNhanVienByMa(String maNV) {
        NhanVien nv = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT * FROM NHANVIEN WHERE MaNV = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maNV);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setMaTK(rs.getString("MaTK"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setChucVu(rs.getString("ChucVu"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    DatabaseUtil.reconnect();
//                    return getNhanVienByMa(maNV); // Thử lại một lần
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return nv;
    }
    
    public NhanVien getNhanVienByMaTK(String maTK) {
        NhanVien nv = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT * FROM NHANVIEN WHERE MaTK = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maTK);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setMaTK(rs.getString("MaTK"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setChucVu(rs.getString("ChucVu"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    DatabaseUtil.reconnect();
//                    return getNhanVienByMaTK(maTK); // Thử lại một lần
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return nv;
    }
    
    public boolean existsNhanVien(String maNV) {
        boolean exists = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT COUNT(*) FROM NHANVIEN WHERE MaNV = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maNV);
            
            rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                exists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    DatabaseUtil.reconnect();
//                    return existsNhanVien(maNV); // Thử lại một lần
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return exists;
    }
}