//package ui.admin.QLHD;
//
//import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
//import com.toedter.calendar.JDateChooser; // Add this import for JCalendar
//import controller.XeController;
//import model.ChiTietHD;
//import model.Xe;
//import java.awt.*;
//import java.awt.event.*;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableCellEditor;
//import javax.swing.table.TableCellRenderer;
//import java.text.NumberFormat;
//import java.util.Locale;
//import controller.HopDongController;
//
//public class ChonXeDialog extends JDialog {
//    private XeController xeController;
//    private List<ChiTietHD> selectedXeList;
//    
//    private HopDongController hopDongController;
//    private String maHDHienTai; // Mã HD hiện tại (nếu đang sửa HD)
//   
//    // UI Components
//    private JTable tblXe;
//    private DefaultTableModel modelXe;
//    private JTextField txtSearch;
//    private JComboBox<String> cboHangXe;
//    private JComboBox<String> cboSoCho;
//    
//    public ChonXeDialog(Window owner, String maHD) {
//        super(owner, "Chọn xe thuê", ModalityType.APPLICATION_MODAL);
//        this.hopDongController = new HopDongController();
//        this.maHDHienTai = maHD;
//        this.xeController = new XeController();
//        this.selectedXeList = new ArrayList<>();
//       
//        initComponents();
//        loadDataToTable();
//    }
//    
//    private void initComponents() {
//        setSize(1000, 600);
//        setLocationRelativeTo(getOwner());
//        setLayout(new BorderLayout(10, 10));
//       
//        // Panel tìm kiếm
//        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
//        pnlSearch.setBorder(BorderFactory.createTitledBorder("Tìm kiếm xe"));
//        
//        pnlSearch.add(new JLabel("Tên xe:"));
//        txtSearch = new JTextField(15);
//        pnlSearch.add(txtSearch);
//        txtSearch.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                    searchXe();
//                }
//            }
//        });
//        
//        pnlSearch.add(new JLabel("Hãng xe:"));
//        cboHangXe = new JComboBox<>(new String[]{"Tất cả", "Toyota", "Honda", "Hyundai", "KIA", "Mazda", "Ford", "Chevrolet", "Mercedes-Benz", "BMW", "Audi", "Lamborghini", "Roll Royce", "Khác"});
//        pnlSearch.add(cboHangXe);
//        
//        pnlSearch.add(new JLabel("Số chỗ:"));
//        cboSoCho = new JComboBox<>(new String[]{"Tất cả", "4", "5", "7", "8", "16"});
//        pnlSearch.add(cboSoCho);
//        
//        JButton btnSearch = new JButton("Tìm kiếm");
//        pnlSearch.add(btnSearch);
//        
//        // Panel bảng dữ liệu
//        JPanel pnlTable = new JPanel(new BorderLayout(10, 10));
//        pnlTable.setBorder(BorderFactory.createTitledBorder("Danh sách xe sẵn sàng"));
//        
//        String[] columns = {"Chọn", "Mã xe", "Tên xe", "Biển số", "Hãng xe", "Số chỗ", "Giá thuê/ngày", "Từ ngày", "Đến ngày"};
//        modelXe = new DefaultTableModel(columns, 0) {
//            @Override
//            public Class<?> getColumnClass(int columnIndex) {
//                if (columnIndex == 0) {
//                    return Boolean.class;
//                }
//                return Object.class; // Changed to Object to handle Date objects
//            }
//            
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return column == 0 || column == 7 || column == 8;
//            }
//        };
//        
//        tblXe = new JTable(modelXe);
//        tblXe.getTableHeader().setReorderingAllowed(false);
//        
//        // Set custom editor for date columns
//        tblXe.getColumnModel().getColumn(7).setCellEditor(new DateChooserCellEditor());
//        tblXe.getColumnModel().getColumn(8).setCellEditor(new DateChooserCellEditor());
//        
//        // Set custom renderer for date columns
//        tblXe.getColumnModel().getColumn(7).setCellRenderer(new DateChooserCellRenderer());
//        tblXe.getColumnModel().getColumn(8).setCellRenderer(new DateChooserCellRenderer());
//        
//        JScrollPane scrollPane = new JScrollPane(tblXe);
//        pnlTable.add(scrollPane, BorderLayout.CENTER);
//        
//        // Panel nút
//        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        JButton btnAdd = new JButton("Thêm vào hợp đồng");
//        btnAdd.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        btnAdd.setBackground(new Color(41, 121, 255));           
//        btnAdd.setForeground(Color.WHITE);   
//        JButton btnCancel = new JButton("Hủy");
//        btnCancel.setBackground(Color.GRAY);            // Màu xám
//        btnCancel.setForeground(Color.WHITE); 
//        btnCancel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//
//        
//        pnlButtons.add(btnAdd);
//        pnlButtons.add(btnCancel);
//        
//        // Layout tổng thể
//        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
//        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));
//        pnlMain.add(pnlSearch, BorderLayout.NORTH);
//        pnlMain.add(pnlTable, BorderLayout.CENTER);
//        
//        add(pnlMain, BorderLayout.CENTER);
//        add(pnlButtons, BorderLayout.SOUTH);
//        
//        // Sự kiện tìm kiếm
//        btnSearch.addActionListener(e -> searchXe());
//        
//        // Sự kiện nút thêm
//        btnAdd.addActionListener(e -> addSelectedXe());
//        
//        // Sự kiện nút hủy
//        btnCancel.addActionListener(e -> dispose());
//    }
//    
//    // Custom cell editor using JDateChooser
//    class DateChooserCellEditor extends AbstractCellEditor implements TableCellEditor {
//        private JDateChooser dateChooser;
//        
//        public DateChooserCellEditor() {
//            dateChooser = new JDateChooser();
//            dateChooser.setDateFormatString("dd/MM/yyyy");
//            dateChooser.setDate(new Date()); // Default to today
//        }
//        
//        @Override
//        public Object getCellEditorValue() {
//            return dateChooser.getDate();
//        }
//        
//        @Override
//        public Component getTableCellEditorComponent(JTable table, Object value, 
//                boolean isSelected, int row, int column) {
//            if (value instanceof Date) {
//                dateChooser.setDate((Date) value);
//            } else if (value instanceof String) {
//                try {
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                    dateChooser.setDate(sdf.parse((String) value));
//                } catch (ParseException e) {
//                    dateChooser.setDate(new Date());
//                }
//            } else {
//                dateChooser.setDate(new Date());
//            }
//            return dateChooser;
//        }
//    }
//    
//    // Custom renderer for dates
//    class DateChooserCellRenderer extends DefaultTableCellRenderer {
//        private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value,
//                boolean isSelected, boolean hasFocus, int row, int column) {
//            if (value instanceof Date) {
//                value = dateFormat.format((Date) value);
//            }
//            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//        }
//    }
//    
//    private void loadDataToTable() {
//        // Xóa dữ liệu cũ
//        modelXe.setRowCount(0);
//
//        // Format để hiển thị số tiền
//        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//
//        Date today = new Date();
//        Date tomorrow = new Date(today.getTime() + 24 * 60 * 60 * 1000);
//
//        // Lấy danh sách tất cả xe
//        List<Xe> danhSachXe = xeController.getAllXe();
//
//        // Thêm dữ liệu vào bảng
//        for (Xe xe : danhSachXe) {
//            modelXe.addRow(new Object[]{
//                Boolean.FALSE,
//                xe.getMaXe(),
//                xe.getTenXe(),
//                xe.getBienSo(),
//                xe.getHangXe(),
//                xe.getSoCho(),
//                currencyFormat.format(xe.getGiaThueNgay()),
//                today,  // Now using Date objects directly
//                tomorrow
//            });
//        }
//    }
//
//    
//    private void searchXe() {
//        String keyword = txtSearch.getText().trim();
//        String hangXe = cboHangXe.getSelectedItem().toString();
//        String soCho = cboSoCho.getSelectedItem().toString();
//
//        // Xóa dữ liệu cũ
//        modelXe.setRowCount(0);
//
//        // Format để hiển thị số tiền
//        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//
//        Date today = new Date();
//        Date tomorrow = new Date(today.getTime() + 24 * 60 * 60 * 1000);
//
//        // Sử dụng phương thức searchXe() đã có để tìm kiếm
//        List<Xe> danhSachXe = xeController.searchXe(keyword);
//
//        // Lọc thêm theo hãng xe và số chỗ
//        for (Xe xe : danhSachXe) {
//            boolean matchHangXe = hangXe.equals("Tất cả") || xe.getHangXe().equals(hangXe);
//            boolean matchSoCho = soCho.equals("Tất cả") || String.valueOf(xe.getSoCho()).equals(soCho);
//
//            if (matchHangXe && matchSoCho) {
//                modelXe.addRow(new Object[]{
//                    Boolean.FALSE,
//                    xe.getMaXe(),
//                    xe.getTenXe(),
//                    xe.getBienSo(),
//                    xe.getHangXe(),
//                    xe.getSoCho(),
//                    currencyFormat.format(xe.getGiaThueNgay()),
//                    today,  // Now using Date objects directly
//                    tomorrow
//                });
//            }
//        }
//    }
//   
//    private void addSelectedXe() {
//        // Kiểm tra có xe nào được chọn không
//        boolean hasSelected = false;
//
//        // Kiểm tra từng xe được chọn trước khi thêm
//        StringBuilder errorMessages = new StringBuilder();
//        boolean hasErrors = false;
//        List<ChiTietHD> validCars = new ArrayList<>();
//
//        for (int i = 0; i < tblXe.getRowCount(); i++) {
//            Boolean selected = (Boolean) tblXe.getValueAt(i, 0);
//            if (selected) {
//                hasSelected = true;
//
//                try {
//                    // Lấy thông tin ngày thuê - now directly as Date objects
//                    Date fromDate = (Date) tblXe.getValueAt(i, 7);
//                    Date toDate = (Date) tblXe.getValueAt(i, 8);
//
//                    if (toDate.before(fromDate)) {
//                        JOptionPane.showMessageDialog(this, 
//                                "Ngày kết thúc phải sau ngày bắt đầu ở xe: " + tblXe.getValueAt(i, 2).toString(), 
//                                "Lỗi", 
//                                JOptionPane.ERROR_MESSAGE);
//                        return;
//                    }
//
//                    // Lấy thông tin xe
//                    String maXe = tblXe.getValueAt(i, 1).toString();
//                    String tenXe = tblXe.getValueAt(i, 2).toString();
//                    String bienSo = tblXe.getValueAt(i, 3).toString();
//
//                    // Kiểm tra xe có thể thuê được không
//                    String xeError = hopDongController.kiemTraXeThueDuoc(
//                        maXe, fromDate, toDate, maHDHienTai);
//
//                    if (xeError != null) {
//                        hasErrors = true;
//                        errorMessages.append("- ").append(tenXe).append(" (").append(bienSo).append("): ")
//                                  .append(xeError).append("\n");
//                        continue; // Bỏ qua xe này, kiểm tra xe khác
//                    }
//
//                    String hangXe = tblXe.getValueAt(i, 4).toString();
//                    int soCho = Integer.parseInt(tblXe.getValueAt(i, 5).toString());
//
//                    // Lấy giá thuê từ cột giá thuê/ngày
//                    String giaThueStr = tblXe.getValueAt(i, 6).toString().replaceAll("[^\\d]", "");
//                    double giaThueNgay = Double.parseDouble(giaThueStr);
//
//                    // Tạo chi tiết hợp đồng
//                    ChiTietHD ct = new ChiTietHD();
//                    ct.setMaXe(maXe);
//                    ct.setTenXe(tenXe);
//                    ct.setBienSo(bienSo);
//                    ct.setHangXe(hangXe);
//                    ct.setSoCho(soCho);
//                    ct.setGiaThueNgay(giaThueNgay);
//                    ct.setNgayBatDau(fromDate);
//                    ct.setNgayKetThuc(toDate);
//
//                    validCars.add(ct);
//
//                } catch (NumberFormatException e) {
//                    JOptionPane.showMessageDialog(this, 
//                            "Lỗi khi xử lý giá thuê xe: " + tblXe.getValueAt(i, 2).toString(), 
//                            "Lỗi", 
//                            JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//            }
//        }
//
//        if (!hasSelected) {
//            JOptionPane.showMessageDialog(this, 
//                    "Vui lòng chọn ít nhất một xe!", 
//                    "Thông báo", 
//                    JOptionPane.INFORMATION_MESSAGE);
//            return;
//        }
//
//        // Hiển thị thông báo lỗi nếu có
//        if (hasErrors) {
//            JOptionPane.showMessageDialog(this, 
//                    "Không thể thêm các xe sau vào hợp đồng:\n" + errorMessages.toString(), 
//                    "Lỗi", 
//                    JOptionPane.ERROR_MESSAGE);
//
//            // Nếu không có xe nào hợp lệ thì không đóng dialog
//            if (validCars.isEmpty()) {
//                return;
//            }
//        }
//
//        // Cập nhật danh sách xe đã chọn với các xe hợp lệ
//        selectedXeList = validCars;
//        dispose();
//    }
//    
//    public List<ChiTietHD> getSelectedXeList() {
//        return selectedXeList;
//    }
//}



