package ui.admin.QLNV;

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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import model.NhanVien;
import ui.admin.QLXe.ButtonRenderer;

public class NhanVienTablePanel extends JPanel {
    private JTable tableNhanVien;
    private DefaultTableModel modelNhanVien;
    private NhanVienPanel parentPanel;

    // Cột của bảng
    private final String[] COLUMNS = {
            "Mã NV", "Họ Tên", "Số Điện Thoại", "Email",
            "Chức Vụ", "Thao Tác"
    };

    public NhanVienTablePanel(NhanVienPanel parent) {
        this.parentPanel = parent;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(5, 0, 5, 0));

        // Khởi tạo model cho bảng
        modelNhanVien = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Chỉ cho phép chỉnh sửa cột "Thao tác"
            }
        };

        // Khởi tạo bảng
        tableNhanVien = new JTable(modelNhanVien);
        tableNhanVien.setRowHeight(40);
        tableNhanVien.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableNhanVien.setShowGrid(true);
        tableNhanVien.setGridColor(new Color(230, 230, 230));
        tableNhanVien.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));

        // Thiết lập độ rộng cột
        TableColumnModel columnModel = tableNhanVien.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60); // Mã NV
        columnModel.getColumn(1).setPreferredWidth(150); // Họ Tên
        columnModel.getColumn(2).setPreferredWidth(120); // Số Điện Thoại
        columnModel.getColumn(3).setPreferredWidth(180); // Email
        columnModel.getColumn(4).setPreferredWidth(120); // Chức Vụ
        columnModel.getColumn(5).setPreferredWidth(150); // Thao tác

        // Custom renderer cho cột thao tác
        tableNhanVien.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        tableNhanVien.getColumnModel().getColumn(5).setCellEditor(new ButtonEditorNV(parentPanel));

        // Tùy chỉnh header bảng
        JTableHeader header = tableNhanVien.getTableHeader();
        header.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(60, 60, 60));
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        // Tùy chỉnh renderer cho bảng
        tableNhanVien.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
                if (!isSelected && column != 5) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    comp.setForeground(new Color(50, 50, 50));
                }

                return comp;
            }
        });

        // Thêm bảng vào scroll pane
        JScrollPane scrollPane = new JScrollPane(tableNhanVien);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);
    }

    // Phương thức cập nhật dữ liệu cho bảng
    public void updateData(List<NhanVien> danhSachNhanVien) {
        modelNhanVien.setRowCount(0); // Xóa dữ liệu cũ

        if (danhSachNhanVien != null) {
            for (NhanVien nv : danhSachNhanVien) {
                modelNhanVien.addRow(new Object[] {
                        nv.getMaNV(),
                        nv.getHoTen(),
                        nv.getSdt(),
                        nv.getEmail(),
                        nv.getChucVu(),
                        "" // Cột thao tác
                });
            }
        }
    }

    // Lấy nhân viên được chọn
    public String getSelectedNhanVienId() {
        int row = tableNhanVien.getSelectedRow();
        if (row >= 0) {
            return tableNhanVien.getValueAt(row, 0).toString();
        }
        return null;
    }

    public JTable getTable() {
        return tableNhanVien;
    }
}
