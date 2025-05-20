package ui.admin.QLHD;

import dao.ChiTietHDDao;
import model.ChiTietHD;
import model.HopDong;
import controller.HopDongController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    
    public XemChiTietHDDialog(Window owner, String maHD) {
        super(owner, "Xem chi tiết hợp đồng", ModalityType.APPLICATION_MODAL);
        
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
        // Thiết lập layout chính
        setLayout(new BorderLayout(10, 10));
        
        // ===== PANEL TRÁI - THÔNG TIN HỢP ĐỒNG VÀ KHÁCH HÀNG =====
        JPanel leftPanel = new JPanel(new BorderLayout(5, 10));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel chứa avatar và tên khách hàng
        JPanel customerPanel = new JPanel(new BorderLayout());
        JLabel avatarLabel = new JLabel(createDefaultAvatar());
        avatarLabel.setHorizontalAlignment(JLabel.CENTER);
        JLabel customerNameLabel = new JLabel(hopDong.getTenKH(), JLabel.CENTER);
        customerNameLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
        String phoneNumber = ""; // Thông tin SĐT có thể thêm sau
        JLabel customerPhoneLabel = new JLabel(phoneNumber, JLabel.CENTER);
        customerPhoneLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12));
        
        JPanel customerInfoPanel = new JPanel();
        customerInfoPanel.setLayout(new BoxLayout(customerInfoPanel, BoxLayout.Y_AXIS));
        customerInfoPanel.add(customerNameLabel);
        customerInfoPanel.add(customerPhoneLabel);
        
        customerPanel.add(avatarLabel, BorderLayout.CENTER);
        customerPanel.add(customerInfoPanel, BorderLayout.SOUTH);
        customerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Panel chứa thông tin chi tiết hợp đồng
        JPanel contractDetailsPanel = new JPanel();
        contractDetailsPanel.setLayout(new BoxLayout(contractDetailsPanel, BoxLayout.Y_AXIS));
        contractDetailsPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết hợp đồng"));
        
        // Thêm thông tin chi tiết với style
        addDetailItem(contractDetailsPanel, "Mã hợp đồng", hopDong.getMaHD(), true);
        addDetailItem(contractDetailsPanel, "Dịch vụ", "Thuê xe tự lái", false);
        addDetailItem(contractDetailsPanel, "Số xe thuê", 
                      String.valueOf(hopDong.getDanhSachXeThue() != null ? 
                                     hopDong.getDanhSachXeThue().size() : 0), false);
                                     
        // Ngày đi - lấy từ chi tiết đầu tiên nếu có
        if (hopDong.getDanhSachXeThue() != null && hopDong.getDanhSachXeThue().size() > 0) {
            ChiTietHD firstCT = hopDong.getDanhSachXeThue().get(0);
            addDetailItem(contractDetailsPanel, "Ngày đi", formatDate(firstCT.getNgayBatDau()), false);
        }
        
        // Các thông tin tài chính
        double tongTienXe = hopDong.getTongTien();
        addDetailItem(contractDetailsPanel, "Tiền xe", formatCurrency(tongTienXe), false);
        addDetailItem(contractDetailsPanel, "Doanh thu", formatCurrency(tongTienXe), false);
        addDetailItem(contractDetailsPanel, "Chi phí", "0 đ", false);
        addDetailItem(contractDetailsPanel, "Lợi nhuận", formatCurrency(tongTienXe), true);
        
        // Thông tin khác
        addDetailItem(contractDetailsPanel, "Nhân viên phụ trách", hopDong.getTenNV(), false);
        addDetailItem(contractDetailsPanel, "Ngày tạo hợp đồng", formatDateTime(hopDong.getNgayLap()), false);
        addDetailItem(contractDetailsPanel, "Địa chỉ giao", hopDong.getDiaChiGiao() != null ? hopDong.getDiaChiGiao() : "", false);
        
        // Thêm panel thông tin vào panel trái
        leftPanel.add(customerPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(contractDetailsPanel), BorderLayout.CENTER);
        
        // ===== PANEL PHẢI - BẢNG DANH SÁCH XE THUÊ =====
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
        // Panel menu tab
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tabPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        
        JLabel infoTabLabel = createTabLabel("Tổng quan", true);
       
        
        tabPanel.add(infoTabLabel);
      
        
     
        // Tạo bảng danh sách xe thuê
        String[] columnNames = {"STT", "XE", "THỜI GIAN THUÊ", "DOANH THU"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        rentTable = new JTable(tableModel);
        rentTable.setRowHeight(35);
        rentTable.getTableHeader().setReorderingAllowed(false);
        rentTable.getTableHeader().setResizingAllowed(true);
        rentTable.setShowGrid(true);
        rentTable.setGridColor(Color.LIGHT_GRAY);
        
        // Định dạng header bảng
        JTableHeader header = rentTable.getTableHeader();
        header.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
        header.setBackground(new Color(240, 240, 240));
        
        // Định dạng các ô trong bảng
        rentTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Căn phải cho các cột tiền tệ
                if (column == 4 || column == 5 || column == 6 || column == 7 || column == 8) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                return comp;
            }
        });
        
        // Panel tổng cộng
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel totalLabel = new JLabel("Tổng: ");
        totalValueLabel = new JLabel(formatCurrency(hopDong.getTongTien()));
        totalValueLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        totalPanel.add(totalLabel);
        totalPanel.add(totalValueLabel);
        
        // Thêm các panel vào panel phải
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(tabPanel, BorderLayout.NORTH);
      //  contentPanel.add(paymentInfoPanel, BorderLayout.CENTER);
        
        rightPanel.add(contentPanel, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(rentTable), BorderLayout.CENTER);
        rightPanel.add(totalPanel, BorderLayout.SOUTH);
        
        // Thêm 2 panel chính vào dialog
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.3);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Panel nút bấm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Thiết lập kích thước
        setSize(1000, 700);
        setLocationRelativeTo(getOwner());
    }
    
    private void loadData() {
        try {
            // Lấy danh sách chi tiết thuê xe từ database
            List<ChiTietHD> danhSachXeThue = chiTietHDDao.getChiTietHDByMaHD(hopDong.getMaHD());
            
            // Cập nhật bảng
            tableModel.setRowCount(0);
            
            if (danhSachXeThue.isEmpty()) {
                Object[] emptyRow = {"", "Không có dữ liệu", "", "", "", "", "", "", ""};
                tableModel.addRow(emptyRow);
            } else {
                int stt = 1;
                double tongTien = 0;
                
                for (ChiTietHD ct : danhSachXeThue) {
                    String tenXe = ct.getTenXe() + " - " + ct.getBienSo();
                    String thoiGianThue = formatDate(ct.getNgayBatDau()) + " - " + formatDate(ct.getNgayKetThuc());
                    double thanhTien = ct.getGiaThueNgay() * ct.getSoNgayThue();
                    tongTien += thanhTien;
                    
                    Object[] row = {
                        stt++,
                        tenXe,
                        "Tự lái",
                        thoiGianThue,
                        formatCurrency(thanhTien),
                        "0 đ",
                        "0 đ",
                        "0 đ",
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

    // Phương thức hỗ trợ tạo giao diện
    private ImageIcon createDefaultAvatar() {
        // Tạo hình tròn với chữ cái đầu của tên khách hàng
        BufferedImage img = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillOval(0, 0, 80, 80);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 36));
        
        String firstLetter = "";
        if (hopDong.getTenKH() != null && !hopDong.getTenKH().isEmpty()) {
            firstLetter = hopDong.getTenKH().substring(0, 1).toUpperCase();
        }
        
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(firstLetter);
        int textHeight = fm.getHeight();
        
        g2d.drawString(firstLetter, (80 - textWidth) / 2, ((80 - textHeight) / 2) + fm.getAscent());
        g2d.dispose();
        
        return new ImageIcon(img);
    }

    private JLabel createTabLabel(String text, boolean active) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FlatRobotoFont.FAMILY, active ? Font.BOLD : Font.PLAIN, 14));
        label.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        if (active) {
            label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 123, 255)));
            label.setForeground(new Color(0, 123, 255));
        }
        return label;
    }
    
    private void addDetailItem(JPanel panel, String label, String value, boolean highlight) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font(FlatRobotoFont.FAMILY, highlight ? Font.BOLD : Font.PLAIN, 14));
        if (highlight) {
            valueComponent.setForeground(new Color(0, 128, 0));
        }
        
        itemPanel.add(labelComponent, BorderLayout.WEST);
        itemPanel.add(valueComponent, BorderLayout.EAST);
        
        panel.add(itemPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
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
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(amount).replace("₫", "đ");
    }
}