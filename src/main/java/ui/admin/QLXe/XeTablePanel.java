package ui.admin.QLXe;

import model.Xe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class XeTablePanel extends JPanel {
    private JTable tableXe;
    private DefaultTableModel modelXe;
    private XePanel parentPanel;
    
    // Cột của bảng
    private final String[] COLUMNS = {
        "Mã Xe", "Tên Xe", "Biển Số", "Hãng Xe", 
        "Năm SX", "Trạng Thái", "Giá Thuê/Ngày", "Thao Tác"
    };
    
    public XeTablePanel(XePanel parent) {
        this.parentPanel = parent;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(5, 0, 5, 0));
        
        // Khởi tạo model cho bảng
        modelXe = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Chỉ cho phép chỉnh sửa cột "Thao tác"
            }
        };
        
        // Khởi tạo bảng
        tableXe = new JTable(modelXe);
        tableXe.setRowHeight(40);
        tableXe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableXe.setShowGrid(true);
        tableXe.setGridColor(new Color(230, 230, 230));
        tableXe.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        
        // Thiết lập độ rộng cột
        TableColumnModel columnModel = tableXe.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);  // Mã xe
        columnModel.getColumn(1).setPreferredWidth(150); // Tên xe
        columnModel.getColumn(2).setPreferredWidth(100); // Biển số
        columnModel.getColumn(3).setPreferredWidth(100); // Hãng xe
        columnModel.getColumn(4).setPreferredWidth(80);  // Năm SX
        columnModel.getColumn(5).setPreferredWidth(120); // Trạng thái
        columnModel.getColumn(6).setPreferredWidth(120); // Giá thuê
        columnModel.getColumn(7).setPreferredWidth(150); // Thao tác
        
        // Custom renderer cho cột thao tác
        tableXe.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        tableXe.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(parentPanel));
        
        // Tùy chỉnh header bảng
        JTableHeader header = tableXe.getTableHeader();
        header.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(60, 60, 60));
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        // Tùy chỉnh renderer cho bảng
        tableXe.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
                if (!isSelected && column != 7) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    comp.setForeground(new Color(50, 50, 50));
                }

                // Căn phải cho cột giá tiền
                if (column == 6) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                return comp;
            }
        });
        
        // Thêm bảng vào scroll pane
        JScrollPane scrollPane = new JScrollPane(tableXe);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.setBackground(Color.WHITE);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    // Phương thức cập nhật dữ liệu cho bảng
    public void updateData(List<Xe> danhSachXe) {
        modelXe.setRowCount(0); // Xóa dữ liệu cũ
        
        if (danhSachXe != null) {
            for (Xe xe : danhSachXe) {
                modelXe.addRow(new Object[]{
                    xe.getMaXe(),
                    xe.getTenXe(),
                    xe.getBienSo(),
                    xe.getHangXe(),
                    xe.getNamSX(),
                    xe.getTrangThai(),
                    String.format("%,d VND", (int)xe.getGiaThueNgay()),
                    "" // Cột thao tác
                });
            }
        }
    }
    
    // Lấy xe được chọn
    public String getSelectedXeId() {
        int row = tableXe.getSelectedRow();
        if (row >= 0) {
            return tableXe.getValueAt(row, 0).toString();
        }
        return null;
    }
}