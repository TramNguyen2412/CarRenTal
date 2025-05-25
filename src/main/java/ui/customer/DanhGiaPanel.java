
package ui.customer;

import controller.DanhGiaController;
import model.KhachHang;
import model.TaiKhoan;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import model.ChiTietHD;
public class DanhGiaPanel extends JPanel {
    private DanhGiaController danhGiaController;
    private TaiKhoan taiKhoan;
    private KhachHang khachHang;
    
    private JTabbedPane tabbedPane;
    private JPanel unratedPanel;
    private JPanel ratedPanel;
    private JTable unratedTable;
    private JTable ratedTable;
    private DefaultTableModel unratedTableModel;
    private DefaultTableModel ratedTableModel;
    
    private SimpleDateFormat dateFormat;
    private DecimalFormat moneyFormat;
    
    public DanhGiaPanel() {
        this(null, null);
    }
    
    public DanhGiaPanel(TaiKhoan taiKhoan, KhachHang khachHang) {
        this.taiKhoan = taiKhoan;
        this.khachHang = khachHang;
        this.danhGiaController = new DanhGiaController();
        
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        moneyFormat = new DecimalFormat("#,###");
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        initComponents();
        if (taiKhoan != null && khachHang != null) {
            loadData();
        }
    }
    
    private void initComponents() {
        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Đánh Giá Hợp Đồng");
        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Tạo TabPane cho 2 tab: Chưa đánh giá và Đã đánh giá
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        
        // Tab Chưa đánh giá
        unratedPanel = createUnratedPanel();
        tabbedPane.addTab("Chưa Đánh Giá", unratedPanel);
        
        // Tab Đã đánh giá
        ratedPanel = createRatedPanel();
        tabbedPane.addTab("Đã Đánh Giá", ratedPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createUnratedPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Tạo model cho bảng
        String[] columnNames = {
            "Mã Hợp Đồng", "Xe Thuê", "Thời Gian Thuê", "Tổng Tiền", "Thao Tác"
        };
        unratedTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Chỉ cho phép chỉnh sửa cột thao tác
            }
        };
        
        // Tạo bảng
        unratedTable = new JTable(unratedTableModel);
        unratedTable.setRowHeight(40);
        unratedTable.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        unratedTable.getTableHeader().setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        unratedTable.setShowGrid(true);
        unratedTable.setGridColor(new Color(230, 230, 230));
        
