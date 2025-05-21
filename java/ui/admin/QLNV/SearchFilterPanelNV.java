package ui.admin.QLNV;

import ui.admin.QLNV.NhanVienPanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import model.NhanVien;

@SuppressWarnings("serial")
public class SearchFilterPanelNV extends JPanel {
    private JTextField txtSearch;
    private JComboBox<String> cboFilter;
    private JButton btnRefresh, btnExport;
    private NhanVienPanel parentPanel;

    // Truyền thêm panel cha cùng filterOptions
    public SearchFilterPanelNV(String[] filterOptions, NhanVienPanel parent) {
        this.parentPanel = parent;
        initComponents(filterOptions);
    }

    private void initComponents(String[] filterOptions) {
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setBackground(Color.WHITE);

        // Ô nhập từ khóa tìm kiếm
        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(200, 30));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Lắng nghe phím gõ để thực hiện tìm kiếm
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                timKiem();
            }
        });

        // ComboBox lọc
        cboFilter = new JComboBox<>(filterOptions);
        cboFilter.setPreferredSize(new Dimension(150, 30));

        // Nút “Làm mới” và “Xuất Excel”
        btnRefresh = new JButton("Làm mới");
        styleButton(btnRefresh, new Color(0, 150, 136));
        btnExport = new JButton("Xuất Excel");
        styleButton(btnExport, new Color(113, 85, 156));

        // Thêm vào panel
        add(new JLabel("Tìm kiếm:"));
        add(txtSearch);
        add(new JLabel("Lọc:"));
        add(cboFilter);
        add(btnRefresh);
        add(btnExport);

        txtSearch.setToolTipText("Nhập tên, mã, email hoặc số điện thoại để tìm nhân viên");
    }

    // Tạo style thống nhất cho nút
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
    }

    // Mỗi khi gõ phím xong, thực hiện tìm kiếm
    private void timKiem() {
        String keyword = txtSearch.getText().trim();
        if (parentPanel != null) {
            if (keyword.isEmpty()) {
                // Nếu không nhập gì, tải lại toàn bộ
                parentPanel.loadDataNhanVien();
            } else {
                // Có từ khóa => tìm
                List<NhanVien> dsNV = parentPanel.searchNhanVien(keyword);
                parentPanel.updateNhanVienTable(dsNV);
            }
        }
    }

    // Các phương thức tiện ích thêm
    public String getSearchText() {
        return txtSearch != null ? txtSearch.getText().trim() : "";
    }

    public String getSelectedFilter() {
        return cboFilter != null && cboFilter.getSelectedItem() != null
                ? cboFilter.getSelectedItem().toString()
                : "";
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
        if (txtSearch != null)
            txtSearch.setText("");
        if (cboFilter != null && cboFilter.getItemCount() > 0) {
            cboFilter.setSelectedIndex(0);
        }
    }

    public void updateFilterOptions(String[] options) {
        // Lưu lại tùy chọn đang chọn hiện tại nếu có
        String currentSelection = (cboFilter.getSelectedItem() != null)
                ? cboFilter.getSelectedItem().toString()
                : null;

        cboFilter.removeAllItems();

        // Thêm ít nhất 1 tùy chọn nếu không có
        if (options == null || options.length == 0) {
            cboFilter.addItem("Tất cả");
            cboFilter.setSelectedIndex(0);
            return;
        }

        // Thêm tùy chọn mới
        for (String option : options) {
            cboFilter.addItem(option);
        }

        // Khôi phục tùy chọn
        if (currentSelection != null) {
            for (int i = 0; i < cboFilter.getItemCount(); i++) {
                if (currentSelection.equals(cboFilter.getItemAt(i))) {
                    cboFilter.setSelectedIndex(i);
                    return;
                }
            }
        }

        // Nếu không khớp, đặt về đầu
        if (cboFilter.getItemCount() > 0) {
            cboFilter.setSelectedIndex(0);
        }
    }

    // Đặt placeholder nếu cần
    public void setSearchPlaceholder(String placeholder) {
        txtSearch.putClientProperty("JTextField.placeholderText", placeholder);
    }
}
