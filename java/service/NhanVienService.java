package service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import dao.NhanVienDAO;
import model.NhanVien;

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

    public boolean addNhanVien(NhanVien nv) {
        errorMessage = new StringBuilder();

        // Validate dữ liệu trước khi thêm
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

        // Validate dữ liệu trước khi cập nhật
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

        // TODO: Kiểm tra nhân viên có liên quan đến dữ liệu khác không (ví dụ: Hợp
        // đồng, Phiếu bảo dưỡng)
        // Ví dụ: if (hopDongDAO.hasActiveContractsForNhanVien(maNV)) {
        // errorMessage.append("Không thể xóa nhân viên vì có hợp đồng liên quan.");
        // return false;
        // }

        boolean success = nhanVienDAO.deleteNhanVien(maNV);
        if (!success) {
            errorMessage.append("Xóa nhân viên không thành công do lỗi hệ thống.");
        }
        return success;
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

        if (nv.getMaTK() == null || nv.getMaTK().trim().isEmpty()) {
            return "Mã tài khoản không được để trống";
        }

        return null; // Không có lỗi
    }

    public String getErrorMessage() {
        String msg = errorMessage.toString();
        errorMessage.setLength(0); // Clear message after retrieval
        return msg;
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
        // TODO: Add checks for dependencies before deleting multiple employees
        return nhanVienDAO.deleteMultipleNhanVien(maNVList);
    }

    // Phương thức nhập danh sách nhân viên từ danh sách
    public int importNhanVien(List<NhanVien> danhSachNV) {
        // TODO: Add more robust validation for each NhanVien in the list
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
}
