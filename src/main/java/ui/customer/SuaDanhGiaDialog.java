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
//import java.util.Map;
//import java.util.HashMap;
//import java.util.List;
//import model.ChiTietHD;
//public class SuaDanhGiaDialog extends JDialog {
//    private DanhGiaController danhGiaController;
//    private StarRatingComponent starRating;
//    private JTextArea binhLuanText;
//    private JButton btnLuu;
//    private JButton btnHuy;
//    private String maDG;
//    private Map<String, Object> hopDong;
//    private SimpleDateFormat dateFormat;
//    private DecimalFormat moneyFormat;
//    
//    public SuaDanhGiaDialog(JFrame parent, Map<String, Object> hopDong) {
//        super(parent, "Chỉnh Sửa Đánh Giá", true);
//        
//        this.danhGiaController = new DanhGiaController();
//        this.hopDong = hopDong;
//        this.maDG = (String) hopDong.get("MaDG");
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
//        JLabel titleLabel = new JLabel("Chỉnh Sửa Đánh Giá");
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
//        
//        // Load dữ liệu đánh giá cũ
//        loadRatingData();
//    }
//    
////    private JPanel createContractInfoPanel() {
////        JPanel panel = new JPanel();
////        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
////        panel.setBorder(BorderFactory.createCompoundBorder(
////            new MatteBorder(1, 1, 1, 1, new Color(230, 230, 230)),
////            new EmptyBorder(15, 15, 15, 15)
////        ));
////        panel.setBackground(new Color(250, 250, 250));
////        
////        // Thông tin hợp đồng
////        JLabel contractLabel = new JLabel("Thông tin hợp đồng #" + hopDong.get("MaHD"));
////        contractLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
////        
////        // Tạo panel với GridLayout để hiển thị thông tin
////        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 5));
////        infoPanel.setOpaque(false);
////        
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
////        addInfoRow(infoPanel, "Đã đánh giá:", dateFormat.format(hopDong.get("NgayDanhGia")));
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
//        String maHD = (String) hopDong.get("MaHD");
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
//        addInfoRow(infoPanel, "Đã đánh giá:", dateFormat.format(hopDong.get("NgayDanhGia")));
//
//        panel.add(contractLabel);
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(infoPanel);
//
//        return panel;
//    }
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
//               JLabel lblName = new JLabel(label);
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
//        btnLuu = new JButton("Lưu Thay Đổi");
//        btnLuu.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
//        btnLuu.setBackground(new Color(0, 150, 0));
//        btnLuu.setForeground(Color.WHITE);
//        btnLuu.setFocusPainted(false);
//        btnLuu.putClientProperty("JButton.buttonType", "roundRect");
//        btnLuu.addActionListener(e -> capNhatDanhGia());
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
//    private void loadRatingData() {
//        // Lấy thông tin đánh giá cũ từ hợp đồng và điền vào giao diện
//        int oldRating = (int) hopDong.get("DiemSo");
//        String oldComment = (String) hopDong.get("BinhLuan");
//        
//        starRating.setRating(oldRating);
//        if (oldComment != null) {
//            binhLuanText.setText(oldComment);
//        }
//    }
//    
//    private void capNhatDanhGia() {
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
//        boolean result = danhGiaController.capNhatDanhGia(maDG, rating, comment);
//        
//        if (result) {
//            JOptionPane.showMessageDialog(this,
//                "Đánh giá của bạn đã được cập nhật thành công!",
//                "Thành công",
//                JOptionPane.INFORMATION_MESSAGE);
//            dispose();
//        } else {
//            JOptionPane.showMessageDialog(this,
//                "Không thể cập nhật đánh giá: " + danhGiaController.getErrorMessage(),
//                "Lỗi",
//                JOptionPane.ERROR_MESSAGE);
//        }
//    }
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
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import controller.DanhGiaController;
import model.DanhGia;

public class SuaDanhGiaDialog extends JDialog {
    private JTextField txtMaDG, txtMaHD, txtTenXe, txtNgayDanhGia;
    private JRadioButton rad1Sao, rad2Sao, rad3Sao, rad4Sao, rad5Sao;
    private JTextArea txtBinhLuan;
    private JButton btnLuu, btnHuy;
    
    private DanhGiaController controller;
    private DanhGia danhGia;
    private boolean success = false;
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public SuaDanhGiaDialog(JFrame parent, boolean modal, DanhGiaController controller, DanhGia danhGia) {
        super(parent, "Sửa đánh giá", modal);
        this.controller = controller;
        this.danhGia = danhGia;
        initComponents();
        loadDanhGiaData();
    }
    
    private void initComponents() {
        setSize(600, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        
        // Panel tiêu đề - bỏ nền màu xanh
        JPanel pnlTitle = new JPanel();
        pnlTitle.setBackground(Color.WHITE);
        pnlTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        pnlTitle.setPreferredSize(new Dimension(600, 50));
        
        JLabel lblTitle = new JLabel("SỬA ĐÁNH GIÁ");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 102, 204));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        pnlTitle.add(lblTitle);
        
        add(pnlTitle, BorderLayout.NORTH);
        
