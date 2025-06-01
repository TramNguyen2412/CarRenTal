package ui.admin.BaoDuong;

import ui.admin.CTBD.DichVuBDPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import controller.DichVuBDController;
import model.DichVuBD;

public class ButtonEditorDichVuBD extends DefaultCellEditor {
    private JPanel panel;
    private JButton btnEdit, btnDelete;
    private String maDV;
    private DichVuBDPanel parent;
    private DichVuBDController dichVuBDController;

    public ButtonEditorDichVuBD(DichVuBDPanel parent) {
        super(new JCheckBox());
        this.parent = parent;
        this.dichVuBDController = new DichVuBDController();
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        btnEdit = createStyledButton("Sửa", new Color(255, 193, 7));
        btnDelete = createStyledButton("Xóa", new Color(220, 53, 69));
        panel.add(btnEdit);
        panel.add(btnDelete);

        btnEdit.addActionListener((ActionEvent e) -> {
            DichVuBD dichVu = dichVuBDController.getDichVuBDById(maDV);
            if (dichVu != null) {
                parent.showDichVuDialog(dichVu);
            }
            fireEditingStopped();
        });

        btnDelete.addActionListener((ActionEvent e) -> {
            int confirm = JOptionPane.showConfirmDialog(
                    SwingUtilities.getWindowAncestor(parent),
                    "Bạn có chắc chắn muốn xóa dịch vụ này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String result = dichVuBDController.deleteDichVuBD(maDV);
                JOptionPane.showMessageDialog(
                        SwingUtilities.getWindowAncestor(parent),
                        result,
                        "Thông báo",
                        result.contains("thành công") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                parent.loadDataToTable();
            }
            fireEditingStopped();
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(60, 28));
        return button;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        maDV = table.getValueAt(row, 0).toString();
        panel.setBackground(table.getSelectionBackground());
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}
