
package ui.admin.QLXe;

import model.Xe;
import util.ImageUtil;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class XeDetailDialog extends JDialog {
    private Xe xe;
    private XePanel parent;
    
    public XeDetailDialog(Window owner, Xe xe, XePanel parent) {
        super(owner, "Chi tiết xe", ModalityType.APPLICATION_MODAL);
        this.xe = xe;
        this.parent = parent;
        
        initComponents();
        setResizable(false);
    }
    
    private void initComponents() {
        // Tăng kích thước dialog
        setSize(900, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        // Panel chính có màu nền nhẹ nhàng
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
      //  mainPanel.setBackground(new Color(245, 245, 245));
        
        // === PANEL THÔNG TIN BÊN TRÁI ===
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Sử dụng GroupLayout cho thông tin xe
        GroupLayout layout = new GroupLayout(infoPanel);
        infoPanel.setLayout(layout);
        
        // Đặt các thuộc tính tự động tạo khoảng cách giữa các thành phần
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        // Tiêu đề phần thông tin
        JLabel lblTitle = new JLabel("THÔNG TIN CHI TIẾT");
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 20));
      //  lblTitle.setForeground(new Color(40, 40, 40));
        lblTitle.setForeground(new Color(33, 150, 243));
        // Tạo các label với font lớn hơn
        JLabel lblMaXeTitle = createLabel("Mã xe:", 16);
        JLabel lblTenXeTitle = createLabel("Tên xe:", 16);
        JLabel lblBienSoTitle = createLabel("Biển số:", 16);
        JLabel lblSoChoTitle = createLabel("Số chỗ:", 16);
        JLabel lblHangXeTitle = createLabel("Hãng xe:", 16);
        JLabel lblNamSXTitle = createLabel("Năm sản xuất:", 16);
        JLabel lblTrangThaiTitle = createLabel("Trạng thái:", 16);
        JLabel lblGiaThueTitle = createLabel("Giá thuê/ngày:", 16);
        
        // Tạo các label giá trị với font lớn hơn
        JLabel lblMaXe = createValueLabel(xe.getMaXe());
        JLabel lblTenXe = createValueLabel(xe.getTenXe());
        JLabel lblBienSo = createValueLabel(xe.getBienSo());
        JLabel lblSoCho = createValueLabel(String.valueOf(xe.getSoCho()));
        JLabel lblHangXe = createValueLabel(xe.getHangXe());
        JLabel lblNamSX = createValueLabel(String.valueOf(xe.getNamSX()));
        
        // Tạo label trạng thái với màu sắc tùy thuộc vào trạng thái
        JLabel lblTrangThai = createValueLabel(xe.getTrangThai());
        if (xe.getTrangThai().equals("Sẵn sàng")) {
            lblTrangThai.setForeground(new Color(0, 150, 136));
        } else if (xe.getTrangThai().equals("Đang thuê")) {
            lblTrangThai.setForeground(new Color(33, 150, 243));
        } else {
            lblTrangThai.setForeground(new Color(244, 67, 54));
        }
        
        // Label giá thuê với định dạng tiền tệ
        JLabel lblGiaThue = createValueLabel(String.format("%,d VND", (int)xe.getGiaThueNgay()));
        lblGiaThue.setForeground(new Color(213, 0, 0));
        
        // Thiết lập nhóm ngang cho GroupLayout
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(lblTitle)
                .addComponent(lblMaXeTitle)
                .addComponent(lblTenXeTitle)
                .addComponent(lblBienSoTitle)
                .addComponent(lblSoChoTitle)
                .addComponent(lblHangXeTitle)
                .addComponent(lblNamSXTitle)
                .addComponent(lblTrangThaiTitle)
                .addComponent(lblGiaThueTitle));
        
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(lblMaXe)
                .addComponent(lblTenXe)
                .addComponent(lblBienSo)
                .addComponent(lblSoCho)
                .addComponent(lblHangXe)
                .addComponent(lblNamSX)
                .addComponent(lblTrangThai)
                .addComponent(lblGiaThue));
        
        layout.setHorizontalGroup(hGroup);
        
        // Thiết lập nhóm dọc cho GroupLayout
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        
        vGroup.addComponent(lblTitle);
        vGroup.addGap(20);
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblMaXeTitle)
                .addComponent(lblMaXe));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblTenXeTitle)
                .addComponent(lblTenXe));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblBienSoTitle)
                .addComponent(lblBienSo));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblSoChoTitle)
                .addComponent(lblSoCho));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblHangXeTitle)
                .addComponent(lblHangXe));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblNamSXTitle)
                .addComponent(lblNamSX));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblTrangThaiTitle)
                .addComponent(lblTrangThai));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblGiaThueTitle)
                .addComponent(lblGiaThue));
        
        layout.setVerticalGroup(vGroup);
        
        // === PANEL HÌNH ẢNH BÊN PHẢI ===
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Tiêu đề phần hình ảnh
        JLabel lblImageTitle = new JLabel("HÌNH ẢNH XE", JLabel.CENTER);
        lblImageTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
        lblImageTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Label hiển thị hình ảnh với kích thước lớn hơn
        JLabel lblImage = new JLabel("", JLabel.CENTER);
        lblImage.setPreferredSize(new Dimension(400, 300));
        lblImage.setMinimumSize(new Dimension(400, 300));
        
        // Đặt border cho ảnh để nổi bật
        lblImage.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240), 2));
        
        // ===== SỬA THEO YÊU CẦU: Sử dụng ImageUtil.displayImage() =====
        if (xe.getHinhAnh() != null && !xe.getHinhAnh().isEmpty()) {
            // Đặt kích thước cố định cho label trước khi hiển thị ảnh
            lblImage.setSize(400, 300);
            // Sử dụng phương thức displayImage từ ImageUtil
            ImageUtil.displayImage(xe.getHinhAnh(), lblImage);
        } else {
            lblImage.setText("Không có ảnh");
            lblImage.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 16));
        }
        
        imagePanel.add(lblImageTitle, BorderLayout.NORTH);
        imagePanel.add(lblImage, BorderLayout.CENTER);
        
        // === PANEL CHO CÁC NÚT THAO TÁC ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
     //   buttonPanel.setBackground(new Color(245, 245, 245));
        
        // Tạo các nút lớn hơn và đẹp hơn
        JButton btnEdit = createStyledButton("Chỉnh sửa", new Color(33, 150, 243));
        JButton btnClose = createStyledButton("Đóng", new Color(120, 120, 120));
        
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnClose);
        
        // Thêm các panel vào panel chính với giao diện hai cột
        JPanel contentPanel = new JPanel(new GridBagLayout());
      //  contentPanel.setBackground(new Color(245, 245, 245));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        contentPanel.add(infoPanel, gbc);
        
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 0, 0);
        contentPanel.add(imagePanel, gbc);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Thêm sự kiện cho các nút
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
    
    private JLabel createLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, size));
        label.setForeground(new Color(70, 70, 70));
        return label;
    }
    
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 45));
        return button;
    }
}