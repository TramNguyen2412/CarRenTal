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
    
    public ChiTietBaoDuongDAO() {
        
        phieuBaoDuongDAO = new PhieuBaoDuongDAO();
        dichVuBDDAO = new DichVuBDDAO();
    }
    
    public List<ChiTietBaoDuong> getChiTietBaoDuongByPhieuBD(String maBD) {
        List<ChiTietBaoDuong> chiTietBaoDuongs = new ArrayList<>();
        String sql = "SELECT * FROM CHITIETBAODUONG WHERE MaBD = ?";
        
        try ( Connection conn = DatabaseUtil.getConnection();
              PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maBD);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ChiTietBaoDuong ctbd = new ChiTietBaoDuong();
                ctbd.setMaBD(rs.getString("MaBD"));
                ctbd.setMaDV(rs.getString("MaDV"));
                ctbd.setSoLuong(rs.getInt("SoLuong"));
                
                // Load phiếu bảo dưỡng và dịch vụ
                PhieuBaoDuong pbd = phieuBaoDuongDAO.getPhieuBaoDuongByMaBD(ctbd.getMaBD());
                DichVuBD dv = dichVuBDDAO.getDichVuBDByMaDV(ctbd.getMaDV());
                ctbd.setPhieuBaoDuong(pbd);
                ctbd.setDichVuBD(dv);
                
                chiTietBaoDuongs.add(ctbd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return chiTietBaoDuongs;
    }
    
    public boolean addChiTietBaoDuong(ChiTietBaoDuong chiTietBaoDuong) {
        String sql = "INSERT INTO CHITIETBAODUONG (MaBD, MaDV, SoLuong) VALUES (?, ?, ?)";
        
            try (Connection conn = DatabaseUtil.getConnection()) {
        conn.setAutoCommit(false);  // Tắt tự động commit để chủ động quản lý giao dịch

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, chiTietBaoDuong.getMaBD());
            stmt.setString(2, chiTietBaoDuong.getMaDV());
            stmt.setInt(3, chiTietBaoDuong.getSoLuong());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                conn.commit(); // Thành công → commit
                return true;
            } else {
                conn.rollback(); // Không có dòng nào bị ảnh hưởng → rollback
                return false;
            }
        } catch (SQLException e) {
            conn.rollback(); // Nếu lỗi trong khi thực thi → rollback
            throw e;
        } finally {
            conn.setAutoCommit(true); // Bật lại autoCommit sau khi xong
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
    }
    
    public boolean updateChiTietBaoDuong(ChiTietBaoDuong chiTietBaoDuong) {
        String sql = "UPDATE CHITIETBAODUONG SET SoLuong = ? WHERE MaBD = ? AND MaDV = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, chiTietBaoDuong.getSoLuong());
            stmt.setString(2, chiTietBaoDuong.getMaBD());
            stmt.setString(3, chiTietBaoDuong.getMaDV());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteChiTietBaoDuong(String maBD, String maDV) {
        String sql = "DELETE FROM CHITIETBAODUONG WHERE MaBD = ? AND MaDV = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, maBD);
            stmt.setString(2, maDV);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean isServiceUsedInMaintenance(String maDV) {
        String sql = "SELECT COUNT(*) FROM CHITIETBAODUONG WHERE MaDV = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maDV);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if service is used: " + e.getMessage());
        }
        
        return false;
    }
}