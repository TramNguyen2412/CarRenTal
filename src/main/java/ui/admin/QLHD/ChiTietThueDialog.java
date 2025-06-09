//package ui.admin.QLHD;
//
//import model.ChiTietHD;
//import java.awt.*;
//import java.awt.event.*;
//import java.text.NumberFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.text.MaskFormatter;
//import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
//import controller.HopDongController;
//
//public class ChiTietThueDialog extends JDialog {
//    private ChiTietHD chiTietHD;
//    private boolean confirmed = false;
//    private HopDongController hopDongController;
//    private String maHDHienTai;
//    
//    // UI Components
//    private JTextField txtMaXe, txtTenXe, txtBienSo, txtHangXe, txtSoCho, txtGiaThue;
//    private JFormattedTextField txtNgayBatDau, txtNgayKetThuc;
//    private JLabel lblSoNgay, lblThanhTien;
//    
////    public ChiTietThueDialog(Window owner, ChiTietHD chiTietHD) {
////        super(owner, "Thông tin thuê xe", ModalityType.APPLICATION_MODAL);
////        this.chiTietHD = chiTietHD;
////        this.hopDongController = new HopDongController();
////        initComponents();
////    }
//    public ChiTietThueDialog(Window owner, ChiTietHD chiTietHD, String maHD) {
//        super(owner, "Thông tin thuê xe", ModalityType.APPLICATION_MODAL);
//        this.chiTietHD = chiTietHD;
//        this.maHDHienTai = maHD;
//        this.hopDongController = new HopDongController();
//
//        initComponents();
//    }
//    private void initComponents() {
//        setSize(600, 400);
//        setLocationRelativeTo(getOwner());
//        setLayout(new BorderLayout(10, 10));
//        
//        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
//        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));
//        
//        // Panel thông tin xe
//        JPanel pnlInfo = new JPanel(new GridBagLayout());
//        pnlInfo.setBorder(BorderFactory.createTitledBorder("Thông tin xe"));
//        
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.insets = new Insets(5, 5, 5, 5);
//        
//        // Mã xe
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        pnlInfo.add(new JLabel("Mã xe:"), gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 1.0;
//        txtMaXe = new JTextField(20);
//        txtMaXe.setEditable(false);
//        pnlInfo.add(txtMaXe, gbc);
//        
//        // Tên xe
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        gbc.weightx = 0.0;
//        pnlInfo.add(new JLabel("Tên xe:"), gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 1.0;
//        txtTenXe = new JTextField(20);
//        txtTenXe.setEditable(false);
//        pnlInfo.add(txtTenXe, gbc);
//        
//        // Biển số
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        gbc.weightx = 0.0;
//        pnlInfo.add(new JLabel("Biển số:"), gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 1.0;
//        txtBienSo = new JTextField(20);
//        txtBienSo.setEditable(false);
//        pnlInfo.add(txtBienSo, gbc);
//        
//        // Hãng xe
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        gbc.weightx = 0.0;
//        pnlInfo.add(new JLabel("Hãng xe:"), gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 1.0;
//        txtHangXe = new JTextField(20);
//        txtHangXe.setEditable(false);
//        pnlInfo.add(txtHangXe, gbc);
//        
//        // Số chỗ
//        gbc.gridx = 0;
//        gbc.gridy = 4;
//        gbc.weightx = 0.0;
//        pnlInfo.add(new JLabel("Số chỗ:"), gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 1.0;
//        txtSoCho = new JTextField(20);
//        txtSoCho.setEditable(false);
//        pnlInfo.add(txtSoCho, gbc);
//        
//        // Giá thuê/ngày
//        gbc.gridx = 0;
//        gbc.gridy = 5;
//        gbc.weightx = 0.0;
//        pnlInfo.add(new JLabel("Giá thuê/ngày:"), gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 1.0;
//        txtGiaThue = new JTextField(20);
//        txtGiaThue.setEditable(false);
//        pnlInfo.add(txtGiaThue, gbc);
//        
//        // Panel thông tin thuê
//        JPanel pnlThue = new JPanel(new GridBagLayout());
//        pnlThue.setBorder(BorderFactory.createTitledBorder("Thông tin thuê"));
//        
//        gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.insets = new Insets(5, 5, 5, 5);
//        
//        // Từ ngày
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.weightx = 0.0;
//        pnlThue.add(new JLabel("Từ ngày:"), gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 1.0;
//        
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        
//        try {
//            MaskFormatter dateMask = new MaskFormatter("##/##/####");
//            dateMask.setPlaceholderCharacter('_');
//            
//            txtNgayBatDau = new JFormattedTextField(dateMask);
//            txtNgayBatDau.setColumns(10);
//            pnlThue.add(txtNgayBatDau, gbc);
//            
//            // Đến ngày
//            gbc.gridx = 0;
//            gbc.gridy = 1;
//            gbc.weightx = 0.0;
//            pnlThue.add(new JLabel("Đến ngày:"), gbc);
//            
//            gbc.gridx = 1;
//            gbc.weightx = 1.0;
//            txtNgayKetThuc = new JFormattedTextField(dateMask);
//            txtNgayKetThuc.setColumns(10);
//            pnlThue.add(txtNgayKetThuc, gbc);
//            
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        
//        // Số ngày
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        gbc.weightx = 0.0;
//        pnlThue.add(new JLabel("Số ngày thuê:"), gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 1.0;
//        lblSoNgay = new JLabel();
//        lblSoNgay.setFont(lblSoNgay.getFont().deriveFont(Font.BOLD));
//        pnlThue.add(lblSoNgay, gbc);
//        
//        // Thành tiền
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        gbc.weightx = 0.0;
//        pnlThue.add(new JLabel("Thành tiền:"), gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 1.0;
//        lblThanhTien = new JLabel();
//        lblThanhTien.setFont(lblThanhTien.getFont().deriveFont(Font.BOLD));
//        pnlThue.add(lblThanhTien, gbc);
//        
//        // Layout tổng thể
//        JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 10, 0));
//        pnlCenter.add(pnlInfo);
//        pnlCenter.add(pnlThue);
//        
//        pnlMain.add(pnlCenter, BorderLayout.CENTER);
//        
//        // Panel nút
//        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        JButton btnSave = new JButton("Lưu");
//        btnSave.setBackground(new Color(41, 121, 255)); // Xanh dương
//        btnSave.setForeground(Color.WHITE);            // Chữ trắng
//        btnSave.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14)); // Chữ đậm
//       
//
//        JButton btnCancel = new JButton("Hủy");
//        btnCancel.setBackground(Color.GRAY);           // Màu xám
//        btnCancel.setForeground(Color.WHITE);          // Chữ trắng
//        btnCancel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14)); // Chữ đậm
//               
//        pnlButtons.add(btnSave);
//        pnlButtons.add(btnCancel);
//        
//        add(pnlMain, BorderLayout.CENTER);
//        add(pnlButtons, BorderLayout.SOUTH);
//        
//        // Hiển thị thông tin xe
//        txtMaXe.setText(chiTietHD.getMaXe());
//        txtTenXe.setText(chiTietHD.getTenXe());
//        txtBienSo.setText(chiTietHD.getBienSo());
//        txtHangXe.setText(chiTietHD.getHangXe());
//        txtSoCho.setText(String.valueOf(chiTietHD.getSoCho()));
//        
//        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//        txtGiaThue.setText(currencyFormat.format(chiTietHD.getGiaThueNgay()));
//        
//        // Hiển thị thông tin thuê
//        txtNgayBatDau.setText(dateFormat.format(chiTietHD.getNgayBatDau()));
//        txtNgayKetThuc.setText(dateFormat.format(chiTietHD.getNgayKetThuc()));
//        
//        updateCalculations();
//        
//        // Sự kiện khi thay đổi ngày
//        txtNgayBatDau.addPropertyChangeListener("value", e -> updateCalculations());
//        txtNgayKetThuc.addPropertyChangeListener("value", e -> updateCalculations());
//        
//        // Sự kiện nút lưu
//        btnSave.addActionListener(e -> saveChiTietThue());
//        
//        // Sự kiện nút hủy
//        btnCancel.addActionListener(e -> dispose());
//    }
//    
//    private void updateCalculations() {
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            Date ngayBatDau = dateFormat.parse(txtNgayBatDau.getText());
//            Date ngayKetThuc = dateFormat.parse(txtNgayKetThuc.getText());
//            
//            if (ngayKetThuc.before(ngayBatDau)) {
//                lblSoNgay.setText("Lỗi: Ngày kết thúc phải sau ngày bắt đầu!");
//                lblThanhTien.setText("");
//                return;
//            }
//            
//            // Cập nhật số ngày
//            long diffInMillies = Math.abs(ngayKetThuc.getTime() - ngayBatDau.getTime());
//            long diff = diffInMillies / (24 * 60 * 60 * 1000);
//            int soNgay = (int) diff + 1;
//            lblSoNgay.setText(String.valueOf(soNgay));
//            
//            // Cập nhật thành tiền
//            double thanhTien = soNgay * chiTietHD.getGiaThueNgay();
//            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//            lblThanhTien.setText(currencyFormat.format(thanhTien));
//            
//        } catch (ParseException e) {
//            lblSoNgay.setText("Lỗi: Định dạng ngày không hợp lệ!");
//            lblThanhTien.setText("");
//        }
//    }
//   
//    private void saveChiTietThue() {
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            Date ngayBatDau = dateFormat.parse(txtNgayBatDau.getText());
//            Date ngayKetThuc = dateFormat.parse(txtNgayKetThuc.getText());
//
//            if (ngayKetThuc.before(ngayBatDau)) {
//                JOptionPane.showMessageDialog(this, 
//                        "Ngày kết thúc phải sau ngày bắt đầu!", 
//                        "Lỗi", 
//                        JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            // Kiểm tra xe có thể thuê được không
//            String errorMessage = hopDongController.kiemTraXeThueDuoc(
//                chiTietHD.getMaXe(), ngayBatDau, ngayKetThuc, maHDHienTai);
//
//            if (errorMessage != null) {
//                JOptionPane.showMessageDialog(this, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            // Nếu không có lỗi, cập nhật thông tin thuê
//            chiTietHD.setNgayBatDau(ngayBatDau);
//            chiTietHD.setNgayKetThuc(ngayKetThuc);
//            confirmed = true;
//            dispose();
//
//        } catch (ParseException e) {
//            JOptionPane.showMessageDialog(this, 
//                    "Vui lòng nhập đúng định dạng ngày (dd/MM/yyyy)!", 
//                    "Lỗi", 
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    
//    public boolean isConfirmed() {
//        return confirmed;
//    }
//}


package ui.admin.QLHD;

import model.ChiTietHD;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.toedter.calendar.JDateChooser; // Thêm import này
import controller.HopDongController;
import java.util.Calendar;
public class ChiTietThueDialog extends JDialog {
    private ChiTietHD chiTietHD;
    private boolean confirmed = false;
    private HopDongController hopDongController;
    private String maHDHienTai;
    
    // UI Components
    private JTextField txtMaXe, txtTenXe, txtBienSo, txtHangXe, txtSoCho, txtGiaThue;
    // Thay JFormattedTextField bằng JDateChooser
    private JDateChooser dateNgayBatDau, dateNgayKetThuc;
    private JLabel lblSoNgay, lblThanhTien;
    
    public ChiTietThueDialog(Window owner, ChiTietHD chiTietHD, String maHD) {
        super(owner, "Thông tin thuê xe", ModalityType.APPLICATION_MODAL);
        this.chiTietHD = chiTietHD;
        this.maHDHienTai = maHD;
        this.hopDongController = new HopDongController();

        initComponents();
    }
    
    private void initComponents() {
        setSize(600, 400);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));
        
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Panel thông tin xe
        JPanel pnlInfo = new JPanel(new GridBagLayout());
        pnlInfo.setBorder(BorderFactory.createTitledBorder("Thông tin xe"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Mã xe
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlInfo.add(new JLabel("Mã xe:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtMaXe = new JTextField(20);
        txtMaXe.setEditable(false);
        pnlInfo.add(txtMaXe, gbc);
        
        // Tên xe
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        pnlInfo.add(new JLabel("Tên xe:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTenXe = new JTextField(20);
        txtTenXe.setEditable(false);
        pnlInfo.add(txtTenXe, gbc);
        
        // Biển số
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        pnlInfo.add(new JLabel("Biển số:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtBienSo = new JTextField(20);
        txtBienSo.setEditable(false);
        pnlInfo.add(txtBienSo, gbc);
        
        // Hãng xe
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        pnlInfo.add(new JLabel("Hãng xe:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtHangXe = new JTextField(20);
        txtHangXe.setEditable(false);
        pnlInfo.add(txtHangXe, gbc);
        
        // Số chỗ
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        pnlInfo.add(new JLabel("Số chỗ:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtSoCho = new JTextField(20);
        txtSoCho.setEditable(false);
        pnlInfo.add(txtSoCho, gbc);
        
        // Giá thuê/ngày
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        pnlInfo.add(new JLabel("Giá thuê/ngày:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGiaThue = new JTextField(20);
        txtGiaThue.setEditable(false);
        pnlInfo.add(txtGiaThue, gbc);
        
        // Panel thông tin thuê
        JPanel pnlThue = new JPanel(new GridBagLayout());
        pnlThue.setBorder(BorderFactory.createTitledBorder("Thông tin thuê"));
        
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Từ ngày - Sử dụng JDateChooser thay vì JFormattedTextField
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        pnlThue.add(new JLabel("Từ ngày:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        
        // Khởi tạo JDateChooser cho ngày bắt đầu
        dateNgayBatDau = new JDateChooser();
        dateNgayBatDau.setDateFormatString("dd/MM/yyyy");
        dateNgayBatDau.setPreferredSize(new Dimension(120, 25));
        pnlThue.add(dateNgayBatDau, gbc);
        
        // Đến ngày
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        pnlThue.add(new JLabel("Đến ngày:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        // Khởi tạo JDateChooser cho ngày kết thúc
        dateNgayKetThuc = new JDateChooser();
        dateNgayKetThuc.setDateFormatString("dd/MM/yyyy");
        dateNgayKetThuc.setPreferredSize(new Dimension(120, 25));
        pnlThue.add(dateNgayKetThuc, gbc);
        
        // Số ngày
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        pnlThue.add(new JLabel("Số ngày thuê:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblSoNgay = new JLabel();
        lblSoNgay.setFont(lblSoNgay.getFont().deriveFont(Font.BOLD));
        pnlThue.add(lblSoNgay, gbc);
        
        // Thành tiền
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        pnlThue.add(new JLabel("Thành tiền:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblThanhTien = new JLabel();
        lblThanhTien.setFont(lblThanhTien.getFont().deriveFont(Font.BOLD));
        pnlThue.add(lblThanhTien, gbc);
        
        // Layout tổng thể
        JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlCenter.add(pnlInfo);
        pnlCenter.add(pnlThue);
        
        pnlMain.add(pnlCenter, BorderLayout.CENTER);
        
        // Panel nút
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu");
        btnSave.setBackground(new Color(41, 121, 255)); // Xanh dương
        btnSave.setForeground(Color.WHITE);            // Chữ trắng
        btnSave.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14)); // Chữ đậm
       
        JButton btnCancel = new JButton("Hủy");
        btnCancel.setBackground(Color.GRAY);           // Màu xám
        btnCancel.setForeground(Color.WHITE);          // Chữ trắng
        btnCancel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14)); // Chữ đậm
               
        pnlButtons.add(btnSave);
        pnlButtons.add(btnCancel);
        
        add(pnlMain, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);
        
        // Hiển thị thông tin xe
        txtMaXe.setText(chiTietHD.getMaXe());
        txtTenXe.setText(chiTietHD.getTenXe());
        txtBienSo.setText(chiTietHD.getBienSo());
        txtHangXe.setText(chiTietHD.getHangXe());
        txtSoCho.setText(String.valueOf(chiTietHD.getSoCho()));
        
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        txtGiaThue.setText(currencyFormat.format(chiTietHD.getGiaThueNgay()));
        
        // Thiết lập ngày trong JDateChooser
        dateNgayBatDau.setDate(chiTietHD.getNgayBatDau());
        dateNgayKetThuc.setDate(chiTietHD.getNgayKetThuc());
        
        updateCalculations();
        
        // Sự kiện khi thay đổi ngày trong JDateChooser
        dateNgayBatDau.getDateEditor().addPropertyChangeListener("date", e -> updateCalculations());
        dateNgayKetThuc.getDateEditor().addPropertyChangeListener("date", e -> updateCalculations());
        
        // Sự kiện nút lưu
        btnSave.addActionListener(e -> saveChiTietThue());
        
        // Sự kiện nút hủy
        btnCancel.addActionListener(e -> dispose());
    }
    
//    private void updateCalculations() {
//        try {
//            Date ngayBatDau = dateNgayBatDau.getDate();
//            Date ngayKetThuc = dateNgayKetThuc.getDate();
//            
//            if (ngayBatDau == null || ngayKetThuc == null) {
//                lblSoNgay.setText("Vui lòng chọn ngày bắt đầu và kết thúc");
//                lblThanhTien.setText("");
//                return;
//            }
//            
//            if (ngayKetThuc.before(ngayBatDau)) {
//                lblSoNgay.setText("Lỗi: Ngày kết thúc phải sau ngày bắt đầu!");
//                lblThanhTien.setText("");
//                return;
//            }
//            
//            // Cập nhật số ngày
//            long diffInMillies = Math.abs(ngayKetThuc.getTime() - ngayBatDau.getTime());
//            long diff = diffInMillies / (24 * 60 * 60 * 1000);
//            int soNgay = (int) diff + 1;
//            lblSoNgay.setText(String.valueOf(soNgay));
//            
//            // Cập nhật thành tiền
//            double thanhTien = soNgay * chiTietHD.getGiaThueNgay();
//            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//            lblThanhTien.setText(currencyFormat.format(thanhTien));
//            
//        } catch (Exception e) {
//            lblSoNgay.setText("Lỗi: Không thể tính toán!");
//            lblThanhTien.setText("");
//        }
//    }
    private void updateCalculations() {
        try {
            Date ngayBatDau = dateNgayBatDau.getDate();
            Date ngayKetThuc = dateNgayKetThuc.getDate();

            if (ngayBatDau == null || ngayKetThuc == null) {
                lblSoNgay.setText("Vui lòng chọn ngày bắt đầu và kết thúc");
                lblThanhTien.setText("");
                return;
            }

            if (ngayKetThuc.before(ngayBatDau)) {
                lblSoNgay.setText("Lỗi: Ngày kết thúc phải sau ngày bắt đầu!");
                lblThanhTien.setText("");
                return;
            }

            // Cập nhật số ngày - sử dụng Calendar để đảm bảo chính xác
            Calendar calBatDau = Calendar.getInstance();
            calBatDau.setTime(ngayBatDau);
            calBatDau.set(Calendar.HOUR_OF_DAY, 0);
            calBatDau.set(Calendar.MINUTE, 0);
            calBatDau.set(Calendar.SECOND, 0);
            calBatDau.set(Calendar.MILLISECOND, 0);

            Calendar calKetThuc = Calendar.getInstance();
            calKetThuc.setTime(ngayKetThuc);
            calKetThuc.set(Calendar.HOUR_OF_DAY, 0);
            calKetThuc.set(Calendar.MINUTE, 0);
            calKetThuc.set(Calendar.SECOND, 0);
            calKetThuc.set(Calendar.MILLISECOND, 0);

            long diffInMillies = calKetThuc.getTimeInMillis() - calBatDau.getTimeInMillis();
            int soNgay = (int) (diffInMillies / (24 * 60 * 60 * 1000)) + 1;
            lblSoNgay.setText(String.valueOf(soNgay));

            // Cập nhật thành tiền
            double thanhTien = soNgay * chiTietHD.getGiaThueNgay();
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            lblThanhTien.setText(currencyFormat.format(thanhTien));

        } catch (Exception e) {
            lblSoNgay.setText("Lỗi: Không thể tính toán!");
            lblThanhTien.setText("");
        }
    }

   
    private void saveChiTietThue() {
        try {
            Date ngayBatDau = dateNgayBatDau.getDate();
            Date ngayKetThuc = dateNgayKetThuc.getDate();
            
            if (ngayBatDau == null || ngayKetThuc == null) {
                JOptionPane.showMessageDialog(this, 
                        "Vui lòng chọn ngày bắt đầu và kết thúc!", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (ngayKetThuc.before(ngayBatDau)) {
                JOptionPane.showMessageDialog(this, 
                        "Ngày kết thúc phải sau ngày bắt đầu!", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra xe có thể thuê được không
            String errorMessage = hopDongController.kiemTraXeThueDuoc(
                chiTietHD.getMaXe(), ngayBatDau, ngayKetThuc, maHDHienTai);

            if (errorMessage != null) {
                JOptionPane.showMessageDialog(this, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Nếu không có lỗi, cập nhật thông tin thuê
            chiTietHD.setNgayBatDau(ngayBatDau);
            chiTietHD.setNgayKetThuc(ngayKetThuc);
            confirmed = true;
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Đã xảy ra lỗi khi lưu thông tin!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
}