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


    public DichVuBD getDichVuBDByMaDV(String maDV) {
        String sql = "SELECT * FROM DICHVUBD WHERE MaDV = ?";
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maDV);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                DichVuBD dv = new DichVuBD();
                dv.setMaDV(rs.getString("MaDV"));
                dv.setTenDV(rs.getString("TenDV"));
                dv.setGiaDV(rs.getDouble("GiaDV"));
                return dv;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving service by ID: " + e.getMessage());
        }
        return null;
    }

    public List<DichVuBD> getAllDichVuBD() {
        List<DichVuBD> dichVuBDs = new ArrayList<>();
        String sql = "SELECT * FROM DICHVUBD";

        try ( Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                DichVuBD dv = new DichVuBD();
                dv.setMaDV(rs.getString("MaDV"));
                dv.setTenDV(rs.getString("TenDV"));
                dv.setGiaDV(rs.getDouble("GiaDV"));
                dichVuBDs.add(dv);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving services: " + e.getMessage());
        }

        return dichVuBDs;
    }

    public boolean addDichVuBD(DichVuBD dichVuBD) {
        try(Connection conn = DatabaseUtil.getConnection();
            // Use the stored procedure to add a new service
            CallableStatement cstmt = conn.prepareCall("{call sp_ThemDichVu(?, ?, ?)}");) { 
            cstmt.setString(1, dichVuBD.getTenDV());
            cstmt.setDouble(2, dichVuBD.getGiaDV());
            cstmt.registerOutParameter(3, Types.VARCHAR);
            
            cstmt.execute();
            
            String message = cstmt.getString(3);
            cstmt.close();
            
            if (message.startsWith("Thêm dịch vụ thành công")) {
                return true;
            } else {
                System.err.println("Error adding service: " + message);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error adding service: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateDichVuBD(DichVuBD dichVuBD) {
        String sql = "UPDATE DICHVUBD SET TenDV = ?, GiaDV = ? WHERE MaDV = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, dichVuBD.getTenDV());
            stmt.setDouble(2, dichVuBD.getGiaDV());
            stmt.setString(3, dichVuBD.getMaDV());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating service: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDichVuBD(String maDV) {
        String sql = "DELETE FROM DICHVUBD WHERE MaDV = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maDV);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting service: " + e.getMessage());
            return false;
        }
    }

public List<DichVuBD> searchDichVuBD(String keyword) {
        List<DichVuBD> list = new ArrayList<>();
        String sql = "SELECT * FROM DICHVUBD WHERE UPPER(TenDV) LIKE UPPER(?) ORDER BY TenDV";
        
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (keyword == null) keyword = "";
            pstmt.setString(1, "%" + keyword + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DichVuBD dv = new DichVuBD();
                    dv.setMaDV(rs.getString("MaDV"));
                    dv.setTenDV(rs.getString("TenDV"));
                    dv.setGiaDV(rs.getDouble("GiaDV"));
                    list.add(dv);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching services: " + e.getMessage());
        }
        
        return list;
    }
}
