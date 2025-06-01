//
//package ui.customer;
//
//import controller.DanhGiaController;
//import model.KhachHang;
//import model.TaiKhoan;
//import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
//
//import javax.swing.*;
//import javax.swing.border.*;
//import javax.swing.table.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.List;
//import java.util.Map;
//import java.util.HashMap;
//import model.ChiTietHD;
//public class DanhGiaPanel extends JPanel {
//    private DanhGiaController danhGiaController;
//    private TaiKhoan taiKhoan;
//    private KhachHang khachHang;
//    
//    private JTabbedPane tabbedPane;
//    private JPanel unratedPanel;
//    private JPanel ratedPanel;
//    private JTable unratedTable;
//    private JTable ratedTable;
//    private DefaultTableModel unratedTableModel;
//    private DefaultTableModel ratedTableModel;
//    
//    private SimpleDateFormat dateFormat;
//    private DecimalFormat moneyFormat;
//    
//    public DanhGiaPanel() {
//        this(null, null);
//    }
//    
//    public DanhGiaPanel(TaiKhoan taiKhoan, KhachHang khachHang) {
//        this.taiKhoan = taiKhoan;
//        this.khachHang = khachHang;
//        this.danhGiaController = new DanhGiaController();
//        
//        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        moneyFormat = new DecimalFormat("#,###");
//        
//        setLayout(new BorderLayout());
//        setBorder(new EmptyBorder(20, 20, 20, 20));
//        setBackground(Color.WHITE);
//        
//        initComponents();
//        if (taiKhoan != null && khachHang != null) {
//            loadData();
//        }
//    }
//    
//    private void initComponents() {
//        // Panel tiêu đề
//        JPanel titlePanel = new JPanel(new BorderLayout());
//        titlePanel.setOpaque(false);
//        
//        JLabel titleLabel = new JLabel("Đánh Giá Hợp Đồng");
//        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
//        titlePanel.add(titleLabel, BorderLayout.WEST);
//        
//        add(titlePanel, BorderLayout.NORTH);
//        
//        // Tạo TabPane cho 2 tab: Chưa đánh giá và Đã đánh giá
//        tabbedPane = new JTabbedPane();
//        tabbedPane.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        
//        // Tab Chưa đánh giá
//        unratedPanel = createUnratedPanel();
//        tabbedPane.addTab("Chưa Đánh Giá", unratedPanel);
//        
//        // Tab Đã đánh giá
//        ratedPanel = createRatedPanel();
//        tabbedPane.addTab("Đã Đánh Giá", ratedPanel);
//        
//        add(tabbedPane, BorderLayout.CENTER);
//    }
//    
//    private JPanel createUnratedPanel() {
//        JPanel panel = new JPanel(new BorderLayout(0, 10));
//        panel.setOpaque(false);
//        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
//        
//        // Tạo model cho bảng
//        String[] columnNames = {
//            "Mã Hợp Đồng", "Xe Thuê", "Thời Gian Thuê", "Tổng Tiền", "Thao Tác"
//        };
//        unratedTableModel = new DefaultTableModel(columnNames, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return column == 4; // Chỉ cho phép chỉnh sửa cột thao tác
//            }
//        };
//        
//        // Tạo bảng
//        unratedTable = new JTable(unratedTableModel);
//        unratedTable.setRowHeight(40);
//        unratedTable.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        unratedTable.getTableHeader().setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        unratedTable.setShowGrid(true);
//        unratedTable.setGridColor(new Color(230, 230, 230));
//        
//        // Căn giữa các cột
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//        
//        // Căn phải cột tiền
//        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
//        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
//        
//        // Thiết lập renderer cho các cột
//        for (int i = 0; i < unratedTable.getColumnCount() - 1; i++) {
//            if (i == 3) { // Cột tổng tiền căn phải
//                unratedTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
//            } else {
//                unratedTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
//            }
//        }
//        
//        // Thiết lập renderer cho cột thao tác
//        unratedTable.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//                JButton button = new JButton("Đánh giá");
//                button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
//                button.setBackground(new Color(0, 120, 215));
//                button.setForeground(Color.WHITE);
//                
//                return button;
//            }
//        });
//        
//        // Thiết lập editor cho cột thao tác
//        unratedTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JTextField()) {
//            private int clickedRow;
//            
//            {
//                setClickCountToStart(1); // Chỉ cần click 1 lần
//            }
//            
//            @Override
//            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//                clickedRow = row;
//                
//                JButton button = new JButton("Đánh giá");
//                button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
//                button.setBackground(new Color(0, 120, 215));
//                button.setForeground(Color.WHITE);
//                button.addActionListener(e -> {
//                    SwingUtilities.invokeLater(() -> rateContract(clickedRow));
//                    fireEditingStopped();
//                });
//                
//                return button;
//            }
//            
//            @Override
//            public Object getCellEditorValue() {
//                return "";
//            }
//        });
//        
//        // Thiết lập kích thước cột
//        unratedTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Mã HD
//        unratedTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Xe thuê
//        unratedTable.getColumnModel().getColumn(2).setPreferredWidth(250); // Thời gian
//        unratedTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Tổng tiền
//        unratedTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Thao tác
//        
//        // Tạo panel con để chứa bảng
//        JScrollPane scrollPane = new JScrollPane(unratedTable);
//        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230)));
//        
//        panel.add(createInfoLabel("Các hợp đồng hoàn thành chưa được đánh giá"), BorderLayout.NORTH);
//        panel.add(scrollPane, BorderLayout.CENTER);
//        
//        return panel;
//    }
//    
//    private JPanel createRatedPanel() {
//        JPanel panel = new JPanel(new BorderLayout(0, 10));
//        panel.setOpaque(false);
//        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
//        
//        // Tạo model cho bảng
//        String[] columnNames = {
//            "Mã Hợp Đồng", "Xe Thuê", "Thời Gian Thuê", "Đánh Giá", "Ngày Đánh Giá", "Thao Tác"
//        };
//        ratedTableModel = new DefaultTableModel(columnNames, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return column == 5; // Chỉ cho phép chỉnh sửa cột thao tác
//            }
//        };
//        
//        // Tạo bảng
//        ratedTable = new JTable(ratedTableModel);
//        ratedTable.setRowHeight(40);
//        ratedTable.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        ratedTable.getTableHeader().setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        ratedTable.setShowGrid(true);
//        ratedTable.setGridColor(new Color(230, 230, 230));
//        
//        // Căn giữa các cột
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//        
//        // Thiết lập renderer cho các cột (trừ cột thao tác và đánh giá)
//        for (int i = 0; i < ratedTable.getColumnCount(); i++) {
//            if (i != 3 && i != 5) { // Bỏ qua cột đánh giá (3) và thao tác (5)
//                ratedTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
//            }
//        }
//        
//        // Renderer cho cột đánh giá (hiển thị sao)
//        ratedTable.getColumnModel().getColumn(3).setCellRenderer(new TableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//                JPanel panel = new JPanel();
//                panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
//                
//                if (isSelected) {
//                    panel.setBackground(table.getSelectionBackground());
//                } else {
//                    panel.setBackground(table.getBackground());
//                }
//                
//                if (value instanceof Integer) {
//                    int rating = (Integer) value;
//                    
//                    // Tạo panel chứa các ngôi sao
//                    JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
//                    starsPanel.setOpaque(false);
//                    
//                    for (int i = 1; i <= 5; i++) {
//                        JLabel starLabel = new JLabel();
//                        if (i <= rating) {
//                            starLabel.setText("★"); // Filled star
//                            starLabel.setForeground(new Color(255, 165, 0)); // Orange
//                        } else {
//                            starLabel.setText("☆"); // Empty star
//                            starLabel.setForeground(new Color(180, 180, 180)); // Light gray
//                        }
//                        starLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
//                        starsPanel.add(starLabel);
//                    }
//                    
//                    JLabel ratingLabel = new JLabel(" " + rating + "/5");
//                    ratingLabel.setForeground(new Color(100, 100, 100));
//                    ratingLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12));
//                    
//                    panel.add(starsPanel);
//                    panel.add(ratingLabel);
//                }
//                
//                return panel;
//            }
//        });
//        
//        // Thiết lập renderer cho cột thao tác
//        ratedTable.getColumnModel().getColumn(5).setCellRenderer(new TableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
//                
//                if (isSelected) {
//                    panel.setBackground(table.getSelectionBackground());
//                } else {
//                    panel.setBackground(table.getBackground());
//                }
//                
//                JButton editButton = new JButton("Sửa");
//                editButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
//                editButton.setPreferredSize(new Dimension(70, 30));
//                editButton.setBackground(new Color(52, 152, 219));
//                editButton.setForeground(Color.WHITE);
//                
//                JButton deleteButton = new JButton("Xóa");
//                deleteButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
//                deleteButton.setPreferredSize(new Dimension(70, 30));
//                deleteButton.setBackground(new Color(231, 76, 60));
//                deleteButton.setForeground(Color.WHITE);
//                
//                panel.add(editButton);
//                panel.add(deleteButton);
//                
//                return panel;
//            }
//        });
//
//        // Editor cho cột thao tác
//        ratedTable.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JTextField()) {
//            private int clickedRow;
//            
//            {
//                setClickCountToStart(1); // Chỉ cần click 1 lần
//            }
//            
//            @Override
//            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//                clickedRow = row;
//                
//                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
//                panel.setBackground(table.getSelectionBackground());
//                
//                JButton editButton = new JButton("Sửa");
//                editButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
//                editButton.setPreferredSize(new Dimension(70, 30));
//                editButton.setBackground(new Color(52, 152, 219));
//                editButton.setForeground(Color.WHITE);
//                editButton.addActionListener(e -> {
//                    SwingUtilities.invokeLater(() -> editRating(clickedRow));
//                    fireEditingStopped();
//                });
//                
//                JButton deleteButton = new JButton("Xóa");
//                deleteButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
//                deleteButton.setPreferredSize(new Dimension(70, 30));
//                deleteButton.setBackground(new Color(231, 76, 60));
//                deleteButton.setForeground(Color.WHITE);
//                deleteButton.addActionListener(e -> {
//                    SwingUtilities.invokeLater(() -> deleteRating(clickedRow));
//                    fireEditingStopped();
//                });
//                
//                panel.add(editButton);
//                panel.add(deleteButton);
//                
//                return panel;
//            }
//            
//            @Override
//            public Object getCellEditorValue() {
//                return "";
//            }
//        });
//        
//        // Thiết lập kích thước cột
//        ratedTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Mã HD
//        ratedTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Xe thuê
//        ratedTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Thời gian
//        ratedTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Đánh giá
//        ratedTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Ngày đánh giá
//        ratedTable.getColumnModel().getColumn(5).setPreferredWidth(180); // Thao tác
//        
//        // Tạo panel con để chứa bảng
//        JScrollPane scrollPane = new JScrollPane(ratedTable);
//        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230)));
//        
//        panel.add(createInfoLabel("Các hợp đồng đã được đánh giá"), BorderLayout.NORTH);
//        panel.add(scrollPane, BorderLayout.CENTER);
//        
//        return panel;
//    }
//    
//    private JLabel createInfoLabel(String text) {
//        JLabel label = new JLabel(text);
//        label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
//        label.setBorder(new EmptyBorder(0, 0, 10, 0));
//        return label;
//    }
//    
//    public void loadData() {
//        if (khachHang == null || khachHang.getMaKH() == null) {
//            JOptionPane.showMessageDialog(this,
//                "Không thể tải dữ liệu đánh giá. Vui lòng đăng nhập lại.",
//                "Lỗi tải dữ liệu",
//                JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        
//        // Xóa dữ liệu cũ
//        unratedTableModel.setRowCount(0);
//        ratedTableModel.setRowCount(0);
//        
//        // Tải danh sách hợp đồng chưa đánh giá
////        List<Map<String, Object>> unratedContracts = danhGiaController.getHopDongChuaDanhGia(khachHang.getMaKH());
////        for (Map<String, Object> contract : unratedContracts) {
////            String maHD = (String) contract.get("MaHD");
////            String tenXe = (String) contract.get("TenXe");
////            int soLuongXe = (int) contract.get("SoLuongXe");
////            
////            String xeInfo = tenXe;
////            if (soLuongXe > 1) {
////                xeInfo += " và " + (soLuongXe - 1) + " xe khác";
////            }
////            
////            String thoiGian = dateFormat.format(contract.get("NgayBatDau")) + " đến " + 
////                             dateFormat.format(contract.get("NgayKetThuc"));
////                             
////            String tongTien = moneyFormat.format(contract.get("TongTien")) + " VND";
////            
////            unratedTableModel.addRow(new Object[]{
////                maHD,
////                xeInfo,
////                thoiGian,
////                tongTien,
////                "Đánh giá"
////            });
////        }
////        
////        // Tải danh sách hợp đồng đã đánh giá
////        List<Map<String, Object>> ratedContracts = danhGiaController.getHopDongDaDanhGia(khachHang.getMaKH());
////        for (Map<String, Object> contract : ratedContracts) {
////            String maHD = (String) contract.get("MaHD");
////            String tenXe = (String) contract.get("TenXe");
////            int soLuongXe = (int) contract.get("SoLuongXe");
////            
////            String xeInfo = tenXe;
////            if (soLuongXe > 1) {
////                xeInfo += " và " + (soLuongXe - 1) + " xe khác";
////            }
////            
////            String thoiGian = dateFormat.format(contract.get("NgayBatDau")) + " đến " + 
////                             dateFormat.format(contract.get("NgayKetThuc"));
////                             
////            int diemSo = (int) contract.get("DiemSo");
////            String ngayDanhGia = dateFormat.format(contract.get("NgayDanhGia"));
////            
////            ratedTableModel.addRow(new Object[]{
////                maHD,
////                xeInfo,
////                thoiGian,
////                diemSo,
////                ngayDanhGia,
////                "Thao tác" // Giá trị này không quan trọng vì sẽ bị thay thế bởi custom renderer
////            });
////        }
//        List<Map<String, Object>> unratedContracts = danhGiaController.getHopDongChuaDanhGia(khachHang.getMaKH());
//        for (Map<String, Object> contract : unratedContracts) {
//            String maHD = (String) contract.get("MaHD");
//
//            // Lấy danh sách chi tiết xe thuê theo mã hợp đồng
//            List<ChiTietHD> chiTietList = danhGiaController.getChiTietHopDong(maHD);
//
//            // Xây dựng thông tin xe
//            StringBuilder xeInfoBuilder = new StringBuilder();
//            Map<String, Integer> xeCounts = new HashMap<>(); // Để đếm số lượng mỗi loại xe
//
//            // Đếm số lượng của mỗi loại xe dựa trên tên xe
//            for (ChiTietHD chiTiet : chiTietList) {
//                String tenXe = chiTiet.getTenXe();
//                xeCounts.put(tenXe, xeCounts.getOrDefault(tenXe, 0) + 1);
//            }
//
//            // Xây dựng chuỗi thông tin xe
//            int i = 0;
//            for (Map.Entry<String, Integer> entry : xeCounts.entrySet()) {
//                String tenXe = entry.getKey();
//                int soLuong = entry.getValue();
//
//                if (soLuong > 1) {
//                    xeInfoBuilder.append(tenXe).append(" (").append(soLuong).append(" xe)");
//                } else {
//                    xeInfoBuilder.append(tenXe);
//                }
//
//                // Nếu không phải mục cuối cùng, thêm dấu phẩy
//                if (i < xeCounts.size() - 1) {
//                    xeInfoBuilder.append(", ");
//                }
//                i++;
//            }
//
//            String xeInfo = xeInfoBuilder.toString();
//
//            String thoiGian = dateFormat.format(contract.get("NgayBatDau")) + " đến " + 
//                             dateFormat.format(contract.get("NgayKetThuc"));
//
//            String tongTien = moneyFormat.format(contract.get("TongTien")) + " VND";
//
//            unratedTableModel.addRow(new Object[]{
//                maHD,
//                xeInfo,
//                thoiGian,
//                tongTien,
//                "Đánh giá"
//            });
//        }
//        List<Map<String, Object>> ratedContracts = danhGiaController.getHopDongDaDanhGia(khachHang.getMaKH());
//        for (Map<String, Object> contract : ratedContracts) {
//            String maHD = (String) contract.get("MaHD");
//
//            // Lấy danh sách chi tiết xe thuê theo mã hợp đồng
//            List<ChiTietHD> chiTietList = danhGiaController.getChiTietHopDong(maHD);
//
//            // Xây dựng thông tin xe
//            StringBuilder xeInfoBuilder = new StringBuilder();
//            Map<String, Integer> xeCounts = new HashMap<>(); // Để đếm số lượng mỗi loại xe
//
//            // Đếm số lượng của mỗi loại xe dựa trên tên xe
//            for (ChiTietHD chiTiet : chiTietList) {
//                String tenXe = chiTiet.getTenXe();
//                xeCounts.put(tenXe, xeCounts.getOrDefault(tenXe, 0) + 1);
//            }
//
//            // Xây dựng chuỗi thông tin xe
//            int i = 0;
//            for (Map.Entry<String, Integer> entry : xeCounts.entrySet()) {
//                String tenXe = entry.getKey();
//                int soLuong = entry.getValue();
//
//                if (soLuong > 1) {
//                    xeInfoBuilder.append(tenXe).append(" (").append(soLuong).append(" xe)");
//                } else {
//                    xeInfoBuilder.append(tenXe);
//                }
//
//                // Nếu không phải mục cuối cùng, thêm dấu phẩy
//                if (i < xeCounts.size() - 1) {
//                    xeInfoBuilder.append(", ");
//                }
//                i++;
//            }
//
//            String xeInfo = xeInfoBuilder.toString();
//
//            String thoiGian = dateFormat.format(contract.get("NgayBatDau")) + " đến " + 
//                             dateFormat.format(contract.get("NgayKetThuc"));
//
//            int diemSo = (int) contract.get("DiemSo");
//            String ngayDanhGia = dateFormat.format(contract.get("NgayDanhGia"));
//
//            ratedTableModel.addRow(new Object[]{
//                maHD,
//                xeInfo,
//                thoiGian,
//                diemSo,
//                ngayDanhGia,
//                "Thao tác" // Giá trị này không quan trọng vì sẽ bị thay thế bởi custom renderer
//            });
//        }
//
//
//        // Thông báo nếu không có dữ liệu
//        if (unratedContracts.isEmpty()) {
//            JLabel emptyLabel = new JLabel("Không có hợp đồng nào cần đánh giá", JLabel.CENTER);
//            emptyLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 14));
//            emptyLabel.setForeground(Color.GRAY);
//            JPanel emptyPanel = new JPanel(new BorderLayout());
//            emptyPanel.add(emptyLabel, BorderLayout.CENTER);
//            emptyPanel.setBackground(Color.WHITE);
//            emptyPanel.setBorder(new EmptyBorder(50, 0, 0, 0));
//            
//            unratedPanel.add(emptyPanel, BorderLayout.CENTER);
//            unratedPanel.revalidate();
//        }
//        
//        if (ratedContracts.isEmpty()) {
//            JLabel emptyLabel = new JLabel("Bạn chưa đánh giá hợp đồng nào", JLabel.CENTER);
//            emptyLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 14));
//            emptyLabel.setForeground(Color.GRAY);
//            JPanel emptyPanel = new JPanel(new BorderLayout());
//            emptyPanel.add(emptyLabel, BorderLayout.CENTER);
//            emptyPanel.setBackground(Color.WHITE);
//            emptyPanel.setBorder(new EmptyBorder(50, 0, 0, 0));
//            
//            ratedPanel.add(emptyPanel, BorderLayout.CENTER);
//            ratedPanel.revalidate();
//        }
//    }
//    
//    private void rateContract(int row) {
//        if (row < 0 || row >= unratedTableModel.getRowCount()) {
//            return;
//        }
//        
//        String maHD = (String) unratedTableModel.getValueAt(row, 0);
//        Map<String, Object> hopDong = null;
//        
//        // Tìm hợp đồng tương ứng
//        List<Map<String, Object>> unratedContracts = danhGiaController.getHopDongChuaDanhGia(khachHang.getMaKH());
//        for (Map<String, Object> contract : unratedContracts) {
//            if (maHD.equals(contract.get("MaHD"))) {
//                hopDong = contract;
//                break;
//            }
//        }
//        
//        if (hopDong != null) {
//            DanhGiaDialog dialog = new DanhGiaDialog((JFrame) SwingUtilities.getWindowAncestor(this), 
//                                                     khachHang.getMaKH(), hopDong);
//            dialog.setVisible(true);
//            
//            // Refresh data after rating
//            loadData();
//        }
//    }
//    
//    private void editRating(int row) {
//        if (row < 0 || row >= ratedTableModel.getRowCount()) {
//            return;
//        }
//        
//        String maHD = (String) ratedTableModel.getValueAt(row, 0);
//        Map<String, Object> hopDong = null;
//        
//        // Tìm hợp đồng tương ứng
//        List<Map<String, Object>> ratedContracts = danhGiaController.getHopDongDaDanhGia(khachHang.getMaKH());
//        for (Map<String, Object> contract : ratedContracts) {
//            if (maHD.equals(contract.get("MaHD"))) {
//                hopDong = contract;
//                break;
//            }
//        }
//        
//        if (hopDong != null) {
//            SuaDanhGiaDialog dialog = new SuaDanhGiaDialog((JFrame) SwingUtilities.getWindowAncestor(this), hopDong);
//            dialog.setVisible(true);
//            
//            // Refresh data after editing
//            loadData();
//        }
//    }
//    
//    private void deleteRating(int row) {
//        if (row < 0 || row >= ratedTableModel.getRowCount()) {
//            return;
//        }
//        
//        String maHD = (String) ratedTableModel.getValueAt(row, 0);
//        Map<String, Object> hopDong = null;
//        
//        // Tìm hợp đồng tương ứng
//        List<Map<String, Object>> ratedContracts = danhGiaController.getHopDongDaDanhGia(khachHang.getMaKH());
//        for (Map<String, Object> contract : ratedContracts) {
//            if (maHD.equals(contract.get("MaHD"))) {
//                hopDong = contract;
//                break;
//            }
//        }
//        
//        if (hopDong != null) {
//            int option = JOptionPane.showConfirmDialog(this,
//                "Bạn có chắc chắn muốn xóa đánh giá này không?",
//                "Xác nhận xóa",
//                JOptionPane.YES_NO_OPTION,
//                JOptionPane.QUESTION_MESSAGE);
//            
//            if (option == JOptionPane.YES_OPTION) {
//                String maDG = (String) hopDong.get("MaDG");
//                boolean result = danhGiaController.xoaDanhGia(maDG);
//                
//                if (result) {
//                    JOptionPane.showMessageDialog(this,
//                        "Đã xóa đánh giá thành công!",
//                        "Thành công",
//                        JOptionPane.INFORMATION_MESSAGE);
//                    
//                    // Refresh data after deleting
//                    loadData();
//                } else {
//                    JOptionPane.showMessageDialog(this,
//                        "Không thể xóa đánh giá: " + danhGiaController.getErrorMessage(),
//                        "Lỗi",
//                        JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        }
//    }
//    
//    public void setAccount(TaiKhoan taiKhoan, KhachHang khachHang) {
//        this.taiKhoan = taiKhoan;
//        this.khachHang = khachHang;
//        
//        if (taiKhoan != null && khachHang != null) {
//            loadData();
//        }
//    }
//}



