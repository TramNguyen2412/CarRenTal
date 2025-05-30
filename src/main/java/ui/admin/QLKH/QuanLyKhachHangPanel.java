package ui.admin.QLKH;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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

import controller.KhachHangController;
import model.KhachHang;

@SuppressWarnings("serial")
public class QuanLyKhachHangPanel extends JPanel implements ActionListener {
    private final KhachHangController controller;
    private KhachHangTablePanel tablePanel;
    private SearchFilterPanelKH searchFilterPanel = null;
    private JButton btnThemKH, btnThongKeKH;
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public QuanLyKhachHangPanel() {
        this.controller = new KhachHangController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        initComponents();
        loadData();
    }

    private void initComponents() {
        // 1. Panel tiêu đề và tìm kiếm/lọc
        JPanel pnlTitleAndSearch = new JPanel(new BorderLayout());
        pnlTitleAndSearch.setBorder(new EmptyBorder(0, 0, 10, 0));
        pnlTitleAndSearch.setOpaque(false);

        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        lblTitle.setForeground(new Color(50, 50, 50));
        pnlTitleAndSearch.add(lblTitle, BorderLayout.WEST);

        String[] filterOptions = { "Tất cả", "Còn nợ", "Không nợ" };
        searchFilterPanel = new SearchFilterPanelKH(filterOptions);
        searchFilterPanel.setSearchPlaceholder("Tìm theo Mã, Tên, SĐT, Email, CCCD...");
        searchFilterPanel.addSearchKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                locDuLieu();
            }
        });
        searchFilterPanel.addFilterActionListener(e -> locDuLieu());
        searchFilterPanel.addRefreshActionListener(e -> loadData());
        searchFilterPanel.addExportCSVActionListener(e -> xuatCSV());
        pnlTitleAndSearch.add(searchFilterPanel, BorderLayout.EAST);

        add(pnlTitleAndSearch, BorderLayout.NORTH);

        // 2. Bảng Khách Hàng
        tablePanel = new KhachHangTablePanel(this);
        add(tablePanel, BorderLayout.CENTER);

        // 3. Panel nút thao tác
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlActions.setOpaque(false); // Nên đặt false nếu panel cha có màu nền

        btnThemKH = new JButton("Thêm Khách Hàng");
        styleButton(btnThemKH, new Color(0, 123, 255)); // Blue
        btnThemKH.addActionListener(this);
        pnlActions.add(btnThemKH);

        btnThongKeKH = new JButton("Thống Kê");
        styleButton(btnThongKeKH, new Color(23, 162, 184)); // Teal
        btnThongKeKH.addActionListener(this);
        pnlActions.add(btnThongKeKH);

        add(pnlActions, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(160, 35)); // Kích thước tương tự NhanVienPanel
    }

    public KhachHangController getController() {
        return controller;
    }

    public KhachHangTablePanel getTablePanel() {
        return tablePanel;
    }

    public void loadData() {
        List<KhachHang> danhSachKhachHang = controller.getAllKhachHang();
        tablePanel.updateData(danhSachKhachHang);
        if (searchFilterPanel != null) { // Guard against null during initial construction
            searchFilterPanel.resetFilter();
            locDuLieu(); // Apply default filter ("Tất cả") after loading
        }
    }

    public void locDuLieu() {
        if (searchFilterPanel == null || tablePanel == null || controller == null) {
            return; // Guard against components not yet initialized
        }
        String filterType = searchFilterPanel.getSelectedFilter();
        String keyword = searchFilterPanel.getSearchText();

        List<KhachHang> baseList;
        if (keyword.trim().isEmpty()) {
            baseList = controller.getAllKhachHang();
        } else {
            baseList = controller.searchKhachHang(keyword);
        }

        List<KhachHang> finalList;
        switch (filterType) {
            case "Còn nợ":
                finalList = baseList.stream()
                        .filter(kh -> kh.getTongTienNo() > 0)
                        .collect(Collectors.toList());
                break;
            case "Không nợ":
                finalList = baseList.stream()
                        .filter(kh -> kh.getTongTienNo() <= 0)
                        .collect(Collectors.toList());
                break;
            case "Tất cả":
            default:
                finalList = baseList;
                break;
        }
        tablePanel.updateData(finalList);
    }

    public void xuatCSV() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Chọn vị trí lưu file CSV");
        fc.setSelectedFile(new File("DanhSachKhachHang.csv"));
        fc.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));

        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = fc.getSelectedFile();
        if (!f.getName().toLowerCase().endsWith(".csv")) {
            f = new File(f.getAbsolutePath() + ".csv");
        }

        try (FileWriter w = new FileWriter(f)) {
            // Header
            w.append("Mã KH,Họ Tên,SĐT,Email,CCCD,Địa Chỉ,Tổng Tiền Nợ,Mã Tài Khoản\n");
            List<KhachHang> currentTableData = tablePanel.getCurrentDataFromTableModel();

            for (KhachHang kh : currentTableData) {
                w.append(escapeCsv(kh.getMaKH())).append(",")
                        .append(escapeCsv(kh.getHoTen())).append(",")
                        .append(escapeCsv(kh.getSdt())).append(",")
                        .append(escapeCsv(kh.getEmail())).append(",")
                        .append(escapeCsv(kh.getCccd())).append(",")
                        .append(escapeCsv(kh.getDiaChi())).append(",")
                        .append(String.valueOf(kh.getTongTienNo())).append(",")
                        .append(escapeCsv(kh.getMaTK())).append("\n");
            }
            JOptionPane.showMessageDialog(this,
                    "Xuất CSV thành công:\n" + f.getAbsolutePath(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi xuất CSV:\n" + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String escapeCsv(String data) {
        if (data == null)
            return "";
        String escapedData = data.replace("\"", "\"\"");
        if (escapedData.contains(",") || escapedData.contains("\"") || escapedData.contains("\n")) {
            escapedData = "\"" + escapedData + "\"";
        }
        return escapedData;
    }

    public boolean xoaKhachHang(String maKH) {
        boolean success = controller.deleteKhachHang(maKH);
        if (!success) {
            String errorMessage = controller.getErrorMessage();
            JOptionPane.showMessageDialog(this,
                    "Không thể xóa khách hàng. " + (errorMessage != null && !errorMessage.isEmpty() ? errorMessage
                            : "Vui lòng kiểm tra lại."),
                    "Lỗi Xóa Khách Hàng", JOptionPane.ERROR_MESSAGE);
        }
        return success;
    }

    private void showThongKeKhachHang() {
        Map<String, Object> thongKe = controller.getThongKeKhachHang();
        if (thongKe == null || thongKe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu thống kê khách hàng.", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog statsDialog;
        Window owner = SwingUtilities.getWindowAncestor(this);
        statsDialog = new JDialog(owner, "Thống kê Khách Hàng", Dialog.ModalityType.APPLICATION_MODAL);

        statsDialog.setSize(750, 600);
        statsDialog.setLocationRelativeTo(this);

        JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.setBackground(Color.WHITE);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("THỐNG KÊ KHÁCH HÀNG");
        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 123, 255));
        titlePanel.add(titleLabel);
        mainContentPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel overviewPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        overviewPanel.setBackground(Color.WHITE);

        long tongSoKhachHang = (long) thongKe.getOrDefault("tongSoKhachHang", 0L);
        long soKhachHangCoNo = (long) thongKe.getOrDefault("soKhachHangCoNo", 0L);
        long soKhachHangKhongNo = (long) thongKe.getOrDefault("soKhachHangKhongNo", 0L);
        double tongTienNo = (double) thongKe.getOrDefault("tongTienNoAll", 0.0);

        overviewPanel.add(createStatPanel("Tổng số KH", String.valueOf(tongSoKhachHang), new Color(0, 123, 255)));
        overviewPanel.add(createStatPanel("KH có nợ", String.valueOf(soKhachHangCoNo), new Color(220, 53, 69)));
        overviewPanel.add(createStatPanel("KH không nợ", String.valueOf(soKhachHangKhongNo), new Color(40, 167, 69)));
        overviewPanel
                .add(createStatPanel("Tổng tiền nợ", currencyFormatter.format(tongTienNo), new Color(255, 193, 7)));

        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (tongSoKhachHang == 0) {
                    g2d.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 16));
                    g2d.drawString("Không có dữ liệu để vẽ biểu đồ", getWidth() / 2 - 100, getHeight() / 2);
                    return;
                }

                int barWidth = 80;
                int spacing = 50;
                int chartHeight = getHeight() - 80;
                // int chartWidth = getWidth() - 100; // Not directly used for bar placement
                // logic here

                int yAxisEnd = getHeight() - 40;
                int xAxisStart = 50;

                int coNoHeight = (int) (((double) soKhachHangCoNo / tongSoKhachHang) * (chartHeight - 20));
                g2d.setColor(new Color(220, 53, 69));
                g2d.fillRect(xAxisStart + spacing, yAxisEnd - coNoHeight, barWidth, coNoHeight);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(xAxisStart + spacing, yAxisEnd - coNoHeight, barWidth, coNoHeight);
                g2d.drawString(String.valueOf(soKhachHangCoNo), xAxisStart + spacing + barWidth / 2 - 10,
                        yAxisEnd - coNoHeight - 5);
                g2d.drawString("Có nợ", xAxisStart + spacing + barWidth / 2 - 20, yAxisEnd + 15);

                int khongNoHeight = (int) (((double) soKhachHangKhongNo / tongSoKhachHang) * (chartHeight - 20));
                g2d.setColor(new Color(40, 167, 69));
                g2d.fillRect(xAxisStart + spacing + barWidth + spacing, yAxisEnd - khongNoHeight, barWidth,
                        khongNoHeight);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(xAxisStart + spacing + barWidth + spacing, yAxisEnd - khongNoHeight, barWidth,
                        khongNoHeight);
                g2d.drawString(String.valueOf(soKhachHangKhongNo),
                        xAxisStart + spacing + barWidth + spacing + barWidth / 2 - 10, yAxisEnd - khongNoHeight - 5);
                g2d.drawString("Không nợ", xAxisStart + spacing + barWidth + spacing + barWidth / 2 - 30,
                        yAxisEnd + 15);

                g2d.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
                g2d.drawString("Phân loại khách hàng theo công nợ", getWidth() / 2 - 150, 30);
            }
        };
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        chartPanel.setPreferredSize(new Dimension(300, 220));

        JPanel debtTablePanel = new JPanel(new BorderLayout(0, 5));
        debtTablePanel.setBackground(Color.WHITE);
        JLabel debtTableTitle = new JLabel("Top 5 Khách Hàng Nợ Nhiều Nhất", JLabel.CENTER);
        debtTableTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        debtTablePanel.add(debtTableTitle, BorderLayout.NORTH);

        String[] columnNames = { "Mã KH", "Tên Khách Hàng", "Tổng Nợ" };
        @SuppressWarnings("unchecked")
        List<KhachHang> topDebtors = (List<KhachHang>) thongKe.getOrDefault("top5Debtors",
                new java.util.ArrayList<KhachHang>());
        Object[][] data = new Object[topDebtors.size()][3];

        for (int i = 0; i < topDebtors.size(); i++) {
            KhachHang kh = topDebtors.get(i);
            data[i][0] = kh.getMaKH();
            data[i][1] = kh.getHoTen();
            data[i][2] = currencyFormatter.format(kh.getTongTienNo());
        }

        JTable debtTable = new JTable(new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        debtTable.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        debtTable.getTableHeader().setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 13));
        debtTable.setRowHeight(25);
        debtTable.setGridColor(new Color(230, 230, 230));
        JScrollPane debtTableScrollPane = new JScrollPane(debtTable);
        debtTableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        debtTablePanel.add(debtTableScrollPane, BorderLayout.CENTER);
        debtTablePanel.setPreferredSize(new Dimension(300, 180));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        JButton closeButton = createStyledDialogButton("Đóng", new Color(120, 120, 120));
        closeButton.addActionListener(e -> statsDialog.dispose());
        buttonPanel.add(closeButton);
        mainContentPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel centerContentHolder = new JPanel(new BorderLayout(10, 10));
        centerContentHolder.setBackground(Color.WHITE);
        centerContentHolder.setBorder(new EmptyBorder(10, 0, 10, 0));
        centerContentHolder.add(overviewPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chartPanel, debtTablePanel);
        splitPane.setDividerLocation(230); // Height for the chartPanel
        splitPane.setResizeWeight(0.0); // Keep top component (chartPanel) height fixed
        splitPane.setBorder(null);
        splitPane.setEnabled(false); // Makes the divider non-movable

        centerContentHolder.add(splitPane, BorderLayout.CENTER);

        mainContentPanel.add(centerContentHolder, BorderLayout.CENTER);

        JScrollPane dialogScrollPane = new JScrollPane(mainContentPanel);
        dialogScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        dialogScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        dialogScrollPane.setBorder(BorderFactory.createEmptyBorder());

        statsDialog.setContentPane(dialogScrollPane);
        statsDialog.setVisible(true);
    } // Kết thúc phương thức showThongKeKhachHang

    private JPanel createStatPanel(String title, String value, Color valueColor) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setPreferredSize(new Dimension(150, 70)); // Adjusted size
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        titleLabel.setForeground(new Color(80, 80, 80));

        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
        valueLabel.setForeground(valueColor);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    private JButton createStyledDialogButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 35));
        button.setFocusPainted(false);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnThemKH) {
            ThemKhachHangDialog dlg = new ThemKhachHangDialog(
                    SwingUtilities.getWindowAncestor(this),
                    controller);
            dlg.setVisible(true);
            if (dlg.isSuccessfullyAdded()) {
                loadData();
            }
        } else if (source == btnThongKeKH) {
            showThongKeKhachHang();
        }
    }
}