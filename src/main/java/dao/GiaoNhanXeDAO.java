package dao;

import model.GiaoNhanXe;
import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GiaoNhanXeDAO {

    private Connection getValidConnection() throws SQLException {
        Connection conn = DatabaseUtil.getConnection();
        if (conn == null || conn.isClosed() || !conn.isValid(2)) {
            System.out.println("Connection invalidated or closed, attempting to reconnect...");
        //    DatabaseUtil.reconnect();
            conn = DatabaseUtil.getConnection();
            if (conn == null || conn.isClosed() || !conn.isValid(2)) {
                throw new SQLException("Failed to establish a valid database connection.");
            }
            System.out.println("Reconnection successful.");
        }
        return conn;
    }

    public List<GiaoNhanXe> getAllGiaoNhanXe() {
        List<GiaoNhanXe> danhSachGiaoNhan = new ArrayList<>();
        String sql = "SELECT * FROM GIAONHANXE ORDER BY MaGiaoNhan";
        try (Connection conn = getValidConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                GiaoNhanXe gn = new GiaoNhanXe();
                gn.setMaGiaoNhan(rs.getString("MaGiaoNhan"));
                gn.setMaHD(rs.getString("MaHD"));
                gn.setMaXe(rs.getString("MaXe"));
                gn.setMaNV(rs.getString("MaNV"));
                gn.setTrangThaiXe(rs.getString("TrangThaiXe"));
                gn.setGhiChu(rs.getString("GhiChu"));
                gn.setTrangThaiGN(rs.getString("TrangThaiGN"));
                danhSachGiaoNhan.add(gn);
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllGiaoNhanXe: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSachGiaoNhan;
    }

    public GiaoNhanXe getGiaoNhanXeByMa(String maGiaoNhan) {
        GiaoNhanXe gn = null;
        String sql = "SELECT * FROM GIAONHANXE WHERE MaGiaoNhan = ?";
        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maGiaoNhan);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    gn = new GiaoNhanXe();
                    gn.setMaGiaoNhan(rs.getString("MaGiaoNhan"));
                    gn.setMaHD(rs.getString("MaHD"));
                    gn.setMaXe(rs.getString("MaXe"));
                    gn.setMaNV(rs.getString("MaNV"));
                    gn.setTrangThaiXe(rs.getString("TrangThaiXe"));
                    gn.setGhiChu(rs.getString("GhiChu"));
                    gn.setTrangThaiGN(rs.getString("TrangThaiGN"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getGiaoNhanXeByMa: " + e.getMessage());
            e.printStackTrace();
        }
        return gn;
    }

    public String addGiaoNhanXe(GiaoNhanXe gn) {
        String newMaGiaoNhan = null;
        String sqlInsert = "INSERT INTO GIAONHANXE (MaHD, MaXe, MaNV, TrangThaiXe, GhiChu, TrangThaiGN) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlGetId = "SELECT MaGiaoNhan FROM GIAONHANXE WHERE MaHD = ? AND MaXe = ? AND MaNV = ? AND TrangThaiGN = ? ORDER BY MaGiaoNhan DESC FETCH FIRST 1 ROW ONLY";
        Connection conn = null;

        try {
            conn = getValidConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                pstmtInsert.setString(1, gn.getMaHD());
                pstmtInsert.setString(2, gn.getMaXe());
                pstmtInsert.setString(3, gn.getMaNV());
                pstmtInsert.setString(4, gn.getTrangThaiXe());
                pstmtInsert.setString(5, gn.getGhiChu());
                pstmtInsert.setString(6, gn.getTrangThaiGN());
                int rowsAffected = pstmtInsert.executeUpdate();

                if (rowsAffected > 0) {
                    try (PreparedStatement pstmtGetId = conn.prepareStatement(sqlGetId)) {
                        pstmtGetId.setString(1, gn.getMaHD());
                        pstmtGetId.setString(2, gn.getMaXe());
                        pstmtGetId.setString(3, gn.getMaNV());
                        pstmtGetId.setString(4, gn.getTrangThaiGN());
                        try (ResultSet rs = pstmtGetId.executeQuery()) {
                            if (rs.next()) {
                                newMaGiaoNhan = rs.getString("MaGiaoNhan");
                            }
                        }
                    }
                }
            }

            if (newMaGiaoNhan != null) {
                conn.commit();
            } else {
                conn.rollback();
                System.err.println("Failed to retrieve new MaGiaoNhan, rolling back.");
            }

        } catch (SQLException e) {
            System.err.println("Error in addGiaoNhanXe: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error during rollback: " + ex.getMessage());
                }
            }
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error restoring auto-commit: " + e.getMessage());
                }
            }
        }
        return newMaGiaoNhan;
    }

    public boolean updateGiaoNhanXe(GiaoNhanXe gn) {
        String sql = "UPDATE GIAONHANXE SET MaHD = ?, MaXe = ?, MaNV = ?, TrangThaiXe = ?, GhiChu = ?, TrangThaiGN = ? WHERE MaGiaoNhan = ?";
        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gn.getMaHD());
            pstmt.setString(2, gn.getMaXe());
            pstmt.setString(3, gn.getMaNV());
            pstmt.setString(4, gn.getTrangThaiXe());
            pstmt.setString(5, gn.getGhiChu());
            pstmt.setString(6, gn.getTrangThaiGN());
            pstmt.setString(7, gn.getMaGiaoNhan());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error in updateGiaoNhanXe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteGiaoNhanXe(String maGiaoNhan) {
        String sql = "DELETE FROM GIAONHANXE WHERE MaGiaoNhan = ?";
        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maGiaoNhan);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error in deleteGiaoNhanXe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<GiaoNhanXe> searchGiaoNhanXe(String keyword) {
        List<GiaoNhanXe> danhSachGiaoNhan = new ArrayList<>();
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllGiaoNhanXe();
        }
        
        // Tách keyword thành các từ riêng biệt
        String[] keywords = keyword.trim().split("\\s+");
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM GIAONHANXE WHERE ");
        
        List<String> conditions = new ArrayList<>();
        for (int i = 0; i < keywords.length; i++) {
            conditions.add("(UPPER(MaGiaoNhan) LIKE ? OR UPPER(MaHD) LIKE ? OR UPPER(MaXe) LIKE ? OR UPPER(MaNV) LIKE ? OR UPPER(TrangThaiXe) LIKE ? OR UPPER(TrangThaiGN) LIKE ?)");
        }
        
        sqlBuilder.append(String.join(" AND ", conditions));
        
        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {
            
            int paramIndex = 1;
            for (String word : keywords) {
                String searchWord = "%" + word.toUpperCase() + "%";
                pstmt.setString(paramIndex++, searchWord); // MaGiaoNhan
                pstmt.setString(paramIndex++, searchWord); // MaHD
                pstmt.setString(paramIndex++, searchWord); // MaXe
                pstmt.setString(paramIndex++, searchWord); // MaNV
                pstmt.setString(paramIndex++, searchWord); // TrangThaiXe
                pstmt.setString(paramIndex++, searchWord); // TrangThaiGN
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    GiaoNhanXe gn = new GiaoNhanXe();
                    gn.setMaGiaoNhan(rs.getString("MaGiaoNhan"));
                    gn.setMaHD(rs.getString("MaHD"));
                    gn.setMaXe(rs.getString("MaXe"));
                    gn.setMaNV(rs.getString("MaNV"));
                    gn.setTrangThaiXe(rs.getString("TrangThaiXe"));
                    gn.setGhiChu(rs.getString("GhiChu"));
                    gn.setTrangThaiGN(rs.getString("TrangThaiGN"));
                    danhSachGiaoNhan.add(gn);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in searchGiaoNhanXe: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSachGiaoNhan;
    }
    
    public boolean existsGiaoNhanXe(String maGiaoNhan) {
        String sql = "SELECT COUNT(*) FROM GIAONHANXE WHERE MaGiaoNhan = ?";
        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maGiaoNhan);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in existsGiaoNhanXe: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}