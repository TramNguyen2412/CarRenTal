//package ui.customer;
//
//import javax.swing.*;
//import javax.swing.border.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.util.*;
//import java.text.DecimalFormat;
//import javax.swing.event.DocumentEvent;
//import javax.swing.event.DocumentListener;
//import com.toedter.calendar.JDateChooser;
//import model.Xe;
//import model.KhachHang;
//import model.TaiKhoan;
//import controller.XeController;
//import controller.GioXeController;
//import com.formdev.flatlaf.FlatClientProperties;
//import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
//import util.ImageUtil;
//import java.util.List;
//import java.util.ArrayList;
//
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
//import com.toedter.calendar.JDateChooser;
//import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
//import com.formdev.flatlaf.FlatClientProperties;
//
//import java.util.Collections;
//import java.util.Comparator;
//
////
////public class XemDatXePanel extends JPanel {
////    private XeController xeController;
////    private GioXeController gioXeController;
////    
////    // Components
////    private JTextField searchField;
////    private JComboBox<String> hangXeComboBox;
////    private JComboBox<String> soChoComboBox;
////    private JComboBox<String> sapXepComboBox;
////    private JDateChooser ngayBatDauChooser;
////    private JDateChooser ngayKetThucChooser;
////    private JButton applyFilterButton;
////    private JPanel carListPanel;
////    private JScrollPane scrollPane;
////    private JLabel totalXeLabel;
////    private JLabel emptyLabel;
////    
////    // Data
////    private List<Xe> danhSachXe;
////    private List<Xe> danhSachXeHienThi;
////    
////    // User info
////    private TaiKhoan taiKhoan;
////    private KhachHang khachHang;
////    
////    // Màu sắc và bo tròn
////    private static final int CARD_RADIUS = 15; // Độ bo tròn của card
////    private static final int IMAGE_RADIUS = 10; // Độ bo tròn của ảnh
////    private static final Color BORDER_COLOR = new Color(230, 230, 230);
////    private static final Color CARD_BG_COLOR = Color.WHITE;
////    private static final Color IMAGE_BG_COLOR = new Color(245, 245, 245);
////    
////    // Kích thước cố định cho card
////    private static final int CARD_HEIGHT = 450;
////    
////    public XemDatXePanel() {
////        this(null, null);
////    }
////    
////    public XemDatXePanel(TaiKhoan taiKhoan, KhachHang khachHang) {
////        this.taiKhoan = taiKhoan;
////        this.khachHang = khachHang;
////        
////        this.xeController = new XeController();
////        this.gioXeController = new GioXeController();
////        
////        danhSachXe = new ArrayList<>();
////        danhSachXeHienThi = new ArrayList<>();
////        
////        setLayout(new BorderLayout(0, 0));
////        setBackground(Color.WHITE);
////        
////        initComponents();
////        loadData();
////    }
////    
////    private void initComponents() {
////        // NORTH: Title panel with search and filters
////        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
////        topPanel.setBackground(Color.WHITE);
////        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
////        
////        // Title area
////        JPanel titlePanel = new JPanel(new BorderLayout());
////        titlePanel.setOpaque(false);
////        
////        JLabel titleLabel = new JLabel("Xem & Đặt Thuê Xe");
////        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
////        titlePanel.add(titleLabel, BorderLayout.WEST);
////        
////        totalXeLabel = new JLabel("0 xe");
////        totalXeLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
////        titlePanel.add(totalXeLabel, BorderLayout.EAST);
////        
////        topPanel.add(titlePanel, BorderLayout.NORTH);
////        
////        // Search and filters
////        JPanel searchFilterPanel = createSearchFilterPanel();
////        topPanel.add(searchFilterPanel, BorderLayout.CENTER);
////        
////        add(topPanel, BorderLayout.NORTH);
////        
////        // CENTER: Cars display in a grid
////        carListPanel = new JPanel();
////        // Sử dụng GridLayout với số cột cố định (3 cột) nhưng số hàng không giới hạn (0)
////        carListPanel.setLayout(new GridLayout(0, 3, 20, 20)); // Khoảng cách 20px giữa các xe
////        carListPanel.setBackground(Color.WHITE);
////        carListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
////        
////        // Tạo JPanel để giữ carListPanel và đảm bảo chiều cao cố định
////        JPanel fixedHeightContainer = new JPanel(new BorderLayout());
////        fixedHeightContainer.setBackground(Color.WHITE);
////        fixedHeightContainer.add(carListPanel, BorderLayout.NORTH); // Đặt ở NORTH để không bị kéo dãn xuống dưới
////        
////        scrollPane = new JScrollPane(fixedHeightContainer);
////        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
////        scrollPane.setBorder(BorderFactory.createEmptyBorder());
////        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
////        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
////        scrollPane.getViewport().setBackground(Color.WHITE);
////        
////        // Empty results message
////        emptyLabel = new JLabel("Không tìm thấy xe nào phù hợp", SwingConstants.CENTER);
////        emptyLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 16));
////        emptyLabel.setForeground(new Color(120, 120, 120));
////        emptyLabel.setVisible(false);
////        
////        // Panel to hold both scroll pane and empty label
////        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
////        contentPanel.setBackground(Color.WHITE);
////        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
////        contentPanel.add(emptyLabel, BorderLayout.NORTH);
////        contentPanel.add(scrollPane, BorderLayout.CENTER);
////        
////        add(contentPanel, BorderLayout.CENTER);
////    }
////    
////    private JPanel createSearchFilterPanel() {
////        JPanel panel = new JPanel();
////        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
////        panel.setOpaque(false);
////        panel.setBorder(BorderFactory.createCompoundBorder(
////            new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
////            new EmptyBorder(10, 0, 15, 0)
////        ));
////        
////        // Search bar with label
////        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
////        searchPanel.setOpaque(false);
////        
////        // Thêm label tìm kiếm
////        JLabel searchLabel = new JLabel("Tìm kiếm:");
////        searchLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
////        searchPanel.add(searchLabel, BorderLayout.WEST);
////        
////        searchField = new JTextField();
////        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên xe, biển số hoặc hãng xe...");
////        searchField.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
////        searchField.getDocument().addDocumentListener(new DocumentListener() {
////            @Override
////            public void insertUpdate(DocumentEvent e) { filterXe(); }
////            @Override
////            public void removeUpdate(DocumentEvent e) { filterXe(); }
////            @Override
////            public void changedUpdate(DocumentEvent e) { filterXe(); }
////        });
////        
////        searchPanel.add(searchField, BorderLayout.CENTER);
////        
////        applyFilterButton = new JButton("Áp dụng bộ lọc");
////        applyFilterButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
////        applyFilterButton.addActionListener(e -> filterXe());
////        searchPanel.add(applyFilterButton, BorderLayout.EAST);
////        
////        panel.add(searchPanel);
////        panel.add(Box.createVerticalStrut(15));
////        
////        // Filter controls
////        JPanel filterPanel = new JPanel();
////        filterPanel.setLayout(new GridLayout(1, 5, 15, 0));
////        filterPanel.setOpaque(false);
////        
////        // Hãng xe filter
////        JPanel hangXePanel = new JPanel(new BorderLayout());
////        hangXePanel.setOpaque(false);
////        
////        JLabel hangXeLabel = new JLabel("Hãng xe");
////        hangXeLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
////        
////        hangXeComboBox = new JComboBox<>(new String[] {"Tất cả", "Toyota", "Honda", "Mazda", "BMW", "Mercedes", "Hyundai", "Kia"});
////        hangXeComboBox.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
////        hangXeComboBox.addActionListener(e -> filterXe());
////        
////        hangXePanel.add(hangXeLabel, BorderLayout.NORTH);
////        hangXePanel.add(hangXeComboBox, BorderLayout.CENTER);
////        
////        // Số chỗ filter
////        JPanel soChoPanel = new JPanel(new BorderLayout());
////        soChoPanel.setOpaque(false);
////        
////        JLabel soChoLabel = new JLabel("Số chỗ");
////        soChoLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
////        
////        soChoComboBox = new JComboBox<>(new String[] {"Tất cả", "4 chỗ", "5 chỗ", "7 chỗ", "9 chỗ", "16 chỗ"});
////        soChoComboBox.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
////        soChoComboBox.addActionListener(e -> filterXe());
////        
////        soChoPanel.add(soChoLabel, BorderLayout.NORTH);
////        soChoPanel.add(soChoComboBox, BorderLayout.CENTER);
////        
////        // Sort options
////        JPanel sapXepPanel = new JPanel(new BorderLayout());
////        sapXepPanel.setOpaque(false);
////        
////        JLabel sapXepLabel = new JLabel("Sắp xếp theo");
////        sapXepLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
////        
////        sapXepComboBox = new JComboBox<>(new String[] {
////            "Giá: Thấp đến Cao", 
////            "Giá: Cao đến Thấp", 
////            "Tên A-Z", 
////            "Tên Z-A", 
////            "Năm SX: Mới nhất", 
////            "Năm SX: Cũ nhất"
////        });
////        sapXepComboBox.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
////        sapXepComboBox.addActionListener(e -> filterXe());
////        
////        sapXepPanel.add(sapXepLabel, BorderLayout.NORTH);
////        sapXepPanel.add(sapXepComboBox, BorderLayout.CENTER);
////        
////        // Date selectors
////        JPanel ngayBDPanel = new JPanel(new BorderLayout());
////        ngayBDPanel.setOpaque(false);
////        
////        JLabel ngayBDLabel = new JLabel("Ngày bắt đầu");
////        ngayBDLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
////        
////        ngayBatDauChooser = new JDateChooser();
////        ngayBatDauChooser.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
////        ngayBatDauChooser.setDateFormatString("dd/MM/yyyy");
////        ngayBatDauChooser.setDate(new Date());
////        
////        ngayBDPanel.add(ngayBDLabel, BorderLayout.NORTH);
////        ngayBDPanel.add(ngayBatDauChooser, BorderLayout.CENTER);
////        
////        JPanel ngayKTPanel = new JPanel(new BorderLayout());
////        ngayKTPanel.setOpaque(false);
////        
////        JLabel ngayKTLabel = new JLabel("Ngày kết thúc");
////        ngayKTLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
////        
////        ngayKetThucChooser = new JDateChooser();
////        ngayKetThucChooser.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
////        ngayKetThucChooser.setDateFormatString("dd/MM/yyyy");
////        
////        Calendar cal = Calendar.getInstance();
////        cal.add(Calendar.DAY_OF_MONTH, 7);
////        ngayKetThucChooser.setDate(cal.getTime());
////        
////        ngayKTPanel.add(ngayKTLabel, BorderLayout.NORTH);
////        ngayKTPanel.add(ngayKetThucChooser, BorderLayout.CENTER);
////        
////        // Add all filter panels
////        filterPanel.add(hangXePanel);
////        filterPanel.add(soChoPanel);
////        filterPanel.add(sapXepPanel);
////        filterPanel.add(ngayBDPanel);
////        filterPanel.add(ngayKTPanel);
////        
////        panel.add(filterPanel);
////        
////        return panel;
////    }
////    
////    private void loadData() {
////        try {
////            danhSachXe = xeController.getAllXe();
////            System.out.println("Đã tải tổng cộng " + danhSachXe.size() + " xe từ CSDL");
////            
////            // Mặc định hiển thị tất cả xe
////            danhSachXeHienThi = new ArrayList<>(danhSachXe);
////            
////            // Áp dụng filter
////            filterXe();
////            
////        } catch (Exception e) {
////            JOptionPane.showMessageDialog(this,
////                "Lỗi khi tải danh sách xe: " + e.getMessage(),
////                "Lỗi",
////                JOptionPane.ERROR_MESSAGE);
////            e.printStackTrace();
////        }
////    }
////    
////    private void filterXe() {
////        String searchText = searchField.getText().toLowerCase().trim();
////        String hangXe = hangXeComboBox.getSelectedItem().toString();
////        String soCho = soChoComboBox.getSelectedItem().toString();
////        
////        System.out.println("Đang lọc xe với từ khóa: " + searchText);
////        System.out.println("Hãng xe: " + hangXe + ", Số chỗ: " + soCho);
////        
////        danhSachXeHienThi = new ArrayList<>();
////        
////        for (Xe xe : danhSachXe) {
////            // Tìm kiếm theo text
////            boolean matchSearch = searchText.isEmpty() ||
////                xe.getTenXe().toLowerCase().contains(searchText) ||
////                xe.getBienSo().toLowerCase().contains(searchText) ||
////                xe.getHangXe().toLowerCase().contains(searchText);
////            
////            // Lọc theo hãng xe
////            boolean matchHangXe = hangXe.equals("Tất cả") || 
////                xe.getHangXe().equals(hangXe);
////            
////            // Lọc theo số chỗ
////            boolean matchSoCho = soCho.equals("Tất cả");
////            if (!matchSoCho) {
////                int soChoValue = Integer.parseInt(soCho.split(" ")[0]);
////                matchSoCho = xe.getSoCho() == soChoValue;
////            }
////            
////            // Không lọc theo trạng thái để hiển thị tất cả xe
////            
////            if (matchSearch && matchHangXe && matchSoCho) {
////                danhSachXeHienThi.add(xe);
////            }
////        }
////        
////        // Sắp xếp
////        sapXepXe();
////        
////        // Hiển thị
////        hienThiXe();
////    }
////    
////    private void sapXepXe() {
////        String sortOption = sapXepComboBox.getSelectedItem().toString();
////        
////        switch (sortOption) {
////            case "Giá: Thấp đến Cao":
////                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getGiaThueNgay));
////                break;
////            case "Giá: Cao đến Thấp":
////                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getGiaThueNgay).reversed());
////                break;
////            case "Tên A-Z":
////                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getTenXe));
////                break;
////            case "Tên Z-A":
////                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getTenXe).reversed());
////                break;
////            case "Năm SX: Mới nhất":
////                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getNamSX).reversed());
////                break;
////            case "Năm SX: Cũ nhất":
////                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getNamSX));
////                break;
////        }
////    }
////    
////    private void hienThiXe() {
////        carListPanel.removeAll();
////        
////        if (danhSachXeHienThi.isEmpty()) {
////            emptyLabel.setVisible(true);
////            totalXeLabel.setText("0 xe");
////        } else {
////            emptyLabel.setVisible(false);
////            totalXeLabel.setText(danhSachXeHienThi.size() + " xe");
////            
////            // Thiết lập số cột thích hợp dựa vào số xe
////            int columns = 3; // Luôn có 3 cột để hiển thị
////            int rows = (int) Math.ceil((double) danhSachXeHienThi.size() / columns);
////            carListPanel.setLayout(new GridLayout(rows, columns, 20, 20));
////            
////            // Thêm các card với kích thước cố định
////            for (Xe xe : danhSachXeHienThi) {
////                JPanel cardWrapper = new JPanel(new BorderLayout());
////                cardWrapper.setOpaque(false);
////                // Đặt kích thước cố định cho wrapper
////                cardWrapper.setPreferredSize(new Dimension(0, CARD_HEIGHT));
////                cardWrapper.setMinimumSize(new Dimension(0, CARD_HEIGHT));
////                cardWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, CARD_HEIGHT));
////                
////                // Thêm card vào wrapper
////                RoundedPanel cardPanel = createCarCard(xe);
////                cardWrapper.add(cardPanel, BorderLayout.CENTER);
////                
////                carListPanel.add(cardWrapper);
////            }
////            
////            // Thêm các ô trống để điền hết lưới
////            int remainingCells = rows * columns - danhSachXeHienThi.size();
////            for (int i = 0; i < remainingCells; i++) {
////                carListPanel.add(createEmptyCard());
////            }
////        }
////        
////        // Buộc panel cập nhật lại layout
////        carListPanel.revalidate();
////        carListPanel.repaint();
////        
////        System.out.println("Hiển thị " + danhSachXeHienThi.size() + " xe");
////    }
////    
////    // Tạo card trống để điền vào lưới
////    private JPanel createEmptyCard() {
////        JPanel emptyPanel = new JPanel();
////        emptyPanel.setOpaque(false);
////        // Đặt kích thước cố định cho empty card
////        emptyPanel.setPreferredSize(new Dimension(0, CARD_HEIGHT));
////        return emptyPanel;
////    }
////    
////    private RoundedPanel createCarCard(Xe xe) {
////        // Tạo card chính với bo tròn góc
////        RoundedPanel cardPanel = new RoundedPanel(CARD_RADIUS);
////        cardPanel.setLayout(new BorderLayout(0, 5)); // Giảm khoảng cách giữa các phần
////        cardPanel.setBackground(CARD_BG_COLOR);
////        
////        // Sử dụng border shadow thay vì line border
////        cardPanel.setBorder(BorderFactory.createCompoundBorder(
////            new ShadowBorder(5, 2, 0.2f), // Shadow border
////            BorderFactory.createEmptyBorder(12, 12, 12, 12)
////        ));
////        
////        // Phần hình ảnh - với bo tròn
////        RoundedPanel imagePanel = new RoundedPanel(IMAGE_RADIUS);
////        imagePanel.setLayout(new BorderLayout());
////        imagePanel.setBackground(IMAGE_BG_COLOR);
////        imagePanel.setPreferredSize(new Dimension(0, 180)); // Điều chỉnh chiều cao ảnh phù hợp
////        
////        JLabel imageLabel = new JLabel("", JLabel.CENTER);
////        
////        if (xe.getHinhAnh() != null && !xe.getHinhAnh().isEmpty()) {
////            try {
////                ImageUtil.displayImage(xe.getHinhAnh(), imageLabel);
////            } catch (Exception e) {
////                e.printStackTrace();
////                imageLabel.setText(xe.getTenXe());
////                imageLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
////            }
////        } else {
////            imageLabel.setText(xe.getTenXe());
////            imageLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
////        }
////        
////        imagePanel.add(imageLabel, BorderLayout.CENTER);
////        
////        // Panel thông tin xe - cải tiến bố cục
////        JPanel infoPanel = new JPanel(new BorderLayout(0, 8)); // Border layout để cải thiện bố cục
////        infoPanel.setOpaque(false);
////        
////        // Panel tên xe và giá
////        JPanel headerPanel = new JPanel(new BorderLayout());
////        headerPanel.setOpaque(false);
////        
////        JLabel nameLabel = new JLabel(xe.getTenXe());
////        nameLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
////        
////        DecimalFormat df = new DecimalFormat("#,###");
////        JLabel priceLabel = new JLabel(df.format(xe.getGiaThueNgay()) + " VND/ngày");
////        priceLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
////        priceLabel.setForeground(new Color(255, 165, 0));
////        
////        headerPanel.add(nameLabel, BorderLayout.NORTH);
////        headerPanel.add(priceLabel, BorderLayout.CENTER);
////        
////        // Panel chi tiết xe
////        JPanel detailsPanel = new JPanel();
////        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
////        detailsPanel.setOpaque(false);
////        
////        // Cải thiện bố cục thông tin
////        addDetailRow(detailsPanel, "Biển số:", xe.getBienSo());
////        addDetailRow(detailsPanel, "Hãng xe:", xe.getHangXe());
////        addDetailRow(detailsPanel, "Số chỗ:", xe.getSoCho() + " chỗ");
////        addDetailRow(detailsPanel, "Năm SX:", String.valueOf(xe.getNamSX()));
////        
////        // Panel nút
////        JPanel buttonPanel = new JPanel(new BorderLayout());
////        buttonPanel.setOpaque(false);
////        
////        JButton detailButton = new JButton("Xem Chi Tiết");
////        detailButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
////        detailButton.setBackground(new Color(255, 165, 0));
////        detailButton.setForeground(Color.WHITE);
////        detailButton.setFocusPainted(false);
////        // Bo tròn nút
////        detailButton.putClientProperty("JButton.buttonType", "roundRect");
////        detailButton.addActionListener(e -> openCarDetails(xe));
////        
////        buttonPanel.add(detailButton, BorderLayout.CENTER);
////        
////        // Thêm tất cả vào panel thông tin
////        infoPanel.add(headerPanel, BorderLayout.NORTH);
////        infoPanel.add(detailsPanel, BorderLayout.CENTER);
////        infoPanel.add(buttonPanel, BorderLayout.SOUTH);
////        
////        // Thêm các thành phần vào card chính
////        cardPanel.add(imagePanel, BorderLayout.NORTH);
////        cardPanel.add(infoPanel, BorderLayout.CENTER);
////        
////        return cardPanel;
////    }
////    
////    private void addDetailRow(JPanel container, String label, String value) {
////        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 3)); // Giảm khoảng cách dọc
////        rowPanel.setOpaque(false);
////        
////        JLabel labelComponent = new JLabel(label);
////        labelComponent.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
////        
////        JLabel valueComponent = new JLabel(value);
////        valueComponent.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
////        
////        rowPanel.add(labelComponent);
////        rowPanel.add(valueComponent);
////        container.add(rowPanel);
////    }
////    
////    private void openCarDetails(Xe xe) {
////        
////       // THÊM DEBUG LOG
////    System.out.println("DEBUG - Mở chi tiết xe: " + xe.getTenXe());
////    System.out.println("DEBUG - Tài khoản: " + (taiKhoan != null ? taiKhoan.getTenDangNhap() : "null"));
////    System.out.println("DEBUG - Khách hàng: " + (khachHang != null ? khachHang.getHoTen() : "null"));
////    
////        Date ngayBatDau = ngayBatDauChooser.getDate();
////        Date ngayKetThuc = ngayKetThucChooser.getDate();
////        
////        ChiTietXeDialog dialog = new ChiTietXeDialog(
////            (JFrame) SwingUtilities.getWindowAncestor(this),
////            xe,
////            taiKhoan,
////            khachHang,
////            ngayBatDau,
////            ngayKetThuc,
////            gioXeController
////        );
////        
////        dialog.setVisible(true);
////    }
////    
////    // Cập nhật thông tin tài khoản
////    public void updateAccount(TaiKhoan taiKhoan, KhachHang khachHang) {
////        this.taiKhoan = taiKhoan;
////        this.khachHang = khachHang;
////    }
////    
////    // Làm mới dữ liệu
////    public void refreshData() {
////        loadData();
////    }
////    
////    // Panel bo tròn góc
////    private static class RoundedPanel extends JPanel {
////        private int cornerRadius;
////        
////        public RoundedPanel(int cornerRadius) {
////            super();
////            this.cornerRadius = cornerRadius;
////            setOpaque(false);
////        }
////        
////        @Override
////        protected void paintComponent(Graphics g) {
////            Graphics2D g2 = (Graphics2D) g.create();
////            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
////            
////            g2.setColor(getBackground());
////            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
////            
////            g2.dispose();
////        }
////    }
////    
////    // Border tạo hiệu ứng đổ bóng
////    private static class ShadowBorder extends AbstractBorder {
////        private int shadowSize;
////        private int cornerRadius;
////        private float shadowOpacity;
////        
////        public ShadowBorder(int shadowSize, int cornerRadius, float shadowOpacity) {
////            this.shadowSize = shadowSize;
////            this.cornerRadius = cornerRadius;
////            this.shadowOpacity = shadowOpacity;
////        }
////        
////        @Override
////        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
////            Graphics2D g2 = (Graphics2D) g.create();
////            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
////            
////            Color shadowColor = new Color(0, 0, 0, (int)(shadowOpacity * 255));
////            g2.setColor(shadowColor);
////            
////            // Vẽ đổ bóng
////            for (int i = 0; i < shadowSize; i++) {
////                float alpha = (shadowOpacity / shadowSize) * (shadowSize - i);
////                Color color = new Color(0, 0, 0, (int)(alpha * 255));
////                g2.setColor(color);
////                g2.drawRoundRect(x + i, y + i, width - i*2, height - i*2, cornerRadius + i, cornerRadius + i);
////            }
////            
////            g2.dispose();
////        }
////        
////        @Override
////        public Insets getBorderInsets(Component c) {
////            return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
////        }
////        
////        @Override
////        public Insets getBorderInsets(Component c, Insets insets) {
////            insets.left = insets.top = insets.right = insets.bottom = shadowSize;
////            return insets;
////        }
////    }
////}
//
//
//
//
//import javax.swing.*;
//import javax.swing.border.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.util.*;
//import java.text.DecimalFormat;
//import javax.swing.event.DocumentEvent;
//import javax.swing.event.DocumentListener;
//import com.toedter.calendar.JDateChooser;
//import model.Xe;
//import model.KhachHang;
//import model.TaiKhoan;
//import controller.XeController;
//import controller.GioXeController;
//import controller.HopDongController;
//import com.formdev.flatlaf.FlatClientProperties;
//import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
//import util.ImageUtil;
//import java.util.List;
//import java.util.ArrayList;
//
//public class XemDatXePanel extends JPanel {
//    private XeController xeController;
//    private GioXeController gioXeController;
//    private HopDongController hopDongController;
//    
//    // Components
//    private JTextField searchField;
//    private JComboBox<String> hangXeComboBox;
//    private JComboBox<String> soChoComboBox;
//    private JComboBox<String> sapXepComboBox;
//    private JDateChooser ngayBatDauChooser;
//    private JDateChooser ngayKetThucChooser;
//    private JButton applyFilterButton;
//    private JPanel carListPanel;
//    private JScrollPane scrollPane;
//    private JLabel totalXeLabel;
//    private JLabel emptyLabel;
//    
//    // Data
//    private List<Xe> danhSachXe;
//    private List<Xe> danhSachXeHienThi;
//    
//    // User info
//    private TaiKhoan taiKhoan;
//    private KhachHang khachHang;
//    
//    // Màu sắc và bo tròn
//    private static final int CARD_RADIUS = 15; // Độ bo tròn của card
//    private static final int IMAGE_RADIUS = 10; // Độ bo tròn của ảnh
//    private static final Color BORDER_COLOR = new Color(230, 230, 230);
//    private static final Color CARD_BG_COLOR = Color.WHITE;
//    private static final Color IMAGE_BG_COLOR = new Color(245, 245, 245);
//    
//    // Kích thước cố định cho card
//    private static final int CARD_HEIGHT = 450;
//    
//    // Cờ hiển thị thông báo
//    private boolean isInitialMessageShown = true;
//    
//    public XemDatXePanel() {
//        this(null, null);
//    }
//    
//    public XemDatXePanel(TaiKhoan taiKhoan, KhachHang khachHang) {
//        this.taiKhoan = taiKhoan;
//        this.khachHang = khachHang;
//        
//        this.xeController = new XeController();
//        this.gioXeController = new GioXeController();
//        this.hopDongController = new HopDongController();
//        
//        danhSachXe = new ArrayList<>();
//        danhSachXeHienThi = new ArrayList<>();
//        
//        setLayout(new BorderLayout(0, 0));
//        setBackground(Color.WHITE);
//        
//        initComponents();
//        // Không load dữ liệu ngay lập tức
//        showInitialMessage();
//    }
//    
//    private void initComponents() {
//        // NORTH: Title panel with search and filters
//        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
//        topPanel.setBackground(Color.WHITE);
//        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
//        
//        // Title area
//        JPanel titlePanel = new JPanel(new BorderLayout());
//        titlePanel.setOpaque(false);
//        
//        JLabel titleLabel = new JLabel("Xem & Đặt Thuê Xe");
//        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
//        titlePanel.add(titleLabel, BorderLayout.WEST);
//        
//        totalXeLabel = new JLabel("0 xe");
//        totalXeLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
//        titlePanel.add(totalXeLabel, BorderLayout.EAST);
//        
//        topPanel.add(titlePanel, BorderLayout.NORTH);
//        
//        // Search and filters
//        JPanel searchFilterPanel = createSearchFilterPanel();
//        topPanel.add(searchFilterPanel, BorderLayout.CENTER);
//        
//        add(topPanel, BorderLayout.NORTH);
//        
//        // CENTER: Cars display in a grid
//        carListPanel = new JPanel();
//        // Sử dụng GridLayout với số cột cố định (3 cột) nhưng số hàng không giới hạn (0)
//        carListPanel.setLayout(new GridLayout(0, 3, 20, 20)); // Khoảng cách 20px giữa các xe
//        carListPanel.setBackground(Color.WHITE);
//        carListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        
//        // Tạo JPanel để giữ carListPanel và đảm bảo chiều cao cố định
//        JPanel fixedHeightContainer = new JPanel(new BorderLayout());
//        fixedHeightContainer.setBackground(Color.WHITE);
//        fixedHeightContainer.add(carListPanel, BorderLayout.NORTH); // Đặt ở NORTH để không bị kéo dãn xuống dưới
//        
//        scrollPane = new JScrollPane(fixedHeightContainer);
//        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
//        scrollPane.setBorder(BorderFactory.createEmptyBorder());
//        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.getViewport().setBackground(Color.WHITE);
//        
//        // Empty results message
//        emptyLabel = new JLabel("Không tìm thấy xe nào phù hợp", SwingConstants.CENTER);
//        emptyLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 16));
//        emptyLabel.setForeground(new Color(120, 120, 120));
//        emptyLabel.setVisible(false);
//        
//        // Panel to hold both scroll pane and empty label
//        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
//        contentPanel.setBackground(Color.WHITE);
//        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
//        contentPanel.add(emptyLabel, BorderLayout.NORTH);
//        contentPanel.add(scrollPane, BorderLayout.CENTER);
//        
//        add(contentPanel, BorderLayout.CENTER);
//    }
//    
//    private JPanel createSearchFilterPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setOpaque(false);
//        panel.setBorder(BorderFactory.createCompoundBorder(
//            new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
//            new EmptyBorder(10, 0, 15, 0)
//        ));
//        
//        // Date selectors - Đặt lên trên đầu tiên
//        JPanel datePanel = new JPanel(new GridLayout(1, 3, 15, 0));
//        datePanel.setOpaque(false);
//        
//        // Ngày bắt đầu
//        JPanel ngayBDPanel = new JPanel(new BorderLayout());
//        ngayBDPanel.setOpaque(false);
//        
//        JLabel ngayBDLabel = new JLabel("Ngày bắt đầu thuê");
//        ngayBDLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        
//        ngayBatDauChooser = new JDateChooser();
//        ngayBatDauChooser.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        ngayBatDauChooser.setDateFormatString("dd/MM/yyyy");
//        
//        // Thiết lập ngày mặc định là ngày mai
//        Calendar tomorrow = Calendar.getInstance();
//        tomorrow.add(Calendar.DAY_OF_MONTH, 1); // Thêm 1 ngày vào ngày hiện tại
//        ngayBatDauChooser.setDate(tomorrow.getTime());
//        ngayBatDauChooser.setMinSelectableDate(Calendar.getInstance().getTime());
//        
//        ngayBDPanel.add(ngayBDLabel, BorderLayout.NORTH);
//        ngayBDPanel.add(ngayBatDauChooser, BorderLayout.CENTER);
//        
//        // Ngày kết thúc
//        JPanel ngayKTPanel = new JPanel(new BorderLayout());
//        ngayKTPanel.setOpaque(false);
//        
//        JLabel ngayKTLabel = new JLabel("Ngày kết thúc thuê");
//        ngayKTLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        
//        ngayKetThucChooser = new JDateChooser();
//        ngayKetThucChooser.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        ngayKetThucChooser.setDateFormatString("dd/MM/yyyy");
//        
//        // Thiết lập ngày mặc định là 4 ngày sau
//        Calendar fourDaysLater = Calendar.getInstance();
//        fourDaysLater.add(Calendar.DAY_OF_MONTH, 4); // Thêm 4 ngày vào ngày hiện tại
//        ngayKetThucChooser.setDate(fourDaysLater.getTime());
//        ngayKetThucChooser.setMinSelectableDate(tomorrow.getTime()); // Không chọn ngày trước ngày bắt đầu
//        
//        ngayKTPanel.add(ngayKTLabel, BorderLayout.NORTH);
//        ngayKTPanel.add(ngayKetThucChooser, BorderLayout.CENTER);
//        
//        // Nút tìm xe
//        JPanel findCarPanel = new JPanel(new BorderLayout());
//        findCarPanel.setOpaque(false);
//        
//        JLabel spacerLabel = new JLabel(" ");
//        spacerLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        
//        JButton findCarButton = new JButton("Tìm Xe Có Sẵn");
//        findCarButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        findCarButton.setBackground(new Color(0, 102, 204));
//        findCarButton.setForeground(Color.WHITE);
//        findCarButton.setFocusPainted(false);
//        findCarButton.addActionListener(e -> searchAvailableCars());
//        
//        findCarPanel.add(spacerLabel, BorderLayout.NORTH);
//        findCarPanel.add(findCarButton, BorderLayout.CENTER);
//        
//        datePanel.add(ngayBDPanel);
//        datePanel.add(ngayKTPanel);
//        datePanel.add(findCarPanel);
//        
//        panel.add(datePanel);
//        panel.add(Box.createVerticalStrut(15));
//        
//        // Search bar with label
//        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
//        searchPanel.setOpaque(false);
//        
//        // Thêm label tìm kiếm
//        JLabel searchLabel = new JLabel("Tìm kiếm:");
//        searchLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        searchPanel.add(searchLabel, BorderLayout.WEST);
//        
//        searchField = new JTextField();
//        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên xe, biển số hoặc hãng xe...");
//        searchField.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        searchField.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) { if (!isInitialMessageShown) filterXe(); }
//            @Override
//            public void removeUpdate(DocumentEvent e) { if (!isInitialMessageShown) filterXe(); }
//            @Override
//            public void changedUpdate(DocumentEvent e) { if (!isInitialMessageShown) filterXe(); }
//        });
//        
//        searchPanel.add(searchField, BorderLayout.CENTER);
//        
//        applyFilterButton = new JButton("Áp dụng bộ lọc");
//        applyFilterButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        applyFilterButton.addActionListener(e -> {
//            if (!isInitialMessageShown) filterXe();
//        });
//        applyFilterButton.setEnabled(false); // Vô hiệu hóa cho đến khi tìm xe
//        searchPanel.add(applyFilterButton, BorderLayout.EAST);
//        
//        panel.add(searchPanel);
//        panel.add(Box.createVerticalStrut(15));
//        
//        // Filter controls
//        JPanel filterPanel = new JPanel();
//        filterPanel.setLayout(new GridLayout(1, 3, 15, 0));
//        filterPanel.setOpaque(false);
//        
//        // Hãng xe filter
//        JPanel hangXePanel = new JPanel(new BorderLayout());
//        hangXePanel.setOpaque(false);
//        
//        JLabel hangXeLabel = new JLabel("Hãng xe");
//        hangXeLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        
//        hangXeComboBox = new JComboBox<>(new String[] {"Tất cả", "Toyota", "Honda", "Mazda", "BMW", "Mercedes-Benz", "Hyundai", "KIA", "Audi", "Roll-Royce", "Ford"});
//        hangXeComboBox.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        hangXeComboBox.addActionListener(e -> {
//            if (!isInitialMessageShown) filterXe();
//        });
//        hangXeComboBox.setEnabled(false); // Vô hiệu hóa cho đến khi tìm xe
//        
//        hangXePanel.add(hangXeLabel, BorderLayout.NORTH);
//        hangXePanel.add(hangXeComboBox, BorderLayout.CENTER);
//        
//        // Số chỗ filter
//        JPanel soChoPanel = new JPanel(new BorderLayout());
//        soChoPanel.setOpaque(false);
//        
//        JLabel soChoLabel = new JLabel("Số chỗ");
//        soChoLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        
//        soChoComboBox = new JComboBox<>(new String[] {"Tất cả", "4 chỗ", "5 chỗ", "7 chỗ", "9 chỗ", "16 chỗ"});
//        soChoComboBox.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        soChoComboBox.addActionListener(e -> {
//            if (!isInitialMessageShown) filterXe();
//        });
//        soChoComboBox.setEnabled(false); // Vô hiệu hóa cho đến khi tìm xe
//        
//        soChoPanel.add(soChoLabel, BorderLayout.NORTH);
//        soChoPanel.add(soChoComboBox, BorderLayout.CENTER);
//        
//        // Sort options
//        JPanel sapXepPanel = new JPanel(new BorderLayout());
//        sapXepPanel.setOpaque(false);
//        
//        JLabel sapXepLabel = new JLabel("Sắp xếp theo");
//        sapXepLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        
//        sapXepComboBox = new JComboBox<>(new String[] {
//            "Giá: Thấp đến Cao", 
//            "Giá: Cao đến Thấp", 
//            "Tên A-Z", 
//            "Tên Z-A", 
//            "Năm SX: Mới nhất", 
//            "Năm SX: Cũ nhất"
//        });
//        sapXepComboBox.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        sapXepComboBox.addActionListener(e -> {
//            if (!isInitialMessageShown) filterXe();
//        });
//        sapXepComboBox.setEnabled(false); // Vô hiệu hóa cho đến khi tìm xe
//        
//        sapXepPanel.add(sapXepLabel, BorderLayout.NORTH);
//        sapXepPanel.add(sapXepComboBox, BorderLayout.CENTER);
//        
//        // Add all filter panels
//        filterPanel.add(hangXePanel);
//        filterPanel.add(soChoPanel);
//        filterPanel.add(sapXepPanel);
//        
//        panel.add(filterPanel);
//        
//        return panel;
//    }
//    
//    private void showInitialMessage() {
//        isInitialMessageShown = true;
//        
//        // Xóa nội dung hiện tại
//        carListPanel.removeAll();
//        
//        // Tạo panel chứa thông báo
//        JPanel messagePanel = new JPanel();
//        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
//        messagePanel.setOpaque(false);
//        
//        // Icon lịch
//        JLabel iconLabel = new JLabel("\uD83D\uDCC5"); // Unicode calendar emoji
//        iconLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 72));
//        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        // Tiêu đề thông báo
//        JLabel titleLabel = new JLabel("Bạn muốn thuê xe khi nào?");
//        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
//        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        // Mô tả
//        JLabel descLabel = new JLabel("Vui lòng chọn ngày bắt đầu và kết thúc thuê xe, sau đó nhấn 'Tìm Xe Có Sẵn'");
//        descLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
//        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        // Hướng dẫn
//        JPanel instructionPanel = new JPanel();
//        instructionPanel.setLayout(new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
//        instructionPanel.setOpaque(false);
//        instructionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        instructionPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
//        
//        addInstruction(instructionPanel, "1", "Chọn ngày bắt đầu và kết thúc thuê xe");
//        addInstruction(instructionPanel, "2", "Nhấn 'Tìm Xe Có Sẵn' để xem danh sách xe");
//        addInstruction(instructionPanel, "3", "Lọc xe theo hãng xe, số chỗ hoặc sắp xếp theo ý muốn");
//        addInstruction(instructionPanel, "4", "Xem chi tiết và thêm xe vào giỏ hàng");
//        
//        // Thêm tất cả vào panel thông báo
//        messagePanel.add(Box.createVerticalGlue());
//        messagePanel.add(iconLabel);
//        messagePanel.add(Box.createVerticalStrut(20));
//        messagePanel.add(titleLabel);
//        messagePanel.add(Box.createVerticalStrut(10));
//        messagePanel.add(descLabel);
//        messagePanel.add(instructionPanel);
//        messagePanel.add(Box.createVerticalGlue());
//        
//        // Xóa nội dung hiện tại và thêm thông báo
//        carListPanel.setLayout(new BorderLayout());
//        carListPanel.add(messagePanel, BorderLayout.CENTER);
//        
//        // Ẩn empty label
//        emptyLabel.setVisible(false);
//        
//        // Vô hiệu hóa các controls lọc
//        applyFilterButton.setEnabled(false);
//        hangXeComboBox.setEnabled(false);
//        soChoComboBox.setEnabled(false);
//        sapXepComboBox.setEnabled(false);
//        
//        // Cập nhật UI
//        totalXeLabel.setText("0 xe");
//        carListPanel.revalidate();
//        carListPanel.repaint();
//    }
//    
//    private void addInstruction(JPanel container, String step, String text) {
//        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
//        row.setOpaque(false);
//        row.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        JLabel stepLabel = new JLabel(step);
//        stepLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
//        stepLabel.setForeground(new Color(0, 102, 204));
//        stepLabel.setPreferredSize(new Dimension(30, 30));
//        stepLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        stepLabel.setOpaque(true);
//        stepLabel.setBackground(new Color(230, 240, 255));
//        stepLabel.setBorder(new LineBorder(new Color(200, 220, 255), 1, true));
//        
//        JLabel textLabel = new JLabel(text);
//        textLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
//        
//        row.add(stepLabel);
//        row.add(textLabel);
//        container.add(row);
//    }
//    
//    private void searchAvailableCars() {
//        // Lấy ngày bắt đầu và kết thúc từ datepicker
//        Date ngayBatDau = ngayBatDauChooser.getDate();
//        Date ngayKetThuc = ngayKetThucChooser.getDate();
//        
//        // Kiểm tra ngày hợp lệ
//        if (ngayBatDau == null || ngayKetThuc == null) {
//            JOptionPane.showMessageDialog(this,
//                "Vui lòng chọn ngày bắt đầu và ngày kết thúc",
//                "Thông báo",
//                JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        
//        if (ngayBatDau.after(ngayKetThuc)) {
//            JOptionPane.showMessageDialog(this,
//                "Ngày kết thúc phải sau ngày bắt đầu",
//                "Thông báo",
//                JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        
//        try {
//            // Lấy danh sách xe khả dụng trong khoảng thời gian
//            danhSachXe = xeController.getXeKhaDungTrongThoiGian(ngayBatDau, ngayKetThuc);
//            System.out.println("Đã tải tổng cộng " + danhSachXe.size() + " xe khả dụng từ CSDL");
//            if (danhSachXe.isEmpty()) {
//                JOptionPane.showMessageDialog(this,
//                    "Không tìm thấy xe nào khả dụng trong khoảng thời gian đã chọn.\nVui lòng thử chọn khoảng thời gian khác.",
//                    "Không có xe khả dụng",
//                    JOptionPane.INFORMATION_MESSAGE);
//                return;
//            }
//            // Mặc định hiển thị tất cả xe
//            danhSachXeHienThi = new ArrayList<>(danhSachXe);
//            
//            // Bật chế độ hiển thị xe
//            isInitialMessageShown = false;
//            
//            // Kích hoạt các controls lọc
//            applyFilterButton.setEnabled(true);
//            hangXeComboBox.setEnabled(true);
//            soChoComboBox.setEnabled(true);
//            sapXepComboBox.setEnabled(true);
//            
//            // Áp dụng filter
//            filterXe();
//            
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this,
//                "Lỗi khi tải danh sách xe: " + e.getMessage(),
//                "Lỗi",
//                JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
//    }
//    
//    private void filterXe() {
//        if (isInitialMessageShown || danhSachXe.isEmpty()) return;
//        
//        String searchText = searchField.getText().toLowerCase().trim();
//        String hangXe = hangXeComboBox.getSelectedItem().toString();
//        String soCho = soChoComboBox.getSelectedItem().toString();
//        
//        System.out.println("Đang lọc xe với từ khóa: " + searchText);
//        System.out.println("Hãng xe: " + hangXe + ", Số chỗ: " + soCho);
//        
//        danhSachXeHienThi = new ArrayList<>();
//        
//        for (Xe xe : danhSachXe) {
//            // Tìm kiếm theo text
//            boolean matchSearch = searchText.isEmpty() ||
//                xe.getTenXe().toLowerCase().contains(searchText) ||
//                xe.getBienSo().toLowerCase().contains(searchText) ||
//                xe.getHangXe().toLowerCase().contains(searchText);
//            
//            // Lọc theo hãng xe
//            boolean matchHangXe = hangXe.equals("Tất cả") || 
//                xe.getHangXe().equals(hangXe);
//            
//            // Lọc theo số chỗ
//            boolean matchSoCho = soCho.equals("Tất cả");
//            if (!matchSoCho) {
//                int soChoValue = Integer.parseInt(soCho.split(" ")[0]);
//                matchSoCho = xe.getSoCho() == soChoValue;
//            }
//            
//            if (matchSearch && matchHangXe && matchSoCho) {
//                danhSachXeHienThi.add(xe);
//            }
//        }
//        
//        // Sắp xếp
//        sapXepXe();
//        
//        // Hiển thị
//        hienThiXe();
//    }
//    
//    private void sapXepXe() {
//        String sortOption = sapXepComboBox.getSelectedItem().toString();
//        
//        switch (sortOption) {
//            case "Giá: Thấp đến Cao":
//                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getGiaThueNgay));
//                break;
//            case "Giá: Cao đến Thấp":
//                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getGiaThueNgay).reversed());
//                break;
//            case "Tên A-Z":
//                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getTenXe));
//                break;
//            case "Tên Z-A":
//                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getTenXe).reversed());
//                break;
//            case "Năm SX: Mới nhất":
//                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getNamSX).reversed());
//                break;
//            case "Năm SX: Cũ nhất":
//                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getNamSX));
//                break;
//        }
//    }
//    
//    private void hienThiXe() {
//        carListPanel.removeAll();
//        
//        if (danhSachXeHienThi.isEmpty()) {
//            if (isInitialMessageShown) {
//                showInitialMessage();
//                return;
//            }
//            
//            emptyLabel.setVisible(true);
//            totalXeLabel.setText("0 xe");
//            
//            // Hiển thị thông báo không tìm thấy xe
//            JPanel messagePanel = new JPanel();
//            messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
//            messagePanel.setOpaque(false);
//            
//            JLabel iconLabel = new JLabel("\uD83D\uDE41"); // Unicode sad emoji
//            iconLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 72));
//            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//            
//            JLabel messageLabel = new JLabel("Không tìm thấy xe nào phù hợp");
//            messageLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
//            messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//            
//            JLabel hintLabel = new JLabel("Vui lòng thử thay đổi ngày thuê hoặc điều chỉnh bộ lọc");
//            hintLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
//            hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//            
//            messagePanel.add(Box.createVerticalGlue());
//            messagePanel.add(iconLabel);
//            messagePanel.add(Box.createVerticalStrut(20));
//            messagePanel.add(messageLabel);
//            messagePanel.add(Box.createVerticalStrut(10));
//            messagePanel.add(hintLabel);
//            messagePanel.add(Box.createVerticalGlue());
//            
//            carListPanel.setLayout(new BorderLayout());
//            carListPanel.add(messagePanel, BorderLayout.CENTER);
//        } else {
//            emptyLabel.setVisible(false);
//            totalXeLabel.setText(danhSachXeHienThi.size() + " xe");
//            
//            // Thiết lập số cột thích hợp
//            carListPanel.setLayout(new GridLayout(0, 3, 20, 20)); // 0 rows = as many as needed
//            
//            // Thêm các card xe
//            for (Xe xe : danhSachXeHienThi) {
//                JPanel cardWrapper = new JPanel(new BorderLayout());
//                cardWrapper.setOpaque(false);
//                cardWrapper.setPreferredSize(new Dimension(0, CARD_HEIGHT));
//                
//                RoundedPanel cardPanel = createCarCard(xe);
//                cardWrapper.add(cardPanel, BorderLayout.CENTER);
//                
//                carListPanel.add(cardWrapper);
//            }
//        }
//        
//        // Buộc panel cập nhật lại layout
//        carListPanel.revalidate();
//        carListPanel.repaint();
//        
//        System.out.println("Hiển thị " + danhSachXeHienThi.size() + " xe");
//    }
//    
//    private RoundedPanel createCarCard(Xe xe) {
//        // Tạo card chính với bo tròn góc
//        RoundedPanel cardPanel = new RoundedPanel(CARD_RADIUS);
//        cardPanel.setLayout(new BorderLayout(0, 5)); // Giảm khoảng cách giữa các phần
//        cardPanel.setBackground(CARD_BG_COLOR);
//        
//        // Sử dụng border shadow thay vì line border
//        cardPanel.setBorder(BorderFactory.createCompoundBorder(
//            new ShadowBorder(5, 2, 0.2f), // Shadow border
//            BorderFactory.createEmptyBorder(12, 12, 12, 12)
//        ));
//        
//        // Phần hình ảnh - với bo tròn
//        RoundedPanel imagePanel = new RoundedPanel(IMAGE_RADIUS);
//        imagePanel.setLayout(new BorderLayout());
//        imagePanel.setBackground(IMAGE_BG_COLOR);
//        imagePanel.setPreferredSize(new Dimension(0, 180)); // Điều chỉnh chiều cao ảnh phù hợp
//        
//        JLabel imageLabel = new JLabel("", JLabel.CENTER);
//        
//        if (xe.getHinhAnh() != null && !xe.getHinhAnh().isEmpty()) {
//            try {
//                ImageUtil.displayImage(xe.getHinhAnh(), imageLabel);
//            } catch (Exception e) {
//                e.printStackTrace();
//                imageLabel.setText(xe.getTenXe());
//                imageLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
//            }
//        } else {
//            imageLabel.setText(xe.getTenXe());
//            imageLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
//        }
//        
//        imagePanel.add(imageLabel, BorderLayout.CENTER);
//        
//        // Panel thông tin xe - cải tiến bố cục
//        JPanel infoPanel = new JPanel(new BorderLayout(0, 8)); // Border layout để cải thiện bố cục
//        infoPanel.setOpaque(false);
//        
//        // Panel tên xe và giá
//        JPanel headerPanel = new JPanel(new BorderLayout());
//        headerPanel.setOpaque(false);
//        
//        JLabel nameLabel = new JLabel(xe.getTenXe());
//        nameLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
//        
//        DecimalFormat df = new DecimalFormat("#,###");
//        JLabel priceLabel = new JLabel(df.format(xe.getGiaThueNgay()) + " VND/ngày");
//        priceLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
//        priceLabel.setForeground(new Color(255, 165, 0));
//        
//        headerPanel.add(nameLabel, BorderLayout.NORTH);
//        headerPanel.add(priceLabel, BorderLayout.CENTER);
//        
//        // Panel chi tiết xe
//        JPanel detailsPanel = new JPanel();
//        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
//        detailsPanel.setOpaque(false);
//        
//        // Cải thiện bố cục thông tin
//        addDetailRow(detailsPanel, "Biển số:", xe.getBienSo());
//        addDetailRow(detailsPanel, "Hãng xe:", xe.getHangXe());
//        addDetailRow(detailsPanel, "Số chỗ:", xe.getSoCho() + " chỗ");
//        addDetailRow(detailsPanel, "Năm SX:", String.valueOf(xe.getNamSX()));
//        
//        // Thêm thông tin thời gian thuê
//        JPanel rentalPanel = new JPanel();
//        rentalPanel.setLayout(new BoxLayout(rentalPanel, BoxLayout.Y_AXIS));
//        rentalPanel.setOpaque(false);
//        rentalPanel.setBorder(BorderFactory.createCompoundBorder(
//            new MatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
//            new EmptyBorder(8, 0, 8, 0)
//        ));
//        
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        String rentPeriod = sdf.format(ngayBatDauChooser.getDate()) + " đến " + sdf.format(ngayKetThucChooser.getDate());
//        
//        JLabel periodLabel = new JLabel("Thời gian thuê: " + rentPeriod);
//        periodLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 13));
//        periodLabel.setForeground(new Color(100, 100, 100));
//        periodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        
//        rentalPanel.add(periodLabel);
//        
//        // Panel nút
//        JPanel buttonPanel = new JPanel(new BorderLayout());
//        buttonPanel.setOpaque(false);
//        
//        JButton addToCartButton = new JButton("Thêm vào giỏ");
//        addToCartButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        addToCartButton.setBackground(new Color(0, 160, 0));
//        addToCartButton.setForeground(Color.WHITE);
//        addToCartButton.setFocusPainted(false);
//        // Bo tròn nút
//        addToCartButton.putClientProperty("JButton.buttonType", "roundRect");
//        addToCartButton.addActionListener(e -> addToCart(xe));
//        
//        buttonPanel.add(addToCartButton, BorderLayout.CENTER);
//        
//        // Thêm tất cả vào panel thông tin
//        infoPanel.add(headerPanel, BorderLayout.NORTH);
//        infoPanel.add(detailsPanel, BorderLayout.CENTER);
//        infoPanel.add(rentalPanel, BorderLayout.SOUTH);
//        
//        // Thêm các thành phần vào card chính
//        cardPanel.add(imagePanel, BorderLayout.NORTH);
//        cardPanel.add(infoPanel, BorderLayout.CENTER);
//        cardPanel.add(buttonPanel, BorderLayout.SOUTH);
//        
//        return cardPanel;
//    }
//    
//    private void addDetailRow(JPanel container, String label, String value) {
//        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 3)); // Giảm khoảng cách dọc
//        rowPanel.setOpaque(false);
//        
//        JLabel labelComponent = new JLabel(label);
//        labelComponent.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        
//        JLabel valueComponent = new JLabel(value);
//        valueComponent.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        
//        rowPanel.add(labelComponent);
//        rowPanel.add(valueComponent);
//        container.add(rowPanel);
//    }
//    
//    private void addToCart(Xe xe) {
//        if (taiKhoan == null || khachHang == null) {
//            JOptionPane.showMessageDialog(this,
//                "Vui lòng đăng nhập để đặt xe",
//                "Yêu cầu đăng nhập",
//                JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        Date ngayBatDau = ngayBatDauChooser.getDate();
//        Date ngayKetThuc = ngayKetThucChooser.getDate();
//
//        // Thêm xe vào giỏ hàng
//        boolean result = gioXeController.themXeVaoGio(xe.getMaXe(), khachHang.getMaKH(), ngayBatDau, ngayKetThuc);
//
//        if (result) {
//            JOptionPane.showMessageDialog(this,
//                "Đã thêm xe " + xe.getTenXe() + " vào giỏ hàng",
//                "Thành công",
//                JOptionPane.INFORMATION_MESSAGE);
//
//            // Lưu trữ tham chiếu đến panel hiện tại để sử dụng trong action listeners
//            final XemDatXePanel thisPanel = this;
//
//            // Tạo các nút tùy chỉnh với màu sắc
//            JButton cartButton = new JButton("Xem giỏ hàng");
//            cartButton.setBackground(new Color(0, 122, 255)); // Màu xanh
//            cartButton.setForeground(Color.WHITE);
//            cartButton.setFocusPainted(false);
//            cartButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//
//            JButton continueButton = new JButton("Tiếp tục thuê xe");
//            continueButton.setBackground(new Color(40, 167, 69)); // Màu xanh lá
//            continueButton.setForeground(Color.WHITE);
//            continueButton.setFocusPainted(false);
//            continueButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//
//            JButton closeButton = new JButton("Đóng");
//            closeButton.setBackground(new Color(108, 117, 125)); // Màu xám
//            closeButton.setForeground(Color.WHITE);
//            closeButton.setFocusPainted(false);
//            closeButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//
//            // Tạo panel chứa các nút
//            JPanel buttonPanel = new JPanel();
//            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
//            buttonPanel.add(cartButton);
//            buttonPanel.add(continueButton);
//            buttonPanel.add(closeButton);
//
//            // Tạo panel chính với message
//            JPanel messagePanel = new JPanel(new BorderLayout(0, 10));
//            JLabel messageLabel = new JLabel("Bạn muốn làm gì tiếp theo?");
//            messageLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
//            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
//            messagePanel.add(messageLabel, BorderLayout.NORTH);
//            messagePanel.add(buttonPanel, BorderLayout.CENTER);
//            messagePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
//
//            // Tạo dialog tùy chỉnh
//            final JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Lựa chọn");
//            dialog.setModal(true);
//            dialog.setContentPane(messagePanel);
//            dialog.pack();
//            dialog.setSize(450, 150);
//            dialog.setLocationRelativeTo(this);
//
//            // Thêm action listeners cho các nút
//            cartButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    // Đóng dialog trước
//                    dialog.dispose();
//
//                    // Tìm CustomerDashboard ở level cao hơn
//                    Component comp = thisPanel;
//                    CustomerDashboard dashboard = null;
//
//                    while (comp != null && dashboard == null) {
//                        comp = comp.getParent();
//                        if (comp instanceof CustomerDashboard) {
//                            dashboard = (CustomerDashboard) comp;
//                        }
//                    }
//
//                    if (dashboard != null) {
//                        // Đảm bảo chuyển trang được thực hiện sau khi dialog đã đóng
//                        final CustomerDashboard finalDashboard = dashboard;
//                        SwingUtilities.invokeLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                System.out.println("Chuyển đến trang giỏ hàng");
//                                finalDashboard.onMenuItemClicked("gioXe");
//                            }
//                        });
//                    } else {
//                        System.err.println("Không tìm thấy CustomerDashboard");
//                    }
//                }
//            });
//
//            continueButton.addActionListener(e -> {
//                dialog.dispose();
//                // Tiếp tục ở trang hiện tại
//            });
//
//            closeButton.addActionListener(e -> {
//                dialog.dispose();
//            });
//
//            // Hiển thị dialog
//            dialog.setVisible(true);
//        } else {
//            JOptionPane.showMessageDialog(this,
//                "Không thể thêm xe vào giỏ hàng: " + gioXeController.getErrorMessage(),
//                "Lỗi",
//                JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    
//    // Cập nhật thông tin tài khoản
//    public void updateAccount(TaiKhoan taiKhoan, KhachHang khachHang) {
//        System.out.println("XemDatXePanel - Cập nhật tài khoản: " + 
//                          (taiKhoan != null ? taiKhoan.getTenDangNhap() : "null"));
//        System.out.println("XemDatXePanel - Cập nhật khách hàng: " + 
//                          (khachHang != null ? khachHang.getMaKH() : "null"));
//                          
//        this.taiKhoan = taiKhoan;
//        this.khachHang = khachHang;
//    }
//    
//    // Làm mới dữ liệu
//    public void refreshData() {
//        if (!isInitialMessageShown) {
//            searchAvailableCars(); // Tải lại danh sách xe khả dụng
//        }
//    }
////    
//    // Panel bo tròn góc
//    private static class RoundedPanel extends JPanel {
//        private int cornerRadius;
//        
//        public RoundedPanel(int cornerRadius) {
//            super();
//            this.cornerRadius = cornerRadius;
//            setOpaque(false);
//        }
//        
//        @Override
//        protected void paintComponent(Graphics g) {
//            Graphics2D g2 = (Graphics2D) g.create();
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            
//            g2.setColor(getBackground());
//            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
//            
//            g2.dispose();
//        }
//    }
//    
//    // Border tạo hiệu ứng đổ bóng
//    private static class ShadowBorder extends AbstractBorder {
//        private int shadowSize;
//        private int cornerRadius;
//        private float shadowOpacity;
//        
//        public ShadowBorder(int shadowSize, int cornerRadius, float shadowOpacity) {
//            this.shadowSize = shadowSize;
//            this.cornerRadius = cornerRadius;
//            this.shadowOpacity = shadowOpacity;
//        }
//        
//        @Override
//        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
//            Graphics2D g2 = (Graphics2D) g.create();
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            
//            Color shadowColor = new Color(0, 0, 0, (int)(shadowOpacity * 255));
//            g2.setColor(shadowColor);
//            
//            // Vẽ đổ bóng
//            for (int i = 0; i < shadowSize; i++) {
//                float alpha = (shadowOpacity / shadowSize) * (shadowSize - i);
//                Color color = new Color(0, 0, 0, (int)(alpha * 255));
//                g2.setColor(color);
//                g2.drawRoundRect(x + i, y + i, width - i*2, height - i*2, cornerRadius + i, cornerRadius + i);
//            }
//            
//            g2.dispose();
//        }
//        
//        @Override
//        public Insets getBorderInsets(Component c) {
//            return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
//        }
//        
//        @Override
//        public Insets getBorderInsets(Component c, Insets insets) {
//            insets.left = insets.top = insets.right = insets.bottom = shadowSize;
//            return insets;
//        }
//    }
//}


