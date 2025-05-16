package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.KhachHang;
import util.DatabaseUtil;

public class KhachHangDAO {
    private Connection conn;
    
    public KhachHangDAO() {
         try {
            conn = DatabaseUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> danhSachKH = new ArrayList<>();
        
        try {
            String sql = "SELECT * FROM KHACHHANG ORDER BY MAKH";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
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
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return danhSachKH;
    }
    
    public KhachHang getKhachHangByMa(String maKH) {
        KhachHang kh = null;
        
        try {
            String sql = "SELECT * FROM KHACHHANG WHERE MAKH = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maKH);
            
            ResultSet rs = pstmt.executeQuery();
            
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
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return kh;
    }
    
    public String addKhachHang(KhachHang kh) {
        String maKH = generateMaKH();
        
        try {
            String sql = "INSERT INTO KHACHHANG (MAKH, MATK, TONGTIENNO, HOTEN, SDT, EMAIL, CCCD, DIACHI) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maKH);
            pstmt.setString(2, kh.getMaTK());
            pstmt.setDouble(3, kh.getTongTienNo());
            pstmt.setString(4, kh.getHoTen());
            pstmt.setString(5, kh.getSdt());
            pstmt.setString(6, kh.getEmail());
            pstmt.setString(7, kh.getCccd());
            pstmt.setString(8, kh.getDiaChi());
            
            int rows = pstmt.executeUpdate();
            pstmt.close();
            
            if (rows > 0) {
                return maKH;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean updateKhachHang(KhachHang kh) {
        try {
            String sql = "UPDATE KHACHHANG SET MATK = ?, TONGTIENNO = ?, HOTEN = ?, SDT = ?, " +
                         "EMAIL = ?, CCCD = ?, DIACHI = ? WHERE MAKH = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, kh.getMaTK());
            pstmt.setDouble(2, kh.getTongTienNo());
            pstmt.setString(3, kh.getHoTen());
            pstmt.setString(4, kh.getSdt());
            pstmt.setString(5, kh.getEmail());
            pstmt.setString(6, kh.getCccd());
            pstmt.setString(7, kh.getDiaChi());
            pstmt.setString(8, kh.getMaKH());
            
            int rows = pstmt.executeUpdate();
            pstmt.close();
            
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteKhachHang(String maKH) {
        try {
            String sql = "DELETE FROM KHACHHANG WHERE MAKH = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maKH);
            
            int rows = pstmt.executeUpdate();
            pstmt.close();
            
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<KhachHang> searchKhachHang(String keyword) {
        List<KhachHang> danhSachKH = new ArrayList<>();
        
        try {
            String sql = "SELECT * FROM KHACHHANG WHERE UPPER(MAKH) LIKE ? OR UPPER(HOTEN) LIKE ? " +
                         "OR SDT LIKE ? OR UPPER(EMAIL) LIKE ? OR CCCD LIKE ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            String searchParam = "%" + keyword.toUpperCase() + "%";
            pstmt.setString(1, searchParam);
            pstmt.setString(2, searchParam);
            pstmt.setString(3, searchParam);
            pstmt.setString(4, searchParam);
            pstmt.setString(5, searchParam);
            
            ResultSet rs = pstmt.executeQuery();
            
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
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return danhSachKH;
    }
    
    public boolean isPhoneNumberExists(String sdt, String excludeMaKH) {
        try {
            String sql = "SELECT COUNT(*) FROM KHACHHANG WHERE SDT = ?";
            
            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                sql += " AND MAKH != ?";
            }
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sdt);
            
            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                pstmt.setString(2, excludeMaKH);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean isEmailExists(String email, String excludeMaKH) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        
        try {
            String sql = "SELECT COUNT(*) FROM KHACHHANG WHERE UPPER(EMAIL) = UPPER(?)";
            
            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                sql += " AND MAKH != ?";
            }
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            
            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                pstmt.setString(2, excludeMaKH);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean isCCCDExists(String cccd, String excludeMaKH) {
        if (cccd == null || cccd.isEmpty()) {
            return false;
        }
        
        try {
            String sql = "SELECT COUNT(*) FROM KHACHHANG WHERE CCCD = ?";
            
            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                sql += " AND MAKH != ?";
            }
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cccd);
            
            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                pstmt.setString(2, excludeMaKH);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Tạo mã khách hàng mới
    private String generateMaKH() {
        try {
            String sql = "SELECT 'KH' || LPAD(NVL(MAX(TO_NUMBER(SUBSTR(MAKH, 3))), 0) + 1, 6, '0') AS MAKH " +
                         "FROM KHACHHANG";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                return rs.getString("MAKH");
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return "KH000001"; // Mặc định nếu không có khách hàng nào
    }
}