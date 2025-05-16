package ui.auth;

import dao.TaiKhoanDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.border.EmptyBorder;
import model.TaiKhoan;
import ui.admin.AdminDashboard;
import ui.customer.CustomerDashboard;
import java.io.File;
import java.awt.Image;

public class LoginForm extends javax.swing.JFrame {
    
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JLabel lblTitle;
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblImage;
    
    public LoginForm() {
        initComponents();
        this.setLocationRelativeTo(null); // Center window
    }
    
    private void initComponents() {
        setTitle("Đăng nhập");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 600);
        setResizable(false);
        
        mainPanel = new JPanel(new GridLayout(1, 2));
        leftPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel(new GridBagLayout());
        
        // Left panel with image
        
//                try {
//             // Tạo panel gradient thay vì dùng ảnh
//             JPanel gradientPanel = new JPanel() {
//                 @Override
//                 protected void paintComponent(Graphics g) {
//                     super.paintComponent(g);
//                     Graphics2D g2d = (Graphics2D) g.create();
//
//                     // Gradient từ xanh đậm sang xanh nhạt
//                     g2d.setPaint(new GradientPaint(
//                         0, 0, new Color(41, 128, 185),
//                         0, getHeight(), new Color(109, 213, 250)
//                     ));
//                     g2d.fillRect(0, 0, getWidth(), getHeight());
//
//                     // Vẽ tiêu đề
//                     g2d.setFont(new Font("Arial", Font.BOLD, 30));
//                     g2d.setColor(Color.WHITE);
//                     g2d.drawString("CAR RENTAL", getWidth()/2 - 100, getHeight()/2 - 50);
//                     g2d.setFont(new Font("Arial", Font.BOLD, 20));
//                     g2d.drawString("MANAGEMENT SYSTEM", getWidth()/2 - 120, getHeight()/2);
//
//                     g2d.dispose();
//                 }
//             };
//
//             leftPanel.setLayout(new BorderLayout());
//             leftPanel.add(gradientPanel, BorderLayout.CENTER);
//
//         } catch (Exception e) {
//             leftPanel.setBackground(new Color(208, 240, 240));
//             System.out.println("Lỗi: " + e.getMessage());
//             e.printStackTrace();
//         }
    
        try {
            // Tìm đường dẫn tuyệt đối của file
            File file = new File("src/main/java/img/login_image2.jpg");
            if(file.exists()) {
                ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());

                // Resize ảnh
                Image img = imageIcon.getImage().getScaledInstance(450, 600, Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(img);

                lblImage = new JLabel(imageIcon);
                leftPanel.add(lblImage, BorderLayout.CENTER);

                System.out.println("✅ Đã load ảnh thành công bằng đường dẫn: " + file.getAbsolutePath());
            } else {
                System.out.println("❌ Không tìm thấy file ảnh tại: " + file.getAbsolutePath());
                leftPanel.setBackground(new Color(208, 240, 240));
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
            leftPanel.setBackground(new Color(208, 240, 240));
        }
        // Right panel with login form
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        
        // Use GridBagConstraints for proper layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        
        // Title
        lblTitle = new JLabel("ĐĂNG NHẬP VÀO HỆ THỐNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        rightPanel.add(lblTitle, gbc);
        
        // Username Label
        lblUsername = new JLabel("Tên đăng nhập");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 5, 0);
        rightPanel.add(lblUsername, gbc);
        
        // Username TextField
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsername.setPreferredSize(new Dimension(300, 40));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        rightPanel.add(txtUsername, gbc);
        
        // Password Label
        lblPassword = new JLabel("Mật khẩu");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 5, 0);
        rightPanel.add(lblPassword, gbc);
        
        // Password Field
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(300, 40));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 40, 0);
        // Thêm sự kiện Enter key để đăng nhập
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnLoginActionPerformed();
                }
            }
        });
        rightPanel.add(txtPassword, gbc);
        
        // Login Button
        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(300, 50));
        btnLogin.setBackground(Color.BLACK);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> btnLoginActionPerformed());
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 0, 0, 0);
        rightPanel.add(btnLogin, gbc);
        
        // Add panels to main panel
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        
        // Add main panel to frame
        getContentPane().add(mainPanel);
    }
    
    private void btnLoginActionPerformed() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin đăng nhập");
            return;
        }
        
        TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
        TaiKhoan taiKhoan = taiKhoanDAO.checkLogin(username, password);
        
        if (taiKhoan != null) {
            // Kiểm tra vai trò
            switch (taiKhoan.getMaVaiTro()) {
                case "VT001" -> {
                    // Mở form dành cho khách hàng
                    this.dispose();
                    new CustomerDashboard(taiKhoan).setVisible(true);
                }
                case "VT002" -> {
                    // Mở form dành cho quản lý
                    this.dispose();
                    new AdminDashboard(taiKhoan).setVisible(true);
                }
                default -> JOptionPane.showMessageDialog(this,
                        "Vai trò của tài khoản không được hỗ trợ: " + taiKhoan.getMaVaiTro());
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Đăng nhập thất bại! Kiểm tra lại thông tin đăng nhập");
        }
    }
}