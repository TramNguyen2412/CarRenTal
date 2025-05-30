package ui.admin.QLGNX;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SearchFilterPanelGNX extends JPanel {
    private JTextField txtSearch;
    private JComboBox<String> cboTrangThaiGNFilter;
    private JButton btnSearchAction;
    private GiaoNhanXePanel parentPanel; // To call the search/filter method on the parent

    // Consistent styling colors
    private static final Color PRIMARY_COLOR = new Color(41, 121, 255);
    private static final Color TEXT_FIELD_BACKGROUND = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);

    public SearchFilterPanelGNX(GiaoNhanXePanel parentPanel) {
        this.parentPanel = parentPanel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Added some vertical padding
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0, BORDER_COLOR), // Bottom border
            new EmptyBorder(10,5,10,5) // Padding around content
        ));


        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(lblSearch);

        txtSearch = new JTextField(25); // Increased width
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setPreferredSize(new Dimension(250, 32));
        txtSearch.setBackground(TEXT_FIELD_BACKGROUND);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(0, 5, 0, 5) 
        ));
        add(txtSearch);

        JLabel lblFilter = new JLabel("Trạng thái Giao Nhận:");
        lblFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(lblFilter);

        cboTrangThaiGNFilter = new JComboBox<>(new String[]{"Tất cả", "Đã giao", "Đã nhận về"});
        cboTrangThaiGNFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboTrangThaiGNFilter.setPreferredSize(new Dimension(180, 32));
        cboTrangThaiGNFilter.setBackground(Color.WHITE);
        add(cboTrangThaiGNFilter);

        btnSearchAction = new JButton("Lọc/Tìm kiếm");
        styleButton(btnSearchAction, PRIMARY_COLOR, 140, 32);
        btnSearchAction.addActionListener(e -> parentPanel.searchAndFilterGiaoNhan());
        add(btnSearchAction);
    }

    private void styleButton(JButton button, Color color, int width, int height) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(width, height));
        button.setBorder(BorderFactory.createEmptyBorder(5,15,5,15));
    }

    public String getSearchKeyword() {
        return txtSearch.getText().trim();
    }

    public String getSelectedTrangThaiFilter() {
        if (cboTrangThaiGNFilter.getSelectedIndex() == -1) {
            return "Tất cả"; // Default or handle as error
        }
        return cboTrangThaiGNFilter.getSelectedItem().toString();
    }
    
    public void resetFilters() {
        txtSearch.setText("");
        cboTrangThaiGNFilter.setSelectedIndex(0); // Select "Tất cả"
    }
}