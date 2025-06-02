package model;

import java.util.Date;
import java.util.List;
import java.util.Map;
public class KhachHangDoanhThu {
        private String maKH;
        private String hoTen;
        private int soHopDong;
        private double doanhThu;
        
        public KhachHangDoanhThu() {}
        
        public String getMaKH() { return maKH; }
        public void setMaKH(String maKH) { this.maKH = maKH; }
        
        public String getHoTen() { return hoTen; }
        public void setHoTen(String hoTen) { this.hoTen = hoTen; }
        
        public int getSoHopDong() { return soHopDong; }
        public void setSoHopDong(int soHopDong) { this.soHopDong = soHopDong; }
        
        public double getDoanhThu() { return doanhThu; }
        public void setDoanhThu(double doanhThu) { this.doanhThu = doanhThu; }
}
   