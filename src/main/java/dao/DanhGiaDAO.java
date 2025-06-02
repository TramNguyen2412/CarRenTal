
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.DanhGia;
import util.DatabaseUtil;

public class DanhGiaDAO {
    
    // Lấy danh sách đánh giá
    public List<DanhGia> getAllDanhGia() {
        List<DanhGia> danhSachDanhGia = new ArrayList<>();
        String sql = "SELECT dg.*, kh.HoTen as TenKhachHang, kh.MaKH, x.TenXe " +
                    "FROM DANHGIA dg " +
                    "JOIN HOPDONG hd ON dg.MaHD = hd.MaHD " +
                    "JOIN KHACHHANG kh ON hd.MaKH = kh.MaKH " +
                    "JOIN CTHD ct ON hd.MaHD = ct.MaHD " +
                    "JOIN XE x ON ct.MaXe = x.MaXe " +
                    "ORDER BY dg.NgayDanhGia DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                DanhGia danhGia = new DanhGia();
                danhGia.setMaDG(rs.getString("MaDG"));
                danhGia.setMaHD(rs.getString("MaHD"));
                danhGia.setDiemSo(rs.getInt("DiemSo"));
                danhGia.setBinhLuan(rs.getString("BinhLuan"));
                danhGia.setNgayDanhGia(rs.getDate("NgayDanhGia"));
                danhGia.setTenKhachHang(rs.getString("TenKhachHang"));
                danhGia.setMaKH(rs.getString("MaKH"));
                danhGia.setTenXe(rs.getString("TenXe"));
                
                danhSachDanhGia.add(danhGia);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return danhSachDanhGia;
    }
    
    // Lấy danh sách đánh giá theo mã khách hàng
    public List<DanhGia> getDanhGiaByMaKH(String maKH) {
        List<DanhGia> danhSachDanhGia = new ArrayList<>();
        String sql = "SELECT dg.*, kh.HoTen as TenKhachHang, kh.MaKH, x.TenXe " +
                    "FROM DANHGIA dg " +
                    "JOIN HOPDONG hd ON dg.MaHD = hd.MaHD " +
                    "JOIN KHACHHANG kh ON hd.MaKH = kh.MaKH " +
                    "JOIN CTHD ct ON hd.MaHD = ct.MaHD " +
                    "JOIN XE x ON ct.MaXe = x.MaXe " +
                    "WHERE kh.MaKH = ? " +
                    "ORDER BY dg.NgayDanhGia DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maKH);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DanhGia danhGia = new DanhGia();
                    danhGia.setMaDG(rs.getString("MaDG"));
                    danhGia.setMaHD(rs.getString("MaHD"));
                    danhGia.setDiemSo(rs.getInt("DiemSo"));
                    danhGia.setBinhLuan(rs.getString("BinhLuan"));
                    danhGia.setNgayDanhGia(rs.getDate("NgayDanhGia"));
                    danhGia.setTenKhachHang(rs.getString("TenKhachHang"));
                    danhGia.setMaKH(rs.getString("MaKH"));
                    danhGia.setTenXe(rs.getString("TenXe"));
                    
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
        DanhGia danhGia = null;
        String sql = "SELECT dg.*, kh.HoTen as TenKhachHang, kh.MaKH, x.TenXe " +
                    "FROM DANHGIA dg " +
                    "JOIN HOPDONG hd ON dg.MaHD = hd.MaHD " +
                    "JOIN KHACHHANG kh ON hd.MaKH = kh.MaKH " +
                    "JOIN CTHD ct ON hd.MaHD = ct.MaHD " +
                    "JOIN XE x ON ct.MaXe = x.MaXe " +
                    "WHERE dg.MaDG = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maDG);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    danhGia = new DanhGia();
                    danhGia.setMaDG(rs.getString("MaDG"));
                    danhGia.setMaHD(rs.getString("MaHD"));
                    danhGia.setDiemSo(rs.getInt("DiemSo"));
                    danhGia.setBinhLuan(rs.getString("BinhLuan"));
                    danhGia.setNgayDanhGia(rs.getDate("NgayDanhGia"));
                    danhGia.setTenKhachHang(rs.getString("TenKhachHang"));
                    danhGia.setMaKH(rs.getString("MaKH"));
                    danhGia.setTenXe(rs.getString("TenXe"));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return danhGia;
    }
    
    // Thêm đánh giá mới
    public boolean addDanhGia(DanhGia danhGia) {
        String sql = "INSERT INTO DANHGIA (MaDG, MaHD, DiemSo, BinhLuan, NgayDanhGia) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, danhGia.getMaDG());
            ps.setString(2, danhGia.getMaHD());
            ps.setInt(3, danhGia.getDiemSo());
            ps.setString(4, danhGia.getBinhLuan());
            ps.setDate(5, new java.sql.Date(danhGia.getNgayDanhGia().getTime()));
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật đánh giá
    public boolean updateDanhGia(DanhGia danhGia) {
        String sql = "UPDATE DANHGIA SET DiemSo = ?, BinhLuan = ? WHERE MaDG = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, danhGia.getDiemSo());
            ps.setString(2, danhGia.getBinhLuan());
            ps.setString(3, danhGia.getMaDG());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa đánh giá
    public boolean deleteDanhGia(String maDG) {
        String sql = "DELETE FROM DANHGIA WHERE MaDG = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maDG);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Tạo mã đánh giá mới
    public String generateMaDG() {
        String maDG = "DG";
        String sql = "SELECT MaDG FROM DANHGIA ORDER BY MaDG DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String lastMaDG = rs.getString("MaDG");
                int number = Integer.parseInt(lastMaDG.substring(2)) + 1;
                maDG += String.format("%03d", number);
            } else {
                maDG += "001";
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            maDG += "001";
        }
        
        return maDG;
    }
    
    // Lấy danh sách hợp đồng của khách hàng chưa có đánh giá
    public List<String> getHopDongChuaDanhGia(String maKH) {
        List<String> danhSachMaHD = new ArrayList<>();
        String sql = "SELECT hd.MaHD FROM HOPDONG hd " +
                    "WHERE hd.MaKH = ? AND hd.TrangThai = 'Hoàn thành' " +
                    "AND NOT EXISTS (SELECT 1 FROM DANHGIA dg WHERE dg.MaHD = hd.MaHD)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maKH);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSachMaHD.add(rs.getString("MaHD"));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return danhSachMaHD;
    }
    
   
    
    // Lấy MaKH từ MaHD
    public String getMaKHFromMaHD(String maHD) {
        String maKH = null;
        String sql = "SELECT MaKH FROM HOPDONG WHERE MaHD = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHD);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    maKH = rs.getString("MaKH");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return maKH;
    }
    
    // Thêm phương thức mới
    public List<String> getXesByMaHD(String maHD) {
    List<String> danhSachXe = new ArrayList<>();
    String sql = "SELECT x.TenXe FROM XE x " +
                "JOIN CTHD ct ON x.MaXe = ct.MaXe " +
                "WHERE ct.MaHD = ? " +
                "ORDER BY x.TenXe";
    
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, maHD);
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                danhSachXe.add(rs.getString("TenXe"));
            }
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return danhSachXe;
}

    // Cập nhật phương thức getThongTinHopDong để bao gồm tất cả các xe
    public String getThongTinHopDong(String maHD) {
        StringBuilder thongTin = new StringBuilder(maHD);
        List<String> tenXeList = getXesByMaHD(maHD);

        if (!tenXeList.isEmpty()) {
            thongTin.append(" - ").append(String.join(", ", tenXeList));
        }

        return thongTin.toString();
    }
}