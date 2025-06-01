package ui.admin.QLXe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class SearchFilterPanel extends JPanel {
    private JTextField txtSearch;
    private JComboBox<String> cboFilter;
    private JButton btnRefresh, btnExport;
    
    public SearchFilterPanel() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setBackground(Color.WHITE); // Màu nền xanh nhạt
        
        // Khởi tạo components
        txtSearch = new JTextField(20);
        cboFilter = new JComboBox<>(new String[]{"Tất cả", "Đang thuê", "Sẵn sàng", "Bảo dưỡng"});
        btnRefresh = new JButton("Làm mới");
        btnExport = new JButton("Xuất Excel");
        
        // Style các components
        txtSearch.setPreferredSize(new Dimension(200, 30));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        cboFilter.setPreferredSize(new Dimension(150, 30));
        
        styleButton(btnRefresh, new Color(0, 150, 136));
        styleButton(btnExport, new Color(113, 85, 156));
        
        // Thêm components vào panel
        add(new JLabel("Tìm kiếm:"));
        add(txtSearch);
        add(new JLabel("Lọc:"));
        add(cboFilter);
        add(btnRefresh);
        add(btnExport);
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
    }
    
    // Getter và setter
    public String getSearchText() {
        return txtSearch.getText().trim();
    }
    
    public String getSelectedFilter() {
        return (String) cboFilter.getSelectedItem();
    }
    
    // Phương thức để thêm sự kiện
    public void addSearchActionListener(ActionListener listener) {
        txtSearch.addActionListener(listener);
    }
    
    public void addFilterActionListener(ActionListener listener) {
        cboFilter.addActionListener(listener);
    }
    
    public void addRefreshActionListener(ActionListener listener) {
        btnRefresh.addActionListener(listener);
    }
    
    public void addExportActionListener(ActionListener listener) {
        btnExport.addActionListener(listener);
    }
    
    public void resetFilter() {
        txtSearch.setText("");
        cboFilter.setSelectedIndex(0);
    }
}