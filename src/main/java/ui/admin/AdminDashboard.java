package ui.admin;
import java.net.URL;
import model.TaiKhoan;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import ui.admin.XePanel;
import ui.admin.HopDongPanel;

public class AdminDashboard extends javax.swing.JFrame {
    
    private JPanel mainPanel;
    private JPanel menuPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private TaiKhoan taiKhoan; // Thông tin tài khoản đăng nhập
    
    // Các panel quản lý - hiện tại chỉ khai báo để tránh lỗi biên dịch
    private JPanel khachHangPanel;
    private JPanel nhanVienPanel;
    private XePanel xePanel;
    private JPanel dichVuBDPanel;
    private JPanel hopDongPanel;
    private JPanel baoDuongPanel;
    private JPanel congNoPanel;
    private JPanel giaoNhanXePanel;
    private JPanel baoCaoPanel;
    private JPanel trangChuPanel;
    
    // Màu vàng pastel cho menu
    private final Color MENU_COLOR = new Color(255, 236, 179); // Màu vàng pastel
    private final Color MENU_HOVER_COLOR = new Color(255, 224, 130); // Màu hover cho menu
    private final Color MENU_TEXT_COLOR = new Color(50, 50, 50); // Màu chữ tối
    
    public AdminDashboard(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Mở full màn hình
    }
    
    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Thuê Xe - CarRental");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0)); // Bỏ border
        
        // Thiết lập menu bên trái
        setupMenuPanel();
        
        // Thiết lập phần hiển thị nội dung
        setupContentPanel();
        
