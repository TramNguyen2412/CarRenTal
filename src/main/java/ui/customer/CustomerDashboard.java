package ui.customer;

import model.TaiKhoan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class CustomerDashboard extends JFrame {
    private TaiKhoan taiKhoan;
    private JPanel mainPanel, menuPanel, contentPanel;
    private CardLayout cardLayout;

    // Màu sắc cho giao diện
    private final Color MENU_COLOR = new Color(252, 227, 138); // Màu vàng nhẹ cho menu
    private final Color MENU_HOVER_COLOR = new Color(255, 217, 102); // Màu vàng đậm khi hover
    private final Color MENU_TEXT_COLOR = new Color(51, 51, 51); // Màu chữ đen nhạt

    public CustomerDashboard(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
        setTitle("Hệ Thống Quản Lý Thuê Xe - CarRental");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        
        setupMenuPanel();
        setupContentPanel();
        
        add(mainPanel);
    }

    private void setupMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(250, 0));
        menuPanel.setBackground(MENU_COLOR); // Đổi màu nền sang vàng pastel
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        // Logo và thông tin người dùng
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(new Color(13, 25, 38)); // Màu tối cho logo
        logoPanel.setPreferredSize(new Dimension(250, 80));
        logoPanel.setMaximumSize(new Dimension(250, 80));
        logoPanel.setMinimumSize(new Dimension(250, 80));

        JLabel lblLogo = new JLabel("CarRental");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 28)); // Tăng cỡ chữ
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Padding bên trái
        
        logoPanel.add(lblLogo, BorderLayout.CENTER);
        menuPanel.add(logoPanel);

        // Panel thông tin người dùng
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(MENU_COLOR);
        userPanel.setPreferredSize(new Dimension(250, 60));
        userPanel.setMaximumSize(new Dimension(250, 60));

        // Xử lý khi taiKhoan có thể null trong giai đoạn phát triển
        String username = "Khách hàng";
        if (taiKhoan != null && taiKhoan.getTenDangNhap() != null) {
            username = taiKhoan.getTenDangNhap();
        }

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding các cạnh

        JLabel nameLabel = new JLabel(username);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(MENU_TEXT_COLOR);

        JLabel roleLabel = new JLabel("Khách hàng");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(100, 100, 100));

        userInfoPanel.add(nameLabel);
        userInfoPanel.add(Box.createVerticalStrut(5)); // Khoảng cách giữa tên và role
        userInfoPanel.add(roleLabel);

        userPanel.add(userInfoPanel, BorderLayout.CENTER);
        menuPanel.add(userPanel);

        // Thêm đường phân cách
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        separator.setBackground(new Color(200, 200, 200));
        separator.setPreferredSize(new Dimension(250, 1));
        menuPanel.add(separator);

        // Thêm khoảng cách
        menuPanel.add(Box.createVerticalStrut(10));

        // Các nút menu mới theo yêu cầu
        addMenuItem("Trang chủ", e -> showPanel("trangChu"));
        addMenuItem("Thuê xe", e -> showPanel("thueXe"));
        addMenuItem("Giỏ xe", e -> showPanel("gioXe"));
        addMenuItem("Đánh giá dịch vụ", e -> showPanel("danhGia"));

        // Nút đăng xuất ở dưới cùng
        menuPanel.add(Box.createVerticalGlue());

        // Thêm đường phân cách
        JSeparator separatorBottom = new JSeparator();
        separatorBottom.setForeground(new Color(200, 200, 200));
        separatorBottom.setBackground(new Color(200, 200, 200));
        separatorBottom.setPreferredSize(new Dimension(250, 1));
        menuPanel.add(separatorBottom);

        addMenuItem("Đăng xuất", e -> logout());

        mainPanel.add(menuPanel, BorderLayout.WEST);
    }

    private void addMenuItem(String text, ActionListener action) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(MENU_COLOR);
        itemPanel.setPreferredSize(new Dimension(250, 48)); // Chiều cao cố định cho item
        itemPanel.setMaximumSize(new Dimension(250, 48));
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", Font.BOLD, 15));
        textLabel.setForeground(MENU_TEXT_COLOR);
        textLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Padding bên trái
        
        itemPanel.add(textLabel, BorderLayout.CENTER);
        
        // Xử lý hover effect
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
                    action.actionPerformed(new java.awt.event.ActionEvent(
                        itemPanel, java.awt.event.ActionEvent.ACTION_PERFORMED, text
                    ));
                }
            }
        });
        
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuPanel.add(itemPanel);
        menuPanel.add(Box.createVerticalStrut(8)); // Khoảng cách giữa các item
    }

    private void setupContentPanel() {
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Tạo các panel cho các chức năng mới
        contentPanel.add(createDashboardPanel(), "trangChu");
        contentPanel.add(createPlaceholderPanel("Thuê xe", "Chức năng đang được phát triển"), "thueXe");
        contentPanel.add(createPlaceholderPanel("Giỏ xe", "Chức năng đang được phát triển"), "gioXe");
        contentPanel.add(createPlaceholderPanel("Đánh giá dịch vụ", "Chức năng đang được phát triển"), "danhGia");

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Hiển thị panel mặc định
        cardLayout.show(contentPanel, "trangChu");
    }
    
    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }

    // Tạo panel placeholder cho các chức năng đang phát triển
    private JPanel createPlaceholderPanel(String title, String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        messageLabel.setForeground(new Color(120, 120, 120));
        
        centerPanel.add(messageLabel);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Header với ngày hiện tại
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Trang chủ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        LocalDate now = LocalDate.now();
        String formattedDate = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        JLabel dateLabel = new JLabel(formattedDate);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);
        
        // Content area
        JPanel contentArea = new JPanel();
        contentArea.setLayout(new BoxLayout(contentArea, BoxLayout.Y_AXIS));
        contentArea.setBackground(Color.WHITE);
        contentArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Chào mừng đến với hệ thống thuê xe CarRental!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel instructionLabel = new JLabel(
            "<html>Hệ thống cho phép bạn thực hiện các thao tác:<br>" +
            "- Thuê xe và quản lý đơn hàng<br>" +
            "- Quản lý giỏ xe của bạn<br>" +
            "- Đánh giá chất lượng dịch vụ<br>" +
            "- Liên hệ hỗ trợ khách hàng</html>"
        );
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentArea.add(welcomeLabel);
        contentArea.add(Box.createVerticalStrut(20));
        contentArea.add(instructionLabel);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentArea, BorderLayout.CENTER);
        
        return panel;
    }

    private void logout() {
        // Xử lý đăng xuất
        int option = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION
        );
        
        if (option == JOptionPane.YES_OPTION) {
            dispose(); // Đóng cửa sổ hiện tại
            
            // Hiển thị màn hình đăng nhập
             ui.auth.LoginForm loginForm = new ui.auth.LoginForm();
             loginForm.setVisible(true);
//            
//            // Hoặc hiển thị thông báo tạm thời
//            JOptionPane.showMessageDialog(null, "Đã đăng xuất thành công!");
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            CustomerDashboard dashboard = new CustomerDashboard(null);
            dashboard.setVisible(true);
        });
    }
}