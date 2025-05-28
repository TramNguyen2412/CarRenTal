package model;

public class TaiKhoanExtended extends TaiKhoan {
    private String loaiNguoiDung; // "NV" hoặc "KH" hoặc "UNKNOWN"
    private String maNguoiDung;   // MaNV hoặc MaKH
    private String tenNguoiDung;  // Họ tên của NV hoặc KH
    private String tenVaiTro;     // Tên vai trò
    
    public TaiKhoanExtended() {
        super();
    }
    
    public TaiKhoanExtended(TaiKhoan taiKhoan) {
        super();
        this.setMaTK(taiKhoan.getMaTK());
        this.setMaVaiTro(taiKhoan.getMaVaiTro());
        this.setTenDangNhap(taiKhoan.getTenDangNhap());
        this.setMatKhau(taiKhoan.getMatKhau());
        this.setTrangThai(taiKhoan.getTrangThai());
    }

    public String getLoaiNguoiDung() {
        return loaiNguoiDung;
    }

    public void setLoaiNguoiDung(String loaiNguoiDung) {
        this.loaiNguoiDung = loaiNguoiDung;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }
    
    public String getTenVaiTro() {
        return tenVaiTro;
    }
    
    public void setTenVaiTro(String tenVaiTro) {
        this.tenVaiTro = tenVaiTro;
    }
}