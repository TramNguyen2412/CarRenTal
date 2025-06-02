package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.TaiKhoan;
import util.DatabaseUtil;
import model.TaiKhoanExtended;
public class TaiKhoanDAO {
    // Phương thức kiểm tra kết nối và khôi phục nếu cần
    private Connection getValidConnection() throws SQLException {
        Connection conn = DatabaseUtil.getConnection();
//        
//        // Kiểm tra kết nối còn hợp lệ không
//        if (!conn.isValid(2)) { // timeout 2 giây
//            System.out.println("Connection invalidated, reconnecting...");
//            DatabaseUtil.reconnect();
//            conn = DatabaseUtil.getConnection();
//        }
//        
        return conn;
    }
    public TaiKhoan checkLogin(String username, String password) {
        String sql = "SELECT * FROM TAIKHOAN WHERE TenDangNhap = ? AND MatKhau = ? AND TrangThai = 'Hoạt động'";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setMaTK(rs.getString("MaTK"));
                    tk.setMaVaiTro(rs.getString("MaVaiTro"));
                    tk.setTenDangNhap(rs.getString("TenDangNhap"));
                    tk.setMatKhau(rs.getString("MatKhau"));
                    tk.setTrangThai(rs.getString("TrangThai"));
                    return tk;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<TaiKhoan> getAllTaiKhoan() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM TAIKHOAN";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setMaTK(rs.getString("MaTK"));
                tk.setMaVaiTro(rs.getString("MaVaiTro"));
                tk.setTenDangNhap(rs.getString("TenDangNhap"));
                tk.setMatKhau(rs.getString("MatKhau"));
                tk.setTrangThai(rs.getString("TrangThai"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
    public boolean removeAllUserLinks(String maTK) {
        Connection conn = null;
        boolean success = true;

        try {
            conn = getValidConnection();
            conn.setAutoCommit(false);

            // Gỡ bỏ liên kết từ NHANVIEN
            String sqlNV = "UPDATE NHANVIEN SET MaTK = NULL WHERE MaTK = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlNV)) {
                pstmt.setString(1, maTK);
                pstmt.executeUpdate();
            }

            // Gỡ bỏ liên kết từ KHACHHANG
            String sqlKH = "UPDATE KHACHHANG SET MaTK = NULL WHERE MaTK = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlKH)) {
                pstmt.setString(1, maTK);
                pstmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }
        
    // Lấy danh sách tài khoản mở rộng (có thêm thông tin người dùng)
    public List<TaiKhoanExtended> getAllTaiKhoanExtended() {
        List<TaiKhoanExtended> list = new ArrayList<>();
        String sql = "SELECT TK.*, " +
                    "VT.TenVaiTro, " +  // Thêm tên vai trò
                    "CASE " +
                    "   WHEN NV.MaNV IS NOT NULL THEN 'NV' " +
                    "   WHEN KH.MaKH IS NOT NULL THEN 'KH' " +
                    "   ELSE 'UNKNOWN' " +
                    "END AS LoaiNguoiDung, " +
                    "COALESCE(NV.MaNV, KH.MaKH) AS MaNguoiDung, " +
                    "COALESCE(NV.HoTen, KH.HoTen) AS TenNguoiDung " +
                    "FROM TAIKHOAN TK " +
                    "LEFT JOIN VAITRO VT ON TK.MaVaiTro = VT.MaVaiTro " +
                    "LEFT JOIN NHANVIEN NV ON TK.MaTK = NV.MaTK " +
                    "LEFT JOIN KHACHHANG KH ON TK.MaTK = KH.MaTK";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                TaiKhoanExtended tk = new TaiKhoanExtended();
                tk.setMaTK(rs.getString("MaTK"));
                tk.setMaVaiTro(rs.getString("MaVaiTro"));
                tk.setTenVaiTro(rs.getString("TenVaiTro")); // Lưu tên vai trò
                tk.setTenDangNhap(rs.getString("TenDangNhap"));
                tk.setMatKhau(rs.getString("MatKhau"));
                tk.setTrangThai(rs.getString("TrangThai"));

                // Thông tin mở rộng
                tk.setLoaiNguoiDung(rs.getString("LoaiNguoiDung"));
                tk.setMaNguoiDung(rs.getString("MaNguoiDung"));
                tk.setTenNguoiDung(rs.getString("TenNguoiDung"));

                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    // Tương tự, cập nhật phương thức getTaiKhoanExtendedById
    public TaiKhoanExtended getTaiKhoanExtendedById(String maTK) {
        String sql = "SELECT TK.*, " +
                    "VT.TenVaiTro, " +  // Thêm tên vai trò
                    "CASE " +
                    "   WHEN NV.MaNV IS NOT NULL THEN 'NV' " +
                    "   WHEN KH.MaKH IS NOT NULL THEN 'KH' " +
                    "   ELSE 'UNKNOWN' " +
                    "END AS LoaiNguoiDung, " +
                    "COALESCE(NV.MaNV, KH.MaKH) AS MaNguoiDung, " +
                    "COALESCE(NV.HoTen, KH.HoTen) AS TenNguoiDung " +
                    "FROM TAIKHOAN TK " +
                    "LEFT JOIN VAITRO VT ON TK.MaVaiTro = VT.MaVaiTro " +
                    "LEFT JOIN NHANVIEN NV ON TK.MaTK = NV.MaTK " +
                    "LEFT JOIN KHACHHANG KH ON TK.MaTK = KH.MaTK " +
                    "WHERE TK.MaTK = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maTK);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                TaiKhoanExtended tk = new TaiKhoanExtended();
                tk.setMaTK(rs.getString("MaTK"));
                tk.setMaVaiTro(rs.getString("MaVaiTro"));
                tk.setTenVaiTro(rs.getString("TenVaiTro")); // Lưu tên vai trò
                tk.setTenDangNhap(rs.getString("TenDangNhap"));
                tk.setMatKhau(rs.getString("MatKhau"));
                tk.setTrangThai(rs.getString("TrangThai"));

                // Thông tin mở rộng
                tk.setLoaiNguoiDung(rs.getString("LoaiNguoiDung"));
                tk.setMaNguoiDung(rs.getString("MaNguoiDung"));
                tk.setTenNguoiDung(rs.getString("TenNguoiDung"));

                return tk;
            }
        } catch (SQLException e) {
            e.printStackTrace();

            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in getTaiKhoanExtendedById");
//                    DatabaseUtil.reconnect();
//                    return getTaiKhoanExtendedById(maTK); // Thử lại một lần
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    // Lấy danh sách nhân viên chưa có tài khoản
    public List<String[]> getNhanVienChuaCoTaiKhoan() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT MaNV, HoTen FROM NHANVIEN WHERE MaTK IS NULL";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String[] nv = new String[2];
                nv[0] = rs.getString("MaNV");
                nv[1] = rs.getString("HoTen");
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in getNhanVienChuaCoTaiKhoan");
//                    DatabaseUtil.reconnect();
//                    return getNhanVienChuaCoTaiKhoan(); // Thử lại một lần
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return list;
    }
    
    // Lấy danh sách khách hàng chưa có tài khoản
    public List<String[]> getKhachHangChuaCoTaiKhoan() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT MaKH, HoTen FROM KHACHHANG WHERE MaTK IS NULL";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String[] kh = new String[2];
                kh[0] = rs.getString("MaKH");
                kh[1] = rs.getString("HoTen");
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in getKhachHangChuaCoTaiKhoan");
//                    DatabaseUtil.reconnect();
//                    return getKhachHangChuaCoTaiKhoan(); // Thử lại một lần
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return list;
    }
    
    // Thêm tài khoản mới
    public boolean insert(TaiKhoan tk) {
        String sql = "INSERT INTO TAIKHOAN (MaTK, MaVaiTro, TenDangNhap, MatKhau, TrangThai) VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getValidConnection();
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tk.getMaTK());
            pstmt.setString(2, tk.getMaVaiTro());
            pstmt.setString(3, tk.getTenDangNhap());
            pstmt.setString(4, tk.getMatKhau());
            pstmt.setString(5, tk.getTrangThai());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in insert");
//                    DatabaseUtil.reconnect();
//                    return insert(tk); // Thử lại một lần
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
            }
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Cập nhật tài khoản
    public boolean update(TaiKhoan tk) {
        String sql = "UPDATE TAIKHOAN SET MaVaiTro = ?, TenDangNhap = ?, MatKhau = ?, TrangThai = ? WHERE MaTK = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getValidConnection();
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tk.getMaVaiTro());
            pstmt.setString(2, tk.getTenDangNhap());
            pstmt.setString(3, tk.getMatKhau());
            pstmt.setString(4, tk.getTrangThai());
            pstmt.setString(5, tk.getMaTK());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in update");
//                    DatabaseUtil.reconnect();
//                    return update(tk); // Thử lại một lần
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
            }
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Cập nhật MaTK cho NhanVien
    public boolean updateMaTKForNhanVien(String maNV, String maTK) {
        String sql = "UPDATE NHANVIEN SET MaTK = ? WHERE MaNV = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getValidConnection();
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maTK);
            pstmt.setString(2, maNV);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in updateMaTKForNhanVien");
//                    DatabaseUtil.reconnect();
//                    return updateMaTKForNhanVien(maNV, maTK); // Thử lại một lần
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
            }
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Cập nhật MaTK cho KhachHang
    public boolean updateMaTKForKhachHang(String maKH, String maTK) {
        String sql = "UPDATE KHACHHANG SET MaTK = ? WHERE MaKH = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getValidConnection();
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maTK);
            pstmt.setString(2, maKH);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in updateMaTKForKhachHang");
