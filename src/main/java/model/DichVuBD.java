package model;

public class DichVuBD {
    private String maDV;
    private String tenDV;
    private double giaDV;
    
    public DichVuBD() {}
    
    public DichVuBD(String maDV, String tenDV, double giaDV) {
        this.maDV = maDV;
        this.tenDV = tenDV;
        this.giaDV = giaDV;
    }
    
    // Getters and Setters
    public String getMaDV() {
        return maDV;
    }
    
    public void setMaDV(String maDV) {
        this.maDV = maDV;
    }
    
    public String getTenDV() {
        return tenDV;
    }
    
    public void setTenDV(String tenDV) {
        this.tenDV = tenDV;
    }
    
    public double getGiaDV() {
        return giaDV;
    }
    
    public void setGiaDV(double giaDV) {
        this.giaDV = giaDV;
    }
    
    @Override
    public String toString() {
        return tenDV;
    }
}