package controller;

import dao.TaiKhoanDAO;
import dao.KhachHangDAO;
import dao.NhanVienDAO;
import dao.VaiTroDAO;
import model.TaiKhoan;
import model.TaiKhoanExtended;
import model.VaiTro;
import model.KhachHang;
import model.NhanVien;
import java.util.List;

public class TaiKhoanController {
    private TaiKhoanDAO taiKhoanDAO;
    private KhachHangDAO khachHangDAO;
    private NhanVienDAO nhanVienDAO;
    private VaiTroDAO vaiTroDAO;
    
    public TaiKhoanController() {
        this.taiKhoanDAO = new TaiKhoanDAO();
        this.khachHangDAO = new KhachHangDAO();
        this.nhanVienDAO = new NhanVienDAO();
        this.vaiTroDAO = new VaiTroDAO();
    }
 
    public TaiKhoan dangNhap(String username, String password) {
        // Kiểm tra đăng nhập qua DAO
        TaiKhoan taiKhoan = taiKhoanDAO.checkLogin(username, password);
        return taiKhoan;
    }
    
      public KhachHang getKhachHangByMaTK(String maTK) {
        return khachHangDAO.getKhachHangByTaiKhoan(maTK);
    }
 
    
    public NhanVien getNhanVienByMaTK(String maTK) {
        return nhanVienDAO.getNhanVienByMaTK(maTK);
    }
    
    public TaiKhoan getTaiKhoanById(String maTK) {
        // Lấy tài khoản theo ID
        return taiKhoanDAO.getTaiKhoanExtendedById(maTK);
    }
    
    // Lấy danh sách tài khoản mở rộng (có thông tin người dùng)
    public List<TaiKhoanExtended> getAllTaiKhoanExtended() {
        return taiKhoanDAO.getAllTaiKhoanExtended();
    }
    
    // Lấy danh sách vai trò
    public List<VaiTro> getAllVaiTro() {
        return vaiTroDAO.getAllVaiTro();
    }
    
    // Lấy danh sách nhân viên chưa có tài khoản
    public List<String[]> getNhanVienChuaCoTaiKhoan() {
        return taiKhoanDAO.getNhanVienChuaCoTaiKhoan();
    }
    
    // Lấy danh sách khách hàng chưa có tài khoản
    public List<String[]> getKhachHangChuaCoTaiKhoan() {
        return taiKhoanDAO.getKhachHangChuaCoTaiKhoan();
    }
    

    public boolean createTaiKhoan(TaiKhoan taiKhoan, String loaiNguoiDung, String maNguoiDung) {
        try {
            // Tạo mã tài khoản mới
            String newMaTK = taiKhoanDAO.generateNewMaTK();
            taiKhoan.setMaTK(newMaTK);

            System.out.println("Thêm tài khoản - MaTK: " + newMaTK);
            System.out.println("Loại người dùng: " + loaiNguoiDung);
            System.out.println("Mã người dùng: " + maNguoiDung);

            // Tạo tài khoản
            boolean success = taiKhoanDAO.insert(taiKhoan);
            if (!success) {
                System.out.println("Thêm tài khoản thất bại");
                return false;
            }

            // Liên kết với người dùng tương ứng
            if (loaiNguoiDung != null && maNguoiDung != null && !maNguoiDung.isEmpty()) {
                boolean linkSuccess = false;

                if ("NV".equals(loaiNguoiDung)) {
                    linkSuccess = taiKhoanDAO.updateMaTKForNhanVien(maNguoiDung, newMaTK);
                    System.out.println("Liên kết với nhân viên " + maNguoiDung + ": " + (linkSuccess ? "thành công" : "thất bại"));
                } else if ("KH".equals(loaiNguoiDung)) {
                    linkSuccess = taiKhoanDAO.updateMaTKForKhachHang(maNguoiDung, newMaTK);
                    System.out.println("Liên kết với khách hàng " + maNguoiDung + ": " + (linkSuccess ? "thành công" : "thất bại"));
                }

                return linkSuccess;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật thông tin tài khoản
    public boolean updateTaiKhoan(TaiKhoan taiKhoan) {
      if (taiKhoan instanceof TaiKhoanExtended) {
          TaiKhoanExtended tkExtended = (TaiKhoanExtended) taiKhoan;
          String loaiNguoiDung = tkExtended.getLoaiNguoiDung();
          String maNguoiDung = tkExtended.getMaNguoiDung();

          try {
              // Cập nhật thông tin cơ bản của tài khoản
              boolean success = taiKhoanDAO.update(taiKhoan);
              if (!success) return false;

              // Xóa liên kết cũ (nếu có)
              taiKhoanDAO.removeAllUserLinks(taiKhoan.getMaTK());

              // Tạo liên kết mới nếu có thông tin người dùng
              if (maNguoiDung != null && !maNguoiDung.isEmpty()) {
                  if ("NV".equals(loaiNguoiDung)) {
                      return taiKhoanDAO.updateMaTKForNhanVien(maNguoiDung, taiKhoan.getMaTK());
                  } else if ("KH".equals(loaiNguoiDung)) {
                      return taiKhoanDAO.updateMaTKForKhachHang(maNguoiDung, taiKhoan.getMaTK());
                  }
              }

              return true;
          } catch (Exception e) {
              e.printStackTrace();
              return false;
          }
      } else {
          // Chỉ cập nhật thông tin cơ bản nếu không phải là TaiKhoanExtended
          return taiKhoanDAO.update(taiKhoan);
      }
  }
    
    // Xóa tài khoản
    public boolean deleteTaiKhoan(String maTK) {
        return taiKhoanDAO.delete(maTK);
    }
    
    // Tạo mã tài khoản mới tự động
    public String generateNewMaTK() {
        return taiKhoanDAO.generateNewMaTK();
    }
    
    // Kiểm tra tên đăng nhập đã tồn tại chưa
    public boolean isUsernameExists(String username) {
        return taiKhoanDAO.isUsernameExists(username);
    }
  
}