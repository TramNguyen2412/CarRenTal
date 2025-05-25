package ui.admin;
import java.net.URL;
import model.TaiKhoan;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import ui.admin.QLXe.XePanel;
import ui.admin.QLHD.HopDongPanel;
import ui.admin.ThongKePanel;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;


public class AdminDashboard extends JFrame implements SidebarMenuPanel.MenuClickListener {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private TaiKhoan taiKhoan;
    
    // Các panel quản lý
    private JPanel trangChuPanel;
    private JPanel khachHangPanel;
    private JPanel nhanVienPanel;
    private XePanel xePanel;
    private JPanel dichVuBDPanel;
    private HopDongPanel hopDongPanel;
    private JPanel baoDuongPanel;
    private JPanel congNoPanel;
    private JPanel giaoNhanXePanel;
    private ThongKePanel tkPanel;
    
    public AdminDashboard(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Mở full màn hình
    }
    
    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Thuê Xe - CarRental");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        
        // Panel chính chứa tất cả
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Bỏ border
        
        // Tạo menu panel - đã tách thành class riêng
        SidebarMenuPanel menuPanel = new SidebarMenuPanel(taiKhoan, this);
        
        // Thiết lập content
        setupContentPanel();
        
        // Thêm vào main panel
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Thêm main panel vào frame
        getContentPane().add(mainPanel);
    }
    
    private void setupContentPanel() {
        // Thiết lập màu nền
        this.getContentPane().setBackground(new Color(240, 248, 255)); // Màu xanh Alice Blue
        
        contentPanel = new JPanel();
        contentPanel.setBackground(new Color(245, 245, 245));
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        
        // Tạo các panel chức năng
        trangChuPanel = createWelcomePanel();
        khachHangPanel = createSimplePanel("Quản Lý Khách Hàng");
        nhanVienPanel = createSimplePanel("Quản Lý Nhân Viên");
        xePanel = new XePanel(); // Panel riêng cho Xe
        dichVuBDPanel = createSimplePanel("Quản Lý Dịch Vụ Bảo Dưỡng");
        hopDongPanel = new HopDongPanel(); // Panel riêng cho Hợp đồng
        baoDuongPanel = createSimplePanel("Quản Lý Bảo Dưỡng");
        congNoPanel = createSimplePanel("Quản Lý Công Nợ");
        giaoNhanXePanel = createSimplePanel("Quản Lý Giao Nhận Xe");
        tkPanel = new ThongKePanel();
        
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
        contentPanel.add(tkPanel, "baoCao");
        
        // Hiển thị panel mặc định
        cardLayout.show(contentPanel, "trangChu");
    }
    
    // Xử lý khi click vào menu item
    @Override
    public void onMenuItemClicked(String panelName) {
        if ("logout".equals(panelName)) {
            logout();
        } else {
            cardLayout.show(contentPanel, panelName);
        }
    }
    
    // Phương thức đăng xuất
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
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
    
    // Tạo panel chào mừng đơn giản
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG QUẢN LÝ CHO THUÊ XE");
        welcomeLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Khoảng cách
        centerPanel.add(Box.createVerticalStrut(200));
        centerPanel.add(welcomeLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        
        JLabel subLabel = new JLabel("Hệ thống quản lý hiện đại, tiện lợi và dễ sử dụng");
        subLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 18));
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(subLabel);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }
    
    // Tạo một panel đơn giản với tiêu đề để hiển thị tạm thời
    private JPanel createSimplePanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        panel.add(lblTitle, BorderLayout.NORTH);
        
        JLabel lblMessage = new JLabel("Chức năng này đang được phát triển...");
        lblMessage.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 18));
        lblMessage.setHorizontalAlignment(JLabel.CENTER);
        panel.add(lblMessage, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Tạo màn hình dashboard thống kê
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        JLabel welcomeLabel = new JLabel("Chào mừng quay trở lại, Admin!");
        welcomeLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(50, 50, 50));
        
        JLabel dateLabel = new JLabel("Hôm nay: " + date);
        dateLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
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
        recentLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
        
        String[] columnNames = {"Mã HĐ", "Khách hàng", "Xe", "Ngày thuê", "Ngày trả", "Trạng thái"};
        Object[][] data = {
            {"HD001", "Nguyễn Văn A", "Toyota Vios", "01/05/2023", "05/05/2023", "Đã hoàn thành"},
            {"HD002", "Trần Thị B", "Honda City", "03/05/2023", "10/05/2023", "Đang thuê"},
            {"HD003", "Lê Văn C", "Ford Ranger", "05/05/2023", "12/05/2023", "Đang thuê"}
        };
        
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(30);
        table.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
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
        iconLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        iconLabel.setForeground(Color.WHITE);
        
        iconPanel.add(iconLabel, BorderLayout.CENTER);
        
        // Information panel with title and value
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(valueLabel);
        
        card.add(iconPanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        
        return card;
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