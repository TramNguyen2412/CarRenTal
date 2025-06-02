
package ui.customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.DanhGiaController;
import model.DanhGia;
import model.KhachHang;
import model.TaiKhoan;
public class DanhGiaPanel extends JPanel {
    private JTable tableDanhGia;
    private DefaultTableModel tableModel;
    private JButton btnThemDanhGia, btnSuaDanhGia, btnXoaDanhGia;
    
    private DanhGiaController controller;
    private TaiKhoan taiKhoan;
    private KhachHang khachHang;
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    // Định nghĩa màu sắc
    private final Color DARK_BLUE = new Color(0, 51, 153); // Màu xanh đậm cho text và đường kẻ
    private final Color SELECTION_COLOR = new Color(0, 102, 204); // Màu xanh đậm cho selection
    
    public DanhGiaPanel() {
        controller = new DanhGiaController();
        initComponents();
    }
    
    public DanhGiaPanel(TaiKhoan taiKhoan, KhachHang khachHang) {
        this.taiKhoan = taiKhoan;
        this.khachHang = khachHang;
        controller = new DanhGiaController();
        initComponents();
        loadData();
    }
    
    public void setAccount(TaiKhoan taiKhoan, KhachHang khachHang) {
        this.taiKhoan = taiKhoan;
        this.khachHang = khachHang;
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // ---- PHẦN TIÊU ĐỀ ---- (Không có phần nền màu xanh lớn)
        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        pnlHeader.setBackground(Color.WHITE); // Nền trắng
        
        // Tiêu đề chính
        JLabel lblTitle = new JLabel("ĐÁNH GIÁ HỢP ĐỒNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(DARK_BLUE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlHeader.add(lblTitle);
        
        // Tiêu đề phụ
        JLabel lblSubTitle = new JLabel("Chia sẻ đánh giá của bạn về dịch vụ thuê xe");
        lblSubTitle.setFont(new Font("Arial", Font.ITALIC, 14));
        lblSubTitle.setForeground(new Color(100, 100, 100));
        lblSubTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
      //  pnlHeader.add(Box.createVerticalStrut(5));
        pnlHeader.add(lblSubTitle);
        
        // Đường kẻ phân cách
        JPanel pnlLine = new JPanel();
        pnlLine.setMaximumSize(new Dimension(1200, 1));
        pnlLine.setPreferredSize(new Dimension(1200, 1));
        pnlLine.setBackground(DARK_BLUE);
        pnlLine.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    //    pnlHeader.add(Box.createVerticalStrut(10));
        pnlHeader.add(pnlLine);
        
        add(pnlHeader, BorderLayout.NORTH);
        
        // ---- PHẦN NỘI DUNG CHÍNH ----
        JPanel pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBackground(Color.WHITE);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        // ---- BẢNG ĐÁNH GIÁ ----
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        
        // Tiêu đề panel
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                "Lịch sử đánh giá của bạn");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titledBorder.setTitleColor(DARK_BLUE);
        
        pnlTable.setBorder(BorderFactory.createCompoundBorder(
                titledBorder,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        // Table model và bảng
        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Mã đánh giá", "Hợp đồng", "Tên xe", "Điểm số", "Nhận xét", "Ngày đánh giá"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) {
                    return Integer.class; // Cột điểm số
                }
                return String.class;
            }
        };
        
        tableDanhGia = new JTable(tableModel);
        tableDanhGia.setRowHeight(30);
        tableDanhGia.setFont(new Font("Arial", Font.PLAIN, 14));
        tableDanhGia.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableDanhGia.getTableHeader().setBackground(new Color(240, 240, 240));
        
        // Thay đổi màu selection thành màu xanh đậm
        tableDanhGia.setSelectionBackground(SELECTION_COLOR); // Màu xanh đậm cho selection
        tableDanhGia.setSelectionForeground(Color.WHITE); // Màu chữ trắng khi được chọn
        tableDanhGia.setGridColor(new Color(240, 240, 240));
        
        // Căn giữa cho một số cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        tableDanhGia.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Mã đánh giá
        tableDanhGia.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Hợp đồng
        tableDanhGia.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Điểm số
        tableDanhGia.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Ngày đánh giá
        
        // Set độ rộng các cột
        tableDanhGia.getColumnModel().getColumn(0).setPreferredWidth(80);  // Mã đánh giá
        tableDanhGia.getColumnModel().getColumn(1).setPreferredWidth(80);  // Hợp đồng
        tableDanhGia.getColumnModel().getColumn(2).setPreferredWidth(150); // Tên xe
        tableDanhGia.getColumnModel().getColumn(3).setPreferredWidth(60);  // Điểm số
        tableDanhGia.getColumnModel().getColumn(4).setPreferredWidth(300); // Nhận xét
        tableDanhGia.getColumnModel().getColumn(5).setPreferredWidth(100); // Ngày đánh giá
        
