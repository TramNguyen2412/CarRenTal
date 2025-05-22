package dao;

import model.GioXe;
import model.Xe;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GioXeDAO {
    
    // Lấy danh sách xe trong giỏ của một khách hàng
    public List<GioXe> getGioXeByMaKH(String maKH) {
        List<GioXe> danhSachGioXe = new ArrayList<>();
        
        String sql = "SELECT g.*, x.TENXE, x.BIENSO, x.HANGXE, x.SOCHO, x.NAMSX, x.GIATHUENGAY, x.HINHANH " +
                     "FROM GIOHANG g " +
                     "JOIN XE x ON g.MAXE = x.MAXE " +
                     "WHERE g.MAKH = ? " +
                     "ORDER BY g.NGAYTHEM DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maKH);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                GioXe gioXe = new GioXe();
                gioXe.setMaGH(rs.getString("MAGH"));
                gioXe.setMaKH(rs.getString("MAKH"));
                gioXe.setMaXe(rs.getString("MAXE"));
                gioXe.setNgayBatDau(rs.getDate("NGAYBATDAU"));
                gioXe.setNgayKetThuc(rs.getDate("NGAYKETTHUC"));
                gioXe.setNgayThem(rs.getDate("NGAYTHEM"));
                
                // Thêm thông tin xe
                gioXe.setTenXe(rs.getString("TENXE"));
                gioXe.setBienSo(rs.getString("BIENSO"));
                gioXe.setHangXe(rs.getString("HANGXE"));
                gioXe.setSoCho(rs.getInt("SOCHO"));
                gioXe.setNamSX(rs.getInt("NAMSX"));
                gioXe.setGiaThueNgay(rs.getDouble("GIATHUENGAY"));
                gioXe.setHinhAnh(rs.getString("HINHANH"));
                
                danhSachGioXe.add(gioXe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return danhSachGioXe;
    }
    
    // Thêm xe vào giỏ hàng
    public boolean themXeVaoGio(GioXe gioXe) {
        String sql = "INSERT INTO GIOHANG (MAKH, MAXE, NGAYBATDAU, NGAYKETTHUC) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, gioXe.getMaKH());
            pstmt.setString(2, gioXe.getMaXe());
            pstmt.setDate(3, new java.sql.Date(gioXe.getNgayBatDau().getTime()));
            pstmt.setDate(4, new java.sql.Date(gioXe.getNgayKetThuc().getTime()));
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa xe khỏi giỏ hàng
    public boolean xoaXeKhoiGio(String maGH) {
        String sql = "DELETE FROM GIOHANG WHERE MAGH = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maGH);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa tất cả xe trong giỏ hàng của khách hàng
    public boolean xoaTatCaXeTrongGio(String maKH) {
        String sql = "DELETE FROM GIOHANG WHERE MAKH = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maKH);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật thông tin thuê xe trong giỏ
    public boolean capNhatGioXe(GioXe gioXe) {
        String sql = "UPDATE GIOHANG SET NGAYBATDAU = ?, NGAYKETTHUC = ? WHERE MAGH = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, new java.sql.Date(gioXe.getNgayBatDau().getTime()));
            pstmt.setDate(2, new java.sql.Date(gioXe.getNgayKetThuc().getTime()));
            pstmt.setString(3, gioXe.getMaGH());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Kiểm tra xem xe có trong giỏ hàng của khách hàng không
    public GioXe getGioXeByMaXeAndMaKH(String maXe, String maKH) {
        String sql = "SELECT g.*, x.TENXE, x.BIENSO, x.HANGXE, x.SOCHO, x.NAMSX, x.GIATHUENGAY, x.HINHANH " +
                     "FROM GIOHANG g " +
                     "JOIN XE x ON g.MAXE = x.MAXE " +
                     "WHERE g.MAXE = ? AND g.MAKH = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maXe);
            pstmt.setString(2, maKH);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                GioXe gioXe = new GioXe();
                gioXe.setMaGH(rs.getString("MAGH"));
                gioXe.setMaKH(rs.getString("MAKH"));
                gioXe.setMaXe(rs.getString("MAXE"));
                gioXe.setNgayBatDau(rs.getDate("NGAYBATDAU"));
                gioXe.setNgayKetThuc(rs.getDate("NGAYKETTHUC"));
                gioXe.setNgayThem(rs.getDate("NGAYTHEM"));
                
                // Thêm thông tin xe
                gioXe.setTenXe(rs.getString("TENXE"));
                gioXe.setBienSo(rs.getString("BIENSO"));
                gioXe.setHangXe(rs.getString("HANGXE"));
                gioXe.setSoCho(rs.getInt("SOCHO"));
                gioXe.setNamSX(rs.getInt("NAMSX"));
                gioXe.setGiaThueNgay(rs.getDouble("GIATHUENGAY"));
                gioXe.setHinhAnh(rs.getString("HINHANH"));
                
                return gioXe;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Đếm số lượng xe trong giỏ của khách hàng
    public int demSoXeTrongGio(String maKH) {
        String sql = "SELECT COUNT(*) FROM GIOHANG WHERE MAKH = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maKH);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
}