//                    DatabaseUtil.reconnect();
//                    return updateMaTKForKhachHang(maKH, maTK); // Thử lại một lần
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
            }
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean delete(String maTK) {
        // Trước khi xóa, cần set NULL cho các khóa ngoại trong NhanVien và KhachHang
        Connection conn = null;
        
        try {
            conn = getValidConnection();
            conn.setAutoCommit(false);
            
            // Set NULL cho MaTK trong bảng NhanVien
            String sqlNV = "UPDATE NHANVIEN SET MaTK = NULL WHERE MaTK = ?";
            try (PreparedStatement pstmtNV = conn.prepareStatement(sqlNV)) {
                pstmtNV.setString(1, maTK);
                pstmtNV.executeUpdate();
            }
            
            // Set NULL cho MaTK trong bảng KhachHang
            String sqlKH = "UPDATE KHACHHANG SET MaTK = NULL WHERE MaTK = ?";
            try (PreparedStatement pstmtKH = conn.prepareStatement(sqlKH)) {
                pstmtKH.setString(1, maTK);
                pstmtKH.executeUpdate();
            }
            
            // Xóa tài khoản
            String sqlTK = "DELETE FROM TAIKHOAN WHERE MaTK = ?";
            try (PreparedStatement pstmtTK = conn.prepareStatement(sqlTK)) {
                pstmtTK.setString(1, maTK);
                int rowsAffected = pstmtTK.executeUpdate();
                
                if (rowsAffected > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in delete");
//                    DatabaseUtil.reconnect();
//                    return delete(maTK); // Thử lại một lần
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Tạo mã tài khoản mới tự động
    public String generateNewMaTK() {
        String newMaTK = "TK001";
        String sql = "SELECT MAX(MaTK) AS MaxMaTK FROM TAIKHOAN WHERE MaTK LIKE 'TK%'";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                String maxMaTK = rs.getString("MaxMaTK");
                if (maxMaTK != null) {
                    // Tách lấy số từ mã hiện tại
                    int currentNumber = Integer.parseInt(maxMaTK.substring(2));
                    // Tăng lên 1 và định dạng lại
                    newMaTK = String.format("TK%03d", currentNumber + 1);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in generateNewMaTK");
//                    DatabaseUtil.reconnect();
//                    return generateNewMaTK(); // Thử lại một lần
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return newMaTK;
    }
    
    // Kiểm tra tên đăng nhập đã tồn tại chưa
    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM TAIKHOAN WHERE TenDangNhap = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in isUsernameExists");
//                    DatabaseUtil.reconnect();
//                    return isUsernameExists(username); // Thử lại một lần
//                } catch (SQLException ex) {
//                    System.err.println("Failed to reconnect: " + ex.getMessage());
//                }
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false;
    }
}