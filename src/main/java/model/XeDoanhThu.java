package model;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class XeDoanhThu {
        private String maXe;
        private String tenXe;
        private String bienSo;
        private int soLuotThue;
        private double doanhThu;
        
        public XeDoanhThu() {}
        
        public String getMaXe() { return maXe; }
        public void setMaXe(String maXe) { this.maXe = maXe; }
        
        public String getTenXe() { return tenXe; }
        public void setTenXe(String tenXe) { this.tenXe = tenXe; }
        
        public String getBienSo() { return bienSo; }
        public void setBienSo(String bienSo) { this.bienSo = bienSo; }
        
        public int getSoLuotThue() { return soLuotThue; }
        public void setSoLuotThue(int soLuotThue) { this.soLuotThue = soLuotThue; }
        
        public double getDoanhThu() { return doanhThu; }
        public void setDoanhThu(double doanhThu) { this.doanhThu = doanhThu; }
}