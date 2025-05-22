//package ui.customer;
//
//import model.TaiKhoan;
//import javax.swing.*;
//import java.awt.*;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
//
//public class CustomerDashboard extends JFrame implements CustomerSidebarMenuPanel.MenuClickListener {
//    private JPanel mainPanel;
//    private JPanel contentPanel;
//    private CardLayout cardLayout;
//    private TaiKhoan taiKhoan;
//    
//    // Các panel chức năng
//    private JPanel trangChuPanel;
//    private JPanel thongTinCaNhanPanel;
//    private JPanel xemDatXePanel;
//    private JPanel gioXePanel;
//    private JPanel danhGiaHopDongPanel;
//    
//    public CustomerDashboard(TaiKhoan taiKhoan) {
//        this.taiKhoan = taiKhoan;
//        initComponents();
//        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Mở full màn hình
//    }
//    
//    private void initComponents() {
//        setTitle("Hệ Thống Thuê Xe - CarRental");
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setMinimumSize(new Dimension(1200, 800));
//        
//        // Panel chính chứa tất cả
//        mainPanel = new JPanel(new BorderLayout(0, 0));
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Bỏ border
//        
//        // Tạo menu panel - menu sidebar cho khách hàng
//        CustomerSidebarMenuPanel menuPanel = new CustomerSidebarMenuPanel(taiKhoan, this);
//        
//        // Thiết lập content
//        setupContentPanel();
//        
//        // Thêm vào main panel
//        mainPanel.add(menuPanel, BorderLayout.WEST);
//        mainPanel.add(contentPanel, BorderLayout.CENTER);
//        
//        // Thêm main panel vào frame
//        getContentPane().add(mainPanel);
//    }
//    
//    private void setupContentPanel() {
//        // Thiết lập màu nền
//        this.getContentPane().setBackground(new Color(240, 248, 255)); // Màu xanh Alice Blue
//        
//        contentPanel = new JPanel();
//        contentPanel.setBackground(new Color(245, 245, 245));
//        cardLayout = new CardLayout();
//        contentPanel.setLayout(cardLayout);
//        
//        // Tạo các panel chức năng
//        trangChuPanel = createWelcomePanel();
//        thongTinCaNhanPanel = createUserInfoPanel();
//        xemDatXePanel = createCarRentalPanel();
//        gioXePanel = createCartPanel();
//        danhGiaHopDongPanel = createContractReviewPanel();
//        
//        // Thêm các panel vào cardLayout
//        contentPanel.add(trangChuPanel, "trangChu");
//        contentPanel.add(thongTinCaNhanPanel, "thongTinCaNhan");
//        contentPanel.add(xemDatXePanel, "xemDatXe");
//        contentPanel.add(gioXePanel, "gioXe");
//        contentPanel.add(danhGiaHopDongPanel, "danhGiaHopDong");
//        
//        // Hiển thị panel mặc định
//        cardLayout.show(contentPanel, "trangChu");
//    }
//    
//    // Xử lý khi click vào menu item
//    @Override
//    public void onMenuItemClicked(String panelName) {
//        if ("logout".equals(panelName)) {
//            logout();
//        } else {
//            cardLayout.show(contentPanel, panelName);
//        }
//    }
//    
//    // Phương thức đăng xuất
//    private void logout() {
//        int confirm = JOptionPane.showConfirmDialog(this, 
//                "Bạn có chắc muốn đăng xuất?", 
//                "Xác nhận đăng xuất", 
//                JOptionPane.YES_NO_OPTION);
//        
//        if (confirm == JOptionPane.YES_OPTION) {
//            this.dispose();
//            try {
//                ui.auth.LoginForm loginForm = new ui.auth.LoginForm();
//                loginForm.setVisible(true);
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(this, "Lỗi khi đăng xuất: " + e.getMessage());
//                e.printStackTrace();
//                System.exit(0);
//            }
//        }
//    }
//    
//    // Tạo panel chào mừng đơn giản
//    private JPanel createWelcomePanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBackground(Color.WHITE);
//        
//        JPanel centerPanel = new JPanel();
//        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
//        centerPanel.setBackground(Color.WHITE);
//        
//        JLabel welcomeLabel = new JLabel("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG THUÊ XE");
//        welcomeLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
//        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        // Khoảng cách
//        centerPanel.add(Box.createVerticalStrut(200));
//        centerPanel.add(welcomeLabel);
//        centerPanel.add(Box.createVerticalStrut(20));
//        
//        // Thêm thông tin khách hàng nếu có
//        String customerName = "Khách hàng";
//        if (taiKhoan != null && taiKhoan.getTenDangNhap() != null) {
//            customerName = taiKhoan.getTenDangNhap();
//        }
//        
//        JLabel nameLabel = new JLabel("Xin chào, " + customerName + "!");
//        nameLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 18));
//        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        centerPanel.add(nameLabel);
//        
//        // Thêm ngày hiện tại
//        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//        JLabel dateLabel = new JLabel("Hôm nay: " + date);
//        dateLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        centerPanel.add(Box.createVerticalStrut(10));
//        centerPanel.add(dateLabel);
//        
//        // Thêm các card thống kê
//        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
//        statsPanel.setOpaque(false);
//        statsPanel.setMaximumSize(new Dimension(800, 120));
//        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        statsPanel.add(createStatCard("Xe đang thuê", "0", new Color(76, 175, 80)));
//        statsPanel.add(createStatCard("Giỏ hàng", "0", new Color(255, 152, 0)));
//        statsPanel.add(createStatCard("Lượt đánh giá", "0", new Color(156, 39, 176)));
//        
//        centerPanel.add(Box.createVerticalStrut(50));
//        centerPanel.add(statsPanel);
//        
//        panel.add(centerPanel, BorderLayout.CENTER);
//        return panel;
//    }
//    
//    // Tạo panel thông tin cá nhân
//    private JPanel createUserInfoPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBackground(Color.WHITE);
//        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        
//        JLabel lblTitle = new JLabel("Thông Tin Cá Nhân");
//        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
//        lblTitle.setHorizontalAlignment(JLabel.CENTER);
//        panel.add(lblTitle, BorderLayout.NORTH);
//        
//        // Form thông tin cá nhân
//        JPanel formPanel = new JPanel(new GridBagLayout());
//        formPanel.setOpaque(false);
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10);
//        gbc.anchor = GridBagConstraints.WEST;
//        
//        // Tạo các trường nhập liệu
//        addFormField(formPanel, gbc, "Họ tên:", new JTextField(20), 0);
//        addFormField(formPanel, gbc, "Email:", new JTextField(20), 1);
//        addFormField(formPanel, gbc, "Số điện thoại:", new JTextField(20), 2);
//        addFormField(formPanel, gbc, "Địa chỉ:", new JTextField(20), 3);
//        addFormField(formPanel, gbc, "CMND/CCCD:", new JTextField(20), 4);
//        
//        // Nút cập nhật
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        buttonPanel.setOpaque(false);
//        JButton updateButton = new JButton("Cập nhật thông tin");
//        updateButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        updateButton.setBackground(new Color(46, 149, 233));
//        updateButton.setForeground(Color.WHITE);
//        buttonPanel.add(updateButton);
//        
//        // Panel chứa form và nút
//        JPanel centerPanel = new JPanel(new BorderLayout());
//        centerPanel.setOpaque(false);
//        centerPanel.add(formPanel, BorderLayout.CENTER);
//        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
//        
//        panel.add(centerPanel, BorderLayout.CENTER);
//        
//        return panel;
//    }
//    
//    // Hàm trợ giúp thêm trường vào form
//    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
//        gbc.gridx = 0;
//        gbc.gridy = row;
//        JLabel label = new JLabel(labelText);
//        label.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        panel.add(label, gbc);
//        
//        gbc.gridx = 1;
//        panel.add(field, gbc);
//    }
//    
//    // Tạo panel xem và đặt thuê xe
//    private JPanel createCarRentalPanel() {
//        JPanel panel = new JPanel(new BorderLayout(15, 15));
//        panel.setBackground(Color.WHITE);
//        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        
//        JLabel lblTitle = new JLabel("Xem & Đặt Thuê Xe");
//        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
//        panel.add(lblTitle, BorderLayout.NORTH);
//        
//        // Panel tìm kiếm
//        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        searchPanel.setBackground(new Color(245, 245, 245));
//        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        
//        searchPanel.add(new JLabel("Tìm kiếm:"));
//        searchPanel.add(new JTextField(20));
//        
//        JButton searchButton = new JButton("Tìm");
//        searchButton.setBackground(new Color(46, 149, 233));
//        searchButton.setForeground(Color.WHITE);
//        searchPanel.add(searchButton);
//        
//        // Panel hiển thị danh sách xe
//        JPanel carListPanel = new JPanel(new GridLayout(0, 3, 15, 15));
//        carListPanel.setBackground(Color.WHITE);
//        
//        // Thêm một số xe mẫu
//        carListPanel.add(createCarCard("Toyota Vios", "Sedan", "500,000 VNĐ/ngày"));
//        carListPanel.add(createCarCard("Honda City", "Sedan", "550,000 VNĐ/ngày"));
//        carListPanel.add(createCarCard("Ford Ranger", "Bán tải", "800,000 VNĐ/ngày"));
//        carListPanel.add(createCarCard("Hyundai Accent", "Sedan", "450,000 VNĐ/ngày"));
//        carListPanel.add(createCarCard("Mazda CX-5", "SUV", "900,000 VNĐ/ngày"));
//        carListPanel.add(createCarCard("Kia Morning", "Hatchback", "350,000 VNĐ/ngày"));
//        
//        JScrollPane scrollPane = new JScrollPane(carListPanel);
//        scrollPane.setBorder(null);
//        
//        // Thêm vào panel chính
//        panel.add(searchPanel, BorderLayout.NORTH);
//        panel.add(scrollPane, BorderLayout.CENTER);
//        
//        return panel;
//    }
//    
//    // Hàm trợ giúp tạo card xe
//    private JPanel createCarCard(String carName, String carType, String price) {
//        JPanel cardPanel = new JPanel(new BorderLayout());
//        cardPanel.setBackground(Color.WHITE);
//        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
//        
//        // Panel ảnh xe (giả lập)
//        JPanel imagePanel = new JPanel();
//        imagePanel.setBackground(new Color(230, 230, 230));
//        imagePanel.setPreferredSize(new Dimension(100, 100));
//        JLabel imageLabel = new JLabel("Ảnh xe");
//        imageLabel.setHorizontalAlignment(JLabel.CENTER);
//        imagePanel.add(imageLabel);
//        
//        // Panel thông tin
//        JPanel infoPanel = new JPanel();
//        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
//        infoPanel.setBackground(Color.WHITE);
//        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        
//        JLabel nameLabel = new JLabel(carName);
//        nameLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
//        
//        JLabel typeLabel = new JLabel("Loại: " + carType);
//        typeLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        
//        JLabel priceLabel = new JLabel("Giá thuê: " + price);
//        priceLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        
//        // Nút thêm vào giỏ
//        JButton addButton = new JButton("Thêm vào giỏ");
//        addButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
//        addButton.setBackground(new Color(76, 175, 80));
//        addButton.setForeground(Color.WHITE);
//        
//        infoPanel.add(nameLabel);
//        infoPanel.add(Box.createVerticalStrut(5));
//        infoPanel.add(typeLabel);
//        infoPanel.add(Box.createVerticalStrut(5));
//        infoPanel.add(priceLabel);
//        infoPanel.add(Box.createVerticalStrut(10));
//        infoPanel.add(addButton);
//        
//        cardPanel.add(imagePanel, BorderLayout.NORTH);
//        cardPanel.add(infoPanel, BorderLayout.CENTER);
//        
//        return cardPanel;
//    }
//    
//    // Tạo panel giỏ xe
//    private JPanel createCartPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBackground(Color.WHITE);
//        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        
//        JLabel lblTitle = new JLabel("Giỏ Xe");
//        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
//        lblTitle.setHorizontalAlignment(JLabel.CENTER);
//        panel.add(lblTitle, BorderLayout.NORTH);
//        
//        // Tạo bảng giỏ hàng
//        String[] columnNames = {"Xe", "Loại xe", "Giá thuê/ngày", "Ngày bắt đầu", "Ngày kết thúc", "Thành tiền", "Thao tác"};
//        Object[][] data = {
//            // Giỏ trống
//        };
//        
//        JTable table = new JTable(data, columnNames);
//        table.setRowHeight(40);
//        table.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        table.getTableHeader().setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        
//        JScrollPane scrollPane = new JScrollPane(table);
//        scrollPane.setBorder(BorderFactory.createEmptyBorder());
//        
//        // Panel thông tin thanh toán
//        JPanel paymentPanel = new JPanel(new BorderLayout());
//        paymentPanel.setBackground(new Color(245, 245, 245));
//        paymentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
//        
//        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        totalPanel.setOpaque(false);
//        JLabel totalLabel = new JLabel("Tổng cộng: 0 VNĐ");
//        totalLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
//        totalPanel.add(totalLabel);
//        
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        buttonPanel.setOpaque(false);
//        
//        JButton clearButton = new JButton("Xóa giỏ hàng");
//        clearButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        clearButton.setBackground(new Color(255, 107, 107));
//        clearButton.setForeground(Color.WHITE);
//        
//        JButton checkoutButton = new JButton("Thanh toán");
//        checkoutButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        checkoutButton.setBackground(new Color(76, 175, 80));
//        checkoutButton.setForeground(Color.WHITE);
//        
//        buttonPanel.add(clearButton);
//        buttonPanel.add(Box.createHorizontalStrut(10));
//        buttonPanel.add(checkoutButton);
//        
//        paymentPanel.add(totalPanel, BorderLayout.NORTH);
//        paymentPanel.add(buttonPanel, BorderLayout.SOUTH);
//        
//        // Thêm giỏ hàng trống placeholder
//        JPanel emptyCartPanel = new JPanel();
//        emptyCartPanel.setLayout(new BoxLayout(emptyCartPanel, BoxLayout.Y_AXIS));
//        emptyCartPanel.setOpaque(false);
//        
//        JLabel emptyLabel = new JLabel("Giỏ hàng trống");
//        emptyLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
//        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        JLabel suggestLabel = new JLabel("Hãy thêm xe vào giỏ hàng để đặt thuê");
//        suggestLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 14));
//        suggestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        emptyCartPanel.add(Box.createVerticalGlue());
//        emptyCartPanel.add(emptyLabel);
//        emptyCartPanel.add(Box.createVerticalStrut(10));
//        emptyCartPanel.add(suggestLabel);
//        emptyCartPanel.add(Box.createVerticalGlue());
//        
//        // Thêm vào panel chính
//        panel.add(emptyCartPanel, BorderLayout.CENTER);
//        panel.add(paymentPanel, BorderLayout.SOUTH);
//        
//        return panel;
//    }
//    
//    // Tạo panel đánh giá hợp đồng
//    private JPanel createContractReviewPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBackground(Color.WHITE);
//        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        
//        JLabel lblTitle = new JLabel("Đánh Giá Hợp Đồng");
//        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
//        lblTitle.setHorizontalAlignment(JLabel.CENTER);
//        panel.add(lblTitle, BorderLayout.NORTH);
//        
//        // Bảng hợp đồng đã hoàn thành
//        String[] columnNames = {"Mã HĐ", "Xe", "Ngày thuê", "Ngày trả", "Thành tiền", "Trạng thái", "Đánh giá"};
//        Object[][] data = {
//            // Chưa có hợp đồng
//        };
//        
//        JTable table = new JTable(data, columnNames);
//        table.setRowHeight(40);
//        table.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        table.getTableHeader().setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        
//        JScrollPane scrollPane = new JScrollPane(table);
//        scrollPane.setBorder(BorderFactory.createEmptyBorder());
//        
//        // Panel trống khi chưa có hợp đồng
//        JPanel emptyPanel = new JPanel();
//        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
//        emptyPanel.setOpaque(false);
//        
//        JLabel emptyLabel = new JLabel("Chưa có hợp đồng nào hoàn thành");
//        emptyLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
//        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        JLabel suggestLabel = new JLabel("Hãy thuê xe để có thể đánh giá sau khi hoàn thành hợp đồng");
//        suggestLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 14));
//        suggestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        emptyPanel.add(Box.createVerticalGlue());
//        emptyPanel.add(emptyLabel);
//        emptyPanel.add(Box.createVerticalStrut(10));
//        emptyPanel.add(suggestLabel);
//        emptyPanel.add(Box.createVerticalGlue());
//        
//        // Thêm vào panel chính
//        panel.add(emptyPanel, BorderLayout.CENTER);
//        
//        return panel;
//    }
//    
//    // Tạo thẻ thống kê
//    private JPanel createStatCard(String title, String value, Color color) {
//        JPanel card = new JPanel(new BorderLayout(15, 0));
//        card.setBackground(Color.WHITE);
//        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
//        
//        // Icon panel với nền màu
//        JPanel iconPanel = new JPanel(new BorderLayout());
//        iconPanel.setPreferredSize(new Dimension(60, 60));
//        iconPanel.setBackground(color);
//        
//        JLabel iconLabel = new JLabel("?", JLabel.CENTER);
//        iconLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
//        iconLabel.setForeground(Color.WHITE);
//        
//        iconPanel.add(iconLabel, BorderLayout.CENTER);
//        
//        // Panel thông tin với tiêu đề và giá trị
//        JPanel infoPanel = new JPanel();
//        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
//        infoPanel.setOpaque(false);
//        
//        JLabel titleLabel = new JLabel(title);
//        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        titleLabel.setForeground(new Color(100, 100, 100));
//        
//        JLabel valueLabel = new JLabel(value);
//        valueLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
//        
//        infoPanel.add(titleLabel);
//        infoPanel.add(Box.createVerticalStrut(5));
//        infoPanel.add(valueLabel);
//        
//        card.add(iconPanel, BorderLayout.WEST);
//        card.add(infoPanel, BorderLayout.CENTER);
//        
//        return card;
//    }
//    
//    // Main method cho testing
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                // Set Look and Feel
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//                
//                // Test với tài khoản null
//                CustomerDashboard customerDashboard = new CustomerDashboard(null);
//                customerDashboard.setVisible(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//}

