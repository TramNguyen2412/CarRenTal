package service;

import dao.KhachHangDAO;
import model.KhachHang;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class KhachHangService {
    private KhachHangDAO khachHangDAO;
    private StringBuilder errorMessage = new StringBuilder();
    public KhachHangService() {
        this.khachHangDAO = new KhachHangDAO();
    }
    
    public List<KhachHang> getAllKhachHang() {
        return khachHangDAO.getAllKhachHang();
    }
    
    public KhachHang getKhachHangByMa(String maKH) {
        return khachHangDAO.getKhachHangByMa(maKH);
    }
    
    public String addKhachHang(KhachHang kh) {
        // Validate dữ liệu trước khi thêm
        if (!validateKhachHang(kh)) {
            return null;
        }
        
        return khachHangDAO.addKhachHang(kh);
    }
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

        if (kh.getEmail() != null && !kh.getEmail().isEmpty() &&
                !Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", kh.getEmail())) {
            return "Email không hợp lệ";
        }

        if (kh.getCccd() != null && !kh.getCccd().isEmpty() &&
                !Pattern.matches("^[0-9]{12}$", kh.getCccd())) {
            return "CCCD không hợp lệ (phải đủ 12 số)";
        }

        return null; // Không có lỗi
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
    
    public KhachHang getKhachHangByTaiKhoan(String maTK) {
        return khachHangDAO.getKhachHangByTaiKhoan(maTK);
    }
    
    public String dangKyKhachHang(String hoTen, String sdt, String email, String cccd, 
                                  String diaChi, String tenDangNhap, String matKhau) {
        // Kiểm tra dữ liệu đầu vào
        if (hoTen == null || hoTen.trim().isEmpty()) {
            return "Họ tên không được để trống";
        }
        
        if (sdt == null || sdt.trim().isEmpty()) {
            return "Số điện thoại không được để trống";
        }
        
        if (email == null || email.trim().isEmpty()) {
            return "Email không được để trống";
        }
        
        if (cccd == null || cccd.trim().isEmpty()) {
            return "CCCD không được để trống";
        }
        
        if (diaChi == null || diaChi.trim().isEmpty()) {
            return "Địa chỉ không được để trống";
        }
        
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
            return "Tên đăng nhập không được để trống";
        }
        
        if (matKhau == null || matKhau.trim().isEmpty()) {
            return "Mật khẩu không được để trống";
        }
        
        // Gọi DAO để thực hiện stored procedure
        return khachHangDAO.dangKyKhachHang(hoTen, sdt, email, cccd, diaChi, tenDangNhap, matKhau);
    }
    
    public boolean validateKhachHang(KhachHang kh) {
        // Kiểm tra họ tên
        if (kh.getHoTen() == null || kh.getHoTen().trim().isEmpty()) {
            return false;
        }
        
        // Kiểm tra SĐT
        if (kh.getSdt() == null || kh.getSdt().trim().isEmpty()) {
            return false;
        }
        
        // Kiểm tra SĐT đã tồn tại
        if (isPhoneNumberExists(kh.getSdt(), kh.getMaKH())) {
            return false;
        }
        
        // Kiểm tra email đã tồn tại (nếu có)
        if (kh.getEmail() != null && !kh.getEmail().trim().isEmpty() && 
            isEmailExists(kh.getEmail(), kh.getMaKH())) {
            return false;
        }
        
        // Kiểm tra CCCD đã tồn tại (nếu có)
        if (kh.getCccd() != null && !kh.getCccd().trim().isEmpty() && 
            isCCCDExists(kh.getCccd(), kh.getMaKH())) {
            return false;
        }
        
        return true;
    }
     public String getErrorMessage() {
        return errorMessage.toString();
    }

    // Phương thức lấy danh sách khách hàng có công nợ
    public List<KhachHang> getKhachHangCoCongNo() {
        return khachHangDAO.getKhachHangCoCongNo();
    }
    public Map<String, Object> getThongKeKhachHang() {
        return khachHangDAO.getThongKeKhachHang();
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

}