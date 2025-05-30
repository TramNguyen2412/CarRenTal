package ui.admin.QLKH;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import controller.KhachHangController;
import model.KhachHang;

@SuppressWarnings("serial")
public class SuaKhachHangDialog extends JDialog {
    private JTextField txtMaKH, txtHoTen, txtSDT, txtEmail, txtCCCD, txtDiaChi, txtTongTienNo;
    private KhachHang khachHang; // The customer being edited
    private KhachHangController controller;
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private boolean successfullyUpdated = false;

    public SuaKhachHangDialog(Window owner, KhachHang khachHang, KhachHangController controller) {
        super(owner, "Sửa Thông Tin Khách Hàng", ModalityType.APPLICATION_MODAL);
        this.khachHang = khachHang;
        this.controller = controller;
        initComponents();
        loadKhachHangData();
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel lblTitle = new JLabel("CHỈNH SỬA THÔNG TIN KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 20));
        lblTitle.setForeground(new Color(33, 150, 243));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(20, 20, 20, 20)));

        GroupLayout layout = new GroupLayout(formPanel);
        formPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel lblMaKH = createLabel("Mã KH:");
        JLabel lblHoTen = createLabel("Họ tên (*):");
        JLabel lblSDT = createLabel("SĐT (*):");
        JLabel lblEmail = createLabel("Email:");
        JLabel lblCCCD = createLabel("CCCD:");
        JLabel lblDiaChi = createLabel("Địa chỉ:");
        JLabel lblTongTienNo = createLabel("Tổng nợ:"); // Thêm label công nợ

        txtMaKH = createStyledTextField();
        txtMaKH.setEditable(false);
        txtMaKH.setBackground(new Color(230, 230, 230));

        txtHoTen = createStyledTextField();
        txtSDT = createStyledTextField();
        txtEmail = createStyledTextField();
        txtCCCD = createStyledTextField();
        txtDiaChi = createStyledTextField();

