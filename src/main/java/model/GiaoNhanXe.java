package model;

public class GiaoNhanXe {
    private String maGiaoNhan;
    private String maHD;
    private String maXe;
    private String maNV;
    private String trangThaiXe;
    private String ghiChu;
    private String trangThaiGN; // "Đã giao" or "Đã nhận về"

    public GiaoNhanXe() {
    }

    public GiaoNhanXe(String maGiaoNhan, String maHD, String maXe, String maNV, 
                      String trangThaiXe, String ghiChu, String trangThaiGN) {
        this.maGiaoNhan = maGiaoNhan;
        this.maHD = maHD;
        this.maXe = maXe;
        this.maNV = maNV;
        this.trangThaiXe = trangThaiXe;
        this.ghiChu = ghiChu;
        this.trangThaiGN = trangThaiGN;
    }

    // Getters and Setters
    public String getMaGiaoNhan() {
        return maGiaoNhan;
    }

    public void setMaGiaoNhan(String maGiaoNhan) {
        this.maGiaoNhan = maGiaoNhan;
    }

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

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTrangThaiXe() {
        return trangThaiXe;
    }

    public void setTrangThaiXe(String trangThaiXe) {
        this.trangThaiXe = trangThaiXe;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getTrangThaiGN() {
        return trangThaiGN;
    }

    public void setTrangThaiGN(String trangThaiGN) {
        this.trangThaiGN = trangThaiGN;
    }

    @Override
    public String toString() {
        return "GiaoNhanXe{" +
                "maGiaoNhan='" + maGiaoNhan + '\'' +
                ", maHD='" + maHD + '\'' +
                ", maXe='" + maXe + '\'' +
                ", maNV='" + maNV + '\'' +
                ", trangThaiXe='" + trangThaiXe + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                ", trangThaiGN='" + trangThaiGN + '\'' +
                '}';
    }
}