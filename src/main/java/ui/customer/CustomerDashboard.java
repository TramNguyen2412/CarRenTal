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
    private DanhGiaPanel danhGiaPanel;
     
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
        gioXePanel.updateAccount(taiKhoan, khachHang);
        danhGiaPanel.setAccount(taiKhoan, khachHang);
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
        
       danhGiaPanel = new DanhGiaPanel(taiKhoan, khachHang);
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
            } else if ("danhGiaHopDong".equals(panelName)) {
                // Nếu cần thì làm mới dữ liệu đánh giá
                danhGiaPanel.loadData();
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