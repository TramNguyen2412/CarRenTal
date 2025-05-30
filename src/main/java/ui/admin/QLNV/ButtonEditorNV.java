package ui.admin.QLNV;

import ui.admin.QLNV.NhanVienPanel;
import model.NhanVien;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class ButtonEditorNV extends DefaultCellEditor {
    private JPanel panel;
    private JButton btnView, btnEdit, btnDelete;
    private String maNV;
    private NhanVienPanel parent;
    
    public ButtonEditorNV(NhanVienPanel parent) {
        super(new JCheckBox());
        this.parent = parent;
        
        panel = new JPanel();
        // Sử dụng GridBagLayout thay vì FlowLayout
        panel.setLayout(new GridBagLayout());
        
        // Tạo và định dạng các nút
        btnView = createStyledButton("Xem", new Color(23, 162, 184));
        btnEdit = createStyledButton("Sửa", new Color(255, 193, 7));
        btnDelete = createStyledButton("Xóa", new Color(220, 53, 69));
        
        // Thiết lập constraints cho GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 2, 0, 2); // Khoảng cách giữa các nút
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Thêm các nút vào panel với căn giữa
        panel.add(btnView, gbc);
        panel.add(btnEdit, gbc);
        panel.add(btnDelete, gbc);
        
        // Xử lý sự kiện cho các nút
        btnView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NhanVien nv = parent.getNhanVienById(maNV);
                if (nv != null) {
                    parent.showNhanVienDetailDialog(nv);
                }
                fireEditingStopped();
            }
        });
        
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NhanVien nv = parent.getNhanVienById(maNV);
                if (nv != null) {
                    parent.showNhanVienDialog(nv);
                }
                fireEditingStopped();
            }
        });
        
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        SwingUtilities.getWindowAncestor(parent),
                        "Bạn có chắc chắn muốn xóa nhân viên này?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                   boolean success = parent.deleteNhanVien(maNV);
                    if (success) {
                        JOptionPane.showMessageDialog(
                                SwingUtilities.getWindowAncestor(parent),
                                "Xóa nhân viên thành công!",
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                        parent.loadDataToTable();
                    } else {
                        JOptionPane.showMessageDialog(
                                SwingUtilities.getWindowAncestor(parent),
                                "Xóa nhân viên thất bại! Nhân viên đang liên quan đến dữ liệu khác.",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
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
        button.setPreferredSize(new Dimension(60, 28));
        return button;
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        maNV = table.getValueAt(row, 0).toString();
        panel.setBackground(table.getSelectionBackground());
        return panel;
    }
    
    @Override
    public Object getCellEditorValue() {
        return "";
    }
}
