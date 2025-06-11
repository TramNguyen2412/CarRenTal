package ui.admin.QLXe;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class ButtonRenderer extends JPanel implements TableCellRenderer {
    
    private JButton btnView, btnEdit, btnDelete;
    
    public ButtonRenderer() {
        // Thay đổi từ FlowLayout sang GridBagLayout để có thể căn giữa theo chiều dọc
        setLayout(new GridBagLayout());
        
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
        add(btnView, gbc);
        add(btnEdit, gbc);
        add(btnDelete, gbc);
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
        button.setPreferredSize(new Dimension(60, 28));
        return button;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        // Thiết lập màu nền dựa trên trạng thái được chọn và dòng chẵn/lẻ
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
        }
        return this;
    }
}
