package ui.admin.QLKH;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

@SuppressWarnings("serial")
public class SearchFilterPanelKH extends JPanel {
    private JTextField txtSearch;
    private JComboBox<String> cboFilter;
    private JButton btnRefresh, btnExportCSV; // Renamed from btnXuatExcel to btnExportCSV for clarity
    // private QuanLyKhachHangPanel parentPanel; // If direct interaction is needed

    // public SearchFilterPanelKH(String[] filterOptions, QuanLyKhachHangPanel
    // parentPanel) {
    public SearchFilterPanelKH(String[] filterOptions) {
        // this.parentPanel = parentPanel;
        initComponents(filterOptions);
    }

    private void initComponents(String[] filterOptions) {
        setLayout(new FlowLayout(FlowLayout.RIGHT)); // Align components to the right
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(5, 10, 5, 10));

        // Ô nhập từ khóa tìm kiếm
        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(200, 30));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        txtSearch.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));

        // ComboBox lọc
        cboFilter = new JComboBox<>(filterOptions != null ? filterOptions : new String[] { "Tất cả" });
        cboFilter.setPreferredSize(new Dimension(150, 30));
        cboFilter.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));

        // Nút “Làm mới” và “Xuất CSV”
        btnRefresh = new JButton("Làm mới");
        styleButton(btnRefresh, new Color(0, 150, 136)); // Greenish color like QLNV
        btnExportCSV = new JButton("Xuất Excel"); // Changed text
        styleButton(btnExportCSV, new Color(113, 85, 156)); // Purplish color like QLNV

        // Thêm vào panel
        add(createLabel("Tìm kiếm:"));
        add(txtSearch);
        add(createLabel("Lọc theo:"));
        add(cboFilter);
        add(btnRefresh);
        add(btnExportCSV);

        txtSearch.setToolTipText("Nhập mã, tên, SĐT, email hoặc CCCD để tìm khách hàng");
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        return lbl;
    }

    private void styleButton(JButton b, Color bg) {
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(120, 35)); // Consistent button size
    }

    public void addSearchKeyListener(KeyListener l) {
        txtSearch.addKeyListener(l);
    }

    public void addFilterActionListener(ActionListener l) {
        cboFilter.addActionListener(l);
    }

    public void addRefreshActionListener(ActionListener l) {
        btnRefresh.addActionListener(l);
    }

    public void addExportCSVActionListener(ActionListener l) { // New method for CSV export
        btnExportCSV.addActionListener(l);
    }

    public String getSearchText() {
        return txtSearch != null ? txtSearch.getText().trim() : "";
    }

    public String getSelectedFilter() {
        return cboFilter != null && cboFilter.getSelectedItem() != null
                ? cboFilter.getSelectedItem().toString()
                : "Tất cả"; // Default to "Tất cả"
    }

    public void resetFilter() {
        if (cboFilter.getItemCount() > 0) {
            cboFilter.setSelectedIndex(0);
        }
        txtSearch.setText("");
    }

    public void updateFilterOptions(String[] options) {
        String currentSelection = (cboFilter.getSelectedItem() != null)
                ? cboFilter.getSelectedItem().toString()
                : null;

        cboFilter.removeAllItems();

        if (options == null || options.length == 0) {
            cboFilter.addItem("Tất cả");
        } else {
            for (String option : options) {
                cboFilter.addItem(option);
            }
        }

        if (currentSelection != null) {
            for (int i = 0; i < cboFilter.getItemCount(); i++) {
                if (currentSelection.equals(cboFilter.getItemAt(i))) {
                    cboFilter.setSelectedIndex(i);
                    return;
                }
            }
        }

        if (cboFilter.getItemCount() > 0) {
            cboFilter.setSelectedIndex(0); // Default to first item if previous selection not found
        }
    }

    public void setSearchPlaceholder(String placeholder) {
        txtSearch.putClientProperty("JTextField.placeholderText", placeholder);
    }
}