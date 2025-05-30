package service;

import java.util.List;

import dao.ChiTietBaoDuongDAO;
import dao.KhachHangDAO;
import dao.PhieuBaoDuongDAO;
import dao.XeDAO;
import model.ChiTietBaoDuong;
import model.PhieuBaoDuong;
import model.Xe;

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
    // Lấy tất cả xe
    public List<Xe> getAllXe() {
        return xeDAO.getAllXe();
    }

    // Lấy xe theo mã
    public Xe getXeByMa(String maXe) {
        return xeDAO.getXeByMa(maXe);
    }

    // Lấy tất cả khách hàng
    public List<model.KhachHang> getAllKhachHang() {
        return khachHangDAO.getAllKhachHang();
    }

    // Lấy khách hàng theo mã
    public model.KhachHang getKhachHangByMa(String maKH) {
        return khachHangDAO.getKhachHangByMa(maKH);
    }

    // Lấy tất cả nhân viên
    public List<model.NhanVien> getAllNhanVien() {
        dao.NhanVienDAO nhanVienDAO = new dao.NhanVienDAO();
        return nhanVienDAO.getAllNhanVien();
    }

    // Lấy nhân viên theo mã
    public model.NhanVien getNhanVienByMa(String maNV) {
        dao.NhanVienDAO nhanVienDAO = new dao.NhanVienDAO();
        return nhanVienDAO.getNhanVienByMa(maNV);
    }

    // Lấy tất cả dịch vụ bảo dưỡng
    public List<model.DichVuBD> getAllDichVuBD() {
        dao.DichVuBDDAO dichVuBDDAO = new dao.DichVuBDDAO();
        return dichVuBDDAO.getAllDichVuBD();
    }

    // Lấy dịch vụ bảo dưỡng theo mã
    public model.DichVuBD getDichVuBDById(String maDV) {
        dao.DichVuBDDAO dichVuBDDAO = new dao.DichVuBDDAO();
        return dichVuBDDAO.getDichVuBDByMaDV(maDV);
    }

    // Tìm kiếm phiếu bảo dưỡng theo keyword và loại bảo dưỡng
    public List<PhieuBaoDuong> searchPhieuBaoDuong(String keyword, String loaiBD) {
        return phieuBaoDuongDAO.searchPhieuBaoDuong(keyword, loaiBD);
    }
    public void updateTongTienPhieuBaoDuong(String maBD, double tongTien) {
    phieuBaoDuongDAO.updateTongTienPhieuBaoDuong(maBD, tongTien);
}

    public void deleteAllChiTietBaoDuong(String maBD) {
    chiTietBaoDuongDAO.deleteAllChiTietBaoDuong(maBD);

}
public String addPhieuBaoDuongFull(PhieuBaoDuong phieu, List<ChiTietBaoDuong> chiTietList) {
    try {
        return phieuBaoDuongDAO.addPhieuBaoDuongFull(phieu, chiTietList);
    } catch (Exception e) {
        String msg = e.getMessage();
        if (msg != null && msg.contains("ORA-200")) {
            // Lấy dòng chứa ORA-200xx: ...
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

public String updatePhieuBaoDuongFull(PhieuBaoDuong phieu, List<ChiTietBaoDuong> chiTietList) {
    try {
        chiTietBaoDuongDAO.deleteAllChiTietBaoDuong(phieu.getMaBD());
        return phieuBaoDuongDAO.updatePhieuBaoDuongFull(phieu, chiTietList);
    } catch (Exception e) {
        String msg = e.getMessage();
        if (msg != null && msg.contains("ORA-200")) {
            // Lấy dòng chứa ORA-200xx: ...
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
// Thêm vào BaoDuongService
public boolean updatePhieuBaoDuongThongTinChung(PhieuBaoDuong phieu) {
    return phieuBaoDuongDAO.updatePhieuBaoDuongThongTinChung(phieu);
}
public List<PhieuBaoDuong> getPhieuBaoDuongByKhachHangAndLoai(String maKH, String loaiBD) {
    return phieuBaoDuongDAO.getPhieuBaoDuongByKhachHangAndLoai(maKH, loaiBD);
}
}