
package ui.admin;

import java.net.URL;
import model.TaiKhoan;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import com.formdev.flatlaf.extras.FlatSVGIcon;

public class SidebarMenuPanel extends JPanel {
    // Màu sắc của menu
    private Color menuColor = Color.decode("#56CCF2"); 
   // private Color menuHoverColor = new Color(255, 224, 130);  // Màu hover
  //  private Color menuTextColor = new Color(50, 50, 50);  // Màu chữ tối
    private Color menuHoverColor = new Color(255, 255, 255, 40);
    private Color menuTextColor = new Color(255, 255, 255);
    private TaiKhoan taiKhoan;  // Thông tin người dùng
    private int menuItemHeight = 48;  // Chiều cao của mỗi menu item
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
    public SidebarMenuPanel(TaiKhoan taiKhoan, MenuClickListener listener) {
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
        addDefaultMenuItems();
        selectMenuItem("trangChu");
    }
    
    // Override để vẽ gradient cho toàn bộ sidebar
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Sử dụng Color.decode() để xác định màu giống mẫu
        // Các màu này dựa theo màu trong hình mẫu
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
        
        // VỀ CƠ BẢN KHÔNG GỌI SUPER ĐỂ KHÔNG VẼ ĐÈ LÊN GRADIENT
    }
    
    // Phương thức tạo logo
    private void setupLogo() {
        JPanel logoPanel = new JPanel(new BorderLayout());
       // logoPanel.setBackground(new Color(13, 25, 38)); // Màu tối cho logo
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
                // Đặt kích thước cho icon - THÊM derive GIỐNG như các icon khác
                svgIcon = svgIcon.derive(40, 40); // Thay vì chỉ gọi derive() mà không gán lại

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
        //lblLogo.setForeground(new Color(50, 50, 50));
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
        String username = "Admin";
        if (taiKhoan != null && taiKhoan.getTenDangNhap() != null) {
            username = taiKhoan.getTenDangNhap();
        }
        
        // Tạo panel chứa avatar và thông tin
        JPanel avatarAndInfoPanel = new JPanel(new BorderLayout(10, 0));
        avatarAndInfoPanel.setOpaque(false); // QUAN TRỌNG: Đặt transparent
        avatarAndInfoPanel.setBorder(new javax.swing.border.EmptyBorder(10, 20, 10, 20));
        
        // Tạo avatar
        JLabel avatarLabel = null;
        try {
        URL resourceUrl = getClass().getResource("/img/admin.svg");
            if (resourceUrl != null) {
                FlatSVGIcon svgIcon = new FlatSVGIcon(resourceUrl);
                // Thêm gán lại giá trị sau khi derive
                svgIcon = svgIcon.derive(40, 40); // Thay vì chỉ gọi derive()
                avatarLabel = new JLabel(svgIcon);
            } else {
                avatarLabel = new JLabel("A");
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
        userInfoPanel.setOpaque(false); // QUAN TRỌNG: Đặt transparent
        
        // Tên người dùng
        JLabel nameLabel = new JLabel(username);
        nameLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
      //  nameLabel.setForeground(menuTextColor);
          nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Vai trò
        JLabel roleLabel = new JLabel("Quản trị viên");
        roleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12));
      //  roleLabel.setForeground(new Color(100, 100, 100));
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
        //separator.setForeground(new Color(200, 200, 200));
        //separator.setBackground(new Color(200, 200, 200));
        separator.setForeground(new Color(255, 255, 255, 60)); // Đổi từ xám sang trắng mờ
        separator.setBackground(new Color(255, 255, 255, 60));
        add(separator);
        
        // Thêm khoảng cách
        add(Box.createVerticalStrut(10));
    }
    
    // Phương thức thêm các menu item mặc định
    private void addDefaultMenuItems() {
        addMenuItem("Trang chủ", "home3.svg", "trangChu");
        addMenuItem("Khách Hàng", "customer.svg", "khachHang");
        addMenuItem("Nhân Viên", "staff.svg", "nhanVien");
        addMenuItem("Quản Lý Xe", "Car.svg", "xe");
        addMenuItem("Dịch Vụ Bảo Dưỡng", "carservices.svg", "dichVuBD");
        addMenuItem("Hợp Đồng", "contract.svg", "hopDong");
        addMenuItem("Bảo Dưỡng", "maintenance.svg", "baoDuong");
        addMenuItem("Công Nợ", "debt.svg", "congNo");
        addMenuItem("Giao Nhận Xe", "giaonhanxe.svg", "giaoNhanXe");
        addMenuItem("Báo Cáo Thống Kê", "thongke.svg", "baoCao");
        
        // Nút đăng xuất ở dưới cùng
        add(Box.createVerticalGlue());
        
        // Thêm đường phân cách
        JSeparator separatorBottom = new JSeparator();
        //separatorBottom.setForeground(new Color(200, 200, 200));
        //separatorBottom.setBackground(new Color(200, 200, 200));
        separatorBottom.setForeground(new Color(255, 255, 255, 60)); // Đổi từ xám sang trắng mờ
        separatorBottom.setBackground(new Color(255, 255, 255, 60));
        add(separatorBottom);
        
        addMenuItem("Đăng Xuất", "logout.svg", "logout");
    }
    
    // Phương thức thêm menu item
    public void addMenuItem(String text, String iconPath, String panelName) {
        // Panel chứa menu item
        RoundedPanelAdmin roundedPanel = new RoundedPanelAdmin();
        roundedPanel.setOpaque(false); // Quan trọng: để thấy gradient từ sidebar
        roundedPanel.setPreferredSize(new Dimension(230, menuItemHeight));
        roundedPanel.setMaximumSize(new Dimension(230, menuItemHeight));
        
        // Panel chứa nội dung (icon + text)
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        contentPanel.setOpaque(false); // Đặt transparent
        
        // Thêm icon nếu có
//        if (iconPath != null && !iconPath.isEmpty()) {
//            try {
//                URL resourceUrl = getClass().getResource("/img/" + iconPath);
////                if (resourceUrl != null) {
////                    ImageIcon icon = new ImageIcon(resourceUrl);
////                    Image img = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
////                    JLabel iconLabel = new JLabel(new ImageIcon(img));
////                    contentPanel.add(iconLabel);
////                }
//                if (resourceUrl != null) {
//                    ImageIcon icon = createHighQualityIcon(resourceUrl, iconSize, iconSize);
//                    if (icon != null) {
//                        JLabel iconLabel = new JLabel(icon);
//                        contentPanel.add(iconLabel);
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println("Không thể tải icon: " + iconPath);
//            }
//        }
//        if (iconPath != null && !iconPath.isEmpty()) {
//            FlatSVGIcon svgIcon = new FlatSVGIcon("img/" + iconPath);
//            // Nếu muốn đặt kích thước cụ thể cho icon:
//            svgIcon.derive(iconSize, iconSize); // hoặc dùng setScale() tùy trường hợp
//
//            JLabel iconLabel = new JLabel(svgIcon);
//            contentPanel.add(iconLabel);
//        }
        if (iconPath != null && !iconPath.isEmpty()) {
         try {
             // Tạo FlatSVGIcon với kích thước cố định
             URL resourceUrl = getClass().getResource("/img/" + iconPath);

             if (resourceUrl != null) {
                 // *** QUAN TRỌNG: Set kích thước nhỏ khi tạo icon ***
                 FlatSVGIcon svgIcon = new FlatSVGIcon(resourceUrl);

                 // Đặt kích thước nhỏ cố định (24x24)
                 svgIcon = svgIcon.derive(30, 30); 

                 JLabel iconLabel = new JLabel(svgIcon);
                 contentPanel.add(iconLabel);
             } else {
                 System.out.println("Không tìm thấy icon: " + iconPath);
             }
         } catch (Exception e) {
             System.out.println("Lỗi khi tải icon: " + e.getMessage());
         }
     }

        
        // Thêm text
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 17));
        textLabel.setForeground(menuTextColor);
        contentPanel.add(textLabel);
        
        roundedPanel.add(contentPanel);
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
                    RoundedPanelAdmin panel = (RoundedPanelAdmin) menuItemPanels.get(key);
                    panel.setSelected(false);
                    panel.setBackground(new Color(0, 0, 0, 0)); // Transparent
                    panel.repaint();
                }
                
                // Chọn panel này
                roundedPanel.setSelected(true);
               // roundedPanel.setBackground(menuHoverColor);
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
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        containerPanel.setOpaque(false);
        containerPanel.add(roundedPanel, BorderLayout.CENTER);
        
        add(containerPanel);
    }
    
    // Phương thức chọn menu item
    public void selectMenuItem(String panelName) {
        // Bỏ chọn tất cả các panel
        for (String key : menuItemPanels.keySet()) {
            RoundedPanelAdmin panel = (RoundedPanelAdmin) menuItemPanels.get(key);
            panel.setSelected(false);
            panel.setBackground(new Color(0, 0, 0, 0)); // Transparent
            panel.repaint();
        }
        
        // Chọn panel mới
        if (menuItemPanels.containsKey(panelName)) {
            RoundedPanelAdmin panel = (RoundedPanelAdmin) menuItemPanels.get(panelName);
            panel.setSelected(true);
            panel.setBackground(menuHoverColor);
            panel.repaint();
        }
    }
    
    // Xóa tất cả menu items (trừ phần logo và user info)
    public void removeAllMenuItems() {
        for (String panelName : menuItemOrder) {
            JPanel panel = menuItemPanels.get(panelName);
            if (panel != null) {
                remove(panel);
            }
        }
        
        menuItemPanels.clear();
        menuItemOrder.clear();
        
        revalidate();
        repaint();
    }
    
    // Ẩn một menu item cụ thể
    public void hideMenuItem(String panelName) {
        JPanel panel = menuItemPanels.get(panelName);
        if (panel != null) {
            panel.setVisible(false);
        }
    }
    
    // Chỉ hiển thị các menu item được chỉ định
    public void showOnlyMenuItems(String... panelNames) {
        for (String name : menuItemOrder) {
            JPanel panel = menuItemPanels.get(name);
            if (panel != null) {
                panel.setVisible(false);
                
                // Kiểm tra xem có trong danh sách hiển thị không
                for (String showName : panelNames) {
                    if (name.equals(showName)) {
                        panel.setVisible(true);
                        break;
                    }
                }
            }
        }
    }
    
    // Các setter để tùy chỉnh
    public void setMenuColor(Color color) {
        this.menuColor = color;
        
        // Cập nhật màu cho tất cả menu items (nếu không được chọn)
        for (String key : menuItemPanels.keySet()) {
            RoundedPanelAdmin panel = (RoundedPanelAdmin) menuItemPanels.get(key);
            if (!panel.isSelected) {
                panel.setBackground(new Color(0, 0, 0, 0)); // Transparent để thấy gradient
                panel.repaint();
            }
        }
        
        repaint(); // Vẽ lại sidebar với gradient
    }
    
    public void setMenuHoverColor(Color color) {
        this.menuHoverColor = color;
    }
    
    public void setMenuTextColor(Color color) {
        this.menuTextColor = color;
        
        // Cập nhật màu text cho tất cả menu items
        for (JPanel panel : menuItemPanels.values()) {
            for (Component comp : panel.getComponents()) {
                if (comp instanceof JPanel) {
                    for (Component innerComp : ((JPanel) comp).getComponents()) {
                        if (innerComp instanceof JLabel) {
                            ((JLabel) innerComp).setForeground(color);
                        }
                    }
                }
            }
        }
    }
    
    public void setIconSize(int size) {
        this.iconSize = size;
        
        // Tái tạo tất cả menu items với kích thước mới
        removeAllMenuItems();
        addDefaultMenuItems();
    }
    private ImageIcon createHighQualityIcon(URL resourceUrl, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(resourceUrl);
            if (originalImage == null) return null;

            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();

            // Thiết lập chất lượng cao
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Vẽ lại ảnh với chất lượng cao
            g2d.drawImage(originalImage, 0, 0, width, height, null);
            g2d.dispose();

            return new ImageIcon(resizedImage);
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo icon: " + e.getMessage());
            return null;
        }
    }
}