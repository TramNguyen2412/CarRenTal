
package ui.admin.QLHD;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import controller.HopDongController;
import model.HopDong;

public class HopDongPanel extends JPanel {
    private JTable tblHopDong;
    private DefaultTableModel modelHopDong;
    private JTextField txtSearch;
    private JComboBox<String> cboTrangThai;
    private JButton btnThem, btnSua, btnXoa, btnXemChiTiet, btnRefresh;
    
    private HopDongController hopDongController;
    
    public HopDongPanel() {
        this.hopDongController = new HopDongController();
        
        initComponents();
        loadDataToTable();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Panel tiêu đề
        JPanel pnlTitle = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ HỢP ĐỒNG");
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        pnlTitle.add(lblTitle, BorderLayout.WEST);
        
        // Panel tìm kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(200, 30));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        pnlSearch.add(new JLabel("Tìm kiếm:"));
        pnlSearch.add(txtSearch);
        
        pnlSearch.add(new JLabel("Trạng thái:"));
        cboTrangThai = new JComboBox<>(new String[]{
            "Tất cả", "Chờ xác nhận", "Đã xác nhận", "Đang thuê", "Vi phạm", "Đã hủy", "Hoàn thành"
        });
        cboTrangThai.setPreferredSize(new Dimension(150, 30));
        pnlSearch.add(cboTrangThai);
        
        JButton btnSearch = new JButton("Tìm");
        styleButton(btnSearch, new Color(41, 121, 255));
        pnlSearch.add(btnSearch);
        
        pnlTitle.add(pnlSearch, BorderLayout.EAST);
        add(pnlTitle, BorderLayout.NORTH);
        
        // Panel bảng dữ liệu
        String[] columns = {"Mã HĐ", "Khách hàng", "Ngày lập", "Tổng tiền", "Trạng thái"};
        modelHopDong = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblHopDong = new JTable(modelHopDong);
        tblHopDong.setRowHeight(40);
        tblHopDong.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblHopDong.setShowGrid(true);
        tblHopDong.setGridColor(new Color(230, 230, 230));
        tblHopDong.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14)); // Tăng font size
        
        // Tùy chỉnh header bảng
        JTableHeader header = tblHopDong.getTableHeader();
        header.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14)); // Tăng font size
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(60, 60, 60));
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        
        // Tùy chỉnh renderer cho bảng
        tblHopDong.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Thêm padding cho text trong ô
                if (comp instanceof JLabel) {
                    ((JLabel) comp).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                }

                // Màu nền dòng chẵn/lẻ nếu không được chọn
                if (!isSelected) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    comp.setForeground(new Color(50, 50, 50));
                }

                // Căn phải cho cột tổng tiền
                if (column == 3) { // Tổng tiền
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                return comp;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblHopDong);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel nút chức năng
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnThem = new JButton("Thêm hợp đồng");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnXemChiTiet = new JButton("Xem chi tiết");
        btnRefresh = new JButton("Làm mới");
        
        styleButton(btnThem, new Color(41, 121, 255));
        styleButton(btnSua, new Color(0, 150, 136));
        styleButton(btnXoa, new Color(211, 47, 47));
        styleButton(btnXemChiTiet, new Color(33, 150, 243));
        styleButton(btnRefresh, new Color(96, 125, 139));
        
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnXemChiTiet);
        pnlButtons.add(btnRefresh);
        
        add(pnlButtons, BorderLayout.SOUTH);
        
        // Xử lý sự kiện
        btnSearch.addActionListener(e -> searchHopDong());
        btnThem.addActionListener(e -> showHopDongDialog(null));
        btnSua.addActionListener(e -> editSelectedHopDong());
        btnXoa.addActionListener(e -> deleteSelectedHopDong());
        btnXemChiTiet.addActionListener(e -> viewSelectedHopDong());
        btnRefresh.addActionListener(e -> loadDataToTable());
        
        // Thêm sự kiện khi nhấn Enter trong ô tìm kiếm
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchHopDong();
                }
            }
        });
        
        // Thêm sự kiện khi thay đổi trạng thái
        cboTrangThai.addActionListener(e -> searchHopDong());
        
        // Xử lý sự kiện double click vào hàng
        tblHopDong.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewSelectedHopDong();
                }
            }
        });
        
        // Hiệu ứng khi chọn hàng
        tblHopDong.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = tblHopDong.getSelectedRow() != -1;
            btnSua.setEnabled(hasSelection);
            btnXoa.setEnabled(hasSelection);
            btnXemChiTiet.setEnabled(hasSelection);
        });
        
        // Ban đầu các nút sẽ disable
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        btnXemChiTiet.setEnabled(false);
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE); // Màu chữ là trắng
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 35));
    }
    
    // Load dữ liệu vào bảng
    public void loadDataToTable() {
        try {
            // Xóa dữ liệu cũ
            modelHopDong.setRowCount(0);
            
            // Format để hiển thị ngày và số tiền
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            
            // Lấy danh sách hợp đồng
            List<HopDong> danhSachHD = hopDongController.getAllHopDong();
            
            // Debug kiểm tra dữ liệu
            System.out.println("Số lượng hợp đồng lấy được: " + (danhSachHD != null ? danhSachHD.size() : 0));
            
            // Thêm dữ liệu vào bảng
            if (danhSachHD != null && !danhSachHD.isEmpty()) {
                for (HopDong hd : danhSachHD) {
                    // Debug: In thông tin hợp đồng
                    System.out.println("Mã HĐ: " + hd.getMaHD() + ", Tên KH: " + hd.getTenKH() + 
                                       ", Ngày: " + (hd.getNgayLap() != null ? dateFormat.format(hd.getNgayLap()) : "null") + 
                                       ", Tiền: " + hd.getTongTien());
                    
                    modelHopDong.addRow(new Object[]{
                        hd.getMaHD(),
                        hd.getTenKH() != null ? hd.getTenKH() : "Không xác định",
                        hd.getNgayLap() != null ? dateFormat.format(hd.getNgayLap()) : "",
                        currencyFormat.format(hd.getTongTien()),
                        hd.getTrangThai()
                    });
                }
                System.out.println("Đã thêm " + modelHopDong.getRowCount() + " dòng vào bảng");
            } else {
                System.out.println("Không có dữ liệu hợp đồng để hiển thị");
            }
            
            // Cập nhật UI
            tblHopDong.repaint();
            tblHopDong.revalidate();
            
        } catch (Exception e) {
            System.err.println("Lỗi khi load dữ liệu vào bảng: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu hợp đồng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Tìm kiếm hợp đồng
    private void searchHopDong() {
        try {
            String keyword = txtSearch.getText().trim();
            String trangThai = cboTrangThai.getSelectedItem().toString();
            
            // Xóa dữ liệu cũ
            modelHopDong.setRowCount(0);
            
            // Format để hiển thị ngày và số tiền
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            
            // Lấy danh sách hợp đồng
            List<HopDong> danhSachHD;
            
            // Nếu không có từ khóa và trạng thái là "Tất cả" thì lấy tất cả hợp đồng
            if (keyword.isEmpty() && trangThai.equals("Tất cả")) {
                danhSachHD = hopDongController.getAllHopDong();
            } else {
                danhSachHD = hopDongController.searchHopDong(keyword, trangThai);
            }
            
            // Debug
            System.out.println("Tìm kiếm với từ khóa: '" + keyword + "', trạng thái: '" + trangThai + "'");
            System.out.println("Kết quả: " + (danhSachHD != null ? danhSachHD.size() : 0) + " hợp đồng");
            
            // Thêm dữ liệu vào bảng
            if (danhSachHD != null && !danhSachHD.isEmpty()) {
                for (HopDong hd : danhSachHD) {
                    modelHopDong.addRow(new Object[]{
                        hd.getMaHD(),
                        hd.getTenKH() != null ? hd.getTenKH() : "Không xác định",
                        hd.getNgayLap() != null ? dateFormat.format(hd.getNgayLap()) : "",
                        currencyFormat.format(hd.getTongTien()),
                        hd.getTrangThai()
                    });
                }
                System.out.println("Đã thêm " + modelHopDong.getRowCount() + " dòng vào bảng");
            } else {
                System.out.println("Không tìm thấy hợp đồng nào phù hợp");
            }
            
            // Cập nhật UI
            tblHopDong.repaint();
            tblHopDong.revalidate();
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm kiếm hợp đồng: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tìm kiếm hợp đồng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Hiển thị dialog thêm/sửa hợp đồng
    private void showHopDongDialog(HopDong hopDong) {
        try {
            HopDongDialog dialog = new HopDongDialog(SwingUtilities.getWindowAncestor(this), hopDong, this);
            dialog.setVisible(true);
        } catch (Exception e) {
            System.err.println("Lỗi khi hiển thị dialog hợp đồng: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi hiển thị dialog hợp đồng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Sửa hợp đồng đã chọn
    private void editSelectedHopDong() {
        try {
            int selectedRow = tblHopDong.getSelectedRow();
            if (selectedRow >= 0) {
                String maHD = tblHopDong.getValueAt(selectedRow, 0).toString();
                
                // Debug
                System.out.println("Đang sửa hợp đồng: " + maHD);
                
                // Lấy hợp đồng từ controller
                HopDong hopDong = hopDongController.getHopDongByMa(maHD);
                
                if (hopDong != null) {
                    // Kiểm tra trạng thái hợp đồng
                   if ("Vi phạm".equals(hopDong.getTrangThai()) || 
                        "Đã hủy".equals(hopDong.getTrangThai()) ||
                        "Hoàn thành".equals(hopDong.getTrangThai())) {
                        JOptionPane.showMessageDialog(this,
                                "Không thể sửa hợp đồng vi phạm, đã hủy hoặc đã hoàn thành!",
                                "Thông báo",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    showHopDongDialog(hopDong);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không tìm thấy thông tin hợp đồng với mã: " + maHD,
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi sửa hợp đồng: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi sửa hợp đồng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Xem chi tiết hợp đồng đã chọn
   // Xem chi tiết hợp đồng đã chọn
    private void viewSelectedHopDong() {
        try {
            int selectedRow = tblHopDong.getSelectedRow();
            if (selectedRow >= 0) {
                String maHD = tblHopDong.getValueAt(selectedRow, 0).toString();

                // Debug
                System.out.println("Đang xem chi tiết hợp đồng: " + maHD);

                // Sử dụng dialog mới XemChiTietHopDongDialog thay vì HopDongDialog
                XemChiTietHDDialog dialog = new XemChiTietHDDialog(
                    SwingUtilities.getWindowAncestor(this), maHD);
                dialog.setVisible(true);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xem chi tiết hợp đồng: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi xem chi tiết hợp đồng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Xóa hợp đồng đã chọn KIỂM TRA LẠI CHO CHẮC ĂN
    
    private void deleteSelectedHopDong() {
        try {
            int selectedRow = tblHopDong.getSelectedRow();
            if (selectedRow >= 0) {
                String maHD = tblHopDong.getValueAt(selectedRow, 0).toString();
                String trangThai = tblHopDong.getValueAt(selectedRow, 4).toString();
                
                // Debug
                System.out.println("Đang xóa hợp đồng: " + maHD + ", trạng thái: " + trangThai);
                
                // Kiểm tra trạng thái
                if ("Đang thuê".equals(trangThai)) {
                    JOptionPane.showMessageDialog(this,
                            "Không thể xóa hợp đồng đang trong trạng thái 'Đang thuê'!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Xác nhận xóa
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc muốn xóa hợp đồng này?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = hopDongController.deleteHopDong(maHD);
                    
                    if (success) {
                        JOptionPane.showMessageDialog(this,
                                "Xóa hợp đồng thành công!",
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                        loadDataToTable();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Xóa hợp đồng thất bại! " + hopDongController.getErrorMessage(),
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa hợp đồng: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi xóa hợp đồng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}