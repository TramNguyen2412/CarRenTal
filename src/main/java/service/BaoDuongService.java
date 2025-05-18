package service;

import dao.PhieuBaoDuongDAO;
import dao.ChiTietBaoDuongDAO;
import dao.XeDAO;
import dao.KhachHangDAO;
import model.PhieuBaoDuong;
import model.ChiTietBaoDuong;
import model.Xe;
import java.util.Date;
import java.util.List;

public class BaoDuongService {
    private PhieuBaoDuongDAO phieuBaoDuongDAO;
    private ChiTietBaoDuongDAO chiTietBaoDuongDAO;
    private XeDAO xeDAO;
    private KhachHangDAO khachHangDAO;
    
    public BaoDuongService() {
        phieuBaoDuongDAO = new PhieuBaoDuongDAO();
        chiTietBaoDuongDAO = new ChiTietBaoDuongDAO();
        xeDAO = new XeDAO();
        khachHangDAO = new KhachHangDAO();
    }
    
    public List<PhieuBaoDuong> getAllPhieuBaoDuong() {
        return phieuBaoDuongDAO.getAllPhieuBaoDuong();
    }
    
    public PhieuBaoDuong getPhieuBaoDuongById(String maBD) {
        return phieuBaoDuongDAO.getPhieuBaoDuongByMaBD(maBD);
    }
    
    public List<ChiTietBaoDuong> getChiTietByMaBD(String maBD) {
        return chiTietBaoDuongDAO.getChiTietBaoDuongByPhieuBD(maBD);
    }
    
    public boolean addPhieuBaoDuong(PhieuBaoDuong phieu) {
        // Validate input
        if (phieu.getMaXe() == null || phieu.getMaXe().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn xe");
        }
        
        if (phieu.getNgayBD() == null) {
            throw new IllegalArgumentException("Vui lòng chọn ngày bảo dưỡng");
        }
        
        if (phieu.getMaNV() == null || phieu.getMaNV().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn nhân viên phụ trách");
        }
        
        if (phieu.getLoaiBD() == null || phieu.getLoaiBD().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn loại bảo dưỡng");
        }
        
        // Validate business rules
        if (phieu.getLoaiBD().equals("Khách gây hư hại") && 
            (phieu.getMaKH() == null || phieu.getMaKH().trim().isEmpty())) {
            throw new IllegalArgumentException("Bảo dưỡng do khách gây hư hại phải có thông tin khách hàng");
        }
        
        if (phieu.getLoaiBD().equals("Định Kỳ") && phieu.getMaKH() != null && !phieu.getMaKH().trim().isEmpty()) {
            throw new IllegalArgumentException("Bảo dưỡng định kỳ không được gán cho khách hàng");
        }
        
        // Check if car exists
        Xe xe = xeDAO.getXeByMa(phieu.getMaXe());
        if (xe == null) {
            throw new IllegalArgumentException("Xe không tồn tại");
        }
        
        // Check if car is in contract on the maintenance date
        if (phieuBaoDuongDAO.isCarInContract(phieu.getMaXe(), phieu.getNgayBD())) {
            throw new IllegalArgumentException("Xe đang được thuê trong ngày bảo dưỡng. Vui lòng chọn ngày khác.");
        }
        
        // The TRG_PHIEUBAODUONG_INSERT_COMPOUND trigger will handle updating car status
        // The TRG_PHIEUBAODUONG_CHECK_DUPLICATE trigger will prevent duplicate maintenance records
        return phieuBaoDuongDAO.addPhieuBaoDuong(phieu);
    }
    
