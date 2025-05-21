package ui.admin.QLNV;

import model.NhanVien;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NhanVienDetailDialog extends JDialog {
    private NhanVien nhanVien;
    private NhanVienPanel parent;
    
    public NhanVienDetailDialog(Window owner, NhanVien nhanVien, NhanVienPanel parent) {
        super(owner, "Chi tiết nhân viên", ModalityType.APPLICATION_MODAL);
        this.nhanVien = nhanVien;
        this.parent = parent;
        
        initComponents();
        setResizable(false);
    }
    
    private void initComponents() {
        // Tăng kích thước dialog
        setSize(600, 400);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        // Panel chính có màu nền nhẹ nhàng
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // === PANEL THÔNG TIN ===
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Sử dụng GroupLayout cho thông tin nhân viên
        GroupLayout layout = new GroupLayout(infoPanel);
        infoPanel.setLayout(layout);
        
        // Đặt các thuộc tính tự động tạo khoảng cách giữa các thành phần
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        // Tiêu đề phần thông tin
        JLabel lblTitle = new JLabel("THÔNG TIN CHI TIẾT");
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 20));
        lblTitle.setForeground(new Color(33, 150, 243));
        
        // Tạo các label với font lớn hơn
        JLabel lblMaNVTitle = createLabel("Mã nhân viên:", 16);
        JLabel lblHoTenTitle = createLabel("Họ tên:", 16);
        JLabel lblSDTTitle = createLabel("Số điện thoại:", 16);
        JLabel lblEmailTitle = createLabel("Email:", 16);
        JLabel lblChucVuTitle = createLabel("Chức vụ:", 16);
        
        // Tạo các label giá trị với font lớn hơn
        JLabel lblMaNV = createValueLabel(nhanVien.getMaNV());
        JLabel lblHoTen = createValueLabel(nhanVien.getHoTen());
        JLabel lblSDT = createValueLabel(nhanVien.getSdt());
        JLabel lblEmail = createValueLabel(nhanVien.getEmail());
        JLabel lblChucVu = createValueLabel(nhanVien.getChucVu());
        
        // Thiết lập nhóm ngang cho GroupLayout
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(lblTitle)
                .addComponent(lblMaNVTitle)
                .addComponent(lblHoTenTitle)
                .addComponent(lblSDTTitle)
                .addComponent(lblEmailTitle)
                .addComponent(lblChucVuTitle));
        
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(lblMaNV)
                .addComponent(lblHoTen)
                .addComponent(lblSDT)
                .addComponent(lblEmail)
                .addComponent(lblChucVu));
        
        layout.setHorizontalGroup(hGroup);
        
        // Thiết lập nhóm dọc cho GroupLayout
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        
        vGroup.addComponent(lblTitle);
        vGroup.addGap(20);
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblMaNVTitle)
                .addComponent(lblMaNV));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblHoTenTitle)
                .addComponent(lblHoTen));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblSDTTitle)
                .addComponent(lblSDT));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblEmailTitle)
                .addComponent(lblEmail));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblChucVuTitle)
                .addComponent(lblChucVu));
        
        layout.setVerticalGroup(vGroup);
        
        // === PANEL CHO CÁC NÚT THAO TÁC ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        
        // Tạo các nút lớn hơn và đẹp hơn
        JButton btnEdit = createStyledButton("Chỉnh sửa", new Color(33, 150, 243));
        JButton btnClose = createStyledButton("Đóng", new Color(120, 120, 120));
        
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnClose);
        
        mainPanel.add(infoPanel, BorderLayout.CENTER);
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
                parent.showNhanVienDialog(nhanVien);
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
