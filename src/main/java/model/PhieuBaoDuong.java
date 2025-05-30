package model;

import java.util.Date;
import java.util.List;

public class PhieuBaoDuong {
    private String maBD;
    private String maXe;
    private String maKH;
    private Date ngayBD;
    private String maNV;
    private String loaiBD;
    private double tongTienBD;
    private Xe xe;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private List<ChiTietBaoDuong> chiTietBaoDuong;
    
    public PhieuBaoDuong() {}
    
    public PhieuBaoDuong(String maBD, String maXe, String maKH, Date ngayBD, 
                        String maNV, String loaiBD, double tongTienBD) {
        this.maBD = maBD;
        this.maXe = maXe;
        this.maKH = maKH;
        this.ngayBD = ngayBD;
        this.maNV = maNV;
        this.loaiBD = loaiBD;
        this.tongTienBD = tongTienBD;
    }
    
    // Getters and Setters
    public String getMaBD() {
        return maBD;
    }
    
    public void setMaBD(String maBD) {
        this.maBD = maBD;
    }
    
    public String getMaXe() {
        return maXe;
    }
    
    public void setMaXe(String maXe) {
        this.maXe = maXe;
    }
    
    public String getMaKH() {
        return maKH;
    }
    
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }
    
    public Date getNgayBD() {
        return ngayBD;
    }
    
    public void setNgayBD(Date ngayBD) {
        this.ngayBD = ngayBD;
    }
    
    public String getMaNV() {
        return maNV;
    }
    
    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
    
    public String getLoaiBD() {
        return loaiBD;
    }
    
    public void setLoaiBD(String loaiBD) {
        this.loaiBD = loaiBD;
    }
    
    public double getTongTienBD() {
        return tongTienBD;
    }
    
    public void setTongTienBD(double tongTienBD) {
        this.tongTienBD = tongTienBD;
    }
    
    public Xe getXe() {
        return xe;
    }
    
    public void setXe(Xe xe) {
        this.xe = xe;
    }
    
    public KhachHang getKhachHang() {
        return khachHang;
    }
    
    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }
    
    public NhanVien getNhanVien() {
        return nhanVien;
    }
    
    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
    
    public List<ChiTietBaoDuong> getChiTietBaoDuong() {
        return chiTietBaoDuong;
    }
    
    public void setChiTietBaoDuong(List<ChiTietBaoDuong> chiTietBaoDuong) {
        this.chiTietBaoDuong = chiTietBaoDuong;
    }

}