package controller;

import service.DichVuBDService;
import model.DichVuBD;
import java.util.List;

public class DichVuBDController {
    private DichVuBDService dichVuBDService;
    
    public DichVuBDController() {
        dichVuBDService = new DichVuBDService();
    }
    
    public List<DichVuBD> getAllDichVuBD() {
        return dichVuBDService.getAllDichVuBD();
    }
    
    public DichVuBD getDichVuBDById(String maDV) {
        return dichVuBDService.getDichVuBDById(maDV);
    }
    
    public String addDichVuBD(String tenDV, double giaDV) {
        try {
            DichVuBD dv = new DichVuBD();
            dv.setTenDV(tenDV);
            dv.setGiaDV(giaDV);
            
            boolean success = dichVuBDService.addDichVuBD(dv);
            
            if (success) {
                return "Thêm dịch vụ thành công";
            } else {
                return "Thêm dịch vụ thất bại";
            }
        } catch (IllegalArgumentException e) {
            return "Lỗi: " + e.getMessage();
        } catch (Exception e) {
            return "Lỗi hệ thống: " + e.getMessage();
        }
    }
    
    public String updateDichVuBD(String maDV, String tenDV, double giaDV) {
        try {
            DichVuBD dv = new DichVuBD();
            dv.setMaDV(maDV);
            dv.setTenDV(tenDV);
            dv.setGiaDV(giaDV);
            
            boolean success = dichVuBDService.updateDichVuBD(dv);
            
            if (success) {
                return "Cập nhật dịch vụ thành công";
            } else {
                return "Cập nhật dịch vụ thất bại";
            }
        } catch (IllegalArgumentException e) {
            return "Lỗi: " + e.getMessage();
        } catch (Exception e) {
            return "Lỗi hệ thống: " + e.getMessage();
        }
    }
    
    public String deleteDichVuBD(String maDV) {
        try {
            boolean success = dichVuBDService.deleteDichVuBD(maDV);
            
            if (success) {
                return "Xóa dịch vụ thành công";
            } else {
                return "Xóa dịch vụ thất bại";
            }
        } catch (IllegalStateException e) {
            return "Lỗi: " + e.getMessage();
        } catch (Exception e) {
            return "Lỗi hệ thống: " + e.getMessage();
        }
    }
    
    public List<DichVuBD> searchDichVuBD(String keyword) {
        return dichVuBDService.searchDichVuBD(keyword);
    }
}