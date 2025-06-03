package ui.admin;

import controller.ThongKeController;
import model.KhachHangDoanhThu;
import model.XeDoanhThu;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.util.*;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartPanel;

import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;

import java.util.Map;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.text.NumberFormat;

import javax.swing.table.DefaultTableCellRenderer; 
import java.awt.event.ItemEvent;
import util.DatabaseUtil;


import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;

import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.general.DefaultPieDataset;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.List;
import util.ExcelExporter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

public class ThongKePanel extends JPanel {
    private ThongKeController controller;
    private JTabbedPane tabbedPane;
    private JPanel pnlTongQuan;
    private JPanel pnlDoanhThuTheoThang;
    private JPanel pnlDoanhThuTheoKhachHang;
    private JPanel pnlDoanhThuTheoXe;
     private JTable topContractsTable; 
    private JLabel lblTongSoXe;
    private JLabel lblTongSoKhachHang;
    private JLabel lblTongSoHopDong;
    private JLabel lblTongDoanhThu;
    
    private JComboBox<Integer> cboNamThongKe;
    private ChartPanel chartPanelDoanhThuThang;
    private ChartPanel chartPanelDoanhThuKhachHang;
    private JTable tblDoanhThuKhachHang;
    private ChartPanel chartPanelDoanhThuXe;
    private JTable tblDoanhThuXe;
    JButton btnTestPhantomRead = new JButton("Test Phantom Read");


    private NumberFormat currencyFormat;
    
    // Colors
    private final Color PRIMARY_COLOR = new Color(41, 121, 255);
    private final Color ACCENT_COLOR = new Color(0, 150, 136);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private final Color HEADER_COLOR = new Color(33, 150, 243);
    private boolean isBarChart = true;
    
//=====TRƯỜNG HỢP CẦN NÚT===/
    public ThongKePanel() {

            controller = new ThongKeController();
        controller.startReportView(); // Bắt đầu chế độ xem báo cáo khi mở panel

        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        initComponents();
        loadData();

        // Đăng ký sự kiện khi panel bị hủy
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                controller.endReportView();
            }
        });
    }
    
    //============================THONGKEPANEL trường hợp không cần nút//
//    public ThongKePanel() {
//        controller = new ThongKeController();
//
//        // Thiết lập isolation level trực tiếp, thay thế cho UI
//        // Uncomment dòng isolation level bạn muốn sử dụng:
//
//        // Sử dụng READ_COMMITTED (cho phép phantom read)
//       // setDirectIsolationLevel(Connection.TRANSACTION_READ_COMMITTED);
//
//        // Sử dụng SERIALIZABLE (ngăn chặn phantom read)
//        setDirectIsolationLevel(Connection.TRANSACTION_SERIALIZABLE);
//
//        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//
//        initComponents();
//        loadData();
//
//        // Đăng ký sự kiện khi panel bị hủy
//        addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentHidden(ComponentEvent e) {
//                controller.endReportView();
//            }
//        });
//    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        
        // Tạo tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setForeground(new Color(50, 50, 50));
        
        // Tạo các panel cho từng tab
        pnlTongQuan = createTongQuanPanel();
        pnlDoanhThuTheoThang = createDoanhThuThangPanel();
        pnlDoanhThuTheoKhachHang = createDoanhThuKhachHangPanel();
        pnlDoanhThuTheoXe = createDoanhThuXePanel();
        
        // Thêm các tab
        tabbedPane.addTab("Tổng quan", pnlTongQuan);
        tabbedPane.addTab("Doanh thu theo tháng", pnlDoanhThuTheoThang);
        tabbedPane.addTab("Doanh thu theo khách hàng", pnlDoanhThuTheoKhachHang);
        tabbedPane.addTab("Doanh thu theo xe", pnlDoanhThuTheoXe);
        
        // Thêm tabbed pane vào panel chính
        add(tabbedPane, BorderLayout.CENTER);
        
        // Tạo combobox chọn năm thống kê và panel điều khiển chung
        JPanel filterPanel = createFilterPanel();
        add(filterPanel, BorderLayout.NORTH);
        addExportButtons();

    }
    
    
     private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBorder(new EmptyBorder(10, 15, 5, 15));
        panel.setBackground(Color.WHITE);
       
        //THÊM ISOLATION NÚT ĐỂ THAY ĐỔI CHO TEST PHANTOM DỄ HƠN.
        //-----------------------------------------------------------==//
        JLabel lblIsolation = new JLabel("Isolation Level:");
        lblIsolation.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lblIsolation);

        JComboBox<String> cboIsolation = new JComboBox<>(new String[]{"READ_COMMITTED", "SERIALIZABLE"});
        cboIsolation.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(cboIsolation);

        // Nút áp dụng isolation level
        JButton btnApplyIsolation = new JButton("Áp dụng");
        btnApplyIsolation.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnApplyIsolation.setBackground(new Color(0, 150, 136));
        btnApplyIsolation.setForeground(Color.WHITE);
        btnApplyIsolation.addActionListener(e -> {
            String level = (String) cboIsolation.getSelectedItem();
            applyIsolationLevel(level);
        });
        panel.add(btnApplyIsolation);

