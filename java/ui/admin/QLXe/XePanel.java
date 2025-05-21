//package ui.admin.QLXe;
//
//import ui.admin.QLXe.XeDetailDialog;
//import ui.admin.QLXe.XeDialog;
//import controller.XeController;
//import model.Xe;
//import util.ImageUtil;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableColumnModel;
//import java.awt.*;
//import java.util.List;
//import javax.swing.table.JTableHeader;  // Thêm import này
//import javax.swing.BorderFactory;  // Thêm import này
//import javax.swing.SwingConstants;  // Thêm import này
//import javax.swing.table.DefaultTableCellRenderer;
//import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
//import ui.admin.ButtonEditor;
//import ui.admin.ButtonRenderer;
//
//public class XePanel extends JPanel {
//    private JTable tableXe;
//    private DefaultTableModel modelXe;
//    private JTextField txtSearch;
//    private JButton btnAdd, btnRefresh, btnExport;
//    private XeController xeController;
//    private JComboBox<String> cboFilter;
//    
//    public static String getImagePath(String fileName) {
//        return ImageUtil.getImageDirPath() + fileName;
//    }
//    // Cột của bảng - giảm số cột hiển thị
//    private final String[] COLUMNS = {
//        "Mã Xe", "Tên Xe", "Biển Số", "Hãng Xe", 
//        "Năm SX", "Trạng Thái", "Giá Thuê/Ngày", "Thao Tác"
//    };
//
//    public XePanel() {
//        xeController = new XeController();
//        initComponents();
//        loadDataToTable();
//    }
//
//    private void initComponents() {
//        setLayout(new BorderLayout(10, 10));
//        setBorder(new EmptyBorder(15, 15, 15, 15));
//        
//        // Panel tiêu đề
//        JPanel pnlTitle = new JPanel(new BorderLayout());
//        JLabel lblTitle = new JLabel("QUẢN LÝ XE");
//        pnlTitle.setLayout(new BorderLayout());
//        pnlTitle.setBorder(new EmptyBorder(15, 20, 15, 20));
//        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
//        pnlTitle.add(lblTitle, BorderLayout.WEST);
//        
//        
//        // Panel tìm kiếm và lọc
//        JPanel pnlSearchFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        txtSearch = new JTextField(20);
//        cboFilter = new JComboBox<>(new String[]{"Tất cả", "Đang thuê", "Sẵn sàng", "Bảo dưỡng"});
//        btnRefresh = new JButton("Làm mới");
//        btnExport = new JButton("Xuất Excel");
//        
//        pnlSearchFilter.add(new JLabel("Tìm kiếm:"));
//        pnlSearchFilter.add(txtSearch);
//        pnlSearchFilter.add(new JLabel("Lọc:"));
//        pnlSearchFilter.add(cboFilter);
//        pnlSearchFilter.add(btnRefresh);
//        pnlSearchFilter.add(btnExport);
//        
//        pnlTitle.add(pnlSearchFilter, BorderLayout.EAST);
//        add(pnlTitle, BorderLayout.NORTH);
//        
//        // Panel thêm xe
//        btnAdd = new JButton("Thêm xe");
//        JPanel pnlAdd = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        pnlAdd.add(btnAdd);
//        add(pnlAdd, BorderLayout.SOUTH);
//        
//        // Bảng danh sách xe
//        modelXe = new DefaultTableModel(COLUMNS, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return column == 7; // Chỉ cho phép chỉnh sửa cột "Thao tác"
//            }
//        };
//        
//        tableXe = new JTable(modelXe);
//        tableXe.setRowHeight(40); // Chiều cao vừa phải
//     
//        // Thiết lập độ rộng cột
//        TableColumnModel columnModel = tableXe.getColumnModel();
//        columnModel.getColumn(0).setPreferredWidth(60);  // Mã xe
//        columnModel.getColumn(1).setPreferredWidth(150); // Tên xe
//        columnModel.getColumn(2).setPreferredWidth(100); // Biển số
//        columnModel.getColumn(3).setPreferredWidth(100); // Hãng xe
//        columnModel.getColumn(4).setPreferredWidth(80);  // Năm SX
//        columnModel.getColumn(5).setPreferredWidth(120); // Trạng thái
//        columnModel.getColumn(6).setPreferredWidth(120); // Giá thuê
//        columnModel.getColumn(7).setPreferredWidth(150); // Thao tác
//        
//        // Custom renderer cho cột thao tác
//        tableXe.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
//        tableXe.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(this));
//        
//        //
//        JTableHeader header = tableXe.getTableHeader();
//        header.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        header.setBackground(new Color(240, 240, 240));
//        header.setForeground(new Color(60, 60, 60));
//        header.setPreferredSize(new Dimension(0, 40));
//        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
//
//        // Tùy chỉnh grid lines
//        tableXe.setShowGrid(true);
//        tableXe.setGridColor(new Color(230, 230, 230));
//
//        // Tạo hiệu ứng dòng sọc
//        tableXe.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value,
//                    boolean isSelected, boolean hasFocus, int row, int column) {
//                Component comp = super.getTableCellRendererComponent(
//                        table, value, isSelected, hasFocus, row, column);
//
//                // Thêm padding cho text trong ô
//                if (comp instanceof JLabel) {
//                    ((JLabel) comp).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
//                }
//
//                // Màu nền dòng chẵn/lẻ nếu không được chọn và không phải cột thao tác
//                if (!isSelected && column != 7) {
//                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
//                    comp.setForeground(new Color(50, 50, 50));
//                }
//
//                // Căn phải cho cột giá tiền
//                if (column == 6) { // Giá thuê
//                    setHorizontalAlignment(SwingConstants.RIGHT);
//                } else {
//                    setHorizontalAlignment(SwingConstants.LEFT);
//                }
//
//                return comp;
//            }
//        });
//        
//        
//        JScrollPane scrollPane = new JScrollPane(tableXe);
//        add(scrollPane, BorderLayout.CENTER);
//        
//        // Làm đẹp các nút
//        styleButton(btnAdd, new Color(41, 121, 255));
//        styleButton(btnRefresh, new Color(0, 150, 136));
//        styleButton(btnExport, new Color(113, 85, 156));
//
//        // Làm đẹp thanh tìm kiếm
//        txtSearch.setPreferredSize(new Dimension(200, 30));
//        txtSearch.setBorder(BorderFactory.createCompoundBorder(
//            BorderFactory.createLineBorder(new Color(200, 200, 200)),
//            BorderFactory.createEmptyBorder(5, 10, 5, 10)
//        ));
//
//        // Làm đẹp dropdown lọc
//        cboFilter.setPreferredSize(new Dimension(150, 30));
//        
//        // Thêm sự kiện
//        btnAdd.addActionListener(e -> showXeDialog(null)); // null = thêm mới
//        btnRefresh.addActionListener(e -> loadDataToTable());
//        btnExport.addActionListener(e -> exportToExcel());
//        
//        txtSearch.addActionListener(e -> searchXe());
//        cboFilter.addActionListener(e -> filterXe());
//    }
//    
//    private void styleButton(JButton button, Color bgColor) {
//        button.setBackground(bgColor);
//        button.setForeground(Color.WHITE);
//        button.setFocusPainted(false);
//        button.setBorderPainted(false);
//        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        button.setPreferredSize(new Dimension(120, 35));
//    }
//    
//    public void loadDataToTable() {
//        modelXe.setRowCount(0); // Xóa dữ liệu cũ
//        
//        List<Xe> danhSachXe = xeController.getAllXe();
//        for (Xe xe : danhSachXe) {
//            // Thêm dữ liệu vào bảng - đơn giản hóa, không hiển thị số chỗ và hình ảnh
//            modelXe.addRow(new Object[]{
//                xe.getMaXe(),
//                xe.getTenXe(),
//                xe.getBienSo(),
//                xe.getHangXe(),
//                xe.getNamSX(),
//                xe.getTrangThai(),
//                String.format("%,d VND", (int)xe.getGiaThueNgay()),
//                "" // Cột thao tác
//            });
//        }
//    }
//
//    private void searchXe() {
//        String keyword = txtSearch.getText().trim();
//        if (keyword.isEmpty()) {
//            loadDataToTable();
//            return;
//        }
//        
//        modelXe.setRowCount(0);
//        List<Xe> danhSachXe = xeController.searchXe(keyword);
//        
//        // Hiển thị kết quả tìm kiếm
//        for (Xe xe : danhSachXe) {
//            modelXe.addRow(new Object[]{
//                xe.getMaXe(),
//                xe.getTenXe(),
//                xe.getBienSo(),
//                xe.getHangXe(),
//                xe.getNamSX(),
//                xe.getTrangThai(),
//                String.format("%,d VND", (int)xe.getGiaThueNgay()),
//                "" // Cột thao tác
//            });
//        }
//    }
//    
//    private void filterXe() {
//        String filter = (String) cboFilter.getSelectedItem();
//        if (filter.equals("Tất cả")) {
//            loadDataToTable();
//            return;
//        }
//        
//        modelXe.setRowCount(0);
//        List<Xe> danhSachXe = xeController.getXeByTrangThai(filter);
//        
//        // Hiển thị kết quả lọc
//        for (Xe xe : danhSachXe) {
//            modelXe.addRow(new Object[]{
//                xe.getMaXe(),
//                xe.getTenXe(),
//                xe.getBienSo(),
//                xe.getHangXe(),
//                xe.getNamSX(),
//                xe.getTrangThai(),
//                String.format("%,d VND", (int)xe.getGiaThueNgay()),
//                "" // Cột thao tác
//            });
//        }
//    }
//    
//    public void showXeDialog(Xe xe) {
//        XeDialog dialog = new XeDialog(SwingUtilities.getWindowAncestor(this), xe, this);
//        dialog.setVisible(true);
//    }
//
//    public void showXeDetailDialog(Xe xe) {
//        XeDetailDialog dialog = new XeDetailDialog(SwingUtilities.getWindowAncestor(this), xe, this);
//        dialog.setVisible(true);
//    }
//    
//    private void exportToExcel() {
//        JOptionPane.showMessageDialog(this, "Chức năng xuất Excel sẽ được phát triển sau!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//    }
//}