        getContentPane().add(mainPanel);
    }

    private void setupContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setBackground(new Color(245, 245, 245));
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        
        // Tạo trang chủ đơn giản
        trangChuPanel = createWelcomePanel();
        
        // Tạo các panel quản lý
        khachHangPanel = createSimplePanel("Quản Lý Khách Hàng");
        nhanVienPanel = createSimplePanel("Quản Lý Nhân Viên");
        xePanel = new XePanel();
        dichVuBDPanel = createSimplePanel("Quản Lý Dịch Vụ Bảo Dưỡng");
        hopDongPanel = new HopDongPanel();
        baoDuongPanel = createSimplePanel("Quản Lý Bảo Dưỡng");
        congNoPanel = createSimplePanel("Quản Lý Công Nợ");
        giaoNhanXePanel = createSimplePanel("Quản Lý Giao Nhận Xe");
        
        // Tạo panel báo cáo thống kê
        baoCaoPanel = createDashboardPanel();
        
        // Thêm các panel vào cardLayout
        contentPanel.add(trangChuPanel, "trangChu");
        contentPanel.add(khachHangPanel, "khachHang");
        contentPanel.add(nhanVienPanel, "nhanVien");
        contentPanel.add(xePanel, "xe");
        contentPanel.add(dichVuBDPanel, "dichVuBD");
        contentPanel.add(hopDongPanel, "hopDong");
        contentPanel.add(baoDuongPanel, "baoDuong");
        contentPanel.add(congNoPanel, "congNo");
        contentPanel.add(giaoNhanXePanel, "giaoNhanXe");
        contentPanel.add(baoCaoPanel, "baoCao");
        
        // Hiển thị panel mặc định
        cardLayout.show(contentPanel, "trangChu");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }

    private void setupMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(250, 0));
        menuPanel.setBackground(MENU_COLOR); // Đổi màu nền sang vàng pastel
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        // Logo và thông tin người dùng - đảm bảo logoPanel có width đúng bằng menuPanel
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(new Color(13, 25, 38)); // Màu tối cho logo
        logoPanel.setPreferredSize(new Dimension(250, 80));
        logoPanel.setMaximumSize(new Dimension(250, 80));
        logoPanel.setMinimumSize(new Dimension(250, 80));

        // Logo panel cần layout BorderLayout để căn giữa logo và text
        JPanel logoContentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoContentPanel.setOpaque(false);

        // Thêm logo Car.png
        try {
            URL resourceUrl = getClass().getResource("/img/carRental.png");
            if (resourceUrl != null) {
                ImageIcon icon = new ImageIcon(resourceUrl);
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                JLabel iconLabel = new JLabel(new ImageIcon(img));
                logoContentPanel.add(iconLabel);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tải logo: " + e.getMessage());
        }

        JLabel lblLogo = new JLabel("CarRental");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 28)); // Tăng cỡ chữ
        lblLogo.setForeground(Color.WHITE);
        logoContentPanel.add(lblLogo);

        logoPanel.add(logoContentPanel, BorderLayout.CENTER);
        menuPanel.add(logoPanel);

        // Panel thông tin người dùng
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(MENU_COLOR);
        userPanel.setPreferredSize(new Dimension(250, 60));
        userPanel.setMaximumSize(new Dimension(250, 60));

        // Xử lý khi taiKhoan có thể null trong giai đoạn phát triển
        String username = "admin";
        if (taiKhoan != null && taiKhoan.getTenDangNhap() != null) {
            username = taiKhoan.getTenDangNhap();
        }

        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        avatarPanel.setOpaque(false);

        // Tạo iconLabel cho avatar
        JLabel iconLabel;
        try {
            URL resourceUrl = getClass().getResource("/img/admin.png");
            if (resourceUrl != null) {
                ImageIcon icon = new ImageIcon(resourceUrl);
                Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                iconLabel = new JLabel(new ImageIcon(img));
            } else {
                iconLabel = new JLabel("👤");
                iconLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
            }
        } catch (Exception e) {
            iconLabel = new JLabel("👤");
            iconLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        }
        iconLabel.setForeground(MENU_TEXT_COLOR);

        avatarPanel.add(iconLabel);

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(username);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(MENU_TEXT_COLOR);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel roleLabel = new JLabel("Quản trị viên");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(100, 100, 100));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        userInfoPanel.add(nameLabel);
        userInfoPanel.add(roleLabel);

        avatarPanel.add(userInfoPanel);
        userPanel.add(avatarPanel, BorderLayout.CENTER);

        menuPanel.add(userPanel);

        // Thêm đường phân cách
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        separator.setBackground(new Color(200, 200, 200));
        menuPanel.add(separator);

        // Thêm khoảng cách
        menuPanel.add(Box.createVerticalStrut(10));

        // Các nút menu với icon
        addMenuItem("Trang chủ", "home3.png", e -> showPanel("trangChu"));
        addMenuItem("Khách Hàng", "customer.png", e -> showPanel("khachHang"));
        addMenuItem("Nhân Viên", "staff.png", e -> showPanel("nhanVien"));
        addMenuItem("Quản Lý Xe", "Car.png", e -> showPanel("xe"));
        addMenuItem("Dịch Vụ Bảo Dưỡng", "carservices.png", e -> showPanel("dichVuBD"));
        addMenuItem("Hợp Đồng", "contract.png", e -> showPanel("hopDong"));
        addMenuItem("Bảo Dưỡng", "maintenance.png", e -> showPanel("baoDuong"));
        addMenuItem("Công Nợ", "dept.png", e -> showPanel("congNo"));
        addMenuItem("Giao Nhận Xe", "giaonhanxe.png", e -> showPanel("giaoNhanXe"));
        addMenuItem("Báo Cáo Thống Kê", "thongke.png", e -> showPanel("baoCao"));

        // Nút đăng xuất ở dưới cùng
        menuPanel.add(Box.createVerticalGlue());

        // Thêm đường phân cách
        JSeparator separatorBottom = new JSeparator();
        separatorBottom.setForeground(new Color(200, 200, 200));
        separatorBottom.setBackground(new Color(200, 200, 200));
        menuPanel.add(separatorBottom);

        addMenuItem("Đăng Xuất", "logout.png", e -> logout());

        mainPanel.add(menuPanel, BorderLayout.WEST);
    }

   private void addMenuItem(String text, String iconPath, ActionListener action) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(MENU_COLOR);
        itemPanel.setPreferredSize(new Dimension(250, 48));
        itemPanel.setMaximumSize(new Dimension(250, 48));

        // Sử dụng FlowLayout.CENTER để căn giữa nội dung
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        contentPanel.setOpaque(false);

        // Thêm icon nếu có
        if (iconPath != null && !iconPath.isEmpty()) {
            try {
                URL resourceUrl = getClass().getResource("/img/" + iconPath);
                if (resourceUrl != null) {
                    ImageIcon icon = new ImageIcon(resourceUrl);
                    Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    JLabel iconLabel = new JLabel(new ImageIcon(img));
                    contentPanel.add(iconLabel);
                }
            } catch (Exception e) {
                System.out.println("Không thể tải icon: " + iconPath);
            }
        }

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", Font.BOLD, 15));
        textLabel.setForeground(MENU_TEXT_COLOR);
        contentPanel.add(textLabel);

        itemPanel.add(contentPanel, BorderLayout.CENTER);

        // Sử dụng MouseAdapter thông qua lớp ẩn danh
        itemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                itemPanel.setBackground(MENU_HOVER_COLOR);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                itemPanel.setBackground(MENU_COLOR);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (action != null) {
                    action.actionPerformed(new java.awt.event.ActionEvent(itemPanel, java.awt.event.ActionEvent.ACTION_PERFORMED, text));
                }
            }
        });

        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        menuPanel.add(itemPanel);
        menuPanel.add(Box.createVerticalStrut(8)); // Khoảng cách giữa các item
    }
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG QUẢN LÝ CHO THUÊ XE");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Khoảng cách
        centerPanel.add(Box.createVerticalStrut(200));
        centerPanel.add(welcomeLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        
        JLabel subLabel = new JLabel("Hệ thống quản lý hiện đại, tiện lợi và dễ sử dụng");
        subLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(subLabel);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        JLabel welcomeLabel = new JLabel("Chào mừng quay trở lại, Admin!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(50, 50, 50));
        
        JLabel dateLabel = new JLabel("Hôm nay: " + date);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(100, 100, 100));
        
        headerPanel.add(welcomeLabel, BorderLayout.NORTH);
        headerPanel.add(dateLabel, BorderLayout.SOUTH);
        
        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        statsPanel.setOpaque(false);
        
        // Thêm 4 thẻ thống kê
        statsPanel.add(createStatCard("Tổng số xe", "24", new Color(46, 149, 233)));
        statsPanel.add(createStatCard("Xe đang thuê", "8", new Color(255, 107, 107)));
        statsPanel.add(createStatCard("Khách hàng", "36", new Color(76, 175, 80)));
        statsPanel.add(createStatCard("Doanh thu tháng", "68,500,000 VNĐ", new Color(156, 39, 176)));
        
        // Recent Contracts Panel
        JPanel recentPanel = new JPanel(new BorderLayout(0, 15));
        recentPanel.setOpaque(false);
        
        JLabel recentLabel = new JLabel("Hợp đồng gần đây");
        recentLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        String[] columnNames = {"Mã HĐ", "Khách hàng", "Xe", "Ngày thuê", "Ngày trả", "Trạng thái"};
        Object[][] data = {
            {"HD001", "Nguyễn Văn A", "Toyota Vios", "01/05/2023", "05/05/2023", "Đã hoàn thành"},
            {"HD002", "Trần Thị B", "Honda City", "03/05/2023", "10/05/2023", "Đang thuê"},
            {"HD003", "Lê Văn C", "Ford Ranger", "05/05/2023", "12/05/2023", "Đang thuê"}
        };
        
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setShowVerticalLines(false);
        table.getTableHeader().setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        recentPanel.add(recentLabel, BorderLayout.NORTH);
        recentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Main Content Layout
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setOpaque(false);
        contentPanel.add(statsPanel, BorderLayout.NORTH);
        contentPanel.add(recentPanel, BorderLayout.CENTER);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Icon panel with colored background
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setPreferredSize(new Dimension(60, 60));
        iconPanel.setBackground(color);
        
        JLabel iconLabel = new JLabel("?", JLabel.CENTER);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 24));
        iconLabel.setForeground(Color.WHITE);
        
        iconPanel.add(iconLabel, BorderLayout.CENTER);
        
        // Information panel with title and value
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(valueLabel);
        
        card.add(iconPanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    // Tạo một panel đơn giản với tiêu đề để hiển thị tạm thời
    private JPanel createSimplePanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        panel.add(lblTitle, BorderLayout.NORTH);
        
        JLabel lblMessage = new JLabel("Chức năng này đang được phát triển...");
        lblMessage.setFont(new Font("Arial", Font.ITALIC, 18));
        lblMessage.setHorizontalAlignment(JLabel.CENTER);
        panel.add(lblMessage, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn đăng xuất?", 
                "Xác nhận đăng xuất", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            try {
                // Giả sử LoginForm là form đăng nhập
                ui.auth.LoginForm loginForm = new ui.auth.LoginForm();
                loginForm.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi đăng xuất: " + e.getMessage());
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
    
    // Main method cho testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set Look and Feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Test với tài khoản null
                AdminDashboard adminDashboard = new AdminDashboard(null);
                adminDashboard.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}