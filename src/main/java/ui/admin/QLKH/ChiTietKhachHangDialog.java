package ui.admin.QLKH;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import model.KhachHang;

@SuppressWarnings("serial")
public class ChiTietKhachHangDialog extends JDialog {
    private KhachHang khachHang;
    private QuanLyKhachHangPanel parentPanel;
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public ChiTietKhachHangDialog(Window owner, KhachHang khachHang, QuanLyKhachHangPanel parentPanel) {
        super(owner, "Chi Tiết Khách Hàng", ModalityType.APPLICATION_MODAL);
        this.khachHang = khachHang;
        this.parentPanel = parentPanel;
        initComponents();
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(new Color(245, 245, 245));

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(20, 20, 20, 20)));

        GroupLayout layout = new GroupLayout(infoPanel);
        infoPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel lblInfoTitle = new JLabel("THÔNG TIN CHI TIẾT KHÁCH HÀNG");
        lblInfoTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 20));
        lblInfoTitle.setForeground(new Color(33, 150, 243));
        lblInfoTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblMaKHTitle = createLabel("Mã KH:");
        JLabel lblHoTenTitle = createLabel("Họ tên:");
        JLabel lblSDTTitle = createLabel("SĐT:");
        JLabel lblEmailTitle = createLabel("Email:");
        JLabel lblCCCDTitle = createLabel("CCCD:");
        JLabel lblDiaChiTitle = createLabel("Địa chỉ:");
        JLabel lblTongTienNoTitle = createLabel("Tổng tiền nợ:");
        JLabel lblMaTKTitle = createLabel("Mã tài khoản:");

        JLabel lblMaKH = createValueLabel(khachHang.getMaKH());
        JLabel lblHoTen = createValueLabel(khachHang.getHoTen());
        JLabel lblSDT = createValueLabel(khachHang.getSdt());
        JLabel lblEmail = createValueLabel(khachHang.getEmail() != null ? khachHang.getEmail() : "Chưa cập nhật");
        JLabel lblCCCD = createValueLabel(khachHang.getCccd() != null ? khachHang.getCccd() : "Chưa cập nhật");
        JLabel lblDiaChi = createValueLabel(khachHang.getDiaChi() != null ? khachHang.getDiaChi() : "Chưa cập nhật");
        JLabel lblTongTienNo = createValueLabel(currencyFormatter.format(khachHang.getTongTienNo()));
        lblTongTienNo.setForeground(khachHang.getTongTienNo() > 0 ? Color.RED : new Color(50, 50, 50));
        JLabel lblMaTK = createValueLabel(khachHang.getMaTK() != null ? khachHang.getMaTK() : "Chưa có");

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(lblInfoTitle)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lblMaKHTitle)
                                .addComponent(lblHoTenTitle)
                                .addComponent(lblSDTTitle)
                                .addComponent(lblEmailTitle)
                                .addComponent(lblCCCDTitle)
                                .addComponent(lblDiaChiTitle)
                                .addComponent(lblTongTienNoTitle)
                                .addComponent(lblMaTKTitle))
                        .addGap(20)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lblMaKH, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                .addComponent(lblHoTen)
                                .addComponent(lblSDT)
                                .addComponent(lblEmail)
                                .addComponent(lblCCCD)
                                .addComponent(lblDiaChi)
                                .addComponent(lblTongTienNo)
                                .addComponent(lblMaTK))));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(lblInfoTitle)
                .addGap(20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblMaKHTitle)
                        .addComponent(lblMaKH))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblHoTenTitle)
                        .addComponent(lblHoTen))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblSDTTitle)
                        .addComponent(lblSDT))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblEmailTitle)
                        .addComponent(lblEmail))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblCCCDTitle)
                        .addComponent(lblCCCD))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblDiaChiTitle)
                        .addComponent(lblDiaChi))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblTongTienNoTitle)
                        .addComponent(lblTongTienNo))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblMaTKTitle)
                        .addComponent(lblMaTK)));

        mainPanel.add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton btnEdit = createStyledButton("Chỉnh sửa", new Color(33, 150, 243));
        btnEdit.addActionListener(this::editKhachHang);

        JButton btnClose = createStyledButton("Đóng", new Color(120, 120, 120));
        btnClose.addActionListener(e -> dispose());

        buttonPanel.add(btnEdit);
        buttonPanel.add(btnClose);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14)); // Adjusted font
        label.setForeground(new Color(70, 70, 70));
        return label;
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14)); // Adjusted font
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
        button.setFocusPainted(false);
        return button;
    }

    private void editKhachHang(ActionEvent e) {
        dispose();
        SuaKhachHangDialog suaDialog = new SuaKhachHangDialog(
                (Window) SwingUtilities.getWindowAncestor(parentPanel),
                khachHang,
                parentPanel.getController());
        suaDialog.setVisible(true);
        if (suaDialog.isSuccessfullyUpdated()) {
            parentPanel.loadData();
        }
    }
}