    public boolean updatePhieuBaoDuong(PhieuBaoDuong phieu) {
        // Validate input
        if (phieu.getMaBD() == null || phieu.getMaBD().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu bảo dưỡng không được để trống");
        }
        
        if (phieu.getMaXe() == null || phieu.getMaXe().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn xe");
        }
        
        if (phieu.getNgayBD() == null) {
            throw new IllegalArgumentException("Vui lòng chọn ngày bảo dưỡng");
        }
        
        if (phieu.getMaNV() == null || phieu.getMaNV().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn nhân viên phụ trách");
        }
        
        if (phieu.getLoaiBD() == null || phieu.getLoaiBD().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn loại bảo dưỡng");
        }
        
        // Validate business rules
        if (phieu.getLoaiBD().equals("Khách gây hư hại") && 
            (phieu.getMaKH() == null || phieu.getMaKH().trim().isEmpty())) {
            throw new IllegalArgumentException("Bảo dưỡng do khách gây hư hại phải có thông tin khách hàng");
        }
        
        if (phieu.getLoaiBD().equals("Định Kỳ") && phieu.getMaKH() != null && !phieu.getMaKH().trim().isEmpty()) {
            throw new IllegalArgumentException("Bảo dưỡng định kỳ không được gán cho khách hàng");
        }
        
        // Get the original record to check for changes
        PhieuBaoDuong originalPhieu = phieuBaoDuongDAO.getPhieuBaoDuongByMaBD(phieu.getMaBD());

        if (originalPhieu == null) {
            throw new IllegalArgumentException("Phiếu bảo dưỡng gốc không tồn tại");
        }

        // If changing car or date, check if the new car is in contract on the new date
        boolean isMaXeChanged = !originalPhieu.getMaXe().equals(phieu.getMaXe());
        boolean isNgayBDChanged = !originalPhieu.getNgayBD().equals(phieu.getNgayBD());

        if ((isMaXeChanged || isNgayBDChanged) && phieuBaoDuongDAO.isCarInContract(phieu.getMaXe(), phieu.getNgayBD())) {
            throw new IllegalArgumentException("Xe đang được thuê trong ngày bảo dưỡng. Vui lòng chọn ngày khác.");
        }

        
        // The TRG_PHIEUBAODUONG_UPDATE_COMPOUND trigger will handle updating car status
        // The TRG_PHIEUBAODUONG_CHECK_DUPLICATE trigger will prevent duplicate maintenance records
        // The trg_Upd_Ins_Del_From_PBD trigger will handle updating customer debt
        return phieuBaoDuongDAO.updatePhieuBaoDuong(phieu);
    }
    
    public boolean deletePhieuBaoDuong(String maBD) {
        // The TRG_PHIEUBAODUONG_DELETE_COMPOUND trigger will handle updating car status
        // The trg_Upd_Ins_Del_From_PBD trigger will handle updating customer debt
        return phieuBaoDuongDAO.deletePhieuBaoDuong(maBD);
    }
    
    public boolean addChiTietBaoDuong(ChiTietBaoDuong ct) {
        // Validate input
        if (ct.getMaBD() == null || ct.getMaBD().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu bảo dưỡng không được để trống");
        }
        
        if (ct.getMaDV() == null || ct.getMaDV().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn dịch vụ");
        }
        
        if (ct.getSoLuong() <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        
        // The trg_CHITIETBAODUONG_ins_upd_Del trigger will handle updating maintenance total cost
        return chiTietBaoDuongDAO.addChiTietBaoDuong(ct);
    }
    
    public boolean updateChiTietBaoDuong(ChiTietBaoDuong ct) {
        // Validate input
        if (ct.getMaBD() == null || ct.getMaBD().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu bảo dưỡng không được để trống");
        }
        
        if (ct.getMaDV() == null || ct.getMaDV().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn dịch vụ");
        }
        
        if (ct.getSoLuong() <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        
        // The trg_CHITIETBAODUONG_ins_upd_Del trigger will handle updating maintenance total cost
        return chiTietBaoDuongDAO.updateChiTietBaoDuong(ct);
    }
    
    public boolean deleteChiTietBaoDuong(String maBD, String maDV) {
        // The trg_CHITIETBAODUONG_ins_upd_Del trigger will handle updating maintenance total cost
        return chiTietBaoDuongDAO.deleteChiTietBaoDuong(maBD, maDV);
    }
    
    public List<PhieuBaoDuong> searchPhieuBaoDuong(String keyword) {
        return phieuBaoDuongDAO.searchPhieuBaoDuong(keyword);
    }
    
    public List<PhieuBaoDuong> getPhieuBaoDuongByKhachHang(String maKH) {
        return phieuBaoDuongDAO.getPhieuBaoDuongByMaKhachHang(maKH);
    }
}