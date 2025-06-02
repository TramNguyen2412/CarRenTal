
package controller;

import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import dao.DanhGiaDAO;
import model.DanhGia;

public class DanhGiaController {
    private DanhGiaDAO danhGiaDAO;
    
    public DanhGiaController() {
        danhGiaDAO = new DanhGiaDAO();
    }
    
    // Lấy tất cả đánh giá
    public List<DanhGia> getAllDanhGia() {
        return danhGiaDAO.getAllDanhGia();
    }
    
    // Lấy đánh giá theo mã khách hàng
    public List<DanhGia> getDanhGiaByMaKH(String maKH) {
        return danhGiaDAO.getDanhGiaByMaKH(maKH);
    }
    
    // Lấy đánh giá theo mã đánh giá
    public DanhGia getDanhGiaByMaDG(String maDG) {
        return danhGiaDAO.getDanhGiaByMaDG(maDG);
    }
    
    // Thêm đánh giá mới
    public boolean addDanhGia(String maHD, int diemSo, String binhLuan) {
        try {
            // Tạo mã đánh giá mới
            String maDG = danhGiaDAO.generateMaDG();
            
            // Tạo đối tượng đánh giá mới
            DanhGia danhGia = new DanhGia();
            danhGia.setMaDG(maDG);
            danhGia.setMaHD(maHD);
            danhGia.setDiemSo(diemSo);
            danhGia.setBinhLuan(binhLuan);
            danhGia.setNgayDanhGia(new Date()); // Ngày hiện tại
            
            // Thêm vào cơ sở dữ liệu
            return danhGiaDAO.addDanhGia(danhGia);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm đánh giá: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật đánh giá
    public boolean updateDanhGia(String maDG, int diemSo, String binhLuan) {
        try {
            // Lấy đánh giá hiện tại
            DanhGia danhGia = danhGiaDAO.getDanhGiaByMaDG(maDG);
            
            if (danhGia != null) {
                // Cập nhật thông tin
                danhGia.setDiemSo(diemSo);
                danhGia.setBinhLuan(binhLuan);
                
                // Lưu vào cơ sở dữ liệu
                return danhGiaDAO.updateDanhGia(danhGia);
            }
            
            return false;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật đánh giá: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa đánh giá
    public boolean deleteDanhGia(String maDG) {
        try {
            return danhGiaDAO.deleteDanhGia(maDG);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa đánh giá: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    // Lấy danh sách hợp đồng chưa đánh giá
    public List<String> getHopDongChuaDanhGia(String maKH) {
        return danhGiaDAO.getHopDongChuaDanhGia(maKH);
    }
    
    // Lấy thông tin hợp đồng
    public String getThongTinHopDong(String maHD) {
        return danhGiaDAO.getThongTinHopDong(maHD);
    }
    
    // Lấy MaKH từ MaHD
    public String getMaKHFromMaHD(String maHD) {
        return danhGiaDAO.getMaKHFromMaHD(maHD);
    }
}