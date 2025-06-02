package controller;

import dao.ThongKeDAO;
import model.KhachHangDoanhThu;
import model.XeDoanhThu;

import java.util.List;
import java.util.Map;
import java.util.Calendar;
import dao.HopDongDAO;
public class ThongKeController {
    private ThongKeDAO thongKeDAO;
    
    public ThongKeController() {
        this.thongKeDAO = new ThongKeDAO();
    }
    
    public Map<String, Number> getTongQuan() {
        return thongKeDAO.getTongQuan();
    }
    
    public Map<Integer, Double> getDoanhThuTheoThang(int nam) {
        // Kiểm tra tính hợp lệ của năm
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (nam < 1900 || nam > currentYear) {
            throw new IllegalArgumentException("Năm không hợp lệ: " + nam);
        }
        
        return thongKeDAO.getDoanhThuTheoThang(nam);
    }
    
    public List<KhachHangDoanhThu> getDoanhThuTheoKhachHang(int nam) {
        // Kiểm tra tính hợp lệ của năm
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (nam < 1900 || nam > currentYear) {
            throw new IllegalArgumentException("Năm không hợp lệ: " + nam);
        }
        
        return thongKeDAO.getDoanhThuTheoKhachHang(nam);
    }
    
    public List<XeDoanhThu> getDoanhThuTheoXe(int nam) {
        // Kiểm tra tính hợp lệ của năm
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (nam < 1900 || nam > currentYear) {
            throw new IllegalArgumentException("Năm không hợp lệ: " + nam);
        }
        
        return thongKeDAO.getDoanhThuTheoXe(nam);
    }
    public List<Map<String, Object>> getTop5HopDong(int year) {
        return HopDongDAO.getTop5HopDong(year);
    }
    public void startReportView() {
        thongKeDAO.startReportView();
    }

    public void endReportView() {
        thongKeDAO.endReportView();
    }
    
}