package ui.admin.QLXe;

import controller.XeController;
import model.Xe;
import util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class XePanel extends JPanel {
    private XeController xeController;
    private SearchFilterPanel searchFilterPanel;
    private XeTablePanel tablePanel;
    private JButton btnAdd;
    
    public static String getImagePath(String fileName) {
        return ImageUtil.getImageDirPath() + fileName;
    }

    public XePanel() {
        xeController = new XeController();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
      //  setBackground(new Color(240, 248, 255)); // Màu nền xanh nhạt
        
        // 1. Panel tiêu đề
        JPanel pnlTitle = new JPanel(new BorderLayout());
     //   pnlTitle.setBackground(Color.WHITE);
        
        pnlTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel lblTitle = new JLabel("QUẢN LÝ XE");
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        
        pnlTitle.add(lblTitle, BorderLayout.WEST);
        
        
        // 2. Tạo SearchFilterPanel
        searchFilterPanel = new SearchFilterPanel();
        pnlTitle.add(searchFilterPanel, BorderLayout.EAST);
        add(pnlTitle, BorderLayout.NORTH);
        
        // 3. Tạo XeTablePanel
        tablePanel = new XeTablePanel(this);
        add(tablePanel, BorderLayout.CENTER);
        
        // 4. Panel nút thêm xe
        btnAdd = new JButton("Thêm xe");
        styleButton(btnAdd, new Color(41, 121, 255));
        
        JPanel pnlAdd = new JPanel(new FlowLayout(FlowLayout.LEFT));
      
        pnlAdd.add(btnAdd);
        add(pnlAdd, BorderLayout.SOUTH);
        
        // 5. Thêm các sự kiện
        btnAdd.addActionListener(e -> showXeDialog(null));
        
        searchFilterPanel.addSearchActionListener(e -> searchXe());
        searchFilterPanel.addFilterActionListener(e -> filterXe());
        searchFilterPanel.addRefreshActionListener(e -> loadDataToTable());
        searchFilterPanel.addExportActionListener(e -> exportToExcel());
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
    }
    
    public void loadDataToTable() {
        List<Xe> danhSachXe = xeController.getAllXe();
        tablePanel.updateData(danhSachXe);
        searchFilterPanel.resetFilter();
    }

    private void searchXe() {
        String keyword = searchFilterPanel.getSearchText();
        if (keyword.isEmpty()) {
            loadDataToTable();
            return;
        }
        
        List<Xe> danhSachXe = xeController.searchXe(keyword);
        tablePanel.updateData(danhSachXe);
    }
    
    private void filterXe() {
        String filter = searchFilterPanel.getSelectedFilter();
        if (filter.equals("Tất cả")) {
            loadDataToTable();
            return;
        }
        
        List<Xe> danhSachXe = xeController.getXeByTrangThai(filter);
        tablePanel.updateData(danhSachXe);
    }
    
    public void showXeDialog(Xe xe) {
        XeDialog dialog = new XeDialog(SwingUtilities.getWindowAncestor(this), xe, this);
        dialog.setVisible(true);
    }

    public void showXeDetailDialog(Xe xe) {
        XeDetailDialog dialog = new XeDetailDialog(SwingUtilities.getWindowAncestor(this), xe, this);
        dialog.setVisible(true);
    }
    
    private void exportToExcel() {
        JOptionPane.showMessageDialog(this, 
            "Chức năng xuất Excel sẽ được phát triển sau!", 
            "Thông báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Phương thức lấy xe từ DB theo mã
    public Xe getXeById(String maXe) {
        return xeController.getXeByMa(maXe);
    }
    public boolean deleteXe(String maXe) {
        return xeController.deleteXe(maXe);
    }
}