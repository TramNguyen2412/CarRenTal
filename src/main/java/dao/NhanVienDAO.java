package dao;

import model.NhanVien;
import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.DatabaseMetaData;

public class NhanVienDAO {
    
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
    
    // Phương thức lấy tất cả nhân viên từ database
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> danhSachNhanVien = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT * FROM NHANVIEN ORDER BY MANV";
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
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getAllNhanVien");
                    DatabaseUtil.reconnect();
                    return getAllNhanVien(); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            // Đóng các tài nguyên nhưng KHÔNG đóng kết nối
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return danhSachNhanVien;
    }
    
    // Phương thức lấy nhân viên theo mã
    public NhanVien getNhanVienByMa(String maNV) {
        NhanVien nv = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT * FROM NHANVIEN WHERE MANV = ?";
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
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getNhanVienByMa");
                    DatabaseUtil.reconnect();
                    return getNhanVienByMa(maNV); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return nv;
    }

    // Phương thức kiểm tra nhân viên tồn tại
    public boolean existsNhanVien(String maNV) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "SELECT COUNT(*) FROM NHANVIEN WHERE MANV = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maNV);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error in existsNhanVien: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in existsNhanVien");
                    DatabaseUtil.reconnect();
                    return existsNhanVien(maNV); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return false;
    }
    
    // Phương thức thêm nhân viên
    public String addNhanVien(NhanVien nv) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String newMaNV = null;

        try {
            conn = getValidConnection();
            conn.setAutoCommit(false);

            // INSERT bình thường, cho trigger tạo mã
            String sql = "INSERT INTO NHANVIEN (MATK, HOTEN, SDT, EMAIL, CHUCVU) " +
                         "VALUES (?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nv.getMaTK());
            pstmt.setString(2, nv.getHoTen());
            pstmt.setString(3, nv.getSdt());
            pstmt.setString(4, nv.getEmail());
            pstmt.setString(5, nv.getChucVu());

            int rows = pstmt.executeUpdate();
            pstmt.close();

            if (rows > 0) {
                // Lấy mã nhân viên mới nhất dựa trên SĐT và họ tên
                String getIdSql = "SELECT MANV FROM NHANVIEN " +
                                  "WHERE SDT = ? AND HOTEN = ? " +
                                  "ORDER BY MANV DESC FETCH FIRST 1 ROW ONLY";

                pstmt = conn.prepareStatement(getIdSql);
                pstmt.setString(1, nv.getSdt());
                pstmt.setString(2, nv.getHoTen());

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    newMaNV = rs.getString("MANV");
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
            return newMaNV;

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
                    System.out.println("Attempting to reconnect in addNhanVien");
                    DatabaseUtil.reconnect();
                    return addNhanVien(nv); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }

            System.err.println("Error in addNhanVien: " + e.getMessage());
            e.printStackTrace();
            return null;

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.setAutoCommit(true); // Khôi phục autocommit
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    // Phương thức cập nhật nhân viên
    public boolean updateNhanVien(NhanVien nv) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "UPDATE NHANVIEN SET MATK = ?, HOTEN = ?, SDT = ?, " +
                         "EMAIL = ?, CHUCVU = ? WHERE MANV = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nv.getMaTK());
            pstmt.setString(2, nv.getHoTen());
            pstmt.setString(3, nv.getSdt());
            pstmt.setString(4, nv.getEmail());
            pstmt.setString(5, nv.getChucVu());
            pstmt.setString(6, nv.getMaNV());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error in updateNhanVien: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in updateNhanVien");
                    DatabaseUtil.reconnect();
                    return updateNhanVien(nv); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    // Phương thức xóa nhân viên
    public boolean deleteNhanVien(String maNV) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getValidConnection();
            
            String sql = "DELETE FROM NHANVIEN WHERE MANV = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maNV);
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error in deleteNhanVien: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in deleteNhanVien");
                    DatabaseUtil.reconnect();
                    return deleteNhanVien(maNV); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
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
            
            String sql = "SELECT * FROM NHANVIEN WHERE UPPER(MANV) LIKE ? OR UPPER(HOTEN) LIKE ? " +
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
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in searchNhanVien");
                    DatabaseUtil.reconnect();
                    return searchNhanVien(keyword); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
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
            
            String sql = "SELECT * FROM NHANVIEN WHERE UPPER(HOTEN) LIKE ?";
            
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
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in searchNhanVienByName");
                    DatabaseUtil.reconnect();
                    return searchNhanVienByName(keyword); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
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
            
            String sql = "SELECT * FROM NHANVIEN WHERE CHUCVU = ?";
            
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
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getNhanVienByChucVu");
                    DatabaseUtil.reconnect();
                    return getNhanVienByChucVu(chucVu); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
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
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getAllChucVu");
                    DatabaseUtil.reconnect();
                    return getAllChucVu(); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
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
                    return isPhoneNumberExists(sdt, excludeMaNV); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
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
            pstmt.setString(1, email);
            
            if (excludeMaNV != null && !excludeMaNV.isEmpty()) {
                pstmt.setString(2, excludeMaNV);
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
                    return isEmailExists(email, excludeMaNV); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
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
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in deleteMultipleNhanVien");
                    DatabaseUtil.reconnect();
                    return deleteMultipleNhanVien(maNVList); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
            
            return 0;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.setAutoCommit(true);
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
            
            String sql = "INSERT INTO NHANVIEN (MATK, HOTEN, SDT, EMAIL, CHUCVU) " +
                         "VALUES (?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            
            for (NhanVien nv : danhSachNV) {
                // Kiểm tra trùng lặp SĐT
                if (isPhoneNumberExists(nv.getSdt(), null)) {
                    continue;
                }
                
                // Kiểm tra trùng lặp email nếu có
                if (nv.getEmail() != null && !nv.getEmail().isEmpty() && 
                    isEmailExists(nv.getEmail(), null)) {
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
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in importNhanVien");
                    DatabaseUtil.reconnect();
                    return importNhanVien(danhSachNV); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
            
            return 0;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.setAutoCommit(true);
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
            rs.close();
            
            // Thống kê theo chức vụ
            rs = stmt.executeQuery("SELECT CHUCVU, COUNT(*) AS SOLUONG FROM NHANVIEN GROUP BY CHUCVU ORDER BY SOLUONG DESC");
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
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getThongKeNhanVien");
                    DatabaseUtil.reconnect();
                    return getThongKeNhanVien(); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
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
            String sql = "SELECT MANV FROM NHANVIEN WHERE UPPER(CHUCVU) = 'QUẢN LÝ' ORDER BY MANV FETCH FIRST 1 ROW ONLY";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                defaultMaNV = rs.getString("MANV");
            } else {
                // Nếu không có nhân viên quản lý, lấy nhân viên đầu tiên
                rs.close();
                pstmt.close();
                
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
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getDefaultNhanVienMa");
                    DatabaseUtil.reconnect();
                    return getDefaultNhanVienMa(); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
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
            
            String sql = "SELECT * FROM NHANVIEN WHERE SDT = ?";
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
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getNhanVienBySDT");
                    DatabaseUtil.reconnect();
                    return getNhanVienBySDT(sdt); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
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
            
            String sql = "SELECT * FROM NHANVIEN WHERE UPPER(EMAIL) = UPPER(?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
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
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in getNhanVienByEmail");
                    DatabaseUtil.reconnect();
                    return getNhanVienByEmail(email); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return nv;
    }

    /**
     * Thêm chức vụ mới vào bảng CHUCVU (nếu có) hoặc thêm vào bảng NHANVIEN với nhân viên mẫu
     * @param chucVu Tên chức vụ mới
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addChucVu(String chucVu) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            // Kiểm tra xem bảng CHUCVU có tồn tại không
            DatabaseMetaData dbm = conn.getMetaData();
            rs = dbm.getTables(null, null, "CHUCVU", null);
            
            if (rs.next()) {
                // Bảng CHUCVU tồn tại, thêm vào bảng này
                String sql = "INSERT INTO CHUCVU (TENCHUCVU) VALUES (?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, chucVu);
                
                int rows = pstmt.executeUpdate();
                return rows > 0;
            } else {
                // Bảng CHUCVU không tồn tại, kiểm tra xem chức vụ đã có trong NHANVIEN chưa
                String checkSql = "SELECT COUNT(*) FROM NHANVIEN WHERE UPPER(CHUCVU) = UPPER(?)";
                pstmt = conn.prepareStatement(checkSql);
                pstmt.setString(1, chucVu);
                rs = pstmt.executeQuery();
                
                if (rs.next() && rs.getInt(1) > 0) {
                    // Chức vụ đã tồn tại trong NHANVIEN
                    return true;
                }
                
                // Chức vụ chưa tồn tại, thêm một nhân viên mẫu với chức vụ này
                // Nhân viên này sẽ được đánh dấu là mẫu và có thể xóa sau
                String insertSql = "INSERT INTO NHANVIEN (HOTEN, SDT, EMAIL, CHUCVU) " +
                                  "VALUES ('Template - " + chucVu + "', '0000000000', 'template@example.com', ?)";
                pstmt = conn.prepareStatement(insertSql);
                pstmt.setString(1, chucVu);
                
                int rows = pstmt.executeUpdate();
                
                if (rows > 0) {
                    // Xóa nhân viên mẫu ngay sau khi thêm
                    String deleteSql = "DELETE FROM NHANVIEN WHERE SDT = '0000000000' AND EMAIL = 'template@example.com'";
                    pstmt = conn.prepareStatement(deleteSql);
                    pstmt.executeUpdate();
                    return true;
                }
                
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error in addChucVu: " + e.getMessage());
            e.printStackTrace();
            
            // Thử kết nối lại nếu bị lỗi kết nối đóng
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
                try {
                    System.out.println("Attempting to reconnect in addChucVu");
                    DatabaseUtil.reconnect();
                    return addChucVu(chucVu); // Gọi lại phương thức
                } catch (SQLException ex) {
                    System.err.println("Failed to reconnect: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
