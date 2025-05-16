package service;

import dao.NhanVienDAO;
import model.NhanVien;
import java.util.List;

public class NhanVienService {
    private NhanVienDAO nhanVienDAO;
    
    public NhanVienService() {
        nhanVienDAO = new NhanVienDAO();
    }
    
    public List<NhanVien> getAllNhanVien() {
        return nhanVienDAO.getAllNhanVien();
    }
    
    public NhanVien getNhanVienByMa(String maNV) {
        return nhanVienDAO.getNhanVienByMa(maNV);
    }
    
    public NhanVien getNhanVienByMaTK(String maTK) {
        return nhanVienDAO.getNhanVienByMaTK(maTK);
    }
    
    public boolean existsNhanVien(String maNV) {
        return nhanVienDAO.existsNhanVien(maNV);
    }
    
    // Phương thức lấy mã nhân viên mặc định nếu cần
    public String getDefaultNhanVienMa() {
        // Trả về một mã nhân viên mặc định có trong hệ thống
        // Hoặc có thể là null nếu không tìm thấy
        List<NhanVien> danhSachNV = getAllNhanVien();
        if (danhSachNV != null && !danhSachNV.isEmpty()) {
            return danhSachNV.get(0).getMaNV();
        }
        return null;
    }
}