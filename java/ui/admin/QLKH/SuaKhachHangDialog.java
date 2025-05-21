package ui.admin.QLKH;

import controller.KhachHangController;
import model.KhachHang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class SuaKhachHangDialog extends JDialog {
    private final KhachHangController controller;
    private final KhachHang khachHang;

    private JTextField txtMaKH, txtMaTK, txtHoTen, txtSDT, txtEmail, txtCCCD, txtDiaChi, txtTongTienNo;
    private final NumberFormat dinhDangTien = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public SuaKhachHangDialog(Window parent, KhachHangController controller, KhachHang khachHang) {
        super(parent, "Sửa Thông Tin Khách Hàng", ModalityType.APPLICATION_MODAL);
        this.controller = controller;
        this.khachHang = khachHang;

        initComponents();
        loadKhachHangData();
    }

    private void initComponents() {
        setSize(450, 500);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        // Tiêu đề
        JLabel lblTitle = new JLabel("SỬA THÔNG TIN KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
        lblTitle.setForeground(new Color(0, 123, 255));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Form thông tin
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));

        formPanel.add(new JLabel("Mã KH:"));        txtMaKH = new JTextField(); txtMaKH.setEditable(false); formPanel.add(txtMaKH);
        formPanel.add(new JLabel("Mã TK:"));        txtMaTK = new JTextField(); formPanel.add(txtMaTK);
        formPanel.add(new JLabel("Họ Tên (*):"));   txtHoTen = new JTextField(); formPanel.add(txtHoTen);
        formPanel.add(new JLabel("SĐT (*):"));      txtSDT = new JTextField(); formPanel.add(txtSDT);
        formPanel.add(new JLabel("Email:"));        txtEmail = new JTextField(); formPanel.add(txtEmail);
        formPanel.add(new JLabel("CCCD:"));         txtCCCD = new JTextField(); formPanel.add(txtCCCD);
        formPanel.add(new JLabel("Địa Chỉ:"));      txtDiaChi = new JTextField(); formPanel.add(txtDiaChi);
        formPanel.add(new JLabel("Tổng Tiền Nợ:")); txtTongTienNo = new JTextField(); formPanel.add(txtTongTienNo);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnLuu = new JButton("Lưu");
        JButton btnHuy = new JButton("Hủy");

        btnLuu.setBackground(new Color(40, 167, 69));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.addActionListener(this::luuKhachHang);

        btnHuy.setBackground(new Color(108, 117, 125));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.addActionListener(e -> dispose());

        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnLuu);
        buttonPanel.add(btnHuy);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void loadKhachHangData() {
        txtMaKH.setText(khachHang.getMaKH());
        txtMaTK.setText(getSafeText(khachHang.getMaTK()));
        txtHoTen.setText(getSafeText(khachHang.getHoTen()));
        txtSDT.setText(getSafeText(khachHang.getSdt()));
        txtEmail.setText(getSafeText(khachHang.getEmail()));
        txtCCCD.setText(getSafeText(khachHang.getCccd()));
        txtDiaChi.setText(getSafeText(khachHang.getDiaChi()));
        txtTongTienNo.setText(String.valueOf(khachHang.getTongTienNo()));
    }

    private String getSafeText(String text) {
        return text != null ? text : "";
    }

    private void luuKhachHang(ActionEvent e) {
        try {
            // Lấy dữ liệu
            String maKH = txtMaKH.getText().trim();
            String maTK = txtMaTK.getText().trim();
            String hoTen = txtHoTen.getText().trim();
            String sdt = txtSDT.getText().trim();
            String email = txtEmail.getText().trim();
            String cccd = txtCCCD.getText().trim();
            String diaChi = txtDiaChi.getText().trim();
            double tongTienNo = parseDouble(txtTongTienNo.getText().trim());

            // Kiểm tra
            if (hoTen.isEmpty()) {
                showError("Họ tên không được để trống", txtHoTen);
                return;
            }
            if (sdt.isEmpty()) {
                showError("Số điện thoại không được để trống", txtSDT);
                return;
            }
            if (!sdt.matches("^0[0-9]{9}$")) {
                showError("SĐT không hợp lệ. Phải bắt đầu bằng 0 và đủ 10 chữ số.", txtSDT);
                return;
            }
            if (!email.isEmpty() && !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                showError("Email không hợp lệ", txtEmail);
                return;
            }
            if (!cccd.isEmpty() && !cccd.matches("^\\d{12}$")) {
                showError("CCCD không hợp lệ. Phải đúng 12 số.", txtCCCD);
                return;
            }

            // Kiểm tra trùng lặp
            if (!sdt.equals(khachHang.getSdt()) && controller.isPhoneNumberExists(sdt, maKH)) {
                showError("SĐT đã tồn tại trong hệ thống", txtSDT);
                return;
            }

            if (!email.isEmpty() && !email.equals(khachHang.getEmail()) && controller.isEmailExists(email, maKH)) {
                showError("Email đã tồn tại trong hệ thống", txtEmail);
                return;
            }

            if (!cccd.isEmpty() && !cccd.equals(khachHang.getCccd()) && controller.isCCCDExists(cccd, maKH)) {
                showError("CCCD đã tồn tại trong hệ thống", txtCCCD);
                return;
            }

            // Cập nhật thông tin
            khachHang.setMaTK(maTK);
            khachHang.setHoTen(hoTen);
            khachHang.setSdt(sdt);
            khachHang.setEmail(email);
            khachHang.setCccd(cccd);
            khachHang.setDiaChi(diaChi);
            khachHang.setTongTienNo(tongTienNo);

            boolean result = controller.updateKhachHang(khachHang);
            if (result) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                showError("Cập nhật thất bại: " + controller.getErrorMessage(), null);
            }

        } catch (Exception ex) {
            showError("Tổng tiền nợ không hợp lệ", txtTongTienNo);
        }
    }

    private double parseDouble(String value) throws ParseException {
        if (value == null || value.isEmpty()) return 0;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            return dinhDangTien.parse(value).doubleValue();
        }
    }

    private void showError(String message, JTextField fieldToFocus) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        if (fieldToFocus != null) fieldToFocus.requestFocus();
    }
}
