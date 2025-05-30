package ui.admin.QLNV;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import controller.NhanVienController;
import model.NhanVien;
import service.NhanVienService;

public class NhanVienPanel extends JPanel {
    private NhanVienController nhanVienController;
    private NhanVienService nhanVienService;
    private SearchFilterPanelNV searchFilterPanel;
    private NhanVienTablePanel tablePanel;
    private JButton btnAdd;
    private JButton btnStats;

    public NhanVienPanel() {
        nhanVienController = new NhanVienController();
        nhanVienService = new NhanVienService();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // 1. Panel tiêu đề
        JPanel pnlTitle = new JPanel(new BorderLayout());
        pnlTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN");
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));

        pnlTitle.add(lblTitle, BorderLayout.WEST);

        // 2. Tạo SearchFilterPanel với các tùy chọn lọc
        List<String> chucVuList = nhanVienController.getAllChucVu();
        String[] filterOptions;
        if (chucVuList == null || chucVuList.isEmpty()) {
            filterOptions = new String[] { "Tất cả" };
        } else {
            filterOptions = new String[chucVuList.size() + 1];
            filterOptions[0] = "Tất cả";
            for (int i = 0; i < chucVuList.size(); i++) {
                filterOptions[i + 1] = chucVuList.get(i);
            }
        }

        // Tạo mới searchFilterPanel
        searchFilterPanel = new SearchFilterPanelNV(filterOptions, this);
        searchFilterPanel.setSearchPlaceholder("Tìm theo tên, mã, email...");

        pnlTitle.add(searchFilterPanel, BorderLayout.EAST);
        add(pnlTitle, BorderLayout.NORTH);

        // 3. Tạo NhanVienTablePanel
        tablePanel = new NhanVienTablePanel(this);
        add(tablePanel, BorderLayout.CENTER);

        // 4. Panel nút thao tác
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlActions.setOpaque(false);

        btnAdd = new JButton("Thêm nhân viên");
        styleButton(btnAdd, new Color(41, 121, 255));

        btnStats = new JButton("Thống kê");
        styleButton(btnStats, new Color(23, 162, 184));

        pnlActions.add(btnAdd);
        pnlActions.add(btnStats);
        add(pnlActions, BorderLayout.SOUTH);

        // 5. Thêm các sự kiện
        btnAdd.addActionListener(e -> showNhanVienDialog(null));
        btnStats.addActionListener(e -> showThongKeNhanVien());

        searchFilterPanel.addFilterActionListener(e -> filterNhanVien());
        searchFilterPanel.addRefreshActionListener(e -> loadDataToTable());
        searchFilterPanel.addExportActionListener(e -> exportToCsv());
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 35));
    }

    public void loadDataToTable() {
        // Cập nhật lại danh sách chức vụ cho filter trước
        updateChucVuFilter();

        // Sau đó mới lấy dữ liệu và cập nhật bảng
        List<NhanVien> danhSachNhanVien = nhanVienController.getAllNhanVien();
        tablePanel.updateData(danhSachNhanVien);

        // Cuối cùng mới reset filter
        searchFilterPanel.resetFilter();
    }

    private void updateChucVuFilter() {
        List<String> chucVuList = nhanVienController.getAllChucVu();

        // Luôn đảm bảo có ít nhất một tùy chọn "Tất cả"
        String[] filterOptions;
        if (chucVuList == null || chucVuList.isEmpty()) {
            filterOptions = new String[] { "Tất cả" };
        } else {
            filterOptions = new String[chucVuList.size() + 1];
            filterOptions[0] = "Tất cả";
            for (int i = 0; i < chucVuList.size(); i++) {
                filterOptions[i + 1] = chucVuList.get(i);
            }
        }

        searchFilterPanel.updateFilterOptions(filterOptions);
    }

    private void searchNhanVien() {
        String keyword = searchFilterPanel.getSearchText();
        if (keyword.isEmpty()) {
            loadDataToTable();
            return;
        }

        try {
            List<NhanVien> danhSachNhanVien = nhanVienController.searchNhanVien(keyword);
            tablePanel.updateData(danhSachNhanVien);

            // Chỉ hiển thị thông báo khi nhấn nút tìm kiếm, không hiển thị khi tự động tìm
            if (danhSachNhanVien.isEmpty() && !keyword.isEmpty()) {
                // Không hiển thị thông báo khi tự động tìm kiếm
                // JOptionPane.showMessageDialog(this,
                // "Không tìm thấy nhân viên nào phù hợp với từ khóa: " + keyword,
                // "Thông báo",
                // JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tìm kiếm: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void filterNhanVien() {
        String filter = searchFilterPanel.getSelectedFilter();
        if (filter == null) {
            loadDataToTable();
            return;
        }

        if (filter.equals("Tất cả")) {
            loadDataToTable();
            return;
        }

        try {
            List<NhanVien> danhSachNhanVien = nhanVienController.getNhanVienByChucVu(filter);
            tablePanel.updateData(danhSachNhanVien);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lọc dữ liệu: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void showNhanVienDialog(NhanVien nv) {
        NhanVienDialog dialog = new NhanVienDialog(SwingUtilities.getWindowAncestor(this), nv, this);
        dialog.setVisible(true);
    }

    public void showNhanVienDetailDialog(NhanVien nv) {
        NhanVienDetailDialog dialog = new NhanVienDetailDialog(SwingUtilities.getWindowAncestor(this), nv, this);
        dialog.setVisible(true);
    }

    private void exportToExcel() {
        JOptionPane.showMessageDialog(this,
                "Chức năng xuất Excel sẽ được phát triển sau!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Phương thức lấy nhân viên từ DB theo mã
    public NhanVien getNhanVienById(String maNV) {
        return nhanVienController.getNhanVienByMa(maNV);
    }

    public boolean deleteNhanVien(String maNV) {
        boolean success = nhanVienController.deleteNhanVien(maNV);
        if (!success) {
            JOptionPane.showMessageDialog(this,
                    "Không thể xóa nhân viên. Nhân viên có thể đang liên quan đến dữ liệu khác.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
        return success;
    }

    // Phương thức hiển thị thống kê nhân viên
    @SuppressWarnings("unchecked") // For casting thongKe.get("thongKeTheoChucVu")
    public void showThongKeNhanVien() {
        Map<String, Object> thongKe = nhanVienService.getThongKeNhanVien();

        // Tạo dialog thống kê
        JDialog statsDialog;
        Window owner = SwingUtilities.getWindowAncestor(this);
        if (owner != null) {
            statsDialog = new JDialog(owner, "Thống kê nhân viên", Dialog.ModalityType.APPLICATION_MODAL);
        } else {
            statsDialog = new JDialog();
            statsDialog.setTitle("Thống kê nhân viên");
            statsDialog.setModal(true);
        }
        statsDialog.setSize(750, 600); // Adjusted size for better viewing with JSplitPane
        statsDialog.setLocationRelativeTo(this);
        statsDialog.getContentPane().setBackground(Color.WHITE);

        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("THỐNG KÊ NHÂN VIÊN");
        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 150, 243));
        titlePanel.add(titleLabel);

        // Panel thông tin tổng quan
        JPanel overviewPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        overviewPanel.setBackground(Color.WHITE);

        int tongSoNhanVien = (int) thongKe.getOrDefault("tongSoNhanVien", 0);

        JPanel totalPanel = createStatPanel("Tổng số nhân viên", String.valueOf(tongSoNhanVien),
                new Color(33, 150, 243));
        overviewPanel.add(totalPanel);

        // Panel biểu đồ
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Map<String, Integer> thongKeTheoChucVuChart = (Map<String, Integer>) thongKe.get("thongKeTheoChucVu");
                if (thongKeTheoChucVuChart == null || thongKeTheoChucVuChart.isEmpty() || tongSoNhanVien == 0) {
                    g2d.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 16));
                    String msg = "Không có dữ liệu thống kê theo chức vụ";
                    if (tongSoNhanVien == 0 && (thongKeTheoChucVuChart == null || thongKeTheoChucVuChart.isEmpty())) {
                        msg = "Không có nhân viên nào trong hệ thống.";
                    }
                    FontMetrics fm = g2d.getFontMetrics();
                    g2d.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
                    return;
                }

                int barWidth = 60;
                int spacing = 40;
                int startX = 50;
                int startY = getHeight() - 70; // Increased bottom margin for rotated labels
                int maxHeight = getHeight() - 120; // Increased top/bottom margins

                // Tìm giá trị lớn nhất để tỷ lệ
                int maxValue = 0;
                for (int value : thongKeTheoChucVuChart.values()) {
                    if (value > maxValue)
                        maxValue = value;
                }
                if (maxValue == 0)
                    maxValue = 1; // Avoid division by zero if all values are 0 but map is not empty

                // Vẽ trục tọa độ
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawLine(startX, startY, startX, startY - maxHeight); // Trục Y
                g2d.drawLine(startX, startY, getWidth() - startX + spacing, startY); // Trục X

                // Vẽ biểu đồ cột
                int x = startX + spacing / 2;
                int colorIndex = 0;
                Color[] colors = {
                        new Color(33, 150, 243), // Blue
                        new Color(76, 175, 80), // Green
                        new Color(255, 152, 0), // Orange
                        new Color(156, 39, 176), // Purple
                        new Color(244, 67, 54), // Red
                        new Color(0, 150, 136) // Teal
                };

                for (Map.Entry<String, Integer> entry : thongKeTheoChucVuChart.entrySet()) {
                    String chucVu = entry.getKey();
                    int value = entry.getValue();

                    int barHeight = (int) (((double) value / maxValue) * maxHeight);
                    if (barHeight < 1 && value > 0)
                        barHeight = 1; // Ensure visible bar for small non-zero values

                    g2d.setColor(colors[colorIndex % colors.length]);
                    g2d.fillRect(x, startY - barHeight, barWidth, barHeight);
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.drawRect(x, startY - barHeight, barWidth, barHeight);

                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 11));
                    String valStr = String.valueOf(value);
                    FontMetrics fmVal = g2d.getFontMetrics();
                    g2d.drawString(valStr, x + (barWidth - fmVal.stringWidth(valStr)) / 2, startY - barHeight - 5);

                    g2d.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 11));
                    FontMetrics fmLabel = g2d.getFontMetrics();
                    // Rotate label
                    AffineTransform oldTransform = g2d.getTransform();
                    g2d.translate(x + barWidth / 2, startY + 5);
                    g2d.rotate(Math.PI / 4); // Rotate 45 degrees
                    g2d.drawString(chucVu, 0, fmLabel.getAscent() / 2);
                    g2d.setTransform(oldTransform);

                    x += barWidth + spacing;
                    colorIndex++;
                }

                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
                String chartTitle = "Số lượng nhân viên theo chức vụ";
                FontMetrics fmTitle = g2d.getFontMetrics();
                g2d.drawString(chartTitle, (getWidth() - fmTitle.stringWidth(chartTitle)) / 2, 30);
            }
        };
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        chartPanel.setPreferredSize(new Dimension(600, 280)); // Chart panel height

        // Panel bảng thống kê
        JPanel statsTablePanel = new JPanel(new BorderLayout());
        statsTablePanel.setBackground(Color.WHITE);
        statsTablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Chi tiết theo chức vụ",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14),
                new Color(50, 50, 50)));

        String[] columnNames = { "Chức vụ", "Số lượng", "Tỷ lệ (%)" };
        Map<String, Integer> thongKeTheoChucVuTable = (Map<String, Integer>) thongKe.get("thongKeTheoChucVu");
        Object[][] data;
        if (thongKeTheoChucVuTable != null && !thongKeTheoChucVuTable.isEmpty()) {
            data = new Object[thongKeTheoChucVuTable.size()][3];
            int i = 0;
            for (Map.Entry<String, Integer> entry : thongKeTheoChucVuTable.entrySet()) {
                data[i][0] = entry.getKey();
                data[i][1] = entry.getValue();
                data[i][2] = (tongSoNhanVien > 0)
                        ? String.format("%.1f", ((double) entry.getValue() / tongSoNhanVien) * 100)
                        : "N/A";
                i++;
            }
        } else {
            data = new Object[0][3]; // Empty data
        }

        JTable table = new JTable(new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        });
        table.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 13));
        table.setRowHeight(25);
        table.setGridColor(new Color(230, 230, 230));
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        statsTablePanel.add(tableScrollPane, BorderLayout.CENTER);
        statsTablePanel.setPreferredSize(new Dimension(600, 200)); // Table panel area height

        // Panel nút đóng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton closeButton = new JButton("Đóng");
        closeButton.setBackground(new Color(120, 120, 120));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.addActionListener(e -> statsDialog.dispose());

        buttonPanel.add(closeButton);

        // Panel trung tâm chứa JSplitPane
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(overviewPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chartPanel, statsTablePanel);
        splitPane.setDividerLocation(280); // Height for the chartPanel
        splitPane.setResizeWeight(0.0); // Keep top component (chartPanel) height fixed on resize
        splitPane.setBorder(null);
        splitPane.setEnabled(false); // Makes the divider non-movable

        centerPanel.add(splitPane, BorderLayout.CENTER);

        // Panel chính của dialog
        JPanel dialogContentPane = new JPanel(new BorderLayout(10, 10));
        dialogContentPane.setBackground(Color.WHITE);
        dialogContentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dialogContentPane.add(titlePanel, BorderLayout.NORTH);
        dialogContentPane.add(centerPanel, BorderLayout.CENTER);
        dialogContentPane.add(buttonPanel, BorderLayout.SOUTH);

        JScrollPane dialogMainScrollPane = new JScrollPane(dialogContentPane);
        dialogMainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        dialogMainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        dialogMainScrollPane.setBorder(BorderFactory.createEmptyBorder());

        statsDialog.getContentPane().removeAll();
        statsDialog.getContentPane().setLayout(new BorderLayout());
        statsDialog.getContentPane().add(dialogMainScrollPane, BorderLayout.CENTER);

        statsDialog.setVisible(true);
    }

    // Phương thức tạo panel thống kê
    private JPanel createStatPanel(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setPreferredSize(new Dimension(200, 80));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        valueLabel.setForeground(color);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }

    public void loadDataNhanVien() {

        loadDataToTable();
    }

    public List<NhanVien> searchNhanVien(String keyword) {

        return nhanVienController.searchNhanVien(keyword);
    }

    public void updateNhanVienTable(List<NhanVien> dsNV) {

        tablePanel.updateData(dsNV);
    }

    private void exportToCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn nơi lưu file CSV");
        chooser.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));
        chooser.setSelectedFile(new File("DanhSachNhanVien.csv"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String path = file.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".csv")) {
                path += ".csv";
                file = new File(path);
            }
            try (FileWriter writer = new FileWriter(file)) {
                DefaultTableModel model = (DefaultTableModel) tablePanel.getTable().getModel();
                // header
                for (int c = 0; c < model.getColumnCount(); c++) {
                    writer.append(model.getColumnName(c));
                    if (c < model.getColumnCount() - 1)
                        writer.append(",");
                }
                writer.append("\n");
                // rows
                for (int r = 0; r < model.getRowCount(); r++) {
                    for (int c = 0; c < model.getColumnCount(); c++) {
                        Object val = model.getValueAt(r, c);
                        String cellValue = (val == null) ? "" : val.toString();
                        // Escape commas and quotes in cell value
                        if (cellValue.contains(",") || cellValue.contains("\"") || cellValue.contains("\n")) {
                            cellValue = "\"" + cellValue.replace("\"", "\"\"") + "\"";
                        }
                        writer.append(cellValue);
                        if (c < model.getColumnCount() - 1)
                            writer.append(",");
                    }
                    writer.append("\n");
                }
                JOptionPane.showMessageDialog(this,
                        "Xuất danh sách thành công!\nFile được lưu tại: " + path,
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi xuất file: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
