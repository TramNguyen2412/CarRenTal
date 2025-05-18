package dao;

import java.sql.*;
import java.util.*;
import util.DatabaseUtil;
public class ThongKeDAO {

    
    // Thống kê doanh thu theo tháng trong năm sử dụng stored procedure
    public Map<Integer, Double> getDoanhThuTheoThang(int nam) {
        Map<Integer, Double> result = new TreeMap<>();
        
        // Initialize all months with 0
        for (int i = 1; i <= 12; i++) {
            result.put(i, 0.0);
        }
        
        try(Connection conn = DatabaseUtil.getConnection(); // Call the stored procedure
                CallableStatement cstmt = conn.prepareCall("{call sp_BaoCaoDoanhThuNam(?)}");) {
            cstmt.setInt(1, nam);
            cstmt.execute();
            
            // Since the procedure outputs to DBMS_OUTPUT, we'll still need to query the data
            String sql = "SELECT EXTRACT(MONTH FROM NgayLap) as Thang, SUM(TongTien) as TongDoanhThu " +
                         "FROM HOPDONG " +
                         "WHERE EXTRACT(YEAR FROM NgayLap) = ? " +
                         "AND TrangThai = 'Hoàn thành' " +
                         "GROUP BY EXTRACT(MONTH FROM NgayLap) " +
                         "ORDER BY Thang";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, nam);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int thang = rs.getInt("Thang");
                        double doanhThu = rs.getDouble("TongDoanhThu");
                        result.put(thang, doanhThu);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving monthly revenue: " + e.getMessage());
        }
        
        return result;
    }
    
    // Thống kê doanh thu theo khách hàng sử dụng stored procedure
    public List<Map<String, Object>> getDoanhThuTheoKhachHang(int nam) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try(Connection conn = DatabaseUtil.getConnection(); // Call the stored procedure
                CallableStatement cstmt = conn.prepareCall("{call sp_BaoCaoDoanhThuKhachHang(?)}");) {
            cstmt.setInt(1, nam);
            cstmt.execute();
            
            // Since the procedure outputs to DBMS_OUTPUT, we'll still need to query the data
            String sql = "SELECT kh.MaKH, kh.HoTen, COUNT(hd.MaHD) as SoHopDong, SUM(hd.TongTien) as TongDoanhThu " +
                         "FROM KHACHHANG kh " +
                         "JOIN HOPDONG hd ON kh.MaKH = hd.MaKH " +
                         "WHERE EXTRACT(YEAR FROM hd.NgayLap) = ? " +
                         "AND hd.TrangThai = 'Hoàn thành' " +
                         "GROUP BY kh.MaKH, kh.HoTen " +
                         "ORDER BY TongDoanhThu DESC";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, nam);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("MaKH", rs.getString("MaKH"));
                        row.put("HoTen", rs.getString("HoTen"));
                        row.put("SoHopDong", rs.getInt("SoHopDong"));
                        row.put("TongDoanhThu", rs.getDouble("TongDoanhThu"));
                        result.add(row);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving customer revenue: " + e.getMessage());
        }
        
        return result;
    }
    
    // Kiểm tra xem xe có đang bảo dưỡng không
    public boolean isCarInMaintenance(String maXe) {
        boolean result = false;
        
        try(Connection conn = DatabaseUtil.getConnection();) {
            // Call the function
            String sql = "SELECT IS_CAR_IN_MAINTENANCE(?) FROM DUAL";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maXe);
                
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    // Oracle returns 1 for TRUE and 0 for FALSE
                    result = rs.getInt(1) == 1;
                }
                
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("Error checking if car is in maintenance: " + e.getMessage());
        }
        
        return result;
    }
    
    // Thống kê doanh thu từ bảo dưỡng
    public double getDoanhThuBaoDuong(int nam) {
        String sql = "SELECT SUM(TongTienBD) as TongDoanhThu " +
                     "FROM PHIEUBAODUONG " +
                     "WHERE EXTRACT(YEAR FROM NgayBD) = ? " +
                     "AND LoaiBD = 'Khách gây hư hại'";
        
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, nam);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("TongDoanhThu");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving maintenance revenue: " + e.getMessage());
        }
        
        return 0;
    }
    
    // Thống kê chi phí bảo dưỡng định kỳ
    public double getChiPhiBaoDuongDinhKy(int nam) {
        String sql = "SELECT SUM(TongTienBD) as TongChiPhi " +
                     "FROM PHIEUBAODUONG " +
                     "WHERE EXTRACT(YEAR FROM NgayBD) = ? " +
                     "AND LoaiBD = 'Định Kỳ'";
        
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, nam);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("TongChiPhi");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving maintenance costs: " + e.getMessage());
        }
        
        return 0;
    }
    
    // Thống kê xe được thuê nhiều nhất
    public List<Map<String, Object>> getXeDuocThueNhieuNhat(int nam) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        String sql = "SELECT x.MaXe, x.TenXe, x.BienSo, COUNT(ct.MaXe) as SoLanThue " +
                     "FROM XE x " +
                     "JOIN CTHD ct ON x.MaXe = ct.MaXe " +
                     "JOIN HOPDONG hd ON ct.MaHD = hd.MaHD " +
                     "WHERE EXTRACT(YEAR FROM hd.NgayLap) = ? " +
                     "GROUP BY x.MaXe, x.TenXe, x.BienSo " +
                     "ORDER BY SoLanThue DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, nam);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("MaXe", rs.getString("MaXe"));
                    row.put("TenXe", rs.getString("TenXe"));
                    row.put("BienSo", rs.getString("BienSo"));
                    row.put("SoLanThue", rs.getInt("SoLanThue"));
                    result.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving most rented cars: " + e.getMessage());
        }
        
        return result;
    }
    
    // Thống kê tổng công nợ
    public double getTongCongNo() {
        String sql = "SELECT SUM(TongTienNo) as TongCongNo FROM KHACHHANG";
        
        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("TongCongNo");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving total debt: " + e.getMessage());
        }
        
        return 0;
    }
}