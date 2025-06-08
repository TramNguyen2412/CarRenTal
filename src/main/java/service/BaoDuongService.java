package service;

import java.util.List;

import dao.ChiTietBaoDuongDAO;
import dao.KhachHangDAO;
import dao.DichVuBDDAO;
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
    private DichVuBDDAO dichVuBDDAO;
    
    public BaoDuongService() {
        phieuBaoDuongDAO = new PhieuBaoDuongDAO();
        chiTietBaoDuongDAO = new ChiTietBaoDuongDAO();
        xeDAO = new XeDAO();
        khachHangDAO = new KhachHangDAO();
        dichVuBDDAO = new DichVuBDDAO();
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
        
        if (phieu.getLoaiBD().equals("Khách gây hư hại") && 
            (phieu.getMaKH() == null || phieu.getMaKH().trim().isEmpty())) {
            throw new IllegalArgumentException("Bảo dưỡng do khách gây hư hại phải có thông tin khách hàng");
        }
        
        if (phieu.getLoaiBD().equals("Định Kỳ") && phieu.getMaKH() != null && !phieu.getMaKH().trim().isEmpty()) {
            throw new IllegalArgumentException("Bảo dưỡng định kỳ không được gán cho khách hàng");
        }
        
        Xe xe = xeDAO.getXeByMa(phieu.getMaXe());
        if (xe == null) {
            throw new IllegalArgumentException("Xe không tồn tại");
        }
        
        if (phieuBaoDuongDAO.isCarInContract(phieu.getMaXe(), phieu.getNgayBD())) {
            throw new IllegalArgumentException("Xe đang được thuê trong ngày bảo dưỡng. Vui lòng chọn ngày khác.");
        }
     
        return phieuBaoDuongDAO.addPhieuBaoDuong(phieu);
    }
    
    public boolean updatePhieuBaoDuong(PhieuBaoDuong phieu) {
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
        
        if (phieu.getLoaiBD().equals("Khách gây hư hại") && 
            (phieu.getMaKH() == null || phieu.getMaKH().trim().isEmpty())) {
            throw new IllegalArgumentException("Bảo dưỡng do khách gây hư hại phải có thông tin khách hàng");
        }
        
        if (phieu.getLoaiBD().equals("Định Kỳ") && phieu.getMaKH() != null && !phieu.getMaKH().trim().isEmpty()) {
            throw new IllegalArgumentException("Bảo dưỡng định kỳ không được gán cho khách hàng");
        }
        
        PhieuBaoDuong originalPhieu = phieuBaoDuongDAO.getPhieuBaoDuongByMaBD(phieu.getMaBD());

        if (originalPhieu == null) {
            throw new IllegalArgumentException("Phiếu bảo dưỡng gốc không tồn tại");
        }

        boolean isMaXeChanged = !originalPhieu.getMaXe().equals(phieu.getMaXe());
        boolean isNgayBDChanged = !originalPhieu.getNgayBD().equals(phieu.getNgayBD());

        if ((isMaXeChanged || isNgayBDChanged) && phieuBaoDuongDAO.isCarInContract(phieu.getMaXe(), phieu.getNgayBD())) {
            throw new IllegalArgumentException("Xe đang được thuê trong ngày bảo dưỡng. Vui lòng chọn ngày khác.");
        }

        return phieuBaoDuongDAO.updatePhieuBaoDuong(phieu);
    }
    
    public boolean deletePhieuBaoDuong(String maBD) {
        return phieuBaoDuongDAO.deletePhieuBaoDuong(maBD);
    }
    
    public boolean addChiTietBaoDuong(ChiTietBaoDuong ct) {
        if (ct.getMaBD() == null || ct.getMaBD().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu bảo dưỡng không được để trống");
        }
        
        if (ct.getMaDV() == null || ct.getMaDV().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn dịch vụ");
        }
        
        if (ct.getSoLuong() <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        return chiTietBaoDuongDAO.addChiTietBaoDuong(ct);
    }
    
    public boolean updateChiTietBaoDuong(ChiTietBaoDuong ct) {
        if (ct.getMaBD() == null || ct.getMaBD().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu bảo dưỡng không được để trống");
        }
        
        if (ct.getMaDV() == null || ct.getMaDV().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn dịch vụ");
        }
        
        if (ct.getSoLuong() <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        
        return chiTietBaoDuongDAO.updateChiTietBaoDuong(ct);
    }
    
    public boolean deleteChiTietBaoDuong(String maBD, String maDV) {
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
        return dichVuBDDAO.getAllDichVuBD();
    }

    // Lấy dịch vụ bảo dưỡng theo mã
    public model.DichVuBD getDichVuBDById(String maDV) {
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