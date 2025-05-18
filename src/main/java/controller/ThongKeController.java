package controller;

import service.ThongKeService;
import java.util.List;
import java.util.Map;

public class ThongKeController {
    private ThongKeService thongKeService;
    
    public ThongKeController() {
        thongKeService = new ThongKeService();
    }
    
    public Map<Integer, Double> getDoanhThuTheoThang(int nam) {
        return thongKeService.getDoanhThuTheoThang(nam);
    }
    
    public List<Map<String, Object>> getDoanhThuTheoKhachHang(int nam) {
        return thongKeService.getDoanhThuTheoKhachHang(nam);
    }
    
    
    public double getDoanhThuBaoDuong(int nam) {
        return thongKeService.getDoanhThuBaoDuong(nam);
    }
    
    public double getChiPhiBaoDuongDinhKy(int nam) {
        return thongKeService.getChiPhiBaoDuongDinhKy(nam);
    }
    
    public List<Map<String, Object>> getXeDuocThueNhieuNhat(int nam) {
        return thongKeService.getXeDuocThueNhieuNhat(nam);
    }
    
    public double getTongCongNo() {
        return thongKeService.getTongCongNo();
    }
}