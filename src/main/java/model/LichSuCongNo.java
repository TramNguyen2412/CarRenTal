package model;

import java.util.Date;

public class LichSuCongNo {
    private String maLichSu;
    private String maKH;
    private Date ngayGiaoDich;
    private String loaiGiaoDich;
    private double soTien;
    private String ghiChu;
    private KhachHang khachHang;
    
    public LichSuCongNo() {}
    
    public LichSuCongNo(String maLichSu, String maKH, Date ngayGiaoDich, 
                       String loaiGiaoDich, double soTien, String ghiChu) {
        this.maLichSu = maLichSu;
        this.maKH = maKH;
        this.ngayGiaoDich = ngayGiaoDich;
        this.loaiGiaoDich = loaiGiaoDich;
        this.soTien = soTien;
        this.ghiChu = ghiChu;
    }
    
    // Getters and Setters
    public String getMaLichSu() {
        return maLichSu;
    }
    
    public void setMaLichSu(String maLichSu) {
        this.maLichSu = maLichSu;
    }
    
    public String getMaKH() {
        return maKH;
    }
    
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }
    
    public Date getNgayGiaoDich() {
        return ngayGiaoDich;
    }
    
    public void setNgayGiaoDich(Date ngayGiaoDich) {
        this.ngayGiaoDich = ngayGiaoDich;
    }
    
    public String getLoaiGiaoDich() {
        return loaiGiaoDich;
    }
    
    public void setLoaiGiaoDich(String loaiGiaoDich) {
        this.loaiGiaoDich = loaiGiaoDich;
    }
    
    public double getSoTien() {
        return soTien;
    }
    
    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }
    
    public String getGhiChu() {
        return ghiChu;
    }
    
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    
    public KhachHang getKhachHang() {
        return khachHang;
    }
    
    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }
}