package ui.admin.QLHD;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.toedter.calendar.JDateChooser;
import controller.XeController;
import model.ChiTietHD;
import model.Xe;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import controller.HopDongController;

public class ChonXeDialog extends JDialog {
    private XeController xeController;
    private List<ChiTietHD> selectedXeList;
    
    private HopDongController hopDongController;
    private String maHDHienTai;
    
    // Giới hạn số xe được chọn và số ngày thuê
    private static final int MAX_CARS_ALLOWED = 10; // Thay đổi từ 3 thành 10
    private static final int MAX_RENTAL_DAYS = 30;
   
    // UI Components
    private JTable tblXe;
    private DefaultTableModel modelXe;
    private JTextField txtSearch;
    private JComboBox<String> cboHangXe;
    private JComboBox<String> cboSoCho;
    private JDateChooser dateFromFilter;
    private JDateChooser dateToFilter;
    private JCheckBox chkShowAvailableOnly;
    private JLabel lblSelectedCount; // Hiển thị số xe đã chọn
    
    public ChonXeDialog(Window owner, String maHD) {
        super(owner, "Chọn xe thuê", ModalityType.APPLICATION_MODAL);
        this.hopDongController = new HopDongController();
        this.maHDHienTai = maHD;
        this.xeController = new XeController();
        this.selectedXeList = new ArrayList<>();
       
        initComponents();
        loadDataToTable();
    }
    
