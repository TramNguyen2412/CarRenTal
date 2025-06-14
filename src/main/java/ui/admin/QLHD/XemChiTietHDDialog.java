package ui.admin.QLHD;

import dao.ChiTietHDDao;
import model.ChiTietHD;
import model.HopDong;
import controller.HopDongController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class XemChiTietHDDialog extends JDialog {
    private HopDong hopDong;
    private ChiTietHDDao chiTietHDDao;
    private DefaultTableModel tableModel;
    private JTable rentTable;
    private JLabel totalValueLabel;
    private Color primaryColor = new Color(41, 128, 185);
    private Color accentColor = new Color(46, 204, 113);
    
    public XemChiTietHDDialog(Window owner, String maHD) {
        super(owner, "Hợp đồng thuê xe", ModalityType.APPLICATION_MODAL);
        
        // Lấy thông tin hợp đồng từ database
        HopDongController hopDongController = new HopDongController();
        this.hopDong = hopDongController.getHopDongByMa(maHD);
        
        if (this.hopDong == null) {
            JOptionPane.showMessageDialog(owner, 
                "Không tìm thấy thông tin hợp đồng với mã: " + maHD, 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
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
        nameLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel contractLabel = new JLabel("Mã hợp đồng: " + hopDong.getMaHD());
        contractLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
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
        dateLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
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
        overviewTab.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
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
        totalLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        
        totalValueLabel = new JLabel(formatCurrency(hopDong.getTongTien()));
        totalValueLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
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
        
        JButton closeButton = new JButton("Đóng");
        closeButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());
        
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
        table.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        table.setBorder(null);
        
        // Set header style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
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
            List<ChiTietHD> danhSachXeThue = chiTietHDDao.getChiTietHDByMaHD(hopDong.getMaHD());
            
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
    
    // Helper methods
    private ImageIcon createAvatar(String name) {
        int size = 60;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(primaryColor);
        g2d.fillOval(0, 0, size, size);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 28));
        
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
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
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
            case "vi phạm":
                bgColor = new Color(255, 87, 34); // Màu đỏ cam cho vi phạm
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