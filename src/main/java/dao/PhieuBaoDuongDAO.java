package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.ChiTietBaoDuong;
import model.KhachHang;
import model.NhanVien;
import model.PhieuBaoDuong;
import model.Xe;
import util.DatabaseUtil;

public class PhieuBaoDuongDAO {

    private XeDAO xeDAO;
    private KhachHangDAO khachHangDAO;
    private NhanVienDAO nhanVienDAO;
    // Transaction management fields
        private boolean reportViewLocked = false;
        private Connection lockedConnection = null;
        private int isolationLevel = Connection.TRANSACTION_READ_COMMITTED;

        // Transaction-aware connection getter
        private Connection getValidConnection() throws SQLException {
            if (reportViewLocked && lockedConnection != null && !lockedConnection.isClosed()) {
                System.out.println("==== REUSING EXISTING CONNECTION: " + lockedConnection.hashCode() + " ====");
                System.out.println("==== WITH ISOLATION LEVEL: " +
                    (lockedConnection.getTransactionIsolation() == Connection.TRANSACTION_SERIALIZABLE ?
                    "SERIALIZABLE" : "READ_COMMITTED") + " ====");
                return lockedConnection;
            } else {
                Connection conn = DatabaseUtil.getConnection();
                if (reportViewLocked) {
                    conn.setAutoCommit(false);
                    conn.setTransactionIsolation(isolationLevel);
                    System.out.println("==== CREATED NEW CONNECTION: " + conn.hashCode() + " ====");
                    System.out.println("==== SET ISOLATION LEVEL: " +
                        (isolationLevel == Connection.TRANSACTION_SERIALIZABLE ?
                        "SERIALIZABLE" : "READ_COMMITTED") + " ====");
                    lockedConnection = conn;
                }
                return conn;
            }
        }
    public PhieuBaoDuongDAO() {

        xeDAO = new XeDAO();
        khachHangDAO = new KhachHangDAO();
        nhanVienDAO = new NhanVienDAO();
    }

