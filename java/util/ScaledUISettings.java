package util;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

public class ScaledUISettings {
    // Kích thước font cơ bản
    private static int BASE_FONT_SIZE = 13;
    // Hệ số scale
    private static float SCALE_FACTOR = 1.0f;
    private static int TEXT_FIELD_HEIGHT = 40;
    private static int BUTTON_HEIGHT = 40;
    private static int LABEL_MARGIN_BOTTOM = 8;
    private static int COMPONENT_VERTICAL_GAP = 16;
    /**
     * Khởi tạo font Roboto cho toàn bộ ứng dụng
     */
    public static void initializeFonts() {
        try {
            // Cài đặt font Roboto
            FlatRobotoFont.install();
            
            // Thiết lập font family cho FlatLaf
            FlatLaf.setPreferredFontFamily(FlatRobotoFont.FAMILY);
            FlatLaf.setPreferredLightFontFamily(FlatRobotoFont.FAMILY_LIGHT);
            FlatLaf.setPreferredSemiboldFontFamily(FlatRobotoFont.FAMILY_SEMIBOLD);
            
            // Áp dụng font mặc định
            applyGlobalFontSettings();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Thay đổi kích thước font cơ bản
     */
    public static void setGlobalFontSize(int newSize) {
        BASE_FONT_SIZE = newSize;
        applyGlobalFontSettings();
    }

    /**
     * Thay đổi hệ số scale
     */
    public static void setScaleFactor(float factor) {
        SCALE_FACTOR = factor;
        applyGlobalFontSettings();
    }

    /**
     * Lấy hệ số scale hiện tại
     */
    public static float getScaleFactor() {
        return SCALE_FACTOR;
    }

    /**
     * Tính kích thước đã được scale
     */
    public static int getScaledSize(int size) {
        return Math.round(size * SCALE_FACTOR);
    }

    /**
     * Lấy kích thước component đã được scale
     */
    public static Dimension getScaledComponentSize(int width, int height) {
        return new Dimension(getScaledSize(width), getScaledSize(height));
    }

    /**
     * Thiết lập font cho tất cả các thành phần UI
     */
    public static void applyGlobalFontSettings() {
        // Font cơ bản
        int scaledSize = getScaledSize(BASE_FONT_SIZE);
        FontUIResource baseFont = new FontUIResource(FlatRobotoFont.FAMILY, Font.PLAIN, scaledSize);
        
        // Thiết lập font mặc định
        UIManager.put("defaultFont", baseFont);
        
        // Áp dụng cho tất cả các thành phần có font
        setUIFont(baseFont);
        
        // Thiết lập font cho các loại component cụ thể
        UIManager.put("Label.font", new FontUIResource(FlatRobotoFont.FAMILY, Font.PLAIN, scaledSize));
        UIManager.put("TextField.font", new FontUIResource(FlatRobotoFont.FAMILY, Font.PLAIN, scaledSize));
        UIManager.put("TextArea.font", new FontUIResource(FlatRobotoFont.FAMILY, Font.PLAIN, scaledSize));
        UIManager.put("PasswordField.font", new FontUIResource(FlatRobotoFont.FAMILY, Font.PLAIN, scaledSize));
        UIManager.put("ComboBox.font", new FontUIResource(FlatRobotoFont.FAMILY, Font.PLAIN, scaledSize));
        UIManager.put("Button.font", new FontUIResource(FlatRobotoFont.FAMILY, Font.PLAIN, scaledSize));
        
        // Thiết lập font cho bảng
        UIManager.put("Table.font", new FontUIResource(FlatRobotoFont.FAMILY, Font.PLAIN, scaledSize));
        UIManager.put("TableHeader.font", new FontUIResource(FlatRobotoFont.FAMILY, Font.BOLD, scaledSize));
        
        // Font cho menu
        UIManager.put("Menu.font", new FontUIResource(FlatRobotoFont.FAMILY, Font.PLAIN, scaledSize));
        UIManager.put("MenuItem.font", new FontUIResource(FlatRobotoFont.FAMILY, Font.PLAIN, scaledSize));
        
        // Font cho tiêu đề
        UIManager.put("TitledBorder.font", new FontUIResource(FlatRobotoFont.FAMILY, Font.BOLD, getScaledSize(BASE_FONT_SIZE + 2)));
        
        // Cập nhật UI nếu cần
        updateUI();
    }

    /**
     * Áp dụng font cho tất cả các thành phần UI
     */
    public static void setUIFont(FontUIResource f) {
        UIDefaults defaults = UIManager.getDefaults();
        Enumeration<Object> keys = defaults.keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = defaults.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    /**
     * Cập nhật UI cho tất cả các cửa sổ đang mở
     */
    public static void updateUI() {
        for (Window window : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
        }
    }
}