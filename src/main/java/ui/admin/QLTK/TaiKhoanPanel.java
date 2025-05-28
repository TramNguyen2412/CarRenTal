package ui.admin.QLTK;

import controller.TaiKhoanController;
import model.TaiKhoan;
import model.TaiKhoanExtended;
import ui.admin.QLTK.ButtonEditor;
import ui.admin.QLTK.ButtonRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class TaiKhoanPanel extends JPanel {
    private TaiKhoanController controller;
    private JTable tblTaiKhoan;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cboFilter;
    private JButton btnAdd, btnRefresh;
    
    // Cột của bảng
    private final String[] COLUMNS = {
        "Mã TK", "Tên đăng nhập", "Vai trò", "Trạng thái", "Loại người dùng", "Tên người dùng", "Thao tác"
    };
    
    public TaiKhoanPanel() {
        controller = new TaiKhoanController();
        initComponents();
        loadDataToTable();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // 1. Panel tiêu đề và tìm kiếm
        JPanel pnlTitle = new JPanel(new BorderLayout());
        pnlTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel lblTitle = new JLabel("QUẢN LÝ TÀI KHOẢN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        pnlTitle.add(lblTitle, BorderLayout.WEST);
        
        // Panel tìm kiếm và lọc
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(200, 30));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchTaiKhoan();
            }
        });
        
        cboFilter = new JComboBox<>(new String[]{"Tất cả", "Hoạt động", "Không hoạt động"});
        cboFilter.setPreferredSize(new Dimension(150, 30));
        cboFilter.addActionListener(e -> filterTaiKhoan());
        
        btnRefresh = createStyledButton("Làm mới", new Color(0, 150, 136));
        btnRefresh.addActionListener(e -> {
            loadDataToTable();
            txtSearch.setText("");
            cboFilter.setSelectedIndex(0);
        });
        
        pnlSearch.add(new JLabel("Tìm kiếm:"));
        pnlSearch.add(txtSearch);
        pnlSearch.add(new JLabel("Lọc:"));
        pnlSearch.add(cboFilter);
        pnlSearch.add(btnRefresh);
        
        pnlTitle.add(pnlSearch, BorderLayout.EAST);
        
        add(pnlTitle, BorderLayout.NORTH);
        
        // 2. Bảng tài khoản
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Chỉ cho phép chỉnh sửa cột "Thao tác"
            }
        };
        
        tblTaiKhoan = new JTable(tableModel);
        tblTaiKhoan.setRowHeight(40);
        tblTaiKhoan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblTaiKhoan.setShowGrid(true);
        tblTaiKhoan.setGridColor(new Color(230, 230, 230));
        tblTaiKhoan.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Thiết lập độ rộng cột
        tblTaiKhoan.getColumnModel().getColumn(0).setPreferredWidth(60);   // Mã TK
        tblTaiKhoan.getColumnModel().getColumn(1).setPreferredWidth(150);  // Tên đăng nhập
        tblTaiKhoan.getColumnModel().getColumn(2).setPreferredWidth(100);  // Vai trò
        tblTaiKhoan.getColumnModel().getColumn(3).setPreferredWidth(100);  // Trạng thái
        tblTaiKhoan.getColumnModel().getColumn(4).setPreferredWidth(100);  // Loại người dùng
        tblTaiKhoan.getColumnModel().getColumn(5).setPreferredWidth(150);  // Tên người dùng
        tblTaiKhoan.getColumnModel().getColumn(6).setPreferredWidth(150);  // Thao tác
        
        // Custom renderer cho cột thao tác
        tblTaiKhoan.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        tblTaiKhoan.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(this));
        
        // Tùy chỉnh header bảng
        JTableHeader header = tblTaiKhoan.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(60, 60, 60));
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        // Tùy chỉnh renderer cho bảng
        tblTaiKhoan.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Thêm padding cho text trong ô
                if (comp instanceof JLabel) {
                    ((JLabel) comp).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                }

                // Màu nền dòng chẵn/lẻ nếu không được chọn và không phải cột thao tác
                if (!isSelected && column != 6) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    comp.setForeground(new Color(50, 50, 50));
                }
                
                // Màu cho trạng thái
                if (column == 3) {
                    String status = value.toString();
                    if ("Hoạt động".equals(status)) {
                        setForeground(new Color(46, 125, 50)); // Màu xanh cho hoạt động
                    } else {
                        setForeground(new Color(211, 47, 47)); // Màu đỏ cho không hoạt động
                    }
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else if (column == 4) {
                    // Màu cho loại user
                    String userType = value.toString();
                    if ("NV".equals(userType)) {
                        setForeground(new Color(25, 118, 210)); // Màu xanh dương cho nhân viên
                    } else if ("KH".equals(userType)) {
                        setForeground(new Color(230, 74, 25)); // Màu cam cho khách hàng
                    } else {
                        setForeground(new Color(117, 117, 117)); // Màu xám cho chưa liên kết
                    }
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                    setForeground(new Color(50, 50, 50));
                }

                return comp;
            }
        });
        
        // Thêm bảng vào scroll pane
        JScrollPane scrollPane = new JScrollPane(tblTaiKhoan);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.setBackground(Color.WHITE);
        
        add(scrollPane, BorderLayout.CENTER);
        
        // 3. Panel nút thêm tài khoản
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAdd = createStyledButton("Thêm tài khoản", new Color(41, 121, 255));
        btnAdd.addActionListener(e -> showTaiKhoanDialog(null));
        pnlBottom.add(btnAdd);
        
        add(pnlBottom, BorderLayout.SOUTH);
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 35));
        return button;
    }
    
    public void loadDataToTable() {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ

        List<TaiKhoanExtended> dsTaiKhoan = controller.getAllTaiKhoanExtended();

        for (TaiKhoanExtended tk : dsTaiKhoan) {
            tableModel.addRow(new Object[]{
                tk.getMaTK(),
                tk.getTenDangNhap(),
                tk.getTenVaiTro(),  // Hiển thị tên vai trò thay vì mã vai trò
                tk.getTrangThai(),
                tk.getLoaiNguoiDung(),
                tk.getTenNguoiDung(),
                "" // Cột thao tác
            });
        }
    }
    
    private void searchTaiKhoan() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tblTaiKhoan.setRowSorter(sorter);
        
        if (keyword.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
        }
    }
    
    private void filterTaiKhoan() {
        int selectedIndex = cboFilter.getSelectedIndex();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tblTaiKhoan.setRowSorter(sorter);
        
        if (selectedIndex == 0) {
            sorter.setRowFilter(null);
        } else {
            String trangThai = selectedIndex == 1 ? "Hoạt động" : "Không hoạt động";
            sorter.setRowFilter(RowFilter.regexFilter("^" + trangThai + "$", 3));
        }
    }
    
    public void showTaiKhoanDialog(TaiKhoanExtended taiKhoan) {
        TaiKhoanDialog dialog = new TaiKhoanDialog(SwingUtilities.getWindowAncestor(this), taiKhoan, this);
        dialog.setVisible(true);
    }
    
    public TaiKhoanExtended getTaiKhoanById(String id) {
        return (TaiKhoanExtended) controller.getTaiKhoanById(id);
    }
    
    public boolean deleteTaiKhoan(String id) {
        return controller.deleteTaiKhoan(id);
    }
}