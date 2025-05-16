package model;

public class Xe {
    private String maXe;
    private String tenXe;
    private String bienSo;
    private int soCho;
    private String hangXe;
    private int namSX;
    private String trangThai;
    private double giaThueNgay;
    private String hinhAnh; 
    public Xe() {
    }
    
    public Xe(String maXe, String tenXe, String bienSo, int soCho, String hangXe, int namSX, String trangThai, double giaThueNgay, String hinhAnh) {
        this.maXe = maXe;
        this.tenXe = tenXe;
        this.bienSo = bienSo;
        this.soCho = soCho;
        this.hangXe = hangXe;
        this.namSX = namSX;
        this.trangThai = trangThai;
        this.giaThueNgay = giaThueNgay;
        this.hinhAnh = hinhAnh;
    }
    
    // Getters và Setters
    public String getMaXe() {
        return maXe;
    }

    public void setMaXe(String maXe) {
        this.maXe = maXe;
    }
   
    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
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

    public int getSoCho() {
        return soCho;
    }

    public void setSoCho(int soCho) {
        this.soCho = soCho;
    }

    public String getHangXe() {
        return hangXe;
    }

    public void setHangXe(String hangXe) {
        this.hangXe = hangXe;
    }

    public int getNamSX() {
        return namSX;
    }

    public void setNamSX(int namSX) {
        this.namSX = namSX;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public double getGiaThueNgay() {
        return giaThueNgay;
    }

    public void setGiaThueNgay(double giaThueNgay) {
        this.giaThueNgay = giaThueNgay;
    }
}