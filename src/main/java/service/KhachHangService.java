package service;

import dao.KhachHangDAO;
import model.KhachHang;
import java.util.List;

public class KhachHangService {
    private KhachHangDAO khachHangDAO;
    
    public KhachHangService() {
        khachHangDAO = new KhachHangDAO();
    }
    
    public List<KhachHang> getAllKhachHang() {
        return khachHangDAO.getAllKhachHang();
    }
    
    public KhachHang getKhachHangByMa(String maKH) {
        return khachHangDAO.getKhachHangByMa(maKH);
    }
    
    public String addKhachHang(KhachHang kh) {
        return khachHangDAO.addKhachHang(kh);
    }
    
    public boolean updateKhachHang(KhachHang kh) {
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
}