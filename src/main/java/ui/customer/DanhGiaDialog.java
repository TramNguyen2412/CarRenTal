//
//package ui.customer;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.Font;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.awt.Insets;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import javax.swing.BorderFactory;
//import javax.swing.ButtonGroup;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JDialog;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JRadioButton;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//import controller.DanhGiaController;
//import java.util.List;
//
//public class DanhGiaDialog extends JDialog {
//    private JComboBox<String> cboHopDong;
//    private JTextField txtMaKH, txtTenKhachHang;
//    private JRadioButton rad1Sao, rad2Sao, rad3Sao, rad4Sao, rad5Sao;
//    private JTextArea txtBinhLuan;
//    private JButton btnLuu, btnHuy;
//    
//    private DanhGiaController danhGiaController;
//    private boolean success = false;
//    private String maKHHienTai = "KH001"; // Giả sử lấy từ thông tin đăng nhập
//    
//    public DanhGiaDialog(JFrame parent, boolean modal, DanhGiaController controller) {
//        super(parent, "Thêm đánh giá mới", modal);
//        this.danhGiaController = controller;
//        initComponents();
//    }
//    
//    private void initComponents() {
//        setSize(600, 500);
//        setLocationRelativeTo(null);
//        setResizable(false);
//        setLayout(new BorderLayout(10, 10));
//        
//        // Panel thông tin
//        JPanel pnlThongTin = new JPanel(new GridBagLayout());
//        pnlThongTin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.anchor = GridBagConstraints.WEST;
//        gbc.insets = new Insets(5, 5, 5, 5);
//        
//        // Thông tin khách hàng
//        JLabel lblMaKH = new JLabel("Mã khách hàng:");
//        lblMaKH.setFont(new Font("Arial", Font.BOLD, 14));
//        pnlThongTin.add(lblMaKH, gbc);
//        
//        gbc.gridx = 1;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.weightx = 1.0;
//        
//        txtMaKH = new JTextField(20);
//        txtMaKH.setFont(new Font("Arial", Font.PLAIN, 14));
//        txtMaKH.setEditable(false);
//        txtMaKH.setText(maKHHienTai);
//        pnlThongTin.add(txtMaKH, gbc);
//        
//        gbc.gridx = 0;
//        gbc.gridy++;
//        gbc.weightx = 0;
//        
//        JLabel lblTenKH = new JLabel("Tên khách hàng:");
//        lblTenKH.setFont(new Font("Arial", Font.BOLD, 14));
//        pnlThongTin.add(lblTenKH, gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 1.0;
//        
//        // Giả sử lấy tên khách hàng từ cơ sở dữ liệu
//        txtTenKhachHang = new JTextField(20);
//        txtTenKhachHang.setFont(new Font("Arial", Font.PLAIN, 14));
//        txtTenKhachHang.setEditable(false);
//        txtTenKhachHang.setText("Nguyễn Văn A"); // Giả sử
//        pnlThongTin.add(txtTenKhachHang, gbc);
//        
//        gbc.gridx = 0;
//        gbc.gridy++;
//        gbc.weightx = 0;
//        
//        JLabel lblHopDong = new JLabel("Chọn hợp đồng:");
//        lblHopDong.setFont(new Font("Arial", Font.BOLD, 14));
//        pnlThongTin.add(lblHopDong, gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 1.0;
//        
//        // Lấy danh sách hợp đồng chưa đánh giá
//        List<String> danhSachMaHD = danhGiaController.getHopDongChuaDanhGia(maKHHienTai);
//        String[] dsHopDong = new String[danhSachMaHD.size()];
//        
//        for (int i = 0; i < danhSachMaHD.size(); i++) {
//            String maHD = danhSachMaHD.get(i);
//            dsHopDong[i] = danhGiaController.getThongTinHopDong(maHD);
//        }
//        
//        cboHopDong = new JComboBox<>(dsHopDong);
//        cboHopDong.setFont(new Font("Arial", Font.PLAIN, 14));
//        pnlThongTin.add(cboHopDong, gbc);
//        
//        gbc.gridx = 0;
//        gbc.gridy++;
//        gbc.weightx = 0;
//        
//        JLabel lblDanhGia = new JLabel("Đánh giá (Sao):");
//        lblDanhGia.setFont(new Font("Arial", Font.BOLD, 14));
//        pnlThongTin.add(lblDanhGia, gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 1.0;
//        
//        JPanel pnlSao = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        ButtonGroup bgSao = new ButtonGroup();
//        
//        rad1Sao = new JRadioButton("1");
//        rad1Sao.setFont(new Font("Arial", Font.PLAIN, 14));
//        rad2Sao = new JRadioButton("2");
//        rad2Sao.setFont(new Font("Arial", Font.PLAIN, 14));
//        rad3Sao = new JRadioButton("3");
//        rad3Sao.setFont(new Font("Arial", Font.PLAIN, 14));
//        rad4Sao = new JRadioButton("4");
//        rad4Sao.setFont(new Font("Arial", Font.PLAIN, 14));
//        rad5Sao = new JRadioButton("5", true);
//        rad5Sao.setFont(new Font("Arial", Font.PLAIN, 14));
//        
//        bgSao.add(rad1Sao);
//        bgSao.add(rad2Sao);
//        bgSao.add(rad3Sao);
//        bgSao.add(rad4Sao);
//        bgSao.add(rad5Sao);
//        
//        pnlSao.add(rad1Sao);
//        pnlSao.add(rad2Sao);
//        pnlSao.add(rad3Sao);
//        pnlSao.add(rad4Sao);
//        pnlSao.add(rad5Sao);
//        
//        pnlThongTin.add(pnlSao, gbc);
//        
//        gbc.gridx = 0;
//        gbc.gridy++;
//        gbc.weightx = 0;
//        
//        JLabel lblBinhLuan = new JLabel("Nhận xét:");
//        lblBinhLuan.setFont(new Font("Arial", Font.BOLD, 14));
//        pnlThongTin.add(lblBinhLuan, gbc);
//        
//        gbc.gridx = 1;
//        gbc.weightx = 1.0;
//        gbc.fill = GridBagConstraints.BOTH;
//        gbc.weighty = 1.0;
//        
//        txtBinhLuan = new JTextArea();
//        txtBinhLuan.setFont(new Font("Arial", Font.PLAIN, 14));
//        txtBinhLuan.setLineWrap(true);
//        txtBinhLuan.setWrapStyleWord(true);
//        JScrollPane scrollBinhLuan = new JScrollPane(txtBinhLuan);
//        scrollBinhLuan.setPreferredSize(new Dimension(300, 100));
//        pnlThongTin.add(scrollBinhLuan, gbc);
//        
//        add(pnlThongTin, BorderLayout.CENTER);
//        
//        // Panel nút
//        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        
//        btnLuu = new JButton("Lưu");
//        btnLuu.setFont(new Font("Arial", Font.PLAIN, 14));
//        btnLuu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                luuDanhGia();
//            }
//        });
//        
//        btnHuy = new JButton("Hủy");
//        btnHuy.setFont(new Font("Arial", Font.PLAIN, 14));
//        btnHuy.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                dispose();
//            }
//        });
//        
//        pnlButton.add(btnLuu);
//        pnlButton.add(btnHuy);
//        
//        add(pnlButton, BorderLayout.SOUTH);
//    }
//    
//    private void luuDanhGia() {
//        try {
//            // Kiểm tra nếu không có hợp đồng nào
//            if (cboHopDong.getItemCount() == 0) {
//                JOptionPane.showMessageDialog(this, "Không có hợp đồng nào để đánh giá!", 
//                        "Thông báo", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//            
//            // Lấy mã hợp đồng từ combobox
//            String hopDongInfo = cboHopDong.getSelectedItem().toString();
//            String maHD = hopDongInfo.split(" - ")[0];
//            
//            // Lấy số sao
//            int diemSo = 5; // Mặc định 5 sao
//            if (rad1Sao.isSelected()) diemSo = 1;
//            else if (rad2Sao.isSelected()) diemSo = 2;
//            else if (rad3Sao.isSelected()) diemSo = 3;
//            else if (rad4Sao.isSelected()) diemSo = 4;
//            
//            // Lấy bình luận
//            String binhLuan = txtBinhLuan.getText().trim();
//            
//            // Kiểm tra bình luận có trống không
//            if (binhLuan.isEmpty()) {
//                int confirm = JOptionPane.showConfirmDialog(this,
//                        "Bạn chưa nhập nhận xét. Bạn có muốn tiếp tục lưu?",
//                        "Xác nhận", JOptionPane.YES_NO_OPTION);
//                
//                if (confirm == JOptionPane.NO_OPTION) {
//                    return;
//                }
//            }
//            
//            // Lưu đánh giá
//            boolean result = danhGiaController.addDanhGia(maHD, diemSo, binhLuan);
//            
//            if (result) {
//                success = true;
//                dispose();
//            } else {
//                JOptionPane.showMessageDialog(this, "Thêm đánh giá thất bại!", 
//                        "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//            
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), 
//                    "Lỗi", JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
//    }
//    
//    public boolean isSuccess() {
//        return success;
//    }
//}