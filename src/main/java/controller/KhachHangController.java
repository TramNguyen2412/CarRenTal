package controller;

import model.KhachHang;
import service.KhachHangService;
import java.util.List;

public class KhachHangController {
    private KhachHangService khachHangService;
    
    public KhachHangController() {
        this.khachHangService = new KhachHangService();
    }
    
    public List<KhachHang> getAllKhachHang() {
        return khachHangService.getAllKhachHang();
    }
    
    public KhachHang getKhachHangByMa(String maKH) {
        return khachHangService.getKhachHangByMa(maKH);
    }
    
    public String addKhachHang(KhachHang kh) {
        // Validate dữ liệu trước khi thêm
        if (!validateKhachHang(kh)) {
            return null;
        }
        
        return khachHangService.addKhachHang(kh);
    }
    
    public boolean updateKhachHang(KhachHang kh) {
        // Validate dữ liệu trước khi cập nhật
        if (!validateKhachHang(kh)) {
            return false;
        }
        
        return khachHangService.updateKhachHang(kh);
    }
    
    public boolean deleteKhachHang(String maKH) {
        return khachHangService.deleteKhachHang(maKH);
    }
    
    public List<KhachHang> searchKhachHang(String keyword) {
        return khachHangService.searchKhachHang(keyword);
    }
    
    public boolean isPhoneNumberExists(String sdt, String excludeMaKH) {
        return khachHangService.isPhoneNumberExists(sdt, excludeMaKH);
    }
    
    public boolean isEmailExists(String email, String excludeMaKH) {
        return khachHangService.isEmailExists(email, excludeMaKH);
    }
    
    public boolean isCCCDExists(String cccd, String excludeMaKH) {
        return khachHangService.isCCCDExists(cccd, excludeMaKH);
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