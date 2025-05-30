package ui.admin.QLKH;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class ButtonRenderer extends JPanel implements TableCellRenderer {

    private JButton btnView, btnEdit, btnDelete;

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 11)); // Giảm font size
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(50, 28)); // Tăng kích thước
        button.setMinimumSize(new Dimension(50, 28));
        button.setMaximumSize(new Dimension(50, 28));
        button.setMargin(new Insets(2, 4, 2, 4)); // Thêm margin
        return button;
    }

    public ButtonRenderer() {
        setLayout(new GridBagLayout());

        btnView = createStyledButton("Xem", new Color(23, 162, 184));
        btnEdit = createStyledButton("Sửa", new Color(255, 193, 7));
        btnDelete = createStyledButton("Xóa", new Color(220, 53, 69));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 1, 2, 1); // Giảm khoảng cách
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        add(btnView, gbc);
        add(btnEdit, gbc);
        add(btnDelete, gbc);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        // Đặt màu nền cho panel dựa trên trạng thái selected
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }

        // Đảm bảo panel có chiều cao phù hợp
        setPreferredSize(new Dimension(getPreferredSize().width, 35));

        return this;
    }
}
