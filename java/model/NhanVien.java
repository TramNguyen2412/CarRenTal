package model;

public class NhanVien {
    private String maNV;
    private String maTK;
    private String hoTen;
    private String sdt;
    private String email;
    private String chucVu;
    private String cccd;
    private String diaChi;
    private double luong;
    private String ngayVaoLam;

    public NhanVien() {
    }

    public NhanVien(String maNV, String maTK, String hoTen, String sdt, String email, String chucVu, 
                    String cccd, String diaChi, double luong, String ngayVaoLam) {
        this.maNV = maNV;
        this.maTK = maTK;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.email = email;
        this.chucVu = chucVu;
        this.cccd = cccd;
        this.diaChi = diaChi;
        this.luong = luong;
        this.ngayVaoLam = ngayVaoLam;
    }

    // Getters and Setters
    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaTK() {
        return maTK;
    }

    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }
    
    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public double getLuong() {
        return luong;
    }

    public void setLuong(double luong) {
        this.luong = luong;
    }

    public String getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(String ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNV='" + maNV + '\'' +
                ", maTK='" + maTK + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", sdt='" + sdt + '\'' +
                ", email='" + email + '\'' +
                ", chucVu='" + chucVu + '\'' +
                ", cccd='" + cccd + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", luong=" + luong +
                ", ngayVaoLam='" + ngayVaoLam + '\'' +
                '}';
    }
}
