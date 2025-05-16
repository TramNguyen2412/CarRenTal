package ui.admin;

import model.Xe;
import util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class XeDetailDialog extends JDialog {
    private Xe xe;
    private XePanel parent;
    
    public XeDetailDialog(Window owner, Xe xe, XePanel parent) {
        super(owner, "Chi tiết xe", ModalityType.APPLICATION_MODAL);
        this.xe = xe;
        this.parent = parent;
        
        initComponents();
    }
    
    private void initComponents() {
        setSize(700, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel hình ảnh
        JPanel imagePanel = new JPanel(new BorderLayout());
        JLabel lblImage = new JLabel("", JLabel.CENTER);
        lblImage.setPreferredSize(new Dimension(250, 200));
        
//        if (xe.getHinhAnh() != null && !xe.getHinhAnh().isEmpty()) {
//           String imagePath = ImageUtil.getImageDirPath() + xe.getHinhAnh();
//            File imageFile = new File(imagePath);
//            if (imageFile.exists()) {
//                ImageIcon icon = new ImageIcon(imagePath);
//                // Resize image to fit the label
//                Image img = icon.getImage();
//                Image scaledImg = img.getScaledInstance(250, 200, Image.SCALE_SMOOTH);
//                lblImage.setIcon(new ImageIcon(scaledImg));
//            } else {
//                lblImage.setText("Không tìm thấy ảnh");
//            }
//        } else {
//            lblImage.setText("Không có ảnh");
//        }
        if (xe.getHinhAnh() != null && !xe.getHinhAnh().isEmpty()) {
            // Sử dụng ImageUtil để hiển thị ảnh
            ImageUtil.displayImage(xe.getHinhAnh(), lblImage);
        } else {
            lblImage.setText("Không có ảnh");
        }
        imagePanel.add(lblImage, BorderLayout.CENTER);
        imagePanel.setBorder(BorderFactory.createTitledBorder("Hình ảnh xe"));
        
        // Panel thông tin
        JPanel detailPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        
        // Hiển thị thông tin chi tiết xe
        detailPanel.add(createBoldLabel("Mã xe:"));
        detailPanel.add(new JLabel(xe.getMaXe()));
        
        detailPanel.add(createBoldLabel("Tên xe:"));
        detailPanel.add(new JLabel(xe.getTenXe()));
        
        detailPanel.add(createBoldLabel("Biển số:"));
        detailPanel.add(new JLabel(xe.getBienSo()));
        
        detailPanel.add(createBoldLabel("Số chỗ:"));
        detailPanel.add(new JLabel(String.valueOf(xe.getSoCho())));
        
        detailPanel.add(createBoldLabel("Hãng xe:"));
        detailPanel.add(new JLabel(xe.getHangXe()));
        
        detailPanel.add(createBoldLabel("Năm sản xuất:"));
        detailPanel.add(new JLabel(String.valueOf(xe.getNamSX())));
        
        detailPanel.add(createBoldLabel("Trạng thái:"));
        detailPanel.add(new JLabel(xe.getTrangThai()));
        
        detailPanel.add(createBoldLabel("Giá thuê/ngày:"));
        detailPanel.add(new JLabel(String.format("%,d VND", (int)xe.getGiaThueNgay())));
        
        // Tạo layout cho toàn bộ dialog
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(imagePanel, BorderLayout.NORTH);
        contentPanel.add(detailPanel, BorderLayout.CENTER);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnClose = new JButton("Đóng");
        JButton btnEdit = new JButton("Chỉnh sửa");
        
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnClose);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                parent.showXeDialog(xe);
            }
        });
    }
    
    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        return label;
    }
}