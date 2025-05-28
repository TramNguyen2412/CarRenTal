package ui.admin.QLTK;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ui.admin.QLXe.XePanel;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JPanel panel;
    private JButton btnEdit;
    private JButton btnDelete;
    private String id;
    private Object parent;
    private JTable table;
    private int row;
    private int column;
    
    public ButtonEditor() {
        this(null);
    }
    
    public ButtonEditor(Object parent) {
        this.parent = parent;
        
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
        
        btnEdit = createButton("Sửa", new Color(255, 193, 7)); // Màu vàng
      
        btnDelete = createButton("Xóa", new Color(244, 67, 54)); // Màu đỏ
        
        btnEdit.setActionCommand("edit");
        btnDelete.setActionCommand("delete");
        
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);
        
        panel.add(btnEdit);
        panel.add(btnDelete);
        
        panel.setOpaque(true);
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(60, 30));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    @Override
    public Object getCellEditorValue() {
        return "";
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        this.row = row;
        this.column = column;
        id = table.getValueAt(row, 0).toString(); // Lấy giá trị ở cột đầu tiên (mã)
        return panel;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("edit".equals(e.getActionCommand())) {
            handleEdit();
        } else if ("delete".equals(e.getActionCommand())) {
            handleDelete();
        }
        fireEditingStopped();
    }
    
    private void handleEdit() {
        if (parent instanceof TaiKhoanPanel) {
            TaiKhoanPanel taiKhoanPanel = (TaiKhoanPanel) parent;
            taiKhoanPanel.showTaiKhoanDialog(taiKhoanPanel.getTaiKhoanById(id));
        }
    }
    
    private void handleDelete() {
        int option = JOptionPane.showConfirmDialog(
            SwingUtilities.getWindowAncestor((Component) parent),
            "Bạn có chắc chắn muốn xóa tài khoản này không?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            boolean success = false;
            
            if (parent instanceof TaiKhoanPanel) {
                TaiKhoanPanel taiKhoanPanel = (TaiKhoanPanel) parent;
                success = taiKhoanPanel.deleteTaiKhoan(id);
                if (success) {
                    taiKhoanPanel.loadDataToTable();
                }
            }
            
            if (success) {
                JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor((Component) parent),
                    "Xóa tài khoản thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor((Component) parent),
                    "Xóa tài khoản thất bại! Tài khoản này có thể đang được sử dụng.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}