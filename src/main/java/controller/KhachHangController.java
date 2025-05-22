package controller;

import dao.KhachHangDAO;
import model.KhachHang;
import java.util.List;

public class KhachHangController {
    private KhachHangDAO khachHangDAO;
    
    public KhachHangController() {
        this.khachHangDAO = new KhachHangDAO();
    }
    
    public List<KhachHang> getAllKhachHang() {
        return khachHangDAO.getAllKhachHang();
    }
    
    public KhachHang getKhachHangByMa(String maKH) {
        return khachHangDAO.getKhachHangByMa(maKH);
    }
    
    public String addKhachHang(KhachHang kh) {
        // Validate dữ liệu trước khi thêm
        if (!validateKhachHang(kh)) {
            return null;
        }
        
        return khachHangDAO.addKhachHang(kh);
    }
    
    public boolean updateKhachHang(KhachHang kh) {
        // Validate dữ liệu trước khi cập nhật
        if (!validateKhachHang(kh)) {
            return false;
        }
        
        return khachHangDAO.updateKhachHang(kh);
    }
    
    public boolean deleteKhachHang(String maKH) {
        return khachHangDAO.deleteKhachHang(maKH);
    }
    
    public List<KhachHang> searchKhachHang(String keyword) {
        return khachHangDAO.searchKhachHang(keyword);
    }
    
    public boolean isPhoneNumberExists(String sdt, String excludeMaKH) {
        return khachHangDAO.isPhoneNumberExists(sdt, excludeMaKH);
    }
    
    public boolean isEmailExists(String email, String excludeMaKH) {
        return khachHangDAO.isEmailExists(email, excludeMaKH);
    }
    
    public boolean isCCCDExists(String cccd, String excludeMaKH) {
        return khachHangDAO.isCCCDExists(cccd, excludeMaKH);
    }
    public KhachHang getKhachHangByTaiKhoan(String maTK) {
        return khachHangDAO.getKhachHangByMa(maTK);
    }
    private boolean validateKhachHang(KhachHang kh) {
        // Kiểm tra họ tên
        if (kh.getHoTen() == null || kh.getHoTen().trim().isEmpty()) {
            return false;
        }
        
        // Kiểm tra SĐT
        if (kh.getSdt() == null || kh.getSdt().trim().isEmpty()) {
            return false;
        }
        
        // Kiểm tra SĐT đã tồn tại
        if (isPhoneNumberExists(kh.getSdt(), kh.getMaKH())) {
            return false;
        }
        
        // Kiểm tra email đã tồn tại (nếu có)
        if (kh.getEmail() != null && !kh.getEmail().trim().isEmpty() && 
            isEmailExists(kh.getEmail(), kh.getMaKH())) {
            return false;
        }
        
        // Kiểm tra CCCD đã tồn tại (nếu có)
        if (kh.getCccd() != null && !kh.getCccd().trim().isEmpty() && 
            isCCCDExists(kh.getCccd(), kh.getMaKH())) {
            return false;
        }
        
        return true;
    }
}