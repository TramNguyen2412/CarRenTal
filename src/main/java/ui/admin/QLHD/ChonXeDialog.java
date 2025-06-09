//package ui.admin.QLHD;
//
//import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
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
//import javax.swing.table.DefaultTableModel;
//import javax.swing.text.MaskFormatter;
//import java.text.NumberFormat;
//import java.util.Locale;
//import controller.HopDongController;
//
//public class ChonXeDialog extends JDialog {
//    private XeController xeController;
//    private List<ChiTietHD> selectedXeList;
//    
//   private HopDongController hopDongController;
//   private String maHDHienTai; // Mã HD hiện tại (nếu đang sửa HD)
//   
//    // UI Components
//    private JTable tblXe;
//    private DefaultTableModel modelXe;
//    private JTextField txtSearch;
//    private JComboBox<String> cboHangXe;
//    private JComboBox<String> cboSoCho;
//    
////    public ChonXeDialog(Window owner) {
////        super(owner, "Chọn xe thuê", ModalityType.APPLICATION_MODAL);
////        this.xeController = new XeController();
////        this.selectedXeList = new ArrayList<>();
////        this.hopDongController = new HopDongController();
////        initComponents();
////        loadDataToTable();
////    }
////    
//    public ChonXeDialog(Window owner, String maHD) {
//        super(owner, "Chọn xe thuê", ModalityType.APPLICATION_MODAL);
//        this.hopDongController = new HopDongController();
//        this.maHDHienTai = maHD;
//         this.xeController = new XeController();
//       this.selectedXeList = new ArrayList<>();
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
//                return String.class;
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
//        // Tạo cell editor cho cột ngày
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        Date today = new Date();
//        Date tomorrow = new Date(today.getTime() + 24 * 60 * 60 * 1000);
//        
//        try {
//            // Editor cho cột Từ ngày (cột 7)
//            MaskFormatter dateMask = new MaskFormatter("##/##/####");
//            dateMask.setPlaceholderCharacter('_');
//            JFormattedTextField txtFromDate = new JFormattedTextField(dateMask);
//            txtFromDate.setText(dateFormat.format(today));
//            DefaultCellEditor fromDateEditor = new DefaultCellEditor(txtFromDate) {
//                @Override
//                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//                    if (value == null)
//                        value = dateFormat.format(today);
//                    return super.getTableCellEditorComponent(table, value, isSelected, row, column);
//                }
//            };
//            tblXe.getColumnModel().getColumn(7).setCellEditor(fromDateEditor);
//            
//            // Editor cho cột Đến ngày (cột 8)
//            JFormattedTextField txtToDate = new JFormattedTextField(dateMask);
//            txtToDate.setText(dateFormat.format(tomorrow));
//            DefaultCellEditor toDateEditor = new DefaultCellEditor(txtToDate) {
//                @Override
//                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//                    if (value == null)
//                        value = dateFormat.format(tomorrow);
//                    return super.getTableCellEditorComponent(table, value, isSelected, row, column);
//                }
//            };
//            tblXe.getColumnModel().getColumn(8).setCellEditor(toDateEditor);
//        
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
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
//    private void loadDataToTable() {
//        // Xóa dữ liệu cũ
//        modelXe.setRowCount(0);
//
//        // Format để hiển thị số tiền
//        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//        Date today = new Date();
//        Date tomorrow = new Date(today.getTime() + 24 * 60 * 60 * 1000);
//
//        // Lấy danh sách tất cả xe (không chỉ xe có trạng thái "Sẵn sàng")
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
//                dateFormat.format(today),
//                dateFormat.format(tomorrow)
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
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
//                    dateFormat.format(today),
//                    dateFormat.format(tomorrow)
//                });
//            }
//        }
//    }
//   
//    private void addSelectedXe() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
//                    // Lấy thông tin ngày thuê
//                    String fromDateStr = tblXe.getValueAt(i, 7).toString();
//                    String toDateStr = tblXe.getValueAt(i, 8).toString();
//
//                    Date fromDate = dateFormat.parse(fromDateStr);
//                    Date toDate = dateFormat.parse(toDateStr);
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
//                    // THÊM MỚI: Kiểm tra xe có thể thuê được không
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
//                    // Lấy giá thuê từ cột giá thuê/ngày (cần parse từ string định dạng tiền tệ)
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
//                } catch (ParseException e) {
//                    JOptionPane.showMessageDialog(this, 
//                            "Vui lòng nhập đúng định dạng ngày (dd/MM/yyyy) ở xe: " + tblXe.getValueAt(i, 2).toString(), 
//                            "Lỗi", 
//                            JOptionPane.ERROR_MESSAGE);
//                    return;
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
//    public List<ChiTietHD> getSelectedXeList() {
//        return selectedXeList;
//    }
//}

package ui.admin.QLHD;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.toedter.calendar.JDateChooser; // Add this import for JCalendar
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
import java.util.Locale;
import controller.HopDongController;

public class ChonXeDialog extends JDialog {
    private XeController xeController;
    private List<ChiTietHD> selectedXeList;
    
    private HopDongController hopDongController;
    private String maHDHienTai; // Mã HD hiện tại (nếu đang sửa HD)
   
