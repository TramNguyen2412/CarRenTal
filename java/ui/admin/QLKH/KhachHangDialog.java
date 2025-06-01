package ui.admin.QLKH;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.KhachHangController;
import model.KhachHang;

public class KhachHangDialog extends JDialog {
    private KhachHang khachHang;
    private KhachHangController khachHangController;
    private boolean isSuccess = false;

    // UI Components
    private JTextField txtHoTen;
    private JTextField txtSDT;
    private JTextField txtEmail;
    private JTextField txtCCCD;
    private JTextArea txtDiaChi;
    private JTextField txtTongTienNo;

    public KhachHangDialog(Window owner, KhachHang khachHang) {
        super(owner, khachHang == null ? "Thêm khách hàng mới" : "Chỉnh sửa khách hàng",
                ModalityType.APPLICATION_MODAL);
        this.khachHang = khachHang;
        this.khachHangController = new KhachHangController();

        // Nếu là tạo mới, khởi tạo đối tượng KhachHang
        if (this.khachHang == null) {
            this.khachHang = new KhachHang();
        }

        initComponents();
    }

    private void initComponents() {
        setSize(600, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        JPanel pnlMain = new JPanel(new GridBagLayout());
        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Mã khách hàng (chỉ hiển thị khi chỉnh sửa)
        if (khachHang.getMaKH() != null) {
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0.0;
            pnlMain.add(new JLabel("Mã khách hàng:"), gbc);

            gbc.gridx = 1;
            gbc.weightx = 1.0;
            JLabel lblMaKH = new JLabel(khachHang.getMaKH());
            lblMaKH.setFont(lblMaKH.getFont().deriveFont(Font.BOLD));
            pnlMain.add(lblMaKH, gbc);
        }

        // Họ tên khách hàng
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        pnlMain.add(new JLabel("Họ tên:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtHoTen = new JTextField(20);
        pnlMain.add(txtHoTen, gbc);

        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        pnlMain.add(new JLabel("Số điện thoại:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtSDT = new JTextField(20);
        pnlMain.add(txtSDT, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        pnlMain.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        pnlMain.add(txtEmail, gbc);

        // CCCD
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        pnlMain.add(new JLabel("Số CCCD:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtCCCD = new JTextField(20);
        pnlMain.add(txtCCCD, gbc);

        // Địa chỉ
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        pnlMain.add(new JLabel("Địa chỉ:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtDiaChi = new JTextArea(3, 20);
        txtDiaChi.setLineWrap(true);
        txtDiaChi.setWrapStyleWord(true);
        JScrollPane scrollDiaChi = new JScrollPane(txtDiaChi);
        pnlMain.add(scrollDiaChi, gbc);

        // Tổng tiền nợ
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        pnlMain.add(new JLabel("Tổng tiền nợ:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTongTienNo = new JTextField(20);
        txtTongTienNo.setEditable(false);
        pnlMain.add(txtTongTienNo, gbc);

        // Panel nút
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        pnlButtons.add(btnSave);
        pnlButtons.add(btnCancel);

        add(pnlMain, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);

        // Hiển thị thông tin khách hàng nếu đang sửa
        if (khachHang.getMaKH() != null) {
            txtHoTen.setText(khachHang.getHoTen());
            txtSDT.setText(khachHang.getSdt());
            txtEmail.setText(khachHang.getEmail());
            txtCCCD.setText(khachHang.getCccd());
            txtDiaChi.setText(khachHang.getDiaChi());
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            txtTongTienNo.setText(currencyFormat.format(khachHang.getTongTienNo()));
        }

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
        if (khachHangController.isPhoneNumberExists(sdt, khachHang.getMaKH())) {
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
        if (!email.isEmpty() && khachHangController.isEmailExists(email, khachHang.getMaKH())) {
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
        if (!cccd.isEmpty() && khachHangController.isCCCDExists(cccd, khachHang.getMaKH())) {
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

        boolean success;
        if (khachHang.getMaKH() == null) {
            // Thêm mới
            String maKH = khachHangController.addKhachHang(khachHang);
            success = (maKH != null);
            if (success) {
                khachHang.setMaKH(maKH);
            }
        } else {
            // Cập nhật
            success = khachHangController.updateKhachHang(khachHang);
        }

        if (success) {
            this.isSuccess = true;
            JOptionPane.showMessageDialog(this,
                    "Lưu thông tin khách hàng thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Lưu thông tin khách hàng thất bại!",
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