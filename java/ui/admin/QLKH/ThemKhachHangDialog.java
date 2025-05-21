package ui.admin.QLKH;

import controller.KhachHangController;
import model.KhachHang;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class ThemKhachHangDialog extends JDialog {
    private KhachHangController controller;
    private JTextField txtMaTK, txtHoTen, txtSDT, txtEmail, txtCCCD, txtDiaChi, txtTongTienNo;
    private JButton btnThem, btnHuy;
    private final NumberFormat dinhDangTien = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    
    public ThemKhachHangDialog(Window parent, KhachHangController controller) {
        super(parent, "Thêm Khách Hàng Mới", ModalityType.APPLICATION_MODAL);
        this.controller = controller;
        initComponents();
    }
    
    private void initComponents() {
        setSize(450, 450);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("THÊM KHÁCH HÀNG MỚI");
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
        lblTitle.setForeground(new Color(0, 123, 255));
        titlePanel.add(lblTitle);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)), 
                "Thông tin khách hàng", 
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
                javax.swing.border.TitledBorder.DEFAULT_POSITION, 
                new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel lblMaTK = new JLabel("Mã TK:");
        lblMaTK.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        formPanel.add(lblMaTK);
        txtMaTK = new JTextField();
        formPanel.add(txtMaTK);
        
        JLabel lblHoTen = new JLabel("Họ Tên (*):");
        lblHoTen.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        formPanel.add(lblHoTen);
        txtHoTen = new JTextField();
        formPanel.add(txtHoTen);
        
        JLabel lblSDT = new JLabel("SĐT (*):");
        lblSDT.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        formPanel.add(lblSDT);
        txtSDT = new JTextField();
        formPanel.add(txtSDT);
        
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        formPanel.add(lblEmail);
        txtEmail = new JTextField();
        formPanel.add(txtEmail);
        
        JLabel lblCCCD = new JLabel("CCCD:");
        lblCCCD.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        formPanel.add(lblCCCD);
        txtCCCD = new JTextField();
        formPanel.add(txtCCCD);
        
        JLabel lblDiaChi = new JLabel("Địa Chỉ:");
        lblDiaChi.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        formPanel.add(lblDiaChi);
        txtDiaChi = new JTextField();
        formPanel.add(txtDiaChi);
        
        JLabel lblTongTienNo = new JLabel("Tổng Tiền Nợ:");
        lblTongTienNo.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        formPanel.add(lblTongTienNo);
        txtTongTienNo = new JTextField("0");
        formPanel.add(txtTongTienNo);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        btnThem = new JButton("Thêm");
        btnThem.setBackground(new Color(40, 167, 69));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFocusPainted(false);
        btnThem.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        btnThem.addActionListener(e -> themKhachHang());
        
        btnHuy = new JButton("Hủy");
        btnHuy.setBackground(new Color(108, 117, 125));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFocusPainted(false);
        btnHuy.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        btnHuy.addActionListener(e -> dispose());
        
        buttonPanel.add(btnThem);
        buttonPanel.add(btnHuy);
        
        // Add to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to dialog
        add(mainPanel);
    }
    
    private void themKhachHang() {
        try {
            // Get data from form
            String maTK = txtMaTK.getText().trim();
            String hoTen = txtHoTen.getText().trim();
            String sdt = txtSDT.getText().trim();
            String email = txtEmail.getText().trim();
            String cccd = txtCCCD.getText().trim();
            String diaChi = txtDiaChi.getText().trim();
            double tongTienNo = parseDouble(txtTongTienNo.getText().trim());
            
            // Validate data
            if (hoTen.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Họ tên không được để trống", 
                                             "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtHoTen.requestFocus();
                return;
            }
            
            if (sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống", 
                                             "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtSDT.requestFocus();
                return;
            }
            
            if (!sdt.matches("^0[0-9]{9}$")) {
                JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ (phải bắt đầu bằng 0 và đủ 10 số)", 
                                             "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtSDT.requestFocus();
                return;
            }
            
            if (!email.isEmpty() && !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                JOptionPane.showMessageDialog(this, "Email không hợp lệ", 
                                             "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtEmail.requestFocus();
                return;
            }
            
            if (!cccd.isEmpty() && !cccd.matches("^[0-9]{12}$")) {
                JOptionPane.showMessageDialog(this, "CCCD không hợp lệ (phải đủ 12 số)", 
                                             "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtCCCD.requestFocus();
                return;
            }
            
            // Check for duplicates
            if (controller.isPhoneNumberExists(sdt, null)) {
                JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại trong hệ thống", 
                                             "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtSDT.requestFocus();
                return;
            }
            
            if (!email.isEmpty() && controller.isEmailExists(email, null)) {
                JOptionPane.showMessageDialog(this, "Email đã tồn tại trong hệ thống", 
                                             "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtEmail.requestFocus();
                return;
            }
            
            if (!cccd.isEmpty() && controller.isCCCDExists(cccd, null)) {
                JOptionPane.showMessageDialog(this, "CCCD đã tồn tại trong hệ thống", 
                                             "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtCCCD.requestFocus();
                return;
            }
            
            // Create customer object
            KhachHang kh = new KhachHang();
            kh.setMaTK(maTK);
            kh.setHoTen(hoTen);
            kh.setSdt(sdt);
            kh.setEmail(email);
            kh.setCccd(cccd);
            kh.setDiaChi(diaChi);
            kh.setTongTienNo(tongTienNo);
            
            // Add customer
            String maKH = controller.addKhachHang(kh);
            
            if (maKH != null) {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công với mã " + maKH, 
                                             "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, controller.getErrorMessage(), 
                                             "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException | ParseException e) {
            JOptionPane.showMessageDialog(this, "Tổng tiền nợ không hợp lệ!", 
                                         "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTongTienNo.requestFocus();
        }
    }
    
    private double parseDouble(String value) throws ParseException {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        
        try {
            // Try to parse as a simple number first
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            // If that fails, try to parse as a currency format
            return dinhDangTien.parse(value).doubleValue();
        }
    }
}
