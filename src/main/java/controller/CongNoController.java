package controller;

import java.util.Date;
import java.util.List;

import model.KhachHang;
import model.LichSuCongNo;
import service.CongNoService;

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
        // NÊN lấy message nghiệp vụ như ở Service (bên dưới), không chỉ e.printStackTrace()
        String msg = e.getMessage();
        if (msg != null && msg.contains("ORA-200")) {
            String[] lines = msg.split("\\r?\\n");
            for (String line : lines) {
                if (line.contains("ORA-200")) {
                    int idx = line.indexOf(":");
                    if (idx > 0 && idx < line.length() - 1) {
                        return line.substring(idx + 1).trim();
                    }
                    return line.trim();
                }
            }
            return msg;
        }
        return "Lỗi hệ thống: " + msg;
            }
        }
    }
    
    public List<KhachHang> getKhachHangCoCongNo() {
        return congNoService.getKhachHangCoCongNo();
    }
    
    public double getTongCongNoKhachHang(String maKH) {
        return congNoService.getTongCongNoKhachHang(maKH);
    }
   public LichSuCongNo getLichSuCongNoByMa(String maLichSu) {
    return congNoService.getLichSuCongNoByMa(maLichSu);
} 
   // CongNoController.java
public String updateLichSuCongNoThongTinChung(String maLS, String maKH, Date ngayGD, String ghiChu) {
    try {
        LichSuCongNo ls = new LichSuCongNo();
        ls.setMaLichSu(maLS);
        ls.setMaKH(maKH);
        ls.setNgayGiaoDich(ngayGD);
        ls.setGhiChu(ghiChu);
        boolean success = congNoService.updateLichSuCongNoThongTinChung(ls);
        return success ? "Cập nhật giao dịch thành công" : "Cập nhật giao dịch thất bại";
    } catch (Exception e) {
        // NÊN lấy message nghiệp vụ như ở Service (bên dưới), không chỉ e.printStackTrace()
        String msg = e.getMessage();
        if (msg != null && msg.contains("ORA-200")) {
            String[] lines = msg.split("\\r?\\n");
            for (String line : lines) {
                if (line.contains("ORA-200")) {
                    int idx = line.indexOf(":");
                    if (idx > 0 && idx < line.length() - 1) {
                        return line.substring(idx + 1).trim();
                    }
                    return line.trim();
                }
            }
            return msg;
        }
        return "Lỗi hệ thống: " + msg;
    }
}
public List<LichSuCongNo> searchLichSuCongNo(String keyword) {
    return congNoService.searchLichSuCongNo(keyword);
}
}