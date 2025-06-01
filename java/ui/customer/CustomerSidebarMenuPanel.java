package ui.customer;

import model.TaiKhoan;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URL;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import ui.customer.RoundedPanel;

public class CustomerSidebarMenuPanel extends JPanel {
    // Màu sắc của menu
    private Color menuColor = Color.decode("#56CCF2"); 
     private Color menuHoverColor = new Color(255, 255, 255, 40);
    private Color menuTextColor = new Color(255, 255, 255);
    private TaiKhoan taiKhoan;  // Thông tin người dùng
    private int menuItemHeight = 36;  // Chiều cao của mỗi menu item
    private int iconSize = 20;  // Kích thước icon
    
    // Lưu trữ các panel menu item để quản lý dễ dàng
    private Map<String, JPanel> menuItemPanels = new HashMap<>();
    private List<String> menuItemOrder = new ArrayList<>();
    
    // Interface để xử lý click menu
    public interface MenuClickListener {
        void onMenuItemClicked(String panelName);
    }
    
    private MenuClickListener clickListener;
    
    // Constructor
    public CustomerSidebarMenuPanel(TaiKhoan taiKhoan, MenuClickListener listener) {
        this.taiKhoan = taiKhoan;
        this.clickListener = listener;
        setOpaque(false); // Quan trọng để thấy gradient
        initComponents();
    }
    
    private void initComponents() {
        // Thiết lập cơ bản cho panel
        setPreferredSize(new Dimension(250, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Thêm các thành phần
        setupLogo();
        setupUserInfo();
        add(Box.createVerticalStrut(5));
        addCustomerMenuItems();
        selectMenuItem("trangChu");
    }
    
    // Override để vẽ gradient cho toàn bộ sidebar
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Sử dụng gradient pastel cho khách hàng
         Color startColor = Color.decode("#E55D87"); 
        Color endColor = Color.decode("#5FC3E4");  
        
        // Tạo gradient từ trên xuống dưới
        GradientPaint gradient = new GradientPaint(
            0, 0, startColor,
            0, getHeight(), endColor
        );
        
        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        g2.dispose();
    }
    
    // Phương thức tạo logo
    private void setupLogo() {
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(250, 80));
        logoPanel.setMaximumSize(new Dimension(250, 80));
        logoPanel.setMinimumSize(new Dimension(250, 80));
        
        // Panel chứa logo và text
        JPanel logoContentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoContentPanel.setOpaque(false);
        
        // Thêm logo
        try {
            URL resourceUrl = getClass().getResource("/img/Carrental.svg");
            if (resourceUrl != null) {
                // Sử dụng FlatSVGIcon để hiển thị SVG
                FlatSVGIcon svgIcon = new FlatSVGIcon(resourceUrl);
                svgIcon = svgIcon.derive(40, 40);

                JLabel iconLabel = new JLabel(svgIcon);
                logoContentPanel.add(iconLabel);
            } else {
                System.out.println("Không tìm thấy file logo: /img/Carrental.svg");
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tải logo: " + e.getMessage());
            e.printStackTrace();
        }

        // Thêm text logo
        JLabel lblLogo = new JLabel("CarRental");
        lblLogo.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 40));     
        lblLogo.setForeground(Color.WHITE); 
        logoContentPanel.add(lblLogo);
        
