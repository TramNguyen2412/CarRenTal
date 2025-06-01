package model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GioXe {
    private String maGH;
    private String maKH;
    private String maXe;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private Date ngayThem;
    
    // Thông tin liên kết với xe
    private String tenXe;
    private String bienSo;
    private String hangXe;
    private int soCho;
    private int namSX;
    private double giaThueNgay;
    private String hinhAnh;
    
    public GioXe() {
        this.ngayThem = new Date();
    }
    
    public GioXe(String maKH, String maXe, Date ngayBatDau, Date ngayKetThuc) {
        this.maKH = maKH;
        this.maXe = maXe;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.ngayThem = new Date();
    }

    // Getters và Setters
    public String getMaGH() {
        return maGH;
    }

    public void setMaGH(String maGH) {
        this.maGH = maGH;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
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

    public Date getNgayThem() {
        return ngayThem;
    }

    public void setNgayThem(Date ngayThem) {
        this.ngayThem = ngayThem;
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

    public int getNamSX() {
        return namSX;
    }

    public void setNamSX(int namSX) {
        this.namSX = namSX;
    }

    public double getGiaThueNgay() {
        return giaThueNgay;
    }

    public void setGiaThueNgay(double giaThueNgay) {
        this.giaThueNgay = giaThueNgay;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
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