    public List<PhieuBaoDuong> getAllPhieuBaoDuong() {
        List<PhieuBaoDuong> danhsachpbd = new ArrayList<>();
        String sql = "SELECT * FROM PHIEUBAODUONG";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
                try {
            conn = getValidConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                PhieuBaoDuong pbd = new PhieuBaoDuong();
                pbd.setMaBD(rs.getString("MaBD"));
                pbd.setMaXe(rs.getString("MaXe"));
                pbd.setMaKH(rs.getString("MaKH"));
                pbd.setNgayBD(new Date(rs.getDate("NgayBD").getTime()));
                pbd.setMaNV(rs.getString("MaNV"));
                pbd.setLoaiBD(rs.getString("LoaiBD"));
                pbd.setTongTienBD(rs.getDouble("TongTienBD"));
                danhsachpbd.add(pbd);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving maintenance records: " + e.getMessage());
        }finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return danhsachpbd;
    }
public PhieuBaoDuong getPhieuBaoDuongByMaBD(String maBD) {
        String sql = "SELECT * FROM PHIEUBAODUONG WHERE MaBD = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getValidConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maBD);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                PhieuBaoDuong pbd = new PhieuBaoDuong();
                pbd.setMaBD(rs.getString("MaBD"));
                pbd.setMaXe(rs.getString("MaXe"));
                pbd.setMaKH(rs.getString("MaKH"));
                pbd.setNgayBD(new Date(rs.getDate("NgayBD").getTime()));
                pbd.setMaNV(rs.getString("MaNV"));
                pbd.setLoaiBD(rs.getString("LoaiBD"));
                pbd.setTongTienBD(rs.getDouble("TongTienBD"));

                Xe xe = xeDAO.getXeByMa(pbd.getMaXe());
                pbd.setXe(xe);

                if (pbd.getMaKH() != null) {
                    KhachHang kh = khachHangDAO.getKhachHangByMa(pbd.getMaKH());
                    pbd.setKhachHang(kh);
                }

                NhanVien nv = nhanVienDAO.getNhanVienByMa(pbd.getMaNV());
                pbd.setNhanVien(nv);

                return pbd;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving maintenance record by MaBD: " + e.getMessage());
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

public List<PhieuBaoDuong> getPhieuBaoDuongByMaXe(String maXe) {
        List<PhieuBaoDuong> phieuBaoDuongs = new ArrayList<>();
        String sql = "SELECT * FROM PHIEUBAODUONG WHERE MaXe = ? ORDER BY NgayBD DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getValidConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maXe);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                PhieuBaoDuong pbd = new PhieuBaoDuong();
                pbd.setMaBD(rs.getString("MaBD"));
                pbd.setMaXe(rs.getString("MaXe"));
                pbd.setMaKH(rs.getString("MaKH"));
                pbd.setNgayBD(new Date(rs.getDate("NgayBD").getTime()));
                pbd.setMaNV(rs.getString("MaNV"));
                pbd.setLoaiBD(rs.getString("LoaiBD"));
                pbd.setTongTienBD(rs.getDouble("TongTienBD"));

                Xe xe = xeDAO.getXeByMa(pbd.getMaXe());
                pbd.setXe(xe);

                if (pbd.getMaKH() != null) {
                    KhachHang kh = khachHangDAO.getKhachHangByMa(pbd.getMaKH());
                    pbd.setKhachHang(kh);
                }

                NhanVien nv = nhanVienDAO.getNhanVienByMa(pbd.getMaNV());
                pbd.setNhanVien(nv);

                phieuBaoDuongs.add(pbd);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving maintenance record by MaXe: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return phieuBaoDuongs;
    }

public List<PhieuBaoDuong> getPhieuBaoDuongByMaKhachHang(String maKH) {
        List<PhieuBaoDuong> phieuBaoDuongs = new ArrayList<>();
        String sql = "SELECT * FROM PHIEUBAODUONG WHERE MaKH = ? ORDER BY NgayBD DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getValidConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maKH);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                PhieuBaoDuong pbd = new PhieuBaoDuong();
                pbd.setMaBD(rs.getString("MaBD"));
                pbd.setMaXe(rs.getString("MaXe"));
                pbd.setMaKH(rs.getString("MaKH"));
                pbd.setNgayBD(new Date(rs.getDate("NgayBD").getTime()));
                pbd.setMaNV(rs.getString("MaNV"));
                pbd.setLoaiBD(rs.getString("LoaiBD"));
                pbd.setTongTienBD(rs.getDouble("TongTienBD"));

                Xe xe = xeDAO.getXeByMa(pbd.getMaXe());
                pbd.setXe(xe);

                if (pbd.getMaKH() != null) {
                    KhachHang kh = khachHangDAO.getKhachHangByMa(pbd.getMaKH());
                    pbd.setKhachHang(kh);
                }

                NhanVien nv = nhanVienDAO.getNhanVienByMa(pbd.getMaNV());
                pbd.setNhanVien(nv);

                phieuBaoDuongs.add(pbd);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving maintenance records by customer: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return phieuBaoDuongs;
    }

    // ...existing code...

    public boolean addPhieuBaoDuong(PhieuBaoDuong phieuBaoDuong) {
        String sql = "INSERT INTO PHIEUBAODUONG (MaBD, MaXe, MaKH, NgayBD, MaNV, LoaiBD, TongTienBD) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getValidConnection();
            conn.setAutoCommit(false);  // Tắt tự động commit để chủ động quản lý giao dịch

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, phieuBaoDuong.getMaBD());
            stmt.setString(2, phieuBaoDuong.getMaXe());
            stmt.setString(3, phieuBaoDuong.getMaKH());
            stmt.setDate(4, new java.sql.Date(phieuBaoDuong.getNgayBD().getTime()));
            stmt.setString(5, phieuBaoDuong.getMaNV());
            stmt.setString(6, phieuBaoDuong.getLoaiBD());
            stmt.setDouble(7, phieuBaoDuong.getTongTienBD());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                conn.commit(); // Thành công → commit
                return true;
            } else {
                conn.rollback(); // Không có dòng nào bị ảnh hưởng → rollback
                return false;
            }
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("Error adding maintenance record: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null && !reportViewLocked) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updatePhieuBaoDuong(PhieuBaoDuong phieuBaoDuong) {
        String sql = "UPDATE PHIEUBAODUONG SET MaXe = ?, MaKH = ?, NgayBD = ?, MaNV = ?, LoaiBD = ?, TongTienBD = ? WHERE MaBD = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getValidConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, phieuBaoDuong.getMaXe());
            stmt.setString(2, phieuBaoDuong.getMaKH());
            stmt.setDate(3, new java.sql.Date(phieuBaoDuong.getNgayBD().getTime()));
            stmt.setString(4, phieuBaoDuong.getMaNV());
            stmt.setString(5, phieuBaoDuong.getLoaiBD());
            stmt.setDouble(6, phieuBaoDuong.getTongTienBD());
            stmt.setString(7, phieuBaoDuong.getMaBD());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating maintenance record: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean deletePhieuBaoDuong(String maBD) {
        String sqlDeleteCT = "DELETE FROM CHITIETBAODUONG WHERE MaBD = ?";
        String sqlDeletePhieu = "DELETE FROM PHIEUBAODUONG WHERE MaBD = ?";
        Connection conn = null;
        PreparedStatement psCT = null;
        PreparedStatement psPhieu = null;
        try {
            conn = getValidConnection();
            conn.setAutoCommit(false);
            psCT = conn.prepareStatement(sqlDeleteCT);
            psCT.setString(1, maBD);
            psCT.executeUpdate();

            psPhieu = conn.prepareStatement(sqlDeletePhieu);
            psPhieu.setString(1, maBD);
            int rowsAffected = psPhieu.executeUpdate();

            conn.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("Error deleting maintenance record: " + e.getMessage());
            return false;
        } finally {
            try {
                if (psCT != null) psCT.close();
                if (psPhieu != null) psPhieu.close();
                if (conn != null && !reportViewLocked) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<PhieuBaoDuong> searchPhieuBaoDuong(String keyword) {
        List<PhieuBaoDuong> list = new ArrayList<>();
        String sql = "SELECT p.*, x.TenXe, x.BienSo, k.HoTen as TenKH, n.HoTen as TenNV " +
                     "FROM PHIEUBAODUONG p " +
                     "JOIN XE x ON p.MaXe = x.MaXe " +
                     "LEFT JOIN KHACHHANG k ON p.MaKH = k.MaKH " +
                     "JOIN NHANVIEN n ON p.MaNV = n.MaNV " +
                     "WHERE UPPER(x.TenXe) LIKE UPPER(?) OR UPPER(x.BienSo) LIKE UPPER(?) " +
                     "OR UPPER(k.HoTen) LIKE UPPER(?) OR UPPER(p.MaBD) LIKE UPPER(?) " +
                     "ORDER BY p.NgayBD DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getValidConnection();
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                PhieuBaoDuong phieu = new PhieuBaoDuong();
                phieu.setMaBD(rs.getString("MaBD"));
                phieu.setMaXe(rs.getString("MaXe"));
                phieu.setMaKH(rs.getString("MaKH"));
                phieu.setNgayBD(rs.getDate("NgayBD"));
                phieu.setMaNV(rs.getString("MaNV"));
                phieu.setLoaiBD(rs.getString("LoaiBD"));
                phieu.setTongTienBD(rs.getDouble("TongTienBD"));
                list.add(phieu);
            }
        } catch (SQLException e) {
            System.err.println("Error searching maintenance records: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public boolean isCarInContract(String maXe, Date ngayBD) {
        String sql = "SELECT COUNT(*) FROM CTHD " +
                     "WHERE MaXe = ? AND ? BETWEEN NgayBatDau AND NgayKetThuc";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getValidConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maXe);
            pstmt.setDate(2, new java.sql.Date(ngayBD.getTime()));

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if car is in contract: " + e.getMessage());
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

// ...existing code...
// ...existing code...

public List<PhieuBaoDuong> searchPhieuBaoDuong(String keyword, String loaiBD) {
    List<PhieuBaoDuong> list = new ArrayList<>();
    String sql = "SELECT p.*, x.TenXe, x.BienSo, k.HoTen as TenKH, n.HoTen as TenNV " +
                 "FROM PHIEUBAODUONG p " +
                 "JOIN XE x ON p.MaXe = x.MaXe " +
                 "LEFT JOIN KHACHHANG k ON p.MaKH = k.MaKH " +
                 "JOIN NHANVIEN n ON p.MaNV = n.MaNV " +
                 "WHERE (UPPER(x.TenXe) LIKE UPPER(?) OR UPPER(x.BienSo) LIKE UPPER(?) " +
                 "OR UPPER(k.HoTen) LIKE UPPER(?) OR UPPER(p.MaBD) LIKE UPPER(?)) " +
                 (loaiBD != null && !loaiBD.trim().isEmpty() ? "AND UPPER(p.LoaiBD) = UPPER(?) " : "") +
                 "ORDER BY p.NgayBD DESC";
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
        conn = getValidConnection();
        pstmt = conn.prepareStatement(sql);
        String searchPattern = "%" + keyword + "%";
        pstmt.setString(1, searchPattern);
        pstmt.setString(2, searchPattern);
        pstmt.setString(3, searchPattern);
        pstmt.setString(4, searchPattern);
        if (loaiBD != null && !loaiBD.trim().isEmpty()) {
            pstmt.setString(5, loaiBD);
        }
        rs = pstmt.executeQuery();
        while (rs.next()) {
            PhieuBaoDuong phieu = new PhieuBaoDuong();
            phieu.setMaBD(rs.getString("MaBD"));
            phieu.setMaXe(rs.getString("MaXe"));
            phieu.setMaKH(rs.getString("MaKH"));
            phieu.setNgayBD(rs.getDate("NgayBD"));
            phieu.setMaNV(rs.getString("MaNV"));
            phieu.setLoaiBD(rs.getString("LoaiBD"));
            phieu.setTongTienBD(rs.getDouble("TongTienBD"));
            // ... có thể set thêm các thông tin khác nếu cần
            list.add(phieu);
        }
    } catch (SQLException e) {
        System.err.println("Error searching maintenance records: " + e.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return list;
}

public void updateTongTienPhieuBaoDuong(String maBD, double tongTien) {
    String sql = "UPDATE PhieuBaoDuong SET TongTienBD = ? WHERE MaBD = ?";
    Connection conn = null;
    PreparedStatement ps = null;
    try {
        conn = getValidConnection();
        ps = conn.prepareStatement(sql);
        ps.setDouble(1, tongTien);
        ps.setString(2, maBD);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public String addPhieuBaoDuongFull(PhieuBaoDuong phieu, List<ChiTietBaoDuong> chiTietList) throws SQLException {
    String sqlGetSeq = "SELECT SEQ_PHIEUBAODUONG.NEXTVAL FROM DUAL";
    String sqlInsertPhieu = "INSERT INTO PhieuBaoDuong (MaBD, MaXe, MaKH, NgayBD, MaNV, LoaiBD, TongTienBD) VALUES (?, ?, ?, ?, ?, ?, ?)";
    String sqlInsertCT = "INSERT INTO CHITIETBAODUONG (MaBD, MaDV, SoLuong) VALUES (?, ?, ?)";
    Connection conn = null;
    PreparedStatement psSeq = null, psPhieu = null, psCT = null;
    ResultSet rs = null;
    try {
        conn = getValidConnection();
        conn.setAutoCommit(false);
        // Lấy mã mới
        psSeq = conn.prepareStatement(sqlGetSeq);
        rs = psSeq.executeQuery();
        String maBD = null;
        if (rs.next()) {
            int soTuSeq = rs.getInt(1);
            maBD = String.format("BD%03d", soTuSeq);
        }
        if (maBD == null) throw new SQLException("Không lấy được mã phiếu!");
        // Insert phiếu
        psPhieu = conn.prepareStatement(sqlInsertPhieu);
        psPhieu.setString(1, maBD);
        psPhieu.setString(2, phieu.getMaXe());
        if ("Định Kỳ".equals(phieu.getLoaiBD())) {
            psPhieu.setNull(3, java.sql.Types.VARCHAR);
        } else {
            psPhieu.setString(3, phieu.getMaKH());
        }
        psPhieu.setDate(4, new java.sql.Date(phieu.getNgayBD().getTime()));
        psPhieu.setString(5, phieu.getMaNV());
        psPhieu.setString(6, phieu.getLoaiBD());
        psPhieu.setDouble(7, 0);
        psPhieu.executeUpdate();
        // Insert chi tiết
        psCT = conn.prepareStatement(sqlInsertCT);
        for (ChiTietBaoDuong ct : chiTietList) {
            psCT.setString(1, maBD);
            psCT.setString(2, ct.getMaDV());
            psCT.setInt(3, ct.getSoLuong());
            psCT.addBatch();
        }
        psCT.executeBatch();
        conn.commit();
        return maBD;
    } catch (Exception e) {
        if (conn != null) conn.rollback();
        throw e;
    } finally {
        try {
            if (rs != null) rs.close();
            if (psSeq != null) psSeq.close();
            if (psPhieu != null) psPhieu.close();
            if (psCT != null) psCT.close();
            if (conn != null && !reportViewLocked) conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public String updatePhieuBaoDuongFull(PhieuBaoDuong phieu, List<ChiTietBaoDuong> chiTietList) throws SQLException {
    Connection conn = null;
    try {
        conn = getValidConnection();
        conn.setAutoCommit(false);

        // Update phiếu
        String sqlUpdatePhieu = "UPDATE PhieuBaoDuong SET MaXe=?, MaKH=?, NgayBD=?, MaNV=?, LoaiBD=? WHERE MaBD=?";
        try (PreparedStatement psPhieu = conn.prepareStatement(sqlUpdatePhieu)) {
            psPhieu.setString(1, phieu.getMaXe());
            psPhieu.setString(2, phieu.getMaKH());
            psPhieu.setDate(3, new java.sql.Date(phieu.getNgayBD().getTime()));
            psPhieu.setString(4, phieu.getMaNV());
            psPhieu.setString(5, phieu.getLoaiBD());
         //   psPhieu.setDouble(6, phieu.getTongTienBD());
            psPhieu.setString(6, phieu.getMaBD());
            psPhieu.executeUpdate();
        }

        String sqlDeleteCT = "DELETE FROM CHITIETBAODUONG WHERE MaBD=?";
        try (PreparedStatement psDel = conn.prepareStatement(sqlDeleteCT)) {
            psDel.setString(1, phieu.getMaBD());
            psDel.executeUpdate();
        }

        String sqlInsertCT = "INSERT INTO CHITIETBAODUONG (MaBD, MaDV, SoLuong) VALUES (?, ?, ?)";
        try (PreparedStatement psCT = conn.prepareStatement(sqlInsertCT)) {
            for (ChiTietBaoDuong ct : chiTietList) {
                psCT.setString(1, phieu.getMaBD());
                psCT.setString(2, ct.getMaDV());
                psCT.setInt(3, ct.getSoLuong());
                psCT.addBatch();
            }
            psCT.executeBatch();
        }

        conn.commit();
        return "Cập nhật phiếu bảo dưỡng thành công";
    } catch (Exception e) {
        if (conn != null) conn.rollback();
        throw e;
    } finally {
        try {
            if (conn != null && !reportViewLocked) conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public boolean updatePhieuBaoDuongThongTinChung(PhieuBaoDuong phieu) {
    String sql = "UPDATE PhieuBaoDuong SET MaXe=?, MaKH=?, NgayBD=?, MaNV=?, LoaiBD=? WHERE MaBD=?";
    Connection conn = null;
    PreparedStatement ps = null;
    try {
        conn = getValidConnection();
        ps = conn.prepareStatement(sql);
        ps.setString(1, phieu.getMaXe());
        ps.setString(2, phieu.getMaKH());
        ps.setDate(3, new java.sql.Date(phieu.getNgayBD().getTime()));
        ps.setString(4, phieu.getMaNV());
        ps.setString(5, phieu.getLoaiBD());
        ps.setString(6, phieu.getMaBD());
        int rows = ps.executeUpdate();
        return rows > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    } finally {
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public List<PhieuBaoDuong> getPhieuBaoDuongByKhachHangAndLoai(String maKH, String loaiBD) {
    System.out.println("Truy vấn phiếu BD: MaKH=" + maKH + ", LoaiBD=" + loaiBD);
    List<PhieuBaoDuong> phieuBaoDuongs = new ArrayList<>();
    List<PhieuBaoDuong> tempList = new ArrayList<>();
    String sql = "SELECT * FROM PHIEUBAODUONG WHERE MaKH = ? AND LoaiBD = ? ORDER BY NgayBD DESC";
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
        conn = getValidConnection();
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, maKH);
        pstmt.setString(2, loaiBD);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            PhieuBaoDuong pbd = new PhieuBaoDuong();
            pbd.setMaBD(rs.getString("MaBD"));
            pbd.setMaXe(rs.getString("MaXe"));
            pbd.setMaKH(rs.getString("MaKH"));
            pbd.setNgayBD(new Date(rs.getDate("NgayBD").getTime()));
            pbd.setMaNV(rs.getString("MaNV"));
            pbd.setLoaiBD(rs.getString("LoaiBD"));
            pbd.setTongTienBD(rs.getDouble("TongTienBD"));
            tempList.add(pbd); // chỉ lấy dữ liệu cơ bản
        }
        // Sau khi đã đóng rs và conn, mới gọi các DAO phụ
        for (PhieuBaoDuong pbd : tempList) {
            Xe xe = xeDAO.getXeByMa(pbd.getMaXe());
            pbd.setXe(xe);
            if (pbd.getMaKH() != null) {
                KhachHang kh = khachHangDAO.getKhachHangByMa(pbd.getMaKH());
                pbd.setKhachHang(kh);
            }
            NhanVien nv = nhanVienDAO.getNhanVienByMa(pbd.getMaNV());
            pbd.setNhanVien(nv);
            phieuBaoDuongs.add(pbd);
        }
    } catch (SQLException e) {
        System.err.println("Error retrieving maintenance records by customer and type: " + e.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return phieuBaoDuongs;
}
}
