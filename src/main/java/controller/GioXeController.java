package controller;

import dao.GioXeDAO;
import dao.ChiTietHDDao;
import model.GioXe;
import model.Xe;

import java.util.Date;
import java.util.List;

public class GioXeController {
    private GioXeDAO gioXeDAO;
    private ChiTietHDDao chiTietHDDao;
    private XeController xeController;
    private StringBuilder errorMessage;
    
    public GioXeController() {
        gioXeDAO = new GioXeDAO();
        chiTietHDDao = new ChiTietHDDao();
        xeController = new XeController();
        errorMessage = new StringBuilder();
    }
    
    // Lấy danh sách xe trong giỏ của khách hàng
    public List<GioXe> getGioXeByMaKH(String maKH) {
        return gioXeDAO.getGioXeByMaKH(maKH);
    }
    
    // Thêm xe vào giỏ hàng
    public boolean themXeVaoGio(String maXe, String maKH, Date ngayBatDau, Date ngayKetThuc) {
        errorMessage = new StringBuilder();
        
        // Kiểm tra dữ liệu đầu vào
        if (maXe == null || maXe.trim().isEmpty()) {
            errorMessage.append("Mã xe không hợp lệ");
            return false;
        }
        
        if (maKH == null || maKH.trim().isEmpty()) {
            errorMessage.append("Mã khách hàng không hợp lệ");
            return false;
        }
        
        if (ngayBatDau == null || ngayKetThuc == null) {
            errorMessage.append("Ngày thuê không hợp lệ");
            return false;
        }
        
        // Kiểm tra ngày bắt đầu và ngày kết thúc
        Date today = new Date();
        if (ngayBatDau.before(today)) {
            errorMessage.append("Ngày bắt đầu không được là ngày trong quá khứ");
            return false;
        }
        
        if (ngayKetThuc.before(ngayBatDau)) {
            errorMessage.append("Ngày kết thúc phải sau ngày bắt đầu");
            return false;
        }
        
        // Kiểm tra xe có khả dụng trong khoảng thời gian này không
        String checkResult = chiTietHDDao.kiemTraXeThueDuoc(maXe, ngayBatDau, ngayKetThuc, null);
        if (checkResult != null) {
            errorMessage.append(checkResult);
            return false;
        }
        
        // Tạo đối tượng GioXe
        GioXe gioXe = new GioXe(maKH, maXe, ngayBatDau, ngayKetThuc);
        
        // Thêm vào giỏ hàng
        return gioXeDAO.themXeVaoGio(gioXe);
    }
    
    // Xóa xe khỏi giỏ hàng
    public boolean xoaXeKhoiGio(String maGH) {
        if (maGH == null || maGH.trim().isEmpty()) {
            errorMessage = new StringBuilder("Mã giỏ hàng không hợp lệ");
            return false;
        }
        
        return gioXeDAO.xoaXeKhoiGio(maGH);
    }
    
    // Xóa tất cả xe trong giỏ của khách hàng
    public boolean xoaTatCaXeTrongGio(String maKH) {
        if (maKH == null || maKH.trim().isEmpty()) {
            errorMessage = new StringBuilder("Mã khách hàng không hợp lệ");
            return false;
        }
        
        return gioXeDAO.xoaTatCaXeTrongGio(maKH);
    }
    
    // Cập nhật thông tin thuê xe trong giỏ
    public boolean capNhatGioXe(GioXe gioXe) {
        errorMessage = new StringBuilder();
        
        // Kiểm tra thông tin
        if (gioXe == null || gioXe.getMaGH() == null || gioXe.getMaGH().trim().isEmpty()) {
            errorMessage.append("Thông tin giỏ hàng không hợp lệ");
            return false;
        }
        
        if (gioXe.getNgayBatDau() == null || gioXe.getNgayKetThuc() == null) {
            errorMessage.append("Ngày thuê không hợp lệ");
            return false;
        }
        
        Date today = new Date();
        if (gioXe.getNgayBatDau().before(today)) {
            errorMessage.append("Ngày bắt đầu không được là ngày trong quá khứ");
            return false;
        }
        
        if (gioXe.getNgayKetThuc().before(gioXe.getNgayBatDau())) {
            errorMessage.append("Ngày kết thúc phải sau ngày bắt đầu");
            return false;
        }
        
        // Kiểm tra xe có khả dụng trong khoảng thời gian mới không
        String checkResult = chiTietHDDao.kiemTraXeThueDuoc(
            gioXe.getMaXe(), gioXe.getNgayBatDau(), gioXe.getNgayKetThuc(), null);
        
        if (checkResult != null) {
            errorMessage.append(checkResult);
            return false;
        }
        
        return gioXeDAO.capNhatGioXe(gioXe);
    }
    
    // Kiểm tra xem xe có trong giỏ hàng của khách hàng không
    public GioXe getGioXeByMaXeAndMaKH(String maXe, String maKH) {
        return gioXeDAO.getGioXeByMaXeAndMaKH(maXe, maKH);
    }
    
    // Đếm số lượng xe trong giỏ của khách hàng
    public int demSoXeTrongGio(String maKH) {
        return gioXeDAO.demSoXeTrongGio(maKH);
    }
    
    // Tính tổng tiền các xe trong giỏ
    public double tinhTongTienGioHang(String maKH) {
        List<GioXe> danhSachGioXe = getGioXeByMaKH(maKH);
        double tongTien = 0;
        
        for (GioXe gioXe : danhSachGioXe) {
            tongTien += gioXe.getThanhTien();
        }
        
        return tongTien;
    }
    
    public String getErrorMessage() {
        return errorMessage.toString();
    }
}