        // Căn giữa các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Căn phải cột tiền
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        // Thiết lập renderer cho các cột
        for (int i = 0; i < unratedTable.getColumnCount() - 1; i++) {
            if (i == 3) { // Cột tổng tiền căn phải
                unratedTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
            } else {
                unratedTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        // Thiết lập renderer cho cột thao tác
        unratedTable.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JButton button = new JButton("Đánh giá");
                button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
                button.setBackground(new Color(0, 120, 215));
                button.setForeground(Color.WHITE);
                
                return button;
            }
        });
        
        // Thiết lập editor cho cột thao tác
        unratedTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JTextField()) {
            private int clickedRow;
            
            {
                setClickCountToStart(1); // Chỉ cần click 1 lần
            }
            
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                clickedRow = row;
                
                JButton button = new JButton("Đánh giá");
                button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
                button.setBackground(new Color(0, 120, 215));
                button.setForeground(Color.WHITE);
                button.addActionListener(e -> {
                    SwingUtilities.invokeLater(() -> rateContract(clickedRow));
                    fireEditingStopped();
                });
                
                return button;
            }
            
            @Override
            public Object getCellEditorValue() {
                return "";
            }
        });
        
        // Thiết lập kích thước cột
        unratedTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Mã HD
        unratedTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Xe thuê
        unratedTable.getColumnModel().getColumn(2).setPreferredWidth(250); // Thời gian
        unratedTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Tổng tiền
        unratedTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Thao tác
        
        // Tạo panel con để chứa bảng
        JScrollPane scrollPane = new JScrollPane(unratedTable);
        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230)));
        
        panel.add(createInfoLabel("Các hợp đồng hoàn thành chưa được đánh giá"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRatedPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Tạo model cho bảng
        String[] columnNames = {
            "Mã Hợp Đồng", "Xe Thuê", "Thời Gian Thuê", "Đánh Giá", "Ngày Đánh Giá", "Thao Tác"
        };
        ratedTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Chỉ cho phép chỉnh sửa cột thao tác
            }
        };
        
        // Tạo bảng
        ratedTable = new JTable(ratedTableModel);
        ratedTable.setRowHeight(40);
        ratedTable.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        ratedTable.getTableHeader().setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        ratedTable.setShowGrid(true);
        ratedTable.setGridColor(new Color(230, 230, 230));
        
        // Căn giữa các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Thiết lập renderer cho các cột (trừ cột thao tác và đánh giá)
        for (int i = 0; i < ratedTable.getColumnCount(); i++) {
            if (i != 3 && i != 5) { // Bỏ qua cột đánh giá (3) và thao tác (5)
                ratedTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        // Renderer cho cột đánh giá (hiển thị sao)
        ratedTable.getColumnModel().getColumn(3).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel();
                panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
                
                if (isSelected) {
                    panel.setBackground(table.getSelectionBackground());
                } else {
                    panel.setBackground(table.getBackground());
                }
                
                if (value instanceof Integer) {
                    int rating = (Integer) value;
                    
                    // Tạo panel chứa các ngôi sao
                    JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
                    starsPanel.setOpaque(false);
                    
                    for (int i = 1; i <= 5; i++) {
                        JLabel starLabel = new JLabel();
                        if (i <= rating) {
                            starLabel.setText("★"); // Filled star
                            starLabel.setForeground(new Color(255, 165, 0)); // Orange
                        } else {
                            starLabel.setText("☆"); // Empty star
                            starLabel.setForeground(new Color(180, 180, 180)); // Light gray
                        }
                        starLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
                        starsPanel.add(starLabel);
                    }
                    
                    JLabel ratingLabel = new JLabel(" " + rating + "/5");
                    ratingLabel.setForeground(new Color(100, 100, 100));
                    ratingLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 12));
                    
                    panel.add(starsPanel);
                    panel.add(ratingLabel);
                }
                
                return panel;
            }
        });
        
        // Thiết lập renderer cho cột thao tác
        ratedTable.getColumnModel().getColumn(5).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
                
                if (isSelected) {
                    panel.setBackground(table.getSelectionBackground());
                } else {
                    panel.setBackground(table.getBackground());
                }
                
                JButton editButton = new JButton("Sửa");
                editButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
                editButton.setPreferredSize(new Dimension(70, 30));
                editButton.setBackground(new Color(52, 152, 219));
                editButton.setForeground(Color.WHITE);
                
                JButton deleteButton = new JButton("Xóa");
                deleteButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
                deleteButton.setPreferredSize(new Dimension(70, 30));
                deleteButton.setBackground(new Color(231, 76, 60));
                deleteButton.setForeground(Color.WHITE);
                
                panel.add(editButton);
                panel.add(deleteButton);
                
                return panel;
            }
        });

        // Editor cho cột thao tác
        ratedTable.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JTextField()) {
            private int clickedRow;
            
            {
                setClickCountToStart(1); // Chỉ cần click 1 lần
            }
            
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                clickedRow = row;
                
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
                panel.setBackground(table.getSelectionBackground());
                
                JButton editButton = new JButton("Sửa");
                editButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
                editButton.setPreferredSize(new Dimension(70, 30));
                editButton.setBackground(new Color(52, 152, 219));
                editButton.setForeground(Color.WHITE);
                editButton.addActionListener(e -> {
                    SwingUtilities.invokeLater(() -> editRating(clickedRow));
                    fireEditingStopped();
                });
                
                JButton deleteButton = new JButton("Xóa");
                deleteButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
                deleteButton.setPreferredSize(new Dimension(70, 30));
                deleteButton.setBackground(new Color(231, 76, 60));
                deleteButton.setForeground(Color.WHITE);
                deleteButton.addActionListener(e -> {
                    SwingUtilities.invokeLater(() -> deleteRating(clickedRow));
                    fireEditingStopped();
                });
                
                panel.add(editButton);
                panel.add(deleteButton);
                
                return panel;
            }
            
            @Override
            public Object getCellEditorValue() {
                return "";
            }
        });
        
        // Thiết lập kích thước cột
        ratedTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Mã HD
        ratedTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Xe thuê
        ratedTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Thời gian
        ratedTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Đánh giá
        ratedTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Ngày đánh giá
        ratedTable.getColumnModel().getColumn(5).setPreferredWidth(180); // Thao tác
        
        // Tạo panel con để chứa bảng
        JScrollPane scrollPane = new JScrollPane(ratedTable);
        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230)));
        
        panel.add(createInfoLabel("Các hợp đồng đã được đánh giá"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
        label.setBorder(new EmptyBorder(0, 0, 10, 0));
        return label;
    }
    
    public void loadData() {
        if (khachHang == null || khachHang.getMaKH() == null) {
            JOptionPane.showMessageDialog(this,
                "Không thể tải dữ liệu đánh giá. Vui lòng đăng nhập lại.",
                "Lỗi tải dữ liệu",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Xóa dữ liệu cũ
        unratedTableModel.setRowCount(0);
        ratedTableModel.setRowCount(0);
        
        // Tải danh sách hợp đồng chưa đánh giá
//        List<Map<String, Object>> unratedContracts = danhGiaController.getHopDongChuaDanhGia(khachHang.getMaKH());
//        for (Map<String, Object> contract : unratedContracts) {
//            String maHD = (String) contract.get("MaHD");
//            String tenXe = (String) contract.get("TenXe");
//            int soLuongXe = (int) contract.get("SoLuongXe");
//            
//            String xeInfo = tenXe;
//            if (soLuongXe > 1) {
//                xeInfo += " và " + (soLuongXe - 1) + " xe khác";
//            }
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
//        
//        // Tải danh sách hợp đồng đã đánh giá
//        List<Map<String, Object>> ratedContracts = danhGiaController.getHopDongDaDanhGia(khachHang.getMaKH());
//        for (Map<String, Object> contract : ratedContracts) {
//            String maHD = (String) contract.get("MaHD");
//            String tenXe = (String) contract.get("TenXe");
//            int soLuongXe = (int) contract.get("SoLuongXe");
//            
//            String xeInfo = tenXe;
//            if (soLuongXe > 1) {
//                xeInfo += " và " + (soLuongXe - 1) + " xe khác";
//            }
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
        List<Map<String, Object>> unratedContracts = danhGiaController.getHopDongChuaDanhGia(khachHang.getMaKH());
        for (Map<String, Object> contract : unratedContracts) {
            String maHD = (String) contract.get("MaHD");

            // Lấy danh sách chi tiết xe thuê theo mã hợp đồng
            List<ChiTietHD> chiTietList = danhGiaController.getChiTietHopDong(maHD);

            // Xây dựng thông tin xe
            StringBuilder xeInfoBuilder = new StringBuilder();
            Map<String, Integer> xeCounts = new HashMap<>(); // Để đếm số lượng mỗi loại xe

            // Đếm số lượng của mỗi loại xe dựa trên tên xe
            for (ChiTietHD chiTiet : chiTietList) {
                String tenXe = chiTiet.getTenXe();
                xeCounts.put(tenXe, xeCounts.getOrDefault(tenXe, 0) + 1);
            }

            // Xây dựng chuỗi thông tin xe
            int i = 0;
            for (Map.Entry<String, Integer> entry : xeCounts.entrySet()) {
                String tenXe = entry.getKey();
                int soLuong = entry.getValue();

                if (soLuong > 1) {
                    xeInfoBuilder.append(tenXe).append(" (").append(soLuong).append(" xe)");
                } else {
                    xeInfoBuilder.append(tenXe);
                }

                // Nếu không phải mục cuối cùng, thêm dấu phẩy
                if (i < xeCounts.size() - 1) {
                    xeInfoBuilder.append(", ");
                }
                i++;
            }

            String xeInfo = xeInfoBuilder.toString();

            String thoiGian = dateFormat.format(contract.get("NgayBatDau")) + " đến " + 
                             dateFormat.format(contract.get("NgayKetThuc"));

            String tongTien = moneyFormat.format(contract.get("TongTien")) + " VND";

            unratedTableModel.addRow(new Object[]{
                maHD,
                xeInfo,
                thoiGian,
                tongTien,
                "Đánh giá"
            });
        }
        List<Map<String, Object>> ratedContracts = danhGiaController.getHopDongDaDanhGia(khachHang.getMaKH());
        for (Map<String, Object> contract : ratedContracts) {
            String maHD = (String) contract.get("MaHD");

            // Lấy danh sách chi tiết xe thuê theo mã hợp đồng
            List<ChiTietHD> chiTietList = danhGiaController.getChiTietHopDong(maHD);

            // Xây dựng thông tin xe
            StringBuilder xeInfoBuilder = new StringBuilder();
            Map<String, Integer> xeCounts = new HashMap<>(); // Để đếm số lượng mỗi loại xe

            // Đếm số lượng của mỗi loại xe dựa trên tên xe
            for (ChiTietHD chiTiet : chiTietList) {
                String tenXe = chiTiet.getTenXe();
                xeCounts.put(tenXe, xeCounts.getOrDefault(tenXe, 0) + 1);
            }

            // Xây dựng chuỗi thông tin xe
            int i = 0;
            for (Map.Entry<String, Integer> entry : xeCounts.entrySet()) {
                String tenXe = entry.getKey();
                int soLuong = entry.getValue();

                if (soLuong > 1) {
                    xeInfoBuilder.append(tenXe).append(" (").append(soLuong).append(" xe)");
                } else {
                    xeInfoBuilder.append(tenXe);
                }

                // Nếu không phải mục cuối cùng, thêm dấu phẩy
                if (i < xeCounts.size() - 1) {
                    xeInfoBuilder.append(", ");
                }
                i++;
            }

            String xeInfo = xeInfoBuilder.toString();

            String thoiGian = dateFormat.format(contract.get("NgayBatDau")) + " đến " + 
                             dateFormat.format(contract.get("NgayKetThuc"));

            int diemSo = (int) contract.get("DiemSo");
            String ngayDanhGia = dateFormat.format(contract.get("NgayDanhGia"));

            ratedTableModel.addRow(new Object[]{
                maHD,
                xeInfo,
                thoiGian,
                diemSo,
                ngayDanhGia,
                "Thao tác" // Giá trị này không quan trọng vì sẽ bị thay thế bởi custom renderer
            });
        }


        // Thông báo nếu không có dữ liệu
        if (unratedContracts.isEmpty()) {
            JLabel emptyLabel = new JLabel("Không có hợp đồng nào cần đánh giá", JLabel.CENTER);
            emptyLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.add(emptyLabel, BorderLayout.CENTER);
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.setBorder(new EmptyBorder(50, 0, 0, 0));
            
            unratedPanel.add(emptyPanel, BorderLayout.CENTER);
            unratedPanel.revalidate();
        }
        
        if (ratedContracts.isEmpty()) {
            JLabel emptyLabel = new JLabel("Bạn chưa đánh giá hợp đồng nào", JLabel.CENTER);
            emptyLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.add(emptyLabel, BorderLayout.CENTER);
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.setBorder(new EmptyBorder(50, 0, 0, 0));
            
            ratedPanel.add(emptyPanel, BorderLayout.CENTER);
            ratedPanel.revalidate();
        }
    }
    
    private void rateContract(int row) {
        if (row < 0 || row >= unratedTableModel.getRowCount()) {
            return;
        }
        
        String maHD = (String) unratedTableModel.getValueAt(row, 0);
        Map<String, Object> hopDong = null;
        
        // Tìm hợp đồng tương ứng
        List<Map<String, Object>> unratedContracts = danhGiaController.getHopDongChuaDanhGia(khachHang.getMaKH());
        for (Map<String, Object> contract : unratedContracts) {
            if (maHD.equals(contract.get("MaHD"))) {
                hopDong = contract;
                break;
            }
        }
        
        if (hopDong != null) {
            DanhGiaDialog dialog = new DanhGiaDialog((JFrame) SwingUtilities.getWindowAncestor(this), 
                                                     khachHang.getMaKH(), hopDong);
            dialog.setVisible(true);
            
            // Refresh data after rating
            loadData();
        }
    }
    
    private void editRating(int row) {
        if (row < 0 || row >= ratedTableModel.getRowCount()) {
            return;
        }
        
        String maHD = (String) ratedTableModel.getValueAt(row, 0);
        Map<String, Object> hopDong = null;
        
        // Tìm hợp đồng tương ứng
        List<Map<String, Object>> ratedContracts = danhGiaController.getHopDongDaDanhGia(khachHang.getMaKH());
        for (Map<String, Object> contract : ratedContracts) {
            if (maHD.equals(contract.get("MaHD"))) {
                hopDong = contract;
                break;
            }
        }
        
        if (hopDong != null) {
            SuaDanhGiaDialog dialog = new SuaDanhGiaDialog((JFrame) SwingUtilities.getWindowAncestor(this), hopDong);
            dialog.setVisible(true);
            
            // Refresh data after editing
            loadData();
        }
    }
    
    private void deleteRating(int row) {
        if (row < 0 || row >= ratedTableModel.getRowCount()) {
            return;
        }
        
        String maHD = (String) ratedTableModel.getValueAt(row, 0);
        Map<String, Object> hopDong = null;
        
        // Tìm hợp đồng tương ứng
        List<Map<String, Object>> ratedContracts = danhGiaController.getHopDongDaDanhGia(khachHang.getMaKH());
        for (Map<String, Object> contract : ratedContracts) {
            if (maHD.equals(contract.get("MaHD"))) {
                hopDong = contract;
                break;
            }
        }
        
        if (hopDong != null) {
            int option = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa đánh giá này không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (option == JOptionPane.YES_OPTION) {
                String maDG = (String) hopDong.get("MaDG");
                boolean result = danhGiaController.xoaDanhGia(maDG);
                
                if (result) {
                    JOptionPane.showMessageDialog(this,
                        "Đã xóa đánh giá thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh data after deleting
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Không thể xóa đánh giá: " + danhGiaController.getErrorMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    public void setAccount(TaiKhoan taiKhoan, KhachHang khachHang) {
        this.taiKhoan = taiKhoan;
        this.khachHang = khachHang;
        
        if (taiKhoan != null && khachHang != null) {
            loadData();
        }
    }
}