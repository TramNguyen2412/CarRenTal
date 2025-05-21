package ui.admin.QLKH;



import model.KhachHang;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

public class ChiTietKhachHangDialog extends JDialog {
    private KhachHang khachHang;
    private JLabel lblMaKH, lblMaTK, lblHoTen, lblSDT, lblEmail, lblCCCD, lblDiaChi, lblTongTienNo;
    private JButton btnDong;
    private final NumberFormat dinhDangTien = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    
    public ChiTietKhachHangDialog(Window parent, KhachHang khachHang) {
        super(parent, "Chi Tiết Khách Hàng", ModalityType.APPLICATION_MODAL);
        this.khachHang = khachHang;
        initComponents();
        loadKhachHangData();
    }
    
    private void initComponents() {
        setSize(500, 450);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        
        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        
        // Panel thông tin
        JPanel infoPanel = new JPanel(new GridLayout(8, 2, 10, 15));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thông tin chi tiết"),
            new EmptyBorder(10, 10, 10, 10)
        ));
        infoPanel.setBackground(Color.WHITE);
        
        // Tạo các label
        infoPanel.add(new JLabel("Mã KH:", JLabel.RIGHT));
        lblMaKH = new JLabel();
        infoPanel.add(lblMaKH);
        
        infoPanel.add(new JLabel("Mã TK:", JLabel.RIGHT));
        lblMaTK = new JLabel();
        infoPanel.add(lblMaTK);
        
        infoPanel.add(new JLabel("Họ Tên:", JLabel.RIGHT));
        lblHoTen = new JLabel();
        lblHoTen.setFont(new Font("Arial", Font.BOLD, 12));
        infoPanel.add(lblHoTen);
        
        infoPanel.add(new JLabel("SĐT:", JLabel.RIGHT));
        lblSDT = new JLabel();
        infoPanel.add(lblSDT);
        
        infoPanel.add(new JLabel("Email:", JLabel.RIGHT));
        lblEmail = new JLabel();
        infoPanel.add(lblEmail);
        
        infoPanel.add(new JLabel("CCCD:", JLabel.RIGHT));
        lblCCCD = new JLabel();
        infoPanel.add(lblCCCD);
        
        infoPanel.add(new JLabel("Địa Chỉ:", JLabel.RIGHT));
        lblDiaChi = new JLabel();
        infoPanel.add(lblDiaChi);
        
        infoPanel.add(new JLabel("Tổng Tiền Nợ:", JLabel.RIGHT));
        lblTongTienNo = new JLabel();
        lblTongTienNo.setFont(new Font("Arial", Font.BOLD, 12));
        lblTongTienNo.setForeground(Color.RED);
        infoPanel.add(lblTongTienNo);
        
        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        btnDong = new JButton("Đóng");
        btnDong.setBackground(new Color(108, 117, 125));
        btnDong.setForeground(Color.WHITE);
        btnDong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        buttonPanel.add(btnDong);
        
        // Thêm vào panel chính
        mainPanel.add(new JLabel("Chi tiết thông tin khách hàng:", JLabel.CENTER), BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Thêm vào dialog
        add(mainPanel);
    }
    
    private void loadKhachHangData() {
        lblMaKH.setText(khachHang.getMaKH());
        lblMaTK.setText(khachHang.getMaTK() != null ? khachHang.getMaTK() : "");
        lblHoTen.setText(khachHang.getHoTen());
        lblSDT.setText(khachHang.getSdt());
        lblEmail.setText(khachHang.getEmail() != null ? khachHang.getEmail() : "");
        lblCCCD.setText(khachHang.getCccd() != null ? khachHang.getCccd() : "");
        lblDiaChi.setText(khachHang.getDiaChi() != null ? khachHang.getDiaChi() : "");
        lblTongTienNo.setText(dinhDangTien.format(khachHang.getTongTienNo()));
    }
}