        // Panel nội dung
        JPanel pnlContent = new JPanel(new GridBagLayout());
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlContent.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Mã đánh giá
        JLabel lblMaDG = new JLabel("Mã đánh giá:");
        lblMaDG.setFont(new Font("Arial", Font.BOLD, 14));
        pnlContent.add(lblMaDG, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        txtMaDG = new JTextField();
        txtMaDG.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMaDG.setEditable(false);
        pnlContent.add(txtMaDG, gbc);
        
        // Mã hợp đồng
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        
        JLabel lblMaHD = new JLabel("Hợp đồng:");
        lblMaHD.setFont(new Font("Arial", Font.BOLD, 14));
        pnlContent.add(lblMaHD, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        txtMaHD = new JTextField();
        txtMaHD.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMaHD.setEditable(false);
        pnlContent.add(txtMaHD, gbc);
        
        // Tên xe - không cần hiển thị vì đã gộp vào hợp đồng
        
        // Ngày đánh giá
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        
        JLabel lblNgayDanhGia = new JLabel("Ngày đánh giá:");
        lblNgayDanhGia.setFont(new Font("Arial", Font.BOLD, 14));
        pnlContent.add(lblNgayDanhGia, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        txtNgayDanhGia = new JTextField();
        txtNgayDanhGia.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNgayDanhGia.setEditable(false);
        pnlContent.add(txtNgayDanhGia, gbc);
        
        // Đánh giá sao
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        
        JLabel lblDanhGia = new JLabel("Đánh giá (sao):");
        lblDanhGia.setFont(new Font("Arial", Font.BOLD, 14));
        pnlContent.add(lblDanhGia, gbc);
        
        gbc.gridx = 1;
        
        JPanel pnlSao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSao.setBackground(Color.WHITE);
        
        ButtonGroup bgSao = new ButtonGroup();
        
        rad1Sao = new JRadioButton("1");
        rad1Sao.setFont(new Font("Arial", Font.PLAIN, 14));
        rad1Sao.setBackground(Color.WHITE);
        rad2Sao = new JRadioButton("2");
        rad2Sao.setFont(new Font("Arial", Font.PLAIN, 14));
        rad2Sao.setBackground(Color.WHITE);
        rad3Sao = new JRadioButton("3");
        rad3Sao.setFont(new Font("Arial", Font.PLAIN, 14));
        rad3Sao.setBackground(Color.WHITE);
        rad4Sao = new JRadioButton("4");
        rad4Sao.setFont(new Font("Arial", Font.PLAIN, 14));
        rad4Sao.setBackground(Color.WHITE);
        rad5Sao = new JRadioButton("5");
        rad5Sao.setFont(new Font("Arial", Font.PLAIN, 14));
        rad5Sao.setBackground(Color.WHITE);
        
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
        
        pnlContent.add(pnlSao, gbc);
        
        // Nhận xét
        gbc.gridx = 0;
        gbc.gridy++;
        
        JLabel lblBinhLuan = new JLabel("Nhận xét:");
        lblBinhLuan.setFont(new Font("Arial", Font.BOLD, 14));
        pnlContent.add(lblBinhLuan, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        txtBinhLuan = new JTextArea();
        txtBinhLuan.setFont(new Font("Arial", Font.PLAIN, 14));
        txtBinhLuan.setLineWrap(true);
        txtBinhLuan.setWrapStyleWord(true);
        JScrollPane scrollBinhLuan = new JScrollPane(txtBinhLuan);
        scrollBinhLuan.setPreferredSize(new Dimension(400, 150));
        pnlContent.add(scrollBinhLuan, gbc);
        
        add(pnlContent, BorderLayout.CENTER);
        
        // Panel nút
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButtons.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnlButtons.setBackground(Color.WHITE);
        
        btnLuu = new JButton("Lưu thay đổi");
        btnLuu.setFont(new Font("Arial", Font.BOLD, 14));
        btnLuu.setBackground(new Color(0, 102, 204));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFocusPainted(false);
        btnLuu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                capNhatDanhGia();
            }
        });
        
        btnHuy = new JButton("Hủy");
        btnHuy.setFont(new Font("Arial", Font.PLAIN, 14));
        btnHuy.setFocusPainted(false);
        btnHuy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        pnlButtons.add(btnLuu);
        pnlButtons.add(btnHuy);
        
        add(pnlButtons, BorderLayout.SOUTH);
    }
    
    private void loadDanhGiaData() {
        if (danhGia == null) return;
        
        txtMaDG.setText(danhGia.getMaDG());
        
        // Hiển thị thông tin hợp đồng với danh sách xe
        String hopDongInfo = controller.getThongTinHopDong(danhGia.getMaHD());
        txtMaHD.setText(hopDongInfo);
        
        txtNgayDanhGia.setText(dateFormat.format(danhGia.getNgayDanhGia()));
        
        // Chọn đánh giá sao
        switch (danhGia.getDiemSo()) {
            case 1: rad1Sao.setSelected(true); break;
            case 2: rad2Sao.setSelected(true); break;
            case 3: rad3Sao.setSelected(true); break;
            case 4: rad4Sao.setSelected(true); break;
            case 5: rad5Sao.setSelected(true); break;
        }
        
        txtBinhLuan.setText(danhGia.getBinhLuan());
    }
    
    private void capNhatDanhGia() {
        try {
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
            
            // Cập nhật đánh giá
            boolean result = controller.updateDanhGia(danhGia.getMaDG(), diemSo, binhLuan);
            
            if (result) {
                success = true;
                JOptionPane.showMessageDialog(this, "Cập nhật đánh giá thành công!", 
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật đánh giá thất bại!", 
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