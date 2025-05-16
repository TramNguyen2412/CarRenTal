/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package service;

import dao.XeDAO;
import model.Xe;

import java.util.List;

public class XeService {
    private XeDAO xeDAO;
    
    public XeService() {
        xeDAO = new XeDAO();
    }
    
    public List<Xe> getAllXe() {
        return xeDAO.getAllXe();
    }
    
    public Xe getXeByMa(String maXe) {
        return xeDAO.getXeByMa(maXe);
    }
    
    public boolean addXe(Xe xe) {
        // Kiểm tra dữ liệu trước khi thêm
        if (xe.getTenXe() == null || xe.getTenXe().trim().isEmpty()) {
            return false;
        }
        if (xe.getBienSo() == null || xe.getBienSo().trim().isEmpty()) {
            return false;
        }
        if (xe.getSoCho() <= 0) {
            return false;
        }
        if (xe.getNamSX() <= 0) {
            return false;
        }
        if (xe.getGiaThueNgay() <= 0) {
            return false;
        }
        
        return xeDAO.addXe(xe);
    }
    
    public boolean updateXe(Xe xe) {
        // Kiểm tra dữ liệu trước khi cập nhật
        if (xe.getMaXe() == null || xe.getMaXe().trim().isEmpty()) {
            return false;
        }
        if (xe.getTenXe() == null || xe.getTenXe().trim().isEmpty()) {
            return false;
        }
        if (xe.getBienSo() == null || xe.getBienSo().trim().isEmpty()) {
            return false;
        }
        if (xe.getSoCho() <= 0) {
            return false;
        }
        if (xe.getNamSX() <= 0) {
            return false;
        }
        if (xe.getGiaThueNgay() <= 0) {
            return false;
        }
        
        return xeDAO.updateXe(xe);
    }
    
    public boolean deleteXe(String maXe) {
        if (maXe == null || maXe.trim().isEmpty()) {
            return false;
        }
        
        // Có thể thêm logic kiểm tra xe có đang được thuê không
        Xe xe = getXeByMa(maXe);
        if (xe != null && xe.getTrangThai().equals("Đang cho thuê")) {
            return false;
        }
        
        return xeDAO.deleteXe(maXe);
    }
    
    public List<Xe> searchXe(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllXe();
        }
        return xeDAO.searchXe(keyword);
    }
    
    public List<Xe> getXeByTrangThai(String trangThai) {
        if (trangThai == null || trangThai.trim().isEmpty()) {
            return getAllXe();
        }
        return xeDAO.getXeByTrangThai(trangThai);
    }
}