//        //Tới đây là hết phần này. Nếu muốn bỏ thì chú thích lại nó đi
//        //==============================================================//
        // Label Năm thống kê
        JLabel lblNamThongKe = new JLabel("Năm thống kê:");
        lblNamThongKe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lblNamThongKe);
        
        // ComboBox chọn năm
        cboNamThongKe = new JComboBox<>();
        cboNamThongKe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Thêm các năm từ 2020 đến năm hiện tại
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = currentYear; year >= 2020; year--) {
            cboNamThongKe.addItem(year);
        }
        
        // Xử lý sự kiện khi chọn năm thì tự động cập nhật dữ liệu ngay lập tức
        cboNamThongKe.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int selectedYear = (Integer) cboNamThongKe.getSelectedItem();
                loadDataByYear(selectedYear);
            }
        });
        
        panel.add(cboNamThongKe);
        
        // Button Xem thống kê - vẫn giữ lại cho trường hợp cần cập nhật theo các điều kiện khác
        JButton btnXemThongKe = new JButton("Xem thống kê");
        btnXemThongKe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXemThongKe.setBackground(PRIMARY_COLOR);
        btnXemThongKe.setForeground(Color.WHITE);
        panel.add(btnXemThongKe);
        
        // Xử lý sự kiện khi nhấn nút Xem thống kê
        btnXemThongKe.addActionListener(e -> {
            int selectedYear = (Integer) cboNamThongKe.getSelectedItem();
            loadDataByYear(selectedYear);
        });
        JButton btnRefreshAll = new JButton("Làm mới dữ liệu");
        btnRefreshAll.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRefreshAll.setBackground(new Color(76, 175, 80)); // Màu xanh lá
        btnRefreshAll.setForeground(Color.WHITE);
        btnRefreshAll.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefreshAll.setFocusPainted(false);
        panel.add(btnRefreshAll);

        // Xử lý sự kiện khi nhấn nút Làm mới dữ liệu
        btnRefreshAll.addActionListener(e -> refreshData());

        return panel;
    }
     private void applyIsolationLevel(String level) {
        try {
            // Kết thúc phiên xem báo cáo cũ
            controller.endReportView();

            // Thiết lập isolation level mới
            if ("SERIALIZABLE".equals(level)) {
                controller.setIsolationLevel(Connection.TRANSACTION_SERIALIZABLE);
            } else {
                controller.setIsolationLevel(Connection.TRANSACTION_READ_COMMITTED);
            }

            // Bắt đầu phiên xem báo cáo mới với isolation level đã chọn
            controller.startReportView();

            // QUAN TRỌNG: Thực hiện ngay một truy vấn để bắt đầu transaction
            // và thiết lập snapshot cho SERIALIZABLE
            int selectedYear = (Integer) cboNamThongKe.getSelectedItem();
            controller.getTongQuan(); // Truy vấn đầu tiên để "khóa" snapshot

            // Hiển thị thông báo và hướng dẫn
            JOptionPane.showMessageDialog(this, 
                "Đã áp dụng isolation level: " + level + "\n\n" +
                "Hướng dẫn demo Phantom Read:\n" +
                "1. ĐÃ tạo transaction với isolation level " + level + "\n" +
                "2. Mở instance khác và thêm hợp đồng mới\n" +
                "3. Quay lại đây và nhấn 'Làm mới dữ liệu'\n" +
                "4. Với READ_COMMITTED: Sẽ thấy hợp đồng mới\n" +
                "5. Với SERIALIZABLE: Sẽ KHÔNG thấy hợp đồng mới",
                "Thiết lập thành công", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
     //Cái này để demo phantom read nhưng mà không cần 2 cái nút đó
//     private void setDirectIsolationLevel(int level) {
//        try {
//            controller.endReportView();
//            controller.setIsolationLevel(level);
//            controller.startReportView();
//
//            // Thực hiện một truy vấn ngay lập tức để bắt đầu transaction
//            controller.getTongQuan();
//
//            System.out.println("Đã thiết lập trực tiếp isolation level: " + 
//                (level == Connection.TRANSACTION_SERIALIZABLE ? "SERIALIZABLE" : "READ_COMMITTED"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

     ///===========================================================//
    public void refreshData() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            //Để demo khong cần nút nè:
         // DEBUG MODE: Uncomment một trong hai dòng dưới đây để nhanh chóng chuyển đổi isolation level
        // setDirectIsolationLevel(Connection.TRANSACTION_READ_COMMITTED); // Cho phép phantom read
        // setDirectIsolationLevel(Connection.TRANSACTION_SERIALIZABLE);   // Ngăn chặn phantom read
        //
            int selectedYear = (Integer) cboNamThongKe.getSelectedItem();

            // Cập nhật dữ liệu thống kê tổng quan
            Map<String, Number> tongQuan = controller.getTongQuan();
            lblTongSoXe.setText(String.valueOf(tongQuan.getOrDefault("tongSoXe", 0)));
            lblTongSoKhachHang.setText(String.valueOf(tongQuan.getOrDefault("tongSoKhachHang", 0)));
            lblTongSoHopDong.setText(String.valueOf(tongQuan.getOrDefault("tongSoHopDong", 0)));
            lblTongDoanhThu.setText(currencyFormat.format(tongQuan.getOrDefault("tongDoanhThu", 0)));

            // Cập nhật bảng top 5 hợp đồng
            updateTopContractsTable(selectedYear);

            // Cập nhật dữ liệu thống kê theo tháng, khách hàng, xe
            loadDataByYear(selectedYear);

            JOptionPane.showMessageDialog(this, 
                "Đã cập nhật dữ liệu thống kê với isolation level: " + 
                (controller.getIsolationLevel() == Connection.TRANSACTION_SERIALIZABLE ? 
                    "SERIALIZABLE (không thấy dữ liệu mới)" : 
                    "READ_COMMITTED (có thể thấy dữ liệu mới)"), 
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi làm mới dữ liệu: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void addExportButtons() {
        // Thêm nút xuất báo cáo doanh thu theo tháng
        JButton btnExportThang = new JButton("Xuất Excel");
     //   btnExportThang.setIcon(new ImageIcon(getClass().getResource("/images/excel_icon.png"))); // Thêm icon nếu có
        btnExportThang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExportThang.setBackground(new Color(76, 175, 80));
        btnExportThang.setForeground(Color.WHITE);

        btnExportThang.addActionListener(e -> {
            int year = (Integer) cboNamThongKe.getSelectedItem();
            exportDoanhThuTheoThang(year);
        });

        // Thêm nút vào panel doanh thu theo tháng ở vị trí thích hợp
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnExportThang);
        // Thêm vào vị trí thích hợp trong panel doanh thu theo tháng
        pnlDoanhThuTheoThang.add(buttonPanel, BorderLayout.SOUTH);

        // Tương tự cho các tab khác
        JButton btnExportKhachHang = new JButton("Xuất Excel");
     //   btnExportKhachHang.setIcon(new ImageIcon(getClass().getResource("/images/excel_icon.png")));
        btnExportKhachHang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExportKhachHang.setBackground(new Color(76, 175, 80));
        btnExportKhachHang.setForeground(Color.WHITE);

        btnExportKhachHang.addActionListener(e -> {
            int year = (Integer) cboNamThongKe.getSelectedItem();
            exportDoanhThuTheoKhachHang(year);
        });

        JPanel buttonPanelKH = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanelKH.setOpaque(false);
        buttonPanelKH.add(btnExportKhachHang);
        pnlDoanhThuTheoKhachHang.add(buttonPanelKH, BorderLayout.SOUTH);

        // Và cho tab doanh thu theo xe
        JButton btnExportXe = new JButton("Xuất Excel");
     //   btnExportXe.setIcon(new ImageIcon(getClass().getResource("/images/excel_icon.png")));
        btnExportXe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExportXe.setBackground(new Color(76, 175, 80));
        btnExportXe.setForeground(Color.WHITE);

        btnExportXe.addActionListener(e -> {
            int year = (Integer) cboNamThongKe.getSelectedItem();
            exportDoanhThuTheoXe(year);
        });

        JPanel buttonPanelXe = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanelXe.setOpaque(false);
        buttonPanelXe.add(btnExportXe);
        pnlDoanhThuTheoXe.add(buttonPanelXe, BorderLayout.SOUTH);
    }
    private void exportDoanhThuTheoThang(int year) {
    try {
        // Lấy dữ liệu
        Map<Integer, Double> doanhThuThang = controller.getDoanhThuTheoThang(year);
        
        // Hiển thị hộp thoại chọn vị trí lưu file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu báo cáo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
        fileChooser.setSelectedFile(new File("Bao_Cao_Doanh_Thu_Theo_Thang_" + year + ".xlsx"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            // Đảm bảo có đuôi .xlsx
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }
            
            // Xuất báo cáo
            ExcelExporter.exportDoanhThuTheoThang(doanhThuThang, year, filePath);
            
            // Thông báo thành công
            JOptionPane.showMessageDialog(this, "Xuất báo cáo thành công!\nFile được lưu tại: " + filePath, 
                    "Xuất báo cáo", JOptionPane.INFORMATION_MESSAGE);
            
            // Mở file sau khi xuất
            if (JOptionPane.showConfirmDialog(this, "Bạn có muốn mở file báo cáo không?", 
                    "Mở file", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                openFile(filePath);
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi khi xuất báo cáo: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    private void exportDoanhThuTheoKhachHang(int year) {
        try {
            // Lấy dữ liệu
            List<KhachHangDoanhThu> doanhThuKhachHang = controller.getDoanhThuTheoKhachHang(year);

            // Hiển thị hộp thoại chọn vị trí lưu file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn vị trí lưu báo cáo");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
            fileChooser.setSelectedFile(new File("Bao_Cao_Doanh_Thu_Theo_Khach_Hang_" + year + ".xlsx"));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                // Đảm bảo có đuôi .xlsx
                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                // Xuất báo cáo
                ExcelExporter.exportDoanhThuTheoKhachHang(doanhThuKhachHang, year, filePath);

                // Thông báo thành công
                JOptionPane.showMessageDialog(this, "Xuất báo cáo thành công!\nFile được lưu tại: " + filePath, 
                        "Xuất báo cáo", JOptionPane.INFORMATION_MESSAGE);

                // Mở file sau khi xuất
                if (JOptionPane.showConfirmDialog(this, "Bạn có muốn mở file báo cáo không?", 
                        "Mở file", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    openFile(filePath);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất báo cáo: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void exportDoanhThuTheoXe(int year) {
        try {
            // Lấy dữ liệu
            List<XeDoanhThu> doanhThuXe = controller.getDoanhThuTheoXe(year);

            // Hiển thị hộp thoại chọn vị trí lưu file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn vị trí lưu báo cáo");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
            fileChooser.setSelectedFile(new File("Bao_Cao_Doanh_Thu_Theo_Xe_" + year + ".xlsx"));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                // Đảm bảo có đuôi .xlsx
                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                // Xuất báo cáo
                ExcelExporter.exportDoanhThuTheoXe(doanhThuXe, year, filePath);

                // Thông báo thành công
                JOptionPane.showMessageDialog(this, "Xuất báo cáo thành công!\nFile được lưu tại: " + filePath, 
                        "Xuất báo cáo", JOptionPane.INFORMATION_MESSAGE);

                // Mở file sau khi xuất
                if (JOptionPane.showConfirmDialog(this, "Bạn có muốn mở file báo cáo không?", 
                        "Mở file", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    openFile(filePath);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất báo cáo: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Phương thức mở file sau khi xuất
    private void openFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể mở file tự động trên hệ thống này.\nFile được lưu tại: " + filePath, 
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Không thể mở file: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private JPanel createTongQuanPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblTitle = new JLabel("Tổng quan thống kê");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(HEADER_COLOR);
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        
        panel.add(titlePanel);
        panel.add(Box.createVerticalStrut(20));
        
        // THÊM MỚI: Bảng Top 5 hợp đồng có số tiền cao nhất
        JPanel topContractsPanel = new JPanel(new BorderLayout());
        topContractsPanel.setBackground(Color.WHITE);
        topContractsPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        JLabel lblTopContracts = new JLabel("  Top 5 hợp đồng có số tiền cao nhất");
        lblTopContracts.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTopContracts.setForeground(new Color(60, 60, 60));
        lblTopContracts.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tạo model cho bảng
        String[] columnNames = {"STT", "Mã HD", "Khách hàng", "Ngày lập", "Thành tiền"};
        
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        topContractsTable = new JTable(tableModel); // Lưu reference vào biến instance
        topContractsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        topContractsTable.setRowHeight(35);
        topContractsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        topContractsTable.getTableHeader().setBackground(new Color(240, 240, 245));
        
        // Căn phải cho cột thành tiền
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        topContractsTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        JScrollPane scrollPane = new JScrollPane(topContractsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        topContractsPanel.add(lblTopContracts, BorderLayout.NORTH);
        topContractsPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(topContractsPanel);
        panel.add(Box.createVerticalStrut(20));
        
        // Panel các ô thống kê (giữ nguyên code cũ)
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(BACKGROUND_COLOR);
        
        // Ô Tổng số xe
        JPanel pnlXe = createStatBoxPanel("Tổng số xe", "0", new Color(33, 150, 243));
        lblTongSoXe = (JLabel) pnlXe.getComponent(1);
        statsPanel.add(pnlXe);
        
        // Ô Tổng số khách hàng
        JPanel pnlKH = createStatBoxPanel("Tổng số khách hàng", "0", new Color(0, 150, 136));
        lblTongSoKhachHang = (JLabel) pnlKH.getComponent(1);
        statsPanel.add(pnlKH);
        
        // Ô Tổng số hợp đồng
        JPanel pnlHD = createStatBoxPanel("Tổng số hợp đồng", "0", new Color(233, 30, 99));
        lblTongSoHopDong = (JLabel) pnlHD.getComponent(1);
        statsPanel.add(pnlHD);
        
        // Ô Tổng doanh thu
        JPanel pnlDT = createStatBoxPanel("Tổng doanh thu", "0 VNĐ", new Color(255, 152, 0));
        lblTongDoanhThu = (JLabel) pnlDT.getComponent(1);
        statsPanel.add(pnlDT);
        
        panel.add(statsPanel);
        
        return panel;
    }

    private void updateTopContractsTable(int year) {
        try {
            // Lấy dữ liệu từ controller
            List<Map<String, Object>> topContracts = controller.getTop5HopDong(year);

            // Lấy model của bảng và xóa dữ liệu cũ
            DefaultTableModel model = (DefaultTableModel) topContractsTable.getModel();
            model.setRowCount(0);

            // Định dạng ngày tháng
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            // Kiểm tra nếu không có dữ liệu
            if (topContracts.isEmpty()) {
                System.out.println("Không có dữ liệu hợp đồng cho năm " + year);
                return;
            }

            // Thêm dữ liệu mới vào bảng
            for (int i = 0; i < topContracts.size(); i++) {
                Map<String, Object> contract = topContracts.get(i);
                Date ngayLap = (Date) contract.get("ngayLap");
                String ngayLapFormatted = ngayLap != null ? dateFormat.format(ngayLap) : "N/A";

                model.addRow(new Object[] {
                    i + 1,
                    contract.get("maHD"),
                    contract.get("tenKH"),
                    ngayLapFormatted,
                    currencyFormat.format((Double) contract.get("tongTien"))
                });
            }
            System.out.println("Đã cập nhật " + topContracts.size() + " hợp đồng vào bảng");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi cập nhật bảng: " + e.getMessage());
        }
    }


    private JPanel createStatBoxPanel(String title, String value, Color color) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Vẽ hình chữ nhật với góc bo tròn
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // Đặt panel thành trong suốt để hiển thị phần vẽ tùy chỉnh
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Tiêu đề
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

        // Giá trị
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(Color.WHITE);
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblValue.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));

        panel.add(lblTitle);
        panel.add(lblValue);

        return panel;
    }

    private JPanel createDoanhThuThangPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel("Doanh thu theo tháng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(HEADER_COLOR);

        // Thêm panel chứa radio buttons để chọn loại biểu đồ
        JPanel chartTypePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        chartTypePanel.setOpaque(false);

        JRadioButton rbBarChart = new JRadioButton("Biểu đồ cột");
        JRadioButton rbLineChart = new JRadioButton("Biểu đồ đường");
        rbBarChart.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rbLineChart.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rbBarChart.setOpaque(false);
        rbLineChart.setOpaque(false);

        // Group các radio buttons
        ButtonGroup chartTypeGroup = new ButtonGroup();
        chartTypeGroup.add(rbBarChart);
        chartTypeGroup.add(rbLineChart);
        rbBarChart.setSelected(true); // Mặc định chọn biểu đồ cột

        chartTypePanel.add(rbBarChart);
        chartTypePanel.add(rbLineChart);

        // Xử lý sự kiện khi chọn loại biểu đồ
        rbBarChart.addActionListener(e -> {
            isBarChart = true;
            int selectedYear = (Integer) cboNamThongKe.getSelectedItem();
            Map<Integer, Double> doanhThuThang = controller.getDoanhThuTheoThang(selectedYear);
            updateDoanhThuThangChart(doanhThuThang, selectedYear, true);
        });

        rbLineChart.addActionListener(e -> {
            isBarChart = false;
            int selectedYear = (Integer) cboNamThongKe.getSelectedItem();
            Map<Integer, Double> doanhThuThang = controller.getDoanhThuTheoThang(selectedYear);
            updateDoanhThuThangChart(doanhThuThang, selectedYear, false);
        });

        // Kết hợp title và panel chọn loại biểu đồ
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(chartTypePanel, BorderLayout.EAST);

        titlePanel.add(headerPanel, BorderLayout.CENTER);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Panel biểu đồ
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        // Tạo biểu đồ trống
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart(
                "Biểu đồ doanh thu theo tháng", 
                "Tháng", 
                "Doanh thu (VNĐ)", 
                dataset,
                PlotOrientation.VERTICAL, 
                true, 
                true, 
                false);

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, PRIMARY_COLOR);
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());

        chartPanelDoanhThuThang = new ChartPanel(chart);
        chartPanelDoanhThuThang.setPreferredSize(new Dimension(700, 500));
        chartPanel.add(chartPanelDoanhThuThang, BorderLayout.CENTER);

        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    
    private JPanel createDoanhThuKhachHangPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblTitle = new JLabel("Doanh thu theo khách hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(HEADER_COLOR);
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        
        // Panel chính chứa biểu đồ và bảng
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Panel biểu đồ
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        // Tạo biểu đồ trống
        DefaultPieDataset dataset = new DefaultPieDataset();
        JFreeChart chart = ChartFactory.createPieChart(
                "Doanh thu theo khách hàng", 
                dataset, 
                true, 
                true, 
                false);
        
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Khách hàng khác", new Color(200, 200, 200));
        
        chartPanelDoanhThuKhachHang = new ChartPanel(chart);
        chartPanelDoanhThuKhachHang.setPreferredSize(new Dimension(400, 300));
        chartPanel.add(chartPanelDoanhThuKhachHang, BorderLayout.CENTER);
        
        // Panel bảng dữ liệu
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        // Tạo mô hình bảng
        String[] columnNames = {"STT", "Mã KH", "Họ tên", "Số hợp đồng", "Doanh thu"};
        Object[][] data = {};
        
        tblDoanhThuKhachHang = new JTable(data, columnNames);
        tblDoanhThuKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblDoanhThuKhachHang.setRowHeight(30);
        tblDoanhThuKhachHang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblDoanhThuKhachHang.getTableHeader().setBackground(new Color(240, 240, 245));
        
        JScrollPane scrollPane = new JScrollPane(tblDoanhThuKhachHang);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Thêm biểu đồ và bảng vào panel chính
        mainPanel.add(chartPanel);
        mainPanel.add(tablePanel);
        
        panel.add(mainPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createDoanhThuXePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblTitle = new JLabel("Doanh thu theo xe");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(HEADER_COLOR);
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        
        // Panel chính chứa biểu đồ và bảng
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Panel biểu đồ
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        // Tạo biểu đồ trống
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart(
                "Top 10 xe có doanh thu cao nhất", 
                "Mã xe", 
                "Doanh thu (VNĐ)", 
                dataset,
                PlotOrientation.VERTICAL, 
                true, 
                true, 
                false);
        
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, ACCENT_COLOR);
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        
        chartPanelDoanhThuXe = new ChartPanel(chart);
        chartPanelDoanhThuXe.setPreferredSize(new Dimension(700, 300));
        chartPanel.add(chartPanelDoanhThuXe, BorderLayout.CENTER);
        
        // Panel bảng dữ liệu
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        // Tạo mô hình bảng
        String[] columnNames = {"STT", "Mã xe", "Tên xe", "Biển số", "Số lượt thuê", "Doanh thu"};
        Object[][] data = {};
        
        tblDoanhThuXe = new JTable(data, columnNames);
        tblDoanhThuXe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblDoanhThuXe.setRowHeight(30);
        tblDoanhThuXe.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblDoanhThuXe.getTableHeader().setBackground(new Color(240, 240, 245));
        
        JScrollPane scrollPane = new JScrollPane(tblDoanhThuXe);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Thêm biểu đồ và bảng vào panel chính
        mainPanel.add(chartPanel);
        mainPanel.add(tablePanel);
        
        panel.add(mainPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadData() {
        // Lấy năm hiện tại và load dữ liệu
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        loadDataByYear(currentYear);
        
        // Load số liệu tổng quan
        try {
            Map<String, Number> tongQuan = controller.getTongQuan();
            
            lblTongSoXe.setText(String.valueOf(tongQuan.getOrDefault("tongSoXe", 0)));
            lblTongSoKhachHang.setText(String.valueOf(tongQuan.getOrDefault("tongSoKhachHang", 0)));
            lblTongSoHopDong.setText(String.valueOf(tongQuan.getOrDefault("tongSoHopDong", 0)));
            lblTongDoanhThu.setText(currencyFormat.format(tongQuan.getOrDefault("tongDoanhThu", 0)));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu tổng quan: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
//    private void loadDataByYear(int year) {
//        try {
//            // Load dữ liệu doanh thu theo tháng
//            Map<Integer, Double> doanhThuThang = controller.getDoanhThuTheoThang(year);
//            updateDoanhThuThangChart(doanhThuThang, year);
//            
//            // Load dữ liệu doanh thu theo khách hàng
//            List<KhachHangDoanhThu> doanhThuKhachHang = controller.getDoanhThuTheoKhachHang(year);
//            updateDoanhThuKhachHangChart(doanhThuKhachHang, year);
//            updateDoanhThuKhachHangTable(doanhThuKhachHang);
//            
//            // Load dữ liệu doanh thu theo xe
//            List<XeDoanhThu> doanhThuXe = controller.getDoanhThuTheoXe(year);
//            updateDoanhThuXeChart(doanhThuXe, year);
//            updateDoanhThuXeTable(doanhThuXe);
//            
//            // THÊM MỚI: Load và cập nhật dữ liệu top 5 hợp đồng
//            updateTopContractsTable(year);
//            
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu năm " + year + ": " + e.getMessage(), 
//                    "Lỗi", JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
//    }
    private void loadDataByYear(int year) {
        try {
            // Sử dụng cùng kết nối hiện có cho tất cả truy vấn
            // Không tạo kết nối mới ở đây

            // Load dữ liệu doanh thu theo tháng
            Map<Integer, Double> doanhThuThang = controller.getDoanhThuTheoThang(year);
            updateDoanhThuThangChart(doanhThuThang, year);

            // Load dữ liệu doanh thu theo khách hàng
            List<KhachHangDoanhThu> doanhThuKhachHang = controller.getDoanhThuTheoKhachHang(year);
            updateDoanhThuKhachHangChart(doanhThuKhachHang, year);
            updateDoanhThuKhachHangTable(doanhThuKhachHang);

            // Load dữ liệu doanh thu theo xe
            List<XeDoanhThu> doanhThuXe = controller.getDoanhThuTheoXe(year);
            updateDoanhThuXeChart(doanhThuXe, year);
            updateDoanhThuXeTable(doanhThuXe);

            // Load dữ liệu top 5 hợp đồng
            updateTopContractsTable(year);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu năm " + year + ": " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    
    private void updateDoanhThuThangChart(Map<Integer, Double> data, int year, boolean isBarChart) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Thêm dữ liệu vào dataset
        for (int i = 1; i <= 12; i++) {
            Double value = data.getOrDefault(i, 0.0);
            dataset.addValue(value, "Doanh thu", "Tháng " + i);
        }

        JFreeChart chart;

        if (isBarChart) {
            // Tạo biểu đồ cột
            chart = ChartFactory.createBarChart(
                    "Biểu đồ doanh thu theo tháng năm " + year, 
                    "Tháng", 
                    "Doanh thu (VNĐ)", 
                    dataset,
                    PlotOrientation.VERTICAL, 
                    true, 
                    true, 
                    false);

            // Tùy chỉnh màu sắc và style cho biểu đồ cột
            CategoryPlot plot = chart.getCategoryPlot();

            // Đổi màu nền plot thành màu xám nhạt
            plot.setBackgroundPaint(new Color(240, 240, 240));

            // Đổi màu lưới thành trắng để tăng độ tương phản
            plot.setDomainGridlinePaint(Color.WHITE);
            plot.setRangeGridlinePaint(Color.WHITE);

            // Đổi độ dày của lưới
            plot.setDomainGridlineStroke(new BasicStroke(1.0f));
            plot.setRangeGridlineStroke(new BasicStroke(1.0f));

            // Tùy chỉnh renderer để làm đẹp các cột
            BarRenderer renderer = (BarRenderer) plot.getRenderer();

            // Đổi màu cột thành gradient từ xanh đậm đến xanh nhạt
            GradientPaint gradientPaint = new GradientPaint(
                    0, 0, new Color(30, 144, 255),  // Xanh đậm
                    0, 500, new Color(135, 206, 250) // Xanh nhạt
            );

            renderer.setSeriesPaint(0, gradientPaint);

            // Bỏ đường viền đen xung quanh cột
            renderer.setDrawBarOutline(false);

            // Làm tròn góc cột
            renderer.setBarPainter(new StandardBarPainter());

            // Shadow effect cho các cột (tùy chọn)
            renderer.setShadowVisible(true);
            renderer.setShadowPaint(new Color(0, 0, 0, 50));
            renderer.setShadowXOffset(2.0);
            renderer.setShadowYOffset(2.0);

            // Hiển thị giá trị bên trong các cột
            renderer.setDefaultItemLabelsVisible(true);

            // Format số với dấu phân cách hàng nghìn
            NumberFormat formatter = NumberFormat.getIntegerInstance();

            // Định dạng nhãn hiển thị trong cột
            renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator(
                    "{2}", formatter));
            renderer.setDefaultItemLabelFont(new Font("Segoe UI", Font.BOLD, 11));

            // Đặt vị trí của nhãn ở GIỮA mỗi cột
            renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(
                    ItemLabelAnchor.CENTER, TextAnchor.CENTER));

            // Đặt màu chữ trắng để nhìn rõ trên nền xanh
            renderer.setDefaultItemLabelPaint(Color.WHITE);
        } else {
            // Tạo biểu đồ đường
            chart = ChartFactory.createLineChart(
                    "Biểu đồ doanh thu theo tháng năm " + year, 
                    "Tháng", 
                    "Doanh thu (VNĐ)", 
                    dataset,
                    PlotOrientation.VERTICAL, 
                    true, 
                    true, 
                    false);

            // Tùy chỉnh màu sắc và style cho biểu đồ đường
            CategoryPlot plot = chart.getCategoryPlot();

            // Đổi màu nền plot thành màu xám nhạt
            plot.setBackgroundPaint(new Color(240, 240, 240));

            // Đổi màu lưới thành trắng để tăng độ tương phản
            plot.setDomainGridlinePaint(Color.WHITE);
            plot.setRangeGridlinePaint(Color.WHITE);

            // Đổi độ dày của lưới
            plot.setDomainGridlineStroke(new BasicStroke(1.0f));
            plot.setRangeGridlineStroke(new BasicStroke(1.0f));

            // Tùy chỉnh renderer cho đường
            org.jfree.chart.renderer.category.LineAndShapeRenderer renderer = 
                    (org.jfree.chart.renderer.category.LineAndShapeRenderer) plot.getRenderer();

            // Đặt màu đường là xanh dương đậm
            renderer.setSeriesPaint(0, new Color(30, 144, 255));

            // Làm đường dày hơn
            renderer.setSeriesStroke(0, new BasicStroke(3.0f));

            // Hiển thị các điểm dữ liệu
            renderer.setSeriesShapesVisible(0, true);

            // Đặt kích thước của các điểm dữ liệu
            renderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-5.0, -5.0, 10.0, 10.0));

            // Đặt màu fill cho các điểm
            renderer.setSeriesShapesFilled(0, true);
            renderer.setSeriesFillPaint(0, Color.WHITE);

            // Đặt màu outline cho các điểm
            renderer.setUseFillPaint(true);
            renderer.setUseOutlinePaint(true);
            renderer.setSeriesOutlinePaint(0, new Color(30, 144, 255));
            renderer.setSeriesOutlineStroke(0, new BasicStroke(2.0f));

            // Hiển thị giá trị trên mỗi điểm
            renderer.setDefaultItemLabelsVisible(true);

            // Format số với dấu phân cách hàng nghìn
            NumberFormat formatter = NumberFormat.getIntegerInstance();

            // Định dạng nhãn hiển thị
            renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator(
                    "{2}", formatter));
            renderer.setDefaultItemLabelFont(new Font("Segoe UI", Font.BOLD, 11));

            // Đặt vị trí của nhãn phía trên mỗi điểm
            renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(
                    ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER));

            // Đặt màu chữ
            renderer.setDefaultItemLabelPaint(new Color(50, 50, 50));
        }

        // Làm đẹp legend
        LegendTitle legend = chart.getLegend();
        legend.setBackgroundPaint(new Color(250, 250, 250));
        legend.setItemFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Làm đẹp tiêu đề biểu đồ
        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 16));
        chart.getTitle().setPaint(new Color(51, 51, 51));

        // Làm đẹp font chữ cho các trục
        CategoryPlot plot = chart.getCategoryPlot();
        plot.getDomainAxis().setLabelFont(new Font("Segoe UI", Font.BOLD, 12));
        plot.getRangeAxis().setLabelFont(new Font("Segoe UI", Font.BOLD, 12));
        plot.getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
        plot.getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));

        chartPanelDoanhThuThang.setChart(chart);
    }

    private void updateDoanhThuThangChart(Map<Integer, Double> data, int year) {
        updateDoanhThuThangChart(data, year, isBarChart); // Sử dụng biến isBarChart của lớp
    }
    private void updateDoanhThuKhachHangChart(List<KhachHangDoanhThu> data, int year) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Lấy top 10 khách hàng có doanh thu cao nhất
        double tongDoanhThu = 0;
        for (KhachHangDoanhThu kh : data) {
            tongDoanhThu += kh.getDoanhThu();
        }

        // Lấy top 10 và tính tổng doanh thu của họ
        double doanhThuTop10 = 0;
        int max = Math.min(10, data.size());
        for (int i = 0; i < max; i++) {
            KhachHangDoanhThu kh = data.get(i);
            dataset.setValue(kh.getHoTen(), kh.getDoanhThu());
            doanhThuTop10 += kh.getDoanhThu();
        }

        // Thêm mục "Khách hàng khác" nếu có nhiều hơn 10 khách hàng
        if (data.size() > 10) {
            double doanhThuKhac = tongDoanhThu - doanhThuTop10;
            dataset.setValue("Khách hàng khác", doanhThuKhac);
        }

        JFreeChart chart = ChartFactory.createPieChart(
               // "Top 10 khách hàng có doanh thu cao nhất năm " + year, 
                "Doanh thu theo khách hàng",
                dataset, 
                true, 
                true, 
                false);

        // Làm đẹp biểu đồ
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 16));
        chart.getTitle().setPaint(new Color(51, 51, 51));

        PiePlot plot = (PiePlot) chart.getPlot();

        // Đặt màu nền
        plot.setBackgroundPaint(new Color(240, 240, 240));
        plot.setOutlineVisible(false);

        // Thiết lập shadow
        plot.setShadowPaint(new Color(0, 0, 0, 50));
        plot.setShadowXOffset(4.0);
        plot.setShadowYOffset(4.0);

        // Thiết lập label
        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 11));
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 180));
        plot.setLabelOutlinePaint(null);
        plot.setLabelShadowPaint(null);

        // Định dạng số hiển thị phần trăm
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(1);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}: {2}", NumberFormat.getInstance(), percentFormat));

        // Đặt các màu đẹp cho các phần
        Color[] colors = {
            new Color(41, 121, 255),    // Xanh dương đậm
            new Color(0, 180, 216),     // Xanh dương nhạt
            new Color(144, 85, 253),    // Tím
            new Color(255, 51, 102),    // Hồng
            new Color(255, 94, 25),     // Cam
            new Color(255, 187, 0),     // Vàng
            new Color(60, 186, 159),    // Xanh lá
            new Color(72, 133, 237),    // Xanh dương Google
            new Color(219, 68, 55),     // Đỏ Google
            new Color(15, 157, 88)      // Xanh lá Google
        };

        // Áp dụng màu cho từng phần (trừ phần "Khách hàng khác")
        List<?> keys = dataset.getKeys();
        int colorIndex = 0;
        for (Object key : keys) {
            String k = key.toString();
            if (!k.equals("Khách hàng khác")) {
                plot.setSectionPaint(k, colors[colorIndex % colors.length]);
                colorIndex++;
            }
        }

        // Đặt màu riêng cho "Khách hàng khác"
        plot.setSectionPaint("Khách hàng khác", new Color(200, 200, 200));

        // Làm đẹp legend
        LegendTitle legend = chart.getLegend();
        legend.setBackgroundPaint(new Color(250, 250, 250));
        legend.setItemFont(new Font("Segoe UI", Font.PLAIN, 11));

        // Điều chỉnh spacing
        plot.setInteriorGap(0.04);
        plot.setLabelGap(0.02);

        chartPanelDoanhThuKhachHang.setChart(chart);
    }


    private void updateDoanhThuKhachHangTable(List<KhachHangDoanhThu> data) {
        Object[][] tableData = new Object[data.size()][5];
        
        for (int i = 0; i < data.size(); i++) {
            KhachHangDoanhThu kh = data.get(i);
            tableData[i][0] = i + 1;
            tableData[i][1] = kh.getMaKH();
            tableData[i][2] = kh.getHoTen();
            tableData[i][3] = kh.getSoHopDong();
            tableData[i][4] = currencyFormat.format(kh.getDoanhThu());
        }
        
        String[] columnNames = {"STT", "Mã KH", "Họ tên", "Số hợp đồng", "Doanh thu"};
        
        tblDoanhThuKhachHang.setModel(new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }
    
    private void updateDoanhThuXeChart(List<XeDoanhThu> data, int year) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Lấy top 10 xe có doanh thu cao nhất
        int max = Math.min(10, data.size());
        for (int i = 0; i < max; i++) {
            XeDoanhThu xe = data.get(i);
            // Sử dụng tên xe thay vì mã xe
            dataset.addValue(xe.getDoanhThu(), "Doanh thu", xe.getTenXe());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Top 10 xe có doanh thu cao nhất năm " + year, 
                "Tên xe", 
                "Doanh thu (VNĐ)", 
                dataset,
                PlotOrientation.VERTICAL, 
                false,  // Không hiện legend
                true, 
                false);

        // Tùy chỉnh màu sắc và style
        CategoryPlot plot = chart.getCategoryPlot();

        // Đổi màu nền plot thành màu xám nhạt
        plot.setBackgroundPaint(new Color(240, 240, 240));

        // Đổi màu lưới thành trắng để tăng độ tương phản
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        // Đổi độ dày của lưới
        plot.setDomainGridlineStroke(new BasicStroke(1.0f));
        plot.setRangeGridlineStroke(new BasicStroke(1.0f));

        // Tùy chỉnh renderer để làm đẹp các cột
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // Đổi màu cột thành gradient từ xanh lá đậm đến xanh lá nhạt
        GradientPaint gradientPaint = new GradientPaint(
                0, 0, new Color(60, 186, 159),  // Xanh lá đậm
                0, 500, new Color(144, 238, 144) // Xanh lá nhạt
        );

        renderer.setSeriesPaint(0, gradientPaint);

        // Bỏ đường viền đen xung quanh cột
        renderer.setDrawBarOutline(false);

        // Làm tròn góc cột
        renderer.setBarPainter(new StandardBarPainter());

        // Shadow effect cho các cột
        renderer.setShadowVisible(true);
        renderer.setShadowPaint(new Color(0, 0, 0, 50));
        renderer.setShadowXOffset(2.0);
        renderer.setShadowYOffset(2.0);

        // Hiển thị giá trị trên mỗi cột
        renderer.setDefaultItemLabelsVisible(true);

        // Format số với dấu phân cách hàng nghìn
        NumberFormat formatter = NumberFormat.getIntegerInstance();

        // Định dạng nhãn hiển thị trên mỗi cột
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator(
                "{2}", formatter));
        renderer.setDefaultItemLabelFont(new Font("Segoe UI", Font.BOLD, 11));

        // *** THAY ĐỔI QUAN TRỌNG: Đặt vị trí của nhãn hiển thị BÊN TRONG đầu mỗi cột ***
        renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.CENTER, TextAnchor.CENTER));

        // Đặt màu chữ trắng để nhìn rõ trên nền xanh
        renderer.setDefaultItemLabelPaint(Color.WHITE);

        // Làm đẹp tiêu đề biểu đồ
        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 16));
        chart.getTitle().setPaint(new Color(51, 51, 51));

        // Làm đẹp font chữ cho các trục
        plot.getDomainAxis().setLabelFont(new Font("Segoe UI", Font.BOLD, 12));
        plot.getRangeAxis().setLabelFont(new Font("Segoe UI", Font.BOLD, 12));
        plot.getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
        plot.getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));

        // Giữ nhãn trục X ngang, KHÔNG xoay
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);

        // Để tránh vượt ra ngoài ranh giới, giới hạn độ dài hiển thị của tên xe
        domainAxis.setMaximumCategoryLabelLines(2); // Cho phép hiển thị tối đa 2 dòng
        domainAxis.setMaximumCategoryLabelWidthRatio(0.5f); // Giới hạn độ rộng nhãn

        // *** THAY ĐỔI QUAN TRỌNG: Điều chỉnh thêm khoảng trống phía trên để có thể hiển thị giá trị ***
        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

        // Đặt khoảng cách giữa các cột
        renderer.setItemMargin(0.1);

        // *** THAY ĐỔI QUAN TRỌNG: Đặt giới hạn trên cao hơn cho trục Y để có chỗ cho nhãn ***
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();

        // Tính giá trị lớn nhất
        double maxValue = 0;
        for (int i = 0; i < dataset.getRowCount(); i++) {
            for (int j = 0; j < dataset.getColumnCount(); j++) {
                Number value = dataset.getValue(i, j);
                if (value != null) {
                    maxValue = Math.max(maxValue, value.doubleValue());
                }
            }
        }

        // Đặt giới hạn trên cao hơn 15% để có chỗ cho nhãn
        rangeAxis.setUpperBound(maxValue * 1.15);

        chartPanelDoanhThuXe.setChart(chart);
    }



    
    private void updateDoanhThuXeTable(List<XeDoanhThu> data) {
        Object[][] tableData = new Object[data.size()][6];
        
        for (int i = 0; i < data.size(); i++) {
            XeDoanhThu xe = data.get(i);
            tableData[i][0] = i + 1;
            tableData[i][1] = xe.getMaXe();
            tableData[i][2] = xe.getTenXe();
            tableData[i][3] = xe.getBienSo();
            tableData[i][4] = xe.getSoLuotThue();
            tableData[i][5] = currencyFormat.format(xe.getDoanhThu());
        }
        
        String[] columnNames = {"STT", "Mã xe", "Tên xe", "Biển số", "Số lượt thuê", "Doanh thu"};
        
        tblDoanhThuXe.setModel(new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }
}