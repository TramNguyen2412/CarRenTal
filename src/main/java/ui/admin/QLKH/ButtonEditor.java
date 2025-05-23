package ui.admin.QLKH;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import model.KhachHang;

@SuppressWarnings("serial")
public class ButtonEditor extends DefaultCellEditor {
    protected JPanel panel;
    protected JButton btnView;
    protected JButton btnEdit;
    protected JButton btnDelete;
    private String maKH;
    private KhachHang currentKhachHang;
    private QuanLyKhachHangPanel parentPanel;

    public ButtonEditor(QuanLyKhachHangPanel parentPanel) {
        super(new JCheckBox());
        this.parentPanel = parentPanel;
        // this.isPushed = false; // isPushed is a protected field in DefaultCellEditor

        panel = new JPanel(new GridBagLayout());
        panel.setOpaque(true);

        btnView = createStyledButton("Xem", new Color(23, 162, 184));
        btnEdit = createStyledButton("Sửa", new Color(255, 193, 7));
        btnDelete = createStyledButton("Xóa", new Color(220, 53, 69));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 2, 0, 2);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        panel.add(btnView, gbc);
        panel.add(btnEdit, gbc);
        panel.add(btnDelete, gbc);

        btnView.addActionListener(e -> {
            if (currentKhachHang != null) {
                ChiTietKhachHangDialog dialog = new ChiTietKhachHangDialog(
                        SwingUtilities.getWindowAncestor(parentPanel), currentKhachHang, parentPanel);
                dialog.setVisible(true);
            }
            fireEditingStopped();
        });

        btnEdit.addActionListener(e -> {
            if (currentKhachHang != null) {
                SuaKhachHangDialog dialog = new SuaKhachHangDialog(
                        SwingUtilities.getWindowAncestor(parentPanel), currentKhachHang, parentPanel.getController());
                dialog.setVisible(true);
                if (dialog.isSuccessfullyUpdated()) {
                    parentPanel.loadData();
                }
            }
            fireEditingStopped();
        });

        btnDelete.addActionListener(e -> {
            if (maKH != null) {
                int confirm = JOptionPane.showConfirmDialog(
                        SwingUtilities.getWindowAncestor(parentPanel),
                        "Bạn có chắc chắn muốn xóa khách hàng '"
                                + (currentKhachHang != null ? currentKhachHang.getHoTen() : maKH) + "' (Mã: " + maKH
                                + ")?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    // QuanLyKhachHangPanel.xoaKhachHang will handle showing specific error messages
                    // from service
                    boolean success = parentPanel.xoaKhachHang(maKH);
                    if (success) {
                        JOptionPane.showMessageDialog(
                                SwingUtilities.getWindowAncestor(parentPanel),
                                "Xóa khách hàng thành công!",
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                        parentPanel.loadData();
                    }
                    // Error message is handled by parentPanel.xoaKhachHang
                }
            }
            fireEditingStopped();
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(60, 25));
        return button;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof KhachHang) {
            this.currentKhachHang = (KhachHang) value;
            this.maKH = this.currentKhachHang.getMaKH();
        } else {
            this.currentKhachHang = null;
            this.maKH = null;
            Object maKhFromCol0 = table.getModel().getValueAt(row, 0);
            if (maKhFromCol0 instanceof String) {
                this.maKH = (String) maKhFromCol0;
                // Attempt to get the full KhachHang object if MaKH is found
                if (parentPanel != null && parentPanel.getController() != null) {
                    this.currentKhachHang = parentPanel.getController().getKhachHangByMa(this.maKH);
                }
            }
        }

        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return currentKhachHang != null ? currentKhachHang : maKH;
    }
}
