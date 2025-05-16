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

        // Các nút menu không có icon
        addMenuItem("Trang chủ", e -> showPanel("trangChu"));
        addMenuItem("Thông tin cá nhân", e -> showPanel("thongTinCaNhan"));
        addMenuItem("Đặt xe", e -> showPanel("datXe"));
        addMenuItem("Xem xe đã thuê", e -> showPanel("xeThue"));
        addMenuItem("Hợp đồng", e -> showPanel("hopDong"));
        addMenuItem("Thanh toán", e -> showPanel("thanhToan"));

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

        // Tạo các panel cho từng chức năng
        contentPanel.add(createDashboardPanel(), "trangChu");
        contentPanel.add(createProfilePanel(), "thongTinCaNhan");
        contentPanel.add(createBookingPanel(), "datXe");
        contentPanel.add(createRentedCarsPanel(), "xeThue");
        contentPanel.add(createContractsPanel(), "hopDong");
        contentPanel.add(createPaymentPanel(), "thanhToan");

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Hiển thị panel mặc định
        cardLayout.show(contentPanel, "trangChu");
    }
    
    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
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
        JLabel welcomeLabel = new JLabel("Chào mừng đến với hệ thống quản lý thuê xe!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel instructionLabel = new JLabel(
            "<html>Hệ thống cho phép bạn thực hiện các thao tác:<br>" +
            "- Xem và cập nhật thông tin cá nhân<br>" +
            "- Đặt xe và quản lý lịch đặt<br>" +
            "- Xem lại các xe đã thuê<br>" +
            "- Quản lý hợp đồng<br>" +
            "- Thực hiện thanh toán</html>"
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

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Thông tin cá nhân");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Placeholder cho nội dung
        JLabel placeholderLabel = new JLabel("Nội dung thông tin cá nhân sẽ được hiển thị tại đây");
        placeholderLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(placeholderLabel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Đặt xe");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Placeholder cho nội dung
        JLabel placeholderLabel = new JLabel("Chức năng đặt xe sẽ được hiển thị tại đây");
        placeholderLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(placeholderLabel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createRentedCarsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Xe đã thuê");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Placeholder cho nội dung
        JLabel placeholderLabel = new JLabel("Danh sách xe đã thuê sẽ được hiển thị tại đây");
        placeholderLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(placeholderLabel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createContractsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Hợp đồng");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Placeholder cho nội dung
        JLabel placeholderLabel = new JLabel("Danh sách hợp đồng sẽ được hiển thị tại đây");
        placeholderLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(placeholderLabel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Thanh toán");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Placeholder cho nội dung
        JLabel placeholderLabel = new JLabel("Chức năng thanh toán sẽ được hiển thị tại đây");
        placeholderLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(placeholderLabel, BorderLayout.CENTER);
        
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
            // LoginForm loginForm = new LoginForm();
            // loginForm.setVisible(true);
            
            // Hoặc hiển thị thông báo tạm thời
            JOptionPane.showMessageDialog(null, "Đã đăng xuất thành công!");
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