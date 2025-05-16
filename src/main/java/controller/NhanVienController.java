package controller;

import service.NhanVienService;
import model.NhanVien;
import java.util.List;

public class NhanVienController {
    private NhanVienService nhanVienService;
    
    public NhanVienController() {
        nhanVienService = new NhanVienService();
    }
    
    public List<NhanVien> getAllNhanVien() {
        return nhanVienService.getAllNhanVien();
    }
    
    public NhanVien getNhanVienByMa(String maNV) {
        return nhanVienService.getNhanVienByMa(maNV);
    }
    
    public NhanVien getNhanVienByMaTK(String maTK) {
        return nhanVienService.getNhanVienByMaTK(maTK);
    }
    
    public boolean existsNhanVien(String maNV) {
        return nhanVienService.existsNhanVien(maNV);
    }
    
    public String getDefaultNhanVienMa() {
        return nhanVienService.getDefaultNhanVienMa();
    }
}