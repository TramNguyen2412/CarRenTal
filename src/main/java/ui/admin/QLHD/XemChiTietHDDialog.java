//package ui.admin.QLHD;
//
//import dao.ChiTietHDDao;
//import model.ChiTietHD;
//import model.HopDong;
//import controller.HopDongController;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.border.MatteBorder;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.JTableHeader;
//import java.awt.*;
//import java.awt.event.*;
//import java.awt.image.BufferedImage;
//import java.text.NumberFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
//
//public class XemChiTietHDDialog extends JDialog {
//    private HopDong hopDong;
//    private ChiTietHDDao chiTietHDDao;
//    private DefaultTableModel tableModel;
//    private JTable rentTable;
//    private JLabel totalValueLabel;
//    private Color primaryColor = new Color(41, 128, 185);
//    private Color accentColor = new Color(46, 204, 113);
//    
//    public XemChiTietHDDialog(Window owner, String maHD) {
//        super(owner, "Hợp đồng thuê xe", ModalityType.APPLICATION_MODAL);
//        
//        // Lấy thông tin hợp đồng từ database
//        HopDongController hopDongController = new HopDongController();
//        this.hopDong = hopDongController.getHopDongByMa(maHD);
//        
//        if (this.hopDong == null) {
//            JOptionPane.showMessageDialog(owner, 
//                "Không tìm thấy thông tin hợp đồng với mã: " + maHD, 
//                "Lỗi", JOptionPane.ERROR_MESSAGE);
//            dispose();
//            return;
//        }
//        
//        this.chiTietHDDao = new ChiTietHDDao();
//        this.setTitle("Hợp đồng thuê xe " + hopDong.getMaHD() + " - " + hopDong.getTenKH());
//        
//        initComponents();
//        loadData();
//    }
//    
//    private void initComponents() {
//        // Main container
//        JPanel mainPanel = new JPanel(new BorderLayout());
//        mainPanel.setBackground(Color.WHITE);
//        
//        // ===== HEADER PANEL =====
//        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
//        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
//        headerPanel.setBackground(Color.WHITE);
//        
//        // Left header - Customer avatar and info
//        JPanel customerInfo = new JPanel(new BorderLayout(15, 0));
//        customerInfo.setBackground(Color.WHITE);
//        
//        // Avatar
//        JLabel avatarLabel = new JLabel(createAvatar(hopDong.getTenKH()));
//        
//        // Customer info
//        JPanel infoPanel = new JPanel();
//        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
//        infoPanel.setBackground(Color.WHITE);
//        
//        JLabel nameLabel = new JLabel(hopDong.getTenKH());
//        nameLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
//        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        
//        JLabel contractLabel = new JLabel("Mã hợp đồng: " + hopDong.getMaHD());
//        contractLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        contractLabel.setForeground(new Color(120, 120, 120));
//        contractLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        
//        infoPanel.add(nameLabel);
//        infoPanel.add(Box.createVerticalStrut(5));
//        infoPanel.add(contractLabel);
//        
//        customerInfo.add(avatarLabel, BorderLayout.WEST);
//        customerInfo.add(infoPanel, BorderLayout.CENTER);
//        
//        // Right header - Status button and date
//        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        statusPanel.setBackground(Color.WHITE);
//        
//        // Status button
//        JButton statusButton = new JButton(hopDong.getTrangThai());
//        styleStatusButton(statusButton, hopDong.getTrangThai());
//        
//        // Date label
//        JLabel dateLabel = new JLabel(formatDateTime(hopDong.getNgayLap()));
//        dateLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        dateLabel.setForeground(new Color(120, 120, 120));
//        
//        JPanel rightPanel = new JPanel();
//        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
//        rightPanel.setBackground(Color.WHITE);
//        
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        buttonPanel.setBackground(Color.WHITE);
//        buttonPanel.add(statusButton);
//        
//        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        datePanel.setBackground(Color.WHITE);
//        datePanel.add(dateLabel);
//        
//        rightPanel.add(buttonPanel);
//        rightPanel.add(datePanel);
//        
//        statusPanel.add(rightPanel);
//        
//        headerPanel.add(customerInfo, BorderLayout.WEST);
//        headerPanel.add(statusPanel, BorderLayout.EAST);
//        
//        // ===== CONTENT PANEL =====
//        JPanel contentPanel = new JPanel(new BorderLayout());
//        contentPanel.setBackground(Color.WHITE);
//        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
//        
//        // Tab panel with underline
//        JPanel tabPanel = new JPanel(new BorderLayout());
//        tabPanel.setBackground(Color.WHITE);
//        
//        JPanel tabBar = new JPanel();
//        tabBar.setLayout(new BoxLayout(tabBar, BoxLayout.X_AXIS));
//        tabBar.setBackground(Color.WHITE);
//        
//        JLabel overviewTab = new JLabel("Tổng quan");
//        overviewTab.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        overviewTab.setForeground(primaryColor);
//        overviewTab.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 20));
//        
//        tabBar.add(overviewTab);
//        tabBar.add(Box.createHorizontalGlue());
//        
//        // Add bottom border line
//        tabPanel.add(tabBar, BorderLayout.CENTER);
//        tabPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(225, 225, 225)));
//        
//        // Table container
//        JPanel tableContainerPanel = new JPanel(new BorderLayout());
//        tableContainerPanel.setBackground(Color.WHITE);
//        tableContainerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
//        
//        // Create table with correct column names
//        String[] columnNames = {"STT", "Xe", "Hình thức", "Thời gian thuê", "Đơn giá", "Số ngày", "Thành tiền"};
//        tableModel = new DefaultTableModel(columnNames, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        
//        rentTable = new JTable(tableModel);
//        setupTable(rentTable);
//        
//        JScrollPane scrollPane = new JScrollPane(rentTable);
//        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225)));
//        scrollPane.getViewport().setBackground(Color.WHITE);
//        
//        // Total panel at bottom
//        JPanel totalPanel = new JPanel(new BorderLayout());
//        totalPanel.setBackground(Color.WHITE);
//        totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
//        
//        JPanel rightAlignPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        rightAlignPanel.setBackground(Color.WHITE);
//        
//        JLabel totalLabel = new JLabel("Tổng tiền:");
//        totalLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        
//        totalValueLabel = new JLabel(formatCurrency(hopDong.getTongTien()));
//        totalValueLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        totalValueLabel.setForeground(accentColor);
//        
//        rightAlignPanel.add(totalLabel);
//        rightAlignPanel.add(totalValueLabel);
//        totalPanel.add(rightAlignPanel, BorderLayout.EAST);
//        
//        tableContainerPanel.add(scrollPane, BorderLayout.CENTER);
//        tableContainerPanel.add(totalPanel, BorderLayout.SOUTH);
//        
//        contentPanel.add(tabPanel, BorderLayout.NORTH);
//        contentPanel.add(tableContainerPanel, BorderLayout.CENTER);
//        
//        // Bottom button panel
//        JPanel buttonBottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        buttonBottomPanel.setBackground(Color.WHITE);
//        buttonBottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
//        
//        JButton closeButton = new JButton("Đóng");
//        closeButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        closeButton.addActionListener(e -> dispose());
//        
//        buttonBottomPanel.add(closeButton);
//        
//        // Add components to main panel
//        mainPanel.add(headerPanel, BorderLayout.NORTH);
//        mainPanel.add(contentPanel, BorderLayout.CENTER);
//        mainPanel.add(buttonBottomPanel, BorderLayout.SOUTH);
//        
//        // Set dialog properties
//        setContentPane(mainPanel);
//        setSize(1050, 600);
//        setLocationRelativeTo(getOwner());
//        setResizable(true);
//    }
//    
//    private void setupTable(JTable table) {
//        table.setRowHeight(40);
//        table.setShowHorizontalLines(true);
//        table.setShowVerticalLines(false);
//        table.setGridColor(new Color(225, 225, 225));
//        table.setBackground(Color.WHITE);
//        table.setSelectionBackground(new Color(240, 240, 240));
//        table.setSelectionForeground(Color.BLACK);
//        table.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        table.setBorder(null);
//        
//        // Set header style
//        JTableHeader header = table.getTableHeader();
//        header.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        header.setBackground(new Color(245, 245, 245));
//        header.setBorder(new MatteBorder(0, 0, 1, 0, new Color(225, 225, 225)));
//        header.setPreferredSize(new Dimension(header.getWidth(), 40));
//        header.setReorderingAllowed(false);
//        
//        // Custom rendering
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//        
//        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
//        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
//        
//        // Set column renderers
//        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // STT
//        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);  // Đơn giá
//        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Số ngày
//        table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);  // Thành tiền
//        
//        // Set column widths
//        table.getColumnModel().getColumn(0).setPreferredWidth(50);
//        table.getColumnModel().getColumn(1).setPreferredWidth(200);
//        table.getColumnModel().getColumn(2).setPreferredWidth(100);
//        table.getColumnModel().getColumn(3).setPreferredWidth(200);
//        table.getColumnModel().getColumn(4).setPreferredWidth(120);
//        table.getColumnModel().getColumn(5).setPreferredWidth(80);
//        table.getColumnModel().getColumn(6).setPreferredWidth(120);
//        
//        // Disable column resizing (optional)
//        table.getTableHeader().setResizingAllowed(true);
//    }
//    
//    private void loadData() {
//        try {
//            // Lấy danh sách chi tiết thuê xe từ database
//            List<ChiTietHD> danhSachXeThue = chiTietHDDao.getChiTietHDByMaHD(hopDong.getMaHD());
//            
//            // Cập nhật bảng
//            tableModel.setRowCount(0);
//            
//            if (danhSachXeThue.isEmpty()) {
//                tableModel.addRow(new Object[] {"", "Không có dữ liệu", "", "", "", "", ""});
//            } else {
//                int stt = 1;
//                double tongTien = 0;
//                
//                for (ChiTietHD ct : danhSachXeThue) {
//                    String tenXe = ct.getTenXe() + (ct.getBienSo() != null ? " - " + ct.getBienSo() : "");
//                    String thoiGianThue = formatDate(ct.getNgayBatDau()) + " - " + formatDate(ct.getNgayKetThuc());
//                    double thanhTien = ct.getGiaThueNgay() * ct.getSoNgayThue();
//                    tongTien += thanhTien;
//                    
//                    Object[] row = {
//                        stt++,
//                        tenXe,
//                        "Tự lái",
//                        thoiGianThue,
//                        formatCurrency(ct.getGiaThueNgay()),
//                        ct.getSoNgayThue(),
//                        formatCurrency(thanhTien)
//                    };
//                    tableModel.addRow(row);
//                }
//                
//                // Cập nhật tổng tiền
//                totalValueLabel.setText(formatCurrency(tongTien));
//            }
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this,
//                    "Lỗi khi tải dữ liệu chi tiết thuê xe: " + e.getMessage(),
//                    "Lỗi",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//    
//    // Helper methods
//    private ImageIcon createAvatar(String name) {
//        int size = 60;
//        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2d = img.createGraphics();
//        
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setColor(primaryColor);
//        g2d.fillOval(0, 0, size, size);
//        
//        g2d.setColor(Color.WHITE);
//        g2d.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 28));
//        
//        String firstLetter = "";
//        if (name != null && !name.isEmpty()) {
//            firstLetter = name.substring(0, 1).toUpperCase();
//        }
//        
//        FontMetrics fm = g2d.getFontMetrics();
//        int textWidth = fm.stringWidth(firstLetter);
//        int textHeight = fm.getHeight();
//        
//        g2d.drawString(firstLetter, (size - textWidth) / 2, ((size - textHeight) / 2) + fm.getAscent());
//        g2d.dispose();
//        
//        return new ImageIcon(img);
//    }
//    
//    private void styleStatusButton(JButton button, String status) {
//        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
//        button.setBorderPainted(false);
//        button.setFocusPainted(false);
//        button.setContentAreaFilled(true);
//        button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
//        
//        Color bgColor;
//        switch (status.toLowerCase()) {
//            case "chờ xác nhận":
//                bgColor = new Color(255, 193, 7); // Amber
//                break;
//            case "đã xác nhận":
//                bgColor = new Color(76, 175, 80); // Green
//                break;
//            case "đang thuê":
//                bgColor = new Color(33, 150, 243); // Blue
//                break;
//            case "đã hoàn thành":
//                bgColor = new Color(46, 204, 113); // Green
//                break;
//            case "đã hủy":
//                bgColor = new Color(239, 83, 80); // Red
//                break;
//            default:
//                bgColor = new Color(158, 158, 158); // Grey
//        }
//        
//        button.setBackground(bgColor);
//        button.setForeground(Color.WHITE);
//    }
//    
//    private String formatDate(Date date) {
//        if (date == null) return "";
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        return sdf.format(date);
//    }
//    
//    private String formatDateTime(Date date) {
//        if (date == null) return "";
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//        return sdf.format(date);
//    }
//    
//    private String formatCurrency(double amount) {
//        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
//        return currencyFormat.format(amount) + " đ";
//    }
//}

