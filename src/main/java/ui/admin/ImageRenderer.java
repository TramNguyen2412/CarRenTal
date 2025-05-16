package ui.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ImageRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        
        if (value != null && value instanceof ImageIcon) {
            label.setIcon((ImageIcon) value);
        } else {
            label.setText("No Image");
        }
        
        return label;
    }
}