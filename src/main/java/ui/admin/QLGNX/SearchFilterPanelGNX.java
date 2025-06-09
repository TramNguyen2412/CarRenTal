package ui.admin.QLGNX;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class SearchFilterPanelGNX extends JPanel {
    private JTextField txtSearch;
    private JComboBox<String> cboTrangThaiFilter;
    private GiaoNhanXePanel parentPanel;

    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    public SearchFilterPanelGNX(GiaoNhanXePanel parent) {
        this.parentPanel = parent;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(BACKGROUND_COLOR);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(LABEL_FONT);
        searchPanel.add(lblSearch);

        txtSearch = new JTextField(25);
        txtSearch.setFont(FIELD_FONT);
        txtSearch.setPreferredSize(new Dimension(250, 32));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204), 1),
                new EmptyBorder(0, 8, 0, 8)));
        txtSearch.setToolTipText("Nhập từ khóa để tìm kiếm (Mã giao nhận, mã hợp đồng, mã xe, nhân viên...)");
        
        // Thêm DocumentListener để tìm kiếm real-time
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                performSearch();
            }
        });
        
        searchPanel.add(txtSearch);

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(LABEL_FONT);
        searchPanel.add(lblTrangThai);

        cboTrangThaiFilter = new JComboBox<>(new String[]{"Tất cả", "Đã giao", "Đã nhận về"});
        cboTrangThaiFilter.setFont(FIELD_FONT);
        cboTrangThaiFilter.setPreferredSize(new Dimension(150, 32));
        cboTrangThaiFilter.addActionListener(e -> performSearch());
        searchPanel.add(cboTrangThaiFilter);

        add(searchPanel, BorderLayout.CENTER);
    }

    private void performSearch() {
        if (parentPanel != null) {
            parentPanel.searchAndFilterGiaoNhan();
        }
    }

    public String getSearchKeyword() {
        return txtSearch.getText().trim();
    }

    public String getSelectedTrangThaiFilter() {
        return (String) cboTrangThaiFilter.getSelectedItem();
    }

    public void resetFilters() {
        txtSearch.setText("");
        cboTrangThaiFilter.setSelectedIndex(0);
    }
}