package ui.admin.QLHD;

import dao.ChiTietHDDao;
import model.ChiTietHD;
import model.HopDong;
import model.KhachHang;
import controller.HopDongController;
import controller.KhachHangController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

// Thêm import cho iText PDF
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import javax.swing.filechooser.FileNameExtensionFilter;

public class XemChiTietHDDialog extends JDialog {
    private HopDong hopDong;
    private ChiTietHDDao chiTietHDDao;
    private KhachHangController khachHangController; // Thêm controller khách hàng
    private DefaultTableModel tableModel;
    private JTable rentTable;
    private JLabel totalValueLabel;
    private Color primaryColor = new Color(41, 128, 185);
    private Color accentColor = new Color(46, 204, 113);
    private List<ChiTietHD> danhSachXeThue;
    private KhachHang khachHang; // Thêm đối tượng khách hàng
    
    public XemChiTietHDDialog(Window owner, String maHD) {
        super(owner, "Hợp đồng thuê xe", ModalityType.APPLICATION_MODAL);
        
        // Khởi tạo controllers
        HopDongController hopDongController = new HopDongController();
        this.khachHangController = new KhachHangController();
        
        // Lấy thông tin hợp đồng từ database
        this.hopDong = hopDongController.getHopDongByMa(maHD);
        
        if (this.hopDong == null) {
            JOptionPane.showMessageDialog(owner, 
                "Không tìm thấy thông tin hợp đồng với mã: " + maHD, 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        // Lấy thông tin khách hàng nếu có mã KH
        if (hopDong.getMaKH() != null && !hopDong.getMaKH().isEmpty()) {
            this.khachHang = khachHangController.getKhachHangByMa(hopDong.getMaKH());
        }
        
        this.chiTietHDDao = new ChiTietHDDao();
        this.setTitle("Hợp đồng thuê xe " + hopDong.getMaHD() + " - " + hopDong.getTenKH());
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // ===== HEADER PANEL =====
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        headerPanel.setBackground(Color.WHITE);
        
        // Left header - Customer avatar and info
        JPanel customerInfo = new JPanel(new BorderLayout(15, 0));
        customerInfo.setBackground(Color.WHITE);
        
        // Avatar
        JLabel avatarLabel = new JLabel(createAvatar(hopDong.getTenKH()));
        
        // Customer info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(hopDong.getTenKH());
        nameLabel.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, java.awt.Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel contractLabel = new JLabel("Mã hợp đồng: " + hopDong.getMaHD());
        contractLabel.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, java.awt.Font.PLAIN, 14));
        contractLabel.setForeground(new Color(120, 120, 120));
        contractLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(contractLabel);
        
        customerInfo.add(avatarLabel, BorderLayout.WEST);
        customerInfo.add(infoPanel, BorderLayout.CENTER);
        
        // Right header - Status button and date
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setBackground(Color.WHITE);
        
        // Status button
        JButton statusButton = new JButton(hopDong.getTrangThai());
        styleStatusButton(statusButton, hopDong.getTrangThai());
        
        // Date label
        JLabel dateLabel = new JLabel(formatDateTime(hopDong.getNgayLap()));
        dateLabel.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, java.awt.Font.PLAIN, 14));
        dateLabel.setForeground(new Color(120, 120, 120));
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(statusButton);
        
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        datePanel.setBackground(Color.WHITE);
        datePanel.add(dateLabel);
        
        rightPanel.add(buttonPanel);
        rightPanel.add(datePanel);
        
        statusPanel.add(rightPanel);
        
        headerPanel.add(customerInfo, BorderLayout.WEST);
        headerPanel.add(statusPanel, BorderLayout.EAST);
        
        // ===== CONTENT PANEL =====
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        
        // Tab panel with underline
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBackground(Color.WHITE);
        
        JPanel tabBar = new JPanel();
        tabBar.setLayout(new BoxLayout(tabBar, BoxLayout.X_AXIS));
        tabBar.setBackground(Color.WHITE);
        
        JLabel overviewTab = new JLabel("Tổng quan");
        overviewTab.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, java.awt.Font.BOLD, 14));
        overviewTab.setForeground(primaryColor);
        overviewTab.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 20));
        
        tabBar.add(overviewTab);
        tabBar.add(Box.createHorizontalGlue());
        
        // Add bottom border line
        tabPanel.add(tabBar, BorderLayout.CENTER);
        tabPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(225, 225, 225)));
        
        // Table container
        JPanel tableContainerPanel = new JPanel(new BorderLayout());
        tableContainerPanel.setBackground(Color.WHITE);
        tableContainerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Create table with correct column names
        String[] columnNames = {"STT", "Xe", "Hình thức", "Thời gian thuê", "Đơn giá", "Số ngày", "Thành tiền"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        rentTable = new JTable(tableModel);
        setupTable(rentTable);
        
        JScrollPane scrollPane = new JScrollPane(rentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Total panel at bottom
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        
        JPanel rightAlignPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightAlignPanel.setBackground(Color.WHITE);
        
        JLabel totalLabel = new JLabel("Tổng tiền:");
        totalLabel.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, java.awt.Font.BOLD, 14));
        
        totalValueLabel = new JLabel(formatCurrency(hopDong.getTongTien()));
        totalValueLabel.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, java.awt.Font.BOLD, 14));
        totalValueLabel.setForeground(accentColor);
        
        rightAlignPanel.add(totalLabel);
        rightAlignPanel.add(totalValueLabel);
        totalPanel.add(rightAlignPanel, BorderLayout.EAST);
        
        tableContainerPanel.add(scrollPane, BorderLayout.CENTER);
        tableContainerPanel.add(totalPanel, BorderLayout.SOUTH);
        
        contentPanel.add(tabPanel, BorderLayout.NORTH);
        contentPanel.add(tableContainerPanel, BorderLayout.CENTER);
        
        // Bottom button panel
        JPanel buttonBottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonBottomPanel.setBackground(Color.WHITE);
        buttonBottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        
        // Thêm nút xuất PDF
        JButton exportPdfButton = new JButton("Xuất PDF");
        exportPdfButton.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, java.awt.Font.BOLD, 14));
        exportPdfButton.setBackground(new Color(220, 53, 69)); // Màu đỏ
        exportPdfButton.setForeground(Color.WHITE);
        exportPdfButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportPdfButton.setFocusPainted(false);
        exportPdfButton.setBorderPainted(false);
        exportPdfButton.addActionListener(e -> exportToPdf());
        
        JButton closeButton = new JButton("Đóng");
        closeButton.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, java.awt.Font.PLAIN, 14));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());
        
        buttonBottomPanel.add(exportPdfButton);
        buttonBottomPanel.add(Box.createHorizontalStrut(10)); // Thêm khoảng cách giữa các nút
        buttonBottomPanel.add(closeButton);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonBottomPanel, BorderLayout.SOUTH);
        
        // Set dialog properties
        setContentPane(mainPanel);
        setSize(1050, 600);
        setLocationRelativeTo(getOwner());
        setResizable(true);
    }
    
    private void setupTable(JTable table) {
        table.setRowHeight(40);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(225, 225, 225));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(240, 240, 240));
        table.setSelectionForeground(Color.BLACK);
        table.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, java.awt.Font.PLAIN, 14));
        table.setBorder(null);
        
        // Set header style
        JTableHeader header = table.getTableHeader();
        header.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, java.awt.Font.BOLD, 14));
        header.setBackground(new Color(245, 245, 245));
        header.setBorder(new MatteBorder(0, 0, 1, 0, new Color(225, 225, 225)));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        // Custom rendering
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        // Set column renderers
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // STT
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);  // Đơn giá
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Số ngày
        table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);  // Thành tiền
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);
        
        // Disable column resizing (optional)
        table.getTableHeader().setResizingAllowed(true);
    }
    
    private void loadData() {
        try {
            // Lấy danh sách chi tiết thuê xe từ database
            danhSachXeThue = chiTietHDDao.getChiTietHDByMaHD(hopDong.getMaHD());
            
            // Cập nhật bảng
            tableModel.setRowCount(0);
            
            if (danhSachXeThue.isEmpty()) {
                tableModel.addRow(new Object[] {"", "Không có dữ liệu", "", "", "", "", ""});
            } else {
                int stt = 1;
                double tongTien = 0;
                
                for (ChiTietHD ct : danhSachXeThue) {
                    String tenXe = ct.getTenXe() + (ct.getBienSo() != null ? " - " + ct.getBienSo() : "");
                    String thoiGianThue = formatDate(ct.getNgayBatDau()) + " - " + formatDate(ct.getNgayKetThuc());
                    double thanhTien = ct.getGiaThueNgay() * ct.getSoNgayThue();
                    tongTien += thanhTien;
                    
                    Object[] row = {
                        stt++,
                        tenXe,
                        "Tự lái",
                        thoiGianThue,
                        formatCurrency(ct.getGiaThueNgay()),
                        ct.getSoNgayThue(),
                        formatCurrency(thanhTien)
                    };
                    tableModel.addRow(row);
                }
                
                // Cập nhật tổng tiền
                totalValueLabel.setText(formatCurrency(tongTien));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu chi tiết thuê xe: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Thêm phương thức xuất PDF
    private void exportToPdf() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu hợp đồng dưới dạng PDF");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
        fileChooser.setSelectedFile(new File("HopDong_" + hopDong.getMaHD() + ".pdf"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String fileName = fileToSave.getAbsolutePath();
            if (!fileName.toLowerCase().endsWith(".pdf")) {
                fileName += ".pdf";
                fileToSave = new File(fileName);
            }

            try {
                createPdf(fileToSave);
                JOptionPane.showMessageDialog(this, 
                        "Xuất hợp đồng thành công!\nĐường dẫn: " + fileToSave.getAbsolutePath(), 
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                        "Lỗi khi xuất PDF: " + ex.getMessage(), 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private void createPdf(File file) throws DocumentException, IOException {
        // Định dạng để hiển thị ngày và số tiền
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        // Khởi tạo document
        Document document = new Document(com.itextpdf.text.PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        
        // Tạo font với Unicode để hỗ trợ tiếng Việt
        BaseFont baseFont = null;
        try {
            // Thử tìm font Arial
            baseFont = BaseFont.createFont("c:\\windows\\fonts\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (Exception e) {
            // Nếu không tìm thấy, sử dụng font mặc định
            baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        }
        
        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(baseFont, 16, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(baseFont, 12, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(baseFont, 12, com.itextpdf.text.Font.NORMAL);
        com.itextpdf.text.Font smallFont = new com.itextpdf.text.Font(baseFont, 10, com.itextpdf.text.Font.NORMAL);
        
        // Logo và tiêu đề công ty (tuỳ chọn)
        Paragraph title = new Paragraph("CÔNG TY TNHH DỊCH VỤ THUÊ XE", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        
        Paragraph address = new Paragraph("Địa chỉ: 97 Đường Man Thiện, Thủ Đức, TP. HCM", smallFont);
        address.setAlignment(Element.ALIGN_CENTER);
        document.add(address);
        
        Paragraph contact = new Paragraph("ĐT: (028) 38123456 - Email: contact@thuexe.com.vn", smallFont);
        contact.setAlignment(Element.ALIGN_CENTER);
        contact.setSpacingAfter(20);
        document.add(contact);
        
        // Tiêu đề hợp đồng
        Paragraph contractTitle = new Paragraph("HỢP ĐỒNG CHO THUÊ XE", headerFont);
        contractTitle.setAlignment(Element.ALIGN_CENTER);
        contractTitle.setSpacingAfter(10);
        document.add(contractTitle);
        
        // Số hợp đồng và ngày lập
        Paragraph contractInfo = new Paragraph(
            "Số hợp đồng: " + hopDong.getMaHD() + 
            "                                               Ngày lập: " + dateFormat.format(hopDong.getNgayLap()),
            normalFont
        );
        contractInfo.setSpacingAfter(20);
        document.add(contractInfo);
        
        // Thông tin khách hàng
        Paragraph customerTitle = new Paragraph("I. THÔNG TIN KHÁCH HÀNG:", headerFont);
        document.add(customerTitle);
        
        // Lấy thông tin từ đối tượng KhachHang nếu có
        String hoTen = hopDong.getTenKH();
        String maKH = hopDong.getMaKH() != null ? hopDong.getMaKH() : "N/A";
        String cccd = "N/A";
        String sdt = "N/A";
        String email = "N/A";
        String diaChi = "N/A";
        
        if (khachHang != null) {
            // Lấy thông tin từ đối tượng KhachHang
            if (khachHang.getHoTen() != null) hoTen = khachHang.getHoTen();
            if (khachHang.getCccd() != null) cccd = khachHang.getCccd();
            if (khachHang.getSdt() != null) sdt = khachHang.getSdt();
            if (khachHang.getEmail() != null) email = khachHang.getEmail();
            if (khachHang.getDiaChi() != null) diaChi = khachHang.getDiaChi();
        }
        
        Paragraph customerInfo = new Paragraph(
            "Họ tên khách hàng: " + hoTen + "\n" +
            "Mã khách hàng: " + maKH + "\n" +
            "CMND/CCCD: " + cccd + "\n" +
            "Số điện thoại: " + sdt + "\n" +
            "Email: " + email + "\n" +
            "Địa chỉ: " + diaChi,
            normalFont
        );
        customerInfo.setSpacingAfter(20);
        document.add(customerInfo);
        
        // Thông tin nhân viên
        Paragraph employeeTitle = new Paragraph("II. ĐẠI DIỆN CÔNG TY:", headerFont);
        document.add(employeeTitle);
        
        Paragraph employeeInfo = new Paragraph(
            "Họ tên nhân viên: " + hopDong.getTenNV() + "\n" +
            "Mã nhân viên: " + hopDong.getMaNV() + "\n" +
            "Chức vụ: Nhân viên phụ trách",
            normalFont
        );
        employeeInfo.setSpacingAfter(20);
        document.add(employeeInfo);
        
        // Thông tin xe thuê
        Paragraph carsTitle = new Paragraph("III. THÔNG TIN XE THUÊ:", headerFont);
        document.add(carsTitle);
        
        // Tạo bảng danh sách xe
        PdfPTable table = new PdfPTable(7); // 7 cột
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        
        // Đặt kích thước tương đối của các cột
        float[] columnWidths = {0.7f, 2.5f, 1.5f, 1.5f, 1.8f, 1f, 2f};
        table.setWidths(columnWidths);
        
        // Header của bảng
        PdfPCell cell;
        
        // Thêm header cho bảng
        String[] headers = {"STT", "Tên xe", "Biển số", "Hãng xe", "Thời gian thuê", "Số ngày", "Thành tiền"};
        for (String header : headers) {
            cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }
        
        // Thêm dữ liệu vào bảng
        int stt = 1;
        double totalAmount = 0;
        
        if (danhSachXeThue != null && !danhSachXeThue.isEmpty()) {
            for (ChiTietHD ct : danhSachXeThue) {
                // STT
                cell = new PdfPCell(new Phrase(String.valueOf(stt++), normalFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
                
                // Tên xe
                cell = new PdfPCell(new Phrase(ct.getTenXe(), normalFont));
                cell.setPadding(5);
                table.addCell(cell);
                
                // Biển số
                cell = new PdfPCell(new Phrase(ct.getBienSo() != null ? ct.getBienSo() : "N/A", normalFont));
                cell.setPadding(5);
                table.addCell(cell);
                
                // Hãng xe
                cell = new PdfPCell(new Phrase(ct.getHangXe() != null ? ct.getHangXe() : "N/A", normalFont));
                cell.setPadding(5);
                table.addCell(cell);
                
                // Thời gian thuê
                String timeRange = "";
                if (ct.getNgayBatDau() != null && ct.getNgayKetThuc() != null) {
                    timeRange = dateFormat.format(ct.getNgayBatDau()) + " - " + dateFormat.format(ct.getNgayKetThuc());
                }
                cell = new PdfPCell(new Phrase(timeRange, normalFont));
                cell.setPadding(5);
                table.addCell(cell);
                
                // Số ngày
                cell = new PdfPCell(new Phrase(String.valueOf(ct.getSoNgayThue()), normalFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
                
                // Thành tiền
                double amount = ct.getGiaThueNgay() * ct.getSoNgayThue();
                totalAmount += amount;
                String formattedAmount = currencyFormat.format(amount).replace(" ₫", " VNĐ");
                cell = new PdfPCell(new Phrase(formattedAmount, normalFont));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPadding(5);
                table.addCell(cell);
            }
        } else {
            cell = new PdfPCell(new Phrase("Không có dữ liệu xe thuê", normalFont));
            cell.setColspan(7);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(10);
            table.addCell(cell);
        }
        
        document.add(table);
        
        // Tổng tiền
        String formattedTotal = currencyFormat.format(totalAmount).replace(" ₫", " VNĐ");
        Paragraph totalAmountParagraph = new Paragraph("Tổng tiền: " + formattedTotal, headerFont);
        totalAmountParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalAmountParagraph);
        
        // Thêm điều khoản
        document.add(new com.itextpdf.text.Chunk("\n"));
        Paragraph termsTitle = new Paragraph("IV. ĐIỀU KHOẢN HỢP ĐỒNG:", headerFont);
        document.add(termsTitle);
        
        Paragraph terms = new Paragraph(
            "1. Khách hàng phải xuất trình đầy đủ giấy tờ hợp lệ: CMND/CCCD, GPLX, Hộ khẩu/KT3.\n" +
            "2. Khách hàng phải đặt cọc tiền hoặc tài sản có giá trị tương đương giá trị xe.\n" +
            "3. Khách hàng cam kết bảo quản xe trong thời gian thuê, chịu trách nhiệm bồi thường nếu làm hư hỏng xe.\n" +
            "4. Không sử dụng xe vào mục đích phi pháp, không được cho người khác mượn lại xe.\n" +
            "5. Nếu trả xe trễ hạn, khách hàng phải chịu phí phạt 150% giá thuê ngày cho mỗi ngày trễ hạn.\n" +
            "6. Công ty không chịu trách nhiệm về các chi phí phát sinh trong quá trình khách hàng sử dụng xe.\n",
            smallFont
        );
        document.add(terms);
        
        // Thông tin ngày tháng
        document.add(new com.itextpdf.text.Chunk("\n"));
        
        // Lấy ngày hiện tại
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("'Ngày' dd 'tháng' MM 'năm' yyyy");
        String currentDate = fullDateFormat.format(new Date());
        
        Paragraph dateSignature = new Paragraph("TP. Hồ Chí Minh, " + currentDate, normalFont);
        dateSignature.setAlignment(Element.ALIGN_RIGHT);
        document.add(dateSignature);
        
        // Chỗ ký tên
        document.add(new com.itextpdf.text.Chunk("\n"));
        
        PdfPTable signatureTable = new PdfPTable(2);
        signatureTable.setWidthPercentage(100);
        
        PdfPCell leftCell = new PdfPCell(new Phrase("ĐẠI DIỆN CÔNG TY\n\n\n\n\n" + hopDong.getTenNV(), normalFont));
        leftCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        leftCell.setBorder(Rectangle.NO_BORDER);
        leftCell.setPadding(10);
        signatureTable.addCell(leftCell);
        
        PdfPCell rightCell = new PdfPCell(new Phrase("KHÁCH HÀNG\n\n\n\n\n" + hoTen, normalFont));
        rightCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setPadding(10);
        signatureTable.addCell(rightCell);
        
        document.add(signatureTable);
        
        document.close();
    }
    
    // Helper methods
    private ImageIcon createAvatar(String name) {
        int size = 60;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(primaryColor);
        g2d.fillOval(0, 0, size, size);
        
        g2d.setColor(Color.WHITE);
        // Sử dụng font mặc định thay vì FlatRobotoFont
        g2d.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 28));
        
        String firstLetter = "";
        if (name != null && !name.isEmpty()) {
            firstLetter = name.substring(0, 1).toUpperCase();
        }
        
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(firstLetter);
        int textHeight = fm.getHeight();
        
        g2d.drawString(firstLetter, (size - textWidth) / 2, ((size - textHeight) / 2) + fm.getAscent());
        g2d.dispose();
        
        return new ImageIcon(img);
    }
    
    private void styleStatusButton(JButton button, String status) {
        button.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, java.awt.Font.BOLD, 12));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        Color bgColor;
        switch (status.toLowerCase()) {
            case "chờ xác nhận":
                bgColor = new Color(255, 193, 7); // Amber
                break;
            case "đã xác nhận":
                bgColor = new Color(76, 175, 80); // Green
                break;
            case "đang thuê":
                bgColor = new Color(33, 150, 243); // Blue
                break;
            case "đã hoàn thành":
                bgColor = new Color(46, 204, 113); // Green
                break;
            case "đã hủy":
                bgColor = new Color(239, 83, 80); // Red
                break;
            default:
                bgColor = new Color(158, 158, 158); // Grey
        }
        
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
    }
    
    private String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
    
    private String formatDateTime(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(date);
    }
    
    private String formatCurrency(double amount) {
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return currencyFormat.format(amount) + " đ";
    }
}