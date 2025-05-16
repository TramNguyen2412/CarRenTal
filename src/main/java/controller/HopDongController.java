package controller;

import model.HopDong;
import model.ChiTietHD;
import model.Xe;
import service.HopDongService;
import service.XeService;
import service.NhanVienService;
import java.util.List;

public class HopDongController {
    private HopDongService hopDongService;
    private XeService xeService;
    private NhanVienService nhanVienService;
    private StringBuilder errorMessage = new StringBuilder();
    
    public HopDongController() {
        hopDongService = new HopDongService();
        xeService = new XeService();
        nhanVienService = new NhanVienService();
    }
    
    public List<HopDong> getAllHopDong() {
        return hopDongService.getAllHopDong();
    }
    
    public HopDong getHopDongByMa(String maHD) {
        return hopDongService.getHopDongByMa(maHD);
    }
    
    public String addHopDong(HopDong hd) {
        // Kiểm tra mã nhân viên
        if (hd.getMaNV() == null || hd.getMaNV().trim().isEmpty()) {
            errorMessage = new StringBuilder("Vui lòng chọn nhân viên phụ trách");
            return null;
        }
        
        // Kiểm tra mã NV có tồn tại không
        if (!nhanVienService.existsNhanVien(hd.getMaNV())) {
            errorMessage = new StringBuilder("Mã nhân viên không tồn tại trong hệ thống");
            return null;
        }
        
        errorMessage = new StringBuilder();
        String maHD = hopDongService.addHopDong(hd, errorMessage);
        return maHD;
    }
    
    public boolean updateHopDong(HopDong hd) {
        // Kiểm tra mã nhân viên
        if (hd.getMaNV() == null || hd.getMaNV().trim().isEmpty()) {
            errorMessage = new StringBuilder("Vui lòng chọn nhân viên phụ trách");
            return false;
        }
        
        // Kiểm tra mã NV có tồn tại không
        if (!nhanVienService.existsNhanVien(hd.getMaNV())) {
            errorMessage = new StringBuilder("Mã nhân viên không tồn tại trong hệ thống");
            return false;
        }
        
        errorMessage = new StringBuilder();
        return hopDongService.updateHopDong(hd, errorMessage);
    }
    
    public boolean deleteHopDong(String maHD) {
        errorMessage = new StringBuilder();
        return hopDongService.deleteHopDong(maHD, errorMessage);
    }
    
    public List<ChiTietHD> getChiTietHDByMaHD(String maHD) {
        return hopDongService.getChiTietHDByMaHD(maHD);
    }
    
    public boolean addChiTietHD(ChiTietHD ct) {
        errorMessage = new StringBuilder();
        return hopDongService.addChiTietHD(ct, errorMessage);
    }
    
    public boolean updateChiTietHD(ChiTietHD ct) {
        errorMessage = new StringBuilder();
        return hopDongService.updateChiTietHD(ct, errorMessage);
    }
    
    public boolean deleteChiTietHD(String maHD, String maXe) {
        errorMessage = new StringBuilder();
        return hopDongService.deleteChiTietHD(maHD, maXe, errorMessage);
    }
    
    public List<Xe> getAvailableCars() {
        return xeService.getXeByTrangThai("Sẵn sàng");
    }
    
    public List<HopDong> searchHopDong(String keyword, String trangThai) {
        return hopDongService.searchHopDong(keyword, trangThai);
    }
    
    public String getErrorMessage() {
        return errorMessage.toString();
    }
    
    // Phương thức liên quan đến nhân viên
    public List<model.NhanVien> getAllNhanVien() {
        return new NhanVienController().getAllNhanVien();
    }
    
    public boolean existsNhanVien(String maNV) {
        return nhanVienService.existsNhanVien(maNV);
    }
    
    public String getDefaultNhanVienMa() {
        return nhanVienService.getDefaultNhanVienMa();
    }
}