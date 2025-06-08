package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ChiTietBaoDuong;
import model.DichVuBD;
import model.PhieuBaoDuong;
import util.DatabaseUtil;

public class ChiTietBaoDuongDAO {
    
    private PhieuBaoDuongDAO phieuBaoDuongDAO;
    private DichVuBDDAO dichVuBDDAO;

    // Thêm biến quản lý transaction nếu cần
    private boolean reportViewLocked = false;
    private Connection lockedConnection = null;
    private int isolationLevel = Connection.TRANSACTION_READ_COMMITTED;

    public ChiTietBaoDuongDAO() {
        phieuBaoDuongDAO = new PhieuBaoDuongDAO();
        dichVuBDDAO = new DichVuBDDAO();
    }

    // Thêm hàm getValidConnection
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

    public List<ChiTietBaoDuong> getChiTietBaoDuongByPhieuBD(String maBD) {
        List<ChiTietBaoDuong> chiTietBaoDuongs = new ArrayList<>();
        String sql = "SELECT * FROM CHITIETBAODUONG WHERE MaBD = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getValidConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, maBD);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ChiTietBaoDuong ctbd = new ChiTietBaoDuong();
                ctbd.setMaBD(rs.getString("MaBD"));
                ctbd.setMaDV(rs.getString("MaDV"));
                ctbd.setSoLuong(rs.getInt("SoLuong"));
                chiTietBaoDuongs.add(ctbd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                // Không đóng connection ở đây để giữ transaction nếu cần
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return chiTietBaoDuongs;
    }

    public boolean addChiTietBaoDuong(ChiTietBaoDuong chiTietBaoDuong) {
        String sql = "INSERT INTO CHITIETBAODUONG (MaBD, MaDV, SoLuong) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getValidConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, chiTietBaoDuong.getMaBD());
            stmt.setString(2, chiTietBaoDuong.getMaDV());
            stmt.setInt(3, chiTietBaoDuong.getSoLuong());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null && !reportViewLocked) conn.setAutoCommit(true);
                // Không đóng connection nếu đang giữ transaction
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updateChiTietBaoDuong(ChiTietBaoDuong chiTietBaoDuong) {
        String sql = "UPDATE CHITIETBAODUONG SET SoLuong = ? WHERE MaBD = ? AND MaDV = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getValidConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, chiTietBaoDuong.getSoLuong());
            stmt.setString(2, chiTietBaoDuong.getMaBD());
            stmt.setString(3, chiTietBaoDuong.getMaDV());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                // Không đóng connection ở đây để giữ transaction nếu cần
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean deleteChiTietBaoDuong(String maBD, String maDV) {
        String sql = "DELETE FROM CHITIETBAODUONG WHERE MaBD = ? AND MaDV = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getValidConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, maBD);
            stmt.setString(2, maDV);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                // Không đóng connection ở đây để giữ transaction nếu cần
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isServiceUsedInMaintenance(String maDV) {
        String sql = "SELECT COUNT(*) FROM CHITIETBAODUONG WHERE MaDV = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getValidConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maDV);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if service is used: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                // Không đóng connection ở đây để giữ transaction nếu cần
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void deleteAllChiTietBaoDuong(String maBD) {
        String sql = "DELETE FROM ChiTietBaoDuong WHERE MaBD = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getValidConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, maBD);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                // Không đóng connection ở đây để giữ transaction nếu cần
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}