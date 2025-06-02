package service;

import java.util.List;

import dao.GiaoNhanXeDAO;
import model.GiaoNhanXe;

public class GiaoNhanXeService {
    private GiaoNhanXeDAO giaoNhanXeDAO;
    private StringBuilder errorMessage = new StringBuilder();

    public GiaoNhanXeService() {
        giaoNhanXeDAO = new GiaoNhanXeDAO();
      
    }

    public List<GiaoNhanXe> getAllGiaoNhanXe() {
        return giaoNhanXeDAO.getAllGiaoNhanXe();
    }

    public GiaoNhanXe getGiaoNhanXeByMa(String maGiaoNhan) {
        return giaoNhanXeDAO.getGiaoNhanXeByMa(maGiaoNhan);
    }

    public String addGiaoNhanXe(GiaoNhanXe gn) {
        errorMessage = new StringBuilder();
        String validationError = validateGiaoNhanXeData(gn, true); // true for new record
        if (validationError != null) {
            errorMessage.append(validationError);
            return null;
        }
     
        return giaoNhanXeDAO.addGiaoNhanXe(gn);
    }

    public boolean updateGiaoNhanXe(GiaoNhanXe gn) {
        errorMessage = new StringBuilder();
        if (gn.getMaGiaoNhan() == null || gn.getMaGiaoNhan().trim().isEmpty()) {
            errorMessage.append("Mã giao nhận không được để trống khi cập nhật.");
            return false;
        }
        if (!giaoNhanXeDAO.existsGiaoNhanXe(gn.getMaGiaoNhan())) {
            errorMessage.append("Không tìm thấy bản ghi giao nhận xe với mã: ").append(gn.getMaGiaoNhan());
            return false;
        }

        String validationError = validateGiaoNhanXeData(gn, false); // false for existing record
        if (validationError != null) {
            errorMessage.append(validationError);
            return false;
        }
        return giaoNhanXeDAO.updateGiaoNhanXe(gn);
    }

    public boolean deleteGiaoNhanXe(String maGiaoNhan) {
        errorMessage = new StringBuilder();
        if (maGiaoNhan == null || maGiaoNhan.trim().isEmpty()) {
            errorMessage.append("Mã giao nhận không hợp lệ.");
            return false;
        }
        if (!giaoNhanXeDAO.existsGiaoNhanXe(maGiaoNhan)) {
            errorMessage.append("Không tìm thấy bản ghi giao nhận xe với mã ").append(maGiaoNhan);
            return false;
        }
        return giaoNhanXeDAO.deleteGiaoNhanXe(maGiaoNhan);
    }

    public List<GiaoNhanXe> searchGiaoNhanXe(String keyword) {
        return giaoNhanXeDAO.searchGiaoNhanXe(keyword);
    }

    public boolean existsGiaoNhanXe(String maGiaoNhan) {
        return giaoNhanXeDAO.existsGiaoNhanXe(maGiaoNhan);
    }

    private String validateGiaoNhanXeData(GiaoNhanXe gn, boolean isNew) {
        if (gn.getMaHD() == null || gn.getMaHD().trim().isEmpty()) {
            return "Mã hợp đồng không được để trống.";
        }
 
        if (gn.getMaXe() == null || gn.getMaXe().trim().isEmpty()) {
            return "Mã xe không được để trống.";
        }
    
        if (gn.getMaNV() == null || gn.getMaNV().trim().isEmpty()) {
            return "Mã nhân viên không được để trống.";
        }

        if (gn.getTrangThaiXe() == null || gn.getTrangThaiXe().trim().isEmpty()) {
            return "Trạng thái xe không được để trống.";
        }
        if (gn.getTrangThaiGN() == null || gn.getTrangThaiGN().trim().isEmpty()) {
            return "Trạng thái giao nhận (Đã giao/Đã nhận về) không được để trống.";
        }
        if (!"Đã giao".equals(gn.getTrangThaiGN()) && !"Đã nhận về".equals(gn.getTrangThaiGN())) {
            return "Trạng thái giao nhận không hợp lệ. Chỉ chấp nhận 'Đã giao' hoặc 'Đã nhận về'.";
        }


        return null; // No validation errors
    }

    public String getErrorMessage() {
        return errorMessage.toString();
    }
}