        logoPanel.add(logoContentPanel, BorderLayout.CENTER);
        add(logoPanel);
    }
    
    // Phương thức hiển thị thông tin người dùng
    private void setupUserInfo() {
        // Panel thông tin người dùng
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setOpaque(false); // QUAN TRỌNG: Đặt transparent để thấy gradient
        userPanel.setPreferredSize(new Dimension(250, 60));
        userPanel.setMaximumSize(new Dimension(250, 60));
        
        // Xử lý khi taiKhoan có thể null
        String username = "Khách hàng";
        if (taiKhoan != null && taiKhoan.getTenDangNhap() != null) {
            username = taiKhoan.getTenDangNhap();
        }
        
        // Tạo panel chứa avatar và thông tin
        JPanel avatarAndInfoPanel = new JPanel(new BorderLayout(10, 0));
        avatarAndInfoPanel.setOpaque(false);
        avatarAndInfoPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // Tạo avatar
        JLabel avatarLabel = null;
        try {
            URL resourceUrl = getClass().getResource("/img/user.svg");
            if (resourceUrl != null) {
                FlatSVGIcon svgIcon = new FlatSVGIcon(resourceUrl);
                svgIcon = svgIcon.derive(40, 40);
                avatarLabel = new JLabel(svgIcon);
            } else {
                avatarLabel = new JLabel("U");
                avatarLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 20));
                avatarLabel.setHorizontalAlignment(JLabel.CENTER);
                avatarLabel.setOpaque(true);
                avatarLabel.setBackground(new Color(25, 118, 210));
                avatarLabel.setForeground(Color.WHITE);
                avatarLabel.setPreferredSize(new Dimension(40, 40));
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tải avatar: " + e.getMessage());
        }
        
        // Panel chứa thông tin người dùng
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);
        
        // Tên người dùng
        JLabel nameLabel = new JLabel(username);
        nameLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Vai trò
        JLabel roleLabel = new JLabel("Khách hàng");
        roleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12));
        roleLabel.setForeground(new Color(220, 220, 220));
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Thêm vào panel thông tin
        userInfoPanel.add(nameLabel);
        userInfoPanel.add(Box.createVerticalStrut(3)); // Khoảng cách nhỏ
        userInfoPanel.add(roleLabel);
        
        // Thêm avatar và thông tin vào panel
        avatarAndInfoPanel.add(avatarLabel, BorderLayout.WEST);
        avatarAndInfoPanel.add(userInfoPanel, BorderLayout.CENTER);
        
        // Thêm vào panel người dùng
        userPanel.add(avatarAndInfoPanel, BorderLayout.CENTER);
        
        add(userPanel);
        
        // Thêm đường phân cách
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(255, 255, 255, 60));
        separator.setBackground(new Color(255, 255, 255, 60));
        add(separator);
        
        // Thêm khoảng cách
        add(Box.createVerticalStrut(10));
    }
    
    // Phương thức thêm các menu item cho khách hàng
    private void addCustomerMenuItems() {
        addMenuItem("Trang chủ", "home3.svg", "trangChu");
        addMenuItem("Thông tin cá nhân", "user.svg", "thongTinCaNhan");
        addMenuItem("Xem & đặt thuê xe", "Car.svg", "xemDatXe");
        addMenuItem("Giỏ xe", "cart.svg", "gioXe");
        addMenuItem("Đánh giá hợp đồng", "contract.svg", "danhGiaHopDong");
        
        // Nút đăng xuất ở dưới cùng
        add(Box.createVerticalGlue());
        
        // Thêm đường phân cách
        JSeparator separatorBottom = new JSeparator();
        separatorBottom.setForeground(new Color(255, 255, 255, 60));
        separatorBottom.setBackground(new Color(255, 255, 255, 60));
        add(separatorBottom);
        
        addMenuItem("Đăng Xuất", "logout.svg", "logout");
    }
    
    // Phương thức thêm menu item
    public void addMenuItem(String text, String iconPath, String panelName) {
        // Panel chứa menu item
        RoundedPanel roundedPanel = new RoundedPanel();
        roundedPanel.setOpaque(false);

        // Giữ nguyên chiều rộng theo yêu cầu
        roundedPanel.setPreferredSize(new Dimension(180, menuItemHeight));
        roundedPanel.setMaximumSize(new Dimension(180, menuItemHeight));

        // Panel chứa icon và text
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0)); // vgap=0 để loại bỏ khoảng cách dọc
        contentPanel.setOpaque(false);

        // Thêm icon nếu có
        if (iconPath != null && !iconPath.isEmpty()) {
            try {
                URL resourceUrl = getClass().getResource("/img/" + iconPath);
                if (resourceUrl != null) {
                    FlatSVGIcon svgIcon = new FlatSVGIcon(resourceUrl);
                    svgIcon = svgIcon.derive(24, 24);
                    JLabel iconLabel = new JLabel(svgIcon);
                    contentPanel.add(iconLabel);
                } else {
                    System.out.println("Không tìm thấy icon: " + iconPath);
                }
            } catch (Exception e) {
                System.out.println("Lỗi khi tải icon: " + e.getMessage());
            }
        }

        // Thêm text - giữ nguyên kích thước như AdminDashboard
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 17)); // Giữ nguyên kích thước font
        textLabel.setForeground(menuTextColor);
        contentPanel.add(textLabel);

        // Thêm content panel vào giữa của rounded panel để căn giữa theo chiều dọc
        roundedPanel.add(contentPanel, BorderLayout.CENTER);

        roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Xử lý sự kiện hover và click
        roundedPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                if (!roundedPanel.isSelected) {
                    roundedPanel.setBackground(menuHoverColor);
                    roundedPanel.repaint();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent evt) {
                if (!roundedPanel.isSelected) {
                    roundedPanel.setBackground(new Color(0, 0, 0, 0)); // Transparent
                    roundedPanel.repaint();
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent evt) {
                // Bỏ chọn tất cả các panel
                for (String key : menuItemPanels.keySet()) {
                    RoundedPanel panel = (RoundedPanel) menuItemPanels.get(key);
                    panel.setSelected(false);
                    panel.setBackground(new Color(0, 0, 0, 0)); // Transparent
                    panel.repaint();
                }
                
                // Chọn panel này
                roundedPanel.setSelected(true);
                roundedPanel.setBackground(new Color(255, 255, 255, 60));
                roundedPanel.repaint();
                
                if (clickListener != null) {
                    clickListener.onMenuItemClicked(panelName);
                }
            }
        });
        
        // Lưu panel để quản lý
        menuItemPanels.put(panelName, roundedPanel);
        menuItemOrder.add(panelName);
        
        // Thêm panel vào container chính với padding
        // Thêm vào containerPanel với padding nhỏ hơn
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBorder(BorderFactory.createEmptyBorder(1, 10, 1, 10)); // Giảm padding dọc từ 2px xuống 1px
        containerPanel.setOpaque(false);
        containerPanel.add(roundedPanel, BorderLayout.CENTER);

        add(containerPanel);
    }
    
    // Phương thức chọn menu item
    public void selectMenuItem(String panelName) {
        // Bỏ chọn tất cả các panel
        for (String key : menuItemPanels.keySet()) {
            RoundedPanel panel = (RoundedPanel) menuItemPanels.get(key);
            panel.setSelected(false);
            panel.setBackground(new Color(0, 0, 0, 0)); // Transparent
            panel.repaint();
        }
        
        // Chọn panel mới
        if (menuItemPanels.containsKey(panelName)) {
            RoundedPanel panel = (RoundedPanel) menuItemPanels.get(panelName);
            panel.setSelected(true);
            panel.setBackground(menuHoverColor);
            panel.repaint();
        }
    }
}