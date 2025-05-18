package controller;

import service.BaoDuongService;
import model.PhieuBaoDuong;
import model.ChiTietBaoDuong;
import java.util.Date;
import java.util.List;

public class BaoDuongController {
    private BaoDuongService baoDuongService;
    
    public BaoDuongController() {
        baoDuongService = new BaoDuongService();
    }
    
    public List<PhieuBaoDuong> getAllPhieuBaoDuong() {
        return baoDuongService.getAllPhieuBaoDuong();
    }
    
    public PhieuBaoDuong getPhieuBaoDuongById(String maBD) {
        return baoDuongService.getPhieuBaoDuongById(maBD);
    }
    
    public List<ChiTietBaoDuong> getChiTietByMaBD(String maBD) {
        return baoDuongService.getChiTietByMaBD(maBD);
    }
    
    public String addPhieuBaoDuong(String maXe, String maKH, Date ngayBD, String maNV, String loaiBD) {
        try {
            PhieuBaoDuong phieu = new PhieuBaoDuong();
            phieu.setMaXe(maXe);
            phieu.setMaKH(maKH);
            phieu.setNgayBD(ngayBD);
            phieu.setMaNV(maNV);
            phieu.setLoaiBD(loaiBD);
            
            boolean success = baoDuongService.addPhieuBaoDuong(phieu);
            
            if (success) {
                return "Thêm phiếu bảo dưỡng thành công";
            } else {
                return "Thêm phiếu bảo dưỡng thất bại";
            }
        } catch (IllegalArgumentException e) {
            return "Lỗi: " + e.getMessage();
        } catch (Exception e) {
            // Extract Oracle error message if available
            String message = e.getMessage();
            if (message.contains("ORA-20017")) {
                return "Lỗi: Xe đã được thuê trong thời gian này";
            } else if (message.contains("ORA-20050")) {
                return "Lỗi: Xe đã có phiếu bảo dưỡng khác trong ngày này";
            } else if (message.contains("ORA-20001")) {
                return "Lỗi: Phiếu bảo dưỡng Định kỳ không được gán MaKH";
            } else if (message.contains("ORA-20002")) {
                return "Lỗi: Phiếu bảo dưỡng của khách hàng phải có MaKH";
            } else {
                return "Lỗi hệ thống: " + e.getMessage();
            }
        }
    }
    
    public String updatePhieuBaoDuong(String maBD, String maXe, String maKH, Date ngayBD, String maNV, String loaiBD) {
        try {
            PhieuBaoDuong phieu = new PhieuBaoDuong();
            phieu.setMaBD(maBD);
            phieu.setMaXe(maXe);
            phieu.setMaKH(maKH);
            phieu.setNgayBD(ngayBD);
            phieu.setMaNV(maNV);
            phieu.setLoaiBD(loaiBD);
            
            boolean success = baoDuongService.updatePhieuBaoDuong(phieu);
            
            if (success) {
                return "Cập nhật phiếu bảo dưỡng thành công";
            } else {
                return "Cập nhật phiếu bảo dưỡng thất bại";
            }
        } catch (IllegalArgumentException e) {
            return "Lỗi: " + e.getMessage();
        } catch (Exception e) {
            // Extract Oracle error message if available
            String message = e.getMessage();
            if (message.contains("ORA-20017")) {
                return "Lỗi: Xe đã được thuê trong thời gian này";
            } else if (message.contains("ORA-20050")) {
                return "Lỗi: Xe đã có phiếu bảo dưỡng khác trong ngày này";
            } else if (message.contains("ORA-20001")) {
                return "Lỗi: Phiếu bảo dưỡng Định kỳ không được gán MaKH";
            } else if (message.contains("ORA-20002")) {
                return "Lỗi: Phiếu bảo dưỡng của khách hàng phải có MaKH";
            } else {
                return "Lỗi hệ thống: " + e.getMessage();
            }
        }
    }
    
    public String deletePhieuBaoDuong(String maBD) {
        try {
            boolean success = baoDuongService.deletePhieuBaoDuong(maBD);
            
            if (success) {
                return "Xóa phiếu bảo dưỡng thành công";
            } else {
                return "Xóa phiếu bảo dưỡng thất bại";
            }
        } catch (Exception e) {
            return "Lỗi hệ thống: " + e.getMessage();
        }
    }
    
    public String addChiTietBaoDuong(String maBD, String maDV, int soLuong) {
        try {
            ChiTietBaoDuong ct = new ChiTietBaoDuong();
            ct.setMaBD(maBD);
            ct.setMaDV(maDV);
            ct.setSoLuong(soLuong);
            
            boolean success = baoDuongService.addChiTietBaoDuong(ct);
            
            if (success) {
                return "Thêm chi tiết bảo dưỡng thành công";
            } else {
                return "Thêm chi tiết bảo dưỡng thất bại";
            }
        } catch (IllegalArgumentException e) {
            return "Lỗi: " + e.getMessage();
        } catch (Exception e) {
            return "Lỗi hệ thống: " + e.getMessage();
        }
    }
    
    public String updateChiTietBaoDuong(String maBD, String maDV, int soLuong) {
        try {
            ChiTietBaoDuong ct = new ChiTietBaoDuong();
            ct.setMaBD(maBD);
            ct.setMaDV(maDV);
            ct.setSoLuong(soLuong);
            
            boolean success = baoDuongService.updateChiTietBaoDuong(ct);
            
            if (success) {
                return "Cập nhật chi tiết bảo dưỡng thành công";
            } else {
                return "Cập nhật chi tiết bảo dưỡng thất bại";
            }
        } catch (IllegalArgumentException e) {
            return "Lỗi: " + e.getMessage();
        } catch (Exception e) {
            return "Lỗi hệ thống: " + e.getMessage();
        }
    }
    
    public String deleteChiTietBaoDuong(String maBD, String maDV) {
        try {
            boolean success = baoDuongService.deleteChiTietBaoDuong(maBD, maDV);
            
            if (success) {
                return "Xóa chi tiết bảo dưỡng thành công";
            } else {
                return "Xóa chi tiết bảo dưỡng thất bại";
            }
        } catch (Exception e) {
            return "Lỗi hệ thống: " + e.getMessage();
        }
    }
    
    public List<PhieuBaoDuong> searchPhieuBaoDuong(String keyword) {
        return baoDuongService.searchPhieuBaoDuong(keyword);
    }
    
    public List<PhieuBaoDuong> getPhieuBaoDuongByKhachHang(String maKH) {
        return baoDuongService.getPhieuBaoDuongByKhachHang(maKH);
    }
}