        // Sự kiện click vào bảng để cập nhật trạng thái nút
        tableDanhGia.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                capNhatTrangThaiNut();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableDanhGia);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        pnlTable.add(scrollPane, BorderLayout.CENTER);
        
        // ---- PHẦN NÚT CHỨC NĂNG ----
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnSuaDanhGia = new JButton("Sửa đánh giá");
        btnSuaDanhGia.setFont(new Font("Arial", Font.PLAIN, 14));
        btnSuaDanhGia.setFocusPainted(false);
        btnSuaDanhGia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suaDanhGia();
            }
        });
        
        btnXoaDanhGia = new JButton("Xóa đánh giá");
        btnXoaDanhGia.setFont(new Font("Arial", Font.PLAIN, 14));
        btnXoaDanhGia.setFocusPainted(false);
        btnXoaDanhGia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xoaDanhGia();
            }
        });
        
        btnThemDanhGia = new JButton("Thêm đánh giá mới");
        btnThemDanhGia.setFont(new Font("Arial", Font.PLAIN, 14));
        btnThemDanhGia.setBackground(DARK_BLUE);
        btnThemDanhGia.setForeground(Color.WHITE);
        btnThemDanhGia.setFocusPainted(false);
        btnThemDanhGia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themDanhGia();
            }
        });
        
        pnlButtons.add(btnSuaDanhGia);
        pnlButtons.add(btnXoaDanhGia);
        pnlButtons.add(btnThemDanhGia);
        
        pnlTable.add(pnlButtons, BorderLayout.SOUTH);
        
        pnlContent.add(pnlTable, BorderLayout.CENTER);
        add(pnlContent, BorderLayout.CENTER);
        
        // Cập nhật trạng thái nút
        capNhatTrangThaiNut();
    }
    
    // Cập nhật trạng thái các nút dựa trên hàng được chọn
    private void capNhatTrangThaiNut() {
        int selectedRow = tableDanhGia.getSelectedRow();
        btnSuaDanhGia.setEnabled(selectedRow != -1);
        btnXoaDanhGia.setEnabled(selectedRow != -1);
    }
    
    // Tải dữ liệu đánh giá
    public void loadData() {
        if (khachHang == null) return;
        
        // Xóa dữ liệu cũ trong bảng
        tableModel.setRowCount(0);
        
        try {
            // Lấy danh sách đánh giá của khách hàng
            List<DanhGia> danhSachDanhGia = controller.getDanhGiaByMaKH(khachHang.getMaKH());
            
            // Nhóm đánh giá theo hợp đồng
            Map<String, List<DanhGia>> groupedByHopDong = danhSachDanhGia.stream()
                    .collect(Collectors.groupingBy(DanhGia::getMaHD));
            
            // Hiển thị dữ liệu nhóm theo hợp đồng
            for (String maHD : groupedByHopDong.keySet()) {
                List<DanhGia> danhGiasInHopDong = groupedByHopDong.get(maHD);
                
                // Tạo chuỗi tên xe cách nhau bởi dấu phẩy
                String tenXeList = danhGiasInHopDong.stream()
                        .map(DanhGia::getTenXe)
                        .distinct()
                        .collect(Collectors.joining(", "));
                
                // Lấy thông tin đánh giá đầu tiên làm đại diện (vì các đánh giá cùng hợp đồng có cùng điểm và bình luận)
                DanhGia firstDanhGia = danhGiasInHopDong.get(0);
                
                tableModel.addRow(new Object[]{
                    firstDanhGia.getMaDG(),
                    maHD,
                    tenXeList,
                    firstDanhGia.getDiemSo(),
                    firstDanhGia.getBinhLuan(),
                    dateFormat.format(firstDanhGia.getNgayDanhGia())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Lỗi khi tải dữ liệu đánh giá: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        
        // Cập nhật trạng thái nút
        capNhatTrangThaiNut();
    }
    
    // Hàm thêm đánh giá mới
    private void themDanhGia() {
        if (khachHang == null) {
            JOptionPane.showMessageDialog(this, 
                    "Vui lòng đăng nhập để thêm đánh giá",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            ThemDanhGiaDialog dialog = new ThemDanhGiaDialog(null, true, controller, khachHang);
            dialog.setVisible(true);
            
            // Nếu thêm thành công thì cập nhật lại bảng
            if (dialog.isSuccess()) {
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Lỗi khi mở form thêm đánh giá: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // Hàm sửa đánh giá
    private void suaDanhGia() {
        int selectedRow = tableDanhGia.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn đánh giá cần sửa",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String maDG = tableDanhGia.getValueAt(selectedRow, 0).toString();
        
        try {
            // Lấy thông tin đánh giá từ controller
            DanhGia danhGia = controller.getDanhGiaByMaDG(maDG);
            
            if (danhGia == null) {
                JOptionPane.showMessageDialog(this, 
                        "Không tìm thấy thông tin đánh giá",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Mở dialog sửa đánh giá
            SuaDanhGiaDialog dialog = new SuaDanhGiaDialog(null, true, controller, danhGia);
            dialog.setVisible(true);
            
            // Nếu sửa thành công thì cập nhật lại bảng
            if (dialog.isSuccess()) {
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Lỗi khi mở form sửa đánh giá: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // Hàm xóa đánh giá
    private void xoaDanhGia() {
        int selectedRow = tableDanhGia.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn đánh giá cần xóa",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String maDG = tableDanhGia.getValueAt(selectedRow, 0).toString();
        String maHD = tableDanhGia.getValueAt(selectedRow, 1).toString();
        String tenXe = tableDanhGia.getValueAt(selectedRow, 2).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa đánh giá cho hợp đồng " + maHD + " (" + tenXe + ")?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = controller.deleteDanhGia(maDG);
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Xóa đánh giá thành công",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không thể xóa đánh giá. Vui lòng thử lại sau",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi xóa đánh giá: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}