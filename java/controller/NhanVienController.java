package controller;

import dao.NhanVienDAO;
import model.NhanVien;
import service.NhanVienService;
import java.util.List;
import java.util.Map;

public class NhanVienController {
    private NhanVienDAO nhanVienDAO;
    private NhanVienService nhanVienService;
    
    public NhanVienController() {
        this.nhanVienDAO = new NhanVienDAO();
        this.nhanVienService = new NhanVienService();
    }
    
    public List<NhanVien> getAllNhanVien() {
        return nhanVienService.getAllNhanVien();
    }
    
    public NhanVien getNhanVienByMa(String maNV) {
        return nhanVienService.getNhanVienByMa(maNV);
    }
    
    public String addNhanVien(NhanVien nv) {
        // Validate dữ liệu trước khi thêm
        return nhanVienService.addNhanVien(nv);
    }
    
    public String getErrorMessage() {
        return nhanVienService.getErrorMessage();
    }
    
    public boolean updateNhanVien(NhanVien nv) {
        // Validate dữ liệu trước khi cập nhật
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
        if (maNV == null || maNV.trim().isEmpty()) {
            return false;
        }
        return nhanVienDAO.existsNhanVien(maNV);
    }
    
    public String getDefaultNhanVienMa() {
        return nhanVienDAO.getDefaultNhanVienMa();
    }
    
    /**
     * Thêm chức vụ mới vào hệ thống
     * @param chucVu Tên chức vụ mới
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addChucVu(String chucVu) {
        return nhanVienService.addChucVu(chucVu);
    }
    
    /**
     * Kiểm tra chức vụ đã tồn tại chưa
     * @param chucVu Tên chức vụ cần kiểm tra
     * @return true nếu chức vụ đã tồn tại, false nếu chưa
     */
    public boolean isChucVuExists(String chucVu) {
        return nhanVienService.isChucVuExists(chucVu);
    }
}
