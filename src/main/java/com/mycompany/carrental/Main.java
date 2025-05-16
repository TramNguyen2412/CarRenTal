package com.mycompany.carrental;
import com.formdev.flatlaf.FlatLightLaf;
import ui.auth.LoginForm;

public class Main {
    public static void main(String[] args) {
//        try {
//            // Set Look and Feel
//            javax.swing.UIManager.setLookAndFeel(
//                javax.swing.UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        

        try {
            // Sử dụng FlatLaf thay vì UIManager mặc định
            FlatLightLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Hiển thị form đăng nhập
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
}