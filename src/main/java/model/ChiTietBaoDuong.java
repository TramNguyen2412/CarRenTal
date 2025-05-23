package model;

public class ChiTietBaoDuong {
    private String maBD;
    private String maDV;
    private int soLuong;
    private PhieuBaoDuong phieuBaoDuong;
    private DichVuBD dichVuBD;
    
    public ChiTietBaoDuong() {}
    
    public ChiTietBaoDuong(String maBD, String maDV, int soLuong) {
        this.maBD = maBD;
        this.maDV = maDV;
        this.soLuong = soLuong;
    }
    
    // Getters and Setters
    public String getMaBD() {
        return maBD;
    }
    
    public void setMaBD(String maBD) {
        this.maBD = maBD;
    }
    
    public String getMaDV() {
        return maDV;
    }
    
    public void setMaDV(String maDV) {
        this.maDV = maDV;
    }
    
    public int getSoLuong() {
        return soLuong;
    }
    
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    
    public PhieuBaoDuong getPhieuBaoDuong() {
        return phieuBaoDuong;
    }
    
    public void setPhieuBaoDuong(PhieuBaoDuong phieuBaoDuong) {
        this.phieuBaoDuong = phieuBaoDuong;
    }
    
    public DichVuBD getDichVuBD() {
        return dichVuBD;
    }
    
    public void setDichVuBD(DichVuBD dichVuBD) {
        this.dichVuBD = dichVuBD;
    }
    
    // Calculate total price for this service
    public double getThanhTien() {
        if (dichVuBD != null) {
            return soLuong * dichVuBD.getGiaDV();
        }
        return 0;
    }
    public boolean isHuHaiKhachGayRa() {
    if (dichVuBD != null && dichVuBD.getTenDV() != null) {
        String ten = dichVuBD.getTenDV().toLowerCase();
        return ten.contains("hư hại") || ten.contains("khách gây hư hại") || ten.contains("khách làm hư");
    }
    return false;
}
}