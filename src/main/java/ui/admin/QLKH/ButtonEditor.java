//package ui.admin.QLKH;
//
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.Cursor;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.awt.Insets;
//
//import javax.swing.DefaultCellEditor;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JTable;
//import javax.swing.SwingUtilities;
//
//import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
//
//import model.KhachHang;
//
//@SuppressWarnings("serial")
//public class ButtonEditor extends DefaultCellEditor {
//    protected JPanel panel;
//    protected JButton btnView;
//    protected JButton btnEdit;
//    protected JButton btnDelete;
//    private String maKH;
//    private KhachHang currentKhachHang;
//    private QuanLyKhachHangPanel parentPanel;
//
//    private JButton createStyledButton(String text, Color bgColor) {
//        JButton button = new JButton(text);
//        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 11)); // Giảm font size
//        button.setForeground(Color.WHITE);
//        button.setBackground(bgColor);
//        button.setFocusPainted(false);
//        button.setBorderPainted(false);
//        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        button.setPreferredSize(new Dimension(50, 28)); // Tăng kích thước
//        button.setMinimumSize(new Dimension(50, 28));
//        button.setMaximumSize(new Dimension(50, 28));
//        button.setMargin(new Insets(2, 4, 2, 4)); // Thêm margin
//        return button;
//    }
//
//    public ButtonEditor(QuanLyKhachHangPanel parentPanel) {
//        super(new JCheckBox());
//        this.parentPanel = parentPanel;
//
//        panel = new JPanel(new GridBagLayout());
//        panel.setOpaque(true);
//
//        btnView = createStyledButton("Xem", new Color(23, 162, 184));
//        btnEdit = createStyledButton("Sửa", new Color(255, 193, 7));
//        btnDelete = createStyledButton("Xóa", new Color(220, 53, 69));
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(2, 1, 2, 1); // Giảm khoảng cách
//        gbc.fill = GridBagConstraints.NONE;
//        gbc.anchor = GridBagConstraints.CENTER;
//
//        panel.add(btnView, gbc);
//        panel.add(btnEdit, gbc);
//        panel.add(btnDelete, gbc);
//
//        btnView.addActionListener(e -> {
//            if (currentKhachHang != null) {
//                ChiTietKhachHangDialog dialog = new ChiTietKhachHangDialog(
//                        SwingUtilities.getWindowAncestor(parentPanel), currentKhachHang, parentPanel);
//                dialog.setVisible(true);
//            }
//            fireEditingStopped();
//        });
//
//        btnEdit.addActionListener(e -> {
//            if (currentKhachHang != null) {
//                SuaKhachHangDialog dialog = new SuaKhachHangDialog(
//                        SwingUtilities.getWindowAncestor(parentPanel), currentKhachHang, parentPanel.getController());
//                dialog.setVisible(true);
//                if (dialog.isSuccessfullyUpdated()) {
//                    parentPanel.loadData();
//                }
//            }
//            fireEditingStopped();
//        });
//
//        btnDelete.addActionListener(e -> {
//            if (currentKhachHang != null) {
//                String maKH = currentKhachHang.getMaKH();
//
//                // Kiểm tra công nợ trước khi hiển thị dialog xác nhận
//                if (currentKhachHang.getTongTienNo() > 0) {
//                    JOptionPane.showMessageDialog(
//                            SwingUtilities.getWindowAncestor(parentPanel),
//                            "Không thể xóa khách hàng '" + currentKhachHang.getHoTen() + "'\n" +
//                                    "vì đang có công nợ: " + String.format("%,.0f", currentKhachHang.getTongTienNo())
//                                    + " VNĐ\n" +
//                                    "Vui lòng thanh toán hết công nợ trước khi xóa.",
//                            "Không thể xóa",
//                            JOptionPane.WARNING_MESSAGE);
//                    return;
//                }
//
//                int confirm = JOptionPane.showConfirmDialog(
//                        SwingUtilities.getWindowAncestor(parentPanel),
//                        "Bạn có chắc chắn muốn xóa khách hàng '" +
//                                currentKhachHang.getHoTen() + "' (Mã: " + maKH + ")?\n" +
//                                "Hành động này không thể hoàn tác!",
//                        "Xác nhận xóa",
//                        JOptionPane.YES_NO_OPTION,
//                        JOptionPane.WARNING_MESSAGE);
//
//                if (confirm == JOptionPane.YES_OPTION) {
//                    boolean success = parentPanel.xoaKhachHang(maKH);
//                    if (success) {
//                        JOptionPane.showMessageDialog(
//                                SwingUtilities.getWindowAncestor(parentPanel),
//                                "Xóa khách hàng thành công!",
//                                "Thông báo",
//                                JOptionPane.INFORMATION_MESSAGE);
//                        parentPanel.loadData();
//                    }
//                    // Nếu không thành công, error message đã được hiển thị trong
//                    // parentPanel.xoaKhachHang
//                }
//            }
//            fireEditingStopped();
//        });
//    }
//
//    @Override
//    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//        if (value instanceof KhachHang) {
//            this.currentKhachHang = (KhachHang) value;
//            this.maKH = this.currentKhachHang.getMaKH();
//        } else {
//            this.currentKhachHang = null;
//            this.maKH = null;
//            Object maKhFromCol0 = table.getModel().getValueAt(row, 0);
//            if (maKhFromCol0 instanceof String) {
//                this.maKH = (String) maKhFromCol0;
//                // Attempt to get the full KhachHang object if MaKH is found
//                if (parentPanel != null && parentPanel.getController() != null) {
//                    this.currentKhachHang = parentPanel.getController().getKhachHangByMa(this.maKH);
//                }
//            }
//        }
//
//        if (isSelected) {
//            panel.setBackground(table.getSelectionBackground());
//        } else {
//            panel.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
//        }
//        return panel;
//    }
//
//    @Override
//    public Object getCellEditorValue() {
//        return currentKhachHang != null ? currentKhachHang : maKH;
//    }
//}

