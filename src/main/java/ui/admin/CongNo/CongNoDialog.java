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

public class CongNoDialog extends JDialog {
    private JTextField txtMaLichSu, txtSoTien, txtGhiChu;
    private JComboBox<String> cboKhachHang, cboLoaiGD;
    private JDateChooser dateNgayGD;
    private JButton btnSave, btnCancel;
    private LichSuCongNo congNo;
    private CongNoPanel parentPanel;
    private CongNoController congNoController = new CongNoController();

    public CongNoDialog(Window owner, LichSuCongNo congNo, CongNoPanel parentPanel) {
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
        // Reset toàn bộ trường giao diện về đúng dữ liệu gốc từ DB
        txtMaLichSu.setText(congNo.getMaLichSu());
        // Chọn đúng khách hàng
        for (int i = 0; i < cboKhachHang.getItemCount(); i++) {
            String item = cboKhachHang.getItemAt(i).toString();
            if (item.startsWith(congNo.getMaKH() + " ") || item.contains(congNo.getMaKH())) {
                cboKhachHang.setSelectedIndex(i);
                break;
            }
        }
        // Chọn đúng loại giao dịch
        cboLoaiGD.setSelectedItem(congNo.getLoaiGiaoDich());
        // Ngày giao dịch
        dateNgayGD.setDate(congNo.getNgayGiaoDich());
        // Số tiền
        txtSoTien.setText(String.valueOf(congNo.getSoTien()));
        // Ghi chú
        txtGhiChu.setText(congNo.getGhiChu() != null ? congNo.getGhiChu() : "");
    }

    private void saveCongNo() {
        if (cboKhachHang.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (dateNgayGD.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày giao dịch", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String soTienStr = txtSoTien.getText().trim();
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
        String khachHangItem = cboKhachHang.getSelectedItem() != null ? cboKhachHang.getSelectedItem().toString() : null;
        String maKH = null;
        if (khachHangItem != null && khachHangItem.contains(" - ")) {
            maKH = khachHangItem.split(" - ")[0].trim();
        } else {
            maKH = khachHangItem;
        }
        String loaiGD = cboLoaiGD.getSelectedItem().toString();
        Date ngayGD = dateNgayGD.getDate();
        String ghiChu = txtGhiChu.getText().trim();

        String result;
        if (congNo == null) {
            result = congNoController.addLichSuCongNo(maKH, ngayGD, loaiGD, soTien, ghiChu);
        } else {
            // So sánh trường chính: số tiền, loại GD, ngày GD, ghi chú
            boolean truongChinhThayDoi = false;
            if (congNo.getSoTien() != soTien || !congNo.getLoaiGiaoDich().equals(loaiGD)) {
                truongChinhThayDoi = true;
            }
            if (!truongChinhThayDoi) {
                // Chỉ update thông tin chung (không ảnh hưởng tổng nợ)
                result = congNoController.updateLichSuCongNoThongTinChung(congNo.getMaLichSu(), maKH, ngayGD, ghiChu);
            } else {
                // Cập nhật toàn bộ (có ảnh hưởng tổng nợ)
                result = congNoController.updateLichSuCongNo(congNo.getMaLichSu(), maKH, ngayGD, loaiGD, soTien, ghiChu);
            }
        }
        JOptionPane.showMessageDialog(this, result);
        if (result.contains("thành công")) {
            parentPanel.loadCongNoData();
            parentPanel.loadKhachHangData();
            dispose();
        }
    }
}
