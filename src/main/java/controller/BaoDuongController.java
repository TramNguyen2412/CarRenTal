package controller;
import java.util.Date;
import java.util.List;

import model.ChiTietBaoDuong;
import model.DichVuBD;
import model.KhachHang;
import model.NhanVien;
import model.PhieuBaoDuong;
import model.Xe;
import service.BaoDuongService;

public class BaoDuongController {
    private BaoDuongService baoDuongService;
    
    public BaoDuongController() {
        baoDuongService = new BaoDuongService();
    }
    
    public List<PhieuBaoDuong> getAllPhieuBaoDuong() {
        return baoDuongService.getAllPhieuBaoDuong();
    }
    
    public PhieuBaoDuong getPhieuBaoDuongById(String maBD) {
        return baoDuongService.getPhieuBaoDuongById(maBD);
    }
    
    public List<ChiTietBaoDuong> getChiTietByMaBD(String maBD) {
        return baoDuongService.getChiTietByMaBD(maBD);
    }
    
    public String addPhieuBaoDuong(String maXe, String maKH, Date ngayBD, String maNV, String loaiBD,Double tongTien) {
        try {
            PhieuBaoDuong phieu = new PhieuBaoDuong();
            phieu.setMaXe(maXe);
            phieu.setMaKH(maKH);
            phieu.setNgayBD(ngayBD);
            phieu.setMaNV(maNV);
            phieu.setLoaiBD(loaiBD);
            phieu.setTongTienBD(tongTien);
            boolean success = baoDuongService.addPhieuBaoDuong(phieu);
            
            if (success) {
                return "Thêm phiếu bảo dưỡng thành công";
            } else {
                return "Thêm phiếu bảo dưỡng thất bại";
            }
        } catch (IllegalArgumentException e) {
            return "Lỗi: " + e.getMessage();
        } catch (Exception e) {
            String message = e.getMessage();
            if (message.contains("ORA-20017")) {
                return "Lỗi: Xe đã được thuê trong thời gian này";
            } else if (message.contains("ORA-20050")) {
                return "Lỗi: Xe đã có phiếu bảo dưỡng khác trong ngày này";
            } else if (message.contains("ORA-20001")) {
                return "Lỗi: Phiếu bảo dưỡng Định kỳ không được gán MaKH";
            } else if (message.contains("ORA-20002")) {
                return "Lỗi: Phiếu bảo dưỡng của khách hàng phải có MaKH";
            } else {
                return "Lỗi hệ thống: " + e.getMessage();
            }
        }
    }
    
    public String updatePhieuBaoDuong(String maBD, String maXe, String maKH, Date ngayBD, String maNV, String loaiBD,Double tongTien) {
        try {
            PhieuBaoDuong phieu = new PhieuBaoDuong();
            phieu.setMaBD(maBD);
            phieu.setMaXe(maXe);
            phieu.setMaKH(maKH);
            phieu.setNgayBD(ngayBD);
            phieu.setMaNV(maNV);
            phieu.setLoaiBD(loaiBD);
            phieu.setTongTienBD(tongTien);
            
            boolean success = baoDuongService.updatePhieuBaoDuong(phieu);
            
            if (success) {
                return "Cập nhật phiếu bảo dưỡng thành công";
            } else {
                return "Cập nhật phiếu bảo dưỡng thất bại";
            }
        } catch (IllegalArgumentException e) {
            return "Lỗi: " + e.getMessage();
        } catch (Exception e) {
            String message = e.getMessage();
            if (message.contains("ORA-20017")) {
                return "Lỗi: Xe đã được thuê trong thời gian này";
            } else if (message.contains("ORA-20050")) {
                return "Lỗi: Xe đã có phiếu bảo dưỡng khác trong ngày này";
            } else if (message.contains("ORA-20001")) {
                return "Lỗi: Phiếu bảo dưỡng Định kỳ không được gán MaKH";
            } else if (message.contains("ORA-20002")) {
                return "Lỗi: Phiếu bảo dưỡng của khách hàng phải có MaKH";
            } else {
                return "Lỗi hệ thống: " + e.getMessage();
            }
        }
    }
    
