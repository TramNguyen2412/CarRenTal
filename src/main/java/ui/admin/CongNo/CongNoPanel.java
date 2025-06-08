package ui.admin.CongNo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import controller.CongNoController;
import controller.KhachHangController;
import java.sql.Connection;
import java.util.Date;
import javax.swing.JComboBox;
import model.HopDong;
import model.KhachHang;
import model.LichSuCongNo;
import model.PhieuBaoDuong;

public class CongNoPanel extends JPanel {
    private JTable tableCongNo;
    private DefaultTableModel modelCongNo;
    private JTable tableKhachHang;
    private DefaultTableModel modelKhachHang;
    private JTextField txtSearch;
    private JButton btnAdd, btnRefresh, btnExport;
    private CongNoController congNoController;
    private KhachHangController khachHangController;
    private JButton btnThemChiTietTongNo;
    private DecimalFormat currencyFormat;
    private SimpleDateFormat dateFormat;
    private controller.HopDongController hopDongController = new controller.HopDongController();
    private controller.BaoDuongController baoDuongController = new controller.BaoDuongController();
    // Cột của bảng công nợ
    private final String[] CONG_NO_COLUMNS = {
        "Mã LS", "Mã Khách Hàng", "Ngày GD", "Loại GD", "Số Tiền", "Ghi Chú", 
    };

    // Cột của bảng khách hàng
    private final String[] KHACH_HANG_COLUMNS = {
        "Mã KH", "Họ Tên", "Tổng Nợ"
    };
    // ...existing code...
    private List<LichSuCongNo> snapshotCongNo = null;
    private List<KhachHang> snapshotKhachHang = null;

    private List<PhieuBaoDuong> snapshotAllPBD = null;
    private List<HopDong> snapshotAllHopDong = null;
// ...existing code...
    // ...existing code...
    // ...existing code...

    public CongNoPanel() {
        congNoController = new CongNoController();
        khachHangController = new KhachHangController();
        
        currencyFormat = new DecimalFormat("#,###");
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        initComponents();
        loadCongNoData();
        loadKhachHangData();
    }

    private void initComponents() {
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Panel tiêu đề
        JPanel pnlTitle = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ CÔNG NỢ");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        pnlTitle.add(lblTitle, BorderLayout.WEST);
        
        // Panel tìm kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtSearch = new JTextField(10);
        btnRefresh = new JButton("Làm mới");
        btnExport = new JButton("Xuất Excel");
        
        pnlSearch.add(new JLabel("Tìm kiếm:"));
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnRefresh);
        pnlSearch.add(btnExport);
        
        //Đoạn thêm mới
        // ==== THÊM ISOLATION LEVEL CHO TEST PHANTOM ==== //
        JLabel lblIsolation = new JLabel("Isolation Level:");
        lblIsolation.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlSearch.add(lblIsolation);

        JComboBox<String> cboIsolation = new JComboBox<>(new String[]{"READ_COMMITTED", "SERIALIZABLE"});
        cboIsolation.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlSearch.add(cboIsolation);

        JButton btnApplyIsolation = new JButton("Áp dụng");
        btnApplyIsolation.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnApplyIsolation.setBackground(new Color(0, 150, 136));
        btnApplyIsolation.setForeground(Color.WHITE);
        btnApplyIsolation.addActionListener(e -> {
            String level = (String) cboIsolation.getSelectedItem();
            applyIsolationLevel(level);
        });
        pnlSearch.add(btnApplyIsolation);
        // ==== HẾT ISOLATION LEVEL ==== //
        pnlTitle.add(pnlSearch, BorderLayout.EAST);
        add(pnlTitle, BorderLayout.NORTH);
        
