/*package ui.admin;

import controller.ThongKeController;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.swing.table.JTableHeader;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class BaoCaoPanel extends JPanel {
    private JComboBox<Integer> cboNam;
    private JButton btnThongKe, btnExport;
    private JTabbedPane tabbedPane;
    
    private JPanel panelDoanhThuThang;
    private JPanel panelDoanhThuKhachHang;
    private JPanel panelXeThueNhieu;
    private JPanel panelTongHop;
    
    private JTable tableDoanhThuKH;
    private DefaultTableModel modelDoanhThuKH;
    private JTable tableXeThueNhieu;
    private DefaultTableModel modelXeThueNhieu;
    
    private ThongKeController thongKeController;
    private DecimalFormat currencyFormat;
    
    // Cột của bảng doanh thu khách hàng
    private final String[] DOANH_THU_KH_COLUMNS = {
        "Mã KH", "Họ tên", "Số hợp đồng", "Doanh thu"
    };
    
    // Cột của bảng xe thuê nhiều
    private final String[] XE_THUE_NHIEU_COLUMNS = {
        "Mã xe", "Tên xe", "Biển số", "Số lần thuê"
    };

    public BaoCaoPanel() {
        thongKeController = new ThongKeController();
        currencyFormat = new DecimalFormat("#,###");
        
        initComponents();
        generateReports();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Panel tiêu đề và điều khiển
        JPanel pnlTitle = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("BÁO CÁO THỐNG KÊ");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        pnlTitle.add(lblTitle, BorderLayout.WEST);
        
        // Panel điều khiển
        JPanel pnlControl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        pnlControl.add(new JLabel("Chọn năm:"));
        
        // Add years from 2020 to current year
        cboNam = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = 2020; year <= currentYear; year++) {
            cboNam.addItem(year);
        }
        cboNam.setSelectedItem(currentYear);
        cboNam.setPreferredSize(new Dimension(100, 30));
        pnlControl.add(cboNam);
        
        btnThongKe = new JButton("Thống kê");
        btnExport = new JButton("Xuất báo cáo");
        
        styleButton(btnThongKe, new Color(41, 121, 255));
        styleButton(btnExport, new Color(113, 85, 156));
        
        pnlControl.add(btnThongKe);
        pnlControl.add(btnExport);
        
        pnlTitle.add(pnlControl, BorderLayout.EAST);
        add(pnlTitle, BorderLayout.NORTH);
        
        // Tạo tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Tạo các panel cho từng tab
        panelDoanhThuThang = new JPanel(new BorderLayout());
        panelDoanhThuKhachHang = new JPanel(new BorderLayout());
        panelXeThueNhieu = new JPanel(new BorderLayout());
        panelTongHop = new JPanel(new BorderLayout());
        
        // Tạo bảng doanh thu khách hàng
        modelDoanhThuKH = new DefaultTableModel(DOANH_THU_KH_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableDoanhThuKH = new JTable(modelDoanhThuKH);
        tableDoanhThuKH.setRowHeight(40);
        
        // Thiết lập độ rộng cột
        TableColumnModel columnModelKH = tableDoanhThuKH.getColumnModel();
        columnModelKH.getColumn(0).setPreferredWidth(60);  // Mã KH
        columnModelKH.getColumn(1).setPreferredWidth(200); // Họ tên
        columnModelKH.getColumn(2).setPreferredWidth(100); // Số hợp đồng
        columnModelKH.getColumn(3).setPreferredWidth(150); // Doanh thu
        
        // Tùy chỉnh header
        JTableHeader headerKH = tableDoanhThuKH.getTableHeader();
        headerKH.setFont(new Font("Arial", Font.BOLD, 14));
        headerKH.setBackground(new Color(240, 240, 240));
        headerKH.setForeground(new Color(60, 60, 60));
        headerKH.setPreferredSize(new Dimension(0, 40));
        
        // Tùy chỉnh grid lines
        tableDoanhThuKH.setShowGrid(true);
        tableDoanhThuKH.setGridColor(new Color(230, 230, 230));
        
        // Tạo hiệu ứng dòng sọc
        tableDoanhThuKH.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Thêm padding cho text trong ô
                if (comp instanceof JLabel) {
                    ((JLabel) comp).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                }

                // Màu nền dòng chẵn/lẻ nếu không được chọn
                if (!isSelected) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    comp.setForeground(new Color(50, 50, 50));
                }

                // Căn phải cho cột doanh thu
                if (column == 3) { // Doanh thu
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else if (column == 2) { // Số hợp đồng
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                return comp;
            }
        });
        
        JScrollPane scrollPaneKH = new JScrollPane(tableDoanhThuKH);
        panelDoanhThuKhachHang.add(scrollPaneKH, BorderLayout.CENTER);
        
        // Tạo bảng xe thuê nhiều
        modelXeThueNhieu = new DefaultTableModel(XE_THUE_NHIEU_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableXeThueNhieu = new JTable(modelXeThueNhieu);
        tableXeThueNhieu.setRowHeight(40);
        
        // Thiết lập độ rộng cột
        TableColumnModel columnModelXe = tableXeThueNhieu.getColumnModel();
        columnModelXe.getColumn(0).setPreferredWidth(60);  // Mã xe
        columnModelXe.getColumn(1).setPreferredWidth(200); // Tên xe
        columnModelXe.getColumn(2).setPreferredWidth(100); // Biển số
        columnModelXe.getColumn(3).setPreferredWidth(100); // Số lần thuê
        
        // Tùy chỉnh header
        JTableHeader headerXe = tableXeThueNhieu.getTableHeader();
        headerXe.setFont(new Font("Arial", Font.BOLD, 14));
        headerXe.setBackground(new Color(240, 240, 240));
        headerXe.setForeground(new Color(60, 60, 60));
        headerXe.setPreferredSize(new Dimension(0, 40));
        
        // Tùy chỉnh grid lines
        tableXeThueNhieu.setShowGrid(true);
        tableXeThueNhieu.setGridColor(new Color(230, 230, 230));
        
        // Tạo hiệu ứng dòng sọc
        tableXeThueNhieu.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Thêm padding cho text trong ô
                if (comp instanceof JLabel) {
                    ((JLabel) comp).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                }

                // Màu nền dòng chẵn/lẻ nếu không được chọn
                if (!isSelected) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    comp.setForeground(new Color(50, 50, 50));
                }

                // Căn giữa cho cột số lần thuê
                if (column == 3) { // Số lần thuê
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                return comp;
            }
        });
        
        JScrollPane scrollPaneXe = new JScrollPane(tableXeThueNhieu);
        panelXeThueNhieu.add(scrollPaneXe, BorderLayout.CENTER);
        
        // Thêm các tab
        tabbedPane.addTab("Doanh thu theo tháng", panelDoanhThuThang);
        tabbedPane.addTab("Doanh thu theo khách hàng", panelDoanhThuKhachHang);
        tabbedPane.addTab("Xe được thuê nhiều nhất", panelXeThueNhieu);
        tabbedPane.addTab("Tổng hợp", panelTongHop);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Thêm sự kiện
        btnThongKe.addActionListener(e -> generateReports());
        btnExport.addActionListener(e -> exportToExcel());
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
    }
    
    private void generateReports() {
        int nam = (int) cboNam.getSelectedItem();
        
        generateDoanhThuThangChart(nam);
        generateDoanhThuKhachHangTable(nam);
        generateXeThueNhieuTable(nam);
        generateTongHopPanel(nam);
    }
    
    private void generateDoanhThuThangChart(int nam) {
        Map<Integer, Double> doanhThuThang = thongKeController.getDoanhThuTheoThang(nam);
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (Map.Entry<Integer, Double> entry : doanhThuThang.entrySet()) {
            dataset.addValue(entry.getValue(), "Doanh thu", "Tháng " + entry.getKey());
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
                "Doanh thu theo tháng năm " + nam,
                "Tháng",
                "Doanh thu (VNĐ)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 500));
        
        panelDoanhThuThang.removeAll();
        panelDoanhThuThang.add(chartPanel, BorderLayout.CENTER);
        panelDoanhThuThang.revalidate();
        panelDoanhThuThang.repaint();
    }
    
    private void generateDoanhThuKhachHangTable(int nam) {
        List<Map<String, Object>> doanhThuKH = thongKeController.getDoanhThuTheoKhachHang(nam);
        
        modelDoanhThuKH.setRowCount(0);
        
        for (Map<String, Object> row : doanhThuKH) {
            Object[] tableRow = {
                row.get("MaKH"),
                row.get("HoTen"),
                row.get("SoHopDong"),
                currencyFormat.format((Double) row.get("TongDoanhThu")) + " VNĐ"
            };
            modelDoanhThuKH.addRow(tableRow);
        }
    }
    
    private void generateXeThueNhieuTable(int nam) {
        List<Map<String, Object>> xeThueNhieu = thongKeController.getXeDuocThueNhieuNhat(nam);
        
        modelXeThueNhieu.setRowCount(0);
        
        for (Map<String, Object> row : xeThueNhieu) {
            Object[] tableRow = {
                row.get("MaXe"),
                row.get("TenXe"),
                row.get("BienSo"),
                row.get("SoLanThue")
            };
            modelXeThueNhieu.addRow(tableRow);
        }
    }
    
    private void generateTongHopPanel(int nam) {
        double doanhThuBaoDuong = thongKeController.getDoanhThuBaoDuong(nam);
        double chiPhiBaoDuongDinhKy = thongKeController.getChiPhiBaoDuongDinhKy(nam);
        double tongCongNo = thongKeController.getTongCongNo();
        
        // Calculate total revenue from monthly data
        Map<Integer, Double> doanhThuThang = thongKeController.getDoanhThuTheoThang(nam);
        double tongDoanhThu = 0;
        for (Double value : doanhThuThang.values()) {
            tongDoanhThu += value;
        }
        
        // Create summary panel
        JPanel summaryPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        summaryPanel.add(createSummaryLabel("Tổng doanh thu thuê xe:"));
        summaryPanel.add(createValueLabel(tongDoanhThu));
        
        summaryPanel.add(createSummaryLabel("Doanh thu từ bảo dưỡng:"));
        summaryPanel.add(createValueLabel(doanhThuBaoDuong));
        
        summaryPanel.add(createSummaryLabel("Chi phí bảo dưỡng định kỳ:"));
        summaryPanel.add(createValueLabel(chiPhiBaoDuongDinhKy));
        
        summaryPanel.add(createSummaryLabel("Tổng công nợ hiện tại:"));
        summaryPanel.add(createValueLabel(tongCongNo));
        
        // Create pie chart for revenue breakdown
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("Doanh thu thuê xe", tongDoanhThu);
        pieDataset.setValue("Doanh thu bảo dưỡng", doanhThuBaoDuong);
        
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Cơ cấu doanh thu năm " + nam,
                pieDataset,
                true,
                true,
                false
        );
        
        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        pieChartPanel.setPreferredSize(new Dimension(400, 300));
        
        // Create panel for profit calculation
        JPanel profitPanel = new JPanel(new BorderLayout());
        profitPanel.setBorder(BorderFactory.createTitledBorder("Lợi nhuận"));
        
        double loiNhuan = tongDoanhThu + doanhThuBaoDuong - chiPhiBaoDuongDinhKy;
        
        JLabel lblLoiNhuan = new JLabel("Lợi nhuận năm " + nam + ": " + currencyFormat.format(loiNhuan) + " VNĐ");
        lblLoiNhuan.setFont(new Font("Arial", Font.BOLD, 16));
        lblLoiNhuan.setHorizontalAlignment(SwingConstants.CENTER);
        lblLoiNhuan.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        profitPanel.add(lblLoiNhuan, BorderLayout.CENTER);
        
        // Layout the components
        panelTongHop.removeAll();
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(summaryPanel, BorderLayout.NORTH);
        topPanel.add(profitPanel, BorderLayout.CENTER);
        
        panelTongHop.add(topPanel, BorderLayout.NORTH);
        panelTongHop.add(pieChartPanel, BorderLayout.CENTER);
        
        panelTongHop.revalidate();
        panelTongHop.repaint();
    }
    
    private JLabel createSummaryLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }
    
    private JLabel createValueLabel(double value) {
        JLabel label = new JLabel(currencyFormat.format(value) + " VNĐ");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(41, 128, 185));
        return label;
    }
    
    private void exportToExcel() {
        JOptionPane.showMessageDialog(this, "Chức năng xuất Excel sẽ được phát triển sau!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}*/