package ui.admin.QLKH;

import model.KhachHang;
import ui.admin.ButtonRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class KhachHangTablePanel extends JPanel {
    private JTable tableKhachHang;
    private DefaultTableModel modelKhachHang;
    private QuanLyKhachHangPanel parentPanel;
    private final NumberFormat dinhDangTien = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    
    // Cột của bảng
    private final String[] COLUMNS = {
        "Mã KH", "Họ Tên", "SĐT", "Email", "CCCD", "Địa Chỉ", "Tổng Tiền Nợ", "Thao Tác"
    };
    
    public KhachHangTablePanel(QuanLyKhachHangPanel parent) {
        this.parentPanel = parent;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(5, 0, 5, 0));
        
        // Khởi tạo model cho bảng
        modelKhachHang = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Chỉ cho phép chỉnh sửa cột "Thao tác"
            }
        };
        
        // Khởi tạo bảng
        tableKhachHang = new JTable(modelKhachHang);
        tableKhachHang.setRowHeight(40);
        tableKhachHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableKhachHang.setShowGrid(true);
        tableKhachHang.setGridColor(new Color(230, 230, 230));
        tableKhachHang.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        
        // Tùy chỉnh header bảng
        JTableHeader header = tableKhachHang.getTableHeader();
        header.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(60, 60, 60));
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        // Set table row height to match the image
        tableKhachHang.setRowHeight(40);

        // Update column widths to better match the image
        TableColumnModel columnModel = tableKhachHang.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);  // Mã KH
        columnModel.getColumn(1).setPreferredWidth(150); // Họ Tên
        columnModel.getColumn(2).setPreferredWidth(100); // SĐT
        columnModel.getColumn(3).setPreferredWidth(150); // Email
        columnModel.getColumn(4).setPreferredWidth(100); // CCCD
        columnModel.getColumn(5).setPreferredWidth(150); // Địa Chỉ
        columnModel.getColumn(6).setPreferredWidth(120); // Tổng Tiền Nợ
        columnModel.getColumn(7).setPreferredWidth(180); // Thao tác - make wider for the buttons
        
        // Custom renderer cho cột thao tác
        tableKhachHang.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        tableKhachHang.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(parentPanel));
        
        // Tùy chỉnh header bảng

        // Tùy chỉnh renderer cho bảng
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

                // Màu nền dòng chẵn/lẻ nếu không được chọn và không phải cột thao tác
                if (!isSelected && column != 7) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    comp.setForeground(new Color(50, 50, 50));
                }

                // Căn giữa cho một số cột
                if (column == 0 || column == 2 || column == 4) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } 
                // Căn phải cho cột tiền
                else if (column == 6) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                return comp;
            }
        });
        
        // Thêm bảng vào scroll pane
        JScrollPane scrollPane = new JScrollPane(tableKhachHang);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.setBackground(Color.WHITE);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    // Phương thức cập nhật dữ liệu cho bảng
    public void updateData(List<KhachHang> danhSachKH) {
        modelKhachHang.setRowCount(0); // Xóa dữ liệu cũ
        
        if (danhSachKH != null) {
            for (KhachHang kh : danhSachKH) {
                modelKhachHang.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getHoTen(),
                    kh.getSdt(),
                    kh.getEmail() != null ? kh.getEmail() : "",
                    kh.getCccd() != null ? kh.getCccd() : "",
                    kh.getDiaChi() != null ? kh.getDiaChi() : "",
                    dinhDangTien.format(kh.getTongTienNo()),
                    "" // Cột thao tác
                });
            }
        }
    }
    
    // Lấy khách hàng được chọn
    public String getSelectedKhachHangId() {
        int row = tableKhachHang.getSelectedRow();
        if (row >= 0) {
            return tableKhachHang.getValueAt(row, 0).toString();
        }
        return null;
    }
}
