package controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import dao.ChiTietHDDao;
import dao.HopDongDAO;
import model.ChiTietHD;
import model.HopDong;
import model.NhanVien;
import model.Xe;


public class HopDongController {
    private HopDongDAO hopDongDAO;
    private ChiTietHDDao chiTietHDDAO;
    private XeController xeController;
    private NhanVienController nhanVienController;
    private StringBuilder errorMessage = new StringBuilder();
    
    public HopDongController() {
        hopDongDAO = new HopDongDAO();
        chiTietHDDAO = new ChiTietHDDao();
        xeController = new XeController();
        nhanVienController = new NhanVienController();
    }
    
    public List<HopDong> getAllHopDong() {
        return hopDongDAO.getAllHopDong();
    }
    
    public HopDong getHopDongByMa(String maHD) {
        return hopDongDAO.getHopDongByMa(maHD);
    }
    
    public String addHopDong(HopDong hd) {
        // Kiểm tra mã nhân viên
        if (hd.getMaNV() == null || hd.getMaNV().trim().isEmpty()) {
            errorMessage = new StringBuilder("Vui lòng chọn nhân viên phụ trách");
            return null;
        }
        
        // Kiểm tra mã NV có tồn tại không
        if (!nhanVienController.existsNhanVien(hd.getMaNV())) {
            errorMessage = new StringBuilder("Mã nhân viên không tồn tại trong hệ thống");
            return null;
        }
        
        errorMessage = new StringBuilder();
        return hopDongDAO.addHopDong(hd, errorMessage);
    }
    
    public String addHopDong(HopDong hd, StringBuilder customErrorMessage) {
        return hopDongDAO.addHopDong(hd, customErrorMessage);
    }
    
    public boolean updateHopDong(HopDong hd) {
        // Kiểm tra mã nhân viên
        if (hd.getMaNV() == null || hd.getMaNV().trim().isEmpty()) {
            errorMessage = new StringBuilder("Vui lòng chọn nhân viên phụ trách");
            return false;
        }
        
        // Kiểm tra mã NV có tồn tại không
        if (!nhanVienController.existsNhanVien(hd.getMaNV())) {
            errorMessage = new StringBuilder("Mã nhân viên không tồn tại trong hệ thống");
            return false;
        }
        
        errorMessage = new StringBuilder();
        return hopDongDAO.updateHopDong(hd, errorMessage);
    }
    
    public boolean updateHopDong(HopDong hd, StringBuilder customErrorMessage) {
        return hopDongDAO.updateHopDong(hd, customErrorMessage);
    }
    
    public boolean deleteHopDong(String maHD) {
        errorMessage = new StringBuilder();
        return hopDongDAO.deleteHopDong(maHD, errorMessage);
    }
    
    public boolean deleteHopDong(String maHD, StringBuilder customErrorMessage) {
        return hopDongDAO.deleteHopDong(maHD, customErrorMessage);
    }
    
    public List<ChiTietHD> getChiTietHopDong(String maHD) {
        return chiTietHDDAO.getChiTietHDByMaHD(maHD);
    }
    