package ui.customer;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.DecimalFormat;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.toedter.calendar.JDateChooser;
import model.Xe;
import model.KhachHang;
import model.TaiKhoan;
import controller.XeController;
import controller.GioXeController;
import controller.HopDongController;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import util.ImageUtil;
import java.util.List;
import java.util.ArrayList;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.Collections;
import java.util.Comparator;

public class XemDatXePanel extends JPanel {
    private XeController xeController;
    private GioXeController gioXeController;
    private HopDongController hopDongController;
    
    // Components
    private JTextField searchField;
    private JComboBox<String> hangXeComboBox;
    private JComboBox<String> soChoComboBox;
    private JComboBox<String> sapXepComboBox;
    private JDateChooser ngayBatDauChooser;
    private JDateChooser ngayKetThucChooser;
    private JButton applyFilterButton;
    private JPanel carListPanel;
    private JScrollPane scrollPane;
    private JLabel totalXeLabel;
    private JLabel emptyLabel;
    
    // Data
    private List<Xe> danhSachXe;
    private List<Xe> danhSachXeHienThi;
    
    // User info
    private TaiKhoan taiKhoan;
    private KhachHang khachHang;
    
    // Màu sắc cho giao diện
    private static final Color BORDER_COLOR = new Color(230, 230, 230);
    private static final Color CARD_BG_COLOR = Color.WHITE;
    private static final Color IMAGE_BG_COLOR = new Color(245, 245, 245);
    