package ui.customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.DanhGiaController;
import model.DanhGia;
import model.KhachHang;
import model.TaiKhoan;
public class DanhGiaPanel extends JPanel {
    private JTable tableDanhGia;
    private DefaultTableModel tableModel;
    private JButton btnThemDanhGia, btnSuaDanhGia, btnXoaDanhGia;
    
    private DanhGiaController controller;
    private TaiKhoan taiKhoan;
    private KhachHang khachHang;
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    // Định nghĩa màu sắc
    private final Color DARK_BLUE = new Color(0, 51, 153); // Màu xanh đậm cho text và đường kẻ
    private final Color SELECTION_COLOR = new Color(0, 102, 204); // Màu xanh đậm cho selection
    
    public DanhGiaPanel() {
        controller = new DanhGiaController();
        initComponents();
    }
    
    public DanhGiaPanel(TaiKhoan taiKhoan, KhachHang khachHang) {
        this.taiKhoan = taiKhoan;
        this.khachHang = khachHang;
        controller = new DanhGiaController();
        initComponents();
        loadData();
    }
    
    public void setAccount(TaiKhoan taiKhoan, KhachHang khachHang) {
        this.taiKhoan = taiKhoan;
        this.khachHang = khachHang;
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // ---- PHẦN TIÊU ĐỀ ---- (Không có phần nền màu xanh lớn)
        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        pnlHeader.setBackground(Color.WHITE); // Nền trắng
        
        // Tiêu đề chính
        JLabel lblTitle = new JLabel("ĐÁNH GIÁ HỢP ĐỒNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(DARK_BLUE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlHeader.add(lblTitle);
        
        // Tiêu đề phụ
        JLabel lblSubTitle = new JLabel("Chia sẻ đánh giá của bạn về dịch vụ thuê xe");
        lblSubTitle.setFont(new Font("Arial", Font.ITALIC, 14));
        lblSubTitle.setForeground(new Color(100, 100, 100));
        lblSubTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
      //  pnlHeader.add(Box.createVerticalStrut(5));
        pnlHeader.add(lblSubTitle);
        
        // Đường kẻ phân cách
        JPanel pnlLine = new JPanel();
        pnlLine.setMaximumSize(new Dimension(1200, 1));
        pnlLine.setPreferredSize(new Dimension(1200, 1));
        pnlLine.setBackground(DARK_BLUE);
        pnlLine.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    //    pnlHeader.add(Box.createVerticalStrut(10));
        pnlHeader.add(pnlLine);
        
        add(pnlHeader, BorderLayout.NORTH);
        
        // ---- PHẦN NỘI DUNG CHÍNH ----
        JPanel pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBackground(Color.WHITE);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        // ---- BẢNG ĐÁNH GIÁ ----
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        
        // Tiêu đề panel
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                "Lịch sử đánh giá của bạn");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titledBorder.setTitleColor(DARK_BLUE);
        
        pnlTable.setBorder(BorderFactory.createCompoundBorder(
                titledBorder,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        // Table model và bảng
        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Mã đánh giá", "Hợp đồng", "Tên xe", "Điểm số", "Nhận xét", "Ngày đánh giá"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) {
                    return Integer.class; // Cột điểm số
                }
                return String.class;
            }
        };
        
