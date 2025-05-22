package ui.customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DatXeDialog extends JDialog {
    private JTextField txtDiaChi;
    private JTextArea txtGhiChu;
    private JButton btnXacNhan;
    private JButton btnHuy;
    private boolean confirmed = false;
    private String diaChi = "";
    private String ghiChu = "";
    
    public DatXeDialog(JFrame parent) {
        super(parent, "Thông tin đặt xe", true);
        initComponents();
    }
    
    private void initComponents() {
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Panel chính
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Panel thông tin
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2, 1, 10, 15));
        
        // Địa chỉ giao xe
        JPanel panelDiaChi = new JPanel(new BorderLayout(0, 5));
        JLabel lblDiaChi = new JLabel("Địa chỉ giao xe:");
        lblDiaChi.setFont(new Font("Arial", Font.BOLD, 14));
        txtDiaChi = new JTextField();
        txtDiaChi.setFont(new Font("Arial", Font.PLAIN, 14));
        panelDiaChi.add(lblDiaChi, BorderLayout.NORTH);
        panelDiaChi.add(txtDiaChi, BorderLayout.CENTER);
        
        // Ghi chú
        JPanel panelGhiChu = new JPanel(new BorderLayout(0, 5));
        JLabel lblGhiChu = new JLabel("Ghi chú (tùy chọn):");
        lblGhiChu.setFont(new Font("Arial", Font.BOLD, 14));
        txtGhiChu = new JTextArea();
        txtGhiChu.setFont(new Font("Arial", Font.PLAIN, 14));
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtGhiChu);
        scrollPane.setPreferredSize(new Dimension(380, 80));
        panelGhiChu.add(lblGhiChu, BorderLayout.NORTH);
        panelGhiChu.add(scrollPane, BorderLayout.CENTER);
        
        // Thêm vào panel thông tin
        infoPanel.add(panelDiaChi);
        infoPanel.add(panelGhiChu);
        
        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnXacNhan = new JButton("Xác nhận đặt xe");
        btnHuy = new JButton("Hủy");
        
        // Styling cho nút xác nhận
        btnXacNhan.setBackground(new Color(0, 102, 204));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFont(new Font("Arial", Font.BOLD, 14));
        btnXacNhan.setPreferredSize(new Dimension(150, 35));
        
        // Styling cho nút hủy
        btnHuy.setFont(new Font("Arial", Font.PLAIN, 14));
        btnHuy.setPreferredSize(new Dimension(80, 35));
        
        btnXacNhan.addActionListener(e -> {
            if (validateForm()) {
                diaChi = txtDiaChi.getText().trim();
                ghiChu = txtGhiChu.getText().trim();
                confirmed = true;
                dispose();
            }
        });
        
        btnHuy.addActionListener(e -> dispose());
        
        buttonPanel.add(btnXacNhan);
        buttonPanel.add(btnHuy);
        
        // Thêm các thành phần vào panel chính
        mainPanel.add(new JLabel("Vui lòng nhập thông tin đặt xe:", JLabel.LEFT), BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Thiết lập focus mặc định
        SwingUtilities.invokeLater(() -> txtDiaChi.requestFocusInWindow());
    }
    
    private boolean validateForm() {
        if (txtDiaChi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập địa chỉ giao xe", 
                "Thông tin không đầy đủ", 
                JOptionPane.WARNING_MESSAGE);
            txtDiaChi.requestFocusInWindow();
            return false;
        }
        return true;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public String getDiaChi() {
        return diaChi;
    }
    
    public String getGhiChu() {
        return ghiChu;
    }
}