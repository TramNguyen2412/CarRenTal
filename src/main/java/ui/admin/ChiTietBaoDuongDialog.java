package ui.admin;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.BaoDuongController;
import java.awt.GridLayout;
import javax.swing.JLabel;
import model.ChiTietBaoDuong;
import model.DichVuBD;
import model.KhachHang;
import model.NhanVien;
import model.PhieuBaoDuong;
import model.Xe;

public class ChiTietBaoDuongDialog extends JDialog {
    private JTable tableInfo; // Bảng thông tin phiếu
    private JTable tableChiTiet;
    private DefaultTableModel modelInfo, modelChiTiet;
    private JButton btnClose;
    private PhieuBaoDuong phieu;
    private BaoDuongPanel parentPanel;
    private JLabel lblMaPhieu, lblXe, lblKhachHang, lblNgayBD, lblNhanVien, lblLoaiBD, lblTongTien;
    private final String[] CHI_TIET_COLUMNS = {"Mã DV", "Tên dịch vụ", "Đơn giá", "Số lượng", "Thành tiền"};
    private BaoDuongController baoDuongController = new BaoDuongController();


    public ChiTietBaoDuongDialog(Window owner, PhieuBaoDuong phieu, BaoDuongPanel parentPanel) {
        super(owner, "Chi Tiết Phiếu Bảo Dưỡng", ModalityType.APPLICATION_MODAL);
        this.phieu = phieu;
        this.parentPanel = parentPanel;
        initComponents();
        loadPhieuData();
        loadChiTietData();
        setSize(700, 500);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Bảng thông tin phiếu bảo dưỡng
        // Thay thế đoạn tạo modelInfo, tableInfo, infoScroll bằng:
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 8));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu bảo dưỡng"));
        lblMaPhieu = new JLabel();
        lblXe = new JLabel();
        lblKhachHang = new JLabel();
        lblNgayBD = new JLabel();
        lblNhanVien = new JLabel();
        lblLoaiBD = new JLabel();
        lblTongTien = new JLabel();
        infoPanel.add(new JLabel("Mã phiếu:")); infoPanel.add(lblMaPhieu);
        infoPanel.add(new JLabel("Xe:")); infoPanel.add(lblXe);
        infoPanel.add(new JLabel("Khách hàng:")); infoPanel.add(lblKhachHang);
        infoPanel.add(new JLabel("Ngày bảo dưỡng:")); infoPanel.add(lblNgayBD);
        infoPanel.add(new JLabel("Nhân viên:")); infoPanel.add(lblNhanVien);
        infoPanel.add(new JLabel("Loại bảo dưỡng:")); infoPanel.add(lblLoaiBD);
        infoPanel.add(new JLabel("Tổng tiền:")); infoPanel.add(lblTongTien);

        // Bảng danh sách dịch vụ bảo dưỡng
        modelChiTiet = new DefaultTableModel(CHI_TIET_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableChiTiet = new JTable(modelChiTiet);
        tableChiTiet.setRowHeight(28);
        tableChiTiet.setFont(new Font("Arial", Font.PLAIN, 14));
        tableChiTiet.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableChiTiet.setPreferredScrollableViewportSize(new Dimension(650, 200));
        JScrollPane chiTietScroll = new JScrollPane(tableChiTiet);
        chiTietScroll.setBorder(BorderFactory.createTitledBorder("Danh sách dịch vụ bảo dưỡng"));

        // Nút đóng
        btnClose = new JButton("Đóng");
        btnClose.setPreferredSize(new Dimension(100, 35));
        btnClose.setBackground(new Color(150, 150, 150));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Arial", Font.BOLD, 14));
        btnClose.addActionListener(e -> dispose());
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtn.add(btnClose);

        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(chiTietScroll, BorderLayout.CENTER);
        panel.add(pnlBtn, BorderLayout.SOUTH);
        getContentPane().add(panel);
    }

private void loadPhieuData() {
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    Xe xe = baoDuongController.getXeByMa(phieu.getMaXe());
    KhachHang kh = phieu.getMaKH() != null ? baoDuongController.getKhachHangByMa(phieu.getMaKH()) : null;
    NhanVien nv = baoDuongController.getNhanVienByMa(phieu.getMaNV());
    lblMaPhieu.setText(phieu.getMaBD());
    lblXe.setText(xe != null ? xe.getBienSo() : phieu.getMaXe());
    lblKhachHang.setText(kh != null ? kh.getHoTen() : "Không có");
    lblNgayBD.setText(df.format(phieu.getNgayBD()));
    lblNhanVien.setText(nv != null ? nv.getHoTen() : phieu.getMaNV());
    lblLoaiBD.setText(phieu.getLoaiBD());
    lblTongTien.setText(nf.format(phieu.getTongTienBD()) + " VNĐ");
}

    private void loadChiTietData() {
        modelChiTiet.setRowCount(0);
        List<ChiTietBaoDuong> chiTietList = baoDuongController.getChiTietByMaBD(phieu.getMaBD());
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        System.out.println("Số lượng chi tiết: " + chiTietList.size());
        System.out.println("MaBD truy vấn: " + phieu.getMaBD());
        for (ChiTietBaoDuong ct : chiTietList) {
            DichVuBD dv = baoDuongController.getDichVuBDById(ct.getMaDV());
            if (dv != null) {
                double thanhTien = dv.getGiaDV() * ct.getSoLuong();
                modelChiTiet.addRow(new Object[]{
                    ct.getMaDV(),
                    dv.getTenDV(),
                    nf.format(dv.getGiaDV()) + " VNĐ",
                    ct.getSoLuong(),
                    nf.format(thanhTien) + " VNĐ"
                });
            }
        }
    }
}