package ui.admin.QLKH;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import model.KhachHang;

@SuppressWarnings("serial")
public class ButtonEditor extends DefaultCellEditor {
    protected JPanel panel;
    protected JButton btnView;
    protected JButton btnEdit;
    protected JButton btnDelete;
    private String maKH;
    private KhachHang currentKhachHang;
    private QuanLyKhachHangPanel parentPanel;
    private boolean isEditing = false;
    private JTable table = null;

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 11)); 
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(50, 28)); 
        button.setMinimumSize(new Dimension(50, 28));
        button.setMaximumSize(new Dimension(50, 28));
        button.setMargin(new Insets(2, 4, 2, 4)); 
        return button;
    }

    public ButtonEditor(QuanLyKhachHangPanel parentPanel) {
        super(new JCheckBox());
        this.parentPanel = parentPanel;

        panel = new JPanel(new GridBagLayout());
        panel.setOpaque(true);

        btnView = createStyledButton("Xem", new Color(23, 162, 184));
        btnEdit = createStyledButton("Sửa", new Color(255, 193, 7));
        btnDelete = createStyledButton("Xóa", new Color(220, 53, 69));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 1, 2, 1); 
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        panel.add(btnView, gbc);
        panel.add(btnEdit, gbc);
        panel.add(btnDelete, gbc);

        btnView.addActionListener(e -> {
            if (currentKhachHang != null) {
                // Trước khi mở dialog, dừng chế độ chỉnh sửa
                SwingUtilities.invokeLater(() -> fireEditingCanceled());
                
                // Sau đó mở dialog
                ChiTietKhachHangDialog dialog = new ChiTietKhachHangDialog(
                        SwingUtilities.getWindowAncestor(parentPanel), currentKhachHang, parentPanel);
                dialog.setVisible(true);
            }
        });

        btnEdit.addActionListener(e -> {
            if (currentKhachHang != null) {
                // Trước khi mở dialog, dừng chế độ chỉnh sửa
                SwingUtilities.invokeLater(() -> fireEditingCanceled());
                
                // Sau đó mở dialog
                SuaKhachHangDialog dialog = new SuaKhachHangDialog(
                        SwingUtilities.getWindowAncestor(parentPanel), currentKhachHang, parentPanel.getController());
                dialog.setVisible(true);
                if (dialog.isSuccessfullyUpdated()) {
                    SwingUtilities.invokeLater(() -> parentPanel.loadData());
                }
            }
        });

        btnDelete.addActionListener(e -> {
            if (currentKhachHang != null) {
                String maKH = currentKhachHang.getMaKH();

                // Trước khi xử lý, dừng chế độ chỉnh sửa
                SwingUtilities.invokeLater(() -> fireEditingCanceled());

                // Kiểm tra công nợ trước khi hiển thị dialog xác nhận
                if (currentKhachHang.getTongTienNo() > 0) {
                    JOptionPane.showMessageDialog(
                            SwingUtilities.getWindowAncestor(parentPanel),
                            "Không thể xóa khách hàng '" + currentKhachHang.getHoTen() + "'\n" +
                                    "vì đang có công nợ: " + String.format("%,.0f", currentKhachHang.getTongTienNo())
                                    + " VNĐ\n" +
                                    "Vui lòng thanh toán hết công nợ trước khi xóa.",
                            "Không thể xóa",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                        SwingUtilities.getWindowAncestor(parentPanel),
                        "Bạn có chắc chắn muốn xóa khách hàng '" +
                                currentKhachHang.getHoTen() + "' (Mã: " + maKH + ")?\n" +
                                "Hành động này không thể hoàn tác!",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = parentPanel.xoaKhachHang(maKH);
                    if (success) {
                        JOptionPane.showMessageDialog(
                                SwingUtilities.getWindowAncestor(parentPanel),
                                "Xóa khách hàng thành công!",
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                        SwingUtilities.invokeLater(() -> parentPanel.loadData());
                    }
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        this.isEditing = true;
        
        if (value instanceof KhachHang) {
            this.currentKhachHang = (KhachHang) value;
            this.maKH = this.currentKhachHang.getMaKH();
        } else {
            this.currentKhachHang = null;
            this.maKH = null;
            
            // Kiểm tra hàng/cột có tồn tại trước khi truy cập
            if (row >= 0 && row < table.getModel().getRowCount() && 
                0 < table.getModel().getColumnCount()) {
                
                Object maKhFromCol0 = table.getModel().getValueAt(row, 0);
                if (maKhFromCol0 instanceof String) {
                    this.maKH = (String) maKhFromCol0;
                    // Attempt to get the full KhachHang object if MaKH is found
                    if (parentPanel != null && parentPanel.getController() != null) {
                        this.currentKhachHang = parentPanel.getController().getKhachHangByMa(this.maKH);
                    }
                }
            }
        }

        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return currentKhachHang != null ? currentKhachHang : maKH;
    }
    
    @Override
    public boolean stopCellEditing() {
        isEditing = false;
        return super.stopCellEditing();
    }
    
    @Override
    public void cancelCellEditing() {
        isEditing = false;
        super.cancelCellEditing();
    }
}

