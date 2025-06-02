package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ImageUtil {
    
    // Đường dẫn cố định trong project
    public static String getImageDirPath() {
        // Đường dẫn đến thư mục src/main/resources/img/cars/
        String projectDir = System.getProperty("user.dir");
        String path = projectDir + File.separator + "src" + File.separator + 
                     "main" + File.separator + "resources" + File.separator + 
                     "img" + File.separator + "cars" + File.separator;
        
        // Tạo thư mục nếu chưa tồn tại
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("Đã tạo thư mục: " + path);
        }
        
        return path;
    }
    
    // Đường dẫn classpath cho việc đọc ảnh khi chạy
    public static String getImageResourcePath() {
        return "/img/cars/";
    }
    
 //    Tạo ImageIcon từ tên file
    public static ImageIcon createImageIcon(String fileName, int width, int height) {
        try {
            // Thử đọc từ filesystem trước
            File file = new File(getImageDirPath() + fileName);
            if (file.exists()) {
                BufferedImage originalImage = ImageIO.read(file);
                if (originalImage != null) {
                    Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImage);
                }
            }
            
            // Nếu không tìm thấy, thử từ resources
            java.net.URL imgURL = ImageUtil.class.getResource(getImageResourcePath() + fileName);
            if (imgURL != null) {
                BufferedImage originalImage = ImageIO.read(imgURL);
                if (originalImage != null) {
                    Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImage);
                }
            }
            
            // Không tìm thấy ảnh
            System.err.println("Không tìm thấy ảnh: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    // Phương thức lưu ảnh vào thư mục
    public static String saveImage(File sourceFile, String fileName) throws IOException {
        if (sourceFile == null || !sourceFile.exists()) {
            return null;
        }
        
        // Tạo thư mục nếu chưa tồn tại
        String dirPath = getImageDirPath();
        
        // Lấy phần mở rộng của file
        String fileExtension = getFileExtension(sourceFile);
        String newFileName = fileName + fileExtension;
        
        // Đường dẫn đích
        File destinationFile = new File(dirPath + newFileName);
        
        System.out.println("Lưu ảnh từ: " + sourceFile.getAbsolutePath());
        System.out.println("Đến: " + destinationFile.getAbsolutePath());
        
        // Copy file ảnh vào thư mục đích
        Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        return newFileName;
    }
    
    // Phương thức lấy phần mở rộng của file
    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ".jpg"; // Mặc định là .jpg nếu không có phần mở rộng
        }
        return name.substring(lastIndexOf);
    }
    
    // Phương thức kiểm tra file có phải là hình ảnh
    public static boolean isImageFile(String fileName) {
        String extension = fileName.toLowerCase();
        return extension.endsWith(".jpg") || extension.endsWith(".jpeg") || 
               extension.endsWith(".png") || extension.endsWith(".gif");
    }
    
    // Phương thức resize ảnh
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        if (originalImage == null) return null;
        
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resizedImage;
    }
    
    public static void displayImage(String imageName, JLabel label) {
        if (imageName == null || imageName.isEmpty()) {
            label.setIcon(null);
            label.setText("Không có ảnh");
            return;
        }

        try {
            Image image = null;
            String fullPath = getImageDirPath() + imageName;
            
            // Tìm file ảnh từ filesystem
            File file = new File(fullPath);
            if (file.exists()) {
                BufferedImage buffImg = ImageIO.read(file);
                if (buffImg != null) {
                    image = buffImg;
                }
            }

            // Nếu không tìm thấy từ filesystem, thử classpath
            if (image == null) {
                java.net.URL url = ImageUtil.class.getResource(getImageResourcePath() + imageName);
                if (url != null) {
                    BufferedImage buffImg = ImageIO.read(url);
                    if (buffImg != null) {
                        image = buffImg;
                    }
                }
            }

            if (image != null) {
                // Lấy kích thước của container
                int containerWidth = label.getParent() != null ? label.getParent().getWidth() : 380;
                int containerHeight = label.getParent() != null ? label.getParent().getHeight() : 280;
                
                // Nếu container chưa được render, sử dụng kích thước mặc định của label hoặc giá trị cố định
                if (containerWidth <= 10) containerWidth = label.getWidth() > 10 ? label.getWidth() : 380;
                if (containerHeight <= 10) containerHeight = label.getHeight() > 10 ? label.getHeight() : 280;
                
                // Kích thước gốc của ảnh
                int imgWidth = image.getWidth(null);
                int imgHeight = image.getHeight(null);
                
                // Tính tỷ lệ để fit ảnh vào container (giữ nguyên tỷ lệ khung hình)
                double widthRatio = (double) containerWidth / imgWidth;
                double heightRatio = (double) containerHeight / imgHeight;
                double ratio = Math.min(widthRatio, heightRatio) * 0.9; // Để lại một chút margin
                
                // Tính kích thước mới theo tỷ lệ
                int newWidth = (int) (imgWidth * ratio);
                int newHeight = (int) (imgHeight * ratio);
                
                // Tạo ảnh được scale với kích thước mới
                Image scaledImg = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                
                // Đặt icon cho label
                label.setIcon(new ImageIcon(scaledImg));
                label.setText("");
                
                // Đảm bảo label căn giữa ảnh
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                
                System.out.println("Đã hiển thị ảnh [" + imageName + "] với kích thước " + newWidth + "x" + newHeight);
            } else {
                // Không tìm thấy ảnh từ cả hai nguồn
                System.out.println("Không tìm thấy ảnh: " + imageName);
                label.setIcon(null);
                label.setText("Không tìm thấy ảnh");
            }
        } catch (Exception e) {
            e.printStackTrace();
            label.setIcon(null);
            label.setText("Lỗi hiển thị ảnh");
        }
    }
    public static void displayImageWithFixedSize(String imageName, JLabel label, int width, int height) {
        if (imageName == null || imageName.isEmpty()) {
            label.setIcon(null);
            label.setText("Không có ảnh");
            return;
        }

        try {
            Image image = null;
            String fullPath = getImageDirPath() + imageName;
            
            // Tìm file ảnh từ filesystem
            File file = new File(fullPath);
            if (file.exists()) {
                BufferedImage buffImg = ImageIO.read(file);
                if (buffImg != null) {
                    image = buffImg;
                }
            }

            // Nếu không tìm thấy từ filesystem, thử classpath
            if (image == null) {
                java.net.URL url = ImageUtil.class.getResource(getImageResourcePath() + imageName);
                if (url != null) {
                    BufferedImage buffImg = ImageIO.read(url);
                    if (buffImg != null) {
                        image = buffImg;
                    }
                }
            }

            if (image != null) {
                // Kích thước gốc của ảnh
                int imgWidth = image.getWidth(null);
                int imgHeight = image.getHeight(null);
                
                // Tính toán kích thước mới giữ nguyên tỷ lệ
                int newWidth, newHeight;
                double imgRatio = (double) imgWidth / imgHeight;
                double targetRatio = (double) width / height;
                
                if (imgRatio > targetRatio) {
                    // Ảnh rộng hơn
                    newWidth = width;
                    newHeight = (int) (width / imgRatio);
                } else {
                    // Ảnh cao hơn
                    newHeight = height;
                    newWidth = (int) (height * imgRatio);
                }
                
                // Tạo ảnh được scale với kích thước mới
                Image scaledImg = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                
                // Đặt icon cho label
                label.setIcon(new ImageIcon(scaledImg));
                label.setText("");
                
                // Đảm bảo label căn giữa ảnh
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                
                System.out.println("Đã hiển thị ảnh [" + imageName + "] với kích thước " + newWidth + "x" + newHeight);
            } else {
                // Không tìm thấy ảnh từ cả hai nguồn
                System.out.println("Không tìm thấy ảnh: " + imageName);
                label.setIcon(null);
                label.setText("Không tìm thấy ảnh");
            }
        } catch (Exception e) {
            e.printStackTrace();
            label.setIcon(null);
            label.setText("Lỗi hiển thị ảnh");
        }
    }
}