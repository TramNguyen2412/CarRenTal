
package com.mycompany.carrental;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import com.formdev.flatlaf.FlatLaf;
import ui.auth.LoginForm;
import util.ScaledUISettings; // Thêm import này

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Sử dụng class ScaledUISettings thay vì thiết lập trực tiếp
            ScaledUISettings.initializeFonts();
      
            FlatIntelliJLaf.setup();
            
            // Các thiết lập UI khác giữ nguyên
           
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("Table.background", Color.WHITE);
            UIManager.put("TableHeader.background", Color.WHITE);
            UIManager.put("Dialog.background", Color.WHITE);
            UIManager.put("JOptionPane.background", Color.WHITE);
            // Màu viền và đường kẻ nhạt
            UIManager.put("Component.borderColor", new Color(230, 230, 230));
            UIManager.put("Table.gridColor", new Color(240, 240, 240));

            // Màu chính (giống mẫu bạn chia sẻ)
            UIManager.put("Button.arc", 6); // Bo góc các button
            UIManager.put("Component.arc", 6); // Bo góc các component

            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("TextComponent.arc", 5);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
            UIManager.put("Button.iconTextGap", 10);
            UIManager.put("PasswordField.showRevealButton", true);
            
            // Tùy chỉnh bảng - sử dụng ScaledUISettings để lấy kích thước đã scale
            UIManager.put("Table.selectionBackground", new Color(25, 118, 210));
            UIManager.put("Table.selectionForeground", new Color(255, 255, 255));
            UIManager.put("Table.scrollPaneBorder", new EmptyBorder(0, 0, 0, 0));
            UIManager.put("Table.rowHeight", ScaledUISettings.getScaledSize(40)); // Sử dụng hàm scale
            UIManager.put("TableHeader.height", ScaledUISettings.getScaledSize(40)); // Sử dụng hàm scale
            
            // Truyền kích thước mặc định cho font (tùy chọn)
            // ScaledUISettings.setGlobalFontSize(14); // Nếu muốn size lớn hơn
            
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        java.awt.EventQueue.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}