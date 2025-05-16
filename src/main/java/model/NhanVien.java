package model;

public class NhanVien {
    private String maNV;
    private String maTK;
    private String hoTen;
    private String sdt;
    private String email;
    private String chucVu;
    
    public NhanVien() {
    }
    
    public NhanVien(String maNV, String maTK, String hoTen, String sdt, String email, String chucVu) {
        this.maNV = maNV;
        this.maTK = maTK;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.email = email;
        this.chucVu = chucVu;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaTK() {
        return maTK;
    }

    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }
    
    @Override
    public String toString() {
        return maNV + " - " + hoTen;
    }
}