//
//package ui.customer;
//
//import model.TaiKhoan;
//import model.KhachHang;
//import controller.KhachHangController;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
//import ui.customer.GioXePanel;
//
//public class CustomerDashboard extends JFrame implements CustomerSidebarMenuPanel.MenuClickListener {
//    private JPanel mainPanel;
//    private JPanel contentPanel;
//    private CardLayout cardLayout;
//    
//    // Thông tin người dùng
//    private TaiKhoan taiKhoan;
//    private KhachHang khachHang;
//    
//    // Các panel chức năng chính
//    private JPanel trangChuPanel;
//    private XemDatXePanel xemDatXePanel;
//    private GioXePanel gioXePanel;
//    
//    public CustomerDashboard(TaiKhoan taiKhoan) {
//        this.taiKhoan = taiKhoan;
//        
//        // Lấy thông tin khách hàng từ tài khoản
//        if (taiKhoan != null) {
//            try {
//                KhachHangController khachHangController = new KhachHangController();
//                this.khachHang = khachHangController.getKhachHangByTaiKhoan(taiKhoan.getMaTK());
//            } catch (Exception e) {
//                System.err.println("Không thể lấy thông tin khách hàng: " + e.getMessage());
//            }
//        }
//        
//        initComponents();
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//    }
//    
//    private void initComponents() {
//        setTitle("Hệ Thống Thuê Xe - CarRental");
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setMinimumSize(new Dimension(1200, 800));
//        
//        // Panel chính
//        mainPanel = new JPanel(new BorderLayout());
//        
//        // Menu sidebar
//        CustomerSidebarMenuPanel menuPanel = new CustomerSidebarMenuPanel(taiKhoan, this);
//        
//        // Panel nội dung
//        contentPanel = new JPanel();
//        cardLayout = new CardLayout();
//        contentPanel.setLayout(cardLayout);
//        contentPanel.setBackground(Color.WHITE);
//        
//        // Khởi tạo các panel chức năng
//        initFunctionalPanels();
//        
//        // Thêm vào giao diện chính
//        mainPanel.add(menuPanel, BorderLayout.WEST);
//        mainPanel.add(contentPanel, BorderLayout.CENTER);
//        getContentPane().add(mainPanel);
//        
//        // Hiển thị trang chủ mặc định
//        cardLayout.show(contentPanel, "trangChu");
//    }
//    
//    private void initFunctionalPanels() {
//        // Trang chủ đơn giản
//        trangChuPanel = createSimpleWelcomePanel();
//        contentPanel.add(trangChuPanel, "trangChu");
//        
//        // Panel xem và đặt xe - sử dụng class XemDatXePanel đã tạo
//        xemDatXePanel = new XemDatXePanel(taiKhoan, khachHang);
//        contentPanel.add(xemDatXePanel, "xemDatXe");
//        
//        // Panel giỏ xe - sẽ được cập nhật sau
//        gioXePanel = new GioXePanel(taiKhoan, khachHang);
//        contentPanel.add(gioXePanel, "gioXe");
//        JLabel gioXeLabel = new JLabel("Giỏ Xe (Đang phát triển...)", JLabel.CENTER);
//        gioXeLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
//        gioXePanel.add(gioXeLabel, BorderLayout.CENTER);
//        contentPanel.add(gioXePanel, "gioXe");
//        
//        // Panel thông tin cá nhân
//        JPanel thongTinPanel = new JPanel(new BorderLayout());
//        JLabel thongTinLabel = new JLabel("Thông Tin Cá Nhân (Đang phát triển...)", JLabel.CENTER);
//        thongTinLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
//        thongTinPanel.add(thongTinLabel, BorderLayout.CENTER);
//        contentPanel.add(thongTinPanel, "thongTinCaNhan");
//        
//        // Panel đánh giá hợp đồng
//        JPanel danhGiaPanel = new JPanel(new BorderLayout());
//        JLabel danhGiaLabel = new JLabel("Đánh Giá Hợp Đồng (Đang phát triển...)", JLabel.CENTER);
//        danhGiaLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
//        danhGiaPanel.add(danhGiaLabel, BorderLayout.CENTER);
//        contentPanel.add(danhGiaPanel, "danhGiaHopDong");
//    }
//    
//    private JPanel createSimpleWelcomePanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBackground(Color.WHITE);
//        
//        JPanel centerPanel = new JPanel();
//        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
//        centerPanel.setBackground(Color.WHITE);
//        
//        // Chào mừng
//        JLabel welcomeLabel = new JLabel("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG THUÊ XE");
//        welcomeLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 28));
//        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        // Tên người dùng
//        String customerName = (taiKhoan != null) ? taiKhoan.getTenDangNhap() : "Khách hàng";
//        JLabel nameLabel = new JLabel("Xin chào, " + customerName + "!");
//        nameLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 20));
//        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        // Hướng dẫn
//        JLabel guideLabel = new JLabel("Vui lòng chọn chức năng từ menu bên trái");
//        guideLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 16));
//        guideLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        centerPanel.add(Box.createVerticalGlue());
//        centerPanel.add(welcomeLabel);
//        centerPanel.add(Box.createVerticalStrut(20));
//        centerPanel.add(nameLabel);
//        centerPanel.add(Box.createVerticalStrut(10));
//        centerPanel.add(guideLabel);
//        centerPanel.add(Box.createVerticalGlue());
//        
//        panel.add(centerPanel, BorderLayout.CENTER);
//        return panel;
//    }
//    
//    @Override
//    public void onMenuItemClicked(String panelName) {
//        if ("logout".equals(panelName)) {
//            logout();
//        } else {
//            // Cập nhật dữ liệu nếu cần
//            if ("xemDatXe".equals(panelName)) {
//                xemDatXePanel.refreshData();
//            }
//            
//            cardLayout.show(contentPanel, panelName);
//        }
//    }
//    
//    private void logout() {
//        int confirm = JOptionPane.showConfirmDialog(this, 
//                "Bạn có chắc muốn đăng xuất?", 
//                "Xác nhận đăng xuất", 
//                JOptionPane.YES_NO_OPTION);
//        
//        if (confirm == JOptionPane.YES_OPTION) {
//            this.dispose();
//            try {
//                ui.auth.LoginForm loginForm = new ui.auth.LoginForm();
//                loginForm.setVisible(true);
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(this, "Lỗi khi đăng xuất: " + e.getMessage());
//                System.exit(0);
//            }
//        }
//    }
//}


