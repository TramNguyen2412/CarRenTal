
package ui.admin.QLXe;

import controller.XeController;
import model.Xe;
import util.ImageUtil;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class XeDialog extends JDialog {
    private Xe xe;
    private XePanel parent;
    private XeController xeController;
    
    // Các thành phần giao diện
    private JTextField txtMaXe, txtTenXe, txtBienSo, txtSoCho, txtNamSX, txtGiaThue;
    private JComboBox<String> cboHangXe, cboTrangThai;
    private JLabel lblImage;
    private File selectedImageFile = null;
    private String currentImageName = null;
    
    public XeDialog(Window owner, Xe xe, XePanel parent) {
        super(owner, xe == null ? "Thêm xe mới" : "Chỉnh sửa xe", ModalityType.APPLICATION_MODAL);
        this.xe = xe;
        // Nếu xe là null, khởi tạo đối tượng xe mới để tránh NullPointerException
        if (this.xe == null) {
            this.xe = new Xe();
        }
        this.parent = parent;
        this.xeController = new XeController();

        initComponents();
        setResizable(false);
    }
    
    // Phương thức tạo thư mục lưu ảnh nếu chưa tồn tại
    private void ensureImageDirExists() throws IOException {
        File imageDir = new File(ImageUtil.getImageDirPath());
        if (!imageDir.exists()) {
            if (!imageDir.mkdirs()) {
                throw new IOException("Không thể tạo thư mục lưu ảnh: " + imageDir.getAbsolutePath());
            }
        }
    }
    
    private void initComponents() {
        // Tăng kích thước dialog
        setSize(900, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        // Panel chính có màu nền nhẹ nhàng
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(new Color(245, 245, 245));
        
        // === PANEL THÔNG TIN BÊN TRÁI ===
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Sử dụng GroupLayout cho form nhập liệu
        GroupLayout layout = new GroupLayout(formPanel);
        formPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        // Tiêu đề form
        JLabel lblTitle = new JLabel(xe.getMaXe() == null ? "THÊM XE MỚI" : "CHỈNH SỬA XE");
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 20));
        lblTitle.setForeground(new Color(33, 150, 243));
     //   lblTitle.setForeground(new Color(40, 40, 40));
        
        // Tạo các label với font lớn
        JLabel lblMaXeTitle = createLabel("Mã xe:", 16);
        JLabel lblTenXeTitle = createLabel("Tên xe:", 16);
        JLabel lblBienSoTitle = createLabel("Biển số:", 16);
        JLabel lblSoChoTitle = createLabel("Số chỗ:", 16);
        JLabel lblHangXeTitle = createLabel("Hãng xe:", 16);
        JLabel lblNamSXTitle = createLabel("Năm sản xuất:", 16);
        JLabel lblTrangThaiTitle = createLabel("Trạng thái:", 16);
        JLabel lblGiaThueTitle = createLabel("Giá thuê/ngày (VND):", 16);
        
        // Tạo các component nhập liệu với font lớn hơn
        txtMaXe = createStyledTextField();
        txtTenXe = createStyledTextField();
        txtBienSo = createStyledTextField();
        txtSoCho = createStyledTextField();
        txtNamSX = createStyledTextField();
        txtGiaThue = createStyledTextField();
        
        // Tạo combobox với style đẹp hơn
        cboHangXe = createStyledComboBox(new String[]{"Toyota", "Honda", "Hyundai", "KIA", "Mazda", "Ford", "Chevrolet", "Mercedes-Benz", "BMW", "Audi", "Lamborghini", "Roll Royce", "Khác"});
        //cboTrangThai = createStyledComboBox(new String[]{"Sẵn sàng", "Đang thuê", "Bảo dưỡng"});
        cboTrangThai = createStyledComboBox(new String[]{"Sẵn sàng", "Đang thuê", "Bảo dưỡng", "Không khả dụng"});

        // === PANEL HÌNH ẢNH BÊN PHẢI ===
        JPanel imagePanel = new JPanel(new BorderLayout(0, 15));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Tiêu đề phần hình ảnh
        JLabel lblImageTitle = new JLabel("HÌNH ẢNH XE", JLabel.CENTER);
        lblImageTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
        lblImageTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Panel chứa ảnh và nút chọn ảnh
        JPanel imageContentPanel = new JPanel(new BorderLayout(0, 10));
        imageContentPanel.setBackground(Color.WHITE);
        
        // Label hiển thị hình ảnh với kích thước lớn
        lblImage = new JLabel("", JLabel.CENTER);
        lblImage.setPreferredSize(new Dimension(400, 300));
        lblImage.setMinimumSize(new Dimension(400, 300));
        lblImage.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240), 2));
        
        // Nút chọn ảnh với style đẹp
        JButton btnChooseImage = createStyledButton("Chọn ảnh", new Color(0, 150, 136));
        btnChooseImage.setPreferredSize(new Dimension(150, 40));
        
        // Panel để căn giữa nút chọn ảnh
        JPanel btnImagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnImagePanel.setBackground(Color.WHITE);
        btnImagePanel.add(btnChooseImage);
        
        imageContentPanel.add(lblImage, BorderLayout.CENTER);
        imageContentPanel.add(btnImagePanel, BorderLayout.SOUTH);
        
        imagePanel.add(lblImageTitle, BorderLayout.NORTH);
        imagePanel.add(imageContentPanel, BorderLayout.CENTER);
        
        // === THIẾT LẬP DỮ LIỆU BAN ĐẦU ===
        
        // Nếu là chế độ sửa, hiển thị dữ liệu của xe
        if (xe.getMaXe() != null && !xe.getMaXe().isEmpty()) {
            txtMaXe.setText(xe.getMaXe());
            txtMaXe.setEditable(false);
            txtTenXe.setText(xe.getTenXe());
            txtBienSo.setText(xe.getBienSo());
            txtSoCho.setText(String.valueOf(xe.getSoCho()));
            cboHangXe.setSelectedItem(xe.getHangXe());
            txtNamSX.setText(String.valueOf(xe.getNamSX()));
            cboTrangThai.setSelectedItem(xe.getTrangThai());
            txtGiaThue.setText(String.valueOf((int)xe.getGiaThueNgay()));

            // Hiển thị ảnh nếu có
            if (xe.getHinhAnh() != null && !xe.getHinhAnh().isEmpty()) {
                currentImageName = xe.getHinhAnh();
                // Đặt kích thước cố định cho label trước khi hiển thị ảnh
                lblImage.setSize(400, 300);
                ImageUtil.displayImage(xe.getHinhAnh(), lblImage);
            } else {
                lblImage.setText("Chưa có ảnh");
                lblImage.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 16));
            }
        } else {
            // Thêm mới
            txtMaXe.setText("Tự động tạo");
            txtMaXe.setEditable(false);
            lblImage.setText("Chưa có ảnh");
            lblImage.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 16));
        }
        
        // === THIẾT LẬP GROUPLAYOUT ===
        
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
                .addComponent(txtMaXe)
                .addComponent(txtTenXe)
                .addComponent(txtBienSo)
                .addComponent(txtSoCho)
                .addComponent(cboHangXe)
                .addComponent(txtNamSX)
                .addComponent(cboTrangThai)
                .addComponent(txtGiaThue));
        
        layout.setHorizontalGroup(hGroup);
        
        // Thiết lập nhóm dọc cho GroupLayout
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        
        vGroup.addComponent(lblTitle);
        vGroup.addGap(20);
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblMaXeTitle)
                .addComponent(txtMaXe));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblTenXeTitle)
                .addComponent(txtTenXe));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblBienSoTitle)
                .addComponent(txtBienSo));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblSoChoTitle)
                .addComponent(txtSoCho));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblHangXeTitle)
                .addComponent(cboHangXe));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblNamSXTitle)
                .addComponent(txtNamSX));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblTrangThaiTitle)
                .addComponent(cboTrangThai));
        
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblGiaThueTitle)
                .addComponent(txtGiaThue));
        
        layout.setVerticalGroup(vGroup);
        
        // === PANEL NÚT THAO TÁC ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));
        
        JButton btnSave = createStyledButton("Lưu", new Color(33, 150, 243));
        JButton btnCancel = createStyledButton("Hủy", new Color(120, 120, 120));
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        // === THÊM CÁC PANEL VÀO PANEL CHÍNH ===
        
        // Thêm các panel vào panel chính với giao diện hai cột
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        contentPanel.add(formPanel, gbc);
        
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 0, 0);
        contentPanel.add(imagePanel, gbc);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // === SỰ KIỆN NÚT CHỌN ẢNH ===
        btnChooseImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Image Files", "jpg", "jpeg", "png", "gif");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(XeDialog.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedImageFile = fileChooser.getSelectedFile();
                    try {
                        // Hiển thị ảnh preview
                        ImageIcon icon = new ImageIcon(selectedImageFile.getPath());
                        Image img = icon.getImage();
                        Image scaledImg = img.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                        lblImage.setIcon(new ImageIcon(scaledImg));
                        lblImage.setText("");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(XeDialog.this, 
                            "Không thể tải hình ảnh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        // === SỰ KIỆN NÚT LƯU ===
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kiểm tra dữ liệu nhập
                if (txtTenXe.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(XeDialog.this, "Vui lòng nhập tên xe!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtTenXe.requestFocus();
                    return;
                }
                
                if (txtBienSo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(XeDialog.this, "Vui lòng nhập biển số xe!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtBienSo.requestFocus();
                    return;
                }
                
                // Kiểm tra số chỗ
                int soCho;
                try {
                    soCho = Integer.parseInt(txtSoCho.getText().trim());
                    if (soCho <= 0) {
                        JOptionPane.showMessageDialog(XeDialog.this, "Số chỗ phải là số dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        txtSoCho.requestFocus();
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(XeDialog.this, "Số chỗ không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtSoCho.requestFocus();
                    return;
                }
                
                // Kiểm tra năm sản xuất
                int namSX;
                try {
                    namSX = Integer.parseInt(txtNamSX.getText().trim());
                    if (namSX <= 0) {
                        JOptionPane.showMessageDialog(XeDialog.this, "Năm sản xuất phải là số dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        txtNamSX.requestFocus();
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(XeDialog.this, "Năm sản xuất không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtNamSX.requestFocus();
                    return;
                }
                
                // Kiểm tra giá thuê
                double giaThue;
                try {
                    giaThue = Double.parseDouble(txtGiaThue.getText().trim());
                    if (giaThue <= 0) {
                        JOptionPane.showMessageDialog(XeDialog.this, "Giá thuê phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        txtGiaThue.requestFocus();
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(XeDialog.this, "Giá thuê không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtGiaThue.requestFocus();
                    return;
                }
                
                // Kiểm tra xem đây là thêm mới hay cập nhật
                boolean isThemMoi = txtMaXe.getText().equals("Tự động tạo");
                
                // Tạo đối tượng xe từ dữ liệu nhập
                Xe newXe = new Xe();
                if (!isThemMoi) {
                    newXe.setMaXe(xe.getMaXe());
                    newXe.setHinhAnh(currentImageName); // Giữ nguyên ảnh cũ nếu không chọn ảnh mới
                }
                
                newXe.setTenXe(txtTenXe.getText().trim());
                newXe.setBienSo(txtBienSo.getText().trim());
                newXe.setSoCho(soCho);
                newXe.setHangXe(cboHangXe.getSelectedItem().toString());
                newXe.setNamSX(namSX);
                newXe.setTrangThai(cboTrangThai.getSelectedItem().toString());
                newXe.setGiaThueNgay(giaThue);
                
                // Xử lý ảnh nếu có chọn ảnh mới
                if (selectedImageFile != null) {
                    try {
                        // Đảm bảo thư mục tồn tại
                        ensureImageDirExists();
                        
                        String imageName;
                        if (!isThemMoi) {
                            // Chế độ sửa - sử dụng mã xe để đặt tên file
                            imageName = "xe_" + xe.getMaXe() + getFileExtension(selectedImageFile);
                        } else {
                            // Chế độ thêm mới - sử dụng timestamp tạm thời
                            imageName = "xe_temp_" + System.currentTimeMillis() + getFileExtension(selectedImageFile);
                        }
                        
                        // Đường dẫn đầy đủ của file ảnh đích
                        File destinationFile = new File(ImageUtil.getImageDirPath(), imageName);
                        
                        // Copy file ảnh vào thư mục đích
                        Files.copy(selectedImageFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        
                        // Cập nhật tên ảnh cho đối tượng xe
                        newXe.setHinhAnh(imageName);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(XeDialog.this, 
                            "Lỗi khi lưu hình ảnh: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                boolean success;
                if (isThemMoi) {
                    // Thêm mới
                    success = xeController.addXe(newXe);
                    if (success) {
                        // Nếu thêm thành công và có ảnh được tải lên với tên tạm thời
                        if (selectedImageFile != null && newXe.getHinhAnh() != null && newXe.getHinhAnh().contains("temp")) {
                            try {
                                // Lấy mã xe vừa được tạo
                                String maXe = newXe.getMaXe();
                                if (maXe != null && !maXe.isEmpty()) {
                                    // Tạo tên file mới dựa trên mã xe
                                    String oldFileName = newXe.getHinhAnh();
                                    String newFileName = "xe_" + maXe + getFileExtension(selectedImageFile);
                                    
                                    // Di chuyển file ảnh
                                    File oldFile = new File(ImageUtil.getImageDirPath(), oldFileName);
                                    File newFile = new File(ImageUtil.getImageDirPath(), newFileName);
                                    
                                    if (oldFile.renameTo(newFile)) {
                                        // Cập nhật tên file trong DB
                                        newXe.setHinhAnh(newFileName);
                                        xeController.updateXe(newXe); // Cập nhật tên file ảnh mới
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        
                        JOptionPane.showMessageDialog(XeDialog.this, "Thêm xe mới thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        parent.loadDataToTable();
                    } else {
                        JOptionPane.showMessageDialog(XeDialog.this, "Thêm xe mới thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Cập nhật
                    success = xeController.updateXe(newXe);
                    if (success) {
                        JOptionPane.showMessageDialog(XeDialog.this, "Cập nhật thông tin xe thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        parent.loadDataToTable();
                    } else {
                        JOptionPane.showMessageDialog(XeDialog.this, "Cập nhật thông tin xe thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
    
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ".jpg"; // Mặc định là .jpg nếu không có phần mở rộng
        }
        return name.substring(lastIndexOf);
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
    
    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
        comboBox.setPreferredSize(new Dimension(250, 35));
        return comboBox;
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