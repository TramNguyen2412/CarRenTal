package ui.customer;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private int cornerRadius = 12; // Giảm độ bo góc
    public boolean isSelected = false;
    
    public RoundedPanel() {
        // Sử dụng BorderLayout thay vì FlowLayout
        setLayout(new BorderLayout());
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
        
        // Vẽ viền mỏng
        if (isSelected) {
            g2.setColor(new Color(255, 255, 255, 150));
            g2.setStroke(new BasicStroke(1.5f)); // Viền mỏng hơn
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
        }
        g2.dispose();
    }
}