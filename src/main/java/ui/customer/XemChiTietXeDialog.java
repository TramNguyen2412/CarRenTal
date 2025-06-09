
package ui.customer;

import model.GioXe;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import util.ImageUtil;

public class XemChiTietXeDialog extends JDialog {
    private GioXe gioXe;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private DecimalFormat moneyFormat = new DecimalFormat("#,###");
    
    public XemChiTietXeDialog(JFrame parent, GioXe gioXe) {
        super(parent, "Chi tiết xe trong giỏ xe", true);
        this.gioXe = gioXe;
        
        initComponents();
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(25, 0));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Panel trái: Hình ảnh xe
        JPanel leftPanel = new JPanel(new BorderLayout(0, 15));
        leftPanel.setBackground(Color.WHITE);
        
        // Panel hình ảnh - thay RoundedPanel bằng JPanel thường
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(380, 280));
        imagePanel.setBackground(new Color(245, 245, 245));
        imagePanel.setBorder(new LineBorder(new Color(230, 230, 230), 1, true));
        
        // Hiển thị ảnh xe hoặc tên xe
        if (gioXe.getHinhAnh() != null && !gioXe.getHinhAnh().isEmpty()) {
            try {
                JLabel imageLabel = new JLabel();
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imageLabel.setVerticalAlignment(SwingConstants.CENTER);
                imagePanel.add(imageLabel, BorderLayout.CENTER);
                
                SwingUtilities.invokeLater(() -> {
                    int panelWidth = imagePanel.getWidth();
                    int panelHeight = imagePanel.getHeight();
                    
                    if (panelWidth <= 10) panelWidth = 380;
                    if (panelHeight <= 10) panelHeight = 280;
                    
                    try {
                        ImageUtil.displayImageWithFixedSize(gioXe.getHinhAnh(), imageLabel, panelWidth, panelHeight);
                    } catch (Exception e) {
                        JLabel noImageLabel = new JLabel(gioXe.getTenXe(), SwingConstants.CENTER);
                        noImageLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
                        imagePanel.add(noImageLabel, BorderLayout.CENTER);
                    }
                });
            } catch (Exception e) {
                JLabel noImageLabel = new JLabel(gioXe.getTenXe(), SwingConstants.CENTER);
                noImageLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
                imagePanel.add(noImageLabel, BorderLayout.CENTER);
            }
        } else {
            JLabel noImageLabel = new JLabel(gioXe.getTenXe(), SwingConstants.CENTER);
            noImageLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
            imagePanel.add(noImageLabel, BorderLayout.CENTER);
        }
        
        leftPanel.add(imagePanel, BorderLayout.NORTH);
        
        // Thông số kỹ thuật - thay RoundedPanel bằng JPanel thường
        JPanel specPanel = new JPanel(new BorderLayout());
        specPanel.setBackground(Color.WHITE);
        specPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel specTitleLabel = new JLabel("THÔNG SỐ KỸ THUẬT");
        specTitleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
        specTitleLabel.setForeground(new Color(60, 60, 60));
        specTitleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JPanel specContent = new JPanel(new GridLayout(3, 2, 20, 15));
        specContent.setOpaque(false);
        
        addSpecDetail(specContent, "Hãng xe", gioXe.getHangXe());
        addSpecDetail(specContent, "Biển số", gioXe.getBienSo());
        addSpecDetail(specContent, "Số chỗ", String.valueOf(gioXe.getSoCho()) + " chỗ");
        
        specPanel.add(specTitleLabel, BorderLayout.NORTH);
        specPanel.add(specContent, BorderLayout.CENTER);
        leftPanel.add(specPanel, BorderLayout.CENTER);
        
        // Panel phải: Thông tin đặt xe
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        // Tên xe và giá
        JLabel nameLabel = new JLabel(gioXe.getTenXe());
        nameLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 32));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel priceLabel = new JLabel(moneyFormat.format(gioXe.getGiaThueNgay()) + " VND/ngày");
        priceLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        priceLabel.setForeground(new Color(255, 140, 0));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Panel thông tin thuê xe - thay RoundedPanel bằng JPanel thường
        JPanel bookingPanel = new JPanel(new BorderLayout(0, 15));
        bookingPanel.setBackground(Color.WHITE);
        bookingPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        bookingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel bookingTitle = new JLabel("THÔNG TIN THUÊ XE");
        bookingTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
        bookingTitle.setForeground(new Color(60, 60, 60));
        
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        infoPanel.setOpaque(false);
        
        addBookingInfoRow(infoPanel, "Ngày bắt đầu:", dateFormat.format(gioXe.getNgayBatDau()));
        addBookingInfoRow(infoPanel, "Ngày kết thúc:", dateFormat.format(gioXe.getNgayKetThuc()));
        addBookingInfoRow(infoPanel, "Số ngày thuê:", gioXe.getSoNgayThue() + " ngày");
        addBookingInfoRow(infoPanel, "Thành tiền:", moneyFormat.format(gioXe.getThanhTien()) + " VND");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        
        JButton btnDong = new JButton("Đóng");
        btnDong.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
        btnDong.setBackground(new Color(108, 117, 125));
        btnDong.setForeground(Color.WHITE);
        btnDong.setFocusPainted(false);
        btnDong.putClientProperty("JButton.buttonType", "roundRect");
        btnDong.addActionListener(e -> dispose());
        
        buttonPanel.add(btnDong);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.add(infoPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(buttonPanel);
        
        bookingPanel.add(bookingTitle, BorderLayout.NORTH);
        bookingPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Mô tả
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText("Xe " + gioXe.getTenXe() + " với " + gioXe.getSoCho() + 
                               " chỗ ngồi. Xe được bảo dưỡng định kỳ, đảm bảo chất lượng và an toàn. " +
                               "Phù hợp cho gia đình hoặc nhóm bạn đi du lịch, công tác.");
        descriptionArea.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionArea.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // Thêm các panel vào panel phải
        rightPanel.add(nameLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(priceLabel);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(descriptionArea);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(bookingPanel);
        
        // Thêm các panel vào panel chính
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void addBookingInfoRow(JPanel panel, String label, String value) {
        JLabel lblName = new JLabel(label);
        lblName.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
        
        panel.add(lblName);
        panel.add(lblValue);
    }
    
    private void addSpecDetail(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout(10, 0));
        rowPanel.setOpaque(false);
        
        JLabel lblSpec = new JLabel(label + ":");
        lblSpec.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
        
        JLabel valSpec = new JLabel(value);
        valSpec.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
        
        rowPanel.add(lblSpec, BorderLayout.WEST);
        rowPanel.add(valSpec, BorderLayout.CENTER);
        
        panel.add(rowPanel);
    }
}