    public String deletePhieuBaoDuong(String maBD) {
        try {
            boolean success = baoDuongService.deletePhieuBaoDuong(maBD);
            
            if (success) {
                return "Xóa phiếu bảo dưỡng thành công";
            } else {
                return "Xóa phiếu bảo dưỡng thất bại";
            }
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
    
    public String addChiTietBaoDuong(String maBD, String maDV, int soLuong) {
        try {
            ChiTietBaoDuong ct = new ChiTietBaoDuong();
            ct.setMaBD(maBD);
            ct.setMaDV(maDV);
            ct.setSoLuong(soLuong);
            
            boolean success = baoDuongService.addChiTietBaoDuong(ct);
            
            if (success) {
                return "Thêm chi tiết bảo dưỡng thành công";
            } else {
                return "Thêm chi tiết bảo dưỡng thất bại";
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
    
    public String updateChiTietBaoDuong(String maBD, String maDV, int soLuong) {
        try {
            ChiTietBaoDuong ct = new ChiTietBaoDuong();
            ct.setMaBD(maBD);
            ct.setMaDV(maDV);
            ct.setSoLuong(soLuong);
            
            boolean success = baoDuongService.updateChiTietBaoDuong(ct);
            
            if (success) {
                return "Cập nhật chi tiết bảo dưỡng thành công";
            } else {
                return "Cập nhật chi tiết bảo dưỡng thất bại";
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
    
    public String deleteChiTietBaoDuong(String maBD, String maDV) {
        try {
            boolean success = baoDuongService.deleteChiTietBaoDuong(maBD, maDV);
            
            if (success) {
                return "Xóa chi tiết bảo dưỡng thành công";
            } else {
                return "Xóa chi tiết bảo dưỡng thất bại";
            }
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
    
    public List<PhieuBaoDuong> searchPhieuBaoDuong(String keyword) {
        return baoDuongService.searchPhieuBaoDuong(keyword);
    }
    
    public List<PhieuBaoDuong> getPhieuBaoDuongByKhachHang(String maKH) {
        return baoDuongService.getPhieuBaoDuongByKhachHang(maKH);
    }

    public List<Xe> getAllXe() {
        return baoDuongService.getAllXe();
    }

    public Xe getXeByMa(String maXe) {
        return baoDuongService.getXeByMa(maXe);
    }

    public List<KhachHang> getAllKhachHang() {
        return baoDuongService.getAllKhachHang();
    }

    public KhachHang getKhachHangByMa(String maKH) {
        return baoDuongService.getKhachHangByMa(maKH);
    }

    public List<NhanVien> getAllNhanVien() {
        return baoDuongService.getAllNhanVien();
    }

    public NhanVien getNhanVienByMa(String maNV) {
        return baoDuongService.getNhanVienByMa(maNV);
    }

    public List<DichVuBD> getAllDichVuBD() {
        return baoDuongService.getAllDichVuBD();
    }

    public DichVuBD getDichVuBDById(String maDV) {
        return baoDuongService.getDichVuBDById(maDV);
    }

    public List<PhieuBaoDuong> searchPhieuBaoDuong(String keyword, String loaiBD) {
        return baoDuongService.searchPhieuBaoDuong(keyword, loaiBD);
    }
    
    public void updateTongTienPhieuBaoDuong(String maBD, double tongTien) {
    baoDuongService.updateTongTienPhieuBaoDuong(maBD, tongTien);
}
    
public String addPhieuBaoDuongFull(String maXe, String maKH, Date ngayBD, String maNV, String loaiBD, double tongTien, List<ChiTietBaoDuong> chiTietList) {
    try {
        PhieuBaoDuong phieu = new PhieuBaoDuong();
        phieu.setMaXe(maXe);
        phieu.setMaKH(maKH);
        phieu.setNgayBD(ngayBD);
        phieu.setMaNV(maNV);
        phieu.setLoaiBD(loaiBD);
        phieu.setTongTienBD(tongTien);
        return baoDuongService.addPhieuBaoDuongFull(phieu, chiTietList);
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

    public void deleteAllChiTietBaoDuong(String maBD) {
    baoDuongService.deleteAllChiTietBaoDuong(maBD);
}


    // Cập nhật phiếu + chi tiết trong 1 transaction
    public String updatePhieuBaoDuongFull(String maBD, String maXe, String maKH, java.util.Date ngayBD, String maNV, String loaiBD, double tongTien, List<ChiTietBaoDuong> chiTietList) {
        try {
            PhieuBaoDuong phieu = new PhieuBaoDuong();
            phieu.setMaBD(maBD);
            phieu.setMaXe(maXe);
            phieu.setMaKH(maKH);
            phieu.setNgayBD(ngayBD);
            phieu.setMaNV(maNV);
            phieu.setLoaiBD(loaiBD);
            phieu.setTongTienBD(tongTien);
            return baoDuongService.updatePhieuBaoDuongFull(phieu, chiTietList);
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
// Thêm vào BaoDuongController
    public String updatePhieuBaoDuongThongTinChung(String maBD, String maXe, String maKH, Date ngayBD, String maNV, String loaiBD) {
        try {
            PhieuBaoDuong phieu = new PhieuBaoDuong();
            phieu.setMaBD(maBD);
            phieu.setMaXe(maXe);
            phieu.setMaKH(maKH);
            phieu.setNgayBD(ngayBD);
            phieu.setMaNV(maNV);
            phieu.setLoaiBD(loaiBD);
            // Không cập nhật tổng tiền, giữ nguyên tổng tiền cũ
            boolean success = baoDuongService.updatePhieuBaoDuongThongTinChung(phieu);
            return success ? "Cập nhật phiếu bảo dưỡng thành công" : "Cập nhật phiếu bảo dưỡng thất bại";
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
    public List<PhieuBaoDuong> getPhieuBaoDuongByKhachHangAndLoai(String maKH, String loaiBD) {
    return baoDuongService.getPhieuBaoDuongByKhachHangAndLoai(maKH, loaiBD);
}
}