package ui.customer;

import model.TaiKhoan;
import model.KhachHang;
import controller.KhachHangController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import ui.customer.GioXePanel;

public class CustomerDashboard extends JFrame implements CustomerSidebarMenuPanel.MenuClickListener {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Thông tin người dùng
    private TaiKhoan taiKhoan;
    private KhachHang khachHang;
    
    // Các panel chức năng chính
    private JPanel trangChuPanel;
    private XemDatXePanel xemDatXePanel;
    private GioXePanel gioXePanel;
    
    public CustomerDashboard(TaiKhoan taiKhoan) {
        this(taiKhoan, null);
        
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    public CustomerDashboard(TaiKhoan taiKhoan, KhachHang khachHang) {
        this.taiKhoan = taiKhoan;
        this.khachHang = khachHang;
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Debug để kiểm tra
        System.out.println("CustomerDashboard - Tài khoản: " + 
                           (taiKhoan != null ? taiKhoan.getTenDangNhap() : "null"));
        System.out.println("CustomerDashboard - Khách hàng: " + 
                           (khachHang != null ? khachHang.getHoTen() : "null"));

        // Truyền thông tin đăng nhập đến các panel
        xemDatXePanel.updateAccount(taiKhoan, khachHang);
        // Truyền thông tin đến các panel khác nếu cần
    }
    private void initComponents() {
        setTitle("Hệ Thống Thuê Xe - CarRental");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        
        // Panel chính
        mainPanel = new JPanel(new BorderLayout());
        
        // Menu sidebar
        CustomerSidebarMenuPanel menuPanel = new CustomerSidebarMenuPanel(taiKhoan, this);
        
        // Panel nội dung
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        
        // Khởi tạo các panel chức năng
        initFunctionalPanels();
        
        // Thêm vào giao diện chính
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        getContentPane().add(mainPanel);
        
        // Hiển thị trang chủ mặc định
        cardLayout.show(contentPanel, "trangChu");
    }
    
    private void initFunctionalPanels() {
        // Trang chủ đơn giản
        trangChuPanel = createSimpleWelcomePanel();
        contentPanel.add(trangChuPanel, "trangChu");
        
        // Panel xem và đặt xe - sử dụng class XemDatXePanel đã tạo
        xemDatXePanel = new XemDatXePanel(taiKhoan, khachHang);
        contentPanel.add(xemDatXePanel, "xemDatXe");
        
        // Panel giỏ xe - sử dụng class GioXePanel đã tạo
        gioXePanel = new GioXePanel(taiKhoan, khachHang);
        contentPanel.add(gioXePanel, "gioXe");
        
        // Panel thông tin cá nhân
        JPanel thongTinPanel = new JPanel(new BorderLayout());
        JLabel thongTinLabel = new JLabel("Thông Tin Cá Nhân (Đang phát triển...)", JLabel.CENTER);
        thongTinLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        thongTinPanel.add(thongTinLabel, BorderLayout.CENTER);
        contentPanel.add(thongTinPanel, "thongTinCaNhan");
        
        // Panel đánh giá hợp đồng
        JPanel danhGiaPanel = new JPanel(new BorderLayout());
        JLabel danhGiaLabel = new JLabel("Đánh Giá Hợp Đồng (Đang phát triển...)", JLabel.CENTER);
        danhGiaLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        danhGiaPanel.add(danhGiaLabel, BorderLayout.CENTER);
        contentPanel.add(danhGiaPanel, "danhGiaHopDong");
    }
    
    private JPanel createSimpleWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        
        // Chào mừng
        JLabel welcomeLabel = new JLabel("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG THUÊ XE");
        welcomeLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 28));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Tên người dùng
        String customerName = (taiKhoan != null) ? taiKhoan.getTenDangNhap() : "Khách hàng";
        JLabel nameLabel = new JLabel("Xin chào, " + customerName + "!");
        nameLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 20));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Hướng dẫn
        JLabel guideLabel = new JLabel("Vui lòng chọn chức năng từ menu bên trái");
        guideLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 16));
        guideLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(welcomeLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(nameLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(guideLabel);
        centerPanel.add(Box.createVerticalGlue());
        
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }
    
    @Override
    public void onMenuItemClicked(String panelName) {
        if ("logout".equals(panelName)) {
            logout();
        } else {
            // Cập nhật dữ liệu nếu cần
            if ("xemDatXe".equals(panelName)) {
                xemDatXePanel.refreshData();
            } else if ("gioXe".equals(panelName)) {
                gioXePanel.refreshData();
            }
            
            cardLayout.show(contentPanel, panelName);
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn đăng xuất?", 
                "Xác nhận đăng xuất", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            try {
                ui.auth.LoginForm loginForm = new ui.auth.LoginForm();
                loginForm.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi đăng xuất: " + e.getMessage());
                System.exit(0);
            }
        }
    } public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set Look and Feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Test với tài khoản null
                CustomerDashboard customerDashboard = new CustomerDashboard(null);
                customerDashboard.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}