        // Panel thêm giao dịch
        btnAdd = new JButton("Thêm giao dịch");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xoá");
        styleButton(btnAdd, new Color(41, 121, 255));
        styleButton(btnEdit, new Color(255, 193, 7));
        styleButton(btnDelete, new Color(220, 53, 69));

        
        // Panel chính chứa 2 bảng
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.7); // 70% cho bảng công nợ, 30% cho bảng khách hàng
        
        // Bảng lịch sử công nợ
        modelCongNo = new DefaultTableModel(CONG_NO_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Chỉ cho phép chỉnh sửa cột "Thao tác"
            }
        };
        
        tableCongNo = new JTable(modelCongNo);
        tableCongNo.setRowHeight(40); // Chiều cao vừa phải
     
        // Thiết lập độ rộng cột
        TableColumnModel columnModel = tableCongNo.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);  // Mã LS
        columnModel.getColumn(1).setPreferredWidth(150); // Khách hàng
        columnModel.getColumn(2).setPreferredWidth(100); // Ngày GD
        columnModel.getColumn(3).setPreferredWidth(100); // Loại GD
        columnModel.getColumn(4).setPreferredWidth(120); // Số tiền
        columnModel.getColumn(5).setPreferredWidth(200); // Ghi chú
       
        
        // Tùy chỉnh header
        JTableHeader headerCongNo = tableCongNo.getTableHeader();
        headerCongNo.setFont(new Font("Arial", Font.BOLD, 14));
        headerCongNo.setBackground(new Color(240, 240, 240));
        headerCongNo.setForeground(new Color(60, 60, 60));
        headerCongNo.setPreferredSize(new Dimension(0, 40));
        headerCongNo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        // Tùy chỉnh grid lines
        tableCongNo.setShowGrid(true);
        tableCongNo.setGridColor(new Color(230, 230, 230));

        // Tạo hiệu ứng dòng sọc
        tableCongNo.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

                // Căn phải cho cột số tiền
                if (column == 4) { // Số tiền
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                return comp;
            }
        });
        
        JScrollPane scrollPaneCongNo = new JScrollPane(tableCongNo);
        
        // Bảng khách hàng có công nợ
        modelKhachHang = new DefaultTableModel(KHACH_HANG_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableKhachHang = new JTable(modelKhachHang);
        tableKhachHang.setRowHeight(40);
        
        // Thiết lập độ rộng cột
        TableColumnModel columnModelKH = tableKhachHang.getColumnModel();
        columnModelKH.getColumn(0).setPreferredWidth(60);  // Mã KH
        columnModelKH.getColumn(1).setPreferredWidth(200); // Họ tên
        columnModelKH.getColumn(2).setPreferredWidth(120); // Tổng nợ
        
        // Tùy chỉnh header
        JTableHeader headerKH = tableKhachHang.getTableHeader();
        headerKH.setFont(new Font("Arial", Font.BOLD, 14));
        headerKH.setBackground(new Color(240, 240, 240));
        headerKH.setForeground(new Color(60, 60, 60));
        headerKH.setPreferredSize(new Dimension(0, 40));
        headerKH.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        
        // Tùy chỉnh grid lines
        tableKhachHang.setShowGrid(true);
        tableKhachHang.setGridColor(new Color(230, 230, 230));
        
        // Tạo hiệu ứng dòng sọc
        tableKhachHang.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

                // Căn phải cho cột tổng nợ
                if (column == 2) { // Tổng nợ
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                return comp;
            }
        });
        
        JScrollPane scrollPaneKH = new JScrollPane(tableKhachHang);
        
        // Thêm tiêu đề cho bảng khách hàng
        JPanel khachHangPanel = new JPanel(new BorderLayout());
        JLabel lblKhachHang = new JLabel("KHÁCH HÀNG CÓ CÔNG NỢ");
        lblKhachHang.setFont(new Font("Arial", Font.BOLD, 16));
        lblKhachHang.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        khachHangPanel.add(lblKhachHang, BorderLayout.CENTER);
        khachHangPanel.add(scrollPaneKH, BorderLayout.CENTER);
        
        btnThemChiTietTongNo = new JButton("Xem chi tiết tổng nợ");
        styleButton(btnThemChiTietTongNo, new Color(255, 152, 0));
        JPanel pnlAdd1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlAdd1.add(btnAdd);
        pnlAdd1.add(btnEdit);
        pnlAdd1.add(btnDelete);
        pnlAdd1.add(btnThemChiTietTongNo);
        add(pnlAdd1, BorderLayout.SOUTH);
        btnThemChiTietTongNo.addActionListener(e -> showTongNoDetailDialog());
        
        // Thêm các bảng vào split pane
        splitPane.setTopComponent(scrollPaneCongNo);
        splitPane.setBottomComponent(khachHangPanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Làm đẹp các nút
        styleButton(btnAdd, new Color(41, 121, 255));
        styleButton(btnRefresh, new Color(0, 150, 136));
        styleButton(btnExport, new Color(113, 85, 156));

        // Làm đẹp thanh tìm kiếm
        txtSearch.setPreferredSize(new Dimension(200, 30));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Thêm sự kiện
        btnAdd.addActionListener(e -> showCongNoDialog(null)); // Thêm mới

        btnEdit.addActionListener(e -> {
            int row = tableCongNo.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một giao dịch công nợ để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String maLS = tableCongNo.getValueAt(row, 0).toString();
            LichSuCongNo ls = congNoController.getLichSuCongNoByMa(maLS);
            if (ls != null) {
                showCapNhatCongNoDialog(ls);
            }
        });

        btnDelete.addActionListener(e -> {
            int row = tableCongNo.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một giao dịch công nợ để xoá!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String maLS = tableCongNo.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xoá giao dịch công nợ này?", "Xác nhận xoá", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String result = congNoController.deleteLichSuCongNo(maLS);
                JOptionPane.showMessageDialog(this, result);
                loadCongNoData();
                loadKhachHangData();
            }
        });
        // ...existing code...
            btnRefresh.addActionListener(e -> {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                    int iso = congNoController.getIsolationLevel();
                    if (iso == Connection.TRANSACTION_SERIALIZABLE) {
                        if (snapshotCongNo == null || snapshotKhachHang == null
                            || snapshotAllPBD == null || snapshotAllHopDong == null) {
                            snapshotCongNo = congNoController.getAllLichSuCongNo();
                            snapshotKhachHang = congNoController.getKhachHangCoCongNo();
                            snapshotAllPBD = baoDuongController.getAllPhieuBaoDuong();
                            snapshotAllHopDong = hopDongController.getAllHopDong();
                        }
                        loadCongNoDataNon(snapshotCongNo);
                        loadKhachHangDataNon(snapshotKhachHang);
                    } else {
                        snapshotCongNo = congNoController.getAllLichSuCongNo();
                        snapshotKhachHang = congNoController.getKhachHangCoCongNo();
                        snapshotAllPBD = baoDuongController.getAllPhieuBaoDuong();
                        snapshotAllHopDong = hopDongController.getAllHopDong();
                        loadCongNoDataNon(snapshotCongNo);
                        loadKhachHangDataNon(snapshotKhachHang);
                        String msg = "Đã làm mới dữ liệu với isolation level: READ_COMMITTED (có thể thấy giao dịch mới thêm vào)";
                        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi làm mới dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            });
        // ...existing code...
        btnExport.addActionListener(e -> exportToExcel());
        
        txtSearch.addActionListener(e -> searchCongNo());
        
        // Thêm sự kiện cho bảng khách hàng
        tableKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tableKhachHang.getSelectedRow();
                if (row >= 0) {
                    String maKH = tableKhachHang.getValueAt(row, 0).toString();
                    filterCongNoByKhachHang(maKH);
                }
            }
        });
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
    }
    
    public void loadCongNoData() {
        modelCongNo.setRowCount(0); // Xóa dữ liệu cũ
        
        List<LichSuCongNo> danhSachCongNo = congNoController.getAllLichSuCongNo();
        for (LichSuCongNo ls : danhSachCongNo) {
            // Lấy thông tin khách hàng để hiển thị mã + tên
            KhachHang kh = khachHangController.getKhachHangByMa(ls.getMaKH());
            String maVaTenKH = kh != null ? (kh.getMaKH() + " - " + kh.getHoTen()) : ls.getMaKH();
            modelCongNo.addRow(new Object[]{
                ls.getMaLichSu(),
                maVaTenKH, // Hiển thị mã + tên khách hàng
                dateFormat.format(ls.getNgayGiaoDich()),
                ls.getLoaiGiaoDich(),
                currencyFormat.format(ls.getSoTien()) + " VNĐ",
                ls.getGhiChu(),
                "" // Cột thao tác
            });
        }
    }
    
    public void showTongNoDetailDialog() {
        int selectedRow = tableKhachHang.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng để xem chi tiết tổng nợ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maKH = tableKhachHang.getValueAt(selectedRow, 0).toString();

        int iso = congNoController.getIsolationLevel();
        KhachHang kh;
        if (iso == Connection.TRANSACTION_SERIALIZABLE && snapshotKhachHang != null) {
            // Lấy khách hàng từ snapshot (KHÔNG truy vấn lại DB)
            kh = snapshotKhachHang.stream()
                    .filter(k -> maKH.equals(k.getMaKH()))
                    .findFirst()
                    .orElse(null);
        } else {
            // Lấy từ DB như cũ
            kh = khachHangController.getKhachHangByMa(maKH);
        }
        if (kh == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<HopDong> hopDongs;
        List<PhieuBaoDuong> dsPBD;
        List<LichSuCongNo> lichSu;

        if (iso == Connection.TRANSACTION_SERIALIZABLE
            && snapshotAllHopDong != null && snapshotAllPBD != null && snapshotCongNo != null) {
            // Lọc từ snapshot
            hopDongs = snapshotAllHopDong.stream()
                .filter(hd -> maKH.equals(hd.getMaKH()))
                .toList();
            dsPBD = snapshotAllPBD.stream()
                .filter(pbd -> maKH.equals(pbd.getMaKH()) && "Khách gây hư hại".equalsIgnoreCase(pbd.getLoaiBD()))
                .toList();
            lichSu = snapshotCongNo.stream()
                .filter(ls -> maKH.equals(ls.getMaKH()))
                .toList();
        } else {
            // Truy vấn DB như cũ
            hopDongs = hopDongController.getHopDongByKhachHang(kh.getMaKH());
            dsPBD = baoDuongController.getPhieuBaoDuongByKhachHangAndLoai(maKH, "Khách gây hư hại");
            lichSu = congNoController.getLichSuCongNoByKhachHang(maKH);
        }

        double tongTienHopDong = 0;
        for (HopDong hd : hopDongs) {
            tongTienHopDong += hd.getTongTien();
        }
        double tongTienPBD = 0;
        DefaultTableModel modelPBD = new DefaultTableModel(new String[]{"Mã phiếu", "Ngày BD", "Xe", "Tổng tiền"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (PhieuBaoDuong pbd : dsPBD) {
            if (pbd.getLoaiBD() != null && "Khách gây hư hại".equalsIgnoreCase(pbd.getLoaiBD().trim())) {
                tongTienPBD += pbd.getTongTienBD();
                modelPBD.addRow(new Object[]{
                    pbd.getMaBD(),
                    dateFormat.format(pbd.getNgayBD()),
                    pbd.getXe() != null ? pbd.getXe().getBienSo() : pbd.getMaXe(),
                    currencyFormat.format(pbd.getTongTienBD()) + " VNĐ"
                });
            }
        }
        // Lấy lịch sử công nợ
        double tongPhatSinh = 0;
        DefaultTableModel modelPhatSinh = new DefaultTableModel(new String[]{"Mã LS", "Ngày GD", "Số tiền", "Ghi chú"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        double tongThanhToan = 0;
        DefaultTableModel modelThanhToan = new DefaultTableModel(new String[]{"Mã LS", "Ngày GD", "Số tiền", "Ghi chú"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (LichSuCongNo ls : lichSu) {
            if ("PHAT SINH".equalsIgnoreCase(ls.getLoaiGiaoDich())) {
                tongPhatSinh += ls.getSoTien();
                modelPhatSinh.addRow(new Object[]{
                    ls.getMaLichSu(),
                    dateFormat.format(ls.getNgayGiaoDich()),
                    currencyFormat.format(ls.getSoTien()) + " VNĐ",
                    ls.getGhiChu()
                });
            } else if ("THANH TOAN".equalsIgnoreCase(ls.getLoaiGiaoDich())) {
                tongThanhToan += ls.getSoTien();
                modelThanhToan.addRow(new Object[]{
                    ls.getMaLichSu(),
                    dateFormat.format(ls.getNgayGiaoDich()),
                    currencyFormat.format(ls.getSoTien()) + " VNĐ",
                    ls.getGhiChu()
                });
            }
        }
        double tongNo = tongTienPBD + tongPhatSinh - tongThanhToan + tongTienHopDong;

    // Tạo dialog chi tiết
    JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết tổng nợ", true);
    dialog.setSize(800, 650);
    dialog.setLocationRelativeTo(this);
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(new EmptyBorder(15, 15, 15, 15));
    // Thông tin KH
    JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 8));
    infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
    infoPanel.add(new JLabel("Mã KH:")); infoPanel.add(new JLabel(kh.getMaKH()));
    infoPanel.add(new JLabel("Họ tên:")); infoPanel.add(new JLabel(kh.getHoTen()));
    infoPanel.add(new JLabel("Tổng nợ hiện tại:")); infoPanel.add(new JLabel(currencyFormat.format(kh.getTongTienNo()) + " VNĐ"));
    panel.add(infoPanel, BorderLayout.NORTH);
    // Tabs chi tiết
    javax.swing.JTabbedPane tabbedPane = new javax.swing.JTabbedPane();
    // Tab phiếu bảo dưỡng
    JPanel pbdPanel = new JPanel(new BorderLayout());
    pbdPanel.add(new JLabel("Danh sách phiếu bảo dưỡng (Khách gây hư hại):", JLabel.LEFT), BorderLayout.NORTH);
    JTable tablePBD = new JTable(modelPBD); tablePBD.setRowHeight(28);
    pbdPanel.add(new JScrollPane(tablePBD), BorderLayout.CENTER);
    pbdPanel.add(new JLabel("Tổng cộng: " + currencyFormat.format(tongTienPBD) + " VNĐ", JLabel.RIGHT), BorderLayout.SOUTH);
    tabbedPane.addTab("Phiếu bảo dưỡng", pbdPanel);
    // Tab phát sinh
    JPanel psPanel = new JPanel(new BorderLayout());
    psPanel.add(new JLabel("Danh sách giao dịch PHÁT SINH:", JLabel.LEFT), BorderLayout.NORTH);
    JTable tablePS = new JTable(modelPhatSinh); tablePS.setRowHeight(28);
    psPanel.add(new JScrollPane(tablePS), BorderLayout.CENTER);
    psPanel.add(new JLabel("Tổng cộng: " + currencyFormat.format(tongPhatSinh) + " VNĐ", JLabel.RIGHT), BorderLayout.SOUTH);
    tabbedPane.addTab("Phát sinh", psPanel);
    // Tab thanh toán
    JPanel ttPanel = new JPanel(new BorderLayout());
    ttPanel.add(new JLabel("Danh sách giao dịch THANH TOÁN:", JLabel.LEFT), BorderLayout.NORTH);
    JTable tableTT = new JTable(modelThanhToan); tableTT.setRowHeight(28);
    ttPanel.add(new JScrollPane(tableTT), BorderLayout.CENTER);
    ttPanel.add(new JLabel("Tổng cộng: " + currencyFormat.format(tongThanhToan) + " VNĐ", JLabel.RIGHT), BorderLayout.SOUTH);
    tabbedPane.addTab("Thanh toán", ttPanel);
    // Tab hợp đồng
    JPanel hdPanel = new JPanel(new BorderLayout());
    DefaultTableModel modelHD = new DefaultTableModel(new String[]{"Mã HĐ", "Ngày lập", "Tổng tiền", "Trạng thái"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    for (HopDong hd : hopDongs) {
        modelHD.addRow(new Object[]{
            hd.getMaHD(),
            hd.getNgayLap() != null ? dateFormat.format(hd.getNgayLap()) : "",
            currencyFormat.format(hd.getTongTien()) + " VNĐ",
            hd.getTrangThai()
        });
    }
    JTable tableHD = new JTable(modelHD); tableHD.setRowHeight(28);
    hdPanel.add(new JLabel("Danh sách hợp đồng:", JLabel.LEFT), BorderLayout.NORTH);
    hdPanel.add(new JScrollPane(tableHD), BorderLayout.CENTER);
    hdPanel.add(new JLabel("Tổng cộng: " + currencyFormat.format(tongTienHopDong) + " VNĐ", JLabel.RIGHT), BorderLayout.SOUTH);
    tabbedPane.addTab("Hợp đồng", hdPanel);
    // Tổng kết
    JPanel summaryPanel = new JPanel(new GridLayout(0, 1, 5, 5));
    summaryPanel.setBorder(BorderFactory.createTitledBorder("Tổng kết"));
    summaryPanel.add(new JLabel("Tổng tiền bảo dưỡng (Khách gây hư hại): +" + currencyFormat.format(tongTienPBD) + " VNĐ"));
    summaryPanel.add(new JLabel("Tổng phát sinh: +" + currencyFormat.format(tongPhatSinh) + " VNĐ"));
    summaryPanel.add(new JLabel("Tổng tiền hợp đồng: +" + currencyFormat.format(tongTienHopDong) + " VNĐ"));
    summaryPanel.add(new JLabel("Tổng thanh toán: -" + currencyFormat.format(tongThanhToan) + " VNĐ"));
    summaryPanel.add(new JLabel("--------------------------------------"));
    summaryPanel.add(new JLabel("TỔNG NỢ: " + currencyFormat.format(tongNo) + " VNĐ", JLabel.RIGHT));
    panel.add(tabbedPane, BorderLayout.CENTER);
    panel.add(summaryPanel, BorderLayout.SOUTH);
    dialog.setContentPane(panel);
    dialog.setVisible(true);
}
    
    public void loadKhachHangData() {
        modelKhachHang.setRowCount(0); // Xóa dữ liệu cũ
        
        List<KhachHang> danhSachKH = congNoController.getKhachHangCoCongNo();
        for (KhachHang kh : danhSachKH) {
            modelKhachHang.addRow(new Object[]{
                kh.getMaKH(),
                kh.getHoTen(),
                currencyFormat.format(kh.getTongTienNo()) + " VNĐ"
            });
        }
    }

    private void searchCongNo() {
        String keyword = txtSearch.getText().trim();
        modelCongNo.setRowCount(0);
        if (keyword.isEmpty()) {
            loadCongNoData();
            return;
        }
        // Tìm kiếm công nợ theo mã LS, mã KH, tên KH, loại GD, ghi chú
        List<LichSuCongNo> danhSach = congNoController.searchLichSuCongNo(keyword);
        for (LichSuCongNo ls : danhSach) {
            KhachHang kh = khachHangController.getKhachHangByMa(ls.getMaKH());
            String maVaTenKH = kh != null ? (kh.getMaKH() + " - " + kh.getHoTen()) : ls.getMaKH();
            modelCongNo.addRow(new Object[]{
                ls.getMaLichSu(),
                maVaTenKH,
                dateFormat.format(ls.getNgayGiaoDich()),
                ls.getLoaiGiaoDich(),
                currencyFormat.format(ls.getSoTien()) + " VNĐ",
                ls.getGhiChu(),
                ""
            });
        }
    }
    
    private void filterCongNoByKhachHang(String maKH) {
        modelCongNo.setRowCount(0);
        
        List<LichSuCongNo> danhSachCongNo = congNoController.getLichSuCongNoByKhachHang(maKH);
        for (LichSuCongNo ls : danhSachCongNo) {
            modelCongNo.addRow(new Object[]{
                ls.getMaLichSu(),
                ls.getMaKH(),
                dateFormat.format(ls.getNgayGiaoDich()),
                ls.getLoaiGiaoDich(),
                currencyFormat.format(ls.getSoTien()) + " VNĐ",
                ls.getGhiChu(),
                "" // Cột thao tác
            });
        }
    }
    
    public void showCongNoDialog(LichSuCongNo congNo) {
        CongNoDialog dialog = new CongNoDialog(SwingUtilities.getWindowAncestor(this), congNo, this);
        dialog.setVisible(true);
    }
    
    public void showCapNhatCongNoDialog(LichSuCongNo congNo) {
        CapNhatCongNoDialog dialog = new CapNhatCongNoDialog(SwingUtilities.getWindowAncestor(this), congNo, this);
        dialog.setVisible(true);
    }   
    
    private void exportToExcel() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
    String defaultName = "CongNoExport_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx";
    fileChooser.setSelectedFile(new File(System.getProperty("user.home") + "/Desktop/" + defaultName));
    int userSelection = fileChooser.showSaveDialog(this);
    if (userSelection != JFileChooser.APPROVE_OPTION) return;
    File fileToSave = fileChooser.getSelectedFile();
    try (Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("CongNo");
        // Header
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < modelCongNo.getColumnCount(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(modelCongNo.getColumnName(i));
        }
        // Data
        for (int row = 0; row < modelCongNo.getRowCount(); row++) {
            Row excelRow = sheet.createRow(row + 1);
            for (int col = 0; col < modelCongNo.getColumnCount(); col++) {
                Object value = modelCongNo.getValueAt(row, col);
                excelRow.createCell(col).setCellValue(value != null ? value.toString() : "");
            }
        }
        // Auto-size columns
        for (int i = 0; i < modelCongNo.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);
        }
        try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
            workbook.write(fos);
        }
        JOptionPane.showMessageDialog(this, "Xuất Excel thành công:\n" + fileToSave.getAbsolutePath(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    } catch (IOException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
    
    //Đoạn thêm mới
    // Thêm hàm applyIsolationLevel
    private void applyIsolationLevel(String level) {
        try {
            // Kết thúc phiên xem báo cáo cũ
            congNoController.endReportView();

            // Thiết lập isolation level mới
            if ("SERIALIZABLE".equals(level)) {
                congNoController.setIsolationLevel(Connection.TRANSACTION_SERIALIZABLE);
            } else {
                congNoController.setIsolationLevel(Connection.TRANSACTION_READ_COMMITTED);
            }


            // Bắt đầu phiên xem báo cáo mới với isolation level đã chọn
            congNoController.startReportView();

            // QUAN TRỌNG: Thực hiện ngay một truy vấn để bắt đầu transaction
            // và thiết lập snapshot cho SERIALIZABLE
            congNoController.getAllLichSuCongNo();

            // Hiển thị thông báo và hướng dẫn
            JOptionPane.showMessageDialog(this,
                "Đã áp dụng isolation level: " + level + "\n\n" +
                "Hướng dẫn demo Phantom Read:\n" +
                "1. ĐÃ tạo transaction với isolation level " + level + "\n" +
                "2. Mở instance khác và thêm giao dịch mới\n" +
                "3. Quay lại đây và nhấn 'Làm mới'\n" +
                "4. Với READ_COMMITTED: Sẽ thấy giao dịch mới\n" +
                "5. Với SERIALIZABLE: Sẽ KHÔNG thấy giao dịch mới",
                "Thiết lập thành công", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }


    // Hàm nạp dữ liệu từ snapshot hoặc từ DB
public void loadCongNoDataNon(List<LichSuCongNo> danhSachCongNo) {
    modelCongNo.setRowCount(0); // Xóa dữ liệu cũ
    for (LichSuCongNo ls : danhSachCongNo) {
        KhachHang kh = khachHangController.getKhachHangByMa(ls.getMaKH());
        String maVaTenKH = kh != null ? (kh.getMaKH() + " - " + kh.getHoTen()) : ls.getMaKH();
        modelCongNo.addRow(new Object[]{
            ls.getMaLichSu(),
            maVaTenKH,
            dateFormat.format(ls.getNgayGiaoDich()),
            ls.getLoaiGiaoDich(),
            currencyFormat.format(ls.getSoTien()) + " VNĐ",
            ls.getGhiChu(),
            ""
        });
    }
}

public void loadKhachHangDataNon(List<KhachHang> danhSachKH) {
    modelKhachHang.setRowCount(0); // Xóa dữ liệu cũ
    for (KhachHang kh : danhSachKH) {
        modelKhachHang.addRow(new Object[]{
            kh.getMaKH(),
            kh.getHoTen(),
            currencyFormat.format(kh.getTongTienNo()) + " VNĐ"
        });
    }
}
}