    // Kích thước cố định cho card
    private static final int CARD_HEIGHT = 450;
    
    // Cờ hiển thị thông báo
    private boolean isInitialMessageShown = true;
    
    public XemDatXePanel() {
        this(null, null);
    }
    
    public XemDatXePanel(TaiKhoan taiKhoan, KhachHang khachHang) {
        this.taiKhoan = taiKhoan;
        this.khachHang = khachHang;
        
        this.xeController = new XeController();
        this.gioXeController = new GioXeController();
        this.hopDongController = new HopDongController();
        
        danhSachXe = new ArrayList<>();
        danhSachXeHienThi = new ArrayList<>();
        
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        
        initComponents();
        // Không load dữ liệu ngay lập tức
        showInitialMessage();
    }
    
    private void initComponents() {
        // NORTH: Title panel with search and filters
        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Title area
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Xem & Đặt Thuê Xe");
        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        totalXeLabel = new JLabel("0 xe");
        totalXeLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
        titlePanel.add(totalXeLabel, BorderLayout.EAST);
        
        topPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Search and filters
        JPanel searchFilterPanel = createSearchFilterPanel();
        topPanel.add(searchFilterPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        
        // CENTER: Cars display in a grid
        carListPanel = new JPanel();
        // Sử dụng GridLayout với số cột cố định (3 cột) nhưng số hàng không giới hạn (0)
        carListPanel.setLayout(new GridLayout(0, 3, 20, 20)); // Khoảng cách 20px giữa các xe
        carListPanel.setBackground(Color.WHITE);
        carListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tạo JPanel để giữ carListPanel và đảm bảo chiều cao cố định
        JPanel fixedHeightContainer = new JPanel(new BorderLayout());
        fixedHeightContainer.setBackground(Color.WHITE);
        fixedHeightContainer.add(carListPanel, BorderLayout.NORTH); // Đặt ở NORTH để không bị kéo dãn xuống dưới
        
        scrollPane = new JScrollPane(fixedHeightContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Empty results message
        emptyLabel = new JLabel("Không tìm thấy xe nào phù hợp", SwingConstants.CENTER);
        emptyLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 16));
        emptyLabel.setForeground(new Color(120, 120, 120));
        emptyLabel.setVisible(false);
        
        // Panel to hold both scroll pane and empty label
        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        contentPanel.add(emptyLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createSearchFilterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            new EmptyBorder(10, 0, 15, 0)
        ));
        
