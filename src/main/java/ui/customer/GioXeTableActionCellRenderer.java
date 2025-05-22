package ui.customer;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class GioXeTableActionCellRenderer extends DefaultTableCellRenderer {
    private JPanel panel;
    private JButton viewButton;
    private JButton deleteButton;
    
    public GioXeTableActionCellRenderer() {
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setOpaque(true);
        
        viewButton = new JButton("Chi tiết");
        viewButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
        viewButton.setBackground(new Color(0, 120, 215));
        viewButton.setForeground(Color.WHITE);
        viewButton.setFocusPainted(false);
        
        deleteButton = new JButton("Xóa");
        deleteButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
        deleteButton.setBackground(new Color(255, 100, 100));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        
        panel.add(viewButton);
        panel.add(deleteButton);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
        }
        
        return panel;
    }
}