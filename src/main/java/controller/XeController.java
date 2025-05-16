package controller;

import model.Xe;
import service.XeService;

import java.util.List;

public class XeController {
    private XeService xeService;
    
    public XeController() {
        xeService = new XeService();
    }
    
    public List<Xe> getAllXe() {
        return xeService.getAllXe();
    }
    
    public Xe getXeByMa(String maXe) {
        return xeService.getXeByMa(maXe);
    }
    
    public boolean addXe(Xe xe) {
        return xeService.addXe(xe);
    }
    
    public boolean updateXe(Xe xe) {
        return xeService.updateXe(xe);
    }
    
    public boolean deleteXe(String maXe) {
        return xeService.deleteXe(maXe);
    }
    
    public List<Xe> searchXe(String keyword) {
        return xeService.searchXe(keyword);
    }
    
    public List<Xe> getXeByTrangThai(String trangThai) {
        return xeService.getXeByTrangThai(trangThai);
    }
    

}