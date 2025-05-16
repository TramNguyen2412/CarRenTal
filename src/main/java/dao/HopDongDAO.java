package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.HopDong;
import model.ChiTietHD;
import util.DatabaseUtil;
import javax.swing.JOptionPane;

public class HopDongDAO {
    private Connection conn;
    
    public HopDongDAO() {
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
    
    public List<HopDong> getAllHopDong() {
        List<HopDong> danhSachHD = new ArrayList<>();

        String sql = "SELECT h.*, k.HOTEN as TENKH, n.HOTEN as TENNV FROM HOPDONG h " +
                     "LEFT JOIN KHACHHANG k ON h.MAKH = k.MAKH " +
                     "LEFT JOIN NHANVIEN n ON h.MANV = n.MANV " +
                     "ORDER BY h.NGAYLAP DESC";

        try (Connection connection = DatabaseUtil.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Executing SQL: " + sql);
            int count = 0;

            while (rs.next()) {
                count++;
                HopDong hd = new HopDong();
                hd.setMaHD(rs.getString("MAHD"));
                hd.setMaKH(rs.getString("MAKH"));
                hd.setMaNV(rs.getString("MANV"));
                hd.setNgayLap(rs.getDate("NGAYLAP"));
                hd.setDiaChiGiao(rs.getString("DIACHIGIAO"));
                hd.setTongTien(rs.getDouble("TONGTIEN"));
                hd.setTrangThai(rs.getString("TRANGTHAI"));
                hd.setTenKH(rs.getString("TENKH")); // Alias từ k.HOTEN as TENKH
                hd.setTenNV(rs.getString("TENNV")); // Alias từ n.HOTEN as TENNV

                danhSachHD.add(hd);
            }

            System.out.println("Found " + count + " contracts in database");
        } catch (SQLException e) {
            System.err.println("Error in getAllHopDong: " + e.getMessage());
            e.printStackTrace();
        }

        return danhSachHD;

    }
    
    public HopDong getHopDongByMa(String maHD) {
        HopDong hd = null;

        String sql = "SELECT h.*, k.HOTEN as TENKH, n.HOTEN as TENNV FROM HOPDONG h " +
                     "LEFT JOIN KHACHHANG k ON h.MAKH = k.MAKH " +
                     "LEFT JOIN NHANVIEN n ON h.MANV = n.MANV " +
                     "WHERE h.MAHD = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            System.out.println("Executing SQL for getHopDongByMa: " + sql + " with MaHD=" + maHD);
            pstmt.setString(1, maHD);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    hd = new HopDong();
                    hd.setMaHD(rs.getString("MAHD"));
                    hd.setMaKH(rs.getString("MAKH"));
                    hd.setMaNV(rs.getString("MANV"));
                    hd.setNgayLap(rs.getDate("NGAYLAP"));
                    hd.setDiaChiGiao(rs.getString("DIACHIGIAO"));
                    hd.setTongTien(rs.getDouble("TONGTIEN"));
                    hd.setTrangThai(rs.getString("TRANGTHAI"));
                    hd.setTenKH(rs.getString("TENKH"));
                    hd.setTenNV(rs.getString("TENNV"));

                    System.out.println("Found contract: " + hd.getMaHD() + ", Customer: " + hd.getTenKH());

                    // Lấy danh sách chi tiết hợp đồng
                    ChiTietHDDao cthdDAO = new ChiTietHDDao();
                    List<ChiTietHD> danhSachCT = cthdDAO.getChiTietHDByMaHD(maHD);
                    hd.setDanhSachXeThue(danhSachCT);
                } else {
                    System.out.println("No contract found with MAHD=" + maHD);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getHopDongByMa: " + e.getMessage());
            e.printStackTrace();
        }

        return hd;
    }
    public String addHopDong(HopDong hd, StringBuilder errorMessage) {
        String maHD = null;
        
        try {
            checkConnection();
            
            // Đảm bảo MaNV luôn có giá trị
            if (hd.getMaNV() == null || hd.getMaNV().trim().isEmpty()) {
                hd.setMaNV("NV001"); // Mã nhân viên mặc định nếu không có
            }
            
            // Kiểm tra khách hàng
            if (hd.getMaKH() == null || hd.getMaKH().isEmpty()) {
                errorMessage.append("Vui lòng chọn khách hàng");
                return null;
            }
            
            conn.setAutoCommit(false); // Bắt đầu transaction
            
            // Sử dụng trigger để tạo MAHD
            String sql = "INSERT INTO HOPDONG (MAKH, MANV, NGAYLAP, DIACHIGIAO, TONGTIEN, TRANGTHAI) " +
                         "VALUES (?, ?, ?, ?, 0, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql, new String[] {"MAHD"});
            pstmt.setString(1, hd.getMaKH());
            pstmt.setString(2, hd.getMaNV());
            pstmt.setDate(3, new java.sql.Date(hd.getNgayLap().getTime()));
            pstmt.setString(4, hd.getDiaChiGiao() != null ? hd.getDiaChiGiao() : "");
            pstmt.setString(5, hd.getTrangThai() != null ? hd.getTrangThai() : "Chờ xác nhận");
            
            int rows = pstmt.executeUpdate();
            
            if (rows == 0) {
                conn.rollback();
                errorMessage.append("Không thể tạo hợp đồng");
                return null;
            }
            
            // Lấy mã hợp đồng vừa được tạo
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                maHD = generatedKeys.getString(1);
            } else {
                conn.rollback();
                errorMessage.append("Không thể lấy mã hợp đồng");
                return null;
            }
            
            // Thêm chi tiết hợp đồng
            if (hd.getDanhSachXeThue() == null || hd.getDanhSachXeThue().isEmpty()) {
                conn.rollback();
                errorMessage.append("Hợp đồng phải có ít nhất 1 xe");
                return null;
            }
            
            ChiTietHDDao cthdDAO = new ChiTietHDDao();
            
            for (ChiTietHD ct : hd.getDanhSachXeThue()) {
                // Kiểm tra dữ liệu chi tiết hợp đồng
                if (ct.getNgayBatDau() == null || ct.getNgayKetThuc() == null) {
                    conn.rollback();
                    errorMessage.append("Vui lòng nhập đầy đủ thông tin ngày thuê xe");
                    return null;
                }
                
                if (ct.getNgayBatDau().after(ct.getNgayKetThuc())) {
                    conn.rollback();
                    errorMessage.append("Ngày bắt đầu không được sau ngày kết thúc");
                    return null;
                }
                
                // Kiểm tra trạng thái xe trước khi thêm vào hợp đồng
                String trangThaiXe = cthdDAO.getXeTrangThai(ct.getMaXe());
                
                // Kiểm tra xe có lịch thuê trong khoảng thời gian này không
                boolean xeDangThue = cthdDAO.isXeDangThueTrongThoiGian(
                    ct.getMaXe(), ct.getNgayBatDau(), ct.getNgayKetThuc());
                
                if (xeDangThue) {
                    conn.rollback();
                    errorMessage.append("Xe " + ct.getTenXe() + " đã được thuê trong khoảng thời gian này");
                    return null;
                }
                
                // Kiểm tra xe có lịch bảo dưỡng trong khoảng thời gian này không
                boolean coLichBaoDuong = cthdDAO.hasMaintenanceSchedule(
                    ct.getMaXe(), ct.getNgayBatDau(), ct.getNgayKetThuc());
                
                if (coLichBaoDuong) {
                    conn.rollback();
                    errorMessage.append("Xe " + ct.getTenXe() + " có lịch bảo dưỡng trong khoảng thời gian thuê");
                    return null;
                }
                
                // Kiểm tra nếu thuê ngay hôm nay, xe phải ở trạng thái "Sẵn sàng"
                java.util.Date today = new java.util.Date();
                if (!ct.getNgayBatDau().after(today) && !"Sẵn sàng".equals(trangThaiXe)) {
                    conn.rollback();
                    errorMessage.append("Xe " + ct.getTenXe() + " không ở trạng thái 'Sẵn sàng' nên không thể thuê ngay");
                    return null;
                }
                
                try {
                    // Thiết lập mã hợp đồng cho chi tiết
                    ct.setMaHD(maHD);
                    
                    // Thêm chi tiết vào CTHD
                    if (!cthdDAO.addChiTietHD(ct)) {
                        conn.rollback();
                        errorMessage.append("Không thể thêm chi tiết hợp đồng cho xe " + ct.getTenXe());
                        return null;
                    }
                } catch (SQLException e) {
                    conn.rollback();
                    
                    String errorMsg = e.getMessage();
                    
                    // Phân tích thông báo lỗi từ trigger
                    if (errorMsg.contains("ORA-20006")) {
                        errorMessage.append("Xe " + ct.getTenXe() + " đã có trong hợp đồng khác trùng thời gian thuê");
                    } else if (errorMsg.contains("ORA-20018")) {
                        errorMessage.append("Xe " + ct.getTenXe() + " có lịch bảo dưỡng trong thời gian thuê");
                    } else if (errorMsg.contains("ORA-20005")) {
                        errorMessage.append("Xe " + ct.getTenXe() + " không ở trạng thái 'Sẵn sàng' nên không thể thuê ngay");
                    } else {
                        errorMessage.append("Lỗi khi thêm chi tiết: " + errorMsg);
                    }
                    
                    return null;
                }
            }
            
            conn.commit();
            generatedKeys.close();
            pstmt.close();
            return maHD;
            
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            String errorMsg = e.getMessage();
            
            // Phân tích thông báo lỗi nếu là từ Oracle
            if (errorMsg.contains("ORA-20002")) {
                errorMessage.append("Tổng tiền ban đầu của hợp đồng phải bằng 0");
            } else if (errorMsg.contains("cannot insert NULL")) {
                errorMessage.append("Thông tin hợp đồng không đầy đủ");
            } else {
                errorMessage.append("Lỗi: " + errorMsg);
            }
            
            e.printStackTrace();
            return null;
            
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
    
    // Phiên bản đơn giản hơn của phương thức addHopDong
    public String addHopDong(HopDong hd) {
        StringBuilder errorMessage = new StringBuilder();
        return addHopDong(hd, errorMessage);
    }
    
    public boolean updateHopDong(HopDong hd, StringBuilder errorMessage) {
        try {
            checkConnection();
            
            // Đảm bảo MaNV luôn có giá trị
            if (hd.getMaNV() == null || hd.getMaNV().trim().isEmpty()) {
                hd.setMaNV("NV001"); // Mã nhân viên mặc định nếu không có
            }
            
            // Kiểm tra dữ liệu đầu vào
            if (hd.getMaHD() == null || hd.getMaHD().isEmpty()) {
                errorMessage.append("Mã hợp đồng không hợp lệ");
                return false;
            }
            
            if (hd.getMaKH() == null || hd.getMaKH().isEmpty()) {
                errorMessage.append("Vui lòng chọn khách hàng");
                return false;
            }
            
            conn.setAutoCommit(false);
            
            String sql = "UPDATE HOPDONG SET " +
                         "MAKH = ?, MANV = ?, DIACHIGIAO = ?, TRANGTHAI = ? " +
                         "WHERE MAHD = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, hd.getMaKH());
            pstmt.setString(2, hd.getMaNV());
            pstmt.setString(3, hd.getDiaChiGiao() != null ? hd.getDiaChiGiao() : "");
            pstmt.setString(4, hd.getTrangThai());
            pstmt.setString(5, hd.getMaHD());
            
            int rows = pstmt.executeUpdate();
            
            if (rows == 0) {
                conn.rollback();
                errorMessage.append("Không tìm thấy hợp đồng để cập nhật");
                return false;
            }
            
            // Cập nhật chi tiết hợp đồng nếu có
            if (hd.getDanhSachXeThue() == null || hd.getDanhSachXeThue().isEmpty()) {
                conn.rollback();
                errorMessage.append("Hợp đồng phải có ít nhất 1 xe");
                return false;
            }
            
            // Xóa chi tiết cũ
            ChiTietHDDao cthdDAO = new ChiTietHDDao();
            try {
                cthdDAO.deleteChiTietHDByMaHD(hd.getMaHD());
            } catch (SQLException e) {
                conn.rollback();
                errorMessage.append("Không thể xóa chi tiết hợp đồng cũ");
                return false;
            }
            
            // Thêm chi tiết mới
            for (ChiTietHD ct : hd.getDanhSachXeThue()) {
                // Kiểm tra dữ liệu chi tiết hợp đồng
                if (ct.getNgayBatDau() == null || ct.getNgayKetThuc() == null) {
                    conn.rollback();
                    errorMessage.append("Vui lòng nhập đầy đủ thông tin ngày thuê xe");
                    return false;
                }
                
                if (ct.getNgayBatDau().after(ct.getNgayKetThuc())) {
                    conn.rollback();
                    errorMessage.append("Ngày bắt đầu không được sau ngày kết thúc");
                    return false;
                }
                
                // Thiết lập mã hợp đồng cho chi tiết
                ct.setMaHD(hd.getMaHD());
                
                try {
                    // Thêm chi tiết vào CTHD
                    if (!cthdDAO.addChiTietHD(ct)) {
                        conn.rollback();
                        errorMessage.append("Không thể thêm chi tiết hợp đồng cho xe " + ct.getTenXe());
                        return false;
                    }
                } catch (SQLException e) {
                    conn.rollback();
                    
                    String errorMsg = e.getMessage();
                    
                    // Phân tích thông báo lỗi từ trigger
                    if (errorMsg.contains("ORA-20006")) {
                        errorMessage.append("Xe " + ct.getTenXe() + " đã có trong hợp đồng khác trùng thời gian thuê");
                    } else if (errorMsg.contains("ORA-20018")) {
                        errorMessage.append("Xe " + ct.getTenXe() + " có lịch bảo dưỡng trong thời gian thuê");
                    } else if (errorMsg.contains("ORA-20005")) {
                        errorMessage.append("Xe " + ct.getTenXe() + " không ở trạng thái 'Sẵn sàng' nên không thể thuê ngay");
                    } else {
                        errorMessage.append("Lỗi khi thêm chi tiết: " + errorMsg);
                    }
                    
                    return false;
                }
            }
            
            conn.commit();
            pstmt.close();
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            String errorMsg = e.getMessage();
            errorMessage.append("Lỗi cập nhật hợp đồng: " + errorMsg);
            
            e.printStackTrace();
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
    
    // Phiên bản đơn giản hơn của phương thức updateHopDong
    public boolean updateHopDong(HopDong hd) {
        StringBuilder errorMessage = new StringBuilder();
        return updateHopDong(hd, errorMessage);
    }
    
    public boolean deleteHopDong(String maHD, StringBuilder errorMessage) {
        try {
            checkConnection();
            
            if (maHD == null || maHD.isEmpty()) {
                errorMessage.append("Mã hợp đồng không hợp lệ");
                return false;
            }
            
            conn.setAutoCommit(false);
            
            // Kiểm tra hợp đồng có đang trong trạng thái 'Đang thuê' không
            String checkSql = "SELECT TRANGTHAI FROM HOPDONG WHERE MAHD = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, maHD);
            
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && "Đang thuê".equals(rs.getString("TRANGTHAI"))) {
                conn.rollback();
                errorMessage.append("Không thể xóa hợp đồng đang trong trạng thái 'Đang thuê'");
                return false;
            }
            
            rs.close();
            checkStmt.close();
            
            // Xóa chi tiết hợp đồng trước
            ChiTietHDDao cthdDAO = new ChiTietHDDao();
            try {
                cthdDAO.deleteChiTietHDByMaHD(maHD);
            } catch (SQLException e) {
                conn.rollback();
                errorMessage.append("Không thể xóa chi tiết hợp đồng");
                return false;
            }
            
            // Sau đó xóa hợp đồng
            String sqlHD = "DELETE FROM HOPDONG WHERE MAHD = ?";
            PreparedStatement pstmtHD = conn.prepareStatement(sqlHD);
            pstmtHD.setString(1, maHD);
            int rows = pstmtHD.executeUpdate();
            
            if (rows == 0) {
                conn.rollback();
                errorMessage.append("Không tìm thấy hợp đồng để xóa");
                return false;
            }
            
            conn.commit();
            pstmtHD.close();
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            String errorMsg = e.getMessage();
            errorMessage.append("Lỗi xóa hợp đồng: " + errorMsg);
            
            e.printStackTrace();
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
    
    // Phiên bản đơn giản hơn của phương thức deleteHopDong
    public boolean deleteHopDong(String maHD) {
        StringBuilder errorMessage = new StringBuilder();
        return deleteHopDong(maHD, errorMessage);
    }
    
    public List<HopDong> searchHopDong(String keyword, String trangThai) {
        List<HopDong> danhSachHD = new ArrayList<>();
        
        try {
            checkConnection();
            
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT h.*, k.TENKH, n.TENNV FROM HOPDONG h ");
            sqlBuilder.append("LEFT JOIN KHACHHANG k ON h.MAKH = k.MAKH ");
            sqlBuilder.append("LEFT JOIN NHANVIEN n ON h.MANV = n.MANV WHERE 1=1 ");
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                sqlBuilder.append("AND (UPPER(h.MAHD) LIKE ? OR UPPER(k.TENKH) LIKE ? OR UPPER(h.DIACHIGIAO) LIKE ?) ");
            }
            
            if (trangThai != null && !trangThai.equals("Tất cả")) {
                sqlBuilder.append("AND h.TRANGTHAI = ? ");
            }
            
            sqlBuilder.append("ORDER BY h.NGAYLAP DESC");
            
            PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString());
            
            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchKeyword = "%" + keyword.toUpperCase() + "%";
                pstmt.setString(paramIndex++, searchKeyword);
                pstmt.setString(paramIndex++, searchKeyword);
                pstmt.setString(paramIndex++, searchKeyword);
            }
            
            if (trangThai != null && !trangThai.equals("Tất cả")) {
                pstmt.setString(paramIndex, trangThai);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                HopDong hd = new HopDong();
                hd.setMaHD(rs.getString("MAHD"));
                hd.setMaKH(rs.getString("MAKH"));
                hd.setMaNV(rs.getString("MANV"));
                hd.setNgayLap(rs.getDate("NGAYLAP"));
                hd.setDiaChiGiao(rs.getString("DIACHIGIAO"));
                hd.setTongTien(rs.getDouble("TONGTIEN"));
                hd.setTrangThai(rs.getString("TRANGTHAI"));
                hd.setTenKH(rs.getString("TENKH"));
                hd.setTenNV(rs.getString("TENNV"));
                
                danhSachHD.add(hd);
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return danhSachHD;
    }
}