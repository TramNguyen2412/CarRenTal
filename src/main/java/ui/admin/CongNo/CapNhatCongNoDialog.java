package ui.admin.CongNo;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import controller.CongNoController;
import controller.KhachHangController;
import model.KhachHang;
import model.LichSuCongNo;

public class CapNhatCongNoDialog extends JDialog {
    private JTextField txtMaLichSu, txtSoTien, txtGhiChu;
    private JComboBox<String> cboKhachHang, cboLoaiGD;
    private JDateChooser dateNgayGD;
    private JButton btnSave, btnCancel;
    private LichSuCongNo congNo;
    private CongNoPanel parentPanel;
    private CongNoController congNoController = new CongNoController();

    public CapNhatCongNoDialog(Window owner, LichSuCongNo congNo, CongNoPanel parentPanel) {
        super(owner, congNo == null ? "Thêm Giao Dịch Công Nợ Mới" : "Cập Nhật Giao Dịch Công Nợ", ModalityType.APPLICATION_MODAL);
        this.congNo = congNo;
        this.parentPanel = parentPanel;
        initComponents();
        loadComboBoxData();
        if (congNo != null) {
            loadCongNoData();
        }
        setSize(500, 400);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Mã lịch sử
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Mã lịch sử:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtMaLichSu = new JTextField(20); txtMaLichSu.setEditable(false);
        panel.add(txtMaLichSu, gbc);

        // Khách hàng
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Khách hàng:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        cboKhachHang = new JComboBox<>();
        panel.add(cboKhachHang, gbc);

        // Ngày giao dịch
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("Ngày giao dịch:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        dateNgayGD = new JDateChooser();
        dateNgayGD.setDate(new Date());
        panel.add(dateNgayGD, gbc);

        // Loại giao dịch
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panel.add(new JLabel("Loại giao dịch:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        cboLoaiGD = new JComboBox<>(new String[]{"PHAT SINH", "THANH TOAN"});
        panel.add(cboLoaiGD, gbc);

        // Số tiền
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        panel.add(new JLabel("Số tiền:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        txtSoTien = new JTextField(20);
        panel.add(txtSoTien, gbc);

        // Ghi chú
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        panel.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 1.0;
        txtGhiChu = new JTextField(20);
        panel.add(txtGhiChu, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        styleButton(btnSave, new Color(41, 121, 255));
        styleButton(btnCancel, new Color(150, 150, 150));
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.weightx = 1.0;
        panel.add(buttonPanel, gbc);

        btnSave.addActionListener(e -> saveCongNo());
        btnCancel.addActionListener(e -> dispose());
        getContentPane().add(panel);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    private void loadComboBoxData() {
        cboKhachHang.removeAllItems();
        KhachHangController khachHangController = new KhachHangController();
        List<KhachHang> danhSachKH = khachHangController.getAllKhachHang();
        for (KhachHang kh : danhSachKH) {
            cboKhachHang.addItem(kh.getMaKH() + " - " + kh.getHoTen());
        }
    }

    private void loadCongNoData() {
        txtMaLichSu.setText(congNo.getMaLichSu());
        for (int i = 0; i < cboKhachHang.getItemCount(); i++) {
            if (cboKhachHang.getItemAt(i).toString().contains(congNo.getMaKH())) {
                cboKhachHang.setSelectedIndex(i);
                break;
            }
        }
        dateNgayGD.setDate(congNo.getNgayGiaoDich());
        cboLoaiGD.setSelectedItem(congNo.getLoaiGiaoDich());
        txtSoTien.setText(String.valueOf(congNo.getSoTien()));
        txtGhiChu.setText(congNo.getGhiChu());
    }

    private void saveCongNo() {

        // Lấy dữ liệu từ form
        String khachHangItem = cboKhachHang.getSelectedItem() != null ? cboKhachHang.getSelectedItem().toString() : null;
        String maKH = null;
        if (khachHangItem != null && khachHangItem.contains(" - ")) {
            maKH = khachHangItem.split(" - ")[0].trim();
        } else {
            maKH = khachHangItem;
        }
        String loaiGD = cboLoaiGD.getSelectedItem() != null ? cboLoaiGD.getSelectedItem().toString() : null;
        Date ngayGD = dateNgayGD.getDate();
        String soTienStr = txtSoTien.getText().trim();
        String ghiChu = txtGhiChu.getText().trim();

        // Validate dữ liệu
        if (maKH == null || maKH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (ngayGD == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày giao dịch", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (soTienStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double soTien;
        try {
            soTien = Double.parseDouble(soTienStr);
            if (soTien <= 0) {
                JOptionPane.showMessageDialog(this, "Số tiền phải lớn hơn 0", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = null;
        boolean success = false;
        if (congNo != null) {
            // Lấy dữ liệu gốc từ DB để so sánh
            LichSuCongNo congNoGoc = congNoController.getLichSuCongNoByMa(congNo.getMaLichSu());
            boolean truongChinhThayDoi = false;
            if (congNoGoc != null) {
                // Chỉ so sánh số tiền và loại giao dịch để quyết định cập nhật tổng tiền
                if (congNoGoc.getSoTien() != soTien || !congNoGoc.getLoaiGiaoDich().equals(loaiGD)) {
                    truongChinhThayDoi = true;
                }
            }
            if (!truongChinhThayDoi) {
                // Chỉ cập nhật thông tin chung (khách hàng, ngày GD, ghi chú, loại GD)
                result = congNoController.updateLichSuCongNoThongTinChung(congNo.getMaLichSu(), maKH, ngayGD, ghiChu);
            } else {
                // Cập nhật toàn bộ (bao gồm tổng tiền)
                result = congNoController.updateLichSuCongNo(congNo.getMaLichSu(), maKH, ngayGD, loaiGD, soTien, ghiChu);
            }
            if (result != null && result.toLowerCase().contains("thành công")) {
                success = true;
            }
        } else {
            // Thêm mới
            result = congNoController.addLichSuCongNo(maKH, ngayGD, loaiGD, soTien, ghiChu);
            if (result != null && result.toLowerCase().contains("thành công")) {
                success = true;
            }
        }
        if (success) {
            JOptionPane.showMessageDialog(this, result);
            parentPanel.loadCongNoData();
            parentPanel.loadKhachHangData();
            dispose();
        } else if (result != null && !result.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, result, "Lỗi nghiệp vụ", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật công nợ thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    // So sánh ngày (bỏ qua giờ/phút/giây nếu cần)
    private boolean sameDate(Date d1, Date d2) {
        if (d1 == null && d2 == null) return true;
        if (d1 == null || d2 == null) return false;
        return d1.getTime() == d2.getTime();
    }
    private boolean safeEquals(String s1, String s2) {
        if (s1 == null && s2 == null) return true;
        if (s1 == null || s2 == null) return false;
        return s1.trim().equals(s2.trim());
    }
}
