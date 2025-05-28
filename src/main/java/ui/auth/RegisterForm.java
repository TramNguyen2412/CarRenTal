package ui.auth;

import controller.KhachHangController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class RegisterForm extends javax.swing.JFrame {
    
    private JPanel mainPanel;
    private JPanel formPanel;
    private JLabel lblTitle;
    
    // Thông tin cá nhân
    private JLabel lblHoTen;
    private JTextField txtHoTen;
    private JLabel lblSDT;
    private JTextField txtSDT;
    private JLabel lblEmail;
    private JTextField txtEmail;
    private JLabel lblCCCD;
    private JTextField txtCCCD;
    private JLabel lblDiaChi;
    private JTextArea txtDiaChi;
    
    // Thông tin đăng nhập
    private JLabel lblTenDangNhap;
    private JTextField txtTenDangNhap;
    private JLabel lblMatKhau;
    private JPasswordField txtMatKhau;
    private JLabel lblNhapLaiMatKhau;
    private JPasswordField txtNhapLaiMatKhau;
    
    // Nút điều khiển
    private JButton btnRegister;
    private JButton btnCancel;
    private JLabel lblPasswordHint;
    
    private KhachHangController khachHangController;
    
    public RegisterForm() {
        khachHangController = new KhachHangController();
        initComponents();
        this.setLocationRelativeTo(null); // Center window
    }
    
    private void initComponents() {
        setTitle("Đăng ký tài khoản");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(700, 700);
        setResizable(false);
        
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        mainPanel.setBackground(Color.WHITE);
        
        // Form panel with GridBagLayout for better control
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;
        
        // Title
        lblTitle = new JLabel("ĐĂNG KÝ TÀI KHOẢN KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(lblTitle, gbc);
        
        // ===== THÔNG TIN CÁ NHÂN =====
        
        // Họ tên
        lblHoTen = new JLabel("Họ và tên:");
        lblHoTen.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(lblHoTen, gbc);
        
        txtHoTen = new JTextField();
        txtHoTen.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        txtHoTen.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        formPanel.add(txtHoTen, gbc);
        
        // Số điện thoại
        lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblSDT, gbc);
        
        txtSDT = new JTextField();
        txtSDT.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        txtSDT.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        formPanel.add(txtSDT, gbc);
        
        // Email
        lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(lblEmail, gbc);
        
        txtEmail = new JTextField();
        txtEmail.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        txtEmail.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);
        
        // CCCD
        lblCCCD = new JLabel("Số CCCD:");
        lblCCCD.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(lblCCCD, gbc);
        
        txtCCCD = new JTextField();
        txtCCCD.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        txtCCCD.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        formPanel.add(txtCCCD, gbc);
        
        // Địa chỉ
        lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTH;
        formPanel.add(lblDiaChi, gbc);
        
        txtDiaChi = new JTextArea();
        txtDiaChi.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        txtDiaChi.setLineWrap(true);
        txtDiaChi.setWrapStyleWord(true);
        JScrollPane scrollDiaChi = new JScrollPane(txtDiaChi);
        scrollDiaChi.setPreferredSize(new Dimension(300, 70));
        gbc.gridx = 1;
        formPanel.add(scrollDiaChi, gbc);
        
        // ===== THÔNG TIN ĐĂNG NHẬP =====
        
        // Tên đăng nhập
        lblTenDangNhap = new JLabel("Tên đăng nhập:");
        lblTenDangNhap.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(25, 5, 5, 5);
        formPanel.add(lblTenDangNhap, gbc);
        
        txtTenDangNhap = new JTextField();
        txtTenDangNhap.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        txtTenDangNhap.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        formPanel.add(txtTenDangNhap, gbc);
        
        // Mật khẩu
        lblMatKhau = new JLabel("Mật khẩu:");
        lblMatKhau.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(lblMatKhau, gbc);
        
        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        txtMatKhau.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        formPanel.add(txtMatKhau, gbc);
        
        // Password hint
        lblPasswordHint = new JLabel("Mật khẩu phải có ít nhất 8 ký tự, chứa chữ hoa, chữ thường và số");
        lblPasswordHint.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 12));
        lblPasswordHint.setForeground(Color.GRAY);
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 5, 10, 5);
        formPanel.add(lblPasswordHint, gbc);
        
        // Nhập lại mật khẩu
        lblNhapLaiMatKhau = new JLabel("Nhập lại mật khẩu:");
        lblNhapLaiMatKhau.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(lblNhapLaiMatKhau, gbc);
        
        txtNhapLaiMatKhau = new JPasswordField();
        txtNhapLaiMatKhau.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        txtNhapLaiMatKhau.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        formPanel.add(txtNhapLaiMatKhau, gbc);
        
        // ===== BUTTONS =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        // Register button
        btnRegister = new JButton("ĐĂNG KÝ");
        btnRegister.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        btnRegister.setPreferredSize(new Dimension(150, 45));
        btnRegister.setBackground(Color.BLACK);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> btnRegisterActionPerformed());
        buttonPanel.add(btnRegister);
        
        // Cancel button
        btnCancel = new JButton("HỦY");
        btnCancel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        btnCancel.setPreferredSize(new Dimension(150, 45));
        btnCancel.setBackground(new Color(240, 240, 240));
        btnCancel.setForeground(Color.BLACK);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorderPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> btnCancelActionPerformed());
        buttonPanel.add(btnCancel);
        
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 5, 5, 5);
        formPanel.add(buttonPanel, gbc);
        
        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        getContentPane().add(mainPanel);
    }
    
    private void btnRegisterActionPerformed() {
        // Lấy dữ liệu từ form
        String hoTen = txtHoTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();
        String cccd = txtCCCD.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());
        String nhapLaiMatKhau = new String(txtNhapLaiMatKhau.getPassword());
        
        // Kiểm tra dữ liệu
        if (hoTen.isEmpty() || sdt.isEmpty() || email.isEmpty() || cccd.isEmpty() || 
            diaChi.isEmpty() || tenDangNhap.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", 
                                         "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra mật khẩu khớp nhau
        if (!matKhau.equals(nhapLaiMatKhau)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu nhập lại không khớp!", 
                                         "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Gọi Controller để đăng ký
        String message = khachHangController.dangKyKhachHang(
            hoTen, sdt, email, cccd, diaChi, tenDangNhap, matKhau);
        
        // Xử lý kết quả trả về
        if (message.startsWith("Đăng ký thành công")) {
            JOptionPane.showMessageDialog(this, message, 
                                         "Đăng ký thành công", JOptionPane.INFORMATION_MESSAGE);
            // Đóng form đăng ký và mở form đăng nhập
            this.dispose();
            new LoginForm().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, message, 
                                         "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void btnCancelActionPerformed() {
        // Đóng form đăng ký và mở lại form đăng nhập
        this.dispose();
        new LoginForm().setVisible(true);
    }
}