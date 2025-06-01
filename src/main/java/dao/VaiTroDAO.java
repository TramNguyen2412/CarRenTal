package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.VaiTro;
import util.DatabaseUtil;

public class VaiTroDAO {
    
    // Phương thức kiểm tra kết nối và khôi phục nếu cần
    private Connection getValidConnection() throws SQLException {
        Connection conn = DatabaseUtil.getConnection();
        
        // Kiểm tra kết nối còn hợp lệ không
//        if (!conn.isValid(2)) { // timeout 2 giây
//            System.out.println("Connection invalidated, reconnecting...");
//            DatabaseUtil.reconnect();
//            conn = DatabaseUtil.getConnection();
//        }
//        
        return conn;
    }
    
    public List<VaiTro> getAllVaiTro() {
        List<VaiTro> list = new ArrayList<>();
        String sql = "SELECT * FROM VAITRO";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                VaiTro vt = new VaiTro();
                vt.setMaVaiTro(rs.getString("MaVaiTro"));
                vt.setTenVaiTro(rs.getString("TenVaiTro"));
                list.add(vt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in getAllVaiTro");
//                    DatabaseUtil.reconnect();
//                    return getAllVaiTro(); // Thử lại một lần
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
    
    public VaiTro getVaiTroById(String maVaiTro) {
        String sql = "SELECT * FROM VAITRO WHERE MaVaiTro = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getValidConnection();
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maVaiTro);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                VaiTro vt = new VaiTro();
                vt.setMaVaiTro(rs.getString("MaVaiTro"));
                vt.setTenVaiTro(rs.getString("TenVaiTro"));
                return vt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("Closed Connection")) {
//                try {
//                    System.out.println("Attempting to reconnect in getVaiTroById");
//                    DatabaseUtil.reconnect();
//                    return getVaiTroById(maVaiTro); // Thử lại một lần
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
}