        // Date selectors - Đặt lên trên đầu tiên
        JPanel datePanel = new JPanel(new GridLayout(1, 3, 15, 0));
        datePanel.setOpaque(false);
        
        // Ngày bắt đầu
        JPanel ngayBDPanel = new JPanel(new BorderLayout());
        ngayBDPanel.setOpaque(false);
        
        JLabel ngayBDLabel = new JLabel("Ngày bắt đầu thuê");
        ngayBDLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        
        ngayBatDauChooser = new JDateChooser();
        ngayBatDauChooser.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        ngayBatDauChooser.setDateFormatString("dd/MM/yyyy");
        
        // Thiết lập ngày mặc định là ngày mai
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1); // Thêm 1 ngày vào ngày hiện tại
        ngayBatDauChooser.setDate(tomorrow.getTime());
        ngayBatDauChooser.setMinSelectableDate(Calendar.getInstance().getTime());
        
        ngayBDPanel.add(ngayBDLabel, BorderLayout.NORTH);
        ngayBDPanel.add(ngayBatDauChooser, BorderLayout.CENTER);
        
        // Ngày kết thúc
        JPanel ngayKTPanel = new JPanel(new BorderLayout());
        ngayKTPanel.setOpaque(false);
        
        JLabel ngayKTLabel = new JLabel("Ngày kết thúc thuê");
        ngayKTLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        
        ngayKetThucChooser = new JDateChooser();
        ngayKetThucChooser.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        ngayKetThucChooser.setDateFormatString("dd/MM/yyyy");
        
        // Thiết lập ngày mặc định là 4 ngày sau
        Calendar fourDaysLater = Calendar.getInstance();
        fourDaysLater.add(Calendar.DAY_OF_MONTH, 4); // Thêm 4 ngày vào ngày hiện tại
        ngayKetThucChooser.setDate(fourDaysLater.getTime());
        ngayKetThucChooser.setMinSelectableDate(tomorrow.getTime()); // Không chọn ngày trước ngày bắt đầu
        
        ngayKTPanel.add(ngayKTLabel, BorderLayout.NORTH);
        ngayKTPanel.add(ngayKetThucChooser, BorderLayout.CENTER);
        
        // Thêm listener để cập nhật ngày kết thúc khi ngày bắt đầu thay đổi
        ngayBatDauChooser.addPropertyChangeListener("date", evt -> {
            if (ngayBatDauChooser.getDate() != null) {
                // Lấy ngày bắt đầu
                Calendar startDate = Calendar.getInstance();
                startDate.setTime(ngayBatDauChooser.getDate());
                
                // Tạo ngày kết thúc là 3 ngày sau ngày bắt đầu
                Calendar endDate = (Calendar) startDate.clone();
                endDate.add(Calendar.DAY_OF_MONTH, 3);
                
                // Cập nhật ngày kết thúc
                ngayKetThucChooser.setDate(endDate.getTime());
                
                // Cập nhật ngày tối thiểu có thể chọn cho ngày kết thúc
                ngayKetThucChooser.setMinSelectableDate(startDate.getTime());
            }
        });
        
        // Nút tìm xe
        JPanel findCarPanel = new JPanel(new BorderLayout());
        findCarPanel.setOpaque(false);
        
        JLabel spacerLabel = new JLabel(" ");
        spacerLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        
        JButton findCarButton = new JButton("Tìm Xe Có Sẵn");
        findCarButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        findCarButton.setBackground(new Color(0, 102, 204));
        findCarButton.setForeground(Color.WHITE);
        findCarButton.setFocusPainted(false);
        findCarButton.addActionListener(e -> searchAvailableCars());
        
        findCarPanel.add(spacerLabel, BorderLayout.NORTH);
        findCarPanel.add(findCarButton, BorderLayout.CENTER);
        
        datePanel.add(ngayBDPanel);
        datePanel.add(ngayKTPanel);
        datePanel.add(findCarPanel);
        
        panel.add(datePanel);
        panel.add(Box.createVerticalStrut(15));
        
        // Search bar with label
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);
        
        // Thêm label tìm kiếm
        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        searchPanel.add(searchLabel, BorderLayout.WEST);
        
        searchField = new JTextField();
        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên xe, biển số hoặc hãng xe...");
        searchField.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { if (!isInitialMessageShown) filterXe(); }
            @Override
            public void removeUpdate(DocumentEvent e) { if (!isInitialMessageShown) filterXe(); }
            @Override
            public void changedUpdate(DocumentEvent e) { if (!isInitialMessageShown) filterXe(); }
        });
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        
        applyFilterButton = new JButton("Áp dụng bộ lọc");
        applyFilterButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        applyFilterButton.addActionListener(e -> {
            if (!isInitialMessageShown) filterXe();
        });
        applyFilterButton.setEnabled(false); // Vô hiệu hóa cho đến khi tìm xe
        searchPanel.add(applyFilterButton, BorderLayout.EAST);
        
        panel.add(searchPanel);
        panel.add(Box.createVerticalStrut(15));
        
        // Filter controls
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridLayout(1, 3, 15, 0));
        filterPanel.setOpaque(false);
        
        // Hãng xe filter
        JPanel hangXePanel = new JPanel(new BorderLayout());
        hangXePanel.setOpaque(false);
        
        JLabel hangXeLabel = new JLabel("Hãng xe");
        hangXeLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        
        hangXeComboBox = new JComboBox<>(new String[] {"Tất cả", "Toyota", "Honda", "Mazda", "BMW", "Mercedes-Benz", "Hyundai", "KIA", "Audi", "Roll-Royce", "Ford"});
        hangXeComboBox.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        hangXeComboBox.addActionListener(e -> {
            if (!isInitialMessageShown) filterXe();
        });
        hangXeComboBox.setEnabled(false); // Vô hiệu hóa cho đến khi tìm xe
        
        hangXePanel.add(hangXeLabel, BorderLayout.NORTH);
        hangXePanel.add(hangXeComboBox, BorderLayout.CENTER);
        
        // Số chỗ filter
        JPanel soChoPanel = new JPanel(new BorderLayout());
        soChoPanel.setOpaque(false);
        
        JLabel soChoLabel = new JLabel("Số chỗ");
        soChoLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        
        soChoComboBox = new JComboBox<>(new String[] {"Tất cả", "4 chỗ", "5 chỗ", "7 chỗ", "9 chỗ", "16 chỗ"});
        soChoComboBox.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        soChoComboBox.addActionListener(e -> {
            if (!isInitialMessageShown) filterXe();
        });
        soChoComboBox.setEnabled(false); // Vô hiệu hóa cho đến khi tìm xe
        
        soChoPanel.add(soChoLabel, BorderLayout.NORTH);
        soChoPanel.add(soChoComboBox, BorderLayout.CENTER);
        
        // Sort options
        JPanel sapXepPanel = new JPanel(new BorderLayout());
        sapXepPanel.setOpaque(false);
        
        JLabel sapXepLabel = new JLabel("Sắp xếp theo");
        sapXepLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        
        sapXepComboBox = new JComboBox<>(new String[] {
            "Giá: Thấp đến Cao", 
            "Giá: Cao đến Thấp", 
            "Tên A-Z", 
            "Tên Z-A", 
            "Năm SX: Mới nhất", 
            "Năm SX: Cũ nhất"
        });
        sapXepComboBox.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        sapXepComboBox.addActionListener(e -> {
            if (!isInitialMessageShown) filterXe();
        });
        sapXepComboBox.setEnabled(false); // Vô hiệu hóa cho đến khi tìm xe
        
        sapXepPanel.add(sapXepLabel, BorderLayout.NORTH);
        sapXepPanel.add(sapXepComboBox, BorderLayout.CENTER);
        
        // Add all filter panels
        filterPanel.add(hangXePanel);
        filterPanel.add(soChoPanel);
        filterPanel.add(sapXepPanel);
        
        panel.add(filterPanel);
        
        return panel;
    }
    
    private void showInitialMessage() {
        isInitialMessageShown = true;
        
        // Xóa nội dung hiện tại
        carListPanel.removeAll();
        
        // Tạo panel chứa thông báo
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setOpaque(false);
        
        // Icon lịch
        JLabel iconLabel = new JLabel("\uD83D\uDCC5"); // Unicode calendar emoji
        iconLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 72));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Tiêu đề thông báo
        JLabel titleLabel = new JLabel("Bạn muốn thuê xe khi nào?");
        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Mô tả
        JLabel descLabel = new JLabel("Vui lòng chọn ngày bắt đầu và kết thúc thuê xe, sau đó nhấn 'Tìm Xe Có Sẵn'");
        descLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Hướng dẫn
        JPanel instructionPanel = new JPanel();
        instructionPanel.setLayout(new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
        instructionPanel.setOpaque(false);
        instructionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        addInstruction(instructionPanel, "1", "Chọn ngày bắt đầu và kết thúc thuê xe");
        addInstruction(instructionPanel, "2", "Nhấn 'Tìm Xe Có Sẵn' để xem danh sách xe");
        addInstruction(instructionPanel, "3", "Lọc xe theo hãng xe, số chỗ hoặc sắp xếp theo ý muốn");
        addInstruction(instructionPanel, "4", "Xem chi tiết và thêm xe vào giỏ hàng");
        
        // Thêm tất cả vào panel thông báo
        messagePanel.add(Box.createVerticalGlue());
        messagePanel.add(iconLabel);
        messagePanel.add(Box.createVerticalStrut(20));
        messagePanel.add(titleLabel);
        messagePanel.add(Box.createVerticalStrut(10));
        messagePanel.add(descLabel);
        messagePanel.add(instructionPanel);
        messagePanel.add(Box.createVerticalGlue());
        
        // Xóa nội dung hiện tại và thêm thông báo
        carListPanel.setLayout(new BorderLayout());
        carListPanel.add(messagePanel, BorderLayout.CENTER);
        
        // Ẩn empty label
        emptyLabel.setVisible(false);
        
        // Vô hiệu hóa các controls lọc
        applyFilterButton.setEnabled(false);
        hangXeComboBox.setEnabled(false);
        soChoComboBox.setEnabled(false);
        sapXepComboBox.setEnabled(false);
        
        // Cập nhật UI
        totalXeLabel.setText("0 xe");
        carListPanel.revalidate();
        carListPanel.repaint();
    }
    
    private void addInstruction(JPanel container, String step, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel stepLabel = new JLabel(step);
        stepLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
        stepLabel.setForeground(new Color(0, 102, 204));
        stepLabel.setPreferredSize(new Dimension(30, 30));
        stepLabel.setHorizontalAlignment(SwingConstants.CENTER);
        stepLabel.setOpaque(true);
        stepLabel.setBackground(new Color(230, 240, 255));
        stepLabel.setBorder(new LineBorder(new Color(200, 220, 255), 1, true));
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
        
        row.add(stepLabel);
        row.add(textLabel);
        container.add(row);
    }
    
    private void searchAvailableCars() {
        // Lấy ngày bắt đầu và kết thúc từ datepicker
        Date ngayBatDau = ngayBatDauChooser.getDate();
        Date ngayKetThuc = ngayKetThucChooser.getDate();
        
        // Kiểm tra ngày hợp lệ
        if (ngayBatDau == null || ngayKetThuc == null) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn ngày bắt đầu và ngày kết thúc",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (ngayBatDau.after(ngayKetThuc)) {
            JOptionPane.showMessageDialog(this,
                "Ngày kết thúc phải sau ngày bắt đầu",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Lấy danh sách xe khả dụng trong khoảng thời gian
            danhSachXe = xeController.getXeKhaDungTrongThoiGian(ngayBatDau, ngayKetThuc);
            System.out.println("Đã tải tổng cộng " + danhSachXe.size() + " xe khả dụng từ CSDL");
            if (danhSachXe.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy xe nào khả dụng trong khoảng thời gian đã chọn.\nVui lòng thử chọn khoảng thời gian khác.",
                    "Không có xe khả dụng",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // Mặc định hiển thị tất cả xe
            danhSachXeHienThi = new ArrayList<>(danhSachXe);
            
            // Bật chế độ hiển thị xe
            isInitialMessageShown = false;
            
            // Kích hoạt các controls lọc
            applyFilterButton.setEnabled(true);
            hangXeComboBox.setEnabled(true);
            soChoComboBox.setEnabled(true);
            sapXepComboBox.setEnabled(true);
            
            // Áp dụng filter
            filterXe();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách xe: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void filterXe() {
        if (isInitialMessageShown || danhSachXe.isEmpty()) return;
        
        String searchText = searchField.getText().toLowerCase().trim();
        String hangXe = hangXeComboBox.getSelectedItem().toString();
        String soCho = soChoComboBox.getSelectedItem().toString();
        
        System.out.println("Đang lọc xe với từ khóa: " + searchText);
        System.out.println("Hãng xe: " + hangXe + ", Số chỗ: " + soCho);
        
        danhSachXeHienThi = new ArrayList<>();
        
        for (Xe xe : danhSachXe) {
            // Tìm kiếm theo text
            boolean matchSearch = searchText.isEmpty() ||
                xe.getTenXe().toLowerCase().contains(searchText) ||
                xe.getBienSo().toLowerCase().contains(searchText) ||
                xe.getHangXe().toLowerCase().contains(searchText);
            
            // Lọc theo hãng xe
            boolean matchHangXe = hangXe.equals("Tất cả") || 
                xe.getHangXe().equals(hangXe);
            
            // Lọc theo số chỗ
            boolean matchSoCho = soCho.equals("Tất cả");
            if (!matchSoCho) {
                int soChoValue = Integer.parseInt(soCho.split(" ")[0]);
                matchSoCho = xe.getSoCho() == soChoValue;
            }
            
            if (matchSearch && matchHangXe && matchSoCho) {
                danhSachXeHienThi.add(xe);
            }
        }
        
        // Sắp xếp
        sapXepXe();
        
        // Hiển thị
        hienThiXe();
    }
    
    private void sapXepXe() {
        String sortOption = sapXepComboBox.getSelectedItem().toString();
        
        switch (sortOption) {
            case "Giá: Thấp đến Cao":
                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getGiaThueNgay));
                break;
            case "Giá: Cao đến Thấp":
                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getGiaThueNgay).reversed());
                break;
            case "Tên A-Z":
                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getTenXe));
                break;
            case "Tên Z-A":
                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getTenXe).reversed());
                break;
            case "Năm SX: Mới nhất":
                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getNamSX).reversed());
                break;
            case "Năm SX: Cũ nhất":
                Collections.sort(danhSachXeHienThi, Comparator.comparing(Xe::getNamSX));
                break;
        }
    }
    
    private void hienThiXe() {
        carListPanel.removeAll();
        
        if (danhSachXeHienThi.isEmpty()) {
            if (isInitialMessageShown) {
                showInitialMessage();
                return;
            }
            
            emptyLabel.setVisible(true);
            totalXeLabel.setText("0 xe");
            
            // Hiển thị thông báo không tìm thấy xe
            JPanel messagePanel = new JPanel();
            messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
            messagePanel.setOpaque(false);
            
            JLabel iconLabel = new JLabel("\uD83D\uDE41"); // Unicode sad emoji
            iconLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 72));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel messageLabel = new JLabel("Không tìm thấy xe nào phù hợp");
            messageLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
            messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel hintLabel = new JLabel("Vui lòng thử thay đổi ngày thuê hoặc điều chỉnh bộ lọc");
            hintLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
            hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            messagePanel.add(Box.createVerticalGlue());
            messagePanel.add(iconLabel);
            messagePanel.add(Box.createVerticalStrut(20));
            messagePanel.add(messageLabel);
            messagePanel.add(Box.createVerticalStrut(10));
            messagePanel.add(hintLabel);
            messagePanel.add(Box.createVerticalGlue());
            
            carListPanel.setLayout(new BorderLayout());
            carListPanel.add(messagePanel, BorderLayout.CENTER);
        } else {
            emptyLabel.setVisible(false);
            totalXeLabel.setText(danhSachXeHienThi.size() + " xe");
            
            // Thiết lập số cột thích hợp
            carListPanel.setLayout(new GridLayout(0, 3, 20, 20)); // 0 rows = as many as needed
            
            // Thêm các card xe
            for (Xe xe : danhSachXeHienThi) {
                JPanel cardWrapper = new JPanel(new BorderLayout());
                cardWrapper.setOpaque(false);
                cardWrapper.setPreferredSize(new Dimension(0, CARD_HEIGHT));
                
                JPanel cardPanel = createCarCard(xe);
                cardWrapper.add(cardPanel, BorderLayout.CENTER);
                
                carListPanel.add(cardWrapper);
            }
        }
        
        // Buộc panel cập nhật lại layout
        carListPanel.revalidate();
        carListPanel.repaint();
        
        System.out.println("Hiển thị " + danhSachXeHienThi.size() + " xe");
    }
    
    private JPanel createCarCard(Xe xe) {
        // Tạo card chính
        JPanel cardPanel = new JPanel(new BorderLayout(0, 5)); // Giảm khoảng cách giữa các phần
        cardPanel.setBackground(CARD_BG_COLOR);
        
        // Sử dụng border đẹp thay vì shadow
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true), // Border với góc bo
            BorderFactory.createEmptyBorder(12, 12, 12, 12) // Padding bên trong
        ));
        
        // Phần hình ảnh
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(IMAGE_BG_COLOR);
        imagePanel.setBorder(new LineBorder(BORDER_COLOR, 1, true)); // Border với góc bo
        imagePanel.setPreferredSize(new Dimension(0, 180)); // Điều chỉnh chiều cao ảnh phù hợp
        
        JLabel imageLabel = new JLabel("", JLabel.CENTER);
        
        if (xe.getHinhAnh() != null && !xe.getHinhAnh().isEmpty()) {
            try {
                ImageUtil.displayImage(xe.getHinhAnh(), imageLabel);
            } catch (Exception e) {
                e.printStackTrace();
                imageLabel.setText(xe.getTenXe());
                imageLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
            }
        } else {
            imageLabel.setText(xe.getTenXe());
            imageLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
        }
        
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        // Panel thông tin xe - cải tiến bố cục
        JPanel infoPanel = new JPanel(new BorderLayout(0, 8)); // Border layout để cải thiện bố cục
        infoPanel.setOpaque(false);
        
        // Panel tên xe và giá
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(xe.getTenXe());
        nameLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
        
        DecimalFormat df = new DecimalFormat("#,###");
        JLabel priceLabel = new JLabel(df.format(xe.getGiaThueNgay()) + " VND/ngày");
        priceLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
       // priceLabel.setForeground(new Color(255, 165, 0));
        priceLabel.setForeground(new Color(239, 71, 35));
        headerPanel.add(nameLabel, BorderLayout.NORTH);
        headerPanel.add(priceLabel, BorderLayout.CENTER);
        
        // Panel chi tiết xe
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        
        // Cải thiện bố cục thông tin
        addDetailRow(detailsPanel, "Biển số:", xe.getBienSo());
        addDetailRow(detailsPanel, "Hãng xe:", xe.getHangXe());
        addDetailRow(detailsPanel, "Số chỗ:", xe.getSoCho() + " chỗ");
        addDetailRow(detailsPanel, "Năm SX:", String.valueOf(xe.getNamSX()));
        
        // Thêm thông tin thời gian thuê
        JPanel rentalPanel = new JPanel();
        rentalPanel.setLayout(new BoxLayout(rentalPanel, BoxLayout.Y_AXIS));
        rentalPanel.setOpaque(false);
        rentalPanel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
            new EmptyBorder(8, 0, 8, 0)
        ));
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String rentPeriod = sdf.format(ngayBatDauChooser.getDate()) + " đến " + sdf.format(ngayKetThucChooser.getDate());
        
        JLabel periodLabel = new JLabel("Thời gian thuê: " + rentPeriod);
        periodLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 13));
        periodLabel.setForeground(new Color(100, 100, 100));
        periodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        rentalPanel.add(periodLabel);
        
        // Panel nút
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);
        
        JButton addToCartButton = new JButton("Thêm vào giỏ");
        addToCartButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        addToCartButton.setBackground(new Color(0, 160, 0));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setFocusPainted(false);
        // Bo tròn nút (nếu có hỗ trợ)
        addToCartButton.putClientProperty("JButton.buttonType", "roundRect");
        addToCartButton.addActionListener(e -> addToCart(xe));
        
        buttonPanel.add(addToCartButton, BorderLayout.CENTER);
        
        // Thêm tất cả vào panel thông tin
        infoPanel.add(headerPanel, BorderLayout.NORTH);
        infoPanel.add(detailsPanel, BorderLayout.CENTER);
        infoPanel.add(rentalPanel, BorderLayout.SOUTH);
        
        // Thêm các thành phần vào card chính
        cardPanel.add(imagePanel, BorderLayout.NORTH);
        cardPanel.add(infoPanel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return cardPanel;
    }
    
    private void addDetailRow(JPanel container, String label, String value) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 3)); // Giảm khoảng cách dọc
        rowPanel.setOpaque(false);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        
        rowPanel.add(labelComponent);
        rowPanel.add(valueComponent);
        container.add(rowPanel);
    }
    
    private void addToCart(Xe xe) {
        if (taiKhoan == null || khachHang == null) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng đăng nhập để đặt xe",
                "Yêu cầu đăng nhập",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date ngayBatDau = ngayBatDauChooser.getDate();
        Date ngayKetThuc = ngayKetThucChooser.getDate();

        // Thêm xe vào giỏ hàng
        boolean result = gioXeController.themXeVaoGio(xe.getMaXe(), khachHang.getMaKH(), ngayBatDau, ngayKetThuc);

        if (result) {
            JOptionPane.showMessageDialog(this,
                "Đã thêm xe " + xe.getTenXe() + " vào giỏ hàng",
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);

            // Lưu trữ tham chiếu đến panel hiện tại để sử dụng trong action listeners
            final XemDatXePanel thisPanel = this;

            // Tạo các nút tùy chỉnh với màu sắc
            JButton cartButton = new JButton("Xem giỏ hàng");
            cartButton.setBackground(new Color(0, 122, 255)); // Màu xanh
            cartButton.setForeground(Color.WHITE);
            cartButton.setFocusPainted(false);
            cartButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));

            JButton continueButton = new JButton("Tiếp tục thuê xe");
            continueButton.setBackground(new Color(40, 167, 69)); // Màu xanh lá
            continueButton.setForeground(Color.WHITE);
            continueButton.setFocusPainted(false);
            continueButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));

            JButton closeButton = new JButton("Đóng");
            closeButton.setBackground(new Color(108, 117, 125)); // Màu xám
            closeButton.setForeground(Color.WHITE);
            closeButton.setFocusPainted(false);
            closeButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));

            // Tạo panel chứa các nút
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
            buttonPanel.add(cartButton);
            buttonPanel.add(continueButton);
            buttonPanel.add(closeButton);

            // Tạo panel chính với message
            JPanel messagePanel = new JPanel(new BorderLayout(0, 10));
            JLabel messageLabel = new JLabel("Bạn muốn làm gì tiếp theo?");
            messageLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            messagePanel.add(messageLabel, BorderLayout.NORTH);
            messagePanel.add(buttonPanel, BorderLayout.CENTER);
            messagePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            // Tạo dialog tùy chỉnh
            final JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Lựa chọn");
            dialog.setModal(true);
            dialog.setContentPane(messagePanel);
            dialog.pack();
            dialog.setSize(450, 150);
            dialog.setLocationRelativeTo(this);

            // Thêm action listeners cho các nút
            cartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Đóng dialog trước
                    dialog.dispose();
                    
                    // In ra log cho việc debug
                    System.out.println("Nút 'Xem giỏ hàng' được nhấn");

                    // Tìm CustomerDashboard ở level cao hơn
                    Component comp = thisPanel;
                    CustomerDashboard dashboard = null;

                    while (comp != null && dashboard == null) {
                        comp = comp.getParent();
                        if (comp instanceof CustomerDashboard) {
                            dashboard = (CustomerDashboard) comp;
                        }
                    }

                    if (dashboard != null) {
                        // Đảm bảo chuyển trang được thực hiện sau khi dialog đã đóng
                        final CustomerDashboard finalDashboard = dashboard;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("Chuyển đến trang giỏ hàng");
                                finalDashboard.onMenuItemClicked("gioXe");
                            }
                        });
                    } else {
                        System.err.println("Không tìm thấy CustomerDashboard");
                    }
                }
            });

            continueButton.addActionListener(e -> {
                dialog.dispose();
                // Tiếp tục ở trang hiện tại
            });

            closeButton.addActionListener(e -> {
                dialog.dispose();
            });

            // Hiển thị dialog
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "Không thể thêm xe vào giỏ hàng: " + gioXeController.getErrorMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Cập nhật thông tin tài khoản
    public void updateAccount(TaiKhoan taiKhoan, KhachHang khachHang) {
        System.out.println("XemDatXePanel - Cập nhật tài khoản: " + 
                          (taiKhoan != null ? taiKhoan.getTenDangNhap() : "null"));
        System.out.println("XemDatXePanel - Cập nhật khách hàng: " + 
                          (khachHang != null ? khachHang.getMaKH() : "null"));
                          
        this.taiKhoan = taiKhoan;
        this.khachHang = khachHang;
    }
    
    // Làm mới dữ liệu
    public void refreshData() {
        if (!isInitialMessageShown) {
            searchAvailableCars(); // Tải lại danh sách xe khả dụng
        }
    }
}