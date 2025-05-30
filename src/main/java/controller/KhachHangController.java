package controller;

import java.util.List;
import java.util.Map; // Import Map

import model.KhachHang;
import service.KhachHangService;

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

    public String addKhachHang(KhachHang kh, String defaultPassword) {
        // This method signature was from a previous response.
        // If TaiKhoanUtils was removed, the password might not be relevant here.
        // The service's addKhachHang should handle all logic.
        return khachHangService.addKhachHang(kh); // Assuming service handles MaTK and password if needed
    }

    // Overload for when password is not handled at this stage or TaiKhoanUtils is
    // not used
    public String addKhachHang(KhachHang kh) {
        return khachHangService.addKhachHang(kh);
    }

    public String getErrorMessage() {
        return khachHangService.getErrorMessage();
    }

    public boolean updateKhachHang(KhachHang kh) {
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
    
    public KhachHang getKhachHangByTaiKhoan(String maTK) {
        return khachHangService.getKhachHangByTaiKhoan(maTK);
    }
    
    public String dangKyKhachHang(String hoTen, String sdt, String email, String cccd, 
                                  String diaChi, String tenDangNhap, String matKhau) {
        return khachHangService.dangKyKhachHang(hoTen, sdt, email, cccd, diaChi, tenDangNhap, matKhau);
    }
    // Phương thức mới để lấy thống kê
    public Map<String, Object> getThongKeKhachHang() {
        return khachHangService.getThongKeKhachHang();
    }
}