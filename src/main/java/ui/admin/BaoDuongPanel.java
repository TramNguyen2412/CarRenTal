
package ui.admin;

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

import controller.BaoDuongController;
import model.KhachHang;
import model.NhanVien;
import model.PhieuBaoDuong;
import model.Xe;

public class BaoDuongPanel extends JPanel {
    private JTable tblBaoDuong;
    private DefaultTableModel modelBaoDuong;
    private JTextField txtSearch;
    private JComboBox<String> cboLoaiBD;
    private JButton btnThem, btnSua, btnXoa, btnXemChiTiet, btnRefresh;

    private BaoDuongController baoDuongController;

    public BaoDuongPanel() {
        this.baoDuongController = new BaoDuongController();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Tiêu đề
        JPanel pnlTitle = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ BẢO DƯỠNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
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

        pnlSearch.add(new JLabel("Loại BD:"));
        cboLoaiBD = new JComboBox<>(new String[]{"Tất cả", "Định Kỳ", "Khách gây hư hại"});
        cboLoaiBD.setPreferredSize(new Dimension(150, 30));
        pnlSearch.add(cboLoaiBD);

        JButton btnSearch = new JButton("Tìm");
        styleButton(btnSearch, new Color(41, 121, 255));
        pnlSearch.add(btnSearch);

        pnlTitle.add(pnlSearch, BorderLayout.EAST);
        add(pnlTitle, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] columns = {"Mã BD", "Mã Xe", "Mã Khách hàng", "Ngày BD", "Nhân viên", "Loại BD", "Tổng tiền"};
        modelBaoDuong = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblBaoDuong = new JTable(modelBaoDuong);
        tblBaoDuong.setRowHeight(40);
        tblBaoDuong.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblBaoDuong.setShowGrid(true);
        tblBaoDuong.setGridColor(new Color(230, 230, 230));
        tblBaoDuong.setFont(new Font("Arial", Font.PLAIN, 14));

        JTableHeader header = tblBaoDuong.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(60, 60, 60));
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        tblBaoDuong.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                if (comp instanceof JLabel) {
                    ((JLabel) comp).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                }
                if (!isSelected) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    comp.setForeground(new Color(50, 50, 50));
                }
                if (column == 6) {
                    ((DefaultTableCellRenderer) comp).setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    ((DefaultTableCellRenderer) comp).setHorizontalAlignment(SwingConstants.LEFT);
                }
                return comp;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblBaoDuong);
        add(scrollPane, BorderLayout.CENTER);

        // Panel nút chức năng
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnThem = new JButton("Thêm");
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

        // Sự kiện
        btnSearch.addActionListener(e -> searchBaoDuong());
        btnThem.addActionListener(e -> showBaoDuongDialog(null));
        btnSua.addActionListener(e -> editSelectedBaoDuong());
        btnXoa.addActionListener(e -> deleteSelectedBaoDuong());
        btnXemChiTiet.addActionListener(e -> viewSelectedBaoDuong());
        btnRefresh.addActionListener(e -> loadDataToTable());

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchBaoDuong();
                }
            }
        });

        cboLoaiBD.addActionListener(e -> searchBaoDuong());

        tblBaoDuong.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewSelectedBaoDuong();
                }
            }
        });

        tblBaoDuong.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = tblBaoDuong.getSelectedRow() != -1;
            btnSua.setEnabled(hasSelection);
            btnXoa.setEnabled(hasSelection);
            btnXemChiTiet.setEnabled(hasSelection);
        });

        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        btnXemChiTiet.setEnabled(false);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 35));
    }

    public void loadDataToTable() {
        try {
            modelBaoDuong.setRowCount(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            List<PhieuBaoDuong> danhSach = baoDuongController.getAllPhieuBaoDuong();
            for (PhieuBaoDuong phieu : danhSach) {
                Xe xe = baoDuongController.getXeByMa(phieu.getMaXe());
                KhachHang kh = phieu.getMaKH() != null ? baoDuongController.getKhachHangByMa(phieu.getMaKH()) : null;
                NhanVien nv = baoDuongController.getNhanVienByMa(phieu.getMaNV());
                modelBaoDuong.addRow(new Object[]{
                        phieu.getMaBD(),
                        xe != null ? phieu.getMaXe() : xe.getBienSo(),
                        kh != null ? kh.getMaKH() : "Không có",
                        dateFormat.format(phieu.getNgayBD()),
                        nv != null ? phieu.getMaNV() : nv.getHoTen(),
                        phieu.getLoaiBD(),
                        currencyFormat.format(phieu.getTongTienBD()) + " VNĐ"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu bảo dưỡng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBaoDuong() {
        try {
            String keyword = txtSearch.getText().trim();
            String loaiBD = cboLoaiBD.getSelectedItem().toString();
            modelBaoDuong.setRowCount(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            List<PhieuBaoDuong> danhSach;
            if (keyword.isEmpty() && loaiBD.equals("Tất cả")) {
                danhSach = baoDuongController.getAllPhieuBaoDuong();
            } else {
                danhSach = baoDuongController.searchPhieuBaoDuong(keyword, loaiBD);
            }
            for (PhieuBaoDuong phieu : danhSach) {
                Xe xe = baoDuongController.getXeByMa(phieu.getMaXe());
                KhachHang kh = phieu.getMaKH() != null ? baoDuongController.getKhachHangByMa(phieu.getMaKH()) : null;
                NhanVien nv = baoDuongController.getNhanVienByMa(phieu.getMaNV());
                modelBaoDuong.addRow(new Object[]{
                        phieu.getMaBD(),
                        xe != null ? xe.getBienSo() : phieu.getMaXe(),
                        kh != null ? kh.getHoTen() : "Không có",
                        dateFormat.format(phieu.getNgayBD()),
                        nv != null ? nv.getHoTen() : phieu.getMaNV(),
                        phieu.getLoaiBD(),
                        currencyFormat.format(phieu.getTongTienBD()) + " VNĐ"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tìm kiếm bảo dưỡng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showBaoDuongDialog(PhieuBaoDuong phieu) {
        try {
            BaoDuongDialog dialog = new BaoDuongDialog(SwingUtilities.getWindowAncestor(this), phieu, this);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi hiển thị dialog bảo dưỡng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelectedBaoDuong() {
        try {
            int selectedRow = tblBaoDuong.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn một phiếu bảo dưỡng để chỉnh sửa!",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String maBD = tblBaoDuong.getValueAt(selectedRow, 0).toString();
            PhieuBaoDuong phieu = baoDuongController.getPhieuBaoDuongById(maBD);
            if (phieu == null) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy thông tin phiếu bảo dưỡng với mã: " + maBD,
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            showBaoDuongDialog(phieu);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi sửa phiếu bảo dưỡng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewSelectedBaoDuong() {
        try {
            int selectedRow = tblBaoDuong.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn một phiếu bảo dưỡng để xem chi tiết!",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String maBD = tblBaoDuong.getValueAt(selectedRow, 0).toString();
            PhieuBaoDuong phieu = baoDuongController.getPhieuBaoDuongById(maBD);
            if (phieu == null) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy thông tin phiếu bảo dưỡng với mã: " + maBD,
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // TODO: show dialog chi tiết phiếu bảo dưỡng
            showChiTietBaoDuongDialog(phieu);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi xem chi tiết phiếu bảo dưỡng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedBaoDuong() {
        try {
            int selectedRow = tblBaoDuong.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn một phiếu bảo dưỡng để xóa!",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String maBD = tblBaoDuong.getValueAt(selectedRow, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa phiếu bảo dưỡng này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String result = baoDuongController.deletePhieuBaoDuong(maBD);
                JOptionPane.showMessageDialog(this, result,
                        result.contains("thành công") ? "Thông báo" : "Lỗi",
                        result.contains("thành công") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                if (result.contains("thành công")) {
                    loadDataToTable();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi xóa phiếu bảo dưỡng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
private void showChiTietBaoDuongDialog(PhieuBaoDuong phieu) {
    ChiTietBaoDuongDialog dialog = new ChiTietBaoDuongDialog(
        SwingUtilities.getWindowAncestor(this),
        phieu,
        this // truyền chính panel hiện tại
    );
    dialog.setVisible(true);
}


}