        tableDanhGia = new JTable(tableModel);
        tableDanhGia.setRowHeight(30);
        tableDanhGia.setFont(new Font("Arial", Font.PLAIN, 14));
        tableDanhGia.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableDanhGia.getTableHeader().setBackground(new Color(240, 240, 240));
        
        // Thay đổi màu selection thành màu xanh đậm
        tableDanhGia.setSelectionBackground(SELECTION_COLOR); // Màu xanh đậm cho selection
        tableDanhGia.setSelectionForeground(Color.WHITE); // Màu chữ trắng khi được chọn
        tableDanhGia.setGridColor(new Color(240, 240, 240));
        
        // Căn giữa cho một số cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        tableDanhGia.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Mã đánh giá
        tableDanhGia.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Hợp đồng
        tableDanhGia.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Điểm số
        tableDanhGia.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Ngày đánh giá
        
        // Set độ rộng các cột
        tableDanhGia.getColumnModel().getColumn(0).setPreferredWidth(80);  // Mã đánh giá
        tableDanhGia.getColumnModel().getColumn(1).setPreferredWidth(80);  // Hợp đồng
        tableDanhGia.getColumnModel().getColumn(2).setPreferredWidth(150); // Tên xe
        tableDanhGia.getColumnModel().getColumn(3).setPreferredWidth(60);  // Điểm số
        tableDanhGia.getColumnModel().getColumn(4).setPreferredWidth(300); // Nhận xét
        tableDanhGia.getColumnModel().getColumn(5).setPreferredWidth(100); // Ngày đánh giá
        
