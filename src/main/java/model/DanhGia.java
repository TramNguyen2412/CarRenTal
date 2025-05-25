package model;

import java.util.Date;

public class DanhGia {
    private String maDG;
    private String maKH;
    private String maHD;
    private int diemSo;
    private String binhLuan;
    private Date ngayDanhGia;
    
    // Thêm các trường phi chuẩn để hiển thị trên UI
    private String tenKhachHang;
    private String tenXe;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    
    public DanhGia() {
    }
    
    public DanhGia(String maDG, String maKH, String maHD, int diemSo, String binhLuan, Date ngayDanhGia) {
        this.maDG = maDG;
        this.maKH = maKH;
        this.maHD = maHD;
        this.diemSo = diemSo;
        this.binhLuan = binhLuan;
        this.ngayDanhGia = ngayDanhGia;
    }
    
    // Getters và setters
    public String getMaDG() {
        return maDG;
    }
    
    public void setMaDG(String maDG) {
        this.maDG = maDG;
    }
    
    public String getMaKH() {
        return maKH;
    }
    
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }
    
    public String getMaHD() {
        return maHD;
    }
    
    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }
    
    public int getDiemSo() {
        return diemSo;
    }
    
    public void setDiemSo(int diemSo) {
        this.diemSo = diemSo;
    }
    
    public String getBinhLuan() {
        return binhLuan;
    }
    
    public void setBinhLuan(String binhLuan) {
        this.binhLuan = binhLuan;
    }
    
    public Date getNgayDanhGia() {
        return ngayDanhGia;
    }
    
    public void setNgayDanhGia(Date ngayDanhGia) {
        this.ngayDanhGia = ngayDanhGia;
    }
    
    public String getTenKhachHang() {
        return tenKhachHang;
    }
    
    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }
    
    public String getTenXe() {
        return tenXe;
    }
    
    public void setTenXe(String tenXe) {
        this.tenXe = tenXe;
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
}