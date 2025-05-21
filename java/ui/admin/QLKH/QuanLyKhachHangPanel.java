package ui.admin.QLKH;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import controller.KhachHangController;
import model.KhachHang;

public class QuanLyKhachHangPanel extends JPanel implements ActionListener {
    private KhachHangController controller;
    private KhachHangTablePanel tablePanel;
    private JTextField txtTimKiem;
    private JComboBox<String> cboLoc;
    private JButton btnLamMoi, btnXuatExcel, btnThemKH;
    private final NumberFormat dinhDangTien = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    
    // Colors
    private final Color BUTTON_PRIMARY = new Color(23, 162, 184);    // View button - Teal
    private final Color BUTTON_WARNING = new Color(255, 193, 7);     // Edit button - Yellow
    private final Color BUTTON_DANGER = new Color(220, 53, 69);      // Delete button - Red
    private final Color BUTTON_SUCCESS = new Color(40, 167, 69);     // Add/Refresh button - Green
    private final Color BUTTON_EXPORT = new Color(111, 66, 193);     // Export button - Purple
    private final Color BUTTON_ADD = new Color(0, 123, 255);         // Add button - Blue
    
    public QuanLyKhachHangPanel() {
        controller = new KhachHangController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Top panel with title and search
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Title
        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
        topPanel.add(lblTitle, BorderLayout.WEST);

        // Search and filter panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.setBackground(Color.WHITE);

        JLabel lblTimKiem = new JLabel("Tìm kiếm: ");
        lblTimKiem.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));

        txtTimKiem = new JTextField(20);
        txtTimKiem.setMaximumSize(new Dimension(250, 30));
        txtTimKiem.setPreferredSize(new Dimension(250, 30));
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                timKiem();
            }
        });

        JLabel lblLoc = new JLabel("   Lọc: ");
        lblLoc.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));

        cboLoc = new JComboBox<>(new String[]{"Tất cả", "Có công nợ", "Không có công nợ"});
        cboLoc.setPreferredSize(new Dimension(150, 30));
        cboLoc.setMaximumSize(new Dimension(150, 30));
        cboLoc.addActionListener(e -> locDuLieu());

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setBackground(new Color(23, 162, 184)); // Teal color
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setBorderPainted(false);
        btnLamMoi.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        btnLamMoi.setPreferredSize(new Dimension(100, 35));
        btnLamMoi.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLamMoi.addActionListener(this);

        btnXuatExcel = new JButton("Xuất Excel");
        btnXuatExcel.setBackground(new Color(111, 66, 193)); // Purple color
        btnXuatExcel.setForeground(Color.WHITE);
        btnXuatExcel.setFocusPainted(false);
        btnXuatExcel.setBorderPainted(false);
        btnXuatExcel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        btnXuatExcel.setPreferredSize(new Dimension(120, 35));
        btnXuatExcel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXuatExcel.addActionListener(this);

        searchPanel.add(lblTimKiem);
        searchPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        searchPanel.add(txtTimKiem);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchPanel.add(lblLoc);
        searchPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        searchPanel.add(cboLoc);
        searchPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        searchPanel.add(btnLamMoi);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchPanel.add(btnXuatExcel);

        topPanel.add(searchPanel, BorderLayout.EAST);
        
        // Table panel
        tablePanel = new KhachHangTablePanel(this);
        
        // Bottom panel with Add button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        btnThemKH = new JButton("Thêm khách hàng");
        btnThemKH.setBackground(new Color(0, 123, 255)); // Blue color
        btnThemKH.setForeground(Color.WHITE);
        btnThemKH.setFocusPainted(false);
        btnThemKH.setBorderPainted(false);
        btnThemKH.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        btnThemKH.setPreferredSize(new Dimension(150, 40));
        btnThemKH.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThemKH.addActionListener(this);

        bottomPanel.add(btnThemKH);
        
        // Add components to main panel
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void loadData() {
        List<KhachHang> danhSachKH = controller.getAllKhachHang();
        tablePanel.updateData(danhSachKH);
    }
    
    private void timKiem() {
        String keyword = txtTimKiem.getText().trim();
        
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        
        List<KhachHang> danhSachKH = controller.searchKhachHang(keyword);
        tablePanel.updateData(danhSachKH);
    }
    
    private void locDuLieu() {
        String loaiLoc = (String) cboLoc.getSelectedItem();
        List<KhachHang> danhSachKH;
        
        if ("Có công nợ".equals(loaiLoc)) {
            // Filter customers with debt
            List<KhachHang> allKH = controller.getAllKhachHang();
            danhSachKH = new java.util.ArrayList<>();
            for (KhachHang kh : allKH) {
                if (kh.getTongTienNo() > 0) {
                    danhSachKH.add(kh);
                }
            }
        } else if ("Không có công nợ".equals(loaiLoc)) {
            // Filter customers without debt
            List<KhachHang> allKH = controller.getAllKhachHang();
            danhSachKH = new java.util.ArrayList<>();
            for (KhachHang kh : allKH) {
                if (kh.getTongTienNo() <= 0) {
                    danhSachKH.add(kh);
                }
            }
        } else {
            danhSachKH = controller.getAllKhachHang();
        }
        
        tablePanel.updateData(danhSachKH);
    }
    
    private void xuatCSV() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn vị trí lưu file CSV");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setSelectedFile(new File("DanhSachKhachHang.csv"));
            
            int userSelection = fileChooser.showSaveDialog(this);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                
                // Ensure file has .csv extension
                if (!filePath.endsWith(".csv")) {
                    filePath += ".csv";
                    fileToSave = new File(filePath);
                }
                
                // Create CSV file
                FileWriter writer = new FileWriter(fileToSave);
                
                // Write header
                writer.append("Mã KH,Họ Tên,SĐT,Email,CCCD,Địa Chỉ,Tổng Tiền Nợ\n");
                
                // Write data
                List<KhachHang> danhSachKH = controller.getAllKhachHang();
                for (KhachHang kh : danhSachKH) {
                    writer.append(kh.getMaKH()).append(",");
                    writer.append(escapeCSV(kh.getHoTen())).append(",");
                    writer.append(kh.getSdt()).append(",");
                    writer.append(escapeCSV(kh.getEmail() != null ? kh.getEmail() : "")).append(",");
                    writer.append(kh.getCccd() != null ? kh.getCccd() : "").append(",");
                    writer.append(escapeCSV(kh.getDiaChi() != null ? kh.getDiaChi() : "")).append(",");
                    writer.append(String.valueOf(kh.getTongTienNo())).append("\n");
                }
                
                writer.flush();
                writer.close();
                
                JOptionPane.showMessageDialog(this, "Xuất danh sách thành công!\nFile được lưu tại: " + filePath, 
                                             "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất file: " + e.getMessage(), 
                                         "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private String escapeCSV(String value) {
        if (value == null) return "";
        // If value contains comma, quote, or newline, wrap in quotes and escape quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    private void themKhachHang() {
        ThemKhachHangDialog dialog = new ThemKhachHangDialog(SwingUtilities.getWindowAncestor(this), controller);
        dialog.setVisible(true);
        loadData(); // Reload data after dialog closes
    }
    
    public void xemKhachHang(String maKH) {
        KhachHang kh = controller.getKhachHangByMa(maKH);
        
        if (kh != null) {
            ChiTietKhachHangDialog dialog = new ChiTietKhachHangDialog(SwingUtilities.getWindowAncestor(this), kh);
            dialog.setVisible(true);
        }
    }
    
    public void suaKhachHang(String maKH) {
        KhachHang kh = controller.getKhachHangByMa(maKH);
        
        if (kh != null) {
            SuaKhachHangDialog dialog = new SuaKhachHangDialog(SwingUtilities.getWindowAncestor(this), controller, kh);
            dialog.setVisible(true);
            loadData(); // Reload data after dialog closes
        }
    }
    
    public void xoaKhachHang(String maKH) {
        KhachHang kh = controller.getKhachHangByMa(maKH);
        
        if (kh != null) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa khách hàng " + kh.getHoTen() + " không?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean ketQua = controller.deleteKhachHang(maKH);
                
                if (ketQua) {
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công", 
                                                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, controller.getErrorMessage(), 
                                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    /*
     * Lấy khách hàng theo mã
     * @param maKH Mã khách hàng cần lấy
     * @return Đối tượng KhachHang nếu tìm thấy, null nếu không tìm thấy
     */
    public KhachHang getKhachHangById(String maKH) {
        return controller.getKhachHangByMa(maKH);
    }

    /*
     * Xóa khách hàng và trả về kết quả
     * @param maKH Mã khách hàng cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLamMoi) {
            loadData();
            txtTimKiem.setText("");
            cboLoc.setSelectedIndex(0);
        } else if (e.getSource() == btnXuatExcel) {
            xuatCSV();
        } else if (e.getSource() == btnThemKH) {
            themKhachHang();
        }
    }
}
