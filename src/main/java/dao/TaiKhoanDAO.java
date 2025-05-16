package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.TaiKhoan;
import util.DatabaseUtil;

public class TaiKhoanDAO {
    
    public TaiKhoan checkLogin(String username, String password) {
        String sql = "SELECT * FROM TAIKHOAN WHERE TenDangNhap = ? AND MatKhau = ? AND TrangThai = 'Hoạt động'";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setMaTK(rs.getString("MaTK"));
                    tk.setMaVaiTro(rs.getString("MaVaiTro"));
                    tk.setTenDangNhap(rs.getString("TenDangNhap"));
                    tk.setMatKhau(rs.getString("MatKhau"));
                    tk.setTrangThai(rs.getString("TrangThai"));
                    return tk;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<TaiKhoan> getAllTaiKhoan() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM TAIKHOAN";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setMaTK(rs.getString("MaTK"));
                tk.setMaVaiTro(rs.getString("MaVaiTro"));
                tk.setTenDangNhap(rs.getString("TenDangNhap"));
                tk.setMatKhau(rs.getString("MatKhau"));
                tk.setTrangThai(rs.getString("TrangThai"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    // Các phương thức CRUD khác: insert, update, delete...
}