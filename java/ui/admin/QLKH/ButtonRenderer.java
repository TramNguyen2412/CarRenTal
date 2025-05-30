
package ui.admin.QLKH;

import java.awt.Color;
import java.awt.Component;
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

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        return this;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
        button.setPreferredSize(new Dimension(70, 28));
        return button;
    }

    public ButtonRenderer() {
        // Thay đổi từ FlowLayout sang GridBagLayout để có thể căn giữa theo chiều dọc
        setLayout(new GridBagLayout());

        // Tạo và định dạng các nút
        btnView = createStyledButton("Xem", new Color(23, 162, 184)); // Teal color
        btnEdit = createStyledButton("Sửa", new Color(255, 193, 7)); // Yellow color
        btnDelete = createStyledButton("Xóa", new Color(220, 53, 69)); // Red color

        // Thiết lập constraints cho GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 2, 0, 2); // Khoảng cách giữa các nút
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        // Thêm các nút vào panel với căn giữa
        add(btnView, gbc);
        add(btnEdit, gbc);
        add(btnDelete, gbc);
    }
}
