package ui.admin.QLHD;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import controller.KhachHangController;
import model.KhachHang;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.regex.Pattern;

public class HopDongKhachHangDialog extends JDialog {
    private KhachHang khachHang;
    private KhachHangController khachHangController;
    private boolean isSuccess = false;
    
    // UI Components
    private JTextField txtHoTen;
    private JTextField txtSDT;
    private JTextField txtEmail;
    private JTextField txtCCCD;
    private JTextArea txtDiaChi;
    
    public HopDongKhachHangDialog(Window owner, KhachHang khachHang) {
        super(owner, "Thêm khách hàng mới", ModalityType.APPLICATION_MODAL);
        this.khachHang = khachHang;
        this.khachHangController = new KhachHangController();
        
        // Luôn tạo mới khách hàng
        this.khachHang = new KhachHang();
        
        initComponents();
    }
    
    private void initComponents() {
        setSize(500, 400);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));
        
        JPanel pnlMain = new JPanel(new GridBagLayout());
        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Họ tên khách hàng
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        pnlMain.add(new JLabel("Họ tên:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtHoTen = new JTextField(20);
        pnlMain.add(txtHoTen, gbc);
        
        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        pnlMain.add(new JLabel("Số điện thoại:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtSDT = new JTextField(20);
        pnlMain.add(txtSDT, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        pnlMain.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        pnlMain.add(txtEmail, gbc);
        
        // CCCD
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        pnlMain.add(new JLabel("Số CCCD:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtCCCD = new JTextField(20);
        pnlMain.add(txtCCCD, gbc);
        
        // Địa chỉ
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        pnlMain.add(new JLabel("Địa chỉ:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtDiaChi = new JTextArea(3, 20);
        txtDiaChi.setLineWrap(true);
        txtDiaChi.setWrapStyleWord(true);
        JScrollPane scrollDiaChi = new JScrollPane(txtDiaChi);
        pnlMain.add(scrollDiaChi, gbc);
        
        // Panel nút
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu");
        btnSave.setBackground(new Color(41, 121, 255)); // Xanh dương da trời
        btnSave.setForeground(Color.WHITE);  
        btnSave.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14)); // Chữ đậm

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setBackground(Color.GRAY);            // Màu xám
        btnCancel.setForeground(Color.WHITE);   
        btnCancel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14)); // Chữ đậm

        pnlButtons.add(btnSave);
        pnlButtons.add(btnCancel);
        
        add(pnlMain, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);
        
        // Sự kiện nút lưu
        btnSave.addActionListener(e -> saveKhachHang());
        
        // Sự kiện nút hủy
        btnCancel.addActionListener(e -> dispose());
    }
    
    private void saveKhachHang() {
        // Validate dữ liệu nhập
        String hoTen = txtHoTen.getText().trim();
        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Vui lòng nhập họ tên khách hàng!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            txtHoTen.requestFocus();
            return;
        }
        
        String sdt = txtSDT.getText().trim();
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Vui lòng nhập số điện thoại!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            txtSDT.requestFocus();
            return;
        }
        
        // Kiểm tra định dạng số điện thoại
        if (!Pattern.matches("^0[0-9]{9}$", sdt)) {
            JOptionPane.showMessageDialog(this, 
                    "Số điện thoại không hợp lệ! (Định dạng: 10 số, bắt đầu bằng 0)", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            txtSDT.requestFocus();
            return;
        }
        
        // Kiểm tra SĐT đã tồn tại chưa
        if (khachHangController.isPhoneNumberExists(sdt, null)) {
            JOptionPane.showMessageDialog(this, 
                    "Số điện thoại này đã được đăng ký bởi khách hàng khác!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            txtSDT.requestFocus();
            return;
        }
        
        // Kiểm tra định dạng email nếu có nhập
        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email)) {
            JOptionPane.showMessageDialog(this, 
                    "Email không hợp lệ!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return;
        }
        
        // Kiểm tra email đã tồn tại chưa
        if (!email.isEmpty() && khachHangController.isEmailExists(email, null)) {
            JOptionPane.showMessageDialog(this, 
                    "Email này đã được đăng ký bởi khách hàng khác!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return;
        }
        
        // Kiểm tra định dạng CCCD nếu có nhập
        String cccd = txtCCCD.getText().trim();
        if (!cccd.isEmpty() && !Pattern.matches("^[0-9]{12}$", cccd)) {
            JOptionPane.showMessageDialog(this, 
                    "Số CCCD không hợp lệ! (Định dạng: 12 số)", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            txtCCCD.requestFocus();
            return;
        }
        
        // Kiểm tra CCCD đã tồn tại chưa
        if (!cccd.isEmpty() && khachHangController.isCCCDExists(cccd, null)) {
            JOptionPane.showMessageDialog(this, 
                    "Số CCCD này đã được đăng ký bởi khách hàng khác!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            txtCCCD.requestFocus();
            return;
        }
        
        // Cập nhật thông tin khách hàng
        khachHang.setHoTen(hoTen);
        khachHang.setSdt(sdt);
        khachHang.setEmail(email);
        khachHang.setCccd(cccd);
        khachHang.setDiaChi(txtDiaChi.getText().trim());
        
        // Thêm mới khách hàng
        String maKH = khachHangController.addKhachHang(khachHang);
        boolean success = (maKH != null);
        if (success) {
            khachHang.setMaKH(maKH);
            this.isSuccess = true;
            JOptionPane.showMessageDialog(this, 
                    "Thêm khách hàng mới thành công!", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Thêm khách hàng mới thất bại!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isSuccess() {
        return isSuccess;
    }
    
    public KhachHang getKhachHang() {
        return khachHang;
    }
}