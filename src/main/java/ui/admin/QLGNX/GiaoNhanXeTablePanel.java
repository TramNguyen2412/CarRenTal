package ui.admin.QLGNX;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import controller.HopDongController;
import controller.NhanVienController;
import controller.XeController;
import model.GiaoNhanXe;
import model.HopDong;
import model.NhanVien;
import model.Xe;

public class GiaoNhanXeTablePanel extends JPanel {
    private JTable tblGiaoNhanXe;
    private DefaultTableModel modelGiaoNhanXe;

    // Thêm controllers để lấy thông tin chi tiết
    private HopDongController hopDongController;
    private XeController xeController;
    private NhanVienController nhanVienController;

    // Cập nhật tên cột để phản ánh thông tin hiển thị
    private final String[] COLUMN_NAMES = {
            "Mã Giao Nhận",
            "Hợp Đồng",
            "Khách Hàng",
            "Xe",
            "Nhân Viên",
            "Trạng Thái Xe",
            "Ghi Chú",
            "Trạng Thái GN"
    };

    public GiaoNhanXeTablePanel() {
        this.hopDongController = new HopDongController();
        this.xeController = new XeController();
        this.nhanVienController = new NhanVienController();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        modelGiaoNhanXe = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblGiaoNhanXe = new JTable(modelGiaoNhanXe);
        tblGiaoNhanXe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblGiaoNhanXe.setRowHeight(40);
        tblGiaoNhanXe.setShowGrid(true);
        tblGiaoNhanXe.setGridColor(new Color(230, 230, 230));
        tblGiaoNhanXe.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Tùy chỉnh header bảng
        JTableHeader header = tblGiaoNhanXe.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(60, 60, 60));
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        // Điều chỉnh độ rộng cột để phù hợp với nội dung mới
        TableColumnModel columnModel = tblGiaoNhanXe.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(120); // Mã Giao Nhận
        columnModel.getColumn(1).setPreferredWidth(180); // Hợp Đồng (Mã + Tên KH)
        columnModel.getColumn(2).setPreferredWidth(200); // Khách Hàng (Mã + Tên)
        columnModel.getColumn(3).setPreferredWidth(220); // Xe (Mã + Tên + Biển số)
        columnModel.getColumn(4).setPreferredWidth(200); // Nhân Viên (Mã + Tên)
        columnModel.getColumn(5).setPreferredWidth(200); // Trạng Thái Xe
        columnModel.getColumn(6).setPreferredWidth(180); // Ghi Chú
        columnModel.getColumn(7).setPreferredWidth(150); // Trạng Thái GN

        // Custom cell renderer
        tblGiaoNhanXe.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
                } else {
                    comp.setBackground(table.getSelectionBackground());
                    comp.setForeground(table.getSelectionForeground());
                }

                setHorizontalAlignment(SwingConstants.LEFT);
                return comp;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblGiaoNhanXe);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateData(List<GiaoNhanXe> danhSachGN) {
        modelGiaoNhanXe.setRowCount(0);
        if (danhSachGN != null) {
            for (GiaoNhanXe gn : danhSachGN) {
                // Lấy thông tin hợp đồng và khách hàng - chỉ hiển thị tên
                String hopDongInfo = "N/A";
                String khachHangInfo = "N/A";
                try {
                    HopDong hd = hopDongController.getHopDongByMa(gn.getMaHD());
                    if (hd != null) {
                        hopDongInfo = gn.getMaHD(); // Chỉ hiển thị mã hợp đồng
                        khachHangInfo = hd.getTenKH() != null ? hd.getTenKH() : "N/A"; // Chỉ tên khách hàng
                    }
                } catch (Exception e) {
                    System.err.println("Error loading contract info for " + gn.getMaHD() + ": " + e.getMessage());
                }

                // Lấy thông tin xe - chỉ hiển thị tên xe
                String xeInfo = "N/A";
                try {
                    Xe xe = xeController.getXeByMa(gn.getMaXe());
                    if (xe != null) {
                        String tenXe = xe.getTenXe() != null ? xe.getTenXe() : "N/A";
                        String bienSo = xe.getBienSo() != null ? xe.getBienSo() : "";
                        // Chỉ hiển thị tên xe và biển số (không có mã)
                        xeInfo = tenXe + (!bienSo.isEmpty() ? " (" + bienSo + ")" : "");
                    }
                } catch (Exception e) {
                    System.err.println("Error loading car info for " + gn.getMaXe() + ": " + e.getMessage());
                }

                // Lấy thông tin nhân viên - chỉ hiển thị tên
                String nhanVienInfo = "N/A";
                try {
                    NhanVien nv = nhanVienController.getNhanVienByMa(gn.getMaNV());
                    if (nv != null) {
                        nhanVienInfo = nv.getHoTen() != null ? nv.getHoTen() : "N/A"; // Chỉ tên nhân viên
                    }
                } catch (Exception e) {
                    System.err.println("Error loading employee info for " + gn.getMaNV() + ": " + e.getMessage());
                }

                modelGiaoNhanXe.addRow(new Object[] {
                        gn.getMaGiaoNhan(), // Mã giao nhận vẫn hiển thị
                        hopDongInfo, // Mã hợp đồng
                        khachHangInfo, // Tên khách hàng
                        xeInfo, // Tên xe (biển số)
                        nhanVienInfo, // Tên nhân viên
                        gn.getTrangThaiXe(),
                        gn.getGhiChu(),
                        gn.getTrangThaiGN()
                });
            }
        }
        tblGiaoNhanXe.clearSelection();
    }

    public JTable getTable() {
        return tblGiaoNhanXe;
    }

    public String getSelectedGiaoNhanXeId() {
        int selectedRow = tblGiaoNhanXe.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = tblGiaoNhanXe.convertRowIndexToModel(selectedRow);
            if (modelRow < modelGiaoNhanXe.getRowCount()) {
                return modelGiaoNhanXe.getValueAt(modelRow, 0).toString();
            }
        }
        return null;
    }
}