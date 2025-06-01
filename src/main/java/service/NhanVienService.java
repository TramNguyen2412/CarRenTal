package service;

import dao.NhanVienDAO;
import model.NhanVien;
import java.util.List;
import java.util.Map;
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
    
    public NhanVien getNhanVienByMaTK(String maTK) {
        return nhanVienDAO.getNhanVienByMaTK(maTK);
    }
    
    public boolean existsNhanVien(String maNV) {
        return nhanVienDAO.existsNhanVien(maNV);
    }
    
    public String getDefaultNhanVienMa() {
        List<NhanVien> danhSachNV = getAllNhanVien();
        if (danhSachNV != null && !danhSachNV.isEmpty()) {
            return danhSachNV.get(0).getMaNV();
        }
        return null;
    }   
    public boolean addNhanVien(NhanVien nv) {
        errorMessage = new StringBuilder();

        // Validate dữ liệu trước khi thêm (không check MaTK)
        String validationError = validateNhanVienData(nv, false); // false for add (MaNV is not set yet)
        if (validationError != null) {
            errorMessage.append(validationError);
            return false;
        }

        // Kiểm tra trùng lặp SĐT
        if (nhanVienDAO.isPhoneNumberExists(nv.getSdt(), null)) { // MaNV is null for new employee
            errorMessage.append("Số điện thoại đã tồn tại trong hệ thống");
            return false;
        }

        // Kiểm tra trùng lặp Email
        if (nv.getEmail() != null && !nv.getEmail().isEmpty() &&
                nhanVienDAO.isEmailExists(nv.getEmail(), null)) { // MaNV is null for new employee
            errorMessage.append("Email đã tồn tại trong hệ thống");
            return false;
        }

        boolean success = nhanVienDAO.addNhanVien(nv);
        if (!success) {
            errorMessage.append("Thêm nhân viên không thành công do lỗi hệ thống.");
        }
        return success;
    }

    public boolean updateNhanVien(NhanVien nv) {
        errorMessage = new StringBuilder();

        // Validate dữ liệu trước khi cập nhật (check MaTK cho update)
        String validationError = validateNhanVienData(nv, true); // true for update (MaNV should exist)
        if (validationError != null) {
            errorMessage.append(validationError);
            return false;
        }

        // Kiểm tra trùng lặp SĐT (loại trừ chính nhân viên đang cập nhật)
        if (nhanVienDAO.isPhoneNumberExists(nv.getSdt(), nv.getMaNV())) {
            errorMessage.append("Số điện thoại đã tồn tại cho một nhân viên khác");
            return false;
        }

        // Kiểm tra trùng lặp Email (loại trừ chính nhân viên đang cập nhật)
        if (nv.getEmail() != null && !nv.getEmail().isEmpty() &&
                nhanVienDAO.isEmailExists(nv.getEmail(), nv.getMaNV())) {
            errorMessage.append("Email đã tồn tại cho một nhân viên khác");
            return false;
        }

        boolean success = nhanVienDAO.updateNhanVien(nv);
        if (!success) {
            errorMessage.append("Cập nhật nhân viên không thành công do lỗi hệ thống hoặc nhân viên không tồn tại.");
        }
        return success;
    }

    public boolean deleteNhanVien(String maNV) {
        errorMessage = new StringBuilder();

        if (maNV == null || maNV.trim().isEmpty()) {
            errorMessage.append("Mã nhân viên không hợp lệ");
            return false;
        }

        // Kiểm tra nhân viên tồn tại
        if (!nhanVienDAO.existsNhanVien(maNV)) {
            errorMessage.append("Không tìm thấy nhân viên với mã " + maNV);
            return false;
        }

        boolean success = nhanVienDAO.deleteNhanVien(maNV);
        if (!success) {
            errorMessage.append("Xóa nhân viên không thành công do lỗi hệ thống.");
        }
        return success;
    }

    public List<NhanVien> searchNhanVien(String keyword) {
        return nhanVienDAO.searchNhanVien(keyword);
    }

    public boolean isPhoneNumberExists(String sdt, String excludeMaNV) {
        return nhanVienDAO.isPhoneNumberExists(sdt, excludeMaNV);
    }

    public boolean isEmailExists(String email, String excludeMaNV) {
        return nhanVienDAO.isEmailExists(email, excludeMaNV);
    }

    public String getErrorMessage() {
        String msg = errorMessage.toString();
        errorMessage.setLength(0); // Clear message after retrieval
        return msg;
    }

    public List<NhanVien> getNhanVienByChucVu(String chucVu) {
        return nhanVienDAO.getNhanVienByChucVu(chucVu);
    }

    public List<String> getAllChucVu() {
        return nhanVienDAO.getAllChucVu();
    }

    public NhanVien getNhanVienBySDT(String sdt) {
        return nhanVienDAO.getNhanVienBySDT(sdt);
    }

    public NhanVien getNhanVienByEmail(String email) {
        return nhanVienDAO.getNhanVienByEmail(email);
    }

    public int deleteMultipleNhanVien(List<String> maNVList) {
        return nhanVienDAO.deleteMultipleNhanVien(maNVList);
    }

    public int importNhanVien(List<NhanVien> danhSachNV) {
        return nhanVienDAO.importNhanVien(danhSachNV);
    }

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

    // Phương thức validate dữ liệu nhân viên
    private String validateNhanVienData(NhanVien nv, boolean isUpdate) {
        if (isUpdate && (nv.getMaNV() == null || nv.getMaNV().trim().isEmpty())) {
            return "Mã nhân viên không được để trống khi cập nhật";
        }

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

        // Chỉ check MaTK khi update, không check khi thêm mới
        if (isUpdate && (nv.getMaTK() == null || nv.getMaTK().trim().isEmpty())) {
            return "Mã tài khoản không được để trống khi cập nhật";
        }

        return null; // Không có lỗi
    }
}

