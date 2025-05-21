package ui.admin.QLKH;

import model.KhachHang;
import controller.KhachHangController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class ButtonEditor extends DefaultCellEditor {
    private JPanel panel;
    private JButton btnView, btnEdit, btnDelete;
    private String maKH;
    private QuanLyKhachHangPanel parent;
    
    public ButtonEditor(QuanLyKhachHangPanel parent) {
        super(new JCheckBox());
        this.parent = parent;
        
        panel = new JPanel();
        // Sử dụng GridBagLayout thay vì FlowLayout
        panel.setLayout(new GridBagLayout());
        
        // Tạo và định dạng các nút
        btnView = createStyledButton("Xem", new Color(23, 162, 184));  // Teal color
        btnEdit = createStyledButton("Sửa", new Color(255, 193, 7));   // Yellow color
        btnDelete = createStyledButton("Xóa", new Color(220, 53, 69)); // Red color
        
        // Thiết lập constraints cho GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 2, 0, 2); // Khoảng cách giữa các nút
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Thêm các nút vào panel với căn giữa
        panel.add(btnView, gbc);
        panel.add(btnEdit, gbc);
        panel.add(btnDelete, gbc);
        
        // Xử lý sự kiện cho nút Xem
        btnView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KhachHang kh = parent.getKhachHangById(maKH);
                if (kh != null) {
                    parent.xemKhachHang(maKH);
                }
                fireEditingStopped();
            }
        });
        
        // Xử lý sự kiện cho nút Sửa
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KhachHang kh = parent.getKhachHangById(maKH);
                if (kh != null) {
                    parent.suaKhachHang(maKH);
                }
                fireEditingStopped();
            }
        });
        
        // Xử lý sự kiện cho nút Xóa
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        SwingUtilities.getWindowAncestor(parent),
                        "Bạn có chắc chắn muốn xóa khách hàng này?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    parent.xoaKhachHang(maKH);
                    // No need to check return value since xoaKhachHang handles the success/failure messages
                    JOptionPane.showMessageDialog(
                            SwingUtilities.getWindowAncestor(parent),
                            "Xóa khách hàng thành công!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                    // Reload data through parent
                    ((QuanLyKhachHangPanel)parent).actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "refresh"));
                }
                fireEditingStopped();
            }
        });
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(70, 28));
        return button;
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        maKH = table.getValueAt(row, 0).toString();
        panel.setBackground(table.getSelectionBackground());
        return panel;
    }
    
    @Override
    public Object getCellEditorValue() {
        return "";
    }
}
