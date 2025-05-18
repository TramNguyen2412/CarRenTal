package service;

import dao.ThongKeDAO;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ThongKeService {
    private ThongKeDAO thongKeDAO;
    
    public ThongKeService() {
        thongKeDAO = new ThongKeDAO();
    }
    
    public Map<Integer, Double> getDoanhThuTheoThang(int nam) {
        // Validate input
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (nam < 2000 || nam > currentYear) {
            throw new IllegalArgumentException("Năm không hợp lệ");
        }
        
        return thongKeDAO.getDoanhThuTheoThang(nam);
    }
    
    public List<Map<String, Object>> getDoanhThuTheoKhachHang(int nam) {
        // Validate input
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (nam < 2000 || nam > currentYear) {
            throw new IllegalArgumentException("Năm không hợp lệ");
        }
        
        return thongKeDAO.getDoanhThuTheoKhachHang(nam);
    }
    
    public double getDoanhThuBaoDuong(int nam) {
        // Validate input
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (nam < 2000 || nam > currentYear) {
            throw new IllegalArgumentException("Năm không hợp lệ");
        }
        
        return thongKeDAO.getDoanhThuBaoDuong(nam);
    }
    
    public double getChiPhiBaoDuongDinhKy(int nam) {
        // Validate input
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (nam < 2000 || nam > currentYear) {
            throw new IllegalArgumentException("Năm không hợp lệ");
        }
        
        return thongKeDAO.getChiPhiBaoDuongDinhKy(nam);
    }
    
    public List<Map<String, Object>> getXeDuocThueNhieuNhat(int nam) {
        // Validate input
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (nam < 2000 || nam > currentYear) {
            throw new IllegalArgumentException("Năm không hợp lệ");
        }
        
        return thongKeDAO.getXeDuocThueNhieuNhat(nam);
    }
    
    public double getTongCongNo() {
        return thongKeDAO.getTongCongNo();
    }
}