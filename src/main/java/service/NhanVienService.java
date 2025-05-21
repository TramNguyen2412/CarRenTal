package service;

import dao.NhanVienDAO;
import model.NhanVien;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

public class NhanVienService {
    private NhanVienDAO nhanVienDAO;
    private StringBuilder errorMessage = new StringBuilder();
    
    public NhanVienService() {
        nhanVienDAO = new NhanVienDAO();
    }
    
    public List<NhanVien> getAllNhanVien() {
        return nhanVienDAO.getAllNhanVien();
    }
    
    public NhanVien getNhanVienByMa(String maNV) {
        return nhanVienDAO.getNhanVienByMa(maNV);
    }
    
    public String addNhanVien(NhanVien nv) {
        errorMessage = new StringBuilder();
        
        // Validate dữ liệu trước khi thêm
        String validationError = validateNhanVienData(nv);
        if (validationError != null) {
            errorMessage.append(validationError);
            return null;
        }
        
        // Kiểm tra trùng lặp
        if (nhanVienDAO.isPhoneNumberExists(nv.getSdt(), nv.getMaNV())) {
            errorMessage.append("Số điện thoại đã tồn tại trong hệ thống");
            return null;
        }
        
        if (nv.getEmail() != null && !nv.getEmail().isEmpty() && 
            nhanVienDAO.isEmailExists(nv.getEmail(), nv.getMaNV())) {
            errorMessage.append("Email đã tồn tại trong hệ thống");
            return null;
        }
        
        return nhanVienDAO.addNhanVien(nv);
    }
    
    public boolean updateNhanVien(NhanVien nv) {
        errorMessage = new StringBuilder();
        
        // Validate dữ liệu trước khi cập nhật
        String validationError = validateNhanVienData(nv);
        if (validationError != null) {
            errorMessage.append(validationError);
            return false;
        }
        
        // Kiểm tra trùng lặp
        if (nhanVienDAO.isPhoneNumberExists(nv.getSdt(), nv.getMaNV())) {
            errorMessage.append("Số điện thoại đã tồn tại trong hệ thống");
            return false;
        }
        
        if (nv.getEmail() != null && !nv.getEmail().isEmpty() && 
            nhanVienDAO.isEmailExists(nv.getEmail(), nv.getMaNV())) {
            errorMessage.append("Email đã tồn tại trong hệ thống");
            return false;
        }
        
        return nhanVienDAO.updateNhanVien(nv);
    }
    
    public boolean deleteNhanVien(String maNV) {
        errorMessage = new StringBuilder();
        
        if (maNV == null || maNV.trim().isEmpty()) {
            errorMessage.append("Mã nhân viên không hợp lệ");
            return false;
        }
        
        // Kiểm tra nhân viên tồn tại
        NhanVien nv = nhanVienDAO.getNhanVienByMa(maNV);
        if (nv == null) {
            errorMessage.append("Không tìm thấy nhân viên với mã " + maNV);
            return false;
        }
        
        // Kiểm tra nhân viên có liên quan đến dữ liệu khác không
        // Có thể thêm logic kiểm tra ràng buộc ở đây
        
        return nhanVienDAO.deleteNhanVien(maNV);
    }
    
    public List<NhanVien> searchNhanVien(String keyword) {
        return nhanVienDAO.searchNhanVien(keyword);
    }
    
    // Phương thức kiểm tra số điện thoại đã tồn tại
    public boolean isPhoneNumberExists(String sdt, String excludeMaNV) {
        return nhanVienDAO.isPhoneNumberExists(sdt, excludeMaNV);
    }
    
    // Phương thức kiểm tra email đã tồn tại
    public boolean isEmailExists(String email, String excludeMaNV) {
        return nhanVienDAO.isEmailExists(email, excludeMaNV);
    }
    
    // Phương thức validate dữ liệu nhân viên
    private String validateNhanVienData(NhanVien nv) {
        if (nv.getHoTen() == null || nv.getHoTen().trim().isEmpty()) {
            return "Họ tên không được để trống";
        }
        
        if (nv.getSdt() == null || nv.getSdt().trim().isEmpty()) {
            return "Số điện thoại không được để trống";
        }
        
        if (!Pattern.matches("^0[0-9]{9}$", nv.getSdt())) {
            return "Số điện thoại không hợp lệ (phải bắt đầu bằng 0 và đủ 10 số)";
        }
        
        if (nv.getEmail() != null && !nv.getEmail().isEmpty() && 
            !Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", nv.getEmail())) {
            return "Email không hợp lệ";
        }
        
        if (nv.getChucVu() == null || nv.getChucVu().trim().isEmpty()) {
            return "Chức vụ không được để trống";
        }
        
        return null; // Không có lỗi
    }
    
    public String getErrorMessage() {
        return errorMessage.toString();
    }
    
    // Phương thức lấy nhân viên theo chức vụ
    public List<NhanVien> getNhanVienByChucVu(String chucVu) {
        return nhanVienDAO.getNhanVienByChucVu(chucVu);
    }
    
    // Phương thức lấy danh sách chức vụ
    public List<String> getAllChucVu() {
        return nhanVienDAO.getAllChucVu();
    }
    
    // Phương thức lấy nhân viên theo số điện thoại
    public NhanVien getNhanVienBySDT(String sdt) {
        return nhanVienDAO.getNhanVienBySDT(sdt);
    }
    
    // Phương thức lấy nhân viên theo email
    public NhanVien getNhanVienByEmail(String email) {
        return nhanVienDAO.getNhanVienByEmail(email);
    }
    
    // Phương thức xóa nhiều nhân viên
    public int deleteMultipleNhanVien(List<String> maNVList) {
        return nhanVienDAO.deleteMultipleNhanVien(maNVList);
    }
    
    // Phương thức nhập danh sách nhân viên từ danh sách
    public int importNhanVien(List<NhanVien> danhSachNV) {
        return nhanVienDAO.importNhanVien(danhSachNV);
    }
    
    // Phương thức lấy thống kê nhân viên
    public Map<String, Object> getThongKeNhanVien() {
        return nhanVienDAO.getThongKeNhanVien();
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
        if (chucVu == null || chucVu.trim().isEmpty()) {
            errorMessage.append("Tên chức vụ không được để trống");
            return false;
        }
        
        // Kiểm tra chức vụ đã tồn tại chưa
        if (isChucVuExists(chucVu)) {
            errorMessage.append("Chức vụ đã tồn tại trong hệ thống");
            return false;
        }
        
        return nhanVienDAO.addChucVu(chucVu);
    }
    
    /**
     * Kiểm tra chức vụ đã tồn tại chưa
     * @param chucVu Tên chức vụ cần kiểm tra
     * @return true nếu chức vụ đã tồn tại, false nếu chưa
     */
    public boolean isChucVuExists(String chucVu) {
        if (chucVu == null || chucVu.trim().isEmpty()) {
            return false;
        }
        
        List<String> danhSachChucVu = nhanVienDAO.getAllChucVu();
        if (danhSachChucVu != null) {
            for (String cv : danhSachChucVu) {
                if (cv.equalsIgnoreCase(chucVu.trim())) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
