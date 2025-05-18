package service;

import dao.DichVuBDDAO;
import dao.ChiTietBaoDuongDAO;
import model.DichVuBD;
import java.util.List;

public class DichVuBDService {
    private DichVuBDDAO dichVuBDDAO;
    private ChiTietBaoDuongDAO chiTietBaoDuongDAO;
    
    public DichVuBDService() {
        dichVuBDDAO = new DichVuBDDAO();
        chiTietBaoDuongDAO = new ChiTietBaoDuongDAO();
    }
    
    public List<DichVuBD> getAllDichVuBD() {
        return dichVuBDDAO.getAllDichVuBD();
    }
    
    public DichVuBD getDichVuBDById(String maDV) {
        return dichVuBDDAO.getDichVuBDByMaDV(maDV);
    }
    
    public boolean addDichVuBD(DichVuBD dv) {
        // Validate input
        if (dv.getTenDV() == null || dv.getTenDV().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên dịch vụ không được để trống");
        }
        
        if (dv.getGiaDV() < 0) {
            throw new IllegalArgumentException("Giá dịch vụ không được âm");
        }
        
        // Using the stored procedure sp_ThemDichVu
        return dichVuBDDAO.addDichVuBD(dv);
    }
    
    public boolean updateDichVuBD(DichVuBD dv) {
        // Validate input
        if (dv.getMaDV() == null || dv.getMaDV().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã dịch vụ không được để trống");
        }
        
        if (dv.getTenDV() == null || dv.getTenDV().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên dịch vụ không được để trống");
        }
        
        if (dv.getGiaDV() < 0) {
            throw new IllegalArgumentException("Giá dịch vụ không được âm");
        }
        
        // The trg_DICHVUBD_UpdateGiaDV trigger will handle updating maintenance costs
        return dichVuBDDAO.updateDichVuBD(dv);
    }
    
    public boolean deleteDichVuBD(String maDV) {
        // Check if service is used in any maintenance record
        if (chiTietBaoDuongDAO.isServiceUsedInMaintenance(maDV)) {
            throw new IllegalStateException("Không thể xóa dịch vụ này vì đã được sử dụng trong phiếu bảo dưỡng");
        }
        
        return dichVuBDDAO.deleteDichVuBD(maDV);
    }
    
    public List<DichVuBD> searchDichVuBD(String keyword) {
        return dichVuBDDAO.searchDichVuBD(keyword);
    }
}