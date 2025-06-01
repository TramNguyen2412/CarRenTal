package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HopDong {
    private String maHD;
    private String maKH;
    private String maNV;
    private Date ngayLap;
    private String diaChiGiao;
    private double tongTien;
    private String trangThai;

    // Thông tin liên kết
    private String tenKH;
    private String tenNV;
    private List<ChiTietHD> danhSachXeThue;

    public HopDong() {
        this.ngayLap = new Date();
        this.trangThai = "Chờ xác nhận";
        this.danhSachXeThue = new ArrayList<>();
    }

    // Getters and setters
    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getDiaChiGiao() {
        return diaChiGiao;
    }

    public void setDiaChiGiao(String diaChiGiao) {
        this.diaChiGiao = diaChiGiao;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public List<ChiTietHD> getDanhSachXeThue() {
        return danhSachXeThue;
    }

    public void setDanhSachXeThue(List<ChiTietHD> danhSachXeThue) {
        this.danhSachXeThue = danhSachXeThue;
    }

    public void addChiTietHD(ChiTietHD chiTiet) {
        if (this.danhSachXeThue == null) {
            this.danhSachXeThue = new ArrayList<>();
        }
        this.danhSachXeThue.add(chiTiet);
    }

    public void removeChiTietHD(ChiTietHD chiTiet) {
        if (this.danhSachXeThue != null) {
            this.danhSachXeThue.remove(chiTiet);
        }
    }

    public void removeChiTietHDByIndex(int index) {
        if (this.danhSachXeThue != null && index >= 0 && index < this.danhSachXeThue.size()) {
            this.danhSachXeThue.remove(index);
        }
    }

    public double tinhTongTien() {
        double total = 0;
        if (danhSachXeThue != null) {
            for (ChiTietHD ct : danhSachXeThue) {
                total += ct.getThanhTien();
            }
        }
        return total;
    }
}