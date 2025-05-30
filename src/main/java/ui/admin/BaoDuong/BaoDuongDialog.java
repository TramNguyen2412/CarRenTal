package ui.admin.BaoDuong;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import controller.BaoDuongController;
import model.ChiTietBaoDuong;
import model.DichVuBD;
import model.PhieuBaoDuong;


public class BaoDuongDialog extends JDialog {
    private JTextField txtMaBD;
    private JComboBox<String> cboXe, cboKhachHang, cboNhanVien, cboLoaiBD;
    private JDateChooser dateNgayBD;
    private JButton btnSave, btnCancel;
    private PhieuBaoDuong phieu;
    private BaoDuongPanel parentPanel;
    private BaoDuongController baoDuongController;
    private JTable tableChiTiet;
    private List<ChiTietBaoDuong> chiTietList = new ArrayList<>();
    private JLabel lblTongTien;
    private ChiTietBaoDuongTableModel chiTietTableModel;

    // ...existing code...

public BaoDuongDialog(Window owner, PhieuBaoDuong phieu, BaoDuongPanel parentPanel) {
    super(owner, phieu == null ? "Thêm Phiếu Bảo Dưỡng Mới" : "Cập Nhật Phiếu Bảo Dưỡng", ModalityType.APPLICATION_MODAL);
    this.phieu = phieu;
    this.parentPanel = parentPanel;
    this.baoDuongController = new BaoDuongController();
    setSize(1000, 700);
    setLocationRelativeTo(owner);
    initComponents();
    if (phieu != null) {
        loadPhieuBaoDuong();
    }
}

private void initComponents() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(new EmptyBorder(20, 20, 20, 20));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Mã phiếu
    gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Mã phiếu:"), gbc);
    gbc.gridx = 1; txtMaBD = new JTextField(15); txtMaBD.setEditable(false); panel.add(txtMaBD, gbc);

    // Xe
    gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Xe:"), gbc);
    gbc.gridx = 1; cboXe = new JComboBox<>(); panel.add(cboXe, gbc);

    // Khách hàng
    gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Khách hàng:"), gbc);
    gbc.gridx = 1; cboKhachHang = new JComboBox<>(); panel.add(cboKhachHang, gbc);

    // Nhân viên
    gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Nhân viên:"), gbc);
    gbc.gridx = 1; cboNhanVien = new JComboBox<>(); panel.add(cboNhanVien, gbc);

    // Ngày bảo dưỡng
    gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Ngày bảo dưỡng:"), gbc);
    gbc.gridx = 1; dateNgayBD = new JDateChooser(); panel.add(dateNgayBD, gbc);

    // Loại bảo dưỡng
    gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Loại bảo dưỡng:"), gbc);
    gbc.gridx = 1; cboLoaiBD = new JComboBox<>(new String[]{"Định Kỳ", "Khách gây hư hại"}); panel.add(cboLoaiBD, gbc);

    // Tổng tiền
    gbc.gridx = 0; gbc.gridy = 6; panel.add(new JLabel("Tổng tiền:"), gbc);
    gbc.gridx = 1; lblTongTien = new JLabel("0 VNĐ"); panel.add(lblTongTien, gbc);

    // Bảng chi tiết bảo dưỡng
    gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
    chiTietTableModel = new ChiTietBaoDuongTableModel(chiTietList);
    tableChiTiet = new JTable(chiTietTableModel);
    JScrollPane scrollPane = new JScrollPane(tableChiTiet);
    scrollPane.setPreferredSize(new Dimension(900, 200));
    panel.add(scrollPane, gbc);

    // Nút thêm/sửa/xoá chi tiết
    JPanel pnlChiTietBtn = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton btnThemCT = new JButton("Thêm chi tiết");
    JButton btnSuaCT = new JButton("Sửa chi tiết");
    JButton btnXoaCT = new JButton("Xoá chi tiết");
    pnlChiTietBtn.add(btnThemCT); pnlChiTietBtn.add(btnSuaCT); pnlChiTietBtn.add(btnXoaCT);
    gbc.gridy = 8; gbc.weighty = 0; panel.add(pnlChiTietBtn, gbc);

