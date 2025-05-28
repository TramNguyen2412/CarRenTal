package ui.admin.QLTK;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JPanel implements TableCellRenderer {
    private JButton btnEdit, btnDelete;

    public ButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 3, 0));
        setOpaque(true);
        
        btnEdit = createButton("Sửa", new Color(255, 193, 7)); // Màu vàng
        btnDelete = createButton("Xóa", new Color(244, 67, 54)); // Màu đỏ
        
        add(btnEdit);
        add(btnDelete);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(60, 30));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        return btn;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return this;
    }
}