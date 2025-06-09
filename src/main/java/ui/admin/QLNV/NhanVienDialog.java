//package ui.admin.QLNV;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Cursor;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.Font;
//import java.awt.Window;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.DefaultComboBoxModel;
//import javax.swing.GroupLayout;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JDialog;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//import javax.swing.border.CompoundBorder;
//import javax.swing.border.EmptyBorder;
//import javax.swing.border.LineBorder;
//
//import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
//
//import controller.NhanVienController;
//import model.NhanVien;
//
//public class NhanVienDialog extends JDialog {
//    // Các thành phần giao diện
//    private JTextField txtMaNV, txtHoTen, txtSDT, txtEmail;
//    private JComboBox<String> cboChucVu; // <-- Để nhập/chọn chức vụ
//    private NhanVien nhanVien;
//    private NhanVienPanel parent;
//    private NhanVienController nhanVienController;
//
//    public NhanVienDialog(Window owner, NhanVien nhanVien, NhanVienPanel parent) {
//        super(owner, nhanVien == null ? "Thêm nhân viên mới" : "Chỉnh sửa nhân viên", ModalityType.APPLICATION_MODAL);
//        this.nhanVien = nhanVien;
//        // Nếu nhanVien là null, khởi tạo đối tượng mới để tránh NullPointerException
//        if (this.nhanVien == null) {
//            this.nhanVien = new NhanVien();
//        }
//        this.parent = parent;
//        this.nhanVienController = new NhanVienController();
//
//        initComponents();
//        setResizable(false);
//    }
//
//    private void initComponents() {
//        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//        setBackground(new Color(245, 245, 245));
//
//        // PANEL CHÍNH
//        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
//        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
//        mainPanel.setBackground(new Color(245, 245, 245));
//
//        // FORM PANEL
//        JPanel formPanel = new JPanel();
//        formPanel.setBackground(Color.WHITE);
//        formPanel.setBorder(new CompoundBorder(
//                new LineBorder(new Color(220, 220, 220)),
//                new EmptyBorder(20, 20, 20, 20)));
//
//        GroupLayout layout = new GroupLayout(formPanel);
//        formPanel.setLayout(layout);
//        layout.setAutoCreateGaps(true);
//        layout.setAutoCreateContainerGaps(true);
//
//        // Các label
//        JLabel lblTitle = new JLabel(nhanVien.getMaNV() == null ? "THÊM NV" : "CHỈNH SỬA NV");
//        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 20));
//        lblTitle.setForeground(new Color(33, 150, 243));
//
//        JLabel lblMaNV = createLabel("Mã NV:", 16);
//        JLabel lblHoTen = createLabel("Họ tên:", 16);
//        JLabel lblSDT = createLabel("SĐT:", 16);
//        JLabel lblEmail = createLabel("Email:", 16);
//        JLabel lblChucVu = createLabel("Chức vụ:", 16);
//
//        // Các field
//        txtMaNV = createStyledTextField();
//        txtHoTen = createStyledTextField();
//        txtSDT = createStyledTextField();
//        txtEmail = createStyledTextField();
//
//        // Cập nhật txtMaNV dựa trên việc thêm mới hay chỉnh sửa
//        if (nhanVien != null && nhanVien.getMaNV() != null && !nhanVien.getMaNV().isEmpty()) {
//            // Chế độ chỉnh sửa: hiển thị MaNV và không cho sửa
//            txtMaNV.setText(nhanVien.getMaNV());
//            txtMaNV.setEditable(false);
//            txtMaNV.setBackground(new Color(230, 230, 230)); // Màu nền cho trường không sửa được
//            lblTitle.setText("CHỈNH SỬA THÔNG TIN NHÂN VIÊN");
//        } else {
//            // Chế độ thêm mới: hiển thị "Tự động tạo" và không cho sửa
//            txtMaNV.setText("Tự động tạo");
//            txtMaNV.setEditable(false);
//            txtMaNV.setBackground(new Color(230, 230, 230)); // Màu nền cho trường không sửa được
//            lblTitle.setText("THÊM NHÂN VIÊN MỚI");
//        }
//
//        // Combobox chức vụ
//        DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
//        nhanVienController.getAllChucVu()
//                .forEach(m::addElement);
//        cboChucVu = new JComboBox<>(m);
//        cboChucVu.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
//        cboChucVu.setPreferredSize(new Dimension(250, 35));
//
//        // Nhóm ngang
//        layout.setHorizontalGroup(
//                layout.createParallelGroup()
//                        .addComponent(lblTitle)
//                        .addGroup(layout.createSequentialGroup()
//                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
//                                        .addComponent(lblMaNV)
//                                        .addComponent(lblHoTen)
//                                        .addComponent(lblSDT)
//                                        .addComponent(lblEmail)
//                                        .addComponent(lblChucVu))
//                                .addGroup(layout.createParallelGroup()
//                                        .addComponent(txtMaNV)
//                                        .addComponent(txtHoTen)
//                                        .addComponent(txtSDT)
//                                        .addComponent(txtEmail)
//                                        .addComponent(cboChucVu))));
//
//        // Nhóm dọc
//        layout.setVerticalGroup(
//                layout.createSequentialGroup()
//                        .addComponent(lblTitle)
//                        .addGap(20)
//                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                .addComponent(lblMaNV)
//                                .addComponent(txtMaNV))
//                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                .addComponent(lblHoTen)
//                                .addComponent(txtHoTen))
//                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                .addComponent(lblSDT)
//                                .addComponent(txtSDT))
//                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                .addComponent(lblEmail)
//                                .addComponent(txtEmail))
//                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                .addComponent(lblChucVu)
//                                .addComponent(cboChucVu)));
//
//        // Nút Lưu/ Hủy
//        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
//        btnPanel.setBackground(new Color(245, 245, 245));
//        JButton btnSave = createStyledButton("Lưu", new Color(33, 150, 243));
//        JButton btnCancel = createStyledButton("Hủy", new Color(120, 120, 120));
//        btnPanel.add(btnSave);
//        btnPanel.add(btnCancel);
//
//        // Đưa vào mainPanel
//        mainPanel.add(formPanel, BorderLayout.CENTER);
//        mainPanel.add(btnPanel, BorderLayout.SOUTH);
//        setContentPane(mainPanel);
//
//        pack();
//        setLocationRelativeTo(getOwner());
//
//        // Sự kiện Lưu
//        btnSave.addActionListener(e -> {
//            // Kiểm tra dữ liệu nhập
//            if (txtHoTen.getText().trim().isEmpty()) {
//                JOptionPane.showMessageDialog(NhanVienDialog.this, "Vui lòng nhập họ tên!", "Lỗi",
//                        JOptionPane.ERROR_MESSAGE);
//                txtHoTen.requestFocus();
//                return;
//            }
//
//            if (txtSDT.getText().trim().isEmpty()) {
//                JOptionPane.showMessageDialog(NhanVienDialog.this, "Vui lòng nhập số điện thoại!", "Lỗi",
//                        JOptionPane.ERROR_MESSAGE);
//                txtSDT.requestFocus();
//                return;
//            }
//
//            // Kiểm tra định dạng số điện thoại
//            if (!txtSDT.getText().trim().matches("^0[0-9]{9}$")) {
//                JOptionPane.showMessageDialog(NhanVienDialog.this,
//                        "Số điện thoại không hợp lệ! Phải bắt đầu bằng số 0 và có 10 chữ số.", "Lỗi",
//                        JOptionPane.ERROR_MESSAGE);
//                txtSDT.requestFocus();
//                return;
//            }
//
//            if (txtEmail.getText().trim().isEmpty()) {
//                JOptionPane.showMessageDialog(NhanVienDialog.this, "Vui lòng nhập email!", "Lỗi",
//                        JOptionPane.ERROR_MESSAGE);
//                txtEmail.requestFocus();
//                return;
//            }
//
//            // Kiểm tra định dạng email
//            if (!txtEmail.getText().trim().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
//                JOptionPane.showMessageDialog(NhanVienDialog.this, "Email không hợp lệ!", "Lỗi",
//                        JOptionPane.ERROR_MESSAGE);
//                txtEmail.requestFocus();
//                return;
//            }
//
//            // Kiểm tra xem đã chọn chức vụ chưa
//            if (cboChucVu.getSelectedItem() == null
//                    || cboChucVu.getSelectedItem().toString().trim().isEmpty()) {
//                JOptionPane.showMessageDialog(NhanVienDialog.this, "Vui lòng chọn hoặc nhập chức vụ!", "Lỗi",
//                        JOptionPane.ERROR_MESSAGE);
//                cboChucVu.requestFocus();
//                return;
//            }
//
//            // Kiểm tra xem đây là thêm mới hay cập nhật
//            boolean isThemMoi = txtMaNV.getText().equals("Tự động tạo");
//
//            // Tạo đối tượng nhân viên từ dữ liệu nhập
//            NhanVien newNV = new NhanVien();
//            if (!isThemMoi) {
//                newNV.setMaNV(nhanVien.getMaNV()); // Lấy MaNV từ đối tượng nhân viên hiện tại khi cập nhật
//                newNV.setMaTK(nhanVien.getMaTK()); // Giữ nguyên MaTK khi cập nhật
//            }
//            // Không cần set MaTK khi thêm mới - sẽ được xử lý tự động
//
//            // Các trường còn lại được lấy từ form
//            newNV.setHoTen(txtHoTen.getText().trim());
//            newNV.setSdt(txtSDT.getText().trim());
//            newNV.setEmail(txtEmail.getText().trim());
//            newNV.setChucVu(cboChucVu.getSelectedItem().toString());
//
//            if (isThemMoi) {
//                // Thêm mới - không cần MaTK
//                boolean success = nhanVienController.addNhanVien(newNV);
//                if (success) {
//                    JOptionPane.showMessageDialog(NhanVienDialog.this,
//                            "Thêm nhân viên mới thành công!",
//                            "Thông báo",
//                            JOptionPane.INFORMATION_MESSAGE);
//                    dispose();
//                    parent.loadDataToTable();
//                } else {
//                    String errorMsg = nhanVienController.getErrorMessage();
//                    if (errorMsg != null && !errorMsg.isEmpty()) {
//                        JOptionPane.showMessageDialog(NhanVienDialog.this,
//                                "Thêm nhân viên mới thất bại! " + errorMsg,
//                                "Lỗi",
//                                JOptionPane.ERROR_MESSAGE);
//                    } else {
//                        JOptionPane.showMessageDialog(NhanVienDialog.this,
//                                "Thêm nhân viên mới thất bại!",
//                                "Lỗi",
//                                JOptionPane.ERROR_MESSAGE);
//                    }
//                }
//            } else {
//                // Cập nhật
//                boolean success = nhanVienController.updateNhanVien(newNV);
//                if (success) {
//                    JOptionPane.showMessageDialog(NhanVienDialog.this,
//                            "Cập nhật thông tin nhân viên thành công!",
//                            "Thông báo",
//                            JOptionPane.INFORMATION_MESSAGE);
//                    dispose();
//                    parent.loadDataToTable();
//                } else {
//                    String errorMsg = nhanVienController.getErrorMessage();
//                    if (errorMsg != null && !errorMsg.isEmpty()) {
//                        JOptionPane.showMessageDialog(NhanVienDialog.this,
//                                "Cập nhật thông tin nhân viên thất bại! " + errorMsg,
//                                "Lỗi",
//                                JOptionPane.ERROR_MESSAGE);
//                    } else {
//                        JOptionPane.showMessageDialog(NhanVienDialog.this,
//                                "Cập nhật thông tin nhân viên thất bại!",
//                                "Lỗi",
//                                JOptionPane.ERROR_MESSAGE);
//                    }
//                }
//            }
//        });
//
//        btnCancel.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                dispose();
//            }
//        });
//    }
//
//    // Phương thức hỗ trợ tạo các thành phần UI đẹp
//    private JLabel createLabel(String text, int size) {
//        JLabel label = new JLabel(text);
//        label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, size));
//        label.setForeground(new Color(70, 70, 70));
//        return label;
//    }
//
//    private JTextField createStyledTextField() {
//        JTextField textField = new JTextField();
//        textField.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
//        textField.setPreferredSize(new Dimension(250, 35));
//        return textField;
//    }
//
//    private JButton createStyledButton(String text, Color bgColor) {
//        JButton button = new JButton(text);
//        button.setBackground(bgColor);
//        button.setForeground(Color.WHITE);
//        button.setFocusPainted(false);
//        button.setBorderPainted(false);
//        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
//        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        button.setPreferredSize(new Dimension(150, 45));
//        return button;
//    }
//}


