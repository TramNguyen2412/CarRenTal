//package ui.customer;
//
//import controller.DanhGiaController;
//import ui.customer.StarRatingComponent;
//import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
//
//import javax.swing.*;
//import javax.swing.border.*;
//import java.awt.*;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Map;
//import model.ChiTietHD;
//import java.util.HashMap;
//import java.util.List;
//public class DanhGiaDialog extends JDialog {
//    private DanhGiaController danhGiaController;
//    private StarRatingComponent starRating;
//    private JTextArea binhLuanText;
//    private JButton btnLuu;
//    private JButton btnHuy;
//    private String maKH;
//    private String maHD;
//    private Map<String, Object> hopDong;
//    private SimpleDateFormat dateFormat;
//    private DecimalFormat moneyFormat;
//    
//    public DanhGiaDialog(JFrame parent, String maKH, Map<String, Object> hopDong) {
//        super(parent, "Đánh giá hợp đồng", true);
//        
//        this.danhGiaController = new DanhGiaController();
//        this.maKH = maKH;
//        this.hopDong = hopDong;
//        this.maHD = (String) hopDong.get("MaHD");
//        
//        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        this.moneyFormat = new DecimalFormat("#,###");
//        
//        initComponents();
//        setSize(500, 600);
//        setLocationRelativeTo(parent);
//        setResizable(false);
//    }
//    
//    private void initComponents() {
//        setLayout(new BorderLayout(10, 10));
//        
//        // Panel chứa toàn bộ nội dung
//        JPanel contentPanel = new JPanel();
//        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
//        contentPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
//        contentPanel.setBackground(Color.WHITE);
//        
//        // Tiêu đề
//        JLabel titleLabel = new JLabel("Đánh Giá Hợp Đồng");
//        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 20));
//        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        // Thêm nội dung
//        contentPanel.add(titleLabel);
//        contentPanel.add(Box.createVerticalStrut(15));
//        contentPanel.add(createContractInfoPanel());
//        contentPanel.add(Box.createVerticalStrut(15));
//        contentPanel.add(createRatingPanel());
//        contentPanel.add(Box.createVerticalStrut(15));
//        contentPanel.add(createCommentPanel());
//        contentPanel.add(Box.createVerticalStrut(20));
//        contentPanel.add(createButtonPanel());
//        
//        // Thêm vào dialog
//        add(contentPanel, BorderLayout.CENTER);
//    }
//    
//  //  private JPanel createContractInfoPanel() {
////        JPanel panel = new JPanel();
////        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
////        panel.setBorder(BorderFactory.createCompoundBorder(
////            new MatteBorder(1, 1, 1, 1, new Color(230, 230, 230)),
////            new EmptyBorder(15, 15, 15, 15)
////        ));
////        panel.setBackground(new Color(250, 250, 250));
////        
////        // Thông tin hợp đồng
////        JLabel contractLabel = new JLabel("Thông tin hợp đồng #" + maHD);
////        contractLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
////        
////        // Tạo panel với GridLayout để hiển thị thông tin
////        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 5));
////        infoPanel.setOpaque(false);
////        List<ChiTietHD> chiTietList = danhGiaController.getChiTietHopDong(maHD);
////        // Thông tin xe
////        String tenXe = (String) hopDong.get("TenXe");
////        int soLuongXe = (int) hopDong.get("SoLuongXe");
////        String xeInfo = tenXe;
////        if (soLuongXe > 1) {
////            xeInfo += " và " + (soLuongXe - 1) + " xe khác";
////        }
////        
////        addInfoRow(infoPanel, "Xe thuê:", xeInfo);
////        addInfoRow(infoPanel, "Ngày thuê:", dateFormat.format(hopDong.get("NgayBatDau")) + " đến " + 
////                                            dateFormat.format(hopDong.get("NgayKetThuc")));
////        addInfoRow(infoPanel, "Tổng tiền:", moneyFormat.format(hopDong.get("TongTien")) + " VND");
////        
////        panel.add(contractLabel);
////        panel.add(Box.createVerticalStrut(10));
////        panel.add(infoPanel);
////        
////        return panel;
////    }
//    private JPanel createContractInfoPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setBorder(BorderFactory.createCompoundBorder(
//            new MatteBorder(1, 1, 1, 1, new Color(230, 230, 230)),
//            new EmptyBorder(15, 15, 15, 15)
//        ));
//        panel.setBackground(new Color(250, 250, 250));
//
//        // Thông tin hợp đồng
//        JLabel contractLabel = new JLabel("Thông tin hợp đồng #" + maHD);
//        contractLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
//
//        // Tạo panel với GridLayout để hiển thị thông tin
//        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 5));
//        infoPanel.setOpaque(false);
//
//        // Lấy danh sách chi tiết xe thuê theo mã hợp đồng
//        List<ChiTietHD> chiTietList = danhGiaController.getChiTietHopDong(maHD);
//
//        // Xây dựng thông tin xe
//        StringBuilder xeInfoBuilder = new StringBuilder();
//        Map<String, Integer> xeCounts = new HashMap<>(); // Để đếm số lượng mỗi loại xe
//
//        // Đếm số lượng của mỗi loại xe dựa trên tên xe
//        for (ChiTietHD chiTiet : chiTietList) {
//            String tenXe = chiTiet.getTenXe();
//            xeCounts.put(tenXe, xeCounts.getOrDefault(tenXe, 0) + 1);
//        }
//
//        // Xây dựng chuỗi thông tin xe
//        int i = 0;
//        for (Map.Entry<String, Integer> entry : xeCounts.entrySet()) {
//            String tenXe = entry.getKey();
//            int soLuong = entry.getValue();
//
//            if (soLuong > 1) {
//                xeInfoBuilder.append(tenXe).append(" (").append(soLuong).append(" xe)");
//            } else {
//                xeInfoBuilder.append(tenXe);
//            }
//
//            // Nếu không phải mục cuối cùng, thêm dấu phẩy
//            if (i < xeCounts.size() - 1) {
//                xeInfoBuilder.append(", ");
//            }
//            i++;
//        }
//
//        String xeInfo = xeInfoBuilder.toString();
//
//        addInfoRow(infoPanel, "Xe thuê:", xeInfo);
//        addInfoRow(infoPanel, "Ngày thuê:", dateFormat.format(hopDong.get("NgayBatDau")) + " đến " + 
//                                            dateFormat.format(hopDong.get("NgayKetThuc")));
//        addInfoRow(infoPanel, "Tổng tiền:", moneyFormat.format(hopDong.get("TongTien")) + " VND");
//
//        panel.add(contractLabel);
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(infoPanel);
//
//        return panel;
//    }
//
//    
//    private void addInfoRow(JPanel panel, String label, String value) {
////        JLabel lblName = new JLabel(label);
////        lblName.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
////        
////        JLabel lblValue = new JLabel(value);
////        lblValue.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
////        
////        panel.add(lblName);
////        panel.add(lblValue);
//           JLabel lblName = new JLabel(label);
//            lblName.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//
//            // Sử dụng JTextArea không cho phép chỉnh sửa để hiển thị multi-line
//            JTextArea txtValue = new JTextArea(value);
//            txtValue.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//            txtValue.setEditable(false); // Không cho phép chỉnh sửa
//            txtValue.setWrapStyleWord(true); // Ngắt dòng theo từ
//            txtValue.setLineWrap(true); // Tự động xuống dòng
//            txtValue.setOpaque(false); // Trong suốt như JLabel
//            txtValue.setBorder(null); // Không có viền
//
//            panel.add(lblName);
//            panel.add(txtValue);
//
//    }
//    
//    private JPanel createRatingPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setOpaque(false);
//        
//        JLabel ratingLabel = new JLabel("Đánh giá chất lượng dịch vụ:");
//        ratingLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
//        ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        
//        // Thêm component đánh giá sao
//        starRating = new StarRatingComponent(5, 0);
//        starRating.setAlignmentX(Component.LEFT_ALIGNMENT);
//        
//        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        starPanel.setOpaque(false);
//        starPanel.add(starRating);
//        
//        JLabel instructionLabel = new JLabel("(Nhấp vào số sao tương ứng với mức độ hài lòng của bạn)");
//        instructionLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 12));
//        instructionLabel.setForeground(Color.GRAY);
//        instructionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        
//        panel.add(ratingLabel);
//        panel.add(Box.createVerticalStrut(5));
//        panel.add(starPanel);
//        panel.add(instructionLabel);
//        
//        return panel;
//    }
//    
//    private JPanel createCommentPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setOpaque(false);
//        
//        JLabel commentLabel = new JLabel("Nhận xét của bạn:");
//        commentLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
//        commentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        
//        binhLuanText = new JTextArea();
//        binhLuanText.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
//        binhLuanText.setLineWrap(true);
//        binhLuanText.setWrapStyleWord(true);
//        binhLuanText.setRows(5);
//        
//        JScrollPane scrollPane = new JScrollPane(binhLuanText);
//        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
//        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
//        
//        panel.add(commentLabel);
//        panel.add(Box.createVerticalStrut(5));
//        panel.add(scrollPane);
//        
//        return panel;
//    }
//    
//    private JPanel createButtonPanel() {
//        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
//        panel.setOpaque(false);
//        
//        btnLuu = new JButton("Gửi Đánh Giá");
//        btnLuu.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        btnLuu.setBackground(new Color(0, 150, 0));
//        btnLuu.setForeground(Color.WHITE);
//        btnLuu.setFocusPainted(false);
//        btnLuu.putClientProperty("JButton.buttonType", "roundRect");
//        btnLuu.addActionListener(e -> guiDanhGia());
//        
//        btnHuy = new JButton("Hủy");
//        btnHuy.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        btnHuy.setFocusPainted(false);
//        btnHuy.putClientProperty("JButton.buttonType", "roundRect");
//        btnHuy.addActionListener(e -> dispose());
//        
//        panel.add(btnLuu);
//        panel.add(btnHuy);
//        
//        return panel;
//    }
//    
//    private void guiDanhGia() {
//        int rating = starRating.getRating();
//        String comment = binhLuanText.getText();
//
//        if (rating == 0) {
//            JOptionPane.showMessageDialog(this,
//                "Vui lòng chọn số sao đánh giá",
//                "Thiếu thông tin",
//                JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        boolean result = danhGiaController.themDanhGia(maKH, maHD, rating, comment);
//
//        if (result) {
//            JOptionPane.showMessageDialog(this,
//                "Đánh giá của bạn đã được gửi thành công!",
//                "Thành công",
//                JOptionPane.INFORMATION_MESSAGE);
//
//            // Lấy panel cha (DanhGiaPanel) và gọi phương thức loadData
//            Component parent = getParent();
//            while (parent != null && !(parent instanceof DanhGiaPanel)) {
//                parent = parent.getParent();
//            }
//
//            if (parent instanceof DanhGiaPanel) {
//                ((DanhGiaPanel) parent).loadData();
//            }
//
//            dispose();
//        } else {
//            JOptionPane.showMessageDialog(this,
//                "Không thể gửi đánh giá: " + danhGiaController.getErrorMessage(),
//                "Lỗi",
//                JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//}



