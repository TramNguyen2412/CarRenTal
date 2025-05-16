package dao;

import model.Xe;
import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class XeDAO {
    
    // Phương thức lấy tất cả xe từ database
    public List<Xe> getAllXe() {
        List<Xe> danhSachXe = new ArrayList<>();
        String sql = "SELECT * FROM XE";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Xe xe = new Xe();
                xe.setMaXe(rs.getString("MaXe"));
                xe.setTenXe(rs.getString("TenXe"));
                xe.setBienSo(rs.getString("BienSo"));
                xe.setSoCho(rs.getInt("SoCho"));
                xe.setHangXe(rs.getString("HangXe"));
                xe.setNamSX(rs.getInt("NamSX"));
                xe.setTrangThai(rs.getString("TrangThai"));
                xe.setGiaThueNgay(rs.getDouble("GiaThueNgay"));
                 xe.setHinhAnh(rs.getString("HinhAnh"));
                danhSachXe.add(xe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return danhSachXe;
    }
    
    // Phương thức lấy xe theo mã
    public Xe getXeByMa(String maXe) {
        String sql = "SELECT * FROM XE WHERE MaXe = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maXe);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Xe xe = new Xe();
                    xe.setMaXe(rs.getString("MaXe"));
                    xe.setTenXe(rs.getString("TenXe"));
                    xe.setBienSo(rs.getString("BienSo"));
                    xe.setSoCho(rs.getInt("SoCho"));
                    xe.setHangXe(rs.getString("HangXe"));
                    xe.setNamSX(rs.getInt("NamSX"));
                    xe.setTrangThai(rs.getString("TrangThai"));
                    xe.setGiaThueNgay(rs.getDouble("GiaThueNgay"));
                     xe.setHinhAnh(rs.getString("HinhAnh"));
                    return xe;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Phương thức thêm xe
    public boolean addXe(Xe xe) {
        String sql = "INSERT INTO XE (TenXe, BienSo, SoCho, HangXe, NamSX, TrangThai, GiaThueNgay, HinhAnh) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, xe.getTenXe());
                pstmt.setString(2, xe.getBienSo());
                pstmt.setInt(3, xe.getSoCho());
                pstmt.setString(4, xe.getHangXe());
                pstmt.setInt(5, xe.getNamSX());
                pstmt.setString(6, xe.getTrangThai());
                pstmt.setDouble(7, xe.getGiaThueNgay());
                pstmt.setString(8, xe.getHinhAnh()); // Lưu đường dẫn hình ảnh
                
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    // Lấy mã xe vừa được tạo
                    String selectSql = "SELECT MAX(MaXe) as MaXe FROM XE WHERE BienSo = ?";
                    try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                        selectStmt.setString(1, xe.getBienSo());
                        try (ResultSet rs = selectStmt.executeQuery()) {
                            if (rs.next()) {
                                String generatedMaXe = rs.getString("MaXe");
                                xe.setMaXe(generatedMaXe);
                            }
                        }
                    }
                    
                    conn.commit();
                    return true;
                }
                
                return false;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Phương thức cập nhật xe
    public boolean updateXe(Xe xe) {
        String sql = "UPDATE XE SET TenXe=?, BienSo=?, SoCho=?, HangXe=?, NamSX=?, TrangThai=?, GiaThueNgay=?, HinhAnh=? WHERE MaXe=?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, xe.getTenXe());
            pstmt.setString(2, xe.getBienSo());
            pstmt.setInt(3, xe.getSoCho());
            pstmt.setString(4, xe.getHangXe());
            pstmt.setInt(5, xe.getNamSX());
            pstmt.setString(6, xe.getTrangThai());
            pstmt.setDouble(7, xe.getGiaThueNgay());
            pstmt.setString(8, xe.getHinhAnh()); // Cập nhật đường dẫn hình ảnh
            pstmt.setString(9, xe.getMaXe());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Phương thức xóa xe
    public boolean deleteXe(String maXe) {
        String sql = "DELETE FROM XE WHERE MaXe=?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maXe);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Phương thức tìm kiếm xe
    public List<Xe> searchXe(String keyword) {
        List<Xe> danhSachXe = new ArrayList<>();
        String sql = "SELECT * FROM XE WHERE " +
                     "UPPER(MaXe) LIKE UPPER(?) OR " +
                     "UPPER(TenXe) LIKE UPPER(?) OR " +
                     "UPPER(BienSo) LIKE UPPER(?) OR " +
                     "UPPER(HangXe) LIKE UPPER(?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchKeyword = "%" + keyword + "%";
            pstmt.setString(1, searchKeyword);
            pstmt.setString(2, searchKeyword);
            pstmt.setString(3, searchKeyword);
            pstmt.setString(4, searchKeyword);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Xe xe = new Xe();
                    xe.setMaXe(rs.getString("MaXe"));
                    xe.setTenXe(rs.getString("TenXe"));
                    xe.setBienSo(rs.getString("BienSo"));
                    xe.setSoCho(rs.getInt("SoCho"));
                    xe.setHangXe(rs.getString("HangXe"));
                    xe.setNamSX(rs.getInt("NamSX"));
                    xe.setTrangThai(rs.getString("TrangThai"));
                    xe.setGiaThueNgay(rs.getDouble("GiaThueNgay"));

                    danhSachXe.add(xe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSachXe;
    }

    
    // Phương thức lọc xe theo trạng thái
    public List<Xe> getXeByTrangThai(String trangThai) {
        List<Xe> danhSachXe = new ArrayList<>();
        String sql = "SELECT * FROM XE WHERE TrangThai = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, trangThai);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Xe xe = new Xe();
                    xe.setMaXe(rs.getString("MaXe"));
                    xe.setTenXe(rs.getString("TenXe"));
                    xe.setBienSo(rs.getString("BienSo"));
                    xe.setSoCho(rs.getInt("SoCho"));
                    xe.setHangXe(rs.getString("HangXe"));
                    xe.setNamSX(rs.getInt("NamSX"));
                    xe.setTrangThai(rs.getString("TrangThai"));
                    xe.setGiaThueNgay(rs.getDouble("GiaThueNgay"));
                    
                    danhSachXe.add(xe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return danhSachXe;
    }
    
   
}