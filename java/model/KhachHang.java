package model;

public class KhachHang {
    private String maKH;
    private String maTK;
    private double tongTienNo;
    private String hoTen;
    private String sdt;
    private String email;
    private String cccd;
    private String diaChi;
    
    public KhachHang() {
        this.tongTienNo = 0;
    }
    
    public KhachHang(String maKH, String hoTen, String sdt, String email, String cccd, String diaChi) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.email = email;
        this.cccd = cccd;
        this.diaChi = diaChi;
        this.tongTienNo = 0;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaTK() {
        return maTK;
    }

    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }

    public double getTongTienNo() {
        return tongTienNo;
    }

    public void setTongTienNo(double tongTienNo) {
        this.tongTienNo = tongTienNo;
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
    
    @Override
    public String toString() {
        return maKH + " - " + hoTen;
    }
}