    // UI Components
    private JTable tblXe;
    private DefaultTableModel modelXe;
    private JTextField txtSearch;
    private JComboBox<String> cboHangXe;
    private JComboBox<String> cboSoCho;
    
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
        setSize(1000, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));
       
        // Panel tìm kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlSearch.setBorder(BorderFactory.createTitledBorder("Tìm kiếm xe"));
        
        pnlSearch.add(new JLabel("Tên xe:"));
        txtSearch = new JTextField(15);
        pnlSearch.add(txtSearch);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchXe();
                }
            }
        });
        
        pnlSearch.add(new JLabel("Hãng xe:"));
        cboHangXe = new JComboBox<>(new String[]{"Tất cả", "Toyota", "Honda", "Hyundai", "KIA", "Mazda", "Ford", "Chevrolet", "Mercedes-Benz", "BMW", "Audi", "Lamborghini", "Roll Royce", "Khác"});
        pnlSearch.add(cboHangXe);
        
        pnlSearch.add(new JLabel("Số chỗ:"));
        cboSoCho = new JComboBox<>(new String[]{"Tất cả", "4", "5", "7", "8", "16"});
        pnlSearch.add(cboSoCho);
        
        JButton btnSearch = new JButton("Tìm kiếm");
        pnlSearch.add(btnSearch);
        
        // Panel bảng dữ liệu
        JPanel pnlTable = new JPanel(new BorderLayout(10, 10));
        pnlTable.setBorder(BorderFactory.createTitledBorder("Danh sách xe sẵn sàng"));
        
        String[] columns = {"Chọn", "Mã xe", "Tên xe", "Biển số", "Hãng xe", "Số chỗ", "Giá thuê/ngày", "Từ ngày", "Đến ngày"};
        modelXe = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return Object.class; // Changed to Object to handle Date objects
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
        
        JScrollPane scrollPane = new JScrollPane(tblXe);
        pnlTable.add(scrollPane, BorderLayout.CENTER);
        
        // Panel nút
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Thêm vào hợp đồng");
        btnAdd.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        btnAdd.setBackground(new Color(41, 121, 255));           
        btnAdd.setForeground(Color.WHITE);   
        JButton btnCancel = new JButton("Hủy");
        btnCancel.setBackground(Color.GRAY);            // Màu xám
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
        
        // Sự kiện nút thêm
        btnAdd.addActionListener(e -> addSelectedXe());
        
        // Sự kiện nút hủy
        btnCancel.addActionListener(e -> dispose());
    }
    
    // Custom cell editor using JDateChooser
    class DateChooserCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JDateChooser dateChooser;
        
        public DateChooserCellEditor() {
            dateChooser = new JDateChooser();
            dateChooser.setDateFormatString("dd/MM/yyyy");
            dateChooser.setDate(new Date()); // Default to today
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
        // Xóa dữ liệu cũ
        modelXe.setRowCount(0);

        // Format để hiển thị số tiền
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + 24 * 60 * 60 * 1000);

        // Lấy danh sách tất cả xe
        List<Xe> danhSachXe = xeController.getAllXe();

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
                today,  // Now using Date objects directly
                tomorrow
            });
        }
    }

    
    private void searchXe() {
        String keyword = txtSearch.getText().trim();
        String hangXe = cboHangXe.getSelectedItem().toString();
        String soCho = cboSoCho.getSelectedItem().toString();

        // Xóa dữ liệu cũ
        modelXe.setRowCount(0);

        // Format để hiển thị số tiền
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + 24 * 60 * 60 * 1000);

        // Sử dụng phương thức searchXe() đã có để tìm kiếm
        List<Xe> danhSachXe = xeController.searchXe(keyword);

        // Lọc thêm theo hãng xe và số chỗ
        for (Xe xe : danhSachXe) {
            boolean matchHangXe = hangXe.equals("Tất cả") || xe.getHangXe().equals(hangXe);
            boolean matchSoCho = soCho.equals("Tất cả") || String.valueOf(xe.getSoCho()).equals(soCho);

            if (matchHangXe && matchSoCho) {
                modelXe.addRow(new Object[]{
                    Boolean.FALSE,
                    xe.getMaXe(),
                    xe.getTenXe(),
                    xe.getBienSo(),
                    xe.getHangXe(),
                    xe.getSoCho(),
                    currencyFormat.format(xe.getGiaThueNgay()),
                    today,  // Now using Date objects directly
                    tomorrow
                });
            }
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
            if (selected) {
                hasSelected = true;

                try {
                    // Lấy thông tin ngày thuê - now directly as Date objects
                    Date fromDate = (Date) tblXe.getValueAt(i, 7);
                    Date toDate = (Date) tblXe.getValueAt(i, 8);

                    if (toDate.before(fromDate)) {
                        JOptionPane.showMessageDialog(this, 
                                "Ngày kết thúc phải sau ngày bắt đầu ở xe: " + tblXe.getValueAt(i, 2).toString(), 
                                "Lỗi", 
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Lấy thông tin xe
                    String maXe = tblXe.getValueAt(i, 1).toString();
                    String tenXe = tblXe.getValueAt(i, 2).toString();
                    String bienSo = tblXe.getValueAt(i, 3).toString();

                    // Kiểm tra xe có thể thuê được không
                    String xeError = hopDongController.kiemTraXeThueDuoc(
                        maXe, fromDate, toDate, maHDHienTai);

                    if (xeError != null) {
                        hasErrors = true;
                        errorMessages.append("- ").append(tenXe).append(" (").append(bienSo).append("): ")
                                  .append(xeError).append("\n");
                        continue; // Bỏ qua xe này, kiểm tra xe khác
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