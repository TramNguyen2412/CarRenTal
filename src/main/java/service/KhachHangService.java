package service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import dao.KhachHangDAO; // Import Map
import model.KhachHang;

public class KhachHangService {
    private KhachHangDAO khachHangDAO;
    private StringBuilder errorMessage = new StringBuilder();

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
        errorMessage = new StringBuilder();

        // Validate dữ liệu trước khi thêm
        String validationError = validateKhachHangData(kh);
        if (validationError != null) {
            errorMessage.append(validationError);
            return null;
        }

        // Kiểm tra trùng lặp
        if (khachHangDAO.isPhoneNumberExists(kh.getSdt(), kh.getMaKH())) {
            errorMessage.append("Số điện thoại đã tồn tại trong hệ thống");
            return null;
        }

        if (kh.getEmail() != null && !kh.getEmail().isEmpty() &&
                khachHangDAO.isEmailExists(kh.getEmail(), kh.getMaKH())) {
            errorMessage.append("Email đã tồn tại trong hệ thống");
            return null;
        }

        if (kh.getCccd() != null && !kh.getCccd().isEmpty() &&
                khachHangDAO.isCCCDExists(kh.getCccd(), kh.getMaKH())) {
            errorMessage.append("CCCD đã tồn tại trong hệ thống");
            return null;
        }

        return khachHangDAO.addKhachHang(kh);
    }

    public boolean updateKhachHang(KhachHang kh) {
        errorMessage = new StringBuilder();

        // Validate dữ liệu
        String validationError = validateKhachHangData(kh);
        if (validationError != null) {
            errorMessage.append(validationError);
            return false;
        }

        // Kiểm tra khách hàng tồn tại
        KhachHang existingKH = khachHangDAO.getKhachHangByMa(kh.getMaKH());
        if (existingKH == null) {
            errorMessage.append("Không tìm thấy khách hàng với mã " + kh.getMaKH());
            return false;
        }

        // Kiểm tra trùng lặp SĐT (loại trừ chính khách hàng đang cập nhật)
        if (!kh.getSdt().equals(existingKH.getSdt()) &&
                khachHangDAO.isPhoneNumberExists(kh.getSdt(), kh.getMaKH())) {
            errorMessage.append("Số điện thoại đã tồn tại cho một khách hàng khác");
            return false;
        }

        // Kiểm tra trùng lặp Email (chỉ khi email không null và không rỗng)
        if (kh.getEmail() != null && !kh.getEmail().trim().isEmpty()) {
            String existingEmail = existingKH.getEmail();
            // Chỉ kiểm tra nếu email thay đổi
            if ((existingEmail == null || !kh.getEmail().equals(existingEmail)) &&
                    khachHangDAO.isEmailExists(kh.getEmail(), kh.getMaKH())) {
                errorMessage.append("Email đã tồn tại cho một khách hàng khác");
                return false;
            }
        }

        // Kiểm tra trùng lặp CCCD (chỉ khi CCCD không null và không rỗng)
        if (kh.getCccd() != null && !kh.getCccd().trim().isEmpty()) {
            String existingCCCD = existingKH.getCccd();
            // Chỉ kiểm tra nếu CCCD thay đổi
            if ((existingCCCD == null || !kh.getCccd().equals(existingCCCD)) &&
                    khachHangDAO.isCCCDExists(kh.getCccd(), kh.getMaKH())) {
                errorMessage.append("CCCD đã tồn tại cho một khách hàng khác");
                return false;
            }
        }

        boolean success = khachHangDAO.updateKhachHang(kh);
        if (!success) {
            errorMessage.append("Cập nhật khách hàng không thành công do lỗi hệ thống.");
        }
        return success;
    }

    public boolean deleteKhachHang(String maKH) {
        errorMessage = new StringBuilder();

        if (maKH == null || maKH.trim().isEmpty()) {
            errorMessage.append("Mã khách hàng không hợp lệ");
            return false;
        }

        // Kiểm tra khách hàng tồn tại
        KhachHang kh = khachHangDAO.getKhachHangByMa(maKH);
        if (kh == null) {
            errorMessage.append("Không tìm thấy khách hàng với mã " + maKH);
            return false;
        }

        // Kiểm tra khách hàng có công nợ - QUAN TRỌNG: phải > 0
        if (kh.getTongTienNo() > 0) {
            errorMessage.append("Không thể xóa khách hàng đang có công nợ. Tổng nợ hiện tại: " +
                    String.format("%,.0f", kh.getTongTienNo()) + " VNĐ");
            return false;
        }

        // Kiểm tra khách hàng có đang thuê xe không (nếu có bảng HopDong)
        // Có thể thêm logic kiểm tra này nếu cần

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

    // Phương thức validate dữ liệu khách hàng
    private String validateKhachHangData(KhachHang kh) {
        if (kh.getHoTen() == null || kh.getHoTen().trim().isEmpty()) {
            return "Họ tên không được để trống";
        }

        if (kh.getSdt() == null || kh.getSdt().trim().isEmpty()) {
            return "Số điện thoại không được để trống";
        }

        if (!Pattern.matches("^0[0-9]{9}$", kh.getSdt())) {
            return "Số điện thoại không hợp lệ (phải bắt đầu bằng 0 và đủ 10 số)";
        }

        // Kiểm tra email nếu có
        if (kh.getEmail() != null && !kh.getEmail().trim().isEmpty() &&
                !Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", kh.getEmail())) {
            return "Email không hợp lệ";
        }

        // Kiểm tra CCCD nếu có
        if (kh.getCccd() != null && !kh.getCccd().trim().isEmpty() &&
                !Pattern.matches("^[0-9]{12}$", kh.getCccd())) {
            return "CCCD không hợp lệ (phải đủ 12 số)";
        }

        // Kiểm tra tổng tiền nợ
        if (kh.getTongTienNo() < 0) {
            return "Tổng tiền nợ không được âm";
        }

        return null; // Không có lỗi
    }

    public String getErrorMessage() {
        return errorMessage.toString();
    }

    // Phương thức lấy danh sách khách hàng có công nợ
    public List<KhachHang> getKhachHangCoCongNo() {
        return khachHangDAO.getKhachHangCoCongNo();
    }

    // Phương thức cập nhật công nợ khách hàng
    public boolean updateCongNo(String maKH, double soTien) {
        errorMessage = new StringBuilder();
        if (maKH == null || maKH.trim().isEmpty()) {
            errorMessage.append("Mã khách hàng không hợp lệ.");
            return false;
        }
        // Additional validation for soTien if needed
        return khachHangDAO.updateCongNo(maKH, soTien);
    }

    // Phương thức mới để lấy thống kê
    public Map<String, Object> getThongKeKhachHang() {
        return khachHangDAO.getThongKeKhachHang();
    }
}
