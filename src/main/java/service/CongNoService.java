package service;

import java.util.List;

import dao.CongNoDAO;
import dao.KhachHangDAO;
import model.KhachHang;
import model.LichSuCongNo;

public class CongNoService {
    private CongNoDAO congNoDAO;
    private KhachHangDAO khachHangDAO;
    
    public CongNoService() {
        congNoDAO = new CongNoDAO();
        khachHangDAO = new KhachHangDAO();
    }
    
    public List<LichSuCongNo> getAllLichSuCongNo() {
        return congNoDAO.getAllLichSuCongNo();
    }
    
    public List<LichSuCongNo> getLichSuCongNoByKhachHang(String maKH) {
        return congNoDAO.getLichSuCongNoByKhachHang(maKH);
    }
    
    public boolean addLichSuCongNo(LichSuCongNo ls) {
        // Validate input
        if (ls.getMaKH() == null || ls.getMaKH().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn khách hàng");
        }
        
        if (ls.getNgayGiaoDich() == null) {
            throw new IllegalArgumentException("Vui lòng chọn ngày giao dịch");
        }
        
        if (ls.getLoaiGiaoDich() == null || ls.getLoaiGiaoDich().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn loại giao dịch");
        }
        
        if (ls.getSoTien() <= 0) {
            throw new IllegalArgumentException("Số tiền phải lớn hơn 0");
        }
        
        // The trg_Update_ins_del_LSCN trigger will handle updating customer debt
        // The trg_kiem_tra_thanhtoan trigger will validate payment amount
        try {
            return congNoDAO.addLichSuCongNo(ls);
        } catch (Exception e) {
            // Extract the Oracle error message
            String message = e.getMessage();
            if (message.contains("ORA-20003")) {
                throw new IllegalArgumentException("Số tiền thanh toán vượt quá tổng công nợ của khách hàng");
            } else if (message.contains("ORA-20010")) {
                throw new IllegalArgumentException("Không thể thêm giao dịch: Công nợ sau sẽ âm");
            } else {
                throw new RuntimeException("Lỗi: " + e.getMessage());
            }
            
        }
    }
    
    public boolean updateLichSuCongNo(LichSuCongNo ls) {
        // Validate input
        if (ls.getMaLichSu() == null || ls.getMaLichSu().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch sử không được để trống");
        }
        
        if (ls.getMaKH() == null || ls.getMaKH().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn khách hàng");
        }
        
        if (ls.getNgayGiaoDich() == null) {
            throw new IllegalArgumentException("Vui lòng chọn ngày giao dịch");
        }
        
        if (ls.getLoaiGiaoDich() == null || ls.getLoaiGiaoDich().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn loại giao dịch");
        }
        
        if (ls.getSoTien() <= 0) {
            throw new IllegalArgumentException("Số tiền phải lớn hơn 0");
        }
        
        // The trg_Update_ins_del_LSCN trigger will handle updating customer debt
        // The trg_kiem_tra_thanhtoan trigger will validate payment amount
        try {
            return congNoDAO.updateLichSuCongNo(ls);
        } catch (Exception e) {
            // Extract the Oracle error message
            String message = e.getMessage();
            if (message.contains("ORA-20003")) {
                throw new IllegalArgumentException("Số tiền thanh toán vượt quá tổng công nợ của khách hàng");
            } else if (message.contains("ORA-20012")) {
                throw new IllegalArgumentException("Không thể cập nhật: Công nợ sau sẽ âm");
            } else if (message.contains("ORA-20013")) {
                throw new IllegalArgumentException("Không thể chuyển giao dịch: Công nợ sau sẽ âm");
            } else {
                throw new RuntimeException("Lỗi: " + e.getMessage());
            }
        }
    }
    
    public boolean deleteLichSuCongNo(String maLichSu) {
        // The trg_Update_ins_del_LSCN trigger will handle updating customer debt
        try {
            return congNoDAO.deleteLichSuCongNo(maLichSu);
        } catch (Exception e) {
            // Extract the Oracle error message
            String message = e.getMessage();
            if (message.contains("ORA-20011")) {
                throw new IllegalArgumentException("Không thể xóa giao dịch: Công nợ sau sẽ âm");
            } else {
                throw new RuntimeException("Lỗi: " + e.getMessage());
            }
        }
    }
    
    public List<KhachHang> getKhachHangCoCongNo() {
        return congNoDAO.getKhachHangCoCongNo();
    }
    
    public double getTongCongNoKhachHang(String maKH) {
        return congNoDAO.getTongCongNoKhachHang(maKH);
    }
    public LichSuCongNo getLichSuCongNoByMa(String maLichSu) {
    return congNoDAO.getLichSuCongNoByMa(maLichSu);
}
}