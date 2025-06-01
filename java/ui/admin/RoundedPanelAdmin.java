package ui.admin;

import javax.swing.*;
import java.awt.*;

public class RoundedPanelAdmin extends JPanel {
    private int cornerRadius = 15;
    public boolean isSelected = false;
    
    public RoundedPanelAdmin() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setOpaque(false);
    }
    
    public void setSelected(boolean selected) {
        this.isSelected = selected;
        repaint();
    }
    
    @Override
    public boolean isOpaque() {
        return false;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Vẽ nền bo tròn
        if (isSelected || getBackground().getAlpha() > 0) {
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        }
        
        // Vẽ viền mỏng để dễ phân biệt (tùy chọn)
//        if (isSelected) {
//            g2.setColor(new Color(255, 193, 7, 150));
//            g2.setStroke(new BasicStroke(2));
//            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
//        }
           if (isSelected) {
                g2.setColor(new Color(255, 255, 255, 150)); // Đổi từ vàng sang trắng mờ
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            }
        g2.dispose();
    }
}