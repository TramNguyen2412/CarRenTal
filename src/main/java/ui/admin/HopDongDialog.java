//package ui.admin;
//import controller.HopDongController;
//import controller.KhachHangController;
//import controller.NhanVienController;
//import model.HopDong;
//import model.ChiTietHD;
//import model.KhachHang;
//import model.NhanVien;
//import java.awt.*;
//import java.awt.event.*;
//import java.text.NumberFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.table.DefaultTableModel;
//
//public class HopDongDialog extends JDialog {
//    private HopDong hopDong;
//    private HopDongPanel parent;
//    private HopDongController hopDongController;
//    private KhachHangController khachHangController;
//    private NhanVienController nhanVienController;
//    
//    private boolean readOnly = false;
//    
//    // UI Components
//    private JTabbedPane tabbedPane;
//    private JComboBox<String> cboKhachHang;
//    private JComboBox<String> cboNhanVien; // Thêm combobox chọn nhân viên
//    private JTextField txtDiaChiGiao;
//    private JComboBox<String> cboTrangThai;
//    private JLabel lblTongTien;
//    
//    // Bảng xe thuê
//    private JTable tblXeThue;
//    private DefaultTableModel modelXeThue;
//    
//   public HopDongDialog(Window owner, HopDong hopDong, HopDongPanel parent) {
//        super(owner, hopDong == null ? "Thêm hợp đồng mới" : "Chỉnh sửa hợp đồng", ModalityType.APPLICATION_MODAL);
//        this.hopDong = hopDong;
//        this.parent = parent;
//        this.hopDongController = new HopDongController();
//        this.khachHangController = new KhachHangController();
//        this.nhanVienController = new NhanVienController();
//
//        // Nếu là tạo mới, khởi tạo đối tượng HopDong
//        if (this.hopDong == null) {
//            this.hopDong = new HopDong();
//            this.hopDong.setNgayLap(new Date());
//            this.hopDong.setDanhSachXeThue(new ArrayList<>());
//        } else {
//            // Khi chỉnh sửa, luôn lấy thông tin đầy đủ từ DB
//            this.hopDong = hopDongController.getHopDongByMa(this.hopDong.getMaHD());
//
//            // Đảm bảo danh sách xe thuê không null
//            if (this.hopDong.getDanhSachXeThue() == null) {
//                this.hopDong.setDanhSachXeThue(new ArrayList<>());
//            }
//        }
//
//        initComponents();
//    }
//    private void initComponents() {
//        setSize(900, 600);
//        setLocationRelativeTo(getOwner());
//        setLayout(new BorderLayout());
//        
//        tabbedPane = new JTabbedPane();
//        
//        // Tab 1: Thông tin tổng quan
//        JPanel pnlInfo = createInfoPanel();
//        tabbedPane.addTab("Thông tin chung", pnlInfo);
//        
//        // Tab 2: Danh sách xe thuê
//        JPanel pnlXeThue = createXeThuePanel();
//        tabbedPane.addTab("Danh sách xe thuê", pnlXeThue);
//        
//        // Panel nút
//        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        JButton btnSave = new JButton("Lưu");
//        JButton btnCancel = new JButton("Hủy");
//        
//        pnlButtons.add(btnSave);
//        pnlButtons.add(btnCancel);
//        
//        add(tabbedPane, BorderLayout.CENTER);
//        add(pnlButtons, BorderLayout.SOUTH);
//        
//        // Sự kiện nút Lưu
//        btnSave.addActionListener(e -> saveHopDong());
//        
//        // Sự kiện nút Hủy
//        btnCancel.addActionListener(e -> dispose());
//        
//        // Nếu là chế độ chỉ xem
//        if (readOnly) {
//            cboKhachHang.setEnabled(false);
//            cboNhanVien.setEnabled(false); // Thêm dòng này
//            txtDiaChiGiao.setEditable(false);
//            cboTrangThai.setEnabled(false);
//            btnSave.setEnabled(false);
//        }
//    }
//    
//    private JPanel createInfoPanel() {
//        JPanel panel = new JPanel(new BorderLayout(10, 10));
//        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
//        
//        JPanel formPanel = new JPanel(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.insets = new Insets(5, 5, 5, 5);
//        
//        // Khách hàng
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        formPanel.add(new JLabel("Khách hàng:"), gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 1.0;
//        cboKhachHang = new JComboBox<>();
//        loadKhachHangToComboBox();
//        formPanel.add(cboKhachHang, gbc);
//        
//        // Nút thêm khách hàng mới
//        gbc.gridx = 2;
//        gbc.weightx = 0.0;
//        JButton btnAddKH = new JButton("Thêm KH mới");
//        formPanel.add(btnAddKH, gbc);
//        
//        // Nhân viên phụ trách (THÊM MỚI)
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        gbc.weightx = 0.0;
//        formPanel.add(new JLabel("Nhân viên phụ trách:"), gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 1.0;
//        cboNhanVien = new JComboBox<>();
//        loadNhanVienToComboBox();
//        formPanel.add(cboNhanVien, gbc);
//        
//        // Địa chỉ giao
//        gbc.gridx = 0;
//        gbc.gridy = 2; // Đã tăng lên 1 vì thêm nhân viên
//        formPanel.add(new JLabel("Địa chỉ giao:"), gbc);
//        
//        gbc.gridx = 1;
//        gbc.gridwidth = 2;
//        txtDiaChiGiao = new JTextField(30);
//        formPanel.add(txtDiaChiGiao, gbc);
//        
//        // Trạng thái
//        gbc.gridx = 0;
//        gbc.gridy = 3; // Đã tăng lên 1
//        gbc.gridwidth = 1;
//        formPanel.add(new JLabel("Trạng thái:"), gbc);
//        
//        gbc.gridx = 1;
//        cboTrangThai = new JComboBox<>(new String[]{
//            "Chờ xác nhận", "Đã xác nhận", "Đang thuê", "Đã trả xe", "Đã hủy", "Hoàn thành"
//        });
//        formPanel.add(cboTrangThai, gbc);
//        
//        // Tổng tiền
//        gbc.gridx = 0;
//        gbc.gridy = 4; // Đã tăng lên 1
//        formPanel.add(new JLabel("Tổng tiền:"), gbc);
//        
//        gbc.gridx = 1;
//        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//        lblTongTien = new JLabel(currencyFormat.format(hopDong.getTongTien()));
//        lblTongTien.setFont(lblTongTien.getFont().deriveFont(Font.BOLD));
//        formPanel.add(lblTongTien, gbc);
//        
//        // Hiển thị thông tin nếu đang sửa hợp đồng
//        if (hopDong.getMaHD() != null) {
//            // Thêm thông tin mã hợp đồng và ngày lập
//            JPanel pnlHeader = new JPanel(new GridLayout(2, 2, 10, 5));
//            pnlHeader.add(new JLabel("Mã hợp đồng:"));
//            JLabel lblMaHD = new JLabel(hopDong.getMaHD());
//            lblMaHD.setFont(lblMaHD.getFont().deriveFont(Font.BOLD));
//            pnlHeader.add(lblMaHD);
//            
//            pnlHeader.add(new JLabel("Ngày lập:"));
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            JLabel lblNgayLap = new JLabel(dateFormat.format(hopDong.getNgayLap()));
//            pnlHeader.add(lblNgayLap);
//            
//            panel.add(pnlHeader, BorderLayout.NORTH);
//            
//            // Set các giá trị
//            selectKhachHangByMa(hopDong.getMaKH());
//            selectNhanVienByMa(hopDong.getMaNV()); // Thêm dòng này
//            txtDiaChiGiao.setText(hopDong.getDiaChiGiao());
//            cboTrangThai.setSelectedItem(hopDong.getTrangThai());
//        }
//        
//        panel.add(formPanel, BorderLayout.CENTER);
//        
//        // Sự kiện thêm khách hàng mới
//        btnAddKH.addActionListener(e -> showThemKHDialog());
//        
//        return panel;
//    }
//    
//    private void loadNhanVienToComboBox() {
//        cboNhanVien.removeAllItems();
//        
//        // Thêm item trống để người dùng có thể chọn
//        cboNhanVien.addItem("--- Chọn nhân viên ---");
//        
//        List<NhanVien> danhSachNV = nhanVienController.getAllNhanVien();
//        
//        for (NhanVien nv : danhSachNV) {
//            String item = nv.getMaNV() + " - " + nv.getHoTen();
//            cboNhanVien.addItem(item);
//        }
//    }
//    
//    // Thêm phương thức chọn nhân viên theo mã
//    private void selectNhanVienByMa(String maNV) {
//        if (maNV == null || maNV.isEmpty()) {
//            cboNhanVien.setSelectedIndex(0);
//            return;
//        }
//        
//        for (int i = 0; i < cboNhanVien.getItemCount(); i++) {
//            String item = cboNhanVien.getItemAt(i);
//            if (item.startsWith(maNV + " - ")) {
//                cboNhanVien.setSelectedIndex(i);
//                break;
//            }
//        }
//    }
//    
//    // Thêm phương thức lấy mã nhân viên từ combobox
//    private String getMaNVFromComboBox() {
//        String selected = (String) cboNhanVien.getSelectedItem();
//        if (selected == null || selected.equals("--- Chọn nhân viên ---")) {
//            return null;
//        }
//        
//        return selected.split(" - ")[0];
//    }
//    
//    private JPanel createXeThuePanel() {
//        JPanel panel = new JPanel(new BorderLayout(10, 10));
//        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
//
//        // Tạo bảng danh sách xe thuê
//        String[] columns = {"Mã xe", "Tên xe", "Biển số", "Hãng xe", "Từ ngày", "Đến ngày", "Số ngày", "Giá thuê/ngày", "Thành tiền"};
//        modelXeThue = new DefaultTableModel(columns, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//
//        tblXeThue = new JTable(modelXeThue);
//        tblXeThue.getTableHeader().setReorderingAllowed(false);
//        JScrollPane scrollPane = new JScrollPane(tblXeThue);
//
//        // Panel nút chức năng
//        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        JButton btnAddXe = new JButton("Thêm xe thuê");
//        JButton btnEditXe = new JButton("Sửa");
//        JButton btnRemoveXe = new JButton("Xóa");
//
//        pnlButtons.add(btnAddXe);
//        pnlButtons.add(btnEditXe);
//        pnlButtons.add(btnRemoveXe);
//
//        panel.add(scrollPane, BorderLayout.CENTER);
//        panel.add(pnlButtons, BorderLayout.SOUTH);
//
//        // Luôn gọi loadXeThueToTable() bất kể danh sách có dữ liệu hay không
//        loadXeThueToTable();
//
//        // Sự kiện nút thêm xe
//        btnAddXe.addActionListener(e -> showChonXeDialog());
//
//        // Sự kiện nút sửa xe
//        btnEditXe.addActionListener(e -> editSelectedXeThue());
//
//        // Sự kiện nút xóa xe
//        btnRemoveXe.addActionListener(e -> removeSelectedXeThue());
//
//        // Sự kiện chọn hàng trong bảng
//        tblXeThue.getSelectionModel().addListSelectionListener(e -> {
//            boolean hasSelection = tblXeThue.getSelectedRow() != -1;
//            btnEditXe.setEnabled(hasSelection && !readOnly);
//            btnRemoveXe.setEnabled(hasSelection && !readOnly);
//        });
//
//        // Ban đầu các nút sẽ disable
//        btnEditXe.setEnabled(false);
//        btnRemoveXe.setEnabled(false);
//
//        // Disable nút thêm xe nếu là chế độ chỉ xem
//        btnAddXe.setEnabled(!readOnly);
//
//        return panel;
//    }
//    
//    private void loadKhachHangToComboBox() {
//        cboKhachHang.removeAllItems();
//        
//        // Thêm item trống để người dùng có thể chọn không có khách hàng
//        cboKhachHang.addItem("--- Chọn khách hàng ---");
//        
//        List<KhachHang> danhSachKH = khachHangController.getAllKhachHang();
//        for (KhachHang kh : danhSachKH) {
//            cboKhachHang.addItem(kh.getMaKH() + " - " + kh.getHoTen());
//        }
//    }
//    
//    private void selectKhachHangByMa(String maKH) {
//        if (maKH == null || maKH.isEmpty()) {
//            cboKhachHang.setSelectedIndex(0);
//            return;
//        }
//        
//        for (int i = 0; i < cboKhachHang.getItemCount(); i++) {
//            String item = cboKhachHang.getItemAt(i);
//            if (item.startsWith(maKH + " - ")) {
//                cboKhachHang.setSelectedIndex(i);
//                break;
//            }
//        }
//    }
//    
//    private String getMaKHFromComboBox() {
//        String selected = (String) cboKhachHang.getSelectedItem();
//        if (selected == null || selected.equals("--- Chọn khách hàng ---")) {
//            return null;
//        }
//        
//        return selected.split(" - ")[0];
//    }
//    
//    private void loadXeThueToTable() {
//        // Xóa dữ liệu cũ
//        modelXeThue.setRowCount(0);
//        
//        // Format để hiển thị ngày và số tiền
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//        
//        // Lấy danh sách chi tiết hợp đồng
//        List<ChiTietHD> danhSachCT = hopDong.getDanhSachXeThue();
//        
//        // Thêm dữ liệu vào bảng
//        if (danhSachCT != null) {
//            for (ChiTietHD ct : danhSachCT) {
//                // Tính số ngày thuê
//                long diffInMillies = Math.abs(ct.getNgayKetThuc().getTime() - ct.getNgayBatDau().getTime());
//                long diffDays = diffInMillies / (24 * 60 * 60 * 1000);
//                int soNgay = (int) diffDays + 1;
//                
//                // Tính thành tiền
//                double thanhTien = soNgay * ct.getGiaThueNgay();
//                
//                modelXeThue.addRow(new Object[]{
//                    ct.getMaXe(),
//                    ct.getTenXe(),
//                    ct.getBienSo(),
//                    ct.getHangXe(),
//                    dateFormat.format(ct.getNgayBatDau()),
//                    dateFormat.format(ct.getNgayKetThuc()),
//                    soNgay,
//                    currencyFormat.format(ct.getGiaThueNgay()),
//                    currencyFormat.format(thanhTien)
//                });
//            }
//            
//            // Cập nhật tổng tiền
//            updateTongTien();
//        }
//    }
//    
//    private void showThemKHDialog() {
//        HopDongKhachHangDialog dialog = new HopDongKhachHangDialog(this, null);
//        dialog.setVisible(true);
//
//        // Sau khi thêm khách hàng, kiểm tra xem đã thành công chưa
//        if (dialog.isSuccess()) {
//            // Lấy khách hàng vừa thêm
//            KhachHang khachHang = dialog.getKhachHang();
//
//            // Load lại combobox khách hàng
//            loadKhachHangToComboBox();
//
//            // Chọn khách hàng vừa thêm
//            selectKhachHangByMa(khachHang.getMaKH());
//        }    }
//    
//    private void showChonXeDialog() {
//        ChonXeDialog dialog = new ChonXeDialog(this);
//        dialog.setVisible(true);
//        
//        // Nếu đã chọn xe, thêm vào danh sách
//        List<ChiTietHD> selectedXeList = dialog.getSelectedXeList();
//        if (!selectedXeList.isEmpty()) {
//            if (hopDong.getDanhSachXeThue() == null) {
//                hopDong.setDanhSachXeThue(new ArrayList<>());
//            }
//            
//            for (ChiTietHD ct : selectedXeList) {
//                hopDong.getDanhSachXeThue().add(ct);
//            }
//            
//            // Cập nhật bảng và tổng tiền
//            loadXeThueToTable();
//        }
//    }
//    
//    private void editSelectedXeThue() {
//        int selectedRow = tblXeThue.getSelectedRow();
//        if (selectedRow >= 0) {
//            String maXe = tblXeThue.getValueAt(selectedRow, 0).toString();
//            
//            // Tìm chi tiết hợp đồng tương ứng
//            ChiTietHD selectedCT = null;
//            for (ChiTietHD ct : hopDong.getDanhSachXeThue()) {
//                if (ct.getMaXe().equals(maXe)) {
//                    selectedCT = ct;
//                    break;
//                }
//            }
//            
//            if (selectedCT != null) {
//                // Hiển thị dialog sửa thông tin thuê
//                ChiTietThueDialog dialog = new ChiTietThueDialog(this, selectedCT);
//                dialog.setVisible(true);
//                
//                // Cập nhật bảng và tổng tiền nếu đã lưu
//                if (dialog.isConfirmed()) {
//                    loadXeThueToTable();
//                }
//            }
//        }
//    }
//    
//    private void removeSelectedXeThue() {
//        int selectedRow = tblXeThue.getSelectedRow();
//        if (selectedRow >= 0) {
//            // Xác nhận xóa
//            int confirm = JOptionPane.showConfirmDialog(this,
//                    "Bạn có chắc muốn xóa xe này khỏi hợp đồng?",
//                    "Xác nhận xóa",
//                    JOptionPane.YES_NO_OPTION);
//            
//            if (confirm == JOptionPane.YES_OPTION) {
//                // Xóa khỏi danh sách
//                if (selectedRow < hopDong.getDanhSachXeThue().size()) {
//                    hopDong.getDanhSachXeThue().remove(selectedRow);
//                    
//                    // Cập nhật bảng và tổng tiền
//                    loadXeThueToTable();
//                }
//            }
//        }
//    }
//    
//    private void updateTongTien() {
//        double tongTien = 0;
//        
//        // Tính tổng tiền từ danh sách xe thuê
//        for (int i = 0; i < modelXeThue.getRowCount(); i++) {
//            String thanhTienStr = modelXeThue.getValueAt(i, 8).toString();
//            // Xóa định dạng tiền tệ để chuyển về số
//            thanhTienStr = thanhTienStr.replaceAll("[^\\d]", "");
//            tongTien += Double.parseDouble(thanhTienStr);
//        }
//        
//        // Cập nhật hiển thị
//        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//        lblTongTien.setText(currencyFormat.format(tongTien));
//        
//        // Không cần gán lại tổng tiền cho hợp đồng vì trigger sẽ tính tổng tiền khi lưu
//    }
//    
//    private void saveHopDong() {
//        // Hiển thị thông báo xác nhận
//        StringBuilder errorMessage = new StringBuilder();
//        
//        // Kiểm tra đã chọn khách hàng chưa
//        String maKH = getMaKHFromComboBox();
//        if (maKH == null) {
//            errorMessage.append("- Vui lòng chọn khách hàng!\n");
//        }
//        
//        // Kiểm tra đã chọn nhân viên chưa (THÊM MỚI)
//        String maNV = getMaNVFromComboBox();
//        if (maNV == null) {
//            errorMessage.append("- Vui lòng chọn nhân viên phụ trách!\n");
//        }
//        
//        // Kiểm tra địa chỉ giao
//        String diaChiGiao = txtDiaChiGiao.getText().trim();
//        if (diaChiGiao.isEmpty()) {
//            errorMessage.append("- Vui lòng nhập địa chỉ giao xe!\n");
//        }
//        
//        // Kiểm tra đã chọn xe chưa
//        if (hopDong.getDanhSachXeThue() == null || hopDong.getDanhSachXeThue().isEmpty()) {
//            errorMessage.append("- Vui lòng chọn ít nhất một xe để thuê!\n");
//        }
//        
//        // Nếu có lỗi, hiển thị thông báo và dừng lại
//        if (errorMessage.length() > 0) {
//            JOptionPane.showMessageDialog(this, 
//                    "Vui lòng sửa các lỗi sau:\n" + errorMessage.toString(), 
//                    "Lỗi", 
//                    JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        
//        // Cập nhật thông tin hợp đồng
//        hopDong.setMaKH(maKH);
//        hopDong.setMaNV(maNV); // Cập nhật mã nhân viên
//        hopDong.setDiaChiGiao(diaChiGiao);
//        hopDong.setTrangThai(cboTrangThai.getSelectedItem().toString());
//        
//        // Lưu hợp đồng
//        try {
//            boolean success = false;
//            String message = "";
//            
//            if (hopDong.getMaHD() == null) {
//                // Thêm mới
//                String maHD = hopDongController.addHopDong(hopDong);
//                success = (maHD != null);
//                if (success) {
//                    message = "Thêm hợp đồng thành công. Mã hợp đồng: " + maHD;
//                } else {
//                    message = "Thêm hợp đồng thất bại.";
//                }
//            } else {
//                // Cập nhật
//                success = hopDongController.updateHopDong(hopDong);
//                if (success) {
//                    message = "Cập nhật hợp đồng thành công.";
//                } else {
//                    message = "Cập nhật hợp đồng thất bại.";
//                }
//            }
//            
//            // Hiển thị kết quả
//            if (success) {
//                JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                
//                // Làm mới danh sách hợp đồng trong panel cha
//                if (parent != null) {
//                    parent.loadDataToTable();
//                }
//                
//                // Đóng dialog
//                dispose();
//            } else {
//                // Kiểm tra thông báo lỗi từ controller/dao
//                if (hopDongController.getErrorMessage().length() > 0) {
//                    JOptionPane.showMessageDialog(this, 
//                            hopDongController.getErrorMessage(), 
//                            "Lỗi", 
//                            JOptionPane.ERROR_MESSAGE);
//                } else {
//                    JOptionPane.showMessageDialog(this, 
//                            message, 
//                            "Lỗi", 
//                            JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, 
//                    "Lỗi không xác định: " + ex.getMessage(), 
//                    "Lỗi", 
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//    
//    public void setReadOnly(boolean readOnly) {
//        this.readOnly = readOnly;
//        
//        // Disable các thành phần nếu là chế độ chỉ xem
//        cboKhachHang.setEnabled(!readOnly);
//        cboNhanVien.setEnabled(!readOnly); // Thêm dòng này để disable nếu chỉ xem
//        txtDiaChiGiao.setEditable(!readOnly);
//        cboTrangThai.setEnabled(!readOnly);
//    }
//}


package ui.admin;
import controller.HopDongController;
import controller.KhachHangController;
import controller.NhanVienController;
import model.HopDong;
import model.ChiTietHD;
import model.KhachHang;
import model.NhanVien;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class HopDongDialog extends JDialog {
    private HopDong hopDong;
    private HopDongPanel parent;
    private HopDongController hopDongController;
    private KhachHangController khachHangController;
    private NhanVienController nhanVienController;
    
    private boolean readOnly = false;
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JComboBox<String> cboKhachHang;
    private JComboBox<String> cboNhanVien;
    private JTextField txtDiaChiGiao;
    private JComboBox<String> cboTrangThai;
    private JLabel lblTongTien;
    
    // Bảng xe thuê
    private JTable tblXeThue;
    private DefaultTableModel modelXeThue;
    
    public HopDongDialog(Window owner, HopDong hopDong, HopDongPanel parent) {
        super(owner, hopDong == null ? "Thêm hợp đồng mới" : "Chỉnh sửa hợp đồng", ModalityType.APPLICATION_MODAL);
        this.parent = parent;
        this.hopDongController = new HopDongController();
        this.khachHangController = new KhachHangController();
        this.nhanVienController = new NhanVienController();

        System.out.println("HopDongDialog constructor called with hopDong: " + (hopDong != null ? hopDong.getMaHD() : "null"));

        // Nếu là tạo mới, khởi tạo đối tượng HopDong
        if (hopDong == null) {
            System.out.println("Creating new HopDong");
            this.hopDong = new HopDong();
            this.hopDong.setNgayLap(new Date());
            this.hopDong.setDanhSachXeThue(new ArrayList<>());
        } else {
            // Nếu là chỉnh sửa, lấy thông tin đầy đủ từ database
            String maHD = hopDong.getMaHD();
            System.out.println("Loading full HopDong details from DB for: " + maHD);

            // In thông tin khách hàng trước khi lấy từ DB
            System.out.println("Original customer ID: " + hopDong.getMaKH());

            this.hopDong = hopDongController.getHopDongByMa(maHD);

            if (this.hopDong == null) {
                System.err.println("ERROR: Failed to load HopDong with ID: " + maHD);
                this.hopDong = hopDong; // Fallback to the original object
            } else {
                System.out.println("Loaded HopDong: " + this.hopDong.getMaHD() + 
                    ", with " + (this.hopDong.getDanhSachXeThue() != null ? 
                    this.hopDong.getDanhSachXeThue().size() : "0") + " rental items");
                System.out.println("Loaded customer ID: " + this.hopDong.getMaKH());
            }

            // Đảm bảo danh sách xe thuê không null
            if (this.hopDong.getDanhSachXeThue() == null) {
                System.out.println("Creating empty vehicle list for HopDong");
                this.hopDong.setDanhSachXeThue(new ArrayList<>());

                // Thử load danh sách xe thuê từ controller
                List<ChiTietHD> danhSach = hopDongController.getChiTietHDByMaHD(this.hopDong.getMaHD());
                if (danhSach != null && !danhSach.isEmpty()) {
                    System.out.println("Successfully loaded " + danhSach.size() + " rental items");
                    this.hopDong.setDanhSachXeThue(danhSach);
                } else {
                    System.out.println("No rental items found or loading failed");
                }
            }
        }

        initComponents();

        // Đảm bảo dữ liệu được hiển thị ngay cả khi loadXeThueToTable không được gọi
        if (this.hopDong.getDanhSachXeThue() != null) {
            System.out.println("Explicitly calling loadXeThueToTable after initComponents");
            loadXeThueToTable();
        }
    }
    
    private void initComponents() {
        setSize(900, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        tabbedPane = new JTabbedPane();
        
        // Tab 1: Thông tin tổng quan
        JPanel pnlInfo = createInfoPanel();
        tabbedPane.addTab("Thông tin chung", pnlInfo);
        
        // Tab 2: Danh sách xe thuê
        JPanel pnlXeThue = createXeThuePanel();
        tabbedPane.addTab("Danh sách xe thuê", pnlXeThue);
        
        // Panel nút
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        
        pnlButtons.add(btnSave);
        pnlButtons.add(btnCancel);
        
        add(tabbedPane, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);
        
        // Sự kiện nút Lưu
        btnSave.addActionListener(e -> saveHopDong());
        
        // Sự kiện nút Hủy
        btnCancel.addActionListener(e -> dispose());
        
        // Nếu là chế độ chỉ xem
        if (readOnly) {
            cboKhachHang.setEnabled(false);
            cboNhanVien.setEnabled(false);
            txtDiaChiGiao.setEditable(false);
            cboTrangThai.setEnabled(false);
            btnSave.setEnabled(false);
        }
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Khách hàng
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Khách hàng:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cboKhachHang = new JComboBox<>();
        loadKhachHangToComboBox();
        formPanel.add(cboKhachHang, gbc);
        
        // Nút thêm khách hàng mới
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        JButton btnAddKH = new JButton("Thêm KH mới");
        formPanel.add(btnAddKH, gbc);
        
        // Nhân viên phụ trách
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Nhân viên phụ trách:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cboNhanVien = new JComboBox<>();
        loadNhanVienToComboBox();
        formPanel.add(cboNhanVien, gbc);
        
        // Địa chỉ giao
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Địa chỉ giao:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtDiaChiGiao = new JTextField(30);
        formPanel.add(txtDiaChiGiao, gbc);
        
        // Trạng thái
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Trạng thái:"), gbc);
        
        gbc.gridx = 1;
        cboTrangThai = new JComboBox<>(new String[]{
            "Chờ xác nhận", "Đã xác nhận", "Đang thuê", "Đã trả xe", "Đã hủy", "Hoàn thành"
        });
        formPanel.add(cboTrangThai, gbc);
        
        // Tổng tiền
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Tổng tiền:"), gbc);
        
        gbc.gridx = 1;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        lblTongTien = new JLabel(currencyFormat.format(hopDong.getTongTien()));
        lblTongTien.setFont(lblTongTien.getFont().deriveFont(Font.BOLD));
        formPanel.add(lblTongTien, gbc);
        
        // Hiển thị thông tin nếu đang sửa hợp đồng
        if (hopDong.getMaHD() != null) {
            // Thêm thông tin mã hợp đồng và ngày lập
            JPanel pnlHeader = new JPanel(new GridLayout(2, 2, 10, 5));
            pnlHeader.add(new JLabel("Mã hợp đồng:"));
            JLabel lblMaHD = new JLabel(hopDong.getMaHD());
            lblMaHD.setFont(lblMaHD.getFont().deriveFont(Font.BOLD));
            pnlHeader.add(lblMaHD);
            
            pnlHeader.add(new JLabel("Ngày lập:"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            JLabel lblNgayLap = new JLabel(dateFormat.format(hopDong.getNgayLap()));
            pnlHeader.add(lblNgayLap);
            
            panel.add(pnlHeader, BorderLayout.NORTH);
            
            System.out.println("Setting customer ID for selection: " + hopDong.getMaKH());
            // Set các giá trị
            selectKhachHangByMa(hopDong.getMaKH());
            selectNhanVienByMa(hopDong.getMaNV());
            txtDiaChiGiao.setText(hopDong.getDiaChiGiao());
            cboTrangThai.setSelectedItem(hopDong.getTrangThai());
        }
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        // Sự kiện thêm khách hàng mới
        btnAddKH.addActionListener(e -> showThemKHDialog());
        
        return panel;
    }
    
    private void loadNhanVienToComboBox() {
        cboNhanVien.removeAllItems();
        
        // Thêm item trống để người dùng có thể chọn
        cboNhanVien.addItem("--- Chọn nhân viên ---");
        
        List<NhanVien> danhSachNV = nhanVienController.getAllNhanVien();
        
        for (NhanVien nv : danhSachNV) {
            String item = nv.getMaNV() + " - " + nv.getHoTen();
            cboNhanVien.addItem(item);
        }
    }
    
    // Thêm phương thức chọn nhân viên theo mã
    private void selectNhanVienByMa(String maNV) {
        if (maNV == null || maNV.isEmpty()) {
            cboNhanVien.setSelectedIndex(0);
            return;
        }
        
        for (int i = 0; i < cboNhanVien.getItemCount(); i++) {
            String item = cboNhanVien.getItemAt(i);
            if (item.startsWith(maNV + " - ")) {
                cboNhanVien.setSelectedIndex(i);
                break;
            }
        }
    }
    
    // Thêm phương thức lấy mã nhân viên từ combobox
    private String getMaNVFromComboBox() {
        String selected = (String) cboNhanVien.getSelectedItem();
        if (selected == null || selected.equals("--- Chọn nhân viên ---")) {
            return null;
        }
        
        return selected.split(" - ")[0];
    }
    
    private JPanel createXeThuePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Tạo bảng danh sách xe thuê
        String[] columns = {"Mã xe", "Tên xe", "Biển số", "Hãng xe", "Từ ngày", "Đến ngày", "Số ngày", "Giá thuê/ngày", "Thành tiền"};
        modelXeThue = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblXeThue = new JTable(modelXeThue);
        tblXeThue.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tblXeThue);
        
        // Panel nút chức năng
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAddXe = new JButton("Thêm xe thuê");
        JButton btnEditXe = new JButton("Sửa");
        JButton btnRemoveXe = new JButton("Xóa");
        
        pnlButtons.add(btnAddXe);
        pnlButtons.add(btnEditXe);
        pnlButtons.add(btnRemoveXe);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(pnlButtons, BorderLayout.SOUTH);
        
        // LUÔN GỌI loadXeThueToTable() ở đây để đảm bảo dữ liệu được hiển thị
        System.out.println("Inside createXeThuePanel: Loading vehicle data");
        loadXeThueToTable();
        
        // Sự kiện nút thêm xe
        btnAddXe.addActionListener(e -> showChonXeDialog());
        
        // Sự kiện nút sửa xe
        btnEditXe.addActionListener(e -> editSelectedXeThue());
        
        // Sự kiện nút xóa xe
        btnRemoveXe.addActionListener(e -> removeSelectedXeThue());
        
        // Sự kiện chọn hàng trong bảng
        tblXeThue.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = tblXeThue.getSelectedRow() != -1;
            btnEditXe.setEnabled(hasSelection && !readOnly);
            btnRemoveXe.setEnabled(hasSelection && !readOnly);
        });
        
        // Ban đầu các nút sẽ disable
        btnEditXe.setEnabled(false);
        btnRemoveXe.setEnabled(false);
        
        // Disable nút thêm xe nếu là chế độ chỉ xem
        btnAddXe.setEnabled(!readOnly);
        
        return panel;
    }
    
    private void selectKhachHangByMa(String maKH) {
        if (maKH == null || maKH.isEmpty()) {
            cboKhachHang.setSelectedIndex(0);
            System.out.println("WARNING: Customer ID is null or empty, selecting default option");
            return;
        }

        System.out.println("Selecting customer with ID: '" + maKH + "'");
        System.out.println("ComboBox has " + cboKhachHang.getItemCount() + " items");

        boolean found = false;
        for (int i = 0; i < cboKhachHang.getItemCount(); i++) {
            String item = cboKhachHang.getItemAt(i);
            System.out.println("Item " + i + ": " + item);
            if (item != null && item.startsWith(maKH + " - ")) {
                cboKhachHang.setSelectedIndex(i);
                System.out.println("Found matching customer at index: " + i);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("WARNING: Could not find customer with ID '" + maKH + "' in combo box");

            // Phương pháp khắc phục: Lấy trực tiếp thông tin khách hàng từ DB
            KhachHang kh = khachHangController.getKhachHangByMa(maKH);
            if (kh != null) {
                String newItem = kh.getMaKH() + " - " + kh.getHoTen();
                System.out.println("Adding missing customer to combo box: " + newItem);
                cboKhachHang.addItem(newItem);
                cboKhachHang.setSelectedItem(newItem);
            } else {
                System.out.println("ERROR: Could not find customer with ID '" + maKH + "' in database");
            }
        }
    }

    private void loadKhachHangToComboBox() {
        try {
            System.out.println("Loading customers to combo box");
            cboKhachHang.removeAllItems();

            // Thêm item trống để người dùng có thể chọn không có khách hàng
            cboKhachHang.addItem("--- Chọn khách hàng ---");

            List<KhachHang> danhSachKH = khachHangController.getAllKhachHang();
            if (danhSachKH == null) {
                System.out.println("ERROR: Customer list is null");
                return;
            }

            System.out.println("Found " + danhSachKH.size() + " customers");
            for (KhachHang kh : danhSachKH) {
                String item = kh.getMaKH() + " - " + kh.getHoTen();
                cboKhachHang.addItem(item);
                System.out.println("Added: " + item);
            }
        } catch (Exception e) {
            System.err.println("Error loading customers: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String getMaKHFromComboBox() {
        String selected = (String) cboKhachHang.getSelectedItem();
        if (selected == null || selected.equals("--- Chọn khách hàng ---")) {
            return null;
        }
        
        return selected.split(" - ")[0];
    }
    
    private void loadXeThueToTable() {
        // Xóa dữ liệu cũ
        modelXeThue.setRowCount(0);
        
        System.out.println("Loading vehicle data to table...");
        
        // Format để hiển thị ngày và số tiền
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        // Lấy danh sách chi tiết hợp đồng
        List<ChiTietHD> danhSachCT = hopDong.getDanhSachXeThue();
        
        // Thêm dữ liệu vào bảng
        if (danhSachCT != null) {
            System.out.println("Found " + danhSachCT.size() + " vehicles to display");
            for (ChiTietHD ct : danhSachCT) {
                if (ct == null) {
                    System.err.println("WARNING: Null ChiTietHD object in list");
                    continue;
                }
                
                try {
                    // Tính số ngày thuê
                    long diffInMillies = Math.abs(ct.getNgayKetThuc().getTime() - ct.getNgayBatDau().getTime());
                    long diffDays = diffInMillies / (24 * 60 * 60 * 1000);
                    int soNgay = (int) diffDays + 1;
                    
                    // Tính thành tiền
                    double thanhTien = soNgay * ct.getGiaThueNgay();
                    
                    System.out.println("Adding vehicle: " + ct.getMaXe() + " - " + ct.getTenXe());
                    
                    modelXeThue.addRow(new Object[]{
                        ct.getMaXe(),
                        ct.getTenXe(),
                        ct.getBienSo(),
                        ct.getHangXe(),
                        dateFormat.format(ct.getNgayBatDau()),
                        dateFormat.format(ct.getNgayKetThuc()),
                        soNgay,
                        currencyFormat.format(ct.getGiaThueNgay()),
                        currencyFormat.format(thanhTien)
                    });
                } catch (Exception e) {
                    System.err.println("Error processing ChiTietHD: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            System.out.println("Added " + modelXeThue.getRowCount() + " rows to table");
            
            // Cập nhật tổng tiền
            updateTongTien();
        } else {
            System.err.println("WARNING: danhSachCT is null");
        }
    }
    
    private void showThemKHDialog() {
        HopDongKhachHangDialog dialog = new HopDongKhachHangDialog(this, null);
        dialog.setVisible(true);

        // Sau khi thêm khách hàng, kiểm tra xem đã thành công chưa
        if (dialog.isSuccess()) {
            // Lấy khách hàng vừa thêm
            KhachHang khachHang = dialog.getKhachHang();

            // Load lại combobox khách hàng
            loadKhachHangToComboBox();

            // Chọn khách hàng vừa thêm
            selectKhachHangByMa(khachHang.getMaKH());
        }
    }
    
    private void showChonXeDialog() {
        ChonXeDialog dialog = new ChonXeDialog(this);
        dialog.setVisible(true);
        
        // Nếu đã chọn xe, thêm vào danh sách
        List<ChiTietHD> selectedXeList = dialog.getSelectedXeList();
        if (!selectedXeList.isEmpty()) {
            if (hopDong.getDanhSachXeThue() == null) {
                hopDong.setDanhSachXeThue(new ArrayList<>());
            }
            
            for (ChiTietHD ct : selectedXeList) {
                hopDong.getDanhSachXeThue().add(ct);
            }
            
            // Cập nhật bảng và tổng tiền
            loadXeThueToTable();
        }
    }
    
    private void editSelectedXeThue() {
        int selectedRow = tblXeThue.getSelectedRow();
        if (selectedRow >= 0) {
            String maXe = tblXeThue.getValueAt(selectedRow, 0).toString();
            
            // Tìm chi tiết hợp đồng tương ứng
            ChiTietHD selectedCT = null;
            for (ChiTietHD ct : hopDong.getDanhSachXeThue()) {
                if (ct.getMaXe().equals(maXe)) {
                    selectedCT = ct;
                    break;
                }
            }
            
            if (selectedCT != null) {
                // Hiển thị dialog sửa thông tin thuê
                ChiTietThueDialog dialog = new ChiTietThueDialog(this, selectedCT);
                dialog.setVisible(true);
                
                // Cập nhật bảng và tổng tiền nếu đã lưu
                if (dialog.isConfirmed()) {
                    loadXeThueToTable();
                }
            }
        }
    }
    
    private void removeSelectedXeThue() {
        int selectedRow = tblXeThue.getSelectedRow();
        if (selectedRow >= 0) {
            // Xác nhận xóa
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa xe này khỏi hợp đồng?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Xóa khỏi danh sách
                if (selectedRow < hopDong.getDanhSachXeThue().size()) {
                    hopDong.getDanhSachXeThue().remove(selectedRow);
                    
                    // Cập nhật bảng và tổng tiền
                    loadXeThueToTable();
                }
            }
        }
    }
    
    private void updateTongTien() {
        double tongTien = 0;
        
        // Tính tổng tiền từ danh sách xe thuê
        for (int i = 0; i < modelXeThue.getRowCount(); i++) {
            String thanhTienStr = modelXeThue.getValueAt(i, 8).toString();
            // Xóa định dạng tiền tệ để chuyển về số
            thanhTienStr = thanhTienStr.replaceAll("[^\\d]", "");
            tongTien += Double.parseDouble(thanhTienStr);
        }
        
        // Cập nhật hiển thị
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        lblTongTien.setText(currencyFormat.format(tongTien));
        
        // Không cần gán lại tổng tiền cho hợp đồng vì trigger sẽ tính tổng tiền khi lưu
    }
    
    private void saveHopDong() {
        // Hiển thị thông báo xác nhận
        StringBuilder errorMessage = new StringBuilder();
        
        // Kiểm tra đã chọn khách hàng chưa
        String maKH = getMaKHFromComboBox();
        if (maKH == null) {
            errorMessage.append("- Vui lòng chọn khách hàng!\n");
        }
        
        // Kiểm tra đã chọn nhân viên chưa
        String maNV = getMaNVFromComboBox();
        if (maNV == null) {
            errorMessage.append("- Vui lòng chọn nhân viên phụ trách!\n");
        }
        
        // Kiểm tra địa chỉ giao
        String diaChiGiao = txtDiaChiGiao.getText().trim();
        if (diaChiGiao.isEmpty()) {
            errorMessage.append("- Vui lòng nhập địa chỉ giao xe!\n");
        }
        
        // Kiểm tra đã chọn xe chưa
        if (hopDong.getDanhSachXeThue() == null || hopDong.getDanhSachXeThue().isEmpty()) {
            errorMessage.append("- Vui lòng chọn ít nhất một xe để thuê!\n");
        }
        
        // Nếu có lỗi, hiển thị thông báo và dừng lại
        if (errorMessage.length() > 0) {
            JOptionPane.showMessageDialog(this, 
                    "Vui lòng sửa các lỗi sau:\n" + errorMessage.toString(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Cập nhật thông tin hợp đồng
        hopDong.setMaKH(maKH);
        hopDong.setMaNV(maNV);
        hopDong.setDiaChiGiao(diaChiGiao);
        hopDong.setTrangThai(cboTrangThai.getSelectedItem().toString());
        
        // Lưu hợp đồng
        try {
            boolean success = false;
            String message = "";
            
            if (hopDong.getMaHD() == null) {
                // Thêm mới
                String maHD = hopDongController.addHopDong(hopDong);
                success = (maHD != null);
                if (success) {
                    message = "Thêm hợp đồng thành công. Mã hợp đồng: " + maHD;
                } else {
                    message = "Thêm hợp đồng thất bại.";
                }
            } else {
                // Cập nhật
                success = hopDongController.updateHopDong(hopDong);
                if (success) {
                    message = "Cập nhật hợp đồng thành công.";
                } else {
                    message = "Cập nhật hợp đồng thất bại.";
                }
            }
            
            // Hiển thị kết quả
            if (success) {
                JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                
                // Làm mới danh sách hợp đồng trong panel cha
                if (parent != null) {
                    parent.loadDataToTable();
                }
                
                // Đóng dialog
                dispose();
            } else {
                // Kiểm tra thông báo lỗi từ controller/dao
                if (hopDongController.getErrorMessage().length() > 0) {
                    JOptionPane.showMessageDialog(this, 
                            hopDongController.getErrorMessage(), 
                            "Lỗi", 
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                            message, 
                            "Lỗi", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "Lỗi không xác định: " + ex.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        
        // Disable các thành phần nếu là chế độ chỉ xem
        if (cboKhachHang != null) cboKhachHang.setEnabled(!readOnly);
        if (cboNhanVien != null) cboNhanVien.setEnabled(!readOnly);
        if (txtDiaChiGiao != null) txtDiaChiGiao.setEditable(!readOnly);
        if (cboTrangThai != null) cboTrangThai.setEnabled(!readOnly);
    }
}