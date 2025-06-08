package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.DichVuBD;
import util.DatabaseUtil;

public class DichVuBDDAO {

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

    public DichVuBD getDichVuBDByMaDV(String maDV) {
        String sql = "SELECT * FROM DICHVUBD WHERE MaDV = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getValidConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maDV);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                DichVuBD dv = new DichVuBD();
                dv.setMaDV(rs.getString("MaDV"));
                dv.setTenDV(rs.getString("TenDV"));
                dv.setGiaDV(rs.getDouble("GiaDV"));
                return dv;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving service by ID: " + e.getMessage());
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

    public List<DichVuBD> getAllDichVuBD() {
        List<DichVuBD> dichVuBDs = new ArrayList<>();
        String sql = "SELECT * FROM DICHVUBD";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getValidConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                DichVuBD dv = new DichVuBD();
                dv.setMaDV(rs.getString("MaDV"));
                dv.setTenDV(rs.getString("TenDV"));
                dv.setGiaDV(rs.getDouble("GiaDV"));
                dichVuBDs.add(dv);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving services: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dichVuBDs;
    }

    public boolean addDichVuBD(DichVuBD dichVuBD) {
        Connection conn = null;
        CallableStatement cstmt = null;
        try {
            conn = getValidConnection();
            cstmt = conn.prepareCall("{call sp_ThemDichVu(?, ?, ?)}");
            cstmt.setString(1, dichVuBD.getTenDV());
            cstmt.setDouble(2, dichVuBD.getGiaDV());
            cstmt.registerOutParameter(3, Types.VARCHAR);

            cstmt.execute();

            String message = cstmt.getString(3);

            if (message.startsWith("Thêm dịch vụ thành công")) {
                return true;
            } else {
                System.err.println("Error adding service: " + message);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error adding service: " + e.getMessage());
            return false;
        } finally {
            try {
                if (cstmt != null) cstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updateDichVuBD(DichVuBD dichVuBD) {
        String sql = "UPDATE DICHVUBD SET TenDV = ?, GiaDV = ? WHERE MaDV = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getValidConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, dichVuBD.getTenDV());
            stmt.setDouble(2, dichVuBD.getGiaDV());
            stmt.setString(3, dichVuBD.getMaDV());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating service: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean deleteDichVuBD(String maDV) {
        String sql = "DELETE FROM DICHVUBD WHERE MaDV = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getValidConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, maDV);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting service: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<DichVuBD> searchDichVuBD(String keyword) {
        List<DichVuBD> list = new ArrayList<>();
        String sql = "SELECT * FROM DICHVUBD WHERE UPPER(TenDV) LIKE UPPER(?) ORDER BY TenDV";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getValidConnection();
            pstmt = conn.prepareStatement(sql);
            if (keyword == null) keyword = "";
            pstmt.setString(1, "%" + keyword + "%");

            rs = pstmt.executeQuery();
            while (rs.next()) {
                DichVuBD dv = new DichVuBD();
                dv.setMaDV(rs.getString("MaDV"));
                dv.setTenDV(rs.getString("TenDV"));
                dv.setGiaDV(rs.getDouble("GiaDV"));
                list.add(dv);
            }
        } catch (SQLException e) {
            System.err.println("Error searching services: " + e.getMessage());
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
}