    public boolean addChiTietHD(ChiTietHD ct) {
        errorMessage = new StringBuilder();
        try {
            boolean result = chiTietHDDAO.addChiTietHD(ct);
            if (!result) {
                errorMessage.append("Thêm chi tiết hợp đồng thất bại");
            }
            return result;
        } catch (SQLException e) {
            // Bắt và xử lý các lỗi từ trigger
            String msg = e.getMessage();
            if (msg.contains("ORA-20006")) {
                errorMessage.append("Xe đã có trong hợp đồng khác trùng thời gian thuê");
            } else if (msg.contains("ORA-20018")) {
                errorMessage.append("Xe có lịch bảo dưỡng trong khoảng thời gian thuê");
            } else if (msg.contains("ORA-20005")) {
                errorMessage.append("Xe không ở trạng thái 'Sẵn sàng' để thuê ngay");
            } else {
                errorMessage.append("Lỗi: ").append(msg);
            }
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            errorMessage.append("Lỗi không xác định: ").append(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean addChiTietHD(ChiTietHD ct, StringBuilder customErrorMessage) {
        try {
            boolean result = chiTietHDDAO.addChiTietHD(ct);
            if (!result) {
                customErrorMessage.append("Thêm chi tiết hợp đồng thất bại");
            }
            return result;
        } catch (SQLException e) {
            // Bắt và xử lý các lỗi từ trigger
            String msg = e.getMessage();
            if (msg.contains("ORA-20006")) {
                customErrorMessage.append("Xe đã có trong hợp đồng khác trùng thời gian thuê");
            } else if (msg.contains("ORA-20018")) {
                customErrorMessage.append("Xe có lịch bảo dưỡng trong khoảng thời gian thuê");
            } else if (msg.contains("ORA-20005")) {
                customErrorMessage.append("Xe không ở trạng thái 'Sẵn sàng' để thuê ngay");
            } else {
                customErrorMessage.append("Lỗi: ").append(msg);
            }
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            customErrorMessage.append("Lỗi không xác định: ").append(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateChiTietHD(ChiTietHD ct) {
        errorMessage = new StringBuilder();
        try {
            boolean result = chiTietHDDAO.updateChiTietHD(ct);
            if (!result) {
                errorMessage.append("Cập nhật chi tiết hợp đồng thất bại");
            }
            return result;
        } catch (SQLException e) {
            // Bắt và xử lý các lỗi từ trigger
            String msg = e.getMessage();
            if (msg.contains("ORA-20006")) {
                errorMessage.append("Xe đã có trong hợp đồng khác trùng thời gian thuê");
            } else if (msg.contains("ORA-20018")) {
                errorMessage.append("Xe có lịch bảo dưỡng trong khoảng thời gian thuê");
            } else if (msg.contains("ORA-20005")) {
                errorMessage.append("Xe không ở trạng thái 'Sẵn sàng' để thuê ngay");
            } else {
                errorMessage.append("Lỗi: ").append(msg);
            }
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            errorMessage.append("Lỗi không xác định: ").append(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateChiTietHD(ChiTietHD ct, StringBuilder customErrorMessage) {
        try {
            boolean result = chiTietHDDAO.updateChiTietHD(ct);
            if (!result) {
                customErrorMessage.append("Cập nhật chi tiết hợp đồng thất bại");
            }
            return result;
        } catch (SQLException e) {
            // Bắt và xử lý các lỗi từ trigger
            String msg = e.getMessage();
            if (msg.contains("ORA-20006")) {
                customErrorMessage.append("Xe đã có trong hợp đồng khác trùng thời gian thuê");
            } else if (msg.contains("ORA-20018")) {
                customErrorMessage.append("Xe có lịch bảo dưỡng trong khoảng thời gian thuê");
            } else if (msg.contains("ORA-20005")) {
                customErrorMessage.append("Xe không ở trạng thái 'Sẵn sàng' để thuê ngay");
            } else {
                customErrorMessage.append("Lỗi: ").append(msg);
            }
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            customErrorMessage.append("Lỗi không xác định: ").append(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteChiTietHD(String maHD, String maXe) {
        errorMessage = new StringBuilder();
        try {
            boolean result = chiTietHDDAO.deleteChiTietHD(maHD, maXe);
            if (!result) {
                errorMessage.append("Xóa chi tiết hợp đồng thất bại");
            }
            return result;
        } catch (SQLException e) {
            errorMessage.append("Lỗi xóa chi tiết hợp đồng: ").append(e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            errorMessage.append("Lỗi không xác định: ").append(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteChiTietHD(String maHD, String maXe, StringBuilder customErrorMessage) {
        try {
            boolean result = chiTietHDDAO.deleteChiTietHD(maHD, maXe);
            if (!result) {
                customErrorMessage.append("Xóa chi tiết hợp đồng thất bại");
            }
            return result;
        } catch (SQLException e) {
            customErrorMessage.append("Lỗi xóa chi tiết hợp đồng: ").append(e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            customErrorMessage.append("Lỗi không xác định: ").append(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteChiTietHDByMaHD(String maHD) {
        errorMessage = new StringBuilder();
        try {
            boolean result = chiTietHDDAO.deleteChiTietHDByMaHD(maHD);
            if (!result) {
                errorMessage.append("Xóa các chi tiết hợp đồng thất bại");
            }
            return result;
        } catch (SQLException e) {
            errorMessage.append("Lỗi xóa chi tiết hợp đồng: ").append(e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            errorMessage.append("Lỗi không xác định: ").append(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteChiTietHDByMaHD(String maHD, StringBuilder customErrorMessage) {
        try {
            boolean result = chiTietHDDAO.deleteChiTietHDByMaHD(maHD);
            if (!result) {
                customErrorMessage.append("Xóa các chi tiết hợp đồng thất bại");
            }
            return result;
        } catch (SQLException e) {
            customErrorMessage.append("Lỗi xóa chi tiết hợp đồng: ").append(e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            customErrorMessage.append("Lỗi không xác định: ").append(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Xe> getAvailableCars() {
        return xeController.getXeByTrangThai("Sẵn sàng");
    }
    
    public List<HopDong> searchHopDong(String keyword, String trangThai) {
        return hopDongDAO.searchHopDong(keyword, trangThai);
    }
    
    public String getErrorMessage() {
        return errorMessage.toString();
    }
    
    // Phương thức liên quan đến nhân viên
    public List<NhanVien> getAllNhanVien() {
        return nhanVienController.getAllNhanVien();
    }
    
    public boolean existsNhanVien(String maNV) {
        return nhanVienController.existsNhanVien(maNV);
    }
    
    public String getDefaultNhanVienMa() {
        return nhanVienController.getDefaultNhanVienMa();
    }
    public String kiemTraXeThueDuoc(String maXe, Date ngayBatDau, Date ngayKetThuc, String maHDHienTai) {
         return chiTietHDDAO.kiemTraXeThueDuoc(maXe, ngayBatDau, ngayKetThuc, maHDHienTai);
    }
// Lấy tất cả hợp đồng của một khách hàng
public List<HopDong> getHopDongByKhachHang(String maKH) {
    List<HopDong> all = hopDongDAO.getAllHopDong();
    List<HopDong> result = new java.util.ArrayList<>();
    for (HopDong hd : all) {
        if (hd.getMaKH() != null && hd.getMaKH().equals(maKH)) {
            result.add(hd);
        }
    }
    return result;
}

public List<ChiTietHD> getChiTietHDByMaHD(String maHD) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getChiTietHDByMaHD'");
}
}