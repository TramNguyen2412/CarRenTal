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
        // NÊN lấy message nghiệp vụ như ở Service (bên dưới), không chỉ e.printStackTrace()
        String msg = e.getMessage();
        if (msg != null && msg.contains("ORA-200")) {
            String[] lines = msg.split("\\r?\\n");
            for (String line : lines) {
                if (line.contains("ORA-200")) {
                    int idx = line.indexOf(":");
                    if (idx > 0 && idx < line.length() - 1) {
                        return line.substring(idx + 1).trim();
                    }
                    return line.trim();
                }
            }
            return msg;
        }
        return "Lỗi hệ thống: " + msg;
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
        // NÊN lấy message nghiệp vụ như ở Service (bên dưới), không chỉ e.printStackTrace()
        String msg = e.getMessage();
        if (msg != null && msg.contains("ORA-200")) {
            String[] lines = msg.split("\\r?\\n");
            for (String line : lines) {
                if (line.contains("ORA-200")) {
                    int idx = line.indexOf(":");
                    if (idx > 0 && idx < line.length() - 1) {
                        return line.substring(idx + 1).trim();
                    }
                    return line.trim();
                }
            }
            return msg;
        }
        return "Lỗi hệ thống: " + msg;
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
        // NÊN lấy message nghiệp vụ như ở Service (bên dưới), không chỉ e.printStackTrace()
        String msg = e.getMessage();
        if (msg != null && msg.contains("ORA-200")) {
            String[] lines = msg.split("\\r?\\n");
            for (String line : lines) {
                if (line.contains("ORA-200")) {
                    int idx = line.indexOf(":");
                    if (idx > 0 && idx < line.length() - 1) {
                        return line.substring(idx + 1).trim();
                    }
                    return line.trim();
                }
            }
            return msg;
        }
        return "Lỗi hệ thống: " + msg;
        }
    }
    
    public List<DichVuBD> searchDichVuBD(String keyword) {
        return dichVuBDService.searchDichVuBD(keyword);
    }
}