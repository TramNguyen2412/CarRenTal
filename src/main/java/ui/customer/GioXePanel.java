package ui.customer;

import controller.GioXeController;
import controller.HopDongController;
import model.TaiKhoan;
import model.KhachHang;
import model.GioXe;
import model.HopDong;
import model.ChiTietHD;
import util.ImageUtil;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class GioXePanel extends JPanel {
    private TaiKhoan taiKhoan;
    private KhachHang khachHang;
    
    private GioXeController gioXeController;
    private HopDongController hopDongController;
    
    private JTable gioXeTable;
    private DefaultTableModel tableModel;
    private JLabel totalItemsLabel;
    private JLabel totalPriceLabel;
    private JButton checkoutButton;
    private JButton clearCartButton;
    
    private SimpleDateFormat dateFormat;
    private DecimalFormat moneyFormat;
    
    private List<GioXe> danhSachGioXe;
    private JPanel centerPanel;
    private JScrollPane scrollPane;
    private JPanel emptyCartPanel;
    
    public GioXePanel() {
        this(null, null);
    }
    
    public GioXePanel(TaiKhoan taiKhoan, KhachHang khachHang) {
        this.taiKhoan = taiKhoan;
        this.khachHang = khachHang;
        this.gioXeController = new GioXeController();
        this.hopDongController = new HopDongController();
        
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        moneyFormat = new DecimalFormat("#,###.##");
        
        danhSachGioXe = new ArrayList<>();
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Giỏ Xe");
        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        totalItemsLabel = new JLabel("0 xe");
        totalItemsLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
        titlePanel.add(totalItemsLabel, BorderLayout.EAST);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Tạo bảng giỏ xe
        createTable();
        
        // Panel thanh toán
        JPanel checkoutPanel = createCheckoutPanel();
        add(checkoutPanel, BorderLayout.SOUTH);
    }
    
    private void createTable() {
        // Cột của bảng
        String[] columnNames = {
            "Tên Xe", "Biển Số", "Hãng Xe", "Ngày Bắt Đầu", 
            "Ngày Kết Thúc", "Số Ngày", "Giá/Ngày", "Thành Tiền", "Thao Tác"
        };
        
        // Model của bảng
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Chỉ cho phép chỉnh sửa cột cuối (thao tác)
                return column == 8;
            }
        };
        
        // Tạo JTable
        gioXeTable = new JTable(tableModel);
        gioXeTable.setRowHeight(40);
        gioXeTable.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        gioXeTable.getTableHeader().setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        gioXeTable.setShowGrid(true);
        gioXeTable.setGridColor(new Color(230, 230, 230));
        
        // Căn giữa header
        TableCellRenderer headerRenderer = gioXeTable.getTableHeader().getDefaultRenderer();
        if (headerRenderer instanceof DefaultTableCellRenderer) {
            ((DefaultTableCellRenderer) headerRenderer).setHorizontalAlignment(JLabel.CENTER);
        }
        
        // Thiết lập renderer cho các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        // Căn giữa hầu hết các cột
        for (int i = 0; i < gioXeTable.getColumnCount() - 1; i++) {
            if (i == 6 || i == 7) {
                // Căn phải cho cột giá và thành tiền
                gioXeTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
            } else {
                gioXeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        // Thiết lập kích thước cột
        gioXeTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Tên xe
        gioXeTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Biển số
        gioXeTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Hãng xe
        gioXeTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Ngày bắt đầu
        gioXeTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Ngày kết thúc
        gioXeTable.getColumnModel().getColumn(5).setPreferredWidth(70);  // Số ngày
        gioXeTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Giá/ngày
        gioXeTable.getColumnModel().getColumn(7).setPreferredWidth(120); // Thành tiền
        gioXeTable.getColumnModel().getColumn(8).setPreferredWidth(150); // Thao tác (bây giờ cần rộng hơn)
        
        // Thêm nút vào cột thao tác
        gioXeTable.getColumnModel().getColumn(8).setCellRenderer(new GioXeTableActionCellRenderer());
        gioXeTable.getColumnModel().getColumn(8).setCellEditor(new GioXeTableActionCellEditor(this));
        
        // Tạo scroll pane cho bảng
        scrollPane = new JScrollPane(gioXeTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230)));
        
        // Panel hiển thị khi giỏ hàng trống
        emptyCartPanel = new JPanel();
        emptyCartPanel.setLayout(new BoxLayout(emptyCartPanel, BoxLayout.Y_AXIS));
        emptyCartPanel.setOpaque(false);
        
        JLabel emptyLabel = new JLabel("Giỏ hàng trống", JLabel.CENTER);
        emptyLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
        emptyLabel.setForeground(new Color(120, 120, 120));
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel suggestLabel = new JLabel("Hãy thêm xe vào giỏ hàng để đặt thuê", JLabel.CENTER);
        suggestLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 16));
        suggestLabel.setForeground(new Color(120, 120, 120));
        suggestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
       JButton browseButton = new JButton("Xem các xe có sẵn");
        browseButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        browseButton.setBackground(new Color(255, 165, 0));
        browseButton.setForeground(Color.WHITE);
        browseButton.setFocusPainted(false);
        browseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Nút 'Xem các xe có sẵn' được nhấn");
                // Tìm CustomerDashboard parent
                Container parent = getParent();
                while (parent != null && !(parent instanceof CustomerDashboard)) {
                    parent = parent.getParent();
                }

                if (parent instanceof CustomerDashboard) {
                    CustomerDashboard dashboard = (CustomerDashboard) parent;
                    System.out.println("Đã tìm thấy CustomerDashboard, chuyển đến tab xemDatXe");
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            dashboard.onMenuItemClicked("xemDatXe");
                        }
                    });
                } else {
                    System.err.println("Không tìm thấy CustomerDashboard");
                }
            }
        });
       
        emptyCartPanel.add(Box.createVerticalGlue());
        emptyCartPanel.add(emptyLabel);
        emptyCartPanel.add(Box.createVerticalStrut(10));
        emptyCartPanel.add(suggestLabel);
        emptyCartPanel.add(Box.createVerticalStrut(20));
        emptyCartPanel.add(browseButton);
        emptyCartPanel.add(Box.createVerticalStrut(20));
        emptyCartPanel.add(Box.createVerticalGlue());
        
        // Card layout để hiển thị bảng hoặc thông báo giỏ hàng trống
        centerPanel = new JPanel(new CardLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(scrollPane, "table");
        centerPanel.add(emptyCartPanel, "empty");
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JPanel createCheckoutPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            new EmptyBorder(15, 0, 0, 0)
        ));
        
        // Panel tổng tiền
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setOpaque(false);
        
        JLabel totalTitle = new JLabel("Tổng tiền:");
        totalTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
        
        totalPriceLabel = new JLabel("0 VND");
        totalPriceLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
        totalPriceLabel.setForeground(new Color(255, 165, 0));
        
        totalPanel.add(totalTitle);
        totalPanel.add(Box.createHorizontalStrut(10));
        totalPanel.add(totalPriceLabel);
        
        // Panel nút thao tác
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setOpaque(false);
        
        clearCartButton = new JButton("Xóa Giỏ Hàng");
        clearCartButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        clearCartButton.setForeground(new Color(222, 0, 0));
        clearCartButton.setFocusPainted(false);
        clearCartButton.addActionListener(e -> clearCart());
        
        checkoutButton = new JButton("Đặt Xe");
        checkoutButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        checkoutButton.setBackground(new Color(0, 150, 0));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);
        checkoutButton.addActionListener(e -> datXe());
        
        buttonsPanel.add(clearCartButton);
        buttonsPanel.add(Box.createHorizontalStrut(10));
        buttonsPanel.add(checkoutButton);
        
        panel.add(totalPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonsPanel);
        
        return panel;
    }
    
    public void loadData() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Kiểm tra nếu khách hàng đã đăng nhập
        if (taiKhoan == null || khachHang == null) {
            updateCartsStatus();
            return;
        }
        
        try {
            // Lấy danh sách giỏ xe từ database
            danhSachGioXe = gioXeController.getGioXeByMaKH(khachHang.getMaKH());
            
            // Thêm dữ liệu vào bảng
            for (GioXe gioXe : danhSachGioXe) {
                Object[] rowData = {
                    gioXe.getTenXe(),
                    gioXe.getBienSo(),
                    gioXe.getHangXe(),
                    dateFormat.format(gioXe.getNgayBatDau()),
                    dateFormat.format(gioXe.getNgayKetThuc()),
                    gioXe.getSoNgayThue(),
                    moneyFormat.format(gioXe.getGiaThueNgay()) + " VND",
                    moneyFormat.format(gioXe.getThanhTien()) + " VND",
                    "Thao tác" // Placeholder for buttons
                };
                tableModel.addRow(rowData);
            }
            
            // Cập nhật thông tin tổng
            updateCartsStatus();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải giỏ hàng: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }
    
    private void updateCartsStatus() {
        int totalItems = tableModel.getRowCount();
        double totalPrice = 0;
        
        // Tính tổng tiền
        if (danhSachGioXe != null) {
            for (GioXe gioXe : danhSachGioXe) {
                totalPrice += gioXe.getThanhTien();
            }
        }
        
        // Cập nhật labels
        totalItemsLabel.setText(totalItems + " xe");
        totalPriceLabel.setText(moneyFormat.format(totalPrice) + " VND");
        
        // Kích hoạt/vô hiệu hóa nút thanh toán và xóa giỏ hàng
        boolean hasItems = totalItems > 0;
        checkoutButton.setEnabled(hasItems);
        clearCartButton.setEnabled(hasItems);
        
        // Hiển thị panel thích hợp sử dụng CardLayout
        CardLayout cl = (CardLayout) centerPanel.getLayout();
        cl.show(centerPanel, hasItems ? "table" : "empty");
    }
    
    public void viewCarDetails(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= danhSachGioXe.size()) {
            return;
        }
        
        GioXe gioXe = danhSachGioXe.get(rowIndex);
        
        // Mở dialog xem chi tiết xe từ giỏ hàng
        XemChiTietXeDialog dialog = new XemChiTietXeDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            gioXe
        );
        
        dialog.setVisible(true);
    }
    
    private void datXe() {
        if (taiKhoan == null || khachHang == null) {
            JOptionPane.showMessageDialog(this,
                "Bạn cần đăng nhập để đặt xe.",
                "Yêu cầu đăng nhập",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        if (danhSachGioXe.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Giỏ hàng trống. Vui lòng thêm xe vào giỏ hàng trước khi đặt xe.",
                "Giỏ hàng trống",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        // Hiển thị dialog để nhập địa chỉ giao xe
        DatXeDialog dialog = new DatXeDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            String diaChiGiao = dialog.getDiaChi();
            String ghiChu = dialog.getGhiChu();
            
            // Tạo hợp đồng mới
            try {
                // Tính tổng tiền từ giỏ xe
                double tongTien = gioXeController.tinhTongTienGioHang(khachHang.getMaKH());
                
                // Tạo đối tượng hợp đồng
                HopDong hopDong = new HopDong();
                hopDong.setMaKH(khachHang.getMaKH());
                hopDong.setNgayLap(new Date()); // Ngày hiện tại
                hopDong.setDiaChiGiao(diaChiGiao);
                hopDong.setTongTien(tongTien);
                hopDong.setTrangThai("Chờ xác nhận");
                
                // Thêm chi tiết hợp đồng
                for (GioXe gioXe : danhSachGioXe) {
                    ChiTietHD chiTiet = new ChiTietHD();
                    chiTiet.setMaXe(gioXe.getMaXe());
                    chiTiet.setNgayBatDau(gioXe.getNgayBatDau());
                    chiTiet.setNgayKetThuc(gioXe.getNgayKetThuc());
                    
                    // Thêm thông tin xe (để debug và hiển thị thông báo lỗi nếu có)
                    chiTiet.setTenXe(gioXe.getTenXe());
                    chiTiet.setBienSo(gioXe.getBienSo());
                    chiTiet.setHangXe(gioXe.getHangXe());
                    chiTiet.setSoCho(gioXe.getSoCho());
                    chiTiet.setGiaThueNgay(gioXe.getGiaThueNgay());
                    
                    // Thêm vào danh sách chi tiết của hợp đồng
                    hopDong.addChiTietHD(chiTiet);
                }
                
                // Kiểm tra điều kiện thuê xe trước khi tạo hợp đồng
                boolean allValid = true;
                StringBuilder errorMessage = new StringBuilder();
                
                for (ChiTietHD chiTiet : hopDong.getDanhSachXeThue()) {
                    String kiemTraKetQua = hopDongController.kiemTraXeThueDuoc(
                        chiTiet.getMaXe(), 
                        chiTiet.getNgayBatDau(), 
                        chiTiet.getNgayKetThuc(),
                        null // Hợp đồng mới nên không có mã HD hiện tại
                    );
                    
                    if (kiemTraKetQua != null) { // Có lỗi
                        allValid = false;
                        errorMessage.append("- Xe ").append(chiTiet.getTenXe())
                                   .append(": ").append(kiemTraKetQua).append("\n");
                    }
                }
                
                if (!allValid) {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể đặt các xe sau:\n" + errorMessage.toString() + 
                        "\nVui lòng xóa các xe này khỏi giỏ và thử lại.",
                        "Lỗi kiểm tra", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Lưu hợp đồng xuống database
                StringBuilder customErrorMessage = new StringBuilder();
                String maHD = hopDongController.addHopDong(hopDong, customErrorMessage);
                
                if (maHD != null) {
                    // Xóa giỏ xe sau khi đặt thành công
                    boolean clearSuccess = gioXeController.xoaTatCaXeTrongGio(khachHang.getMaKH());
                    
                    if (!clearSuccess) {
                        JOptionPane.showMessageDialog(this, 
                            "Đặt xe thành công nhưng không thể xóa giỏ xe. Vui lòng làm mới giỏ xe.", 
                            "Cảnh báo", 
                            JOptionPane.WARNING_MESSAGE);
                    }
                    
                    // Làm mới giỏ xe
                    loadData();
                    
                    // Thông báo thành công
                    JOptionPane.showMessageDialog(this, 
                        "Đặt xe thành công! Mã hợp đồng: " + maHD + 
                        "\nVui lòng chờ nhân viên xác nhận.",
                        "Đặt xe thành công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Có thể chuyển đến HopDongPanel để xem hợp đồng vừa tạo
                    if (getParent() != null && getParent() instanceof JPanel && getParent().getParent() instanceof CustomerDashboard) {
                        CustomerDashboard dashboard = (CustomerDashboard) getParent().getParent();
                        dashboard.onMenuItemClicked("hopDong");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Đặt xe thất bại: " + customErrorMessage.toString(), 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Đã xảy ra lỗi khi đặt xe: " + e.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearCart() {
        if (taiKhoan == null || khachHang == null || danhSachGioXe.isEmpty()) {
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa tất cả xe trong giỏ hàng?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean result = gioXeController.xoaTatCaXeTrongGio(khachHang.getMaKH());
            
            if (result) {
                JOptionPane.showMessageDialog(this,
                    "Đã xóa tất cả xe trong giỏ hàng.",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
                );
                loadData();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Không thể xóa giỏ hàng: " + gioXeController.getErrorMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    public void removeFromCart(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= danhSachGioXe.size()) {
            return;
        }
        
        GioXe gioXe = danhSachGioXe.get(rowIndex);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xóa xe " + gioXe.getTenXe() + " khỏi giỏ hàng?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean result = gioXeController.xoaXeKhoiGio(gioXe.getMaGH());
            
            if (result) {
                loadData();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Không thể xóa xe khỏi giỏ hàng: " + gioXeController.getErrorMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    // Getter for danhSachGioXe
    public List<GioXe> getDanhSachGioXe() {
        return danhSachGioXe;
    }
    
    // Cập nhật thông tin tài khoản đăng nhập
    public void updateAccount(TaiKhoan taiKhoan, KhachHang khachHang) {
        System.out.println("GioXePanel - Cập nhật tài khoản: " + 
                          (taiKhoan != null ? taiKhoan.getTenDangNhap() : "null"));
        System.out.println("GioXePanel - Cập nhật khách hàng: " + 
                          (khachHang != null ? khachHang.getMaKH() : "null"));
                          
        this.taiKhoan = taiKhoan;
        this.khachHang = khachHang;
        loadData();
    }
    
    // Làm mới dữ liệu
    public void refreshData() {
        loadData();
    }
}