package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap; // Kept for getThongKeNhanVien
import java.util.List;
import java.util.Map; // Kept for getThongKeNhanVien

import model.NhanVien;
import util.DatabaseUtil; // Assuming you have this utility class

public class NhanVienDAO {

    // Helper method to get a valid connection, similar to other DAOs
    private Connection getValidConnection() throws SQLException {
        Connection conn = DatabaseUtil.getConnection(); // Use getConnection from DatabaseUtil
        if (conn == null || conn.isClosed()) {
            System.out.println("Attempting to reconnect in NhanVienDAO");
            DatabaseUtil.reconnect();
            conn = DatabaseUtil.getConnection();
        }
        if (conn == null) {
            throw new SQLException("Failed to establish a valid database connection.");
        }
        return conn;
    }

    // Phương thức lấy tất cả nhân viên từ database
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> danhSachNhanVien = new ArrayList<>();
        String sql = "SELECT MaNV, MaTK, HoTen, SDT, Email, ChucVu FROM NHANVIEN ORDER BY MaNV";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getValidConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setMaTK(rs.getString("MaTK"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setChucVu(rs.getString("ChucVu"));
                danhSachNhanVien.add(nv);
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllNhanVien: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getAllNhanVien");
                    DatabaseUtil.reconnect();
                    return getAllNhanVien(); // Retry the method
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                // Do not close conn here if it's managed by DatabaseUtil or a connection pool
            } catch (SQLException e) {
                System.err.println("Error closing resources in getAllNhanVien: " + e.getMessage());
            }
        }
        return danhSachNhanVien;
    }

    // Phương thức lấy nhân viên theo mã
    public NhanVien getNhanVienByMa(String maNV) {
        NhanVien nv = null;
        String sql = "SELECT MaNV, MaTK, HoTen, SDT, Email, ChucVu FROM NHANVIEN WHERE MaNV = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getValidConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maNV);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setMaTK(rs.getString("MaTK"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setChucVu(rs.getString("ChucVu"));
            }
        } catch (SQLException e) {
            System.err.println("Error in getNhanVienByMa: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getNhanVienByMa");
                    DatabaseUtil.reconnect();
                    return getNhanVienByMa(maNV); // Retry
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources in getNhanVienByMa: " + e.getMessage());
            }
        }
        return nv;
    }

    // Phương thức thêm nhân viên (Trigger sẽ tạo MaNV)
    public boolean addNhanVien(NhanVien nv) {
        // MaNV is generated by a trigger, so we don't insert it.
        String sql = "INSERT INTO NHANVIEN (MaTK, HoTen, SDT, Email, ChucVu) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getValidConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nv.getMaTK());
            pstmt.setString(2, nv.getHoTen());
            pstmt.setString(3, nv.getSdt());
            pstmt.setString(4, nv.getEmail());
            pstmt.setString(5, nv.getChucVu());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error in addNhanVien: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in addNhanVien");
                    DatabaseUtil.reconnect();
                    return addNhanVien(nv); // Retry
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources in addNhanVien: " + e.getMessage());
            }
        }
    }

    // Phương thức cập nhật nhân viên
    public boolean updateNhanVien(NhanVien nv) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE NHANVIEN SET MaTK = ?, HoTen = ?, SDT = ?, Email = ?, ChucVu = ? WHERE MaNV = ?";
        try {
            conn = getValidConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nv.getMaTK());
            pstmt.setString(2, nv.getHoTen());
            pstmt.setString(3, nv.getSdt());
            pstmt.setString(4, nv.getEmail());
            pstmt.setString(5, nv.getChucVu());
            pstmt.setString(6, nv.getMaNV()); // WHERE clause

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error in updateNhanVien: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in updateNhanVien");
                    DatabaseUtil.reconnect();
                    return updateNhanVien(nv); // Retry
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources in updateNhanVien: " + e.getMessage());
            }
        }
    }

    // Phương thức xóa nhân viên
    public boolean deleteNhanVien(String maNV) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM NHANVIEN WHERE MaNV = ?";
        try {
            conn = getValidConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maNV);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error in deleteNhanVien: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in deleteNhanVien");
                    DatabaseUtil.reconnect();
                    return deleteNhanVien(maNV); // Retry
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources in deleteNhanVien: " + e.getMessage());
            }
        }
    }

    // Phương thức tìm kiếm nhân viên
    public List<NhanVien> searchNhanVien(String keyword) {
        List<NhanVien> danhSachNhanVien = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();

            String sql = "SELECT MaNV, MaTK, HoTen, SDT, Email, ChucVu FROM NHANVIEN WHERE UPPER(MANV) LIKE ? OR UPPER(HOTEN) LIKE ? "
                    +
                    "OR SDT LIKE ? OR UPPER(EMAIL) LIKE ? OR UPPER(CHUCVU) LIKE ?";

            pstmt = conn.prepareStatement(sql);
            String searchParam = "%" + keyword.toUpperCase() + "%";
            pstmt.setString(1, searchParam);
            pstmt.setString(2, searchParam);
            pstmt.setString(3, searchParam);
            pstmt.setString(4, searchParam);
            pstmt.setString(5, searchParam);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setMaTK(rs.getString("MaTK"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setChucVu(rs.getString("ChucVu"));
                danhSachNhanVien.add(nv);
            }
        } catch (SQLException e) {
            System.err.println("Error in searchNhanVien: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in searchNhanVien");
                    DatabaseUtil.reconnect();
                    return searchNhanVien(keyword);
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
        return danhSachNhanVien;
    }

    // Phương thức tìm kiếm nhân viên theo tên
    public List<NhanVien> searchNhanVienByName(String keyword) {
        List<NhanVien> danhSachNhanVien = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            String sql = "SELECT MaNV, MaTK, HoTen, SDT, Email, ChucVu FROM NHANVIEN WHERE UPPER(HOTEN) LIKE ?";
            pstmt = conn.prepareStatement(sql);
            String searchParam = "%" + keyword.toUpperCase() + "%";
            pstmt.setString(1, searchParam);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setMaTK(rs.getString("MaTK"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setChucVu(rs.getString("ChucVu"));
                danhSachNhanVien.add(nv);
            }
        } catch (SQLException e) {
            System.err.println("Error in searchNhanVienByName: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in searchNhanVienByName");
                    DatabaseUtil.reconnect();
                    return searchNhanVienByName(keyword);
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
        return danhSachNhanVien;
    }

    // Phương thức lọc nhân viên theo chức vụ
    public List<NhanVien> getNhanVienByChucVu(String chucVu) {
        List<NhanVien> danhSachNhanVien = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            String sql = "SELECT MaNV, MaTK, HoTen, SDT, Email, ChucVu FROM NHANVIEN WHERE CHUCVU = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, chucVu);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setMaTK(rs.getString("MaTK"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setChucVu(rs.getString("ChucVu"));
                danhSachNhanVien.add(nv);
            }
        } catch (SQLException e) {
            System.err.println("Error in getNhanVienByChucVu: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getNhanVienByChucVu");
                    DatabaseUtil.reconnect();
                    return getNhanVienByChucVu(chucVu);
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
        return danhSachNhanVien;
    }

    // Phương thức lấy danh sách các chức vụ
    public List<String> getAllChucVu() {
        List<String> danhSachChucVu = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            String sql = "SELECT DISTINCT CHUCVU FROM NHANVIEN ORDER BY CHUCVU";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String chucVu = rs.getString("CHUCVU");
                if (chucVu != null && !chucVu.trim().isEmpty()) {
                    danhSachChucVu.add(chucVu);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllChucVu: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getAllChucVu");
                    DatabaseUtil.reconnect();
                    return getAllChucVu();
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
        return danhSachChucVu;
    }

    // Phương thức kiểm tra số điện thoại đã tồn tại
    public boolean isPhoneNumberExists(String sdt, String excludeMaNV) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            String sql = "SELECT COUNT(*) FROM NHANVIEN WHERE SDT = ?";
            if (excludeMaNV != null && !excludeMaNV.isEmpty()) {
                sql += " AND MANV != ?";
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sdt);
            if (excludeMaNV != null && !excludeMaNV.isEmpty()) {
                pstmt.setString(2, excludeMaNV);
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error in isPhoneNumberExists: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in isPhoneNumberExists");
                    DatabaseUtil.reconnect();
                    return isPhoneNumberExists(sdt, excludeMaNV);
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

    // Phương thức kiểm tra email đã tồn tại
    public boolean isEmailExists(String email, String excludeMaNV) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            String sql = "SELECT COUNT(*) FROM NHANVIEN WHERE UPPER(EMAIL) = UPPER(?)";
            if (excludeMaNV != null && !excludeMaNV.isEmpty()) {
                sql += " AND MANV != ?";
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email.toUpperCase());
            if (excludeMaNV != null && !excludeMaNV.isEmpty()) {
                pstmt.setString(2, excludeMaNV);
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error in isEmailExists: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in isEmailExists");
                    DatabaseUtil.reconnect();
                    return isEmailExists(email, excludeMaNV);
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

    // Phương thức xóa nhiều nhân viên
    public int deleteMultipleNhanVien(List<String> maNVList) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int countDeleted = 0;

        try {
            conn = getValidConnection();
            conn.setAutoCommit(false);
            String sql = "DELETE FROM NHANVIEN WHERE MANV = ?";
            pstmt = conn.prepareStatement(sql);

            for (String maNV : maNVList) {
                pstmt.setString(1, maNV);
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
            System.err.println("Error in deleteMultipleNhanVien: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in deleteMultipleNhanVien");
                    DatabaseUtil.reconnect();
                    return deleteMultipleNhanVien(maNVList);
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

    // Phương thức nhập danh sách nhân viên từ danh sách
    public int importNhanVien(List<NhanVien> danhSachNV) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int countSuccess = 0;

        try {
            conn = getValidConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO NHANVIEN (MATK, HOTEN, SDT, EMAIL, CHUCVU) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            for (NhanVien nv : danhSachNV) {
                if (isPhoneNumberExists(nv.getSdt(), null)) {
                    continue;
                }
                if (nv.getEmail() != null && !nv.getEmail().isEmpty() && isEmailExists(nv.getEmail(), null)) {
                    continue;
                }
                pstmt.setString(1, nv.getMaTK());
                pstmt.setString(2, nv.getHoTen());
                pstmt.setString(3, nv.getSdt());
                pstmt.setString(4, nv.getEmail());
                pstmt.setString(5, nv.getChucVu());
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
            System.err.println("Error in importNhanVien: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in importNhanVien");
                    DatabaseUtil.reconnect();
                    return importNhanVien(danhSachNV);
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

    // Phương thức lấy thống kê nhân viên
    public Map<String, Object> getThongKeNhanVien() {
        Map<String, Object> thongKe = new HashMap<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            stmt = conn.createStatement();

            // Tổng số nhân viên
            rs = stmt.executeQuery("SELECT COUNT(*) FROM NHANVIEN");
            if (rs.next()) {
                thongKe.put("tongSoNhanVien", rs.getInt(1));
            }
            if (rs != null)
                rs.close();

            // Thống kê theo chức vụ
            rs = stmt.executeQuery(
                    "SELECT CHUCVU, COUNT(*) AS SOLUONG FROM NHANVIEN GROUP BY CHUCVU ORDER BY SOLUONG DESC");
            Map<String, Integer> thongKeTheoChucVu = new HashMap<>();
            while (rs.next()) {
                String chucVu = rs.getString("CHUCVU");
                int soLuong = rs.getInt("SOLUONG");
                thongKeTheoChucVu.put(chucVu, soLuong);
            }
            thongKe.put("thongKeTheoChucVu", thongKeTheoChucVu);

        } catch (SQLException e) {
            System.err.println("Error in getThongKeNhanVien: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getThongKeNhanVien");
                    DatabaseUtil.reconnect();
                    return getThongKeNhanVien();
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
        return thongKe;
    }

    // Phương thức lấy mã nhân viên mặc định
    public String getDefaultNhanVienMa() {
        String defaultMaNV = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            // Ưu tiên lấy nhân viên quản lý
            // FETCH FIRST 1 ROW ONLY is Oracle specific, ensure DB compatibility or use
            // ROWNUM <=1
            String sql = "SELECT MANV FROM NHANVIEN WHERE UPPER(CHUCVU) = 'QUẢN LÝ' ORDER BY MANV FETCH FIRST 1 ROW ONLY";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                defaultMaNV = rs.getString("MANV");
            } else {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                // Nếu không có nhân viên quản lý, lấy nhân viên đầu tiên
                String backupSql = "SELECT MANV FROM NHANVIEN ORDER BY MANV FETCH FIRST 1 ROW ONLY";
                pstmt = conn.prepareStatement(backupSql);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    defaultMaNV = rs.getString("MANV");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getDefaultNhanVienMa: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getDefaultNhanVienMa");
                    DatabaseUtil.reconnect();
                    return getDefaultNhanVienMa();
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
        return defaultMaNV;
    }

    // Phương thức lấy nhân viên theo số điện thoại
    public NhanVien getNhanVienBySDT(String sdt) {
        NhanVien nv = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            String sql = "SELECT MaNV, MaTK, HoTen, SDT, Email, ChucVu FROM NHANVIEN WHERE SDT = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sdt);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setMaTK(rs.getString("MaTK"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setChucVu(rs.getString("ChucVu"));
            }
        } catch (SQLException e) {
            System.err.println("Error in getNhanVienBySDT: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getNhanVienBySDT");
                    DatabaseUtil.reconnect();
                    return getNhanVienBySDT(sdt);
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
        return nv;
    }

    // Phương thức lấy nhân viên theo email
    public NhanVien getNhanVienByEmail(String email) {
        NhanVien nv = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            String sql = "SELECT MaNV, MaTK, HoTen, SDT, Email, ChucVu FROM NHANVIEN WHERE UPPER(EMAIL) = UPPER(?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email.toUpperCase());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setMaTK(rs.getString("MaTK"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setChucVu(rs.getString("ChucVu"));
            }
        } catch (SQLException e) {
            System.err.println("Error in getNhanVienByEmail: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getNhanVienByEmail");
                    DatabaseUtil.reconnect();
                    return getNhanVienByEmail(email);
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
        return nv;
    }

    // Phương thức kiểm tra nhân viên tồn tại bằng MaNV
    public boolean existsNhanVien(String maNV) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT 1 FROM NHANVIEN WHERE MaNV = ?";
        try {
            conn = getValidConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maNV);
            rs = pstmt.executeQuery();
            return rs.next(); // Returns true if a record is found
        } catch (SQLException e) {
            System.err.println("Error in existsNhanVien: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in existsNhanVien");
                    DatabaseUtil.reconnect();
                    return existsNhanVien(maNV); // Retry
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources in existsNhanVien: " + e.getMessage());
            }
        }
    }
}