    private void initComponents() {
        // Không thay đổi
        setSize(1000, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));
       
        // Panel tìm kiếm
        JPanel pnlSearch = new JPanel(new GridBagLayout());
        pnlSearch.setBorder(BorderFactory.createTitledBorder("Tìm kiếm xe"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Hàng 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlSearch.add(new JLabel("Tên xe:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtSearch = new JTextField(15);
        pnlSearch.add(txtSearch, gbc);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchXe();
                }
            }
        });
        
        gbc.gridx = 2;
        gbc.weightx = 0;
        pnlSearch.add(new JLabel("Hãng xe:"), gbc);
        
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        cboHangXe = new JComboBox<>(new String[]{"Tất cả", "Toyota", "Honda", "Hyundai", "KIA", "Mazda", "Ford", "Chevrolet", "Mercedes-Benz", "BMW", "Audi", "Lamborghini", "Roll Royce", "Khác"});
        pnlSearch.add(cboHangXe, gbc);
        
        // Hàng 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        pnlSearch.add(new JLabel("Số chỗ:"), gbc);
        
        gbc.gridx = 1;
        cboSoCho = new JComboBox<>(new String[]{"Tất cả", "4", "5", "7", "8", "16"});
        pnlSearch.add(cboSoCho, gbc);
        
