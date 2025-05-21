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
        styleButton(btnStats, new Color(76, 175, 80));

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
        statsDialog.setSize(700, 500);
        statsDialog.setLocationRelativeTo(this);
        statsDialog.setLayout(new BorderLayout(10, 10));
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

        int tongSoNhanVien = (int) thongKe.get("tongSoNhanVien");

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

                Map<String, Integer> thongKeTheoChucVu = (Map<String, Integer>) thongKe.get("thongKeTheoChucVu");
                if (thongKeTheoChucVu == null || thongKeTheoChucVu.isEmpty()) {
                    g2d.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 16));
                    g2d.drawString("Không có dữ liệu thống kê theo chức vụ", 50, 100);
                    return;
                }

                int barWidth = 60;
                int spacing = 40;
                int startX = 50;
                int startY = getHeight() - 50;
                int maxHeight = getHeight() - 100;

                // Tìm giá trị lớn nhất để tỷ lệ
                int maxValue = 0;
                for (int value : thongKeTheoChucVu.values()) {
                    if (value > maxValue)
                        maxValue = value;
                }

                // Vẽ trục tọa độ
                g2d.setColor(Color.BLACK);
                g2d.drawLine(startX, startY, startX + (thongKeTheoChucVu.size() * (barWidth + spacing)), startY); // Trục
                                                                                                                  // X
                g2d.drawLine(startX, startY, startX, startY - maxHeight); // Trục Y

                // Vẽ biểu đồ cột
                int x = startX + spacing / 2;
                int colorIndex = 0;
                Color[] colors = {
                        new Color(33, 150, 243), // Blue
                        new Color(76, 175, 80), // Green
                        new Color(255, 152, 0), // Orange
                        new Color(156, 39, 176), // Purple
                        new Color(244, 67, 54) // Red
                };

                for (Map.Entry<String, Integer> entry : thongKeTheoChucVu.entrySet()) {
                    String chucVu = entry.getKey();
                    int value = entry.getValue();

                    // Tính chiều cao cột
                    int barHeight = (int) (((double) value / maxValue) * maxHeight);

                    // Vẽ cột
                    g2d.setColor(colors[colorIndex % colors.length]);
                    g2d.fillRect(x, startY - barHeight, barWidth, barHeight);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, startY - barHeight, barWidth, barHeight);

                    // Vẽ giá trị trên cột
                    g2d.drawString(String.valueOf(value), x + barWidth / 2 - 5, startY - barHeight - 5);

                    // Vẽ nhãn chức vụ
                    g2d.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12));
                    FontMetrics fm = g2d.getFontMetrics();
                    String shortChucVu = chucVu;
                    if (fm.stringWidth(chucVu) > barWidth + spacing) {
                        shortChucVu = chucVu.substring(0, 10) + "...";
                    }

                    // Xoay nhãn để dễ đọc
                    AffineTransform oldTransform = g2d.getTransform();
                    g2d.rotate(-Math.PI / 4, x + barWidth / 2, startY + 5);
                    g2d.drawString(shortChucVu, x + barWidth / 2 - fm.stringWidth(shortChucVu) / 2, startY + 15);
                    g2d.setTransform(oldTransform);

                    x += barWidth + spacing;
                    colorIndex++;
                }

                // Vẽ tiêu đề biểu đồ
                g2d.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
                g2d.drawString("Số lượng nhân viên theo chức vụ", getWidth() / 2 - 120, 30);
            }
        };
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Panel bảng thống kê
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        String[] columnNames = { "Chức vụ", "Số lượng", "Tỷ lệ (%)" };
        Map<String, Integer> thongKeTheoChucVu = (Map<String, Integer>) thongKe.get("thongKeTheoChucVu");
        Object[][] data = new Object[thongKeTheoChucVu.size()][3];

        int i = 0;
        for (Map.Entry<String, Integer> entry : thongKeTheoChucVu.entrySet()) {
            data[i][0] = entry.getKey();
            data[i][1] = entry.getValue();
            data[i][2] = String.format("%.1f", ((double) entry.getValue() / tongSoNhanVien) * 100);
            i++;
        }

        JTable table = new JTable(data, columnNames);
        table.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        table.setRowHeight(25);
        table.setGridColor(new Color(230, 230, 230));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

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
        closeButton.addActionListener(e -> statsDialog.dispose());

        buttonPanel.add(closeButton);

        // Thêm các panel vào dialog
        statsDialog.add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        centerPanel.add(overviewPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chartPanel, tablePanel);
        splitPane.setDividerLocation(250);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);

        centerPanel.add(splitPane, BorderLayout.CENTER);

        statsDialog.add(centerPanel, BorderLayout.CENTER);
        statsDialog.add(buttonPanel, BorderLayout.SOUTH);

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
                        writer.append(val == null ? "" : val.toString());
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
