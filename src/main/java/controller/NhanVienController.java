package controller;

import model.NhanVien;
import service.NhanVienService;
import java.util.List;
import java.util.Map;

public class NhanVienController {
    private NhanVienService nhanVienService;
    
    public NhanVienController() {
        this.nhanVienService = new NhanVienService();
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
    public boolean addNhanVien(NhanVien nv) {
        return nhanVienService.addNhanVien(nv);
    }

    public String getErrorMessage() {
        return nhanVienService.getErrorMessage();
    }

    public boolean updateNhanVien(NhanVien nv) {
        return nhanVienService.updateNhanVien(nv);
    }

    public boolean deleteNhanVien(String maNV) {
        return nhanVienService.deleteNhanVien(maNV);
    }

    public List<NhanVien> searchNhanVien(String keyword) {
        return nhanVienService.searchNhanVien(keyword);
    }

    public boolean isPhoneNumberExists(String sdt, String excludeMaNV) {
        return nhanVienService.isPhoneNumberExists(sdt, excludeMaNV);
    }

    public boolean isEmailExists(String email, String excludeMaNV) {
        return nhanVienService.isEmailExists(email, excludeMaNV);
    }

    public List<NhanVien> getNhanVienByChucVu(String chucVu) {
        return nhanVienService.getNhanVienByChucVu(chucVu);
    }

    public List<String> getAllChucVu() {
        return nhanVienService.getAllChucVu();
    }

    public NhanVien getNhanVienBySDT(String sdt) {
        return nhanVienService.getNhanVienBySDT(sdt);
    }

    public NhanVien getNhanVienByEmail(String email) {
        return nhanVienService.getNhanVienByEmail(email);
    }

    public int deleteMultipleNhanVien(List<String> maNVList) {
        return nhanVienService.deleteMultipleNhanVien(maNVList);
    }

    public int importNhanVien(List<NhanVien> danhSachNV) {
        return nhanVienService.importNhanVien(danhSachNV);
    }

    public Map<String, Object> getThongKeNhanVien() {
        return nhanVienService.getThongKeNhanVien();
    }

    public boolean existsNhanVien(String maNV) {
        return nhanVienService.existsNhanVien(maNV);
    }
    public String getDefaultNhanVienMa() {
        return nhanVienService.getDefaultNhanVienMa();
    }
}
