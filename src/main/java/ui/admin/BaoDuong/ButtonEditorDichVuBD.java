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

//
//
//package ui.admin.BaoDuong;
//
//import ui.admin.CTBD.DichVuBDPanel;
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.Cursor;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.Font;
//import java.awt.event.ActionEvent;
//
//import javax.swing.DefaultCellEditor;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JTable;
//import javax.swing.SwingUtilities;
//
//import controller.DichVuBDController;
//import model.DichVuBD;
//
//public class ButtonEditorDichVuBD extends DefaultCellEditor {
//    private JPanel panel;
//    private JButton btnEdit, btnDelete;
//    private String maDV;
//    private DichVuBDPanel parent;
//    private DichVuBDController dichVuBDController;
//
//    public ButtonEditorDichVuBD(DichVuBDPanel parent) {
//        super(new JCheckBox());
//        this.parent = parent;
//        this.dichVuBDController = new DichVuBDController();
//        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
//        btnEdit = createStyledButton("Sửa", new Color(255, 193, 7));
//        btnDelete = createStyledButton("Xóa", new Color(220, 53, 69));
//        panel.add(btnEdit);
//        panel.add(btnDelete);
//
//        btnEdit.addActionListener((ActionEvent e) -> {
//            // Hủy chế độ chỉnh sửa trước khi mở dialog
//            SwingUtilities.invokeLater(() -> fireEditingCanceled());
//            
//            if (maDV != null && !maDV.isEmpty()) {
//                DichVuBD dichVu = dichVuBDController.getDichVuBDById(maDV);
//                if (dichVu != null) {
//                    parent.showDichVuDialog(dichVu);
//                }
//            }
//        });
//
//        btnDelete.addActionListener((ActionEvent e) -> {
//            // Hủy chế độ chỉnh sửa trước khi hiển thị dialog
//            SwingUtilities.invokeLater(() -> fireEditingCanceled());
//            
//            if (maDV != null && !maDV.isEmpty()) {
//                int confirm = JOptionPane.showConfirmDialog(
//                        SwingUtilities.getWindowAncestor(parent),
//                        "Bạn có chắc chắn muốn xóa dịch vụ này?",
//                        "Xác nhận xóa",
//                        JOptionPane.YES_NO_OPTION);
//                if (confirm == JOptionPane.YES_OPTION) {
//                    // Thực hiện xóa và cập nhật UI sau đó
//                    SwingUtilities.invokeLater(() -> {
//                        String result = dichVuBDController.deleteDichVuBD(maDV);
//                        JOptionPane.showMessageDialog(
//                                SwingUtilities.getWindowAncestor(parent),
//                                result,
//                                "Thông báo",
//                                result.contains("thành công") ? 
//                                    JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
//                        parent.loadDataToTable();
//                    });
//                }
//            }
//        });
//    }
//
//    private JButton createStyledButton(String text, Color bgColor) {
//        JButton button = new JButton(text);
//        button.setBackground(bgColor);
//        button.setForeground(Color.WHITE);
//        button.setFocusPainted(false);
//        button.setBorderPainted(false);
//        button.setFont(new Font("Arial", Font.BOLD, 12));
//        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        button.setPreferredSize(new Dimension(60, 28));
//        return button;
//    }
//
//    @Override
//    public Component getTableCellEditorComponent(JTable table, Object value,
//            boolean isSelected, int row, int column) {
//        // Kiểm tra giới hạn trước khi truy cập
//        maDV = null;
//        if (table != null && row >= 0 && row < table.getRowCount() && 
//            0 < table.getColumnCount()) {
//            try {
//                Object maDVObj = table.getValueAt(row, 0);
//                if (maDVObj != null) {
//                    maDV = maDVObj.toString();
//                }
//            } catch (Exception ex) {
//                System.err.println("Lỗi khi lấy mã dịch vụ: " + ex.getMessage());
//            }
//        }
//        
//        panel.setBackground(isSelected ? table.getSelectionBackground() : 
//            (row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248)));
//        return panel;
//    }
//
//    @Override
//    public Object getCellEditorValue() {
//        return "";
//    }
//    
//    // Thêm phương thức này để đảm bảo rằng quá trình chỉnh sửa kết thúc đúng cách
//    @Override
//    public boolean stopCellEditing() {
//        return super.stopCellEditing();
//    }
//    
//    @Override
//    public void cancelCellEditing() {
//        super.cancelCellEditing();
//    }
//}