package ui.customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import controller.DanhGiaController;
import java.util.List;

public class DanhGiaDialog extends JDialog {
    private JComboBox<String> cboHopDong;
    private JTextField txtMaKH, txtTenKhachHang;
    private JRadioButton rad1Sao, rad2Sao, rad3Sao, rad4Sao, rad5Sao;
    private JTextArea txtBinhLuan;
    private JButton btnLuu, btnHuy;
    
    private DanhGiaController danhGiaController;
    private boolean success = false;
    private String maKHHienTai = "KH001"; // Giả sử lấy từ thông tin đăng nhập
    
    public DanhGiaDialog(JFrame parent, boolean modal, DanhGiaController controller) {
        super(parent, "Thêm đánh giá mới", modal);
        this.danhGiaController = controller;
        initComponents();
    }
    
    private void initComponents() {
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        
        // Panel thông tin
        JPanel pnlThongTin = new JPanel(new GridBagLayout());
        pnlThongTin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Thông tin khách hàng
        JLabel lblMaKH = new JLabel("Mã khách hàng:");
        lblMaKH.setFont(new Font("Arial", Font.BOLD, 14));
        pnlThongTin.add(lblMaKH, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        txtMaKH = new JTextField(20);
        txtMaKH.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMaKH.setEditable(false);
        txtMaKH.setText(maKHHienTai);
        pnlThongTin.add(txtMaKH, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        
        JLabel lblTenKH = new JLabel("Tên khách hàng:");
        lblTenKH.setFont(new Font("Arial", Font.BOLD, 14));
        pnlThongTin.add(lblTenKH, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        
        // Giả sử lấy tên khách hàng từ cơ sở dữ liệu
        txtTenKhachHang = new JTextField(20);
        txtTenKhachHang.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTenKhachHang.setEditable(false);
        txtTenKhachHang.setText("Nguyễn Văn A"); // Giả sử
        pnlThongTin.add(txtTenKhachHang, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        
        JLabel lblHopDong = new JLabel("Chọn hợp đồng:");
        lblHopDong.setFont(new Font("Arial", Font.BOLD, 14));
        pnlThongTin.add(lblHopDong, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        
        // Lấy danh sách hợp đồng chưa đánh giá
        List<String> danhSachMaHD = danhGiaController.getHopDongChuaDanhGia(maKHHienTai);
        String[] dsHopDong = new String[danhSachMaHD.size()];
        
        for (int i = 0; i < danhSachMaHD.size(); i++) {
            String maHD = danhSachMaHD.get(i);
            dsHopDong[i] = danhGiaController.getThongTinHopDong(maHD);
        }
        
        cboHopDong = new JComboBox<>(dsHopDong);
        cboHopDong.setFont(new Font("Arial", Font.PLAIN, 14));
        pnlThongTin.add(cboHopDong, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        
        JLabel lblDanhGia = new JLabel("Đánh giá (Sao):");
        lblDanhGia.setFont(new Font("Arial", Font.BOLD, 14));
        pnlThongTin.add(lblDanhGia, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        
        JPanel pnlSao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup bgSao = new ButtonGroup();
        
        rad1Sao = new JRadioButton("1");
        rad1Sao.setFont(new Font("Arial", Font.PLAIN, 14));
        rad2Sao = new JRadioButton("2");
        rad2Sao.setFont(new Font("Arial", Font.PLAIN, 14));
        rad3Sao = new JRadioButton("3");
        rad3Sao.setFont(new Font("Arial", Font.PLAIN, 14));
        rad4Sao = new JRadioButton("4");
        rad4Sao.setFont(new Font("Arial", Font.PLAIN, 14));
        rad5Sao = new JRadioButton("5", true);
        rad5Sao.setFont(new Font("Arial", Font.PLAIN, 14));
        
        bgSao.add(rad1Sao);
        bgSao.add(rad2Sao);
        bgSao.add(rad3Sao);
        bgSao.add(rad4Sao);
        bgSao.add(rad5Sao);
        
        pnlSao.add(rad1Sao);
        pnlSao.add(rad2Sao);
        pnlSao.add(rad3Sao);
        pnlSao.add(rad4Sao);
        pnlSao.add(rad5Sao);
        
        pnlThongTin.add(pnlSao, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        
        JLabel lblBinhLuan = new JLabel("Nhận xét:");
        lblBinhLuan.setFont(new Font("Arial", Font.BOLD, 14));
        pnlThongTin.add(lblBinhLuan, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        
        txtBinhLuan = new JTextArea();
        txtBinhLuan.setFont(new Font("Arial", Font.PLAIN, 14));
        txtBinhLuan.setLineWrap(true);
        txtBinhLuan.setWrapStyleWord(true);
        JScrollPane scrollBinhLuan = new JScrollPane(txtBinhLuan);
        scrollBinhLuan.setPreferredSize(new Dimension(300, 100));
        pnlThongTin.add(scrollBinhLuan, gbc);
        
        add(pnlThongTin, BorderLayout.CENTER);
        
        // Panel nút
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnLuu = new JButton("Lưu");
        btnLuu.setFont(new Font("Arial", Font.PLAIN, 14));
        btnLuu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                luuDanhGia();
            }
        });
        
        btnHuy = new JButton("Hủy");
        btnHuy.setFont(new Font("Arial", Font.PLAIN, 14));
        btnHuy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);
        
        add(pnlButton, BorderLayout.SOUTH);
    }
    
    private void luuDanhGia() {
        try {
            // Kiểm tra nếu không có hợp đồng nào
            if (cboHopDong.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "Không có hợp đồng nào để đánh giá!", 
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Lấy mã hợp đồng từ combobox
            String hopDongInfo = cboHopDong.getSelectedItem().toString();
            String maHD = hopDongInfo.split(" - ")[0];
            
            // Lấy số sao
            int diemSo = 5; // Mặc định 5 sao
            if (rad1Sao.isSelected()) diemSo = 1;
            else if (rad2Sao.isSelected()) diemSo = 2;
            else if (rad3Sao.isSelected()) diemSo = 3;
            else if (rad4Sao.isSelected()) diemSo = 4;
            
            // Lấy bình luận
            String binhLuan = txtBinhLuan.getText().trim();
            
            // Kiểm tra bình luận có trống không
            if (binhLuan.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Bạn chưa nhập nhận xét. Bạn có muốn tiếp tục lưu?",
                        "Xác nhận", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            
            // Lưu đánh giá
            boolean result = danhGiaController.addDanhGia(maHD, diemSo, binhLuan);
            
            if (result) {
                success = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm đánh giá thất bại!", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public boolean isSuccess() {
        return success;
    }
}