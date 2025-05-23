
package ui.admin;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.ChiTietBaoDuong;
import model.DichVuBD;

public class ChiTietBaoDuongEditDialog extends JDialog {
    private JComboBox<String> cboDichVu;
    private JTextField txtSoLuong;
    private JButton btnOK, btnCancel;
    private ChiTietBaoDuong chiTiet;
    private List<DichVuBD> dichVuList;
    private boolean saved = false;

public ChiTietBaoDuongEditDialog(Window owner, ChiTietBaoDuong chiTiet, List<DichVuBD> dichVuList) {
    super(owner, chiTiet == null ? "Thêm chi tiết" : "Sửa chi tiết", ModalityType.APPLICATION_MODAL);
    if (chiTiet == null) {
        this.chiTiet = null;
    } else {
        this.chiTiet = new ChiTietBaoDuong();
        this.chiTiet.setMaDV(chiTiet.getMaDV());
        this.chiTiet.setSoLuong(chiTiet.getSoLuong());
        // Thêm các thuộc tính khác nếu có
    }
    this.dichVuList = dichVuList;
    initComponents();
    if (chiTiet != null) loadData();
    setSize(500, 250);
    setLocationRelativeTo(owner);
}

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Dịch vụ:"), gbc);
        gbc.gridx = 1;
        cboDichVu = new JComboBox<>();
        for (DichVuBD dv : dichVuList) {
            cboDichVu.addItem(dv.getMaDV() + " - " + dv.getTenDV());
        }
        add(cboDichVu, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 1;
        txtSoLuong = new JTextField();
        add(txtSoLuong, gbc);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnOK = new JButton("OK");
        btnCancel = new JButton("Hủy");
        pnlBtn.add(btnOK); pnlBtn.add(btnCancel);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(pnlBtn, gbc);

        btnOK.addActionListener(e -> onOK());
        btnCancel.addActionListener(e -> dispose());
    }

    private void loadData() {
        for (int i = 0; i < dichVuList.size(); i++) {
            if (dichVuList.get(i).getMaDV().equals(chiTiet.getMaDV())) {
                cboDichVu.setSelectedIndex(i);
                break;
            }
        }
        txtSoLuong.setText(String.valueOf(chiTiet.getSoLuong()));
    }

    private void onOK() {
        int idx = cboDichVu.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Chọn dịch vụ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int soLuong;
        try {
            soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DichVuBD dv = dichVuList.get(idx);
        if (chiTiet == null) chiTiet = new ChiTietBaoDuong();
        chiTiet.setMaDV(dv.getMaDV());
        chiTiet.setSoLuong(soLuong);
        saved = true;
        dispose();
    }

    public ChiTietBaoDuong getChiTietBaoDuong() {
        return chiTiet;
    }

    public boolean isSaved() {
        return saved;
    }
}