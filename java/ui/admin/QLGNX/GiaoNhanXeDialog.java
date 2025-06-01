package ui.admin.QLGNX;

import controller.GiaoNhanXeController;
import controller.HopDongController;
import controller.NhanVienController;
import controller.XeController;
import model.GiaoNhanXe;
import model.HopDong;
import model.NhanVien;
import model.Xe;
import model.ChiTietHD; // Assuming ChiTietHD is in the model package

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class GiaoNhanXeDialog extends JDialog {
    private GiaoNhanXeController giaoNhanXeController;
    private HopDongController hopDongController;
    private XeController xeController;
    private NhanVienController nhanVienController;

    private GiaoNhanXe currentGiaoNhanXe;
    private GiaoNhanXePanel parentPanel;
    private boolean isEditMode;
    private boolean readOnly;

    private JTextField txtMaGiaoNhan;
    private JComboBox<String> cboMaHD;
    private JComboBox<String> cboMaXe;
    private JComboBox<String> cboMaNV;
    private JTextArea txtAreaTrangThaiXe;
    private JTextArea txtAreaGhiChu;
    private JComboBox<String> cboTrangThaiGN;
    private JButton btnSave, btnCancel;
    
    private List<HopDong> danhSachHopDong;
    private List<Xe> danhSachXe;
    private List<NhanVien> danhSachNhanVien;

    private static final Color PRIMARY_COLOR = new Color(41, 121, 255);
    private static final Color ACCENT_COLOR = new Color(108, 117, 125);
    private static final Color LABEL_COLOR = new Color(70, 70, 70);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Color READ_ONLY_BACKGROUND = new Color(235, 235, 235);
    private static final Color BORDER_COLOR_FIELD = new Color(204, 204, 204);

    public GiaoNhanXeDialog(Frame owner, GiaoNhanXe giaoNhanXe, GiaoNhanXePanel parentPanel, boolean readOnly) {
        super(owner, true);
        this.currentGiaoNhanXe = giaoNhanXe;
        this.parentPanel = parentPanel;
        this.isEditMode = (giaoNhanXe != null && giaoNhanXe.getMaGiaoNhan() != null && !giaoNhanXe.getMaGiaoNhan().isEmpty());
        this.readOnly = readOnly;

        this.giaoNhanXeController = new GiaoNhanXeController();
        this.hopDongController = new HopDongController();
        this.xeController = new XeController();
        this.nhanVienController = new NhanVienController();

        setTitle(isEditMode ? (readOnly ? "Chi Tiết Giao Nhận Xe" : "Sửa Thông Tin Giao Nhận Xe") : "Thêm Giao Nhận Xe Mới");
        initComponents();
        loadComboBoxData();
        if (isEditMode) {
            loadDataToForm();
        }
        if (readOnly) {
            setFieldsReadOnly();
        }
        pack();
        setMinimumSize(new Dimension(600, getHeight()));
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(10,10));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(15,20,15,20));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createStyledLabel("Mã Giao Nhận:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMaGiaoNhan = createStyledTextField(25);
        txtMaGiaoNhan.setEditable(false);
        txtMaGiaoNhan.setBackground(READ_ONLY_BACKGROUND);
        formPanel.add(txtMaGiaoNhan, gbc);
        gbc.weightx = 0;

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createStyledLabel("Mã Hợp Đồng (*):"), gbc);
        gbc.gridx = 1;
        cboMaHD = createStyledComboBox();
        formPanel.add(cboMaHD, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createStyledLabel("Mã Xe (*):"), gbc);
        gbc.gridx = 1;
        cboMaXe = createStyledComboBox();
        formPanel.add(cboMaXe, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createStyledLabel("Nhân Viên Giao/Nhận (*):"), gbc);
        gbc.gridx = 1;
        cboMaNV = createStyledComboBox();
        formPanel.add(cboMaNV, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(createStyledLabel("Trạng Thái Xe Khi Giao/Nhận (*):"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        txtAreaTrangThaiXe = createStyledTextArea(4, 25);
        JScrollPane scrollTrangThaiXe = new JScrollPane(txtAreaTrangThaiXe);
        scrollTrangThaiXe.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formPanel.add(scrollTrangThaiXe, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(createStyledLabel("Ghi Chú Thêm:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        txtAreaGhiChu = createStyledTextArea(3, 25);
        JScrollPane scrollGhiChu = new JScrollPane(txtAreaGhiChu);
        scrollGhiChu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formPanel.add(scrollGhiChu, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(createStyledLabel("Trạng Thái Giao Nhận (*):"), gbc);
        gbc.gridx = 1;
        cboTrangThaiGN = createStyledComboBox(new String[]{"Đã giao", "Đã nhận về"});
        formPanel.add(cboTrangThaiGN, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10,0,0,0));

        btnSave = new JButton("Lưu Thay Đổi");
        styleDialogButton(btnSave, PRIMARY_COLOR);
        btnSave.addActionListener(e -> saveGiaoNhanXe());

        btnCancel = new JButton("Hủy Bỏ");
        styleDialogButton(btnCancel, ACCENT_COLOR);
        btnCancel.addActionListener(e -> dispose());
        
        if (!readOnly) {
            buttonPanel.add(btnSave);
        }
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(LABEL_COLOR);
        return label;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(FIELD_FONT);
        textField.setPreferredSize(new Dimension(200, 32));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR_FIELD, 1),
            new EmptyBorder(0, 8, 0, 8) 
        ));
        return textField;
    }
    
    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        return styleComboBox(comboBox);
    }
    
    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        return styleComboBox(comboBox);
    }

    private JComboBox<String> styleComboBox(JComboBox<String> comboBox){
        comboBox.setFont(FIELD_FONT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setPreferredSize(new Dimension(200, 32));
        comboBox.setBorder(BorderFactory.createLineBorder(BORDER_COLOR_FIELD, 1));
        return comboBox;
    }

    private JTextArea createStyledTextArea(int rows, int columns) {
        JTextArea textArea = new JTextArea(rows, columns);
        textArea.setFont(FIELD_FONT);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR_FIELD, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        return textArea;
    }
    
    private void styleDialogButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(140, 38));
    }

    private void loadComboBoxData() {
        danhSachHopDong = hopDongController.getAllHopDong(); 
        cboMaHD.addItem("--- Chọn Mã Hợp Đồng ---");
        if (danhSachHopDong != null) {
            for (HopDong hd : danhSachHopDong) {
                if (hd == null) continue; // Defensive check

                // Use direct fields from HopDong.java for customer info
                String khTen = hd.getTenKH() != null ? hd.getTenKH() : "N/A";
                String khMa = hd.getMaKH() != null ? hd.getMaKH() : "N/A";
                String khInfo = khTen + " (" + khMa + ")";

                // For Xe info, use information from danhSachXeThue
                String xeDisplayInfo = "N/A";
                if (hd.getDanhSachXeThue() != null && !hd.getDanhSachXeThue().isEmpty()) {
                    ChiTietHD firstDetail = hd.getDanhSachXeThue().get(0);
                    if (firstDetail != null && firstDetail.getMaXe() != null) {
                        // To get BienSoXe, ChiTietHD would need a getXe() returning Xe, or HopDongController should populate it
                        // For simplicity, we'll just use MaXe from the first detail.
                        // If you have Xe object in ChiTietHD: Xe xe = firstDetail.getXe(); if (xe != null) xeDisplayInfo = xe.getBienSoXe();
                        xeDisplayInfo = "Xe chính: " + firstDetail.getMaXe(); 
                        if (hd.getDanhSachXeThue().size() > 1) {
                             xeDisplayInfo += " (+" + (hd.getDanhSachXeThue().size() -1) + " xe khác)";
                        }
                    } else {
                        xeDisplayInfo = "Không có thông tin xe chi tiết";
                    }
                } else {
                    xeDisplayInfo = "Chưa có xe nào trong HĐ";
                }
                cboMaHD.addItem(hd.getMaHD() + " | KH: " + khInfo + " | Xe: " + xeDisplayInfo);
            }
        }

        danhSachXe = xeController.getAllXe(); 
        cboMaXe.addItem("--- Chọn Mã Xe ---");
        if (danhSachXe != null) {
            for (Xe xe : danhSachXe) {
                if (xe == null) continue;
                // Corrected to use getBienSo() from Xe.java model
                cboMaXe.addItem(xe.getMaXe() + " - " + xe.getTenXe() + " (" + xe.getBienSo() + ")");
            }
        }

        danhSachNhanVien = nhanVienController.getAllNhanVien();
        cboMaNV.addItem("--- Chọn Nhân Viên ---");
        if (danhSachNhanVien != null) {
            for (NhanVien nv : danhSachNhanVien) {
                if (nv == null) continue;
                cboMaNV.addItem(nv.getMaNV() + " - " + nv.getHoTen());
            }
        }
    }

    private void loadDataToForm() {
        if (currentGiaoNhanXe != null) {
            txtMaGiaoNhan.setText(currentGiaoNhanXe.getMaGiaoNhan());
            
            selectComboBoxItemByValue(cboMaHD, currentGiaoNhanXe.getMaHD(), danhSachHopDong != null ? danhSachHopDong.stream().filter(java.util.Objects::nonNull).map(HopDong::getMaHD).collect(Collectors.toList()) : null);
            selectComboBoxItemByValue(cboMaXe, currentGiaoNhanXe.getMaXe(), danhSachXe != null ? danhSachXe.stream().filter(java.util.Objects::nonNull).map(Xe::getMaXe).collect(Collectors.toList()) : null);
            selectComboBoxItemByValue(cboMaNV, currentGiaoNhanXe.getMaNV(), danhSachNhanVien != null ? danhSachNhanVien.stream().filter(java.util.Objects::nonNull).map(NhanVien::getMaNV).collect(Collectors.toList()) : null);

            txtAreaTrangThaiXe.setText(currentGiaoNhanXe.getTrangThaiXe());
            txtAreaGhiChu.setText(currentGiaoNhanXe.getGhiChu());
            cboTrangThaiGN.setSelectedItem(currentGiaoNhanXe.getTrangThaiGN());
        }
    }
    
    private void selectComboBoxItemByValue(JComboBox<String> comboBox, String targetValue, List<String> itemValuesFromSource) {
        if (targetValue == null || targetValue.trim().isEmpty()) {
            if (comboBox.getItemCount() > 0) comboBox.setSelectedIndex(0); 
            return;
        }
        for (int i = 1; i < comboBox.getItemCount(); i++) { 
            String itemText = comboBox.getItemAt(i);
            if (itemText == null) continue;
            // Ensure splitting logic is robust for various display formats
            String itemId = itemText.split(" - ")[0].split(" \\| ")[0].trim(); 
            if (itemId.equals(targetValue)) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        
        // Fallback or warning if not found by displayed text but exists in source list
        if (itemValuesFromSource != null && itemValuesFromSource.contains(targetValue)) {
             System.err.println("Warning: Could not precisely select '" + targetValue + "' in ComboBox " + 
                                (comboBox.getName() != null ? comboBox.getName() : "") + 
                                ". It exists in source but not matched in display.");
        } else if (itemValuesFromSource == null && targetValue != null && !targetValue.isEmpty()){
             System.err.println("Warning: itemValuesFromSource is null for ComboBox " + 
                                (comboBox.getName() != null ? comboBox.getName() : "") + 
                                " when trying to select '" + targetValue + "'.");
        }
        if (comboBox.getItemCount() > 0) comboBox.setSelectedIndex(0); 
    }


    private void setFieldsReadOnly() {
        cboMaHD.setEnabled(false);
        cboMaXe.setEnabled(false);
        cboMaNV.setEnabled(false);
        txtAreaTrangThaiXe.setEditable(false);
        txtAreaTrangThaiXe.setBackground(READ_ONLY_BACKGROUND);
        txtAreaGhiChu.setEditable(false);
        txtAreaGhiChu.setBackground(READ_ONLY_BACKGROUND);
        cboTrangThaiGN.setEnabled(false);
        btnSave.setVisible(false); 
        btnCancel.setText("Đóng"); 
    }

    private String getSelectedIdFromComboBox(JComboBox<String> comboBox, String defaultValue) {
        if (comboBox.getSelectedIndex() <= 0) return defaultValue; 
        String selected = (String) comboBox.getSelectedItem();
        if (selected == null) return defaultValue;
        return selected.split(" - ")[0].split(" \\| ")[0].trim();
    }

    private void saveGiaoNhanXe() {
        String maHD = getSelectedIdFromComboBox(cboMaHD, null);
        String maXe = getSelectedIdFromComboBox(cboMaXe, null);
        String maNV = getSelectedIdFromComboBox(cboMaNV, null);
        String trangThaiXe = txtAreaTrangThaiXe.getText().trim();
        String ghiChu = txtAreaGhiChu.getText().trim(); 
        String trangThaiGN = (cboTrangThaiGN.getSelectedIndex() != -1 && cboTrangThaiGN.getSelectedItem() != null) 
                            ? cboTrangThaiGN.getSelectedItem().toString() 
                            : null;

        StringBuilder validationErrors = new StringBuilder();
        if (maHD == null) validationErrors.append("• Vui lòng chọn Mã Hợp Đồng.\n");
        if (maXe == null) validationErrors.append("• Vui lòng chọn Mã Xe.\n");
        if (maNV == null) validationErrors.append("• Vui lòng chọn Nhân Viên thực hiện.\n");
        if (trangThaiXe.isEmpty()) validationErrors.append("• Vui lòng nhập Trạng Thái Xe khi giao/nhận.\n");
        if (trangThaiGN == null) validationErrors.append("• Vui lòng chọn Trạng Thái Giao Nhận.\n");

        if (validationErrors.length() > 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng hoàn tất các thông tin bắt buộc (*):\n" + validationErrors.toString(), "Lỗi Nhập Liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        GiaoNhanXe gnToSave = new GiaoNhanXe();
        gnToSave.setMaHD(maHD);
        gnToSave.setMaXe(maXe);
        gnToSave.setMaNV(maNV);
        gnToSave.setTrangThaiXe(trangThaiXe);
        gnToSave.setGhiChu(ghiChu);
        gnToSave.setTrangThaiGN(trangThaiGN);

        boolean success;
        String message;

        if (isEditMode) {
            gnToSave.setMaGiaoNhan(this.currentGiaoNhanXe.getMaGiaoNhan());
            success = giaoNhanXeController.updateGiaoNhanXe(gnToSave);
            message = success ? "Cập nhật thông tin giao nhận xe thành công!" : "Cập nhật thất bại: ";
        } else {
            String newMaGN = giaoNhanXeController.addGiaoNhanXe(gnToSave);
            success = (newMaGN != null && !newMaGN.isEmpty());
            if (success) {
                 message = "Thêm biên bản giao nhận xe thành công! (Mã GN: " + newMaGN + ")";
                 txtMaGiaoNhan.setText(newMaGN); 
                 // Fetch the newly added GiaoNhanXe to ensure currentGiaoNhanXe is up-to-date if needed later
                 this.currentGiaoNhanXe = giaoNhanXeController.getGiaoNhanXeByMa(newMaGN); 
                 this.isEditMode = true; 
                 setTitle("Sửa Thông Tin Giao Nhận Xe");
            } else {
                 message = "Thêm thất bại: ";
            }
        }

        if (success) {
            JOptionPane.showMessageDialog(this, message, "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            if (parentPanel != null) {
                parentPanel.loadDataToTable(); 
            }
            dispose(); 
        } else {
            String errMsg = giaoNhanXeController.getErrorMessage();
            JOptionPane.showMessageDialog(this, message + (errMsg.isEmpty() ? "Lỗi không xác định từ hệ thống." : errMsg), "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
        }
    }
}