package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ChiTietHD;
import util.DatabaseUtil;
import javax.swing.JOptionPane;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ChiTietHDDao {

   //  Thay thế phương thức checkConnection bằng getValidConnection
//    private Connection getValidConnection() throws SQLException {
//        Connection conn = DatabaseUtil.getConnection();
//        
//        // Kiểm tra kết nối còn hợp lệ không
//        if (!conn.isValid(2)) { // timeout 2 giây
//            System.out.println("Connection invalidated, reconnecting...");
//            DatabaseUtil.reconnect();
//            conn = DatabaseUtil.getConnection();
//        }
//        
//        return conn;
//    }
    private Connection getValidConnection() throws SQLException {
    Connection conn = DatabaseUtil.getConnection();
    
    // Kiểm tra kết nối còn hợp lệ không
    if (!conn.isValid(2)) { // timeout 2 giây
        System.out.println("Connection invalidated, reconnecting...");
        conn = DatabaseUtil.reconnect(conn); // Truyền conn hiện tại và nhận conn mới
    }
    
    return conn;
  }
    
    public List<ChiTietHD> getChiTietHDByMaHD(String maHD) {
        List<ChiTietHD> danhSachCT = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT c.*, x.TENXE, x.BIENSO, x.HANGXE, x.SOCHO, x.GIATHUENGAY " +
                         "FROM CTHD c JOIN XE x ON c.MAXE = x.MAXE " +
                         "WHERE c.MAHD = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maHD);
            rs = pstmt.executeQuery();
            
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
                ct.setGiaThueNgay(rs.getDouble("GIATHUENGAY"));
                
                danhSachCT.add(ct);
            }
        } catch (SQLException e) {
            System.err.println("Error in getChiTietHDByMaHD: " + e.getMessage());
            e.printStackTrace();
            
//            // Thử kết nối lại nếu bị lỗi kết nối đóng
//            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in getChiTietHDByMaHD");
//                    DatabaseUtil.reconnect();
//                    return getChiTietHDByMaHD(maHD); // Gọi lại phương thức
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
//            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                // KHÔNG đóng connection ở đây
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return danhSachCT;
    }
    
    public boolean addChiTietHD(ChiTietHD ct) throws SQLException {
        if (ct == null || ct.getMaHD() == null || ct.getMaXe() == null || 
            ct.getNgayBatDau() == null || ct.getNgayKetThuc() == null) {
            throw new SQLException("Thông tin chi tiết hợp đồng không đầy đủ");
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "INSERT INTO CTHD (MAHD, MAXE, NGAYBATDAU, NGAYKETTHUC) " +
                         "VALUES (?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ct.getMaHD());
            pstmt.setString(2, ct.getMaXe());
            pstmt.setDate(3, new java.sql.Date(ct.getNgayBatDau().getTime()));
            pstmt.setDate(4, new java.sql.Date(ct.getNgayKetThuc().getTime()));
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error in addChiTietHD: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
//            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in addChiTietHD");
//                    DatabaseUtil.reconnect();
//                    return addChiTietHD(ct); // Gọi lại phương thức
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
//            }
            // Chuyển tiếp ngoại lệ để xử lý ở tầng trên
            throw e;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                // KHÔNG đóng connection ở đây
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    public boolean updateChiTietHD(ChiTietHD ct) throws SQLException {
        if (ct == null || ct.getMaHD() == null || ct.getMaXe() == null || 
            ct.getNgayBatDau() == null || ct.getNgayKetThuc() == null) {
            throw new SQLException("Thông tin chi tiết hợp đồng không đầy đủ");
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "UPDATE CTHD SET NGAYBATDAU = ?, NGAYKETTHUC = ? " +
                         "WHERE MAHD = ? AND MAXE = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(ct.getNgayBatDau().getTime()));
            pstmt.setDate(2, new java.sql.Date(ct.getNgayKetThuc().getTime()));
            pstmt.setString(3, ct.getMaHD());
            pstmt.setString(4, ct.getMaXe());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error in updateChiTietHD: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
//            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in updateChiTietHD");
//                    DatabaseUtil.reconnect();
//                    return updateChiTietHD(ct); // Gọi lại phương thức
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
//            }
            // Chuyển tiếp ngoại lệ để xử lý ở tầng trên
            throw e;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                // KHÔNG đóng connection ở đây
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    public boolean deleteChiTietHD(String maHD, String maXe) throws SQLException {
        if (maHD == null || maXe == null) {
            throw new SQLException("Mã hợp đồng và mã xe không được để trống");
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "DELETE FROM CTHD WHERE MAHD = ? AND MAXE = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maHD);
            pstmt.setString(2, maXe);
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error in deleteChiTietHD: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
//            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in deleteChiTietHD");
//                    DatabaseUtil.reconnect();
//                    return deleteChiTietHD(maHD, maXe); // Gọi lại phương thức
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
//            }
            // Chuyển tiếp ngoại lệ để xử lý ở tầng trên
            throw e;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                // KHÔNG đóng connection ở đây
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    public boolean deleteChiTietHDByMaHD(String maHD) throws SQLException {
        if (maHD == null) {
            throw new SQLException("Mã hợp đồng không được để trống");
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "DELETE FROM CTHD WHERE MAHD = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maHD);
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error in deleteChiTietHDByMaHD: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
//            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in deleteChiTietHDByMaHD");
//                    DatabaseUtil.reconnect();
//                    return deleteChiTietHDByMaHD(maHD); // Gọi lại phương thức
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
//            }
            // Chuyển tiếp ngoại lệ để xử lý ở tầng trên
            throw e;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                // KHÔNG đóng connection ở đây
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    // Phương thức kiểm tra xem xe có đang được thuê trong khoảng thời gian không
    public boolean isXeDangThueTrongThoiGian(String maXe, java.util.Date ngayBatDau, java.util.Date ngayKetThuc) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT COUNT(*) FROM CTHD c JOIN HOPDONG h ON c.MAHD = h.MAHD " +
                         "WHERE c.MAXE = ? AND h.TRANGTHAI IN ('Chờ xác nhận', 'Đang thuê', 'Đã xác nhận') " +
                         "AND (TRUNC(?) <= TRUNC(c.NGAYKETTHUC) AND TRUNC(?) >= TRUNC(c.NGAYBATDAU))";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maXe);
            pstmt.setDate(2, new java.sql.Date(ngayBatDau.getTime()));
            pstmt.setDate(3, new java.sql.Date(ngayKetThuc.getTime()));
            
            rs = pstmt.executeQuery();
            boolean result = false;
            
            if (rs.next() && rs.getInt(1) > 0) {
                result = true;
            }
            return result;
        } catch (SQLException e) {
            System.err.println("Error in isXeDangThueTrongThoiGian: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
//            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in isXeDangThueTrongThoiGian");
//                    DatabaseUtil.reconnect();
//                    return isXeDangThueTrongThoiGian(maXe, ngayBatDau, ngayKetThuc); // Gọi lại phương thức
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
//            }
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                // KHÔNG đóng connection ở đây
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    // Phương thức kiểm tra xe có lịch bảo dưỡng trong khoảng thời gian không
    public boolean hasMaintenanceSchedule(String maXe, java.util.Date ngayBatDau, java.util.Date ngayKetThuc) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT COUNT(*) FROM PHIEUBAODUONG " +
                         "WHERE MaXe = ? AND TRUNC(NgayBD) BETWEEN TRUNC(?) AND TRUNC(?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maXe);
            pstmt.setDate(2, new java.sql.Date(ngayBatDau.getTime()));
            pstmt.setDate(3, new java.sql.Date(ngayKetThuc.getTime()));
            
            rs = pstmt.executeQuery();
            boolean result = false;
            
            if (rs.next() && rs.getInt(1) > 0) {
                result = true;
            }
            return result;
        } catch (SQLException e) {
            System.err.println("Error in hasMaintenanceSchedule: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
//            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in hasMaintenanceSchedule");
//                    DatabaseUtil.reconnect();
//                    return hasMaintenanceSchedule(maXe, ngayBatDau, ngayKetThuc); // Gọi lại phương thức
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
//            }
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                // KHÔNG đóng connection ở đây
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    public String kiemTraXeThueDuoc(String maXe, Date ngayBatDau, Date ngayKetThuc, String maHDHienTai) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();

            // 1. Kiểm tra xe đang được thuê bởi hợp đồng khác
            String sql = "SELECT COUNT(*) FROM CTHD c JOIN HOPDONG h ON c.MAHD = h.MAHD " +
                         "WHERE c.MAXE = ? AND h.TRANGTHAI IN ('Chờ xác nhận', 'Đang thuê', 'Đã xác nhận') " +
                         "AND (TRUNC(?) <= TRUNC(c.NGAYKETTHUC) AND TRUNC(?) >= TRUNC(c.NGAYBATDAU))";

            // Loại trừ hợp đồng hiện tại (nếu đang chỉnh sửa)
            if (maHDHienTai != null && !maHDHienTai.isEmpty()) {
                sql += " AND c.MAHD <> ?";
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maXe);
            pstmt.setDate(2, new java.sql.Date(ngayBatDau.getTime()));
            pstmt.setDate(3, new java.sql.Date(ngayKetThuc.getTime()));

            if (maHDHienTai != null && !maHDHienTai.isEmpty()) {
                pstmt.setString(4, maHDHienTai);
            }

            rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return "Xe đã được thuê trong khoảng thời gian này";
            }

            // 2. Kiểm tra lịch bảo dưỡng
            rs.close();
            pstmt.close();

            sql = "SELECT COUNT(*) FROM PHIEUBAODUONG " +
                  "WHERE MaXe = ? AND TRUNC(NgayBD) BETWEEN TRUNC(?) AND TRUNC(?)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maXe);
            pstmt.setDate(2, new java.sql.Date(ngayBatDau.getTime()));
            pstmt.setDate(3, new java.sql.Date(ngayKetThuc.getTime()));

            rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return "Xe có lịch bảo dưỡng trong khoảng thời gian này";
            }

            // 3. Kiểm tra trạng thái xe nếu thuê ngay
            Date today = new Date();
            // Chuyển sang định dạng chuẩn chỉ có ngày để so sánh
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String todayStr = sdf.format(today);
            String ngayBDStr = sdf.format(ngayBatDau);

            if (todayStr.equals(ngayBDStr)) { // Nếu thuê ngày hôm nay
                rs.close();
                pstmt.close();

                // Chỉ kiểm tra trạng thái xe nếu đây là hợp đồng mới hoặc xe mới được thêm vào hợp đồng
                // Nếu đang sửa thông tin xe đã có trong hợp đồng thì bỏ qua kiểm tra trạng thái
                if (maHDHienTai != null && !maHDHienTai.isEmpty()) {
                    sql = "SELECT COUNT(*) FROM CTHD WHERE MAHD = ? AND MAXE = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, maHDHienTai);
                    pstmt.setString(2, maXe);

                    rs = pstmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        // Xe này đã thuộc hợp đồng hiện tại, bỏ qua kiểm tra trạng thái
                        return null;
                    }
                }

                // Xe không thuộc hợp đồng hiện tại hoặc là hợp đồng mới, kiểm tra trạng thái
                sql = "SELECT TrangThai FROM XE WHERE MaXe = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, maXe);

                rs = pstmt.executeQuery();
                if (rs.next() && !"Sẵn sàng".equals(rs.getString("TrangThai"))) {
                    return "Xe không ở trạng thái 'Sẵn sàng' nên không thể thuê ngay";
                }
            }


            // Nếu không có lỗi
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return "Lỗi kiểm tra: " + e.getMessage();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                // KHÔNG đóng connection ở đây
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Phương thức kiểm tra trạng thái xe
    public String getXeTrangThai(String maXe) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT TrangThai FROM XE WHERE MaXe = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maXe);
            
            rs = pstmt.executeQuery();
            String trangThai = null;
            
            if (rs.next()) {
                trangThai = rs.getString("TrangThai");
            }
            return trangThai;
        } catch (SQLException e) {
            System.err.println("Error in getXeTrangThai: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
//            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in getXeTrangThai");
//                    DatabaseUtil.reconnect();
//                    return getXeTrangThai(maXe); // Gọi lại phương thức
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
//            }
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                // KHÔNG đóng connection ở đây
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}