package dao;

import model.DanhGia;
import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DanhGiaDAO {
    
    // Phương thức lấy tất cả đánh giá từ database
    public List<DanhGia> getAllDanhGia() {
        List<DanhGia> danhSachDanhGia = new ArrayList<>();
        String sql = "SELECT * FROM DANHGIA";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                DanhGia danhGia = new DanhGia();
                danhGia.setMaDG(rs.getString("MaDG"));
                danhGia.setMaKH(rs.getString("MaKH"));
                danhGia.setMaHD(rs.getString("MaHD"));
                danhGia.setDiemSo(rs.getInt("DiemSo"));
                danhGia.setBinhLuan(rs.getString("BinhLuan"));
                danhGia.setNgayDanhGia(rs.getDate("NgayDanhGia"));
                
                danhSachDanhGia.add(danhGia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return danhSachDanhGia;
    }
    
    // Lấy danh sách đánh giá của khách hàng
    public List<DanhGia> getDanhGiaByMaKH(String maKH) {
        List<DanhGia> danhSachDanhGia = new ArrayList<>();
        String sql = "SELECT dg.*, hd.NgayBatDau, hd.NgayKetThuc, kh.HoTen, " +
                    "(SELECT x.TenXe FROM XE x, CHITIETHD ct WHERE ct.MaHD = hd.MaHD AND ct.MaXe = x.MaXe AND ROWNUM = 1) AS TenXe " +
                    "FROM DANHGIA dg " +
                    "JOIN HOPDONG hd ON dg.MaHD = hd.MaHD " +
                    "JOIN KHACHHANG kh ON dg.MaKH = kh.MaKH " +
                    "WHERE dg.MaKH = ? " +
                    "ORDER BY dg.NgayDanhGia DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maKH);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DanhGia danhGia = new DanhGia();
                    danhGia.setMaDG(rs.getString("MaDG"));
                    danhGia.setMaKH(rs.getString("MaKH"));
                    danhGia.setMaHD(rs.getString("MaHD"));
                    danhGia.setDiemSo(rs.getInt("DiemSo"));
                    danhGia.setBinhLuan(rs.getString("BinhLuan"));
                    danhGia.setNgayDanhGia(rs.getDate("NgayDanhGia"));
                    danhGia.setTenKhachHang(rs.getString("HoTen"));
                    danhGia.setTenXe(rs.getString("TenXe"));
                    danhGia.setNgayBatDau(rs.getDate("NgayBatDau"));
                    danhGia.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                    
                    danhSachDanhGia.add(danhGia);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return danhSachDanhGia;
    }
    
    // Lấy đánh giá theo mã đánh giá
    public DanhGia getDanhGiaByMaDG(String maDG) {
        String sql = "SELECT * FROM DANHGIA WHERE MaDG = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maDG);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    DanhGia danhGia = new DanhGia();
                    danhGia.setMaDG(rs.getString("MaDG"));
                    danhGia.setMaKH(rs.getString("MaKH"));
                    danhGia.setMaHD(rs.getString("MaHD"));
                    danhGia.setDiemSo(rs.getInt("DiemSo"));
                    danhGia.setBinhLuan(rs.getString("BinhLuan"));
                    danhGia.setNgayDanhGia(rs.getDate("NgayDanhGia"));
                    
                    return danhGia;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Lấy đánh giá theo mã hợp đồng
    public DanhGia getDanhGiaByMaHD(String maHD) {
        String sql = "SELECT * FROM DANHGIA WHERE MaHD = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maHD);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    DanhGia danhGia = new DanhGia();
                    danhGia.setMaDG(rs.getString("MaDG"));
                    danhGia.setMaKH(rs.getString("MaKH"));
                    danhGia.setMaHD(rs.getString("MaHD"));
                    danhGia.setDiemSo(rs.getInt("DiemSo"));
                    danhGia.setBinhLuan(rs.getString("BinhLuan"));
                    danhGia.setNgayDanhGia(rs.getDate("NgayDanhGia"));
                    
                    return danhGia;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Lấy danh sách hợp đồng đã hoàn thành chưa được đánh giá
    public List<Map<String, Object>> getHopDongChuaDanhGia(String maKH) {
    List<Map<String, Object>> danhSachHopDong = new ArrayList<>();
    String sql = "SELECT hd.MaHD, hd.NgayLap, hd.TongTien, " +
                "MIN(ct.NgayBatDau) as NgayBatDau, " +
                "MAX(ct.NgayKetThuc) as NgayKetThuc, " +
                "(SELECT x.TenXe FROM XE x, CTHD ct2 WHERE ct2.MaHD = hd.MaHD AND ct2.MaXe = x.MaXe AND ROWNUM = 1) AS TenXe, " +
                "(SELECT COUNT(*) FROM CTHD ct3 WHERE ct3.MaHD = hd.MaHD) AS SoLuongXe " +
                "FROM HOPDONG hd " +
                "JOIN CTHD ct ON hd.MaHD = ct.MaHD " +
                "WHERE hd.MaKH = ? AND hd.TrangThai = 'Hoàn thành' " +
                "AND NOT EXISTS (SELECT 1 FROM DANHGIA dg WHERE dg.MaHD = hd.MaHD) " +
                "GROUP BY hd.MaHD, hd.NgayLap, hd.TongTien " +
                "ORDER BY hd.NgayLap DESC";
    
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, maKH);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> hopDong = new HashMap<>();
                hopDong.put("MaHD", rs.getString("MaHD"));
                hopDong.put("NgayLap", rs.getDate("NgayLap"));
                hopDong.put("TongTien", rs.getDouble("TongTien"));
                hopDong.put("NgayBatDau", rs.getDate("NgayBatDau"));
                hopDong.put("NgayKetThuc", rs.getDate("NgayKetThuc"));
                hopDong.put("TenXe", rs.getString("TenXe"));
                hopDong.put("SoLuongXe", rs.getInt("SoLuongXe"));
                
                danhSachHopDong.add(hopDong);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return danhSachHopDong;
}

    // Lấy danh sách hợp đồng đã được đánh giá
    public List<Map<String, Object>> getHopDongDaDanhGia(String maKH) {
        List<Map<String, Object>> danhSachHopDong = new ArrayList<>();
        String sql = "SELECT hd.MaHD, hd.NgayLap, hd.TongTien, " +
                "MIN(ct.NgayBatDau) as NgayBatDau, " +
                "MAX(ct.NgayKetThuc) as NgayKetThuc, " +
                "dg.MaDG, dg.DiemSo, dg.BinhLuan, dg.NgayDanhGia, " +
                "(SELECT x.TenXe FROM XE x, CTHD ct2 WHERE ct2.MaHD = hd.MaHD AND ct2.MaXe = x.MaXe AND ROWNUM = 1) AS TenXe, " +
                "(SELECT COUNT(*) FROM CTHD ct3 WHERE ct3.MaHD = hd.MaHD) AS SoLuongXe " +
                "FROM HOPDONG hd " +
                "JOIN CTHD ct ON hd.MaHD = ct.MaHD " +
                "JOIN DANHGIA dg ON hd.MaHD = dg.MaHD " +
                "WHERE dg.MaKH = ? " +  // Thay đổi ở đây: hd.MaKH -> dg.MaKH
                "GROUP BY hd.MaHD, hd.NgayLap, hd.TongTien, dg.MaDG, dg.DiemSo, dg.BinhLuan, dg.NgayDanhGia " +
                "ORDER BY dg.NgayDanhGia DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maKH);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> hopDong = new HashMap<>();
                    hopDong.put("MaHD", rs.getString("MaHD"));
                    hopDong.put("NgayLap", rs.getDate("NgayLap"));
                    hopDong.put("TongTien", rs.getDouble("TongTien"));
                    hopDong.put("NgayBatDau", rs.getDate("NgayBatDau"));
                    hopDong.put("NgayKetThuc", rs.getDate("NgayKetThuc"));
                    hopDong.put("TenXe", rs.getString("TenXe"));
                    hopDong.put("SoLuongXe", rs.getInt("SoLuongXe"));
                    hopDong.put("MaDG", rs.getString("MaDG"));
                    hopDong.put("DiemSo", rs.getInt("DiemSo"));
                    hopDong.put("BinhLuan", rs.getString("BinhLuan"));
                    hopDong.put("NgayDanhGia", rs.getDate("NgayDanhGia"));

                    danhSachHopDong.add(hopDong);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSachHopDong;
    }

    
    // Phương thức thêm đánh giá mới
    public boolean themDanhGia(DanhGia danhGia) {
        String sql = "INSERT INTO DANHGIA (MaDG, MaKH, MaHD, DiemSo, BinhLuan, NgayDanhGia) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            // Bắt đầu giao dịch
            conn.setAutoCommit(false);
            
            try {
                // Tạo mã đánh giá nếu chưa có
                if (danhGia.getMaDG() == null || danhGia.getMaDG().isEmpty()) {
                    danhGia.setMaDG(generateMaDG(conn));
                }
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, danhGia.getMaDG());
                    pstmt.setString(2, danhGia.getMaKH());
                    pstmt.setString(3, danhGia.getMaHD());
                    pstmt.setInt(4, danhGia.getDiemSo());
                    pstmt.setString(5, danhGia.getBinhLuan());
                    pstmt.setDate(6, new java.sql.Date(danhGia.getNgayDanhGia().getTime()));
                    
                    int rowsAffected = pstmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        conn.commit();
                        return true;
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
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
    
    // Phương thức cập nhật đánh giá
    public boolean capNhatDanhGia(DanhGia danhGia) {
        String sql = "UPDATE DANHGIA SET DiemSo=?, BinhLuan=?, NgayDanhGia=? WHERE MaDG=?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, danhGia.getDiemSo());
            pstmt.setString(2, danhGia.getBinhLuan());
            pstmt.setDate(3, new java.sql.Date(danhGia.getNgayDanhGia().getTime()));
            pstmt.setString(4, danhGia.getMaDG());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Phương thức xóa đánh giá
    public boolean xoaDanhGia(String maDG) {
        String sql = "DELETE FROM DANHGIA WHERE MaDG=?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maDG);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Kiểm tra hợp đồng đã được đánh giá chưa
    public boolean kiemTraHopDongDaDanhGia(String maHD) {
        String sql = "SELECT COUNT(*) FROM DANHGIA WHERE MaHD=?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maHD);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Tạo mã đánh giá tự động
    private String generateMaDG(Connection conn) throws SQLException {
        String sql = "SELECT MAX(MaDG) FROM DANHGIA";
        String maDG = "DG001";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                String currentMaxId = rs.getString(1);
                if (currentMaxId != null) {
                    int idNumber = Integer.parseInt(currentMaxId.substring(2)) + 1;
                    maDG = "DG" + String.format("%03d", idNumber);
                }
            }
        }
        
        return maDG;
    }
}