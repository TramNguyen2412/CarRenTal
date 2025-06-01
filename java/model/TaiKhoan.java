package model;

public class TaiKhoan {
    private String maTK;
    private String maVaiTro;
    private String tenDangNhap;
    private String matKhau;
    private String trangThai;
    
    // Constructor không tham số
    public TaiKhoan() {
    }
    
    // Constructor đầy đủ tham số
    public TaiKhoan(String maTK, String maVaiTro, String tenDangNhap, String matKhau, String trangThai) {
        this.maTK = maTK;
        this.maVaiTro = maVaiTro;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.trangThai = trangThai;
    }
    
    // Getters và Setters
    public String getMaTK() {
        return maTK;
    }

    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }

    public String getMaVaiTro() {
        return maVaiTro;
    }

    public void setMaVaiTro(String maVaiTro) {
        this.maVaiTro = maVaiTro;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}