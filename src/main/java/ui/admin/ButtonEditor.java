package ui.admin;

import ui.admin.QLXe.XePanel;
import controller.XeController;
import model.Xe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
public class ButtonEditor extends DefaultCellEditor {
    private JPanel panel;
    private JButton btnView, btnEdit, btnDelete;
    private String maXe;
    private XePanel parent;
  //  private XeController xeController;
    
    public ButtonEditor(XePanel parent) {
        super(new JCheckBox());
        this.parent = parent;
      //  this.xeController = new XeController();
        
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
        
        // Giữ nguyên code xử lý sự kiện cho các nút
        btnView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // Xe xe = xeController.getXeByMa(maXe);
                Xe xe = parent.getXeById(maXe);
                if (xe != null) {
                    parent.showXeDetailDialog(xe);
                }
                fireEditingStopped();
            }
        });
        
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // Xe xe = xeController.getXeByMa(maXe);
                Xe xe = parent.getXeById(maXe);
                if (xe != null) {
                    parent.showXeDialog(xe);
                }
                fireEditingStopped();
            }
        });
        
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        SwingUtilities.getWindowAncestor(parent),
                        "Bạn có chắc chắn muốn xóa xe này?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                   // boolean success = xeController.deleteXe(maXe);
                   boolean success = parent.deleteXe(maXe);
                    if (success) {
                        JOptionPane.showMessageDialog(
                                SwingUtilities.getWindowAncestor(parent),
                                "Xóa xe thành công!",
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                        parent.loadDataToTable();
                    } else {
                        JOptionPane.showMessageDialog(
                                SwingUtilities.getWindowAncestor(parent),
                                "Xóa xe thất bại! Xe đang được sử dụng.",
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
        maXe = table.getValueAt(row, 0).toString();
        panel.setBackground(table.getSelectionBackground());
        return panel;
    }
    
    @Override
    public Object getCellEditorValue() {
        return "";
    }
}