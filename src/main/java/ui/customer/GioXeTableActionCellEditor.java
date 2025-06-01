package ui.customer;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GioXeTableActionCellEditor extends DefaultCellEditor {
    private JPanel panel;
    private JButton viewButton;
    private JButton deleteButton;
    private int row;
    private GioXePanel gioXePanel;
    
    public GioXeTableActionCellEditor(GioXePanel gioXePanel) {
        super(new JCheckBox());
        this.gioXePanel = gioXePanel;
        
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setOpaque(true);
        
        viewButton = new JButton("Chi tiết");
        viewButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
        viewButton.setBackground(new Color(0, 120, 215));
        viewButton.setForeground(Color.WHITE);
        viewButton.setFocusPainted(false);
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
                gioXePanel.viewCarDetails(row);
            }
        });
        
        deleteButton = new JButton("Xóa");
        deleteButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
        deleteButton.setBackground(new Color(255, 100, 100));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
                gioXePanel.removeFromCart(row);
            }
        });
        
        panel.add(viewButton);
        panel.add(deleteButton);
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.row = row;
        panel.setBackground(table.getSelectionBackground());
        return panel;
    }
    
    @Override
    public Object getCellEditorValue() {
        return "Thao tác";
    }
    
    @Override
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }
    
    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}