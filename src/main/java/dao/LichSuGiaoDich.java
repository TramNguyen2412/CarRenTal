package dao;


import java.io.Serializable;
import java.sql.Timestamp;

public class LichSuGiaoDich implements Serializable {
    private int maGD;
    private String maKH;
    private double soTien;
    private Timestamp ngayGiaoDich;
    private String ghiChu;
    
    public LichSuGiaoDich() {
    }
    
    public LichSuGiaoDich(int maGD, String maKH, double soTien, Timestamp ngayGiaoDich, String ghiChu) {
        this.maGD = maGD;
        this.maKH = maKH;
        this.soTien = soTien;
        this.ngayGiaoDich = ngayGiaoDich;
        this.ghiChu = ghiChu;
    }
    
    // Getters and Setters
    public int getMaGD() {
        return maGD;
    }
    
    public void setMaGD(int maGD) {
        this.maGD = maGD;
    }
    
    public String getMaKH() {
        return maKH;
    }
    
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }
    
    public double getSoTien() {
        return soTien;
    }
    
    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }
    
    public Timestamp getNgayGiaoDich() {
        return ngayGiaoDich;
    }
    
    public void setNgayGiaoDich(Timestamp ngayGiaoDich) {
        this.ngayGiaoDich = ngayGiaoDich;
    }
    
    public String getGhiChu() {
        return ghiChu;
    }
    
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