    // Nút lưu/hủy
    JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    btnSave = new JButton("Lưu"); btnCancel = new JButton("Hủy");
    pnlBtn.add(btnSave); pnlBtn.add(btnCancel);
    gbc.gridy = 9; panel.add(pnlBtn, gbc);

    setContentPane(panel);

    // Sự kiện
    btnThemCT.addActionListener(e -> showEditChiTietDialog(null));
    btnSuaCT.addActionListener(e -> {
        int row = tableChiTiet.getSelectedRow();
        if (row >= 0) showEditChiTietDialog(chiTietList.get(row));
    });
    btnXoaCT.addActionListener(e -> {
        int row = tableChiTiet.getSelectedRow();
        if (row >= 0) {
            chiTietList.remove(row);
            chiTietTableModel.fireTableDataChanged();
            updateTongTien();
        }
    });
    btnSave.addActionListener(e -> savePhieuBaoDuong());
    btnCancel.addActionListener(e -> dispose());
    loadXeToComboBox();
    loadKhachHangToComboBox();
    loadNhanVienToComboBox();
    if (phieu != null) {
        loadPhieuBaoDuong();
        cboXe.setSelectedItem(phieu.getMaXe());
        cboKhachHang.setSelectedItem(phieu.getMaKH());
        cboNhanVien.setSelectedItem(phieu.getMaNV());
        cboLoaiBD.setSelectedItem(phieu.getLoaiBD());
        dateNgayBD.setDate(phieu.getNgayBD());
}
}
private void loadXeToComboBox() {
    cboXe.removeAllItems();
    List<model.Xe> xeList = baoDuongController.getAllXe();
    for (model.Xe xe : xeList) {
        cboXe.addItem(xe.getMaXe()+"-"+xe.getTenXe());
    }
}

private void loadKhachHangToComboBox() {
    cboKhachHang.removeAllItems();
    // Thêm lựa chọn rỗng/null đầu tiên
    cboKhachHang.addItem("Không có"); // hoặc cboKhachHang.addItem("Không chọn");
    List<model.KhachHang> khList = baoDuongController.getAllKhachHang();
    for (model.KhachHang kh : khList) {
        cboKhachHang.addItem(kh.getMaKH() + " - " + kh.getHoTen());
    }
}

private void loadNhanVienToComboBox() {
    cboNhanVien.removeAllItems();
    List<model.NhanVien> nvList = baoDuongController.getAllNhanVien();
    for (model.NhanVien nv : nvList) {
        cboNhanVien.addItem(nv.getMaNV()+ " - " + nv.getHoTen());
    }
}

        private void loadPhieuBaoDuong() {
            txtMaBD.setText(phieu.getMaBD());
            cboXe.setSelectedItem(phieu.getMaXe());
            cboKhachHang.setSelectedItem(phieu.getMaKH());
            cboNhanVien.setSelectedItem(phieu.getMaNV());
            cboLoaiBD.setSelectedItem(phieu.getLoaiBD());
            dateNgayBD.setDate(phieu.getNgayBD());
            chiTietList.clear();
            chiTietList.addAll(baoDuongController.getChiTietByMaBD(phieu.getMaBD()));
            chiTietTableModel.setData(chiTietList); // Đảm bảo TableModel dùng đúng list
            updateTongTien();
        }

        private void showEditChiTietDialog(ChiTietBaoDuong chiTiet) {
            List<DichVuBD> dichVuList = baoDuongController.getAllDichVuBD();
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            ChiTietBaoDuongEditDialog dialog = new ChiTietBaoDuongEditDialog(parentWindow, chiTiet, dichVuList);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                if (chiTiet == null) {
                    chiTietList.add(dialog.getChiTietBaoDuong());
                } else {
                    int idx = chiTietList.indexOf(chiTiet);
                    chiTietList.set(idx, dialog.getChiTietBaoDuong());
                }
                chiTietTableModel.setData(chiTietList); // Luôn cập nhật lại TableModel
                updateTongTien();
            }
        }

