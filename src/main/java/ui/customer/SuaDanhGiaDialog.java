

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