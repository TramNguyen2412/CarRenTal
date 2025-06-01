package model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ChiTietHD {
    private String maHD;
    private String maXe;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    
    // Thông tin liên kết
    private String tenXe;
    private String bienSo;
    private String hangXe;
    private int soCho;
    private double giaThueNgay;
    
    public ChiTietHD() {
    }
    
    public ChiTietHD(String maXe, Date ngayBatDau, Date ngayKetThuc) {
        this.maXe = maXe;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    // Getters and setters
    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaXe() {
        return maXe;
    }

    public void setMaXe(String maXe) {
        this.maXe = maXe;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getTenXe() {
        return tenXe;
    }

    public void setTenXe(String tenXe) {
        this.tenXe = tenXe;
    }

    public String getBienSo() {
        return bienSo;
    }

    public void setBienSo(String bienSo) {
        this.bienSo = bienSo;
    }

    public String getHangXe() {
        return hangXe;
    }

    public void setHangXe(String hangXe) {
        this.hangXe = hangXe;
    }

    public int getSoCho() {
        return soCho;
    }

    public void setSoCho(int soCho) {
        this.soCho = soCho;
    }

    public double getGiaThueNgay() {
        return giaThueNgay;
    }

    public void setGiaThueNgay(double giaThueNgay) {
        this.giaThueNgay = giaThueNgay;
    }
    
    // Tính số ngày thuê
    public int getSoNgayThue() {
        if (ngayBatDau == null || ngayKetThuc == null) {
            return 0;
        }
        
        long diffInMillies = Math.abs(ngayKetThuc.getTime() - ngayBatDau.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return (int) diff + 1; // +1 vì tính cả ngày đầu và ngày cuối
    }
    
    // Tính thành tiền
    public double getThanhTien() {
        return getSoNgayThue() * giaThueNgay;
    }
}