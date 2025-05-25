package controller;

import dao.DanhGiaDAO;
import model.DanhGia;
import java.util.Date;
import java.util.List;
import java.util.Map;
import dao.ChiTietHDDao;
import model.ChiTietHD;
public class DanhGiaController {
    private DanhGiaDAO danhGiaDAO;
    private StringBuilder errorMessage;
    private ChiTietHDDao chiTietHDDAO;
    public DanhGiaController() {
        danhGiaDAO = new DanhGiaDAO();
        errorMessage = new StringBuilder();chiTietHDDAO = new ChiTietHDDao();
    }
    
    // Lấy danh sách đánh giá của khách hàng
    public List<DanhGia> getDanhGiaByMaKH(String maKH) {
        return danhGiaDAO.getDanhGiaByMaKH(maKH);
    }
    
    // Lấy danh sách hợp đồng hoàn thành chưa đánh giá
    public List<Map<String, Object>> getHopDongChuaDanhGia(String maKH) {
        return danhGiaDAO.getHopDongChuaDanhGia(maKH);
    }
    
    // Lấy danh sách hợp đồng đã đánh giá
    public List<Map<String, Object>> getHopDongDaDanhGia(String maKH) {
        return danhGiaDAO.getHopDongDaDanhGia(maKH);
    }
    
    // Thêm đánh giá mới
    public boolean themDanhGia(String maKH, String maHD, int diemSo, String binhLuan) {
        errorMessage = new StringBuilder();
        
        // Kiểm tra dữ liệu đầu vào
        if (maKH == null || maKH.trim().isEmpty()) {
            errorMessage.append("Mã khách hàng không hợp lệ");
            return false;
        }
        
        if (maHD == null || maHD.trim().isEmpty()) {
            errorMessage.append("Mã hợp đồng không hợp lệ");
            return false;
        }
        
        if (diemSo < 1 || diemSo > 5) {
            errorMessage.append("Điểm đánh giá phải từ 1-5 sao");
            return false;
        }
        
        // Kiểm tra hợp đồng đã được đánh giá chưa
        if (danhGiaDAO.kiemTraHopDongDaDanhGia(maHD)) {
            errorMessage.append("Hợp đồng này đã được đánh giá");
            return false;
        }
        
        // Tạo đối tượng đánh giá mới
        DanhGia danhGia = new DanhGia();
        danhGia.setMaKH(maKH);
        danhGia.setMaHD(maHD);
        danhGia.setDiemSo(diemSo);
        danhGia.setBinhLuan(binhLuan);
        danhGia.setNgayDanhGia(new Date());
        
        // Thêm đánh giá vào CSDL
        return danhGiaDAO.themDanhGia(danhGia);
    }
    
    // Cập nhật đánh giá
    public boolean capNhatDanhGia(String maDG, int diemSo, String binhLuan) {
        errorMessage = new StringBuilder();
        
        // Kiểm tra dữ liệu đầu vào
        if (maDG == null || maDG.trim().isEmpty()) {
            errorMessage.append("Mã đánh giá không hợp lệ");
            return false;
        }
        
        if (diemSo < 1 || diemSo > 5) {
            errorMessage.append("Điểm đánh giá phải từ 1-5 sao");
            return false;
        }
        
        // Tạo đối tượng đánh giá để cập nhật
        DanhGia danhGia = new DanhGia();
        danhGia.setMaDG(maDG);
        danhGia.setDiemSo(diemSo);
        danhGia.setBinhLuan(binhLuan);
        danhGia.setNgayDanhGia(new Date());
        
        // Cập nhật đánh giá
        return danhGiaDAO.capNhatDanhGia(danhGia);
    }
    
    // Xóa đánh giá
    public boolean xoaDanhGia(String maDG) {
        errorMessage = new StringBuilder();
        
        if (maDG == null || maDG.trim().isEmpty()) {
            errorMessage.append("Mã đánh giá không hợp lệ");
            return false;
        }
        
        return danhGiaDAO.xoaDanhGia(maDG);
    }
     public List<ChiTietHD> getChiTietHopDong(String maHD) {
        return chiTietHDDAO.getChiTietHDByMaHD(maHD);
    }
    // Lấy thông báo lỗi
    public String getErrorMessage() {
        return errorMessage.toString();
    }
    

}