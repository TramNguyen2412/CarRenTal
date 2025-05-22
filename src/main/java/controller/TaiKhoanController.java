package controller;

import dao.TaiKhoanDAO;
import dao.KhachHangDAO;
import model.TaiKhoan;
import model.KhachHang;

public class TaiKhoanController {
    private TaiKhoanDAO taiKhoanDAO;
    private KhachHangDAO khachHangDAO;
    
    public TaiKhoanController() {
        this.taiKhoanDAO = new TaiKhoanDAO();
        this.khachHangDAO = new KhachHangDAO();
    }
    
    /**
     * Phương thức đăng nhập và lấy thông tin người dùng
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return TaiKhoan đã đăng nhập thành công, null nếu thất bại
     */
    public TaiKhoan dangNhap(String username, String password) {
        // Kiểm tra đăng nhập qua DAO
        TaiKhoan taiKhoan = taiKhoanDAO.checkLogin(username, password);
        return taiKhoan;
    }
    
      public KhachHang getKhachHangByMaTK(String maTK) {
        return khachHangDAO.getKhachHangByTaiKhoan(maTK);
    }
    
    // Các phương thức khác
    public TaiKhoan getTaiKhoanById(String maTK) {
        // Code lấy tài khoản theo ID
        return null; // Triển khai sau
    }
    
    // Thêm các phương thức khác như tạo tài khoản, cập nhật, đổi mật khẩu...
}