        // Thêm trường tổng tiền nợ - có thể chỉnh sửa
        txtTongTienNo = createStyledTextField();
        txtTongTienNo.setHorizontalAlignment(JTextField.RIGHT);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lblMaKH)
                                .addComponent(lblHoTen)
                                .addComponent(lblSDT)
                                .addComponent(lblEmail)
                                .addComponent(lblCCCD)
                                .addComponent(lblDiaChi)
                                .addComponent(lblTongTienNo)) // Thêm vào layout
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(txtMaKH)
                                .addComponent(txtHoTen)
                                .addComponent(txtSDT)
                                .addComponent(txtEmail)
                                .addComponent(txtCCCD)
                                .addComponent(txtDiaChi)
                                .addComponent(txtTongTienNo))); // Thêm vào layout

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblMaKH)
                                .addComponent(txtMaKH))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblHoTen)
                                .addComponent(txtHoTen))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblSDT)
                                .addComponent(txtSDT))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblEmail)
                                .addComponent(txtEmail))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblCCCD)
                                .addComponent(txtCCCD))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblDiaChi)
                                .addComponent(txtDiaChi))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblTongTienNo)
                                .addComponent(txtTongTienNo))); // Thêm vào layout

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton btnLuu = createStyledButton("Lưu", new Color(33, 150, 243));
        btnLuu.addActionListener(this::luuKhachHang);

        JButton btnHuy = createStyledButton("Hủy", new Color(120, 120, 120));
        btnHuy.addActionListener(e -> dispose());

        buttonPanel.add(btnLuu);
        buttonPanel.add(btnHuy);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        label.setForeground(new Color(70, 70, 70));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(250, 30));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        return textField;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 35));
        button.setFocusPainted(false);
        return button;
    }

    private void loadKhachHangData() {
        if (khachHang != null) {
            txtMaKH.setText(khachHang.getMaKH());
            txtHoTen.setText(khachHang.getHoTen());
            txtSDT.setText(khachHang.getSdt());
            txtEmail.setText(khachHang.getEmail() != null ? khachHang.getEmail() : "");
            txtCCCD.setText(khachHang.getCccd() != null ? khachHang.getCccd() : "");
            txtDiaChi.setText(khachHang.getDiaChi() != null ? khachHang.getDiaChi() : "");
            // Hiển thị tổng tiền nợ với format tiền tệ hoặc số thường
            txtTongTienNo.setText(String.valueOf(khachHang.getTongTienNo()));
        }
    }

    private void luuKhachHang(ActionEvent e) {
        String hoTen = txtHoTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();
        String cccd = txtCCCD.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String tongTienNoStr = txtTongTienNo.getText().trim();

        if (hoTen.isEmpty()) {
            showError("Họ tên không được để trống.", txtHoTen);
            return;
        }
        if (sdt.isEmpty()) {
            showError("Số điện thoại không được để trống.", txtSDT);
            return;
        }
        if (!sdt.matches("^0[0-9]{9}$")) {
            showError("SĐT không hợp lệ (phải bắt đầu bằng 0 và có 10 chữ số).", txtSDT);
            return;
        }

        // Sửa regex email - đã có lỗi chính tả "ZaZ"
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            showError("Email không hợp lệ.", txtEmail);
            return;
        }

        if (!cccd.isEmpty() && !cccd.matches("^[0-9]{12}$")) {
            showError("CCCD không hợp lệ (phải đủ 12 số).", txtCCCD);
            return;
        }

        // Kiểm tra và parse tổng tiền nợ
        double tongTienNo = 0;
        try {
            if (!tongTienNoStr.isEmpty()) {
                tongTienNo = Double.parseDouble(tongTienNoStr.replace(",", ""));
                if (tongTienNo < 0) {
                    showError("Tổng tiền nợ không được âm.", txtTongTienNo);
                    return;
                }
            }
        } catch (NumberFormatException ex) {
            showError("Tổng tiền nợ không hợp lệ.", txtTongTienNo);
            return;
        }

        // Sửa logic kiểm tra trùng lặp - so sánh chính xác hơn
        if (!sdt.equals(khachHang.getSdt()) && controller.isPhoneNumberExists(sdt, khachHang.getMaKH())) {
            showError("SĐT đã tồn tại cho khách hàng khác.", txtSDT);
            return;
        }

        // Sửa logic kiểm tra email - xử lý cả trường hợp null
        String currentEmail = khachHang.getEmail();
        if (!email.isEmpty()) {
            // Nếu email mới khác email cũ thì mới kiểm tra trùng lặp
            if ((currentEmail == null || !email.equals(currentEmail))
                    && controller.isEmailExists(email, khachHang.getMaKH())) {
                showError("Email đã tồn tại cho khách hàng khác.", txtEmail);
                return;
            }
        }

        // Sửa logic kiểm tra CCCD
        String currentCCCD = khachHang.getCccd();
        if (!cccd.isEmpty()) {
            // Nếu CCCD mới khác CCCD cũ thì mới kiểm tra trùng lặp
            if ((currentCCCD == null || !cccd.equals(currentCCCD))
                    && controller.isCCCDExists(cccd, khachHang.getMaKH())) {
                showError("CCCD đã tồn tại cho khách hàng khác.", txtCCCD);
                return;
            }
        }

        khachHang.setHoTen(hoTen);
        khachHang.setSdt(sdt);
        khachHang.setEmail(email.isEmpty() ? null : email);
        khachHang.setCccd(cccd.isEmpty() ? null : cccd);
        khachHang.setDiaChi(diaChi.isEmpty() ? null : diaChi);
        khachHang.setTongTienNo(tongTienNo);

        if (controller.updateKhachHang(khachHang)) {
            successfullyUpdated = true;
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin khách hàng thành công!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            String errorMsg = controller.getErrorMessage();
            JOptionPane.showMessageDialog(this,
                    "Cập nhật thông tin khách hàng thất bại!"
                            + (errorMsg != null && !errorMsg.isEmpty() ? "\nLỗi: " + errorMsg : ""),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showError(String message, JTextField field) {
        JOptionPane.showMessageDialog(this, message, "Lỗi Nhập Liệu", JOptionPane.ERROR_MESSAGE);
        if (field != null) {
            field.requestFocus();
            field.selectAll();
        }
    }

    public boolean isSuccessfullyUpdated() {
        return successfullyUpdated;
    }
}