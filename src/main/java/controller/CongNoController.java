package controller;

import service.CongNoService;
import model.LichSuCongNo;
import model.KhachHang;
import java.util.Date;
import java.util.List;

public class CongNoController {
    private CongNoService congNoService;
    
    public CongNoController() {
        congNoService = new CongNoService();
    }
    
    public List<LichSuCongNo> getAllLichSuCongNo() {
        return congNoService.getAllLichSuCongNo();
    }
    
    public List<LichSuCongNo> getLichSuCongNoByKhachHang(String maKH) {
        return congNoService.getLichSuCongNoByKhachHang(maKH);
    }
    
    public String addLichSuCongNo(String maKH, Date ngayGiaoDich, String loaiGiaoDich, double soTien, String ghiChu) {
        try {
            LichSuCongNo ls = new LichSuCongNo();
            ls.setMaKH(maKH);
            ls.setNgayGiaoDich(ngayGiaoDich);
            ls.setLoaiGiaoDich(loaiGiaoDich);
            ls.setSoTien(soTien);
            ls.setGhiChu(ghiChu);
            
            boolean success = congNoService.addLichSuCongNo(ls);
            
            if (success) {
                return "Thêm lịch sử công nợ thành công";
            } else {
                return "Thêm lịch sử công nợ thất bại";
            }
        } catch (IllegalArgumentException e) {
            return "Lỗi: " + e.getMessage();
        } catch (Exception e) {
            // Extract Oracle error message if available
            String message = e.getMessage();
            if (message.contains("ORA-20003")) {
                return "Lỗi: Số tiền thanh toán vượt quá tổng công nợ của khách hàng";
            } else if (message.contains("ORA-20010")) {
                return "Lỗi: Không thể thêm giao dịch vì công nợ sau sẽ âm";
            } else {
                return "Lỗi hệ thống: " + e.getMessage();
            }
        }
    }
    
    public String updateLichSuCongNo(String maLichSu, String maKH, Date ngayGiaoDich, String loaiGiaoDich, double soTien, String ghiChu) {
        try {
            LichSuCongNo ls = new LichSuCongNo();
            ls.setMaLichSu(maLichSu);
            ls.setMaKH(maKH);
            ls.setNgayGiaoDich(ngayGiaoDich);
            ls.setLoaiGiaoDich(loaiGiaoDich);
            ls.setSoTien(soTien);
            ls.setGhiChu(ghiChu);
            
            boolean success = congNoService.updateLichSuCongNo(ls);
            
            if (success) {
                return "Cập nhật lịch sử công nợ thành công";
            } else {
                return "Cập nhật lịch sử công nợ thất bại";
            }
        } catch (IllegalArgumentException e) {
            return "Lỗi: " + e.getMessage();
        } catch (Exception e) {
            // Extract Oracle error message if available
            String message = e.getMessage();
            if (message.contains("ORA-20003")) {
                return "Lỗi: Số tiền thanh toán vượt quá tổng công nợ của khách hàng";
            } else if (message.contains("ORA-20012")) {
                return "Lỗi: Không thể cập nhật vì công nợ sau sẽ âm";
            } else if (message.contains("ORA-20013")) {
                return "Lỗi: Không thể chuyển giao dịch vì công nợ sau sẽ âm";
            } else {
                return "Lỗi hệ thống: " + e.getMessage();
            }
        }
    }
    
    public String deleteLichSuCongNo(String maLichSu) {
        try {
            boolean success = congNoService.deleteLichSuCongNo(maLichSu);
            
            if (success) {
                return "Xóa lịch sử công nợ thành công";
            } else {
                return "Xóa lịch sử công nợ thất bại";
            }
        } catch (IllegalArgumentException e) {
            return "Lỗi: " + e.getMessage();
        } catch (Exception e) {
            // Extract Oracle error message if available
            String message = e.getMessage();
            if (message.contains("ORA-20011")) {
                return "Lỗi: Không thể xóa giao dịch vì công nợ sau sẽ âm";
            } else {
                return "Lỗi hệ thống: " + e.getMessage();
            }
        }
    }
    
    public List<KhachHang> getKhachHangCoCongNo() {
        return congNoService.getKhachHangCoCongNo();
    }
    
    public double getTongCongNoKhachHang(String maKH) {
        return congNoService.getTongCongNoKhachHang(maKH);
    }
}