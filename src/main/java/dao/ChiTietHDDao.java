package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ChiTietHD;
import util.DatabaseUtil;
import javax.swing.JOptionPane;

public class ChiTietHDDao {
    private Connection conn;
    
    public ChiTietHDDao() {
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
    
    public List<ChiTietHD> getChiTietHDByMaHD(String maHD) {
        List<ChiTietHD> danhSachCT = new ArrayList<>();
        
        try {
            checkConnection();
            
            String sql = "SELECT c.*, x.TENXE, x.BIENSO, x.HANGXE, x.SOCHO, x.GIATHUE_NGAY " +
                         "FROM CTHD c JOIN XE x ON c.MAXE = x.MAXE " +
                         "WHERE c.MAHD = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maHD);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ChiTietHD ct = new ChiTietHD();
                ct.setMaHD(rs.getString("MAHD"));
                ct.setMaXe(rs.getString("MAXE"));
                ct.setNgayBatDau(rs.getDate("NGAYBATDAU"));
                ct.setNgayKetThuc(rs.getDate("NGAYKETTHUC"));
                ct.setTenXe(rs.getString("TENXE"));
                ct.setBienSo(rs.getString("BIENSO"));
                ct.setHangXe(rs.getString("HANGXE"));
                ct.setSoCho(rs.getInt("SOCHO"));
                ct.setGiaThueNgay(rs.getDouble("GIATHUE_NGAY"));
                
                danhSachCT.add(ct);
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return danhSachCT;
    }
    
    public boolean addChiTietHD(ChiTietHD ct) throws SQLException {
        if (ct == null || ct.getMaHD() == null || ct.getMaXe() == null || 
            ct.getNgayBatDau() == null || ct.getNgayKetThuc() == null) {
            throw new SQLException("Thông tin chi tiết hợp đồng không đầy đủ");
        }
        
        try {
            checkConnection();
            
            String sql = "INSERT INTO CTHD (MAHD, MAXE, NGAYBATDAU, NGAYKETTHUC) " +
                         "VALUES (?, ?, ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ct.getMaHD());
            pstmt.setString(2, ct.getMaXe());
            pstmt.setDate(3, new java.sql.Date(ct.getNgayBatDau().getTime()));
            pstmt.setDate(4, new java.sql.Date(ct.getNgayKetThuc().getTime()));
            
            int rows = pstmt.executeUpdate();
            pstmt.close();
            
            return rows > 0;
        } catch (SQLException e) {
            // Chuyển tiếp ngoại lệ để xử lý ở tầng DAO
            throw e;
        }
    }
    
    public boolean updateChiTietHD(ChiTietHD ct) throws SQLException {
        if (ct == null || ct.getMaHD() == null || ct.getMaXe() == null || 
            ct.getNgayBatDau() == null || ct.getNgayKetThuc() == null) {
            throw new SQLException("Thông tin chi tiết hợp đồng không đầy đủ");
        }
        
        try {
            checkConnection();
            
            String sql = "UPDATE CTHD SET NGAYBATDAU = ?, NGAYKETTHUC = ? " +
                         "WHERE MAHD = ? AND MAXE = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(ct.getNgayBatDau().getTime()));
            pstmt.setDate(2, new java.sql.Date(ct.getNgayKetThuc().getTime()));
            pstmt.setString(3, ct.getMaHD());
            pstmt.setString(4, ct.getMaXe());
            
            int rows = pstmt.executeUpdate();
            pstmt.close();
            
            return rows > 0;
        } catch (SQLException e) {
            // Chuyển tiếp ngoại lệ để xử lý ở tầng DAO
            throw e;
        }
    }
    
    public boolean deleteChiTietHD(String maHD, String maXe) throws SQLException {
        if (maHD == null || maXe == null) {
            throw new SQLException("Mã hợp đồng và mã xe không được để trống");
        }
        
        try {
            checkConnection();
            
            String sql = "DELETE FROM CTHD WHERE MAHD = ? AND MAXE = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maHD);
            pstmt.setString(2, maXe);
            
            int rows = pstmt.executeUpdate();
            pstmt.close();
            
            return rows > 0;
        } catch (SQLException e) {
            // Chuyển tiếp ngoại lệ để xử lý ở tầng DAO
            throw e;
        }
    }
    
    public boolean deleteChiTietHDByMaHD(String maHD) throws SQLException {
        if (maHD == null) {
            throw new SQLException("Mã hợp đồng không được để trống");
        }
        
        try {
            checkConnection();
            
            String sql = "DELETE FROM CTHD WHERE MAHD = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maHD);
            
            int rows = pstmt.executeUpdate();
            pstmt.close();
            
            return rows > 0;
        } catch (SQLException e) {
            // Chuyển tiếp ngoại lệ để xử lý ở tầng DAO
            throw e;
        }
    }
    
    // Phương thức kiểm tra xem xe có đang được thuê trong khoảng thời gian không
    public boolean isXeDangThueTrongThoiGian(String maXe, java.util.Date ngayBatDau, java.util.Date ngayKetThuc) {
        try {
            checkConnection();
            
            String sql = "SELECT COUNT(*) FROM CTHD c JOIN HOPDONG h ON c.MAHD = h.MAHD " +
                         "WHERE c.MAXE = ? AND h.TRANGTHAI IN ('Chờ xác nhận', 'Đang thuê') " +
                         "AND (? <= c.NGAYKETTHUC AND ? >= c.NGAYBATDAU)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maXe);
            pstmt.setDate(2, new java.sql.Date(ngayBatDau.getTime()));
            pstmt.setDate(3, new java.sql.Date(ngayKetThuc.getTime()));
            
            ResultSet rs = pstmt.executeQuery();
            boolean result = false;
            
            if (rs.next() && rs.getInt(1) > 0) {
                result = true;
            }
            
            rs.close();
            pstmt.close();
            
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Phương thức kiểm tra xe có lịch bảo dưỡng trong khoảng thời gian không
    public boolean hasMaintenanceSchedule(String maXe, java.util.Date ngayBatDau, java.util.Date ngayKetThuc) {
        try {
            checkConnection();
            
            String sql = "SELECT COUNT(*) FROM PHIEUBAODUONG " +
                         "WHERE MaXe = ? AND TRUNC(NgayBD) BETWEEN TRUNC(?) AND TRUNC(?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maXe);
            pstmt.setDate(2, new java.sql.Date(ngayBatDau.getTime()));
            pstmt.setDate(3, new java.sql.Date(ngayKetThuc.getTime()));
            
            ResultSet rs = pstmt.executeQuery();
            boolean result = false;
            
            if (rs.next() && rs.getInt(1) > 0) {
                result = true;
            }
            
            rs.close();
            pstmt.close();
            
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Phương thức kiểm tra trạng thái xe
    public String getXeTrangThai(String maXe) {
        try {
            checkConnection();
            
            String sql = "SELECT TrangThai FROM XE WHERE MaXe = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maXe);
            
            ResultSet rs = pstmt.executeQuery();
            String trangThai = null;
            
            if (rs.next()) {
                trangThai = rs.getString("TrangThai");
            }
            
            rs.close();
            pstmt.close();
            
            return trangThai;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}