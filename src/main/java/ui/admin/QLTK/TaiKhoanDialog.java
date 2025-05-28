package ui.admin.QLTK;

import controller.TaiKhoanController;
import model.TaiKhoan;
import model.VaiTro;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import javax.swing.*;

import java.awt.*;



import model.TaiKhoanExtended;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TaiKhoanDialog extends JDialog {
    private TaiKhoanExtended taiKhoan;
    private TaiKhoanPanel parent;
    private TaiKhoanController controller;
    
    // Các thành phần giao diện
    private JTextField txtMaTK, txtTenDangNhap;
    private JPasswordField txtMatKhau, txtXacNhanMatKhau;
    private JComboBox<VaiTro> cboVaiTro;
    private JComboBox<String> cboTrangThai;
    private JComboBox<String> cboLoaiUser;
    private JComboBox<ComboItem> cboNguoiDung;
    
    public TaiKhoanDialog(Window owner, TaiKhoanExtended taiKhoan, TaiKhoanPanel parent) {
        super(owner, taiKhoan == null ? "Thêm tài khoản mới" : "Chỉnh sửa tài khoản", ModalityType.APPLICATION_MODAL);
        this.taiKhoan = taiKhoan;
        // Nếu taiKhoan là null, khởi tạo đối tượng mới để tránh NullPointerException
        if (this.taiKhoan == null) {
            this.taiKhoan = new TaiKhoanExtended();
        }
        this.parent = parent;
        this.controller = new TaiKhoanController();

        initComponents();
        loadVaiTroToComboBox();
        
        // Nếu là chế độ sửa, hiển thị dữ liệu của tài khoản
        if (taiKhoan != null && taiKhoan.getMaTK() != null && !taiKhoan.getMaTK().isEmpty()) {
            txtMaTK.setText(taiKhoan.getMaTK());
            txtMaTK.setEditable(false);
            txtTenDangNhap.setText(taiKhoan.getTenDangNhap());
            
            // Chọn vai trò
            for (int i = 0; i < cboVaiTro.getItemCount(); i++) {
                VaiTro vt = cboVaiTro.getItemAt(i);
                if (vt.getMaVaiTro().equals(taiKhoan.getMaVaiTro())) {
                    cboVaiTro.setSelectedIndex(i);
                    break;
                }
            }
            
            // Chọn trạng thái
            cboTrangThai.setSelectedItem(taiKhoan.getTrangThai());
            
            // Chọn loại user hiện tại
            if (taiKhoan.getLoaiNguoiDung() != null) {
                cboLoaiUser.setSelectedItem(taiKhoan.getLoaiNguoiDung());
                // Load danh sách người dùng tương ứng
                loadNguoiDungComboBox();
                
                // Thêm thông tin người dùng hiện tại vào combobox (nếu có)
                if (taiKhoan.getMaNguoiDung() != null && !taiKhoan.getMaNguoiDung().isEmpty()) {
                    ComboItem currentUser = new ComboItem(
                        taiKhoan.getTenNguoiDung() + " (" + taiKhoan.getMaNguoiDung() + ")", 
                        taiKhoan.getMaNguoiDung()
                    );
                    cboNguoiDung.addItem(currentUser);
                    cboNguoiDung.setSelectedItem(currentUser);
                }
            }
            
            // Không yêu cầu nhập mật khẩu khi cập nhật
            JLabel lblNote = new JLabel("(Để trống nếu không đổi mật khẩu)");
            lblNote.setFont(new Font("Arial", Font.ITALIC, 12));
            lblNote.setForeground(Color.GRAY);
            JPanel pnlMatKhau = (JPanel) txtMatKhau.getParent();
            pnlMatKhau.add(lblNote, BorderLayout.SOUTH);
        } else {
            // Thêm mới
            txtMaTK.setText("Tự động tạo");
            txtMaTK.setEditable(false);
        }
        
        // Sự kiện khi chọn loại user
        cboLoaiUser.addActionListener(e -> loadNguoiDungComboBox());
        
        setResizable(false);
    }
    
    private void initComponents() {
        // Tăng kích thước dialog
        setSize(700, 650); // Tăng chiều cao để chứa thêm các trường mới
        setLocationRelativeTo(getOwner());
        
        // Panel chính có màu nền nhẹ nhàng
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(new Color(245, 245, 245));
        
        // Panel form với nền trắng
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Sử dụng GroupLayout cho form nhập liệu
        GroupLayout layout = new GroupLayout(formPanel);
        formPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        // Tiêu đề form
        JLabel lblTitle = new JLabel(taiKhoan.getMaTK() == null ? "THÊM TÀI KHOẢN MỚI" : "CHỈNH SỬA TÀI KHOẢN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(33, 150, 243));
        
        // Các label
        JLabel lblMaTKTitle = createLabel("Mã tài khoản:", 16);
        JLabel lblTenDangNhapTitle = createLabel("Tên đăng nhập:", 16);
        JLabel lblMatKhauTitle = createLabel("Mật khẩu:", 16);
        JLabel lblXacNhanMatKhauTitle = createLabel("Xác nhận mật khẩu:", 16);
        JLabel lblVaiTroTitle = createLabel("Vai trò:", 16);
        JLabel lblTrangThaiTitle = createLabel("Trạng thái:", 16);
        JLabel lblLoaiUserTitle = createLabel("Loại người dùng:", 16); // Thêm label mới
        JLabel lblNguoiDungTitle = createLabel("Người dùng:", 16); // Thêm label mới
        
        // Các component nhập liệu
        txtMaTK = createStyledTextField();
        txtTenDangNhap = createStyledTextField();
        txtMatKhau = createStyledPasswordField();
        txtXacNhanMatKhau = createStyledPasswordField();
        
        cboVaiTro = new JComboBox<>();
        cboVaiTro.setFont(new Font("Arial", Font.PLAIN, 16));
        cboVaiTro.setPreferredSize(new Dimension(250, 35));
        
        cboTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Không hoạt động"});
        cboTrangThai.setFont(new Font("Arial", Font.PLAIN, 16));
        cboTrangThai.setPreferredSize(new Dimension(250, 35));
        
        cboLoaiUser = new JComboBox<>(new String[]{"UNKNOWN", "KH", "NV"});
        cboLoaiUser.setFont(new Font("Arial", Font.PLAIN, 16));
        cboLoaiUser.setPreferredSize(new Dimension(250, 35));
        
        cboNguoiDung = new JComboBox<>();
        cboNguoiDung.setFont(new Font("Arial", Font.PLAIN, 16));
        cboNguoiDung.setPreferredSize(new Dimension(250, 35));
        
        // Thiết lập nhóm ngang
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(lblTitle)
                .addComponent(lblMaTKTitle)
                .addComponent(lblTenDangNhapTitle)
                .addComponent(lblMatKhauTitle)
                .addComponent(lblXacNhanMatKhauTitle)
                .addComponent(lblVaiTroTitle)
                .addComponent(lblTrangThaiTitle)
                .addComponent(lblLoaiUserTitle)
                .addComponent(lblNguoiDungTitle));
        
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(txtMaTK)
                .addComponent(txtTenDangNhap)
                .addComponent(txtMatKhau)
                .addComponent(txtXacNhanMatKhau)
                .addComponent(cboVaiTro)
                .addComponent(cboTrangThai)
                .addComponent(cboLoaiUser)
                .addComponent(cboNguoiDung));
        
        layout.setHorizontalGroup(hGroup);
        
        // Thiết lập nhóm dọc
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        
        vGroup.addComponent(lblTitle);
        vGroup.addGap(20);
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblMaTKTitle)
                .addComponent(txtMaTK));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblTenDangNhapTitle)
                .addComponent(txtTenDangNhap));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblMatKhauTitle)
                .addComponent(txtMatKhau));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblXacNhanMatKhauTitle)
                .addComponent(txtXacNhanMatKhau));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblVaiTroTitle)
                .addComponent(cboVaiTro));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblTrangThaiTitle)
                .addComponent(cboTrangThai));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblLoaiUserTitle)
                .addComponent(cboLoaiUser));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblNguoiDungTitle)
                .addComponent(cboNguoiDung));
        
        layout.setVerticalGroup(vGroup);
        
        // Panel chứa các nút thao tác
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));
        
        JButton btnSave = createStyledButton("Lưu", new Color(33, 150, 243));
        JButton btnCancel = createStyledButton("Hủy", new Color(120, 120, 120));
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Sự kiện nút lưu
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    saveTaiKhoan();
                }
            }
        });
        
        // Sự kiện nút hủy
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void loadVaiTroToComboBox() {
        List<VaiTro> danhSachVaiTro = controller.getAllVaiTro();
        
        for (VaiTro vaiTro : danhSachVaiTro) {
            cboVaiTro.addItem(vaiTro);
        }
    }
    
    private void loadNguoiDungComboBox() {
        cboNguoiDung.removeAllItems();
        
        String loai = (String) cboLoaiUser.getSelectedItem();
        if ("KH".equals(loai)) {
            List<String[]> danhSachKH = controller.getKhachHangChuaCoTaiKhoan();
            for (String[] kh : danhSachKH) {
                cboNguoiDung.addItem(new ComboItem(kh[1] + " (" + kh[0] + ")", kh[0]));
            }
            
            // Nếu đang sửa và tài khoản đã liên kết với khách hàng, thêm vào và chọn
            if (taiKhoan.getMaTK() != null && "KH".equals(taiKhoan.getLoaiNguoiDung()) && taiKhoan.getMaNguoiDung() != null) {
                boolean exists = false;
                for (int i = 0; i < cboNguoiDung.getItemCount(); i++) {
                    if (((ComboItem)cboNguoiDung.getItemAt(i)).getValue().equals(taiKhoan.getMaNguoiDung())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    ComboItem currentUser = new ComboItem(taiKhoan.getTenNguoiDung() + " (" + taiKhoan.getMaNguoiDung() + ")", taiKhoan.getMaNguoiDung());
                    cboNguoiDung.addItem(currentUser);
                    cboNguoiDung.setSelectedItem(currentUser);
                }
            }
        } else if ("NV".equals(loai)) {
            List<String[]> danhSachNV = controller.getNhanVienChuaCoTaiKhoan();
            for (String[] nv : danhSachNV) {
                cboNguoiDung.addItem(new ComboItem(nv[1] + " (" + nv[0] + ")", nv[0]));
            }
            
            // Nếu đang sửa và tài khoản đã liên kết với nhân viên, thêm vào và chọn
            if (taiKhoan.getMaTK() != null && "NV".equals(taiKhoan.getLoaiNguoiDung()) && taiKhoan.getMaNguoiDung() != null) {
                boolean exists = false;
                for (int i = 0; i < cboNguoiDung.getItemCount(); i++) {
                    if (cboNguoiDung.getItemCount() > 0 && ((ComboItem)cboNguoiDung.getItemAt(i)).getValue().equals(taiKhoan.getMaNguoiDung())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    ComboItem currentUser = new ComboItem(taiKhoan.getTenNguoiDung() + " (" + taiKhoan.getMaNguoiDung() + ")", taiKhoan.getMaNguoiDung());
                    cboNguoiDung.addItem(currentUser);
                    cboNguoiDung.setSelectedItem(currentUser);
                }
            }
        }
        
        // Vô hiệu hóa combobox nếu là "UNKNOWN" hoặc không có người dùng
        cboNguoiDung.setEnabled(!"UNKNOWN".equals(loai) && cboNguoiDung.getItemCount() > 0);
    }
    
    private boolean validateInput() {
        // Kiểm tra tên đăng nhập
        if (txtTenDangNhap.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenDangNhap.requestFocus();
            return false;
        }
        
        // Kiểm tra tên đăng nhập tồn tại (khi thêm mới hoặc đổi tên đăng nhập khi cập nhật)
        String newUsername = txtTenDangNhap.getText().trim();
        boolean isNew = taiKhoan.getMaTK() == null;

        if (isNew) {
            // Đối với tài khoản mới
            if (controller.isUsernameExists(newUsername)) {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtTenDangNhap.requestFocus();
                return false;
            }
        } else {
            // Đối với tài khoản hiện có, kiểm tra nếu thay đổi tên đăng nhập
            if (!newUsername.equals(taiKhoan.getTenDangNhap())) {
                if (controller.isUsernameExists(newUsername)) {
                    JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtTenDangNhap.requestFocus();
                    return false;
                }
            }
        }
        
        // Kiểm tra mật khẩu khi thêm mới
        if (taiKhoan.getMaTK() == null) {
            if (String.valueOf(txtMatKhau.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mật khẩu không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtMatKhau.requestFocus();
                return false;
            }
            
            if (String.valueOf(txtXacNhanMatKhau.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtXacNhanMatKhau.requestFocus();
                return false;
            }
            
            if (!String.valueOf(txtMatKhau.getPassword()).equals(String.valueOf(txtXacNhanMatKhau.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Mật khẩu và xác nhận mật khẩu không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtMatKhau.requestFocus();
                return false;
            }
        } else {
            // Kiểm tra mật khẩu khi cập nhật (chỉ khi đã nhập)
            if (!String.valueOf(txtMatKhau.getPassword()).isEmpty()) {
                if (String.valueOf(txtXacNhanMatKhau.getPassword()).isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập xác nhận mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtXacNhanMatKhau.requestFocus();
                    return false;
                }
                
                if (!String.valueOf(txtMatKhau.getPassword()).equals(String.valueOf(txtXacNhanMatKhau.getPassword()))) {
                    JOptionPane.showMessageDialog(this, "Mật khẩu và xác nhận mật khẩu không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtMatKhau.requestFocus();
                    return false;
                }
            }
        }
        
        // Kiểm tra vai trò
        if (cboVaiTro.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn vai trò!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            cboVaiTro.requestFocus();
            return false;
        }
        
        // Kiểm tra liên kết người dùng nếu đã chọn loại
        if (!"UNKNOWN".equals(cboLoaiUser.getSelectedItem()) && cboNguoiDung.isEnabled() && cboNguoiDung.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng để liên kết!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            cboNguoiDung.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void saveTaiKhoan() {
        // Cập nhật thông tin tài khoản từ form
        taiKhoan.setTenDangNhap(txtTenDangNhap.getText().trim());
        
        // Cập nhật mật khẩu nếu có
        String matKhau = String.valueOf(txtMatKhau.getPassword());
        if (!matKhau.isEmpty()) {
            taiKhoan.setMatKhau(matKhau);
        }
        
        // Cập nhật vai trò
        VaiTro selectedVaiTro = (VaiTro) cboVaiTro.getSelectedItem();
        taiKhoan.setMaVaiTro(selectedVaiTro.getMaVaiTro());
        
        // Cập nhật trạng thái
        taiKhoan.setTrangThai((String) cboTrangThai.getSelectedItem());
        
        // Cập nhật thông tin liên kết người dùng
        String selectedLoaiUser = (String) cboLoaiUser.getSelectedItem();
        taiKhoan.setLoaiNguoiDung(selectedLoaiUser);
        
        if (cboNguoiDung.isEnabled() && cboNguoiDung.getSelectedItem() != null) {
            ComboItem selectedItem = (ComboItem) cboNguoiDung.getSelectedItem();
            taiKhoan.setMaNguoiDung(selectedItem.getValue());
        } else {
            taiKhoan.setMaNguoiDung(null);
        }
        
        boolean success;
        if (taiKhoan.getMaTK() == null) {
            // Thêm mới tài khoản
            success = controller.createTaiKhoan(taiKhoan, taiKhoan.getLoaiNguoiDung(), taiKhoan.getMaNguoiDung());
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Thêm tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            // Cập nhật tài khoản
            success = controller.updateTaiKhoan(taiKhoan);
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật tài khoản thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Cập nhật lại dữ liệu trên bảng
        if (success) {
            parent.loadDataToTable();
            dispose();
        }
    }
    
    private JLabel createLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        return label;
    }
    
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return passwordField;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
        return button;
    }
    
    // Inner class cho combobox items
    class ComboItem {
        private String label;
        private String value;
        
        public ComboItem(String label, String value) {
            this.label = label;
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        @Override
        public String toString() {
            return label;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ComboItem) {
                return ((ComboItem) obj).getValue().equals(this.value);
            }
            return false;
        }
    }
}