        // Sự kiện click vào bảng để cập nhật trạng thái nút
        tableDanhGia.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                capNhatTrangThaiNut();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableDanhGia);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        pnlTable.add(scrollPane, BorderLayout.CENTER);
        
        // ---- PHẦN NÚT CHỨC NĂNG ----
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnSuaDanhGia = new JButton("Sửa đánh giá");
        btnSuaDanhGia.setFont(new Font("Arial", Font.PLAIN, 14));
        btnSuaDanhGia.setFocusPainted(false);
        btnSuaDanhGia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suaDanhGia();
            }
        });
        
        btnXoaDanhGia = new JButton("Xóa đánh giá");
        btnXoaDanhGia.setFont(new Font("Arial", Font.PLAIN, 14));
        btnXoaDanhGia.setFocusPainted(false);
        btnXoaDanhGia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xoaDanhGia();
            }
        });
        
        btnThemDanhGia = new JButton("Thêm đánh giá mới");
        btnThemDanhGia.setFont(new Font("Arial", Font.PLAIN, 14));
        btnThemDanhGia.setBackground(DARK_BLUE);
        btnThemDanhGia.setForeground(Color.WHITE);
        btnThemDanhGia.setFocusPainted(false);
        btnThemDanhGia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themDanhGia();
            }
        });
        
        pnlButtons.add(btnSuaDanhGia);
        pnlButtons.add(btnXoaDanhGia);
        pnlButtons.add(btnThemDanhGia);
        
        pnlTable.add(pnlButtons, BorderLayout.SOUTH);
        
        pnlContent.add(pnlTable, BorderLayout.CENTER);
        add(pnlContent, BorderLayout.CENTER);
        
        // Cập nhật trạng thái nút
        capNhatTrangThaiNut();
    }
    
    // Cập nhật trạng thái các nút dựa trên hàng được chọn
    private void capNhatTrangThaiNut() {
        int selectedRow = tableDanhGia.getSelectedRow();
        btnSuaDanhGia.setEnabled(selectedRow != -1);
        btnXoaDanhGia.setEnabled(selectedRow != -1);
    }
    
    // Tải dữ liệu đánh giá
    public void loadData() {
        if (khachHang == null) return;
        
        // Xóa dữ liệu cũ trong bảng
        tableModel.setRowCount(0);
        
        try {
            // Lấy danh sách đánh giá của khách hàng
            List<DanhGia> danhSachDanhGia = controller.getDanhGiaByMaKH(khachHang.getMaKH());
            
            // Nhóm đánh giá theo hợp đồng
            Map<String, List<DanhGia>> groupedByHopDong = danhSachDanhGia.stream()
                    .collect(Collectors.groupingBy(DanhGia::getMaHD));
            
            // Hiển thị dữ liệu nhóm theo hợp đồng
            for (String maHD : groupedByHopDong.keySet()) {
                List<DanhGia> danhGiasInHopDong = groupedByHopDong.get(maHD);
                
                // Tạo chuỗi tên xe cách nhau bởi dấu phẩy
                String tenXeList = danhGiasInHopDong.stream()
                        .map(DanhGia::getTenXe)
                        .distinct()
                        .collect(Collectors.joining(", "));
                
                // Lấy thông tin đánh giá đầu tiên làm đại diện (vì các đánh giá cùng hợp đồng có cùng điểm và bình luận)
                DanhGia firstDanhGia = danhGiasInHopDong.get(0);
                
                tableModel.addRow(new Object[]{
                    firstDanhGia.getMaDG(),
                    maHD,
                    tenXeList,
                    firstDanhGia.getDiemSo(),
                    firstDanhGia.getBinhLuan(),
                    dateFormat.format(firstDanhGia.getNgayDanhGia())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Lỗi khi tải dữ liệu đánh giá: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        
        // Cập nhật trạng thái nút
        capNhatTrangThaiNut();
    }
    
    // Hàm thêm đánh giá mới
    private void themDanhGia() {
        if (khachHang == null) {
            JOptionPane.showMessageDialog(this, 
                    "Vui lòng đăng nhập để thêm đánh giá",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            ThemDanhGiaDialog dialog = new ThemDanhGiaDialog(null, true, controller, khachHang);
            dialog.setVisible(true);
            
            // Nếu thêm thành công thì cập nhật lại bảng
            if (dialog.isSuccess()) {
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Lỗi khi mở form thêm đánh giá: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // Hàm sửa đánh giá
    private void suaDanhGia() {
        int selectedRow = tableDanhGia.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn đánh giá cần sửa",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String maDG = tableDanhGia.getValueAt(selectedRow, 0).toString();
        
        try {
            // Lấy thông tin đánh giá từ controller
            DanhGia danhGia = controller.getDanhGiaByMaDG(maDG);
            
            if (danhGia == null) {
                JOptionPane.showMessageDialog(this, 
                        "Không tìm thấy thông tin đánh giá",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Mở dialog sửa đánh giá
            SuaDanhGiaDialog dialog = new SuaDanhGiaDialog(null, true, controller, danhGia);
            dialog.setVisible(true);
            
            // Nếu sửa thành công thì cập nhật lại bảng
            if (dialog.isSuccess()) {
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Lỗi khi mở form sửa đánh giá: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // Hàm xóa đánh giá
    private void xoaDanhGia() {
        int selectedRow = tableDanhGia.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn đánh giá cần xóa",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String maDG = tableDanhGia.getValueAt(selectedRow, 0).toString();
        String maHD = tableDanhGia.getValueAt(selectedRow, 1).toString();
        String tenXe = tableDanhGia.getValueAt(selectedRow, 2).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa đánh giá cho hợp đồng " + maHD + " (" + tenXe + ")?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = controller.deleteDanhGia(maDG);
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Xóa đánh giá thành công",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không thể xóa đánh giá. Vui lòng thử lại sau",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi xóa đánh giá: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}