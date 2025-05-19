package ui.admin;

import controller.BaoDuongController;
import controller.XeController;
import controller.KhachHangController;
import controller.NhanVienController;
import model.PhieuBaoDuong;
import model.ChiTietBaoDuong;
import model.DichVuBD;
import model.Xe;
import model.KhachHang;
import model.NhanVien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.JTableHeader;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import com.toedter.calendar.JDateChooser;
import controller.DichVuBDController;
import javax.swing.table.TableCellRenderer;

public class BaoDuongPanel extends JPanel {
    private JTable tablePhieuBD;
    private DefaultTableModel modelPhieuBD;
    private JTextField txtSearch;
    private JButton btnAdd, btnRefresh, btnExport;
    private JComboBox<String> cboFilter;
    private BaoDuongController baoDuongController;
    private XeController xeController;
    private KhachHangController khachHangController;
    private NhanVienController nhanVienController;
    private DichVuBDController dichvubdController;
    
    private DecimalFormat currencyFormat;
    private SimpleDateFormat dateFormat;
    
    // Cột của bảng
    private final String[] COLUMNS = {
        "Mã BD", "Xe", "Khách Hàng", "Ngày BD", "Nhân Viên", "Loại BD", "Tổng Tiền", "Thao Tác"
    };

    public BaoDuongPanel() {
        baoDuongController = new BaoDuongController();
        xeController = new XeController();
        khachHangController = new KhachHangController();
        nhanVienController = new NhanVienController();
        dichvubdController = new DichVuBDController();
        currencyFormat = new DecimalFormat("#,###");
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Panel tiêu đề
        JPanel pnlTitle = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ BẢO DƯỠNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        pnlTitle.add(lblTitle, BorderLayout.WEST);
        
        // Panel tìm kiếm và lọc
        JPanel pnlSearchFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtSearch = new JTextField(20);
        cboFilter = new JComboBox<>(new String[]{"Tất cả", "Định Kỳ", "Khách gây hư hại"});
        btnRefresh = new JButton("Làm mới");
        btnExport = new JButton("Xuất Excel");
        
        pnlSearchFilter.add(new JLabel("Tìm kiếm:"));
        pnlSearchFilter.add(txtSearch);
        pnlSearchFilter.add(new JLabel("Lọc:"));
        pnlSearchFilter.add(cboFilter);
        pnlSearchFilter.add(btnRefresh);
        pnlSearchFilter.add(btnExport);
        
        pnlTitle.add(pnlSearchFilter, BorderLayout.EAST);
        add(pnlTitle, BorderLayout.NORTH);
        
        // Panel thêm phiếu bảo dưỡng
        btnAdd = new JButton("Thêm phiếu bảo dưỡng");
        JPanel pnlAdd = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlAdd.add(btnAdd);
        add(pnlAdd, BorderLayout.SOUTH);
        
        // Bảng danh sách phiếu bảo dưỡng
        modelPhieuBD = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Chỉ cho phép chỉnh sửa cột "Thao tác"
            }
        };
        
        tablePhieuBD = new JTable(modelPhieuBD);
        tablePhieuBD.setRowHeight(40); // Chiều cao vừa phải
     
        // Thiết lập độ rộng cột
        TableColumnModel columnModel = tablePhieuBD.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);  // Mã BD
        columnModel.getColumn(1).setPreferredWidth(120); // Xe
        columnModel.getColumn(2).setPreferredWidth(120); // Khách hàng
        columnModel.getColumn(3).setPreferredWidth(100); // Ngày BD
        columnModel.getColumn(4).setPreferredWidth(120); // Nhân viên
        columnModel.getColumn(5).setPreferredWidth(120); // Loại BD
        columnModel.getColumn(6).setPreferredWidth(100); // Tổng tiền
        columnModel.getColumn(7).setPreferredWidth(150); // Thao tác
        
        // Custom renderer cho cột thao tác
        tablePhieuBD.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        tablePhieuBD.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(this));
        
        // Tùy chỉnh header
        JTableHeader header = tablePhieuBD.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(60, 60, 60));
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        // Tùy chỉnh grid lines
        tablePhieuBD.setShowGrid(true);
        tablePhieuBD.setGridColor(new Color(230, 230, 230));

        // Tạo hiệu ứng dòng sọc
        tablePhieuBD.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Thêm padding cho text trong ô
                if (comp instanceof JLabel) {
                    ((JLabel) comp).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                }

                // Màu nền dòng chẵn/lẻ nếu không được chọn và không phải cột thao tác
                if (!isSelected && column != 7) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    comp.setForeground(new Color(50, 50, 50));
                }

                // Căn phải cho cột giá tiền
                if (column == 6) { // Tổng tiền
                    ((DefaultTableCellRenderer)comp).setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    ((DefaultTableCellRenderer)comp).setHorizontalAlignment(SwingConstants.LEFT);
                }

                return comp;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablePhieuBD);
        add(scrollPane, BorderLayout.CENTER);
        
        // Làm đẹp các nút
        styleButton(btnAdd, new Color(41, 121, 255));
        styleButton(btnRefresh, new Color(0, 150, 136));
        styleButton(btnExport, new Color(113, 85, 156));

        // Làm đẹp thanh tìm kiếm
        txtSearch.setPreferredSize(new Dimension(200, 30));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Làm đẹp dropdown lọc
        cboFilter.setPreferredSize(new Dimension(150, 30));
        
        // Thêm sự kiện
        btnAdd.addActionListener(e -> showBaoDuongDialog(null)); // null = thêm mới
        btnRefresh.addActionListener(e -> loadDataToTable());
        btnExport.addActionListener(e -> exportToExcel());
        
        txtSearch.addActionListener(e -> searchPhieuBD());
        cboFilter.addActionListener(e -> filterPhieuBD());
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 35));
    }
    
    public void loadDataToTable() {
        modelPhieuBD.setRowCount(0); // Xóa dữ liệu cũ
        
        List<PhieuBaoDuong> danhSachPhieu = baoDuongController.getAllPhieuBaoDuong();
        for (PhieuBaoDuong phieu : danhSachPhieu) {
            Xe xe = xeController.getXeByMa(phieu.getMaXe());
            KhachHang khachHang = phieu.getMaKH() != null ? khachHangController.getKhachHangByMa(phieu.getMaKH()) : null;
            NhanVien nhanVien = nhanVienController.getNhanVienByMa(phieu.getMaNV());
            
            modelPhieuBD.addRow(new Object[]{
                phieu.getMaBD(),
                xe != null ? xe.getBienSo() : phieu.getMaXe(),
                khachHang != null ? khachHang.getHoTen() : "Không có",
                dateFormat.format(phieu.getNgayBD()),
                nhanVien != null ? nhanVien.getHoTen() : phieu.getMaNV(),
                phieu.getLoaiBD(),
                currencyFormat.format(phieu.getTongTienBD()) + " VNĐ",
                "" // Cột thao tác
            });
        }
    }

    private void searchPhieuBD() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadDataToTable();
            return;
        }
        
        modelPhieuBD.setRowCount(0);
        List<PhieuBaoDuong> danhSachPhieu = baoDuongController.searchPhieuBaoDuong(keyword);
        
        for (PhieuBaoDuong phieu : danhSachPhieu) {
            Xe xe = xeController.getXeByMa(phieu.getMaXe());
            KhachHang khachHang = phieu.getMaKH() != null ? khachHangController.getKhachHangByMa(phieu.getMaKH()) : null;
            NhanVien nhanVien = nhanVienController.getNhanVienByMa(phieu.getMaNV());
            
            modelPhieuBD.addRow(new Object[]{
                phieu.getMaBD(),
                xe != null ? xe.getBienSo() : phieu.getMaXe(),
                khachHang != null ? khachHang.getHoTen() : "Không có",
                dateFormat.format(phieu.getNgayBD()),
                nhanVien != null ? nhanVien.getHoTen() : phieu.getMaNV(),
                phieu.getLoaiBD(),
                currencyFormat.format(phieu.getTongTienBD()) + " VNĐ",
                "" // Cột thao tác
            });
        }
    }
    
    private void filterPhieuBD() {
        String filter = (String) cboFilter.getSelectedItem();
        if (filter.equals("Tất cả")) {
            loadDataToTable();
            return;
        }
        
        modelPhieuBD.setRowCount(0);
        List<PhieuBaoDuong> danhSachPhieu = baoDuongController.getAllPhieuBaoDuong();
        
        for (PhieuBaoDuong phieu : danhSachPhieu) {
            if (phieu.getLoaiBD().equals(filter)) {
                Xe xe = xeController.getXeByMa(phieu.getMaXe());
                KhachHang khachHang = phieu.getMaKH() != null ? khachHangController.getKhachHangByMa(phieu.getMaKH()) : null;
                NhanVien nhanVien = nhanVienController.getNhanVienByMa(phieu.getMaNV());
                
                modelPhieuBD.addRow(new Object[]{
                    phieu.getMaBD(),
                    xe != null ? xe.getBienSo() : phieu.getMaXe(),
                    khachHang != null ? khachHang.getHoTen() : "Không có",
                    dateFormat.format(phieu.getNgayBD()),
                    nhanVien != null ? nhanVien.getHoTen() : phieu.getMaNV(),
                    phieu.getLoaiBD(),
                    currencyFormat.format(phieu.getTongTienBD()) + " VNĐ",
                    "" // Cột thao tác
                });
            }
        }
    }
    
    public void showBaoDuongDialog(PhieuBaoDuong phieu) {
        BaoDuongDialog dialog = new BaoDuongDialog(SwingUtilities.getWindowAncestor(this), phieu, this);
        dialog.setVisible(true);
    }
    
    public void showChiTietBaoDuongDialog(PhieuBaoDuong phieu) {
        ChiTietBaoDuongDialog dialog = new ChiTietBaoDuongDialog(SwingUtilities.getWindowAncestor(this), phieu, this);
        dialog.setVisible(true);
    }
    
    private void exportToExcel() {
        JOptionPane.showMessageDialog(this, "Chức năng xuất Excel sẽ được phát triển sau!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Inner class for BaoDuong Dialog
    class BaoDuongDialog extends JDialog {
        private JTextField txtMaBD;
        private JComboBox<String> cboXe, cboKhachHang, cboNhanVien, cboLoaiBD;
        private JDateChooser dateNgayBD;
        private JButton btnSave, btnCancel;
        private PhieuBaoDuong phieu;
        private BaoDuongPanel parentPanel;
        
        public BaoDuongDialog(Window owner, PhieuBaoDuong phieu, BaoDuongPanel parentPanel) {
            super(owner, phieu == null ? "Thêm Phiếu Bảo Dưỡng Mới" : "Cập Nhật Phiếu Bảo Dưỡng", ModalityType.APPLICATION_MODAL);
            this.phieu = phieu;
            this.parentPanel = parentPanel;
            
            initComponents();
            loadComboBoxData();
            
            if (phieu != null) {
                loadPhieuData();
            }
            
            setSize(500, 400);
            setLocationRelativeTo(owner);
        }
        
        private void initComponents() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            
            // Mã bảo dưỡng
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("Mã bảo dưỡng:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            txtMaBD = new JTextField(20);
            txtMaBD.setEditable(false); // Không cho phép sửa mã
            panel.add(txtMaBD, gbc);
            
            // Xe
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0;
            panel.add(new JLabel("Xe:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.weightx = 1.0;
            cboXe = new JComboBox<>();
            panel.add(cboXe, gbc);
            
            // Khách hàng
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.weightx = 0;
            panel.add(new JLabel("Khách hàng:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.weightx = 1.0;
            cboKhachHang = new JComboBox<>();
            panel.add(cboKhachHang, gbc);
            
            // Ngày bảo dưỡng
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.weightx = 0;
            panel.add(new JLabel("Ngày bảo dưỡng:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 3;
            gbc.weightx = 1.0;
            dateNgayBD = new JDateChooser();
            dateNgayBD.setDate(new Date());
            panel.add(dateNgayBD, gbc);
            
            // Nhân viên
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.weightx = 0;
            panel.add(new JLabel("Nhân viên:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 4;
            gbc.weightx = 1.0;
            cboNhanVien = new JComboBox<>();
            panel.add(cboNhanVien, gbc);
            
            // Loại bảo dưỡng
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.weightx = 0;
            panel.add(new JLabel("Loại bảo dưỡng:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 5;
            gbc.weightx = 1.0;
            cboLoaiBD = new JComboBox<>(new String[]{"Định Kỳ", "Khách gây hư hại"});
            panel.add(cboLoaiBD, gbc);
            
            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnSave = new JButton("Lưu");
            btnCancel = new JButton("Hủy");
            
            styleButton(btnSave, new Color(41, 121, 255));
            styleButton(btnCancel, new Color(150, 150, 150));
            
            buttonPanel.add(btnSave);
            buttonPanel.add(btnCancel);
            
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.gridwidth = 2;
            gbc.weightx = 1.0;
            panel.add(buttonPanel, gbc);
            
            // Add event listeners
            btnSave.addActionListener(e -> savePhieuBD());
            btnCancel.addActionListener(e -> dispose());
            
            // Thêm sự kiện cho loại bảo dưỡng
            cboLoaiBD.addActionListener(e -> {
                String loaiBD = (String) cboLoaiBD.getSelectedItem();
                cboKhachHang.setEnabled(loaiBD.equals("Khách gây hư hại"));
            });
            
            getContentPane().add(panel);
        }
        
        private void loadComboBoxData() {
            // Load xe
            cboXe.removeAllItems();
            List<Xe> danhSachXe = xeController.getAllXe();
            for (Xe xe : danhSachXe) {
                cboXe.addItem(xe.getMaXe() + " - " + xe.getBienSo());
            }
            
            // Load khách hàng
            cboKhachHang.removeAllItems();
            cboKhachHang.addItem("Không có");
            List<KhachHang> danhSachKH = khachHangController.getAllKhachHang();
            for (KhachHang kh : danhSachKH) {
                cboKhachHang.addItem(kh.getMaKH() + " - " + kh.getHoTen());
            }
            
            // Load nhân viên
            cboNhanVien.removeAllItems();
            List<NhanVien> danhSachNV = nhanVienController.getAllNhanVien();
            for (NhanVien nv : danhSachNV) {
                cboNhanVien.addItem(nv.getMaNV() + " - " + nv.getHoTen());
            }
        }
        
        private void loadPhieuData() {
            txtMaBD.setText(phieu.getMaBD());
            
            // Set xe
            for (int i = 0; i < cboXe.getItemCount(); i++) {
                if (cboXe.getItemAt(i).startsWith(phieu.getMaXe())) {
                    cboXe.setSelectedIndex(i);
                    break;
                }
            }
            
            // Set khách hàng
            if (phieu.getMaKH() == null || phieu.getMaKH().isEmpty()) {
                cboKhachHang.setSelectedItem("Không có");
            } else {
                for (int i = 0; i < cboKhachHang.getItemCount(); i++) {
                    if (cboKhachHang.getItemAt(i).startsWith(phieu.getMaKH())) {
                        cboKhachHang.setSelectedIndex(i);
                        break;
                    }
                }
            }
            
            // Set ngày bảo dưỡng
            dateNgayBD.setDate(phieu.getNgayBD());
            
            // Set nhân viên
            for (int i = 0; i < cboNhanVien.getItemCount(); i++) {
                if (cboNhanVien.getItemAt(i).startsWith(phieu.getMaNV())) {
                    cboNhanVien.setSelectedIndex(i);
                    break;
                }
            }
            
            // Set loại bảo dưỡng
            cboLoaiBD.setSelectedItem(phieu.getLoaiBD());
            
            // Enable/disable khách hàng combobox based on loại bảo dưỡng
            cboKhachHang.setEnabled(phieu.getLoaiBD().equals("Khách gây hư hại"));
        }
        
        private void savePhieuBD() {
            if (cboXe.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn xe", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (dateNgayBD.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bảo dưỡng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (cboNhanVien.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String loaiBD = (String) cboLoaiBD.getSelectedItem();
            String maKH = null;
            
            if (loaiBD.equals("Khách gây hư hại")) {
                if (cboKhachHang.getSelectedIndex() <= 0) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cho bảo dưỡng do khách gây hư hại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String khachHangStr = cboKhachHang.getSelectedItem().toString();
                maKH = khachHangStr.substring(0, khachHangStr.indexOf(" -"));
            }
            
            String xeStr = cboXe.getSelectedItem().toString();
            String maXe = xeStr.substring(0, xeStr.indexOf(" -"));
            
            String nhanVienStr = cboNhanVien.getSelectedItem().toString();
            String maNV = nhanVienStr.substring(0, nhanVienStr.indexOf(" -"));
            
            Date ngayBD = dateNgayBD.getDate();
            
            String result;
            if (phieu == null) { // Thêm mới
                result = baoDuongController.addPhieuBaoDuong(maXe, maKH, ngayBD, maNV, loaiBD);
            } else { // Cập nhật
                result = baoDuongController.updatePhieuBaoDuong(phieu.getMaBD(), maXe, maKH, ngayBD, maNV, loaiBD);
            }
            
            JOptionPane.showMessageDialog(this, result);
            
            if (result.contains("thành công")) {
                parentPanel.loadDataToTable();
                dispose();
            }
        }
    }
    
    // Inner class for ChiTietBaoDuong Dialog
    class ChiTietBaoDuongDialog extends JDialog {
        private JLabel lblMaBD, lblXe, lblKhachHang, lblNgayBD, lblNhanVien, lblLoaiBD, lblTongTien;
        private JTable tableChiTiet;
        private DefaultTableModel modelChiTiet;
        private JComboBox<String> cboDichVu;
        private JSpinner spinSoLuong;
        private JButton btnAdd, btnDelete, btnClose;
        private PhieuBaoDuong phieu;
        private BaoDuongPanel parentPanel;
        
        private final String[] CHI_TIET_COLUMNS = {
            "Mã DV", "Tên dịch vụ", "Đơn giá", "Số lượng", "Thành tiền"
        };
        
        public ChiTietBaoDuongDialog(Window owner, PhieuBaoDuong phieu, BaoDuongPanel parentPanel) {
            super(owner, "Chi Tiết Phiếu Bảo Dưỡng", ModalityType.APPLICATION_MODAL);
            this.phieu = phieu;
            this.parentPanel = parentPanel;
            
            initComponents();
            loadPhieuData();
            loadChiTietData();
            loadDichVuComboBox();
            
            setSize(800, 600);
            setLocationRelativeTo(owner);
        }
        
        private void initComponents() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            // Panel thông tin phiếu
            JPanel infoPanel = new JPanel(new GridLayout(4, 4, 10, 10));
            infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu bảo dưỡng"));
            
            infoPanel.add(new JLabel("Mã bảo dưỡng:"));
            lblMaBD = new JLabel();
            infoPanel.add(lblMaBD);
            
            infoPanel.add(new JLabel("Xe:"));
            lblXe = new JLabel();
            infoPanel.add(lblXe);
            
            infoPanel.add(new JLabel("Khách hàng:"));
            lblKhachHang = new JLabel();
            infoPanel.add(lblKhachHang);
            
            infoPanel.add(new JLabel("Ngày bảo dưỡng:"));
            lblNgayBD = new JLabel();
            infoPanel.add(lblNgayBD);
            
            infoPanel.add(new JLabel("Nhân viên:"));
            lblNhanVien = new JLabel();
            infoPanel.add(lblNhanVien);
            
            infoPanel.add(new JLabel("Loại bảo dưỡng:"));
            lblLoaiBD = new JLabel();
            infoPanel.add(lblLoaiBD);
            
            infoPanel.add(new JLabel("Tổng tiền:"));
            lblTongTien = new JLabel();
            lblTongTien.setFont(new Font("Arial", Font.BOLD, 14));
            lblTongTien.setForeground(new Color(231, 76, 60));
            infoPanel.add(lblTongTien);
            
            // Panel thêm dịch vụ
            JPanel addServicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            addServicePanel.setBorder(BorderFactory.createTitledBorder("Thêm dịch vụ"));
            
            addServicePanel.add(new JLabel("Dịch vụ:"));
            cboDichVu = new JComboBox<>();
            cboDichVu.setPreferredSize(new Dimension(300, 30));
            addServicePanel.add(cboDichVu);
            
            addServicePanel.add(new JLabel("Số lượng:"));
            spinSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
            spinSoLuong.setPreferredSize(new Dimension(80, 30));
            addServicePanel.add(spinSoLuong);
            
            btnAdd = new JButton("Thêm");
            styleButton(btnAdd, new Color(41, 121, 255));
            btnAdd.setPreferredSize(new Dimension(100, 30));
            addServicePanel.add(btnAdd);
            
            // Bảng chi tiết
            modelChiTiet = new DefaultTableModel(CHI_TIET_COLUMNS, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            tableChiTiet = new JTable(modelChiTiet);
            tableChiTiet.setRowHeight(30);
            
            // Tùy chỉnh header
            JTableHeader header = tableChiTiet.getTableHeader();
            header.setFont(new Font("Arial", Font.BOLD, 14));
            header.setBackground(new Color(240, 240, 240));
            
            JScrollPane scrollPane = new JScrollPane(tableChiTiet);
            scrollPane.setPreferredSize(new Dimension(750, 300));
            
            // Panel nút
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnDelete = new JButton("Xóa dịch vụ");
            btnClose = new JButton("Đóng");
            
            styleButton(btnDelete, new Color(231, 76, 60));
            styleButton(btnClose, new Color(150, 150, 150));
            
            buttonPanel.add(btnDelete);
            buttonPanel.add(btnClose);
            
            // Add components to main panel
            panel.add(infoPanel, BorderLayout.NORTH);
            panel.add(addServicePanel, BorderLayout.CENTER);
            
            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.add(scrollPane, BorderLayout.CENTER);
            tablePanel.add(buttonPanel, BorderLayout.SOUTH);
            
            panel.add(tablePanel, BorderLayout.SOUTH);
            
            // Add event listeners
            btnAdd.addActionListener(e -> addChiTiet());
            btnDelete.addActionListener(e -> deleteChiTiet());
            btnClose.addActionListener(e -> dispose());
            
            getContentPane().add(panel);
        }
        
        private void loadPhieuData() {
            lblMaBD.setText(phieu.getMaBD());
            
            Xe xe = xeController.getXeByMa(phieu.getMaXe());
            lblXe.setText(xe != null ? xe.getBienSo() : phieu.getMaXe());
            
            KhachHang khachHang = phieu.getMaKH() != null ? khachHangController.getKhachHangByMa(phieu.getMaKH()) : null;
            lblKhachHang.setText(khachHang != null ? khachHang.getHoTen() : "Không có");
            
            lblNgayBD.setText(dateFormat.format(phieu.getNgayBD()));
            
            NhanVien nhanVien = nhanVienController.getNhanVienByMa(phieu.getMaNV());
            lblNhanVien.setText(nhanVien != null ? nhanVien.getHoTen() : phieu.getMaNV());
            
            lblLoaiBD.setText(phieu.getLoaiBD());
            lblTongTien.setText(currencyFormat.format(phieu.getTongTienBD()) + " VNĐ");
        }
        
        private void loadChiTietData() {
            modelChiTiet.setRowCount(0);
            
            List<ChiTietBaoDuong> chiTietList = baoDuongController.getChiTietByMaBD(phieu.getMaBD());
            
            for (ChiTietBaoDuong ct : chiTietList) {
                DichVuBD dv = dichvubdController.getDichVuBDById(ct.getMaDV());
                
                if (dv != null) {
                    double thanhTien = dv.getGiaDV() * ct.getSoLuong();
                    
                    modelChiTiet.addRow(new Object[]{
                        ct.getMaDV(),
                        dv.getTenDV(),
                        currencyFormat.format(dv.getGiaDV()) + " VNĐ",
                        ct.getSoLuong(),
                        currencyFormat.format(thanhTien) + " VNĐ"
                    });
                }
            }
        }
        
        private void loadDichVuComboBox() {
            cboDichVu.removeAllItems();
            
            List<DichVuBD> danhSachDV = baoDuongController.getAllDichVuBD();
            for (DichVuBD dv : danhSachDV) {
                cboDichVu.addItem(dv.getMaDV() + " - " + dv.getTenDV() + " (" + currencyFormat.format(dv.getGiaDV()) + " VNĐ)");
            }
        }
        
        private void addChiTiet() {
            if (cboDichVu.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String dichVuStr = cboDichVu.getSelectedItem().toString();
            String maDV = dichVuStr.substring(0, dichVuStr.indexOf(" -"));
            
            int soLuong = (int) spinSoLuong.getValue();
            
            String result = baoDuongController.addChiTietBaoDuong(phieu.getMaBD(), maDV, soLuong);
            JOptionPane.showMessageDialog(this, result);
            
            if (result.contains("thành công")) {
                loadChiTietData();
                // Cập nhật tổng tiền
                phieu = baoDuongController.getPhieuBaoDuongById(phieu.getMaBD());
                lblTongTien.setText(currencyFormat.format(phieu.getTongTienBD()) + " VNĐ");
                parentPanel.loadDataToTable();
            }
        }
        

        
        private void deleteChiTiet() {
            int selectedRow = tableChiTiet.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String maDV = tableChiTiet.getValueAt(selectedRow, 0).toString();
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn xóa dịch vụ này khỏi phiếu bảo dưỡng?", 
                    "Xác nhận xóa", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                String result = baoDuongController.deleteChiTietBaoDuong(phieu.getMaBD(), maDV);
                JOptionPane.showMessageDialog(this, result);
                
                if (result.contains("thành công")) {
                    loadChiTietData();
                    // Cập nhật tổng tiền
                    phieu = baoDuongController.getPhieuBaoDuongById(phieu.getMaBD());
                    lblTongTien.setText(currencyFormat.format(phieu.getTongTienBD()) + " VNĐ");
                    parentPanel.loadDataToTable();
                }
            }
        }
    }
    
// Inner class for rendering buttons in table
class ButtonRenderer extends JPanel implements TableCellRenderer {
    private JButton btnEdit, btnDetail;

    public ButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        btnEdit = new JButton("Sửa");
        btnDetail = new JButton("Chi tiết");
        
        styleButton(btnEdit, new Color(52, 152, 219));
        styleButton(btnDetail, new Color(46, 204, 113));
        
        add(btnEdit);
        add(btnDetail);
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(70, 30));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

// Inner class for handling button clicks in table
class ButtonEditor extends DefaultCellEditor {
    private JPanel panel;
    private JButton btnEdit, btnDetail;
    private String clickedButton;
    private boolean isPushed;
    private BaoDuongPanel parentPanel;
    
    public ButtonEditor(BaoDuongPanel parentPanel) {
        super(new JTextField());
        this.parentPanel = parentPanel;
        
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        btnEdit = new JButton("Sửa");
        btnDetail = new JButton("Chi tiết");
        
        styleButton(btnEdit, new Color(52, 152, 219));
        styleButton(btnDetail, new Color(46, 204, 113));
        
        panel.add(btnEdit);
        panel.add(btnDetail);
        
        btnEdit.addActionListener(e -> {
            clickedButton = "edit";
            isPushed = true;
            fireEditingStopped();
        });
        
        btnDetail.addActionListener(e -> {
            clickedButton = "detail";
            isPushed = true;
            fireEditingStopped();
        });
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(70, 30));
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        isPushed = false;
        return panel;
    }
    
    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            int selectedRow = tablePhieuBD.getSelectedRow();
            String maBD = tablePhieuBD.getValueAt(selectedRow, 0).toString();
            
            // Lấy thông tin phiếu bảo dưỡng từ controller
            PhieuBaoDuong phieu = baoDuongController.getPhieuBaoDuongById(maBD);
            
            if (clickedButton.equals("edit")) {
                parentPanel.showBaoDuongDialog(phieu);
            } else if (clickedButton.equals("detail")) {
                parentPanel.showChiTietBaoDuongDialog(phieu);
            }
        }
        isPushed = false;
        return "";
    }
    
    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
}