package ui.admin.QLKH;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import model.KhachHang;
// import ui.admin.ButtonRenderer; // Already QLKH specific, ensure it's the updated one

@SuppressWarnings("serial")
public class KhachHangTablePanel extends JPanel {
    private JTable tableKhachHang;
    private DefaultTableModel modelKhachHang;
    private final String[] COLUMNS = { "Mã KH", "Họ Tên", "SĐT", "Email", "CCCD", "Địa Chỉ", "Tổng Nợ", "Thao tác" };
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private QuanLyKhachHangPanel parentPanel; // To pass to ButtonEditor

    public KhachHangTablePanel(QuanLyKhachHangPanel parentPanel) {
        this.parentPanel = parentPanel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(5, 0, 5, 0));

        modelKhachHang = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only "Thao tác" column is editable
            }
        };

        tableKhachHang = new JTable(modelKhachHang);
        tableKhachHang.setRowHeight(40);
        tableKhachHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableKhachHang.setShowGrid(true);
        tableKhachHang.setGridColor(new Color(230, 230, 230));
        tableKhachHang.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));

        // Thiết lập độ rộng cột
        TableColumnModel columnModel = tableKhachHang.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80); // Mã KH
        columnModel.getColumn(1).setPreferredWidth(180); // Họ Tên
        columnModel.getColumn(2).setPreferredWidth(100); // SĐT
        columnModel.getColumn(3).setPreferredWidth(180); // Email
        columnModel.getColumn(4).setPreferredWidth(120); // CCCD
        columnModel.getColumn(5).setPreferredWidth(200); // Địa Chỉ
        columnModel.getColumn(6).setPreferredWidth(100); // Tổng Nợ
        columnModel.getColumn(7).setPreferredWidth(180); // Thao tác (was 150)

        // Đặt renderer và editor cho cột button
        tableKhachHang.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        tableKhachHang.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(parentPanel));

        // Tăng chiều rộng cột button để chứa đủ 3 nút
        tableKhachHang.getColumnModel().getColumn(7).setPreferredWidth(160);
        tableKhachHang.getColumnModel().getColumn(7).setMinWidth(160);
        tableKhachHang.getColumnModel().getColumn(7).setMaxWidth(180);

        // Đặt chiều cao hàng phù hợp
        tableKhachHang.setRowHeight(35);

        // Tùy chỉnh header bảng
        JTableHeader header = tableKhachHang.getTableHeader();
        header.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(60, 60, 60));
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        // Tùy chỉnh renderer cho các ô dữ liệu
        tableKhachHang.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (comp instanceof JLabel) {
                    ((JLabel) comp).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Padding
                }

                // Màu nền dòng chẵn/lẻ nếu không được chọn và không phải cột thao tác
                if (!isSelected && column != 7) { // column 7 is "Thao tác"
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    comp.setForeground(new Color(50, 50, 50));
                } else if (isSelected) {
                    comp.setBackground(table.getSelectionBackground());
                    comp.setForeground(table.getSelectionForeground());
                }

                // Căn lề
                if (column == 0 || column == 2 || column == 4) { // Mã KH, SĐT, CCCD
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else if (column == 6) { // Tổng Nợ
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                return comp;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableKhachHang);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateData(List<KhachHang> danhSachKhachHang) {
        modelKhachHang.setRowCount(0); // Xóa dữ liệu cũ
        if (danhSachKhachHang != null) {
            for (KhachHang kh : danhSachKhachHang) {
                modelKhachHang.addRow(new Object[] {
                        kh.getMaKH(),
                        kh.getHoTen(),
                        kh.getSdt(),
                        kh.getEmail(),
                        kh.getCccd(),
                        kh.getDiaChi(),
                        currencyFormatter.format(kh.getTongTienNo()),
                        kh // Pass the KhachHang object itself for the button column
                });
            }
        }
    }

    public KhachHang getSelectedKhachHang() {
        int selectedRow = tableKhachHang.getSelectedRow();
        if (selectedRow >= 0) {
            // Assuming MaKH is the first column and unique identifier
            String maKH = (String) modelKhachHang.getValueAt(selectedRow, 0);
            // You might need to fetch the full KhachHang object from your
            // controller/service
            // For now, let's assume the object passed to the "Thao tác" column is the
            // KhachHang object
            Object khObject = modelKhachHang.getValueAt(selectedRow, 7);
            if (khObject instanceof KhachHang) {
                return (KhachHang) khObject;
            }
            // Fallback or fetch by maKH if the above is not reliable
        }
        return null;
    }

    public List<KhachHang> getCurrentDataFromTableModel() {
        List<KhachHang> khachHangs = new java.util.ArrayList<>();
        for (int i = 0; i < modelKhachHang.getRowCount(); i++) {
            // Assuming the KhachHang object itself is stored in the last column (Thao tác)
            // or you reconstruct it from the row data.
            // This needs to be robust based on how you populate the table.
            Object khObject = modelKhachHang.getValueAt(i, 7); // 7 is the "Thao tác" column index
            if (khObject instanceof KhachHang) {
                khachHangs.add((KhachHang) khObject);
            } else {
                // Fallback: Reconstruct KhachHang from row data if the object isn't directly
                // available
                // This is less ideal and depends on your KhachHang constructor and data types
                try {
                    String maKH = (String) modelKhachHang.getValueAt(i, 0);
                    String hoTen = (String) modelKhachHang.getValueAt(i, 1);
                    String sdt = (String) modelKhachHang.getValueAt(i, 2);
                    String email = (String) modelKhachHang.getValueAt(i, 3);
                    String cccd = (String) modelKhachHang.getValueAt(i, 4);
                    String diaChi = (String) modelKhachHang.getValueAt(i, 5);
                    // TongTienNo needs parsing from formatted string back to double
                    String tongNoStr = (String) modelKhachHang.getValueAt(i, 6);
                    double tongTienNo = 0;
                    try {
                        tongTienNo = currencyFormatter.parse(tongNoStr).doubleValue();
                    } catch (java.text.ParseException ex) {
                        // Handle parsing error, e.g., log or default to 0
                    }
                    // Assuming a constructor or setters are available
                    KhachHang kh = new KhachHang(); // Or new KhachHang(maKH, hoTen, ...);
                    kh.setMaKH(maKH);
                    kh.setHoTen(hoTen);
                    kh.setSdt(sdt);
                    kh.setEmail(email);
                    kh.setCccd(cccd);
                    kh.setDiaChi(diaChi);
                    kh.setTongTienNo(tongTienNo);
                    khachHangs.add(kh);
                } catch (Exception ex) {
                    // Log error or skip row if data is not as expected
                    System.err.println("Error reconstructing KhachHang from table row " + i + ": " + ex.getMessage());
                }
            }
        }
        return khachHangs;
    }

    public JTable getTableKhachHang() {
        return tableKhachHang;
    }
}