package ui.admin.QLNV;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import controller.NhanVienController;
import model.NhanVien;

public class NhanVienDialog extends JDialog {
    // Các thành phần giao diện
    private JTextField txtMaNV, txtHoTen, txtSDT, txtEmail;
    private JComboBox<String> cboChucVu;
    private NhanVien nhanVien;
    private NhanVienPanel parent;
    private NhanVienController nhanVienController;

    public NhanVienDialog(Window owner, NhanVien nhanVien, NhanVienPanel parent) {
        super(owner, nhanVien == null ? "Thêm nhân viên mới" : "Chỉnh sửa nhân viên", ModalityType.APPLICATION_MODAL);
        this.nhanVien = nhanVien;
        // Nếu nhanVien là null, khởi tạo đối tượng mới để tránh NullPointerException
        if (this.nhanVien == null) {
            this.nhanVien = new NhanVien();
        }
        this.parent = parent;
        this.nhanVienController = new NhanVienController();

        initComponents();
        setResizable(false);
    }

    private void initComponents() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(new Color(245, 245, 245));

        // PANEL CHÍNH
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(new Color(245, 245, 245));

        // FORM PANEL
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(20, 20, 20, 20)));

        GroupLayout layout = new GroupLayout(formPanel);
        formPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Các label
        JLabel lblTitle = new JLabel(nhanVien.getMaNV() == null ? "THÊM NHÂN VIÊN MỚI" : "CHỈNH SỬA THÔNG TIN NHÂN VIÊN");
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 20));
        lblTitle.setForeground(new Color(33, 150, 243));

        JLabel lblMaNV = createLabel("Mã NV:", 16);
        JLabel lblHoTen = createLabel("Họ tên:", 16);
        JLabel lblSDT = createLabel("SĐT:", 16);
        JLabel lblEmail = createLabel("Email:", 16);
        JLabel lblChucVu = createLabel("Chức vụ:", 16);

        // Các field
        txtMaNV = createStyledTextField();
        txtHoTen = createStyledTextField();
        txtSDT = createStyledTextField();
        txtEmail = createStyledTextField();

        // Combobox chức vụ
        DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
        List<String> chucVuList = nhanVienController.getAllChucVu();
        for (String cv : chucVuList) {
            m.addElement(cv);
        }
        cboChucVu = new JComboBox<>(m);
        cboChucVu.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
        cboChucVu.setPreferredSize(new Dimension(250, 35));

        // Cập nhật thông tin từ đối tượng nhân viên vào form
        if (nhanVien != null && nhanVien.getMaNV() != null && !nhanVien.getMaNV().isEmpty()) {
            // Chế độ chỉnh sửa: hiển thị MaNV và không cho sửa
            txtMaNV.setText(nhanVien.getMaNV());
            txtMaNV.setEditable(false);
            txtMaNV.setBackground(new Color(230, 230, 230)); // Màu nền cho trường không sửa được
            
            // Hiển thị thông tin nhân viên hiện có vào form
            txtHoTen.setText(nhanVien.getHoTen());
            txtSDT.setText(nhanVien.getSdt());
            txtEmail.setText(nhanVien.getEmail());
            
            // Chọn chức vụ tương ứng trong combobox
            cboChucVu.setSelectedItem(nhanVien.getChucVu());
        } else {
            // Chế độ thêm mới: hiển thị "Tự động tạo" và không cho sửa
            txtMaNV.setText("Tự động tạo");
            txtMaNV.setEditable(false);
            txtMaNV.setBackground(new Color(230, 230, 230)); // Màu nền cho trường không sửa được
        }

        // Nhóm ngang
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addComponent(lblTitle)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblMaNV)
                                        .addComponent(lblHoTen)
                                        .addComponent(lblSDT)
                                        .addComponent(lblEmail)
                                        .addComponent(lblChucVu))
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(txtMaNV)
                                        .addComponent(txtHoTen)
                                        .addComponent(txtSDT)
                                        .addComponent(txtEmail)
                                        .addComponent(cboChucVu))));

        // Nhóm dọc
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(lblTitle)
                        .addGap(20)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblMaNV)
                                .addComponent(txtMaNV))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblHoTen)
                                .addComponent(txtHoTen))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblSDT)
                                .addComponent(txtSDT))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblEmail)
                                .addComponent(txtEmail))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblChucVu)
                                .addComponent(cboChucVu)));

        // Nút Lưu/ Hủy
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        btnPanel.setBackground(new Color(245, 245, 245));
        JButton btnSave = createStyledButton("Lưu", new Color(33, 150, 243));
        JButton btnCancel = createStyledButton("Hủy", new Color(120, 120, 120));
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        // Đưa vào mainPanel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);

        pack();
        setLocationRelativeTo(getOwner());

        // Sự kiện Lưu
        btnSave.addActionListener(e -> {
            // Kiểm tra dữ liệu nhập
            if (txtHoTen.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(NhanVienDialog.this, "Vui lòng nhập họ tên!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                txtHoTen.requestFocus();
                return;
            }

            if (txtSDT.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(NhanVienDialog.this, "Vui lòng nhập số điện thoại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                txtSDT.requestFocus();
                return;
            }

            // Kiểm tra định dạng số điện thoại
            if (!txtSDT.getText().trim().matches("^0[0-9]{9}$")) {
                JOptionPane.showMessageDialog(NhanVienDialog.this,
                        "Số điện thoại không hợp lệ! Phải bắt đầu bằng số 0 và có 10 chữ số.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                txtSDT.requestFocus();
                return;
            }

            if (txtEmail.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(NhanVienDialog.this, "Vui lòng nhập email!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                txtEmail.requestFocus();
                return;
            }

            // Kiểm tra định dạng email
            if (!txtEmail.getText().trim().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                JOptionPane.showMessageDialog(NhanVienDialog.this, "Email không hợp lệ!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                txtEmail.requestFocus();
                return;
            }

            // Kiểm tra xem đã chọn chức vụ chưa
            if (cboChucVu.getSelectedItem() == null
                    || cboChucVu.getSelectedItem().toString().trim().isEmpty()) {
                JOptionPane.showMessageDialog(NhanVienDialog.this, "Vui lòng chọn hoặc nhập chức vụ!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                cboChucVu.requestFocus();
                return;
            }

            // Kiểm tra xem đây là thêm mới hay cập nhật
            boolean isThemMoi = txtMaNV.getText().equals("Tự động tạo");

            // Tạo đối tượng nhân viên từ dữ liệu nhập
            NhanVien newNV = new NhanVien();
            if (!isThemMoi) {
                newNV.setMaNV(nhanVien.getMaNV()); // Lấy MaNV từ đối tượng nhân viên hiện tại khi cập nhật
                newNV.setMaTK(nhanVien.getMaTK()); // Giữ nguyên MaTK khi cập nhật
            }
            // Không cần set MaTK khi thêm mới - sẽ được xử lý tự động

            // Các trường còn lại được lấy từ form
            newNV.setHoTen(txtHoTen.getText().trim());
            newNV.setSdt(txtSDT.getText().trim());
            newNV.setEmail(txtEmail.getText().trim());
            newNV.setChucVu(cboChucVu.getSelectedItem().toString());

            if (isThemMoi) {
                // Thêm mới - không cần MaTK
                boolean success = nhanVienController.addNhanVien(newNV);
                if (success) {
                    JOptionPane.showMessageDialog(NhanVienDialog.this,
                            "Thêm nhân viên mới thành công!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    parent.loadDataToTable();
                } else {
                    String errorMsg = nhanVienController.getErrorMessage();
                    if (errorMsg != null && !errorMsg.isEmpty()) {
                        JOptionPane.showMessageDialog(NhanVienDialog.this,
                                "Thêm nhân viên mới thất bại! " + errorMsg,
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(NhanVienDialog.this,
                                "Thêm nhân viên mới thất bại!",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                // Cập nhật
                boolean success = nhanVienController.updateNhanVien(newNV);
                if (success) {
                    JOptionPane.showMessageDialog(NhanVienDialog.this,
                            "Cập nhật thông tin nhân viên thành công!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    parent.loadDataToTable();
                } else {
                    String errorMsg = nhanVienController.getErrorMessage();
                    if (errorMsg != null && !errorMsg.isEmpty()) {
                        JOptionPane.showMessageDialog(NhanVienDialog.this,
                                "Cập nhật thông tin nhân viên thất bại! " + errorMsg,
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(NhanVienDialog.this,
                                "Cập nhật thông tin nhân viên thất bại!",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    // Phương thức hỗ trợ tạo các thành phần UI đẹp
    private JLabel createLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, size));
        label.setForeground(new Color(70, 70, 70));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
        textField.setPreferredSize(new Dimension(250, 35));
        return textField;
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