        // Thêm lọc theo ngày
        gbc.gridx = 2;
        pnlSearch.add(new JLabel("Từ ngày:"), gbc);
        
        gbc.gridx = 3;
        Date today = new Date();
        dateFromFilter = new JDateChooser(today);
        dateFromFilter.setDateFormatString("dd/MM/yyyy");
        dateFromFilter.setPreferredSize(new Dimension(120, 25));
        pnlSearch.add(dateFromFilter, gbc);
        
        // Hàng 3
        gbc.gridx = 0;
        gbc.gridy = 2;
        chkShowAvailableOnly = new JCheckBox("Chỉ hiển thị xe có sẵn");
        chkShowAvailableOnly.setSelected(true);
        pnlSearch.add(chkShowAvailableOnly, gbc);
        
        gbc.gridx = 2;
        pnlSearch.add(new JLabel("Đến ngày:"), gbc);
        
        gbc.gridx = 3;
        // Tạo ngày mai
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DATE, 1);
        Date tomorrow = c.getTime();
        
        dateToFilter = new JDateChooser(tomorrow);
        dateToFilter.setDateFormatString("dd/MM/yyyy");
        dateToFilter.setPreferredSize(new Dimension(120, 25));
        pnlSearch.add(dateToFilter, gbc);
        
        // Nút tìm kiếm
        gbc.gridx = 4;
        gbc.gridy = 1;
        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBackground(new Color(41, 121, 255));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        pnlSearch.add(btnSearch, gbc);
        
        // Thêm label hiển thị số xe đã chọn 
        gbc.gridx = 4;
        gbc.gridy = 2;
        // Cập nhật hiển thị số xe tối đa
        lblSelectedCount = new JLabel("Đã chọn: 0/" + MAX_CARS_ALLOWED + " xe");
        lblSelectedCount.setForeground(new Color(41, 121, 255));
        lblSelectedCount.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        pnlSearch.add(lblSelectedCount, gbc);
        
        // Panel bảng dữ liệu
        JPanel pnlTable = new JPanel(new BorderLayout(10, 10));
        pnlTable.setBorder(BorderFactory.createTitledBorder("Danh sách xe"));
        
        String[] columns = {"Chọn", "Mã xe", "Tên xe", "Biển số", "Hãng xe", "Số chỗ", "Giá thuê/ngày", "Từ ngày", "Đến ngày"};
        modelXe = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return Object.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 7 || column == 8;
            }
        };
        
        tblXe = new JTable(modelXe);
        tblXe.getTableHeader().setReorderingAllowed(false);
        
        // Set custom editor for date columns
        tblXe.getColumnModel().getColumn(7).setCellEditor(new DateChooserCellEditor());
        tblXe.getColumnModel().getColumn(8).setCellEditor(new DateChooserCellEditor());
        
        // Set custom renderer for date columns
        tblXe.getColumnModel().getColumn(7).setCellRenderer(new DateChooserCellRenderer());
        tblXe.getColumnModel().getColumn(8).setCellRenderer(new DateChooserCellRenderer());
        
        // Thêm listener cho checkbox
        tblXe.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 0) {
                updateSelectedCount();
                
                // Kiểm tra giới hạn số xe được chọn
                int selectedCount = countSelectedCars();
                if (selectedCount > MAX_CARS_ALLOWED) {
                    JOptionPane.showMessageDialog(this, 
                            "Bạn chỉ được chọn tối đa " + MAX_CARS_ALLOWED + " xe!", 
                            "Giới hạn chọn", 
                            JOptionPane.WARNING_MESSAGE);
                    
                    // Bỏ chọn dòng vừa được chọn
                    tblXe.setValueAt(Boolean.FALSE, e.getFirstRow(), 0);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblXe);
        pnlTable.add(scrollPane, BorderLayout.CENTER);
        
        // Panel nút
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Thêm vào hợp đồng");
        btnAdd.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        btnAdd.setBackground(new Color(41, 121, 255));           
        btnAdd.setForeground(Color.WHITE);   
        JButton btnCancel = new JButton("Hủy");
        btnCancel.setBackground(Color.GRAY);
        btnCancel.setForeground(Color.WHITE); 
        btnCancel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));

        pnlButtons.add(btnAdd);
        pnlButtons.add(btnCancel);
        
        // Layout tổng thể
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlMain.add(pnlSearch, BorderLayout.NORTH);
        pnlMain.add(pnlTable, BorderLayout.CENTER);
        
        add(pnlMain, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);
        
        // Sự kiện tìm kiếm
        btnSearch.addActionListener(e -> searchXe());
        
        // Thêm sự kiện khi thay đổi ngày trên bộ lọc
        dateFromFilter.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
                validateDateRangeFilter();
                if (dateFromFilter.getDate() != null) {
                    // Cập nhật ngày bắt đầu cho các dòng trong bảng
                    for (int i = 0; i < tblXe.getRowCount(); i++) {
                        tblXe.setValueAt(dateFromFilter.getDate(), i, 7);
                    }
                }
            }
        });
        
        dateToFilter.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
                validateDateRangeFilter();
                if (dateToFilter.getDate() != null) {
                    // Cập nhật ngày kết thúc cho các dòng trong bảng
                    for (int i = 0; i < tblXe.getRowCount(); i++) {
                        tblXe.setValueAt(dateToFilter.getDate(), i, 8);
                    }
                }
            }
        });
        
        // Sự kiện checkbox
        chkShowAvailableOnly.addActionListener(e -> searchXe());
        
        // Sự kiện nút thêm
        btnAdd.addActionListener(e -> addSelectedXe());
        
        // Sự kiện nút hủy
        btnCancel.addActionListener(e -> dispose());
    }
    
    // Phương thức đếm số xe đã chọn
    private int countSelectedCars() {
        int count = 0;
        for (int i = 0; i < tblXe.getRowCount(); i++) {
            Boolean selected = (Boolean) tblXe.getValueAt(i, 0);
            if (selected != null && selected) {
                count++;
            }
        }
        return count;
    }
    
    // Phương thức cập nhật số lượng xe đã chọn hiển thị
    private void updateSelectedCount() {
        int selectedCount = countSelectedCars();
        lblSelectedCount.setText("Đã chọn: " + selectedCount + "/" + MAX_CARS_ALLOWED + " xe");
        
        // Thay đổi màu nếu đạt giới hạn
        if (selectedCount >= MAX_CARS_ALLOWED) {
            lblSelectedCount.setForeground(Color.RED);
        } else {
            lblSelectedCount.setForeground(new Color(41, 121, 255));
        }
    }
    
    // Phương thức kiểm tra khoảng thời gian hợp lệ
    private void validateDateRangeFilter() {
        Date fromDate = dateFromFilter.getDate();
        Date toDate = dateToFilter.getDate();
        
        if (fromDate != null && toDate != null) {
            // Kiểm tra nếu ngày kết thúc trước ngày bắt đầu
            if (toDate.before(fromDate)) {
                JOptionPane.showMessageDialog(this, 
                    "Ngày kết thúc phải sau ngày bắt đầu!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                
                // Đặt lại ngày kết thúc là ngày mai của ngày bắt đầu
                Calendar c = Calendar.getInstance();
                c.setTime(fromDate);
                c.add(Calendar.DATE, 1);
                dateToFilter.setDate(c.getTime());
                return;
            }
            
            // Kiểm tra giới hạn 30 ngày
            long diffInMillies = Math.abs(toDate.getTime() - fromDate.getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            
            if (diffInDays > MAX_RENTAL_DAYS) {
                JOptionPane.showMessageDialog(this, 
                    "Thời gian thuê tối đa là " + MAX_RENTAL_DAYS + " ngày!", 
                    "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
                
                // Đặt lại ngày kết thúc là ngày bắt đầu + 30 ngày
                Calendar c = Calendar.getInstance();
                c.setTime(fromDate);
                c.add(Calendar.DATE, MAX_RENTAL_DAYS);
                dateToFilter.setDate(c.getTime());
            }
        }
    }
    
    // Custom cell editor using JDateChooser
    class DateChooserCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JDateChooser dateChooser;
        
        public DateChooserCellEditor() {
            dateChooser = new JDateChooser();
            dateChooser.setDateFormatString("dd/MM/yyyy");
            dateChooser.setDate(new Date()); // Default to today
            
            // Thêm sự kiện khi thay đổi ngày trong cell
            dateChooser.getDateEditor().addPropertyChangeListener(e -> {
                if ("date".equals(e.getPropertyName())) {
                    fireEditingStopped();
                }
            });
        }
        
        @Override
        public Object getCellEditorValue() {
            return dateChooser.getDate();
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, 
                boolean isSelected, int row, int column) {
            if (value instanceof Date) {
                dateChooser.setDate((Date) value);
            } else if (value instanceof String) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    dateChooser.setDate(sdf.parse((String) value));
                } catch (ParseException e) {
                    dateChooser.setDate(new Date());
                }
            } else {
                dateChooser.setDate(new Date());
            }
            return dateChooser;
        }
    }
    
    // Custom renderer for dates
    class DateChooserCellRenderer extends DefaultTableCellRenderer {
        private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Date) {
                value = dateFormat.format((Date) value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
    
    private void loadDataToTable() {
        // Khởi tạo với ngày từ các bộ lọc
        Date fromDate = dateFromFilter.getDate();
        Date toDate = dateToFilter.getDate();
        
        // Chỉ tải xe có sẵn nếu checkbox được chọn
        boolean onlyAvailable = chkShowAvailableOnly.isSelected();
        
        modelXe.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        List<Xe> danhSachXe;
        if (onlyAvailable) {
            // Lấy danh sách xe có sẵn trong khoảng thời gian
            danhSachXe = getAvailableCars(fromDate, toDate);
        } else {
            // Lấy tất cả xe
            danhSachXe = xeController.getAllXe();
        }
        
        // Thêm dữ liệu vào bảng
        for (Xe xe : danhSachXe) {
            modelXe.addRow(new Object[]{
                Boolean.FALSE,
                xe.getMaXe(),
                xe.getTenXe(),
                xe.getBienSo(),
                xe.getHangXe(),
                xe.getSoCho(),
                currencyFormat.format(xe.getGiaThueNgay()),
                fromDate,
                toDate
            });
        }
        
        // Thông báo nếu không có xe nào
        if (danhSachXe.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Không tìm thấy xe nào có sẵn trong khoảng thời gian đã chọn.", 
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Phương thức lấy xe có sẵn trong khoảng thời gian
    private List<Xe> getAvailableCars(Date fromDate, Date toDate) {
        List<Xe> availableCars = new ArrayList<>();
        List<Xe> allCars = xeController.getAllXe();
        
        for (Xe xe : allCars) {
            String checkResult = hopDongController.kiemTraXeThueDuoc(
                xe.getMaXe(), fromDate, toDate, maHDHienTai);
            
            if (checkResult == null) {
                // Nếu không có lỗi (null), xe có thể thuê được
                availableCars.add(xe);
            }
        }
        
        return availableCars;
    }
    
    private void searchXe() {
        // Giữ nguyên code tìm kiếm của bạn, chỉ thêm phần kiểm tra giới hạn ngày
        String keyword = txtSearch.getText().trim();
        String hangXe = cboHangXe.getSelectedItem().toString();
        String soCho = cboSoCho.getSelectedItem().toString();
        Date fromDate = dateFromFilter.getDate();
        Date toDate = dateToFilter.getDate();
        boolean onlyAvailable = chkShowAvailableOnly.isSelected();
        
        // Kiểm tra ngày có hợp lệ không
        if (fromDate == null || toDate == null) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn ngày bắt đầu và ngày kết thúc.", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra phạm vi ngày
        validateDateRangeFilter();
        
        fromDate = dateFromFilter.getDate(); // Lấy lại giá trị đã được điều chỉnh nếu có
        toDate = dateToFilter.getDate();
        
        // Xóa dữ liệu cũ
        modelXe.setRowCount(0);

        // Format để hiển thị số tiền
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // Lấy danh sách xe
        List<Xe> danhSachXe;
        if (onlyAvailable) {
            danhSachXe = getAvailableCars(fromDate, toDate);
        } else {
            danhSachXe = xeController.searchXe(keyword);
        }

        // Lọc thêm theo hãng xe và số chỗ
        for (Xe xe : danhSachXe) {
            boolean matchHangXe = hangXe.equals("Tất cả") || xe.getHangXe().equals(hangXe);
            boolean matchSoCho = soCho.equals("Tất cả") || String.valueOf(xe.getSoCho()).equals(soCho);
            boolean matchKeyword = keyword.isEmpty() || 
                                  xe.getTenXe().toLowerCase().contains(keyword.toLowerCase()) ||
                                  xe.getBienSo().toLowerCase().contains(keyword.toLowerCase());

            if (matchHangXe && matchSoCho && matchKeyword) {
                modelXe.addRow(new Object[]{
                    Boolean.FALSE,
                    xe.getMaXe(),
                    xe.getTenXe(),
                    xe.getBienSo(),
                    xe.getHangXe(),
                    xe.getSoCho(),
                    currencyFormat.format(xe.getGiaThueNgay()),
                    fromDate,  
                    toDate
                });
            }
        }
        
        // Cập nhật số lượng xe đã chọn
        updateSelectedCount();
        
        // Thông báo nếu không có kết quả
        if (modelXe.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Không tìm thấy xe phù hợp với điều kiện tìm kiếm.", 
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
   
    private void addSelectedXe() {
        // Kiểm tra có xe nào được chọn không
        boolean hasSelected = false;

        // Kiểm tra từng xe được chọn trước khi thêm
        StringBuilder errorMessages = new StringBuilder();
        boolean hasErrors = false;
        List<ChiTietHD> validCars = new ArrayList<>();

        for (int i = 0; i < tblXe.getRowCount(); i++) {
            Boolean selected = (Boolean) tblXe.getValueAt(i, 0);
            if (selected != null && selected) {
                hasSelected = true;

                try {
                    // Lấy thông tin ngày thuê
                    Date fromDate = (Date) tblXe.getValueAt(i, 7);
                    Date toDate = (Date) tblXe.getValueAt(i, 8);

                    if (toDate.before(fromDate)) {
                        JOptionPane.showMessageDialog(this, 
                                "Ngày kết thúc phải sau ngày bắt đầu ở xe: " + tblXe.getValueAt(i, 2).toString(), 
                                "Lỗi", 
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Kiểm tra giới hạn 30 ngày
                    long diffInMillies = Math.abs(toDate.getTime() - fromDate.getTime());
                    long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    
                    if (diffInDays > MAX_RENTAL_DAYS) {
                        errorMessages.append("- ").append(tblXe.getValueAt(i, 2)).append(": ")
                            .append("Thời gian thuê vượt quá " + MAX_RENTAL_DAYS + " ngày.\n");
                        hasErrors = true;
                        continue;
                    }

                    // Lấy thông tin xe
                    String maXe = tblXe.getValueAt(i, 1).toString();
                    String tenXe = tblXe.getValueAt(i, 2).toString();
                    String bienSo = tblXe.getValueAt(i, 3).toString();

                    // Kiểm tra xe có thể thuê được không (sử dụng logic hiện có)
                    String xeError = hopDongController.kiemTraXeThueDuoc(
                        maXe, fromDate, toDate, maHDHienTai);

                    if (xeError != null) {
                        hasErrors = true;
                        errorMessages.append("- ").append(tenXe).append(" (").append(bienSo).append("): ")
                                  .append(xeError).append("\n");
                        continue;
                    }

                    String hangXe = tblXe.getValueAt(i, 4).toString();
                    int soCho = Integer.parseInt(tblXe.getValueAt(i, 5).toString());

                    // Lấy giá thuê từ cột giá thuê/ngày
                    String giaThueStr = tblXe.getValueAt(i, 6).toString().replaceAll("[^\\d]", "");
                    double giaThueNgay = Double.parseDouble(giaThueStr);

                    // Tạo chi tiết hợp đồng
                    ChiTietHD ct = new ChiTietHD();
                    ct.setMaXe(maXe);
                    ct.setTenXe(tenXe);
                    ct.setBienSo(bienSo);
                    ct.setHangXe(hangXe);
                    ct.setSoCho(soCho);
                    ct.setGiaThueNgay(giaThueNgay);
                    ct.setNgayBatDau(fromDate);
                    ct.setNgayKetThuc(toDate);

                    validCars.add(ct);

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, 
                            "Lỗi khi xử lý giá thuê xe: " + tblXe.getValueAt(i, 2).toString(), 
                            "Lỗi", 
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        if (!hasSelected) {
            JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn ít nhất một xe!", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Kiểm tra số lượng xe - thay đổi thành 10 xe
        if (validCars.size() > MAX_CARS_ALLOWED) {
            JOptionPane.showMessageDialog(this, 
                "Bạn chỉ được chọn tối đa " + MAX_CARS_ALLOWED + " xe!", 
                "Giới hạn chọn", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Hiển thị thông báo lỗi nếu có
        if (hasErrors) {
            JOptionPane.showMessageDialog(this, 
                    "Không thể thêm các xe sau vào hợp đồng:\n" + errorMessages.toString(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);

            // Nếu không có xe nào hợp lệ thì không đóng dialog
            if (validCars.isEmpty()) {
                return;
            }
        }

        // Cập nhật danh sách xe đã chọn với các xe hợp lệ
        selectedXeList = validCars;
        dispose();
    }
    
    public List<ChiTietHD> getSelectedXeList() {
        return selectedXeList;
    }
}