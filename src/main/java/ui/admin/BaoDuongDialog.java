package ui.admin;

import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;
import controller.BaoDuongController;
import model.*;

public class BaoDuongDialog extends JDialog {
    private JTextField txtMaBD;
    private JComboBox<String> cboXe, cboKhachHang, cboNhanVien, cboLoaiBD;
    private JDateChooser dateNgayBD;
    private JButton btnSave, btnCancel;
    private PhieuBaoDuong phieu;
    private BaoDuongPanel parentPanel;
    private BaoDuongController baoDuongController;
    private JTable tableChiTiet;
    private List<ChiTietBaoDuong> chiTietList = new ArrayList<>();
    private JLabel lblTongTien;
    private ChiTietBaoDuongTableModel chiTietTableModel;

    public BaoDuongDialog(Window owner, PhieuBaoDuong phieu, BaoDuongPanel parentPanel) {
        super(owner, phieu == null ? "Thêm Phiếu Bảo Dưỡng Mới" : "Cập Nhật Phiếu Bảo Dưỡng", ModalityType.APPLICATION_MODAL);
        this.phieu = phieu;
        this.parentPanel = parentPanel;
        this.baoDuongController = new BaoDuongController();
        setSize(1000, 700);
        setLocationRelativeTo(owner);
    }

}