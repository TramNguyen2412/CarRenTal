

package ui.admin.QLHD;

import ui.admin.QLHD.ChonXeDialog;
import ui.admin.QLHD.ChiTietThueDialog;
import controller.HopDongController;
import controller.KhachHangController;
import controller.NhanVienController;
import model.HopDong;
import model.ChiTietHD;
import model.KhachHang;
import model.NhanVien;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


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
    
    // Colors
    private final Color PRIMARY_COLOR = new Color(41, 121, 255);
    private final Color ACCENT_COLOR = new Color(0, 150, 136);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private final Color HEADER_COLOR = new Color(33, 150, 243);
    private final Color ERROR_COLOR = new Color(211, 47, 47);
    
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
        setSize(950, 650);
        setLocationRelativeTo(getOwner());
        
        // Main container với border layout và background màu sáng
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // TabPane với font và màu sắc được cải thiện 
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 15));
        tabbedPane.setForeground(new Color(50, 50, 50));
        tabbedPane.setBackground(Color.WHITE);
        
        // Thêm các tab với padding hợp lý
        JPanel pnlInfo = createInfoPanel();
        JPanel pnlXeThue = createXeThuePanel();
        
        // Thêm tab không dùng icon để tránh lỗi
        tabbedPane.addTab("Thông tin chung", pnlInfo);
        tabbedPane.addTab("Danh sách xe thuê", pnlXeThue);
        
        // Thêm tabPane vào container chính
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Panel nút với giao diện được cải thiện
        JPanel pnlButtons = createButtonPanel();
        mainPanel.add(pnlButtons, BorderLayout.SOUTH);
        
        // Thêm container chính vào dialog
        setContentPane(mainPanel);
    }
    
    private JPanel createInfoPanel() {
        // Panel chính với background trắng và viền bo tròn
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        // Panel tiêu đề
        JPanel headerPanel = createHeaderPanel(hopDong.getMaHD() != null ? 
                "Chỉnh sửa hợp đồng" : "Thêm hợp đồng mới", 
                "Nhập thông tin hợp đồng và lựa chọn xe");
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel form sử dụng GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // KHÁCH HÀNG
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        JLabel lblKhachHang = new JLabel("Khách hàng:");
        lblKhachHang.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        formPanel.add(lblKhachHang, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cboKhachHang = new JComboBox<>();
        cboKhachHang.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        cboKhachHang.setPreferredSize(new Dimension(0, 40));
        loadKhachHangToComboBox();
        
        // Panel hàng ngang cho combo box và nút thêm
        JPanel khachHangPanel = new JPanel(new BorderLayout(10, 0));
        khachHangPanel.setBackground(Color.WHITE);
        khachHangPanel.add(cboKhachHang, BorderLayout.CENTER);
        
        // Nút thêm khách hàng
        JButton btnAddKH = new JButton("Thêm KH");
        btnAddKH.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        btnAddKH.setBackground(PRIMARY_COLOR);
        btnAddKH.setForeground(Color.WHITE);
        btnAddKH.setPreferredSize(new Dimension(100, 40));
        khachHangPanel.add(btnAddKH, BorderLayout.EAST);
        
        formPanel.add(khachHangPanel, gbc);
        
        // NHÂN VIÊN PHỤ TRÁCH
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        JLabel lblNhanVien = new JLabel("Nhân viên phụ trách:");
        lblNhanVien.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        formPanel.add(lblNhanVien, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cboNhanVien = new JComboBox<>();
        cboNhanVien.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        cboNhanVien.setPreferredSize(new Dimension(0, 40));
        loadNhanVienToComboBox();
        formPanel.add(cboNhanVien, gbc);
        
        // ĐỊA CHỈ GIAO
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblDiaChi = new JLabel("Địa chỉ giao:");
        lblDiaChi.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        formPanel.add(lblDiaChi, gbc);
        
        gbc.gridx = 1;
        txtDiaChiGiao = new JTextField();
        txtDiaChiGiao.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        txtDiaChiGiao.setPreferredSize(new Dimension(0, 40));
        txtDiaChiGiao.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtDiaChiGiao, gbc);
        
        // TRẠNG THÁI
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        formPanel.add(lblTrangThai, gbc);
        
        gbc.gridx = 1;
        cboTrangThai = new JComboBox<>(new String[]{
            "Chờ xác nhận", "Đã xác nhận", "Đang thuê", "Đã trả xe", "Đã hủy", "Hoàn thành"
        });
        cboTrangThai.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        cboTrangThai.setPreferredSize(new Dimension(0, 40));
        formPanel.add(cboTrangThai, gbc);
        
        // TỔNG TIỀN - Với styling đặc biệt
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblTongTienTitle = new JLabel("Tổng tiền:");
        lblTongTienTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        formPanel.add(lblTongTienTitle, gbc);
        
        gbc.gridx = 1;
        // Panel cho tổng tiền với màu nền và viền khác biệt
        JPanel tongTienPanel = new JPanel(new BorderLayout());
        tongTienPanel.setBackground(new Color(245, 245, 245));
        tongTienPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        // Label tổng tiền với font lớn, đậm và màu nổi bật
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        lblTongTien = new JLabel(currencyFormat.format(hopDong.getTongTien()));
        lblTongTien.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
        lblTongTien.setForeground(ERROR_COLOR);
        tongTienPanel.add(lblTongTien, BorderLayout.CENTER);
        
        formPanel.add(tongTienPanel, gbc);
        
        // Thêm thông tin chi tiết nếu là sửa
        if (hopDong.getMaHD() != null) {
            // Thêm thành phần hiển thị thông tin hợp đồng
            JPanel infoPanel = new JPanel(new GridLayout(2, 2, 15, 10));
            infoPanel.setBackground(new Color(240, 247, 255));
            infoPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 220, 240), 1),
                new EmptyBorder(10, 15, 10, 15)
            ));
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            // Mã hợp đồng với style đặc biệt
            JLabel lblMaHDTitle = new JLabel("Mã hợp đồng:");
            lblMaHDTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
            infoPanel.add(lblMaHDTitle);
            
            JLabel lblMaHDValue = new JLabel(hopDong.getMaHD());
            lblMaHDValue.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
            lblMaHDValue.setForeground(PRIMARY_COLOR);
            infoPanel.add(lblMaHDValue);
            
            // Ngày lập
            JLabel lblNgayLapTitle = new JLabel("Ngày lập:");
            lblNgayLapTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
            infoPanel.add(lblNgayLapTitle);
            
            JLabel lblNgayLapValue = new JLabel(dateFormat.format(hopDong.getNgayLap()));
            lblNgayLapValue.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
            infoPanel.add(lblNgayLapValue);
            
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(20, 5, 8, 5);
            formPanel.add(infoPanel, gbc);
            
            // Đặt giá trị hiện tại
            selectKhachHangByMa(hopDong.getMaKH());
            selectNhanVienByMa(hopDong.getMaNV());
            txtDiaChiGiao.setText(hopDong.getDiaChiGiao());
            cboTrangThai.setSelectedItem(hopDong.getTrangThai());
        }
        
        // Thêm form vào panel chính
        panel.add(formPanel, BorderLayout.CENTER);
        
        // Xử lý sự kiện nút thêm khách hàng
        btnAddKH.addActionListener(e -> showThemKHDialog());
        
        return panel;
    }
    
    private JPanel createXeThuePanel() {
        // Panel chính với background trắng và viền bo tròn
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Tiêu đề và mô tả
        JPanel headerPanel = createHeaderPanel("Quản lý danh sách xe thuê", 
                "Thêm, sửa, xóa xe thuê trong hợp đồng");
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Tạo bảng với styling cải thiện
        String[] columns = {"Mã xe", "Tên xe", "Biển số", "Hãng xe", "Từ ngày", "Đến ngày", "Số ngày", "Giá thuê/ngày", "Thành tiền"};
        modelXeThue = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblXeThue = new JTable(modelXeThue);
        tblXeThue.setRowHeight(40);
        tblXeThue.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        tblXeThue.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblXeThue.setShowGrid(true);
        tblXeThue.setGridColor(new Color(230, 230, 230));
        
        // Styling cho header bảng
        JTableHeader header = tblXeThue.getTableHeader();
        header.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 245));
        header.setForeground(new Color(50, 50, 50));
        header.setPreferredSize(new Dimension(0, 45));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        header.setReorderingAllowed(false);
        
        // Cell renderer cho định dạng dữ liệu
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        // Áp dụng renderer
        tblXeThue.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Mã xe
        tblXeThue.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Hãng xe
        tblXeThue.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Từ ngày
        tblXeThue.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Đến ngày
        tblXeThue.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Số ngày
        tblXeThue.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);  // Giá thuê
        tblXeThue.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);  // Thành tiền
        
        // Custom row renderer cho màu sắc dòng chẵn/lẻ
        tblXeThue.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 252));
                }
                
                // Padding cho text
                setBorder(new CompoundBorder(getBorder(), new EmptyBorder(0, 8, 0, 8)));
                
                return c;
            }
        });
        
        // ScrollPane với styling
        JScrollPane scrollPane = new JScrollPane(tblXeThue);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        scrollPane.setBackground(Color.WHITE);
        
        // Panel nút với các nút được styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        // Các nút với styling (không dùng icon)
        JButton btnAddXe = new JButton("Thêm xe");
        btnAddXe.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        btnAddXe.setBackground(PRIMARY_COLOR);
        btnAddXe.setForeground(Color.WHITE);
        
        JButton btnEditXe = new JButton("Sửa");
        btnEditXe.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        btnEditXe.setBackground(ACCENT_COLOR);
        btnEditXe.setForeground(Color.WHITE);
        
        JButton btnRemoveXe = new JButton("Xóa");
        btnRemoveXe.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        btnRemoveXe.setBackground(ERROR_COLOR);
        btnRemoveXe.setForeground(Color.WHITE);
        
        buttonPanel.add(btnAddXe);
        buttonPanel.add(Box.createHorizontalStrut(10)); // Thêm khoảng cách giữa các nút
        buttonPanel.add(btnEditXe);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(btnRemoveXe);
        
        // Thêm các thành phần vào panel chính
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Luôn gọi loadXeThueToTable để đảm bảo dữ liệu hiển thị
        System.out.println("Inside createXeThuePanel: Loading vehicle data");
        loadXeThueToTable();
        
        // Xử lý sự kiện các nút
        btnAddXe.addActionListener(e -> showChonXeDialog());
        btnEditXe.addActionListener(e -> editSelectedXeThue());
        btnRemoveXe.addActionListener(e -> removeSelectedXeThue());
        
        // Xử lý khi chọn dòng trong bảng
        tblXeThue.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = tblXeThue.getSelectedRow() != -1;
            btnEditXe.setEnabled(hasSelection && !readOnly);
            btnRemoveXe.setEnabled(hasSelection && !readOnly);
        });
        
        // Xử lý double-click vào dòng
        tblXeThue.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !readOnly) {
                    editSelectedXeThue();
                }
            }
        });
        
        // Ban đầu các nút sẽ disable
        btnEditXe.setEnabled(false);
        btnRemoveXe.setEnabled(false);
        
        // Disable nút thêm xe nếu là chế độ chỉ xem
        btnAddXe.setEnabled(!readOnly);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        // Panel nút với background trơn
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBorder(new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        panel.setBackground(new Color(240, 240, 245));
        
        // Nút Hủy
        JButton btnCancel = new JButton("Hủy");
        btnCancel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        btnCancel.setPreferredSize(new Dimension(120, 45));
        btnCancel.setBackground(Color.GRAY);
        btnCancel.setForeground(Color.WHITE);
        // Nút Lưu
        JButton btnSave = new JButton("Lưu");
        btnSave.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 15));
        btnSave.setBackground(PRIMARY_COLOR);
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(150, 45));
        
        // Thêm các nút vào panel
        panel.add(btnCancel);
        panel.add(btnSave);
        
        // Xử lý sự kiện nút Hủy
        btnCancel.addActionListener(e -> dispose());
        
        // Xử lý sự kiện nút Lưu
        btnSave.addActionListener(e -> saveHopDong());
        
        // Nếu là chế độ chỉ xem, disable nút Lưu
        btnSave.setEnabled(!readOnly);
        
        return panel;
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
        //ChonXeDialog dialog = new ChonXeDialog(this);
         // Truyền mã hợp đồng hiện tại vào dialog ChonXeDialog
        String maHD = hopDong.getMaHD(); // Có thể null nếu đang thêm mới

        ChonXeDialog dialog = new ChonXeDialog(this, maHD);
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
                //ChiTietThueDialog dialog = new ChiTietThueDialog(this, selectedCT);
                String maHD = hopDong.getMaHD(); // Có thể null nếu đang thêm mới
            
                // Hiển thị dialog sửa thông tin thuê
                ChiTietThueDialog dialog = new ChiTietThueDialog(this, selectedCT, maHD);
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
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa xe này khỏi hợp đồng?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                    
            if (confirm == JOptionPane.YES_OPTION) {
                // Xóa khỏi danh sách
                if (selectedRow < hopDong.getDanhSachXeThue().size()) {
                    hopDong.getDanhSachXeThue().remove(selectedRow);
                    
                    // Cập nhật bảng và tổng tiền
                    loadXeThueToTable();
                    
                    // Hiển thị thông báo xóa thành công
                    JOptionPane.showMessageDialog(this,
                            "Đã xóa xe khỏi hợp đồng",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
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
    }
    
    private void saveHopDong() {
        // Kiểm tra dữ liệu hợp lệ
        StringBuilder errorMessage = new StringBuilder();
        
        // Kiểm tra đã chọn khách hàng chưa
        String maKH = getMaKHFromComboBox();
        if (maKH == null) {
            errorMessage.append("• Vui lòng chọn khách hàng\n");
        }
        
        // Kiểm tra đã chọn nhân viên chưa
        String maNV = getMaNVFromComboBox();
        if (maNV == null) {
            errorMessage.append("• Vui lòng chọn nhân viên phụ trách\n");
        }
        
        // Kiểm tra địa chỉ giao
        String diaChiGiao = txtDiaChiGiao.getText().trim();
        if (diaChiGiao.isEmpty()) {
            errorMessage.append("• Vui lòng nhập địa chỉ giao xe\n");
        }
        
        // Kiểm tra đã chọn xe chưa
        if (hopDong.getDanhSachXeThue() == null || hopDong.getDanhSachXeThue().isEmpty()) {
            errorMessage.append("• Vui lòng chọn ít nhất một xe để thuê\n");
        }
        
        // Nếu có lỗi, hiển thị thông báo và dừng lại
        if (errorMessage.length() > 0) {
            // Hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(this,
                    errorMessage.toString(),
                    "Vui lòng điền đầy đủ thông tin",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Cập nhật thông tin hợp đồng
        hopDong.setMaKH(maKH);
        hopDong.setMaNV(maNV);
        hopDong.setDiaChiGiao(diaChiGiao);
        hopDong.setTrangThai(cboTrangThai.getSelectedItem().toString());
        
        // Hiển thị con trỏ đang xử lý
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        try {
            boolean success = false;
            String resultMessage = "";
            String errorMsg = "";
            
            if (hopDong.getMaHD() == null) {
                // Thêm mới
                String maHD = hopDongController.addHopDong(hopDong);
                success = (maHD != null);
                if (success) {
                    resultMessage = "Thêm hợp đồng thành công. Mã hợp đồng: " + maHD;
                } else {
                    resultMessage = "Thêm hợp đồng thất bại.";
                    errorMsg = hopDongController.getErrorMessage();
                }
            } else {
                // Cập nhật
                success = hopDongController.updateHopDong(hopDong);
                if (success) {
                    resultMessage = "Cập nhật hợp đồng thành công.";
                } else {
                    resultMessage = "Cập nhật hợp đồng thất bại.";
                    errorMsg = hopDongController.getErrorMessage();
                }
            }
            
            // Khôi phục con trỏ chuột
            setCursor(Cursor.getDefaultCursor());
            
            if (success) {
                // Hiển thị thông báo thành công
                JOptionPane.showMessageDialog(this,
                        resultMessage,
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Làm mới danh sách hợp đồng trong panel cha
                if (parent != null) {
                    parent.loadDataToTable();
                }
                
                // Đóng dialog
                dispose();
            } else {
                // Hiển thị thông báo lỗi
                String msg = errorMsg.isEmpty() ? resultMessage : errorMsg;
                JOptionPane.showMessageDialog(this,
                        msg,
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            // Khôi phục con trỏ chuột
            setCursor(Cursor.getDefaultCursor());
            
            JOptionPane.showMessageDialog(this,
                    "Lỗi không xác định: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel createHeaderPanel(String title, String subTitle) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 20));
        lblTitle.setForeground(HEADER_COLOR);
        panel.add(lblTitle, BorderLayout.NORTH);
        
        JLabel lblSubTitle = new JLabel(subTitle);
        lblSubTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 14));
        lblSubTitle.setForeground(new Color(120, 120, 120));
        panel.add(lblSubTitle, BorderLayout.SOUTH);
        
        return panel;
    }
    
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        
        // Disable các thành phần nếu là chế độ chỉ xem
        if (cboKhachHang != null) cboKhachHang.setEnabled(!readOnly);
        if (cboNhanVien != null) cboNhanVien.setEnabled(!readOnly);
        if (txtDiaChiGiao != null) txtDiaChiGiao.setEditable(!readOnly);
        if (cboTrangThai != null) cboTrangThai.setEnabled(!readOnly);
        
        // Cập nhật UI sau khi đã khởi tạo xong
        if (tabbedPane != null) {
            // Cần update component trên mỗi tab
            SwingUtilities.invokeLater(() -> {
                for (Component comp : getComponents()) {
                    if (comp instanceof JPanel) {
                        updateComponentsReadOnly((JPanel)comp, readOnly);
                    }
                }
            });
        }
    }
    
    // Cập nhật trạng thái readOnly cho tất cả components con
    private void updateComponentsReadOnly(JPanel panel, boolean readOnly) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton)comp;
                // Chỉ disable các nút lưu/thêm/sửa/xóa
                if (!btn.getText().equals("Hủy") && !btn.getText().equals("Đóng")) {
                    btn.setEnabled(!readOnly);
                }
            } else if (comp instanceof JPanel) {
                updateComponentsReadOnly((JPanel)comp, readOnly);
            }
        }
    }
}