package service;

import dao.KhachHangDAO;
import model.KhachHang;
import java.util.List;
import java.util.regex.Pattern;

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
        
        // Validate dữ liệu trước khi cập nhật
        String validationError = validateKhachHangData(kh);
        if (validationError != null) {
            errorMessage.append(validationError);
            return false;
        }
        
        // Kiểm tra trùng lặp
        if (khachHangDAO.isPhoneNumberExists(kh.getSdt(), kh.getMaKH())) {
            errorMessage.append("Số điện thoại đã tồn tại trong hệ thống");
            return false;
        }
        
        if (kh.getEmail() != null && !kh.getEmail().isEmpty() && 
            khachHangDAO.isEmailExists(kh.getEmail(), kh.getMaKH())) {
            errorMessage.append("Email đã tồn tại trong hệ thống");
            return false;
        }
        
        if (kh.getCccd() != null && !kh.getCccd().isEmpty() && 
            khachHangDAO.isCCCDExists(kh.getCccd(), kh.getMaKH())) {
            errorMessage.append("CCCD đã tồn tại trong hệ thống");
            return false;
        }
        
        return khachHangDAO.updateKhachHang(kh);
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
        
        // Kiểm tra khách hàng có công nợ
        if (kh.getTongTienNo() > 0) {
            errorMessage.append("Không thể xóa khách hàng đang có công nợ");
            return false;
        }
        
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
    
    public String getErrorMessage() {
        return errorMessage.toString();
    }
    
    // Phương thức lấy danh sách khách hàng có công nợ
    public List<KhachHang> getKhachHangCoCongNo() {
        List<KhachHang> allKhachHang = getAllKhachHang();
        List<KhachHang> khachHangCoCongNo = new java.util.ArrayList<>();
        
        for (KhachHang kh : allKhachHang) {
            if (kh.getTongTienNo() > 0) {
                khachHangCoCongNo.add(kh);
            }
        }
        
        return khachHangCoCongNo;
    }
    
    // Phương thức cập nhật công nợ khách hàng
    public boolean updateCongNo(String maKH, double soTien) {
        errorMessage = new StringBuilder();
        
        KhachHang kh = getKhachHangByMa(maKH);
        if (kh == null) {
            errorMessage.append("Không tìm thấy khách hàng với mã " + maKH);
            return false;
        }
        
        double tongTienNoMoi = kh.getTongTienNo() + soTien;
        if (tongTienNoMoi < 0) {
            errorMessage.append("Số tiền thanh toán vượt quá công nợ hiện tại");
            return false;
        }
        
        kh.setTongTienNo(tongTienNoMoi);
        return updateKhachHang(kh);
    }
}
