package dao;

import model.LichSuCongNo;
import model.KhachHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DatabaseUtil;
public class CongNoDAO {

    
    public List<LichSuCongNo> getAllLichSuCongNo() {
        List<LichSuCongNo> list = new ArrayList<>();
        String sql = "SELECT ls.*, kh.HoTen as TenKH " +
                     "FROM LICHSUCONGNO ls " +
                     "JOIN KHACHHANG kh ON ls.MaKH = kh.MaKH " +
                     "ORDER BY ls.NgayGiaoDich DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
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
        }
        
        return list;
    }
    
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
    
    public boolean addLichSuCongNo(LichSuCongNo ls) {
        // The trigger trg_Update_ins_del_LSCN will handle updating customer debt
        // The trigger trg_kiem_tra_thanhtoan will validate payment amount
        String sql = "INSERT INTO LICHSUCONGNO (MaKH, NgayGiaoDich, LoaiGiaoDich, SoTien, GhiChu) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ls.getMaKH());
            pstmt.setDate(2, new java.sql.Date(ls.getNgayGiaoDich().getTime()));
            pstmt.setString(3, ls.getLoaiGiaoDich());
            pstmt.setDouble(4, ls.getSoTien());
            pstmt.setString(5, ls.getGhiChu());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding debt history: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateLichSuCongNo(LichSuCongNo ls) {
        // The trigger trg_Update_ins_del_LSCN will handle updating customer debt
        String sql = "UPDATE LICHSUCONGNO SET MaKH = ?, NgayGiaoDich = ?, LoaiGiaoDich = ?, " +
                     "SoTien = ?, GhiChu = ? WHERE MaLichSu = ?";
        
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
        } catch (SQLException e) {
            System.err.println("Error updating debt history: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteLichSuCongNo(String maLichSu) {
        // The trigger trg_Update_ins_del_LSCN will handle updating customer debt
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
    
    public List<KhachHang> getKhachHangCoCongNo() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KHACHHANG WHERE TongTienNo > 0 ORDER BY TongTienNo DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
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
        }
        
        return list;
    }
    
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
}