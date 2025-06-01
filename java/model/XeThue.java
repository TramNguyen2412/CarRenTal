package model;

public class XeThue extends Xe {
    private boolean selected;
    
    public XeThue() {
        super();
        this.selected = false;
    }
    
    public XeThue(Xe xe) {
        this.setMaXe(xe.getMaXe());
        this.setTenXe(xe.getTenXe());
        this.setBienSo(xe.getBienSo());
        this.setSoCho(xe.getSoCho());
        this.setHangXe(xe.getHangXe());
        this.setNamSX(xe.getNamSX());
        this.setTrangThai(xe.getTrangThai());
        this.setGiaThueNgay(xe.getGiaThueNgay());
        this.setHinhAnh(xe.getHinhAnh());
        this.selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}