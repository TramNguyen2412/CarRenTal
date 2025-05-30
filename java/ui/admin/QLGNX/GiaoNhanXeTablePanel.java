package ui.admin.QLGNX;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component; // Added import
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

import model.GiaoNhanXe;

public class GiaoNhanXeTablePanel extends JPanel {
    private JTable tblGiaoNhanXe;
    private DefaultTableModel modelGiaoNhanXe;

    private final String[] COLUMN_NAMES = { "Mã Giao Nhận", "Mã Hợp Đồng", "Mã Xe", "Mã Nhân Viên", "Trạng Thái Xe",
            "Ghi Chú", "Trạng Thái Giao Nhận" };

    public GiaoNhanXeTablePanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE); // Consistent background

        modelGiaoNhanXe = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblGiaoNhanXe = new JTable(modelGiaoNhanXe);
        tblGiaoNhanXe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblGiaoNhanXe.setRowHeight(40); // Match HopDongPanel
        tblGiaoNhanXe.setShowGrid(true); // Match HopDongPanel
        tblGiaoNhanXe.setGridColor(new Color(230, 230, 230)); // Match HopDongPanel
        tblGiaoNhanXe.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Match HopDongPanel font style and size

        // Tùy chỉnh header bảng (similar to HopDongPanel)
        JTableHeader header = tblGiaoNhanXe.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Match HopDongPanel font style and size
        header.setBackground(new Color(240, 240, 240)); // Match HopDongPanel
        header.setForeground(new Color(60, 60, 60)); // Match HopDongPanel
        header.setPreferredSize(new Dimension(0, 40)); // Match HopDongPanel
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220))); // Match HopDongPanel

        // Set column widths (adjust as needed)
        TableColumnModel columnModel = tblGiaoNhanXe.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100); // MaGiaoNhan
        columnModel.getColumn(1).setPreferredWidth(100); // MaHD
        columnModel.getColumn(2).setPreferredWidth(80); // MaXe
        columnModel.getColumn(3).setPreferredWidth(100); // MaNV
        columnModel.getColumn(4).setPreferredWidth(180); // TrangThaiXe
        columnModel.getColumn(5).setPreferredWidth(180); // GhiChu
        columnModel.getColumn(6).setPreferredWidth(150); // TrangThaiGN

        // Custom cell renderer for padding and alternating row colors (similar to
        // HopDongPanel)
        tblGiaoNhanXe.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Thêm padding cho text trong ô (Match HopDongPanel)
                if (comp instanceof JLabel) {
                    ((JLabel) comp).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                }

                // Màu nền dòng chẵn/lẻ nếu không được chọn (Match HopDongPanel)
                if (!isSelected) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    comp.setForeground(new Color(50, 50, 50)); // Match HopDongPanel
                } else {
                    // Use table's default selection colors
                    comp.setBackground(table.getSelectionBackground());
                    comp.setForeground(table.getSelectionForeground());
                }

                // Default alignment to LEFT for all columns in GiaoNhanXeTable
                setHorizontalAlignment(SwingConstants.LEFT);

                return comp;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblGiaoNhanXe);
        scrollPane.getViewport().setBackground(Color.WHITE); // Consistent viewport background
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateData(List<GiaoNhanXe> danhSachGN) {
        modelGiaoNhanXe.setRowCount(0);
        if (danhSachGN != null) {
            for (GiaoNhanXe gn : danhSachGN) {
                modelGiaoNhanXe.addRow(new Object[] {
                        gn.getMaGiaoNhan(),
                        gn.getMaHD(),
                        gn.getMaXe(),
                        gn.getMaNV(),
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