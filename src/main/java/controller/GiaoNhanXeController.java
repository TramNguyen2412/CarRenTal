package controller;

import model.GiaoNhanXe;
import service.GiaoNhanXeService;
import java.util.List;
import dao.HopDongDAO;
import model.ChiTietHD;
public class GiaoNhanXeController {
    private GiaoNhanXeService giaoNhanXeService;
    private HopDongDAO hopDongDAO;
    public GiaoNhanXeController() {
        this.giaoNhanXeService = new GiaoNhanXeService();
    }

    public List<GiaoNhanXe> getAllGiaoNhanXe() {
        return giaoNhanXeService.getAllGiaoNhanXe();
    }

    public GiaoNhanXe getGiaoNhanXeByMa(String maGiaoNhan) {
        return giaoNhanXeService.getGiaoNhanXeByMa(maGiaoNhan);
    }

    public String addGiaoNhanXe(GiaoNhanXe gn) {
        return giaoNhanXeService.addGiaoNhanXe(gn);
    }

    public boolean updateGiaoNhanXe(GiaoNhanXe gn) {
        return giaoNhanXeService.updateGiaoNhanXe(gn);
    }

    public boolean deleteGiaoNhanXe(String maGiaoNhan) {
        return giaoNhanXeService.deleteGiaoNhanXe(maGiaoNhan);
    }

    public List<GiaoNhanXe> searchGiaoNhanXe(String keyword) {
        return giaoNhanXeService.searchGiaoNhanXe(keyword);
    }
    
    public boolean existsGiaoNhanXe(String maGiaoNhan) {
        return giaoNhanXeService.existsGiaoNhanXe(maGiaoNhan);
    }

    public String getErrorMessage() {
        return giaoNhanXeService.getErrorMessage();
    }
    public List<ChiTietHD> getChiTietHDByMaHD(String maHD) {
        return hopDongDAO.getChiTietHDByMaHD(maHD);
    }
}