private void updateTongTien() {
    double tong = 0;
    for (ChiTietBaoDuong ct : chiTietList) {
        DichVuBD dv = baoDuongController.getDichVuBDById(ct.getMaDV());
        if (dv != null) {
            tong += ct.getSoLuong() * dv.getGiaDV();
        }
    }
    lblTongTien.setText(String.format("%,.0f VNĐ", tong));
}

        private void savePhieuBaoDuong() {
            // Lấy dữ liệu từ form
            String maBD = txtMaBD.getText().trim();
            String maXe = null;
            if (cboXe.getSelectedItem() != null) {
                String xeItem = cboXe.getSelectedItem().toString();
                if (xeItem.contains("-")) {
                    maXe = xeItem.split("-")[0].trim();
                } else {
                    maXe = xeItem.trim();
                }
            }
            String khachHangItem = cboKhachHang.getSelectedItem() != null ? cboKhachHang.getSelectedItem().toString() : null;
            String maKH = null;
            if (khachHangItem != null && khachHangItem.contains(" - ")) {
                maKH = khachHangItem.split(" - ")[0].trim();
            } else {
                maKH = khachHangItem;
            }
            String nhanVienItem = cboNhanVien.getSelectedItem() != null ? cboNhanVien.getSelectedItem().toString() : null;
            String maNV = null;
            if (nhanVienItem != null && nhanVienItem.contains(" - ")) {
                maNV = nhanVienItem.split(" - ")[0].trim();
            } else {
                maNV = nhanVienItem;
            }
            String loaiBD = cboLoaiBD.getSelectedItem() != null ? cboLoaiBD.getSelectedItem().toString() : null;
            java.util.Date ngayBD = dateNgayBD.getDate();
            double tongTien = 0;
            for (ChiTietBaoDuong ct : chiTietList) {
                DichVuBD dv = baoDuongController.getDichVuBDById(ct.getMaDV());
                if (dv != null) {
                    tongTien += ct.getSoLuong() * dv.getGiaDV();
                }
            }

            // Kiểm tra hợp lệ
            if (maXe == null || maXe.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn xe!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (loaiBD == null || loaiBD.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn loại bảo dưỡng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (ngayBD == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bảo dưỡng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (tongTien < 0) {
                JOptionPane.showMessageDialog(this, "Tổng tiền phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

        boolean success = false;
        String result = null;
        if (phieu == null) {
            // Nếu loại bảo dưỡng là Định Kỳ thì mã khách hàng là null
            if ("Định Kỳ".equalsIgnoreCase(loaiBD)) {
                maKH = null;
            }
            result = baoDuongController.addPhieuBaoDuongFull(
                maXe, maKH, ngayBD, maNV, loaiBD, tongTien, chiTietList
            );
        } else {
            // Cập nhật phiếu + chi tiết
            result = baoDuongController.updatePhieuBaoDuongFull(
                maBD, maXe, maKH, ngayBD, maNV, loaiBD, tongTien, chiTietList
            );
        }
        // Chỉ thành công nếu result là mã phiếu (bắt đầu bằng "BD") hoặc chứa "thành công"
        if (result != null && (result.trim().startsWith("BD") || result.toLowerCase().contains("thành công"))) {
            success = true;
        }
        if (success) {
            JOptionPane.showMessageDialog(this, "Lưu phiếu bảo dưỡng thành công!");
            parentPanel.loadDataToTable();
            dispose();
        } else if (result != null && !result.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, result, "Lỗi nghiệp vụ", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Lưu phiếu bảo dưỡng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        }

}
// ...existing code...