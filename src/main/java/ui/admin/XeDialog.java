package ui.admin;

import controller.XeController;
import model.Xe;
import util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        this.parent = parent;
        this.xeController = new XeController();
        
        initComponents();
    }
    
    // Phương thức lấy đường dẫn đầy đủ của hình ảnh
    private String getImagePath(String fileName) {
        return ImageUtil.getImageDirPath() + fileName;
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
        setSize(700, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Các trường nhập liệu
        txtMaXe = new JTextField(20);
        txtTenXe = new JTextField(20);
        txtBienSo = new JTextField(20);
        txtSoCho = new JTextField(20);
        cboHangXe = new JComboBox<>(new String[]{"Toyota", "Honda", "Hyundai", "KIA", "Mazda", "Ford", "Chevrolet", "Mercedes-Benz", "BMW", "Audi", "Lamborghini", "Roll Royce", "Khác"});
        txtNamSX = new JTextField(20);
        cboTrangThai = new JComboBox<>(new String[]{"Sẵn sàng", "Đang thuê", "Bảo dưỡng"});
        txtGiaThue = new JTextField(20);
        
        // Panel chọn ảnh
        JPanel pnlImage = new JPanel(new BorderLayout(5, 5));
        lblImage = new JLabel("", JLabel.CENTER);
        lblImage.setPreferredSize(new Dimension(250, 200));
        lblImage.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JButton btnChooseImage = new JButton("Chọn ảnh");
        
        pnlImage.add(lblImage, BorderLayout.CENTER);
        pnlImage.add(btnChooseImage, BorderLayout.SOUTH);
        
        if (xe.getHinhAnh() != null && !xe.getHinhAnh().isEmpty()) {
            ImageUtil.displayImage(xe.getHinhAnh(), lblImage);
        } else {
            lblImage.setText("Không có ảnh");
        }
        // Nếu là chế độ sửa, hiển thị dữ liệu của xe
        if (xe != null) {
            txtMaXe.setText(xe.getMaXe());
            txtMaXe.setEditable(false); // Không cho phép sửa mã xe
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
                String imagePath = getImagePath(xe.getHinhAnh());
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    ImageIcon icon = new ImageIcon(imagePath);
                    Image img = icon.getImage();
                    Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    lblImage.setIcon(new ImageIcon(scaledImg));
                    lblImage.setText("");
                }
            }
        } else {
            // Thêm mới
            txtMaXe.setText("Tự động tạo");
            txtMaXe.setEditable(false);
        }
        
        // Thêm các thành phần vào form
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Mã xe:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtMaXe, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Tên xe:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtTenXe, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Biển số:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtBienSo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Số chỗ:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtSoCho, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Hãng xe:"), gbc);
        
        gbc.gridx = 1;
        panel.add(cboHangXe, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Năm sản xuất:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtNamSX, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Trạng thái:"), gbc);
        
        gbc.gridx = 1;
        panel.add(cboTrangThai, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Giá thuê/ngày (VND):"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtGiaThue, gbc);
        
        // Thêm panel chọn ảnh vào form
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 8;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(pnlImage, gbc);
        
        // Sự kiện chọn ảnh
//        btnChooseImage.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JFileChooser fileChooser = new JFileChooser();
//                FileNameExtensionFilter filter = new FileNameExtensionFilter(
//                    "Image Files", "jpg", "jpeg", "png", "gif");
//                fileChooser.setFileFilter(filter);
//                
//                int result = fileChooser.showOpenDialog(XeDialog.this);
//                if (result == JFileChooser.APPROVE_OPTION) {
//                    selectedImageFile = fileChooser.getSelectedFile();
//                    try {
//                        // Hiển thị ảnh preview
//                        ImageIcon icon = new ImageIcon(selectedImageFile.getPath());
//                        Image scaledImg = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
//                        lblImage.setIcon(new ImageIcon(scaledImg));
//                        lblImage.setText("");
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                        JOptionPane.showMessageDialog(XeDialog.this, 
//                            "Không thể tải hình ảnh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    }
//                }
//            }
//        });
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
                        // Hiển thị ảnh preview sử dụng ImageUtil
                        ImageIcon icon = new ImageIcon(selectedImageFile.getPath());
                        Image scaledImg = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
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
        // Panel nút
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        
        pnlButtons.add(btnSave);
        pnlButtons.add(btnCancel);
        
        add(panel, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);
        
        // Thêm sự kiện cho các nút
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kiểm tra dữ liệu nhập
                if (txtTenXe.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(XeDialog.this, "Vui lòng nhập tên xe!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (txtBienSo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(XeDialog.this, "Vui lòng nhập biển số xe!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Kiểm tra số chỗ
                int soCho;
                try {
                    soCho = Integer.parseInt(txtSoCho.getText().trim());
                    if (soCho <= 0) {
                        JOptionPane.showMessageDialog(XeDialog.this, "Số chỗ phải là số dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(XeDialog.this, "Số chỗ không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Kiểm tra năm sản xuất
                int namSX;
                try {
                    namSX = Integer.parseInt(txtNamSX.getText().trim());
                    if (namSX <= 0) {
                        JOptionPane.showMessageDialog(XeDialog.this, "Năm sản xuất phải là số dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(XeDialog.this, "Năm sản xuất không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Kiểm tra giá thuê
                double giaThue;
                try {
                    giaThue = Double.parseDouble(txtGiaThue.getText().trim());
                    if (giaThue <= 0) {
                        JOptionPane.showMessageDialog(XeDialog.this, "Giá thuê phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(XeDialog.this, "Giá thuê không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Tạo đối tượng xe từ dữ liệu nhập
                Xe newXe = new Xe();
                if (xe != null) {
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
//                if (selectedImageFile != null) {
//                    try {
//                        // Đảm bảo thư mục tồn tại
//                        ensureImageDirExists();
//                        
//                        String imageName;
//                        if (xe != null) {
//                            // Chế độ sửa - sử dụng mã xe để đặt tên file
//                            imageName = "xe_" + xe.getMaXe() + getFileExtension(selectedImageFile);
//                        } else {
//                            // Chế độ thêm mới - sử dụng timestamp
//                            imageName = "xe_temp_" + System.currentTimeMillis() + getFileExtension(selectedImageFile);
//                        }
//                        
//                        // Đường dẫn đầy đủ của file ảnh đích
//                        File destinationFile = new File(ImageUtil.getImageDirPath(), imageName);
//                        
//                        // Copy file ảnh vào thư mục đích
//                        Files.copy(selectedImageFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                        
//                        // Cập nhật tên ảnh cho đối tượng xe
//                        newXe.setHinhAnh(imageName);
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                        JOptionPane.showMessageDialog(XeDialog.this, 
//                            "Lỗi khi lưu hình ảnh: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//                        return;
//                    }
//                }
                if (selectedImageFile != null) {
                    try {
                        // Tạo tên file dựa trên mã xe hoặc thời gian
                        String fileName;
                        if (xe != null) {
                            // Chế độ sửa - sử dụng mã xe để đặt tên file
                            fileName = "xe_" + xe.getMaXe();
                        } else {
                            // Chế độ thêm mới - sử dụng timestamp
                            fileName = "xe_temp_" + System.currentTimeMillis();
                        }

                        // Lưu ảnh và lấy tên file kết quả
                        String savedFileName = ImageUtil.saveImage(selectedImageFile, fileName);
                        if (savedFileName != null) {
                            // Cập nhật tên ảnh cho đối tượng xe
                            newXe.setHinhAnh(savedFileName);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(XeDialog.this, 
                            "Lỗi khi lưu hình ảnh: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                boolean success;
                if (xe == null) {
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
                                // Không gây lỗi ứng dụng nếu không đổi được tên file
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
}