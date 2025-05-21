//package service;
//
//import dao.HopDongDAO;
//import dao.ChiTietHDDao;
//import model.HopDong;
//import model.ChiTietHD;
//import java.sql.SQLException;
//import java.util.List;
//
//public class HopDongService {
//    private HopDongDAO hopDongDAO;
//    private ChiTietHDDao chiTietHDDAO;
//    
//    public HopDongService() {
//        hopDongDAO = new HopDongDAO();
//        chiTietHDDAO = new ChiTietHDDao();
//    }
//    
//    public List<HopDong> getAllHopDong() {
//        return hopDongDAO.getAllHopDong();
//    }
//    
//    public HopDong getHopDongByMa(String maHD) {
//        return hopDongDAO.getHopDongByMa(maHD);
//    }
//    
//    public String addHopDong(HopDong hd, StringBuilder errorMessage) {
//        return hopDongDAO.addHopDong(hd, errorMessage);
//    }
//    
//    public String addHopDong(HopDong hd) {
//        StringBuilder errorMessage = new StringBuilder();
//        return hopDongDAO.addHopDong(hd, errorMessage);
//    }
//    
//    public boolean updateHopDong(HopDong hd, StringBuilder errorMessage) {
//        return hopDongDAO.updateHopDong(hd, errorMessage);
//    }
//    
//    public boolean updateHopDong(HopDong hd) {
//        StringBuilder errorMessage = new StringBuilder();
//        return hopDongDAO.updateHopDong(hd, errorMessage);
//    }
//    
//    public boolean deleteHopDong(String maHD, StringBuilder errorMessage) {
//        return hopDongDAO.deleteHopDong(maHD, errorMessage);
//    }
//    
//    public boolean deleteHopDong(String maHD) {
//        StringBuilder errorMessage = new StringBuilder();
//        return hopDongDAO.deleteHopDong(maHD, errorMessage);
//    }
//    
//    public List<ChiTietHD> getChiTietHDByMaHD(String maHD) {
//        return chiTietHDDAO.getChiTietHDByMaHD(maHD);
//    }
//    
//    public boolean addChiTietHD(ChiTietHD ct, StringBuilder errorMessage) {
//        try {
//            boolean result = chiTietHDDAO.addChiTietHD(ct);
//            if (!result) {
//                errorMessage.append("Thêm chi tiết hợp đồng thất bại");
//            }
//            return result;
//        } catch (SQLException e) {
//            // Bắt và xử lý các lỗi từ trigger
//            String msg = e.getMessage();
//            if (msg.contains("ORA-20006")) {
//                errorMessage.append("Xe đã có trong hợp đồng khác trùng thời gian thuê");
//            } else if (msg.contains("ORA-20018")) {
//                errorMessage.append("Xe có lịch bảo dưỡng trong khoảng thời gian thuê");
//            } else if (msg.contains("ORA-20005")) {
//                errorMessage.append("Xe không ở trạng thái 'Sẵn sàng' để thuê ngay");
//            } else {
//                errorMessage.append("Lỗi: ").append(msg);
//            }
//            e.printStackTrace();
//            return false;
//        } catch (Exception e) {
//            errorMessage.append("Lỗi không xác định: ").append(e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }
//    
//    public boolean addChiTietHD(ChiTietHD ct) {
//        StringBuilder errorMessage = new StringBuilder();
//        return addChiTietHD(ct, errorMessage);
//    }
//    
//    public boolean updateChiTietHD(ChiTietHD ct, StringBuilder errorMessage) {
//        try {
//            boolean result = chiTietHDDAO.updateChiTietHD(ct);
//            if (!result) {
//                errorMessage.append("Cập nhật chi tiết hợp đồng thất bại");
//            }
//            return result;
//        } catch (SQLException e) {
//            // Bắt và xử lý các lỗi từ trigger
//            String msg = e.getMessage();
//            if (msg.contains("ORA-20006")) {
//                errorMessage.append("Xe đã có trong hợp đồng khác trùng thời gian thuê");
//            } else if (msg.contains("ORA-20018")) {
//                errorMessage.append("Xe có lịch bảo dưỡng trong khoảng thời gian thuê");
//            } else if (msg.contains("ORA-20005")) {
//                errorMessage.append("Xe không ở trạng thái 'Sẵn sàng' để thuê ngay");
//            } else {
//                errorMessage.append("Lỗi: ").append(msg);
//            }
//            e.printStackTrace();
//            return false;
//        } catch (Exception e) {
//            errorMessage.append("Lỗi không xác định: ").append(e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }
//    
//    public boolean updateChiTietHD(ChiTietHD ct) {
//        StringBuilder errorMessage = new StringBuilder();
//        return updateChiTietHD(ct, errorMessage);
//    }
//    
//    public boolean deleteChiTietHD(String maHD, String maXe, StringBuilder errorMessage) {
//        try {
//            boolean result = chiTietHDDAO.deleteChiTietHD(maHD, maXe);
//            if (!result) {
//                errorMessage.append("Xóa chi tiết hợp đồng thất bại");
//            }
//            return result;
//        } catch (SQLException e) {
//            errorMessage.append("Lỗi xóa chi tiết hợp đồng: ").append(e.getMessage());
//            e.printStackTrace();
//            return false;
//        } catch (Exception e) {
//            errorMessage.append("Lỗi không xác định: ").append(e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }
//    
//    public boolean deleteChiTietHD(String maHD, String maXe) {
//        StringBuilder errorMessage = new StringBuilder();
//        return deleteChiTietHD(maHD, maXe, errorMessage);
//    }
//    
//    public boolean deleteChiTietHDByMaHD(String maHD, StringBuilder errorMessage) {
//        try {
//            boolean result = chiTietHDDAO.deleteChiTietHDByMaHD(maHD);
//            if (!result) {
//                errorMessage.append("Xóa các chi tiết hợp đồng thất bại");
//            }
//            return result;
//        } catch (SQLException e) {
//            errorMessage.append("Lỗi xóa chi tiết hợp đồng: ").append(e.getMessage());
//            e.printStackTrace();
//            return false;
//        } catch (Exception e) {
//            errorMessage.append("Lỗi không xác định: ").append(e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }
//    
//    public boolean deleteChiTietHDByMaHD(String maHD) {
//        StringBuilder errorMessage = new StringBuilder();
//        return deleteChiTietHDByMaHD(maHD, errorMessage);
//    }
//    
//    public List<HopDong> searchHopDong(String keyword, String trangThai) {
//        return hopDongDAO.searchHopDong(keyword, trangThai);
//    }
//}