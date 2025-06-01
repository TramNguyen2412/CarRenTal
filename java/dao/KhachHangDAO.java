package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.KhachHang;
import util.DatabaseUtil;

public class KhachHangDAO {

    // Phương thức kiểm tra kết nối và khôi phục nếu cần
    private Connection getValidConnection() throws SQLException {
        Connection conn = DatabaseUtil.getConnection();

        // Kiểm tra kết nối còn hợp lệ không
        if (!conn.isValid(2)) { // timeout 2 giây
            System.out.println("Connection invalidated, reconnecting...");
            DatabaseUtil.reconnect();
            conn = DatabaseUtil.getConnection();
        }

        return conn;
    }

    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> danhSachKH = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();

            String sql = "SELECT * FROM KHACHHANG ORDER BY MAKH";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

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
        } catch (SQLException e) {
            System.err.println("Error in getAllKhachHang: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getAllKhachHang");
                    DatabaseUtil.reconnect();
                    return getAllKhachHang(); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            // Đóng các tài nguyên nhưng KHÔNG đóng kết nối
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return danhSachKH;
    }

    public KhachHang getKhachHangByMa(String maKH) {
        KhachHang kh = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();

            String sql = "SELECT * FROM KHACHHANG WHERE MAKH = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maKH);
            rs = pstmt.executeQuery();

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
        } catch (SQLException e) {
            System.err.println("Error in getKhachHangByMa: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getKhachHangByMa");
                    DatabaseUtil.reconnect();
                    return getKhachHangByMa(maKH); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return kh;
    }

    public String addKhachHang(KhachHang kh) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String newMaKH = null;

        try {
            conn = getValidConnection();
            conn.setAutoCommit(false);

            // INSERT bình thường, cho trigger tạo mã
            String sql = "INSERT INTO KHACHHANG (MATK, TONGTIENNO, HOTEN, SDT, EMAIL, CCCD, DIACHI) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, kh.getMaTK());
            pstmt.setDouble(2, kh.getTongTienNo());
            pstmt.setString(3, kh.getHoTen());
            pstmt.setString(4, kh.getSdt());
            pstmt.setString(5, kh.getEmail());
            pstmt.setString(6, kh.getCccd());
            pstmt.setString(7, kh.getDiaChi());

            int rows = pstmt.executeUpdate();
            pstmt.close();

            if (rows > 0) {
                // Lấy mã khách hàng mới nhất dựa trên SĐT và họ tên
                // Đây là cách an toàn nhất để lấy đúng khách hàng vừa insert
                String getIdSql = "SELECT MAKH FROM KHACHHANG " +
                        "WHERE SDT = ? AND HOTEN = ? " +
                        "ORDER BY MAKH DESC FETCH FIRST 1 ROW ONLY";

                pstmt = conn.prepareStatement(getIdSql);
                pstmt.setString(1, kh.getSdt());
                pstmt.setString(2, kh.getHoTen());

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    newMaKH = rs.getString("MAKH");
                } else {
                    // Nếu không tìm thấy mã, rollback và trả về null
                    conn.rollback();
                    return null;
                }
            } else {
                // Nếu không thêm được, rollback và trả về null
                conn.rollback();
                return null;
            }

            // Commit transaction
            conn.commit();
            return newMaKH;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error during rollback: " + ex.getMessage());
                ex.printStackTrace();
            }

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in addKhachHang");
                    DatabaseUtil.reconnect();
                    return addKhachHang(kh); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }

            System.err.println("Error in addKhachHang: " + e.getMessage());
            e.printStackTrace();
            return null;

        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.setAutoCommit(true); // Khôi phục autocommit
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public boolean updateKhachHang(KhachHang kh) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getValidConnection();

            String sql = "UPDATE KHACHHANG SET MATK = ?, TONGTIENNO = ?, HOTEN = ?, SDT = ?, " +
                    "EMAIL = ?, CCCD = ?, DIACHI = ? WHERE MAKH = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, kh.getMaTK());
            pstmt.setDouble(2, kh.getTongTienNo());
            pstmt.setString(3, kh.getHoTen());
            pstmt.setString(4, kh.getSdt());
            pstmt.setString(5, kh.getEmail());
            pstmt.setString(6, kh.getCccd());
            pstmt.setString(7, kh.getDiaChi());
            pstmt.setString(8, kh.getMaKH());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error in updateKhachHang: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in updateKhachHang");
                    DatabaseUtil.reconnect();
                    return updateKhachHang(kh); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public boolean deleteKhachHang(String maKH) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getValidConnection();

            String sql = "DELETE FROM KHACHHANG WHERE MAKH = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maKH);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error in deleteKhachHang: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in deleteKhachHang");
                    DatabaseUtil.reconnect();
                    return deleteKhachHang(maKH); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public List<KhachHang> searchKhachHang(String keyword) {
        List<KhachHang> danhSachKH = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();

            String sql = "SELECT * FROM KHACHHANG WHERE UPPER(MAKH) LIKE ? OR UPPER(HOTEN) LIKE ? " +
                    "OR SDT LIKE ? OR UPPER(EMAIL) LIKE ? OR CCCD LIKE ?";

            pstmt = conn.prepareStatement(sql);
            String searchParam = "%" + keyword.toUpperCase() + "%";
            pstmt.setString(1, searchParam);
            pstmt.setString(2, searchParam);
            pstmt.setString(3, searchParam);
            pstmt.setString(4, searchParam);
            pstmt.setString(5, searchParam);

            rs = pstmt.executeQuery();

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
        } catch (SQLException e) {
            System.err.println("Error in searchKhachHang: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in searchKhachHang");
                    DatabaseUtil.reconnect();
                    return searchKhachHang(keyword); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return danhSachKH;
    }

    public boolean isPhoneNumberExists(String sdt, String excludeMaKH) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();

            String sql = "SELECT COUNT(*) FROM KHACHHANG WHERE SDT = ?";

            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                sql += " AND MAKH != ?";
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sdt);

            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                pstmt.setString(2, excludeMaKH);
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error in isPhoneNumberExists: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in isPhoneNumberExists");
                    DatabaseUtil.reconnect();
                    return isPhoneNumberExists(sdt, excludeMaKH); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return false;
    }

    public boolean isEmailExists(String email, String excludeMaKH) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();

            String sql = "SELECT COUNT(*) FROM KHACHHANG WHERE UPPER(EMAIL) = UPPER(?)";

            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                sql += " AND MAKH != ?";
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);

            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                pstmt.setString(2, excludeMaKH);
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error in isEmailExists: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in isEmailExists");
                    DatabaseUtil.reconnect();
                    return isEmailExists(email, excludeMaKH); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return false;
    }

    public boolean isCCCDExists(String cccd, String excludeMaKH) {
        if (cccd == null || cccd.isEmpty()) {
            return false;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();

            String sql = "SELECT COUNT(*) FROM KHACHHANG WHERE CCCD = ?";

            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                sql += " AND MAKH != ?";
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cccd);

            if (excludeMaKH != null && !excludeMaKH.isEmpty()) {
                pstmt.setString(2, excludeMaKH);
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error in isCCCDExists: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in isCCCDExists");
                    DatabaseUtil.reconnect();
                    return isCCCDExists(cccd, excludeMaKH); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return false;
    }

    // Phương thức mới: Cập nhật công nợ khách hàng
    public boolean updateCongNo(String maKH, double soTien) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getValidConnection();

            // Cập nhật tổng tiền nợ
            String updateSql = "UPDATE KHACHHANG SET TONGTIENNO = TONGTIENNO + ? WHERE MAKH = ?";
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setDouble(1, soTien);
            pstmt.setString(2, maKH);

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error in updateCongNo: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in updateCongNo");
                    DatabaseUtil.reconnect();
                    return updateCongNo(maKH, soTien); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }

            return false;
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // Phương thức mới: Nhập danh sách khách hàng từ danh sách
    public int importKhachHang(List<KhachHang> danhSachKH) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int countSuccess = 0;

        try {
            conn = getValidConnection();
            conn.setAutoCommit(false);

            String sql = "INSERT INTO KHACHHANG (MATK, TONGTIENNO, HOTEN, SDT, EMAIL, CCCD, DIACHI) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql);

            for (KhachHang kh : danhSachKH) {
                // Kiểm tra trùng lặp SĐT
                if (isPhoneNumberExists(kh.getSdt(), null)) {
                    continue;
                }

                // Kiểm tra trùng lặp email nếu có
                if (kh.getEmail() != null && !kh.getEmail().isEmpty() &&
                        isEmailExists(kh.getEmail(), null)) {
                    continue;
                }

                // Kiểm tra trùng lặp CCCD nếu có
                if (kh.getCccd() != null && !kh.getCccd().isEmpty() &&
                        isCCCDExists(kh.getCccd(), null)) {
                    continue;
                }

                pstmt.setString(1, kh.getMaTK());
                pstmt.setDouble(2, kh.getTongTienNo());
                pstmt.setString(3, kh.getHoTen());
                pstmt.setString(4, kh.getSdt());
                pstmt.setString(5, kh.getEmail());
                pstmt.setString(6, kh.getCccd());
                pstmt.setString(7, kh.getDiaChi());

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    countSuccess++;
                }
            }

            conn.commit();
            return countSuccess;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error during rollback: " + ex.getMessage());
            }

            System.err.println("Error in importKhachHang: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in importKhachHang");
                    DatabaseUtil.reconnect();
                    return importKhachHang(danhSachKH); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }

            return 0;
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // Phương thức mới: Lấy thống kê khách hàng
    public Map<String, Object> getThongKeKhachHang() {
        Map<String, Object> thongKe = new HashMap<>();
        String sqlTongSoKH = "SELECT COUNT(*) AS total FROM KHACHHANG";
        String sqlKHCoNo = "SELECT COUNT(*) AS total FROM KHACHHANG WHERE TongTienNo > 0";
        String sqlKHKHongNo = "SELECT COUNT(*) AS total FROM KHACHHANG WHERE TongTienNo <= 0 OR TongTienNo IS NULL";
        String sqlTongTienNoAll = "SELECT SUM(TongTienNo) AS totalDebt FROM KHACHHANG WHERE TongTienNo > 0";
        // Oracle-compatible query for top 5 debtors
        String sqlTop5Debtors = "SELECT MaKH, HoTen, TongTienNo FROM " +
                "(SELECT MaKH, HoTen, TongTienNo FROM KHACHHANG WHERE TongTienNo > 0 ORDER BY TongTienNo DESC) " +
                "WHERE ROWNUM <= 5";

        Connection conn = null; // Declare Connection outside try-with-resources if getValidConnection is used
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection(); // Use the helper method
            stmt = conn.createStatement();

            // Tổng số khách hàng
            rs = stmt.executeQuery(sqlTongSoKH);
            if (rs.next()) {
                thongKe.put("tongSoKhachHang", rs.getLong("total"));
            }
            if (rs != null)
                rs.close();

            // Số khách hàng có nợ
            rs = stmt.executeQuery(sqlKHCoNo);
            if (rs.next()) {
                thongKe.put("soKhachHangCoNo", rs.getLong("total"));
            }
            if (rs != null)
                rs.close();

            // Số khách hàng không nợ
            rs = stmt.executeQuery(sqlKHKHongNo);
            if (rs.next()) {
                thongKe.put("soKhachHangKhongNo", rs.getLong("total"));
            }
            if (rs != null)
                rs.close();

            // Tổng tiền nợ của tất cả khách hàng
            rs = stmt.executeQuery(sqlTongTienNoAll);
            if (rs.next()) {
                thongKe.put("tongTienNoAll", rs.getDouble("totalDebt"));
            } else {
                thongKe.put("tongTienNoAll", 0.0);
            }
            if (rs != null)
                rs.close();

            // Top 5 khách hàng nợ nhiều nhất
            List<KhachHang> topDebtors = new ArrayList<>();
            rs = stmt.executeQuery(sqlTop5Debtors);
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MaKH"));
                kh.setHoTen(rs.getString("HoTen"));
                kh.setTongTienNo(rs.getDouble("TongTienNo"));
                topDebtors.add(kh);
            }
            thongKe.put("top5Debtors", topDebtors);

        } catch (SQLException e) {
            System.err.println("SQL Error in getThongKeKhachHang: " + e.getMessage());
            e.printStackTrace();
            // Consider how to handle errors, e.g., return partial data or throw
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getThongKeKhachHang");
                    DatabaseUtil.reconnect();
                    return getThongKeKhachHang(); // Retry
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                // Do not close conn here if it's managed by DatabaseUtil or a connection pool
                // and obtained via getValidConnection()
            } catch (SQLException e) {
                System.err.println("Error closing resources in getThongKeKhachHang: " + e.getMessage());
            }
        }
        return thongKe;
    }

    // Phương thức mới: Lấy danh sách khách hàng có công nợ
    public List<KhachHang> getKhachHangCoCongNo() {
        List<KhachHang> danhSachKH = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();

            String sql = "SELECT * FROM KHACHHANG WHERE TONGTIENNO > 0 ORDER BY TONGTIENNO DESC";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

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
        } catch (SQLException e) {
            System.err.println("Error in getKhachHangCoCongNo: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getKhachHangCoCongNo");
                    DatabaseUtil.reconnect();
                    return getKhachHangCoCongNo(); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return danhSachKH;
    }

    // Phương thức mới: Lấy khách hàng theo số điện thoại
    public KhachHang getKhachHangBySDT(String sdt) {
        KhachHang kh = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();

            String sql = "SELECT * FROM KHACHHANG WHERE SDT = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sdt);
            rs = pstmt.executeQuery();

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
        } catch (SQLException e) {
            System.err.println("Error in getKhachHangBySDT: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getKhachHangBySDT");
                    DatabaseUtil.reconnect();
                    return getKhachHangBySDT(sdt); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return kh;
    }

    // Phương thức mới: Lấy khách hàng theo CCCD
    public KhachHang getKhachHangByCCCD(String cccd) {
        KhachHang kh = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();

            String sql = "SELECT * FROM KHACHHANG WHERE CCCD = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cccd);
            rs = pstmt.executeQuery();

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
        } catch (SQLException e) {
            System.err.println("Error in getKhachHangByCCCD: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getKhachHangByCCCD");
                    DatabaseUtil.reconnect();
                    return getKhachHangByCCCD(cccd); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return kh;
    }

    // Phương thức mới: Lấy khách hàng theo email
    public KhachHang getKhachHangByEmail(String email) {
        KhachHang kh = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();

            String sql = "SELECT * FROM KHACHHANG WHERE UPPER(EMAIL) = UPPER(?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

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
        } catch (SQLException e) {
            System.err.println("Error in getKhachHangByEmail: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getKhachHangByEmail");
                    DatabaseUtil.reconnect();
                    return getKhachHangByEmail(email); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return kh;
    }

    // Phương thức mới: Xóa nhiều khách hàng
    public int deleteMultipleKhachHang(List<String> maKHList) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int countDeleted = 0;

        try {
            conn = getValidConnection();
            conn.setAutoCommit(false);

            String sql = "DELETE FROM KHACHHANG WHERE MAKH = ?";
            pstmt = conn.prepareStatement(sql);

            for (String maKH : maKHList) {
                // Kiểm tra khách hàng có công nợ không
                KhachHang kh = getKhachHangByMa(maKH);
                if (kh != null && kh.getTongTienNo() > 0) {
                    continue; // Bỏ qua khách hàng có công nợ
                }

                pstmt.setString(1, maKH);
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    countDeleted++;
                }
            }

            conn.commit();
            return countDeleted;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error during rollback: " + ex.getMessage());
            }

            System.err.println("Error in deleteMultipleKhachHang: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in deleteMultipleKhachHang");
                    DatabaseUtil.reconnect();
                    return deleteMultipleKhachHang(maKHList); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }

            return 0;
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // Phương thức mới: Lấy danh sách khách hàng phân trang
    public List<KhachHang> getKhachHangPhanTrang(int trang, int soLuongMoiTrang) {
        List<KhachHang> danhSachKH = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();

            // Tính offset
            int offset = (trang - 1) * soLuongMoiTrang;

            String sql = "SELECT * FROM KHACHHANG ORDER BY MAKH OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, offset);
            pstmt.setInt(2, soLuongMoiTrang);

            rs = pstmt.executeQuery();

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
        } catch (SQLException e) {
            System.err.println("Error in getKhachHangPhanTrang: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getKhachHangPhanTrang");
                    DatabaseUtil.reconnect();
                    return getKhachHangPhanTrang(trang, soLuongMoiTrang); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return danhSachKH;
    }

    // Phương thức mới: Đếm tổng số khách hàng
    public int countKhachHang() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = getValidConnection();

            String sql = "SELECT COUNT(*) FROM KHACHHANG";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error in countKhachHang: " + e.getMessage());
            e.printStackTrace();

            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in countKhachHang");
                    DatabaseUtil.reconnect();
                    return countKhachHang(); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return count;
    }
}
