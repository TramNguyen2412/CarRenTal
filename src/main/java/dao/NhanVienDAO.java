package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.NhanVien;
import util.DatabaseUtil;

public class NhanVienDAO {
    private Connection conn;
    
    public NhanVienDAO() {
        try {
            conn = DatabaseUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Kiểm tra và khôi phục kết nối nếu cần
    private void checkConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DatabaseUtil.getConnection();
        }
    }
    
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> danhSachNV = new ArrayList<>();
        
        try {
            checkConnection();
            
            String sql = "SELECT * FROM NHANVIEN ORDER BY HoTen";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
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
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return danhSachNV;
    }
    
    public NhanVien getNhanVienByMa(String maNV) {
        NhanVien nv = null;
        
        try {
            checkConnection();
            
            String sql = "SELECT * FROM NHANVIEN WHERE MaNV = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maNV);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setMaTK(rs.getString("MaTK"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setChucVu(rs.getString("ChucVu"));
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return nv;
    }
    
    public NhanVien getNhanVienByMaTK(String maTK) {
        NhanVien nv = null;
        
        try {
            checkConnection();
            
            String sql = "SELECT * FROM NHANVIEN WHERE MaTK = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maTK);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setMaTK(rs.getString("MaTK"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setChucVu(rs.getString("ChucVu"));
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return nv;
    }
    
    public boolean existsNhanVien(String maNV) {
        boolean exists = false;
        
        try {
            checkConnection();
            
            String sql = "SELECT COUNT(*) FROM NHANVIEN WHERE MaNV = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maNV);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                exists = true;
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return exists;
    }
}