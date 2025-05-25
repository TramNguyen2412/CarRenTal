package ui.customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

public class StarRatingComponent extends JPanel {
    private int starCount;
    private int rating;
    private int hoverRating;
    private boolean editable;
    private Color starColor;
    private Color hoverColor;
    private Color emptyColor;
    
    private static final int DEFAULT_STAR_SIZE = 25;
    private static final int STAR_SPACING = 5;
    
    public StarRatingComponent() {
        this(5, 0);
    }
    
    public StarRatingComponent(int starCount, int initialRating) {
        this.starCount = starCount;
        this.rating = initialRating;
        this.hoverRating = 0;
        this.editable = true;
        this.starColor = new Color(255, 165, 0); // Orange
        this.hoverColor = new Color(255, 215, 0); // Gold
        this.emptyColor = new Color(220, 220, 220); // Light gray
        
        setOpaque(false);
        setPreferredSize(new Dimension(starCount * (DEFAULT_STAR_SIZE + STAR_SPACING), DEFAULT_STAR_SIZE));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (editable) {
                    int newRating = getStarFromPosition(e.getPoint());
                    if (newRating == rating) {
                        rating = 0; // Toggle off if clicking the same star
                    } else {
                        rating = newRating;
                    }
                    repaint();
                    firePropertyChange("rating", 0, rating);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (editable) {
                    hoverRating = 0;
                    repaint();
                }
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (editable) {
                    int newHoverRating = getStarFromPosition(e.getPoint());
                    if (newHoverRating != hoverRating) {
                        hoverRating = newHoverRating;
                        repaint();
                    }
                }
            }
        });
    }
    
    private int getStarFromPosition(Point point) {
        int starWidth = DEFAULT_STAR_SIZE + STAR_SPACING;
        int star = (int) (point.getX() / starWidth) + 1;
        return Math.max(1, Math.min(star, starCount));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        for (int i = 0; i < starCount; i++) {
            int x = i * (DEFAULT_STAR_SIZE + STAR_SPACING);
            int y = 0;
            
            if (i < (hoverRating > 0 ? hoverRating : rating)) {
                g2d.setColor(hoverRating > 0 ? hoverColor : starColor);
            } else {
                g2d.setColor(emptyColor);
            }
            
            drawStar(g2d, x, y, DEFAULT_STAR_SIZE);
        }
        
        g2d.dispose();
    }
    
    private void drawStar(Graphics2D g2d, int x, int y, int size) {
        int[] xPoints = new int[10];
        int[] yPoints = new int[10];
        
        double radius = size / 2.0;
        double innerRadius = radius * 0.4;
        double angle = Math.PI / 5;
        
        for (int i = 0; i < 10; i++) {
            double r = (i % 2 == 0) ? radius : innerRadius;
            xPoints[i] = x + (int) (radius + r * Math.sin(i * angle));
            yPoints[i] = y + (int) (radius - r * Math.cos(i * angle));
        }
        
        g2d.fillPolygon(xPoints, yPoints, 10);
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = Math.max(0, Math.min(rating, starCount));
        repaint();
    }
    
    public boolean isEditable() {
        return editable;
    }
    
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
    public Color getStarColor() {
        return starColor;
    }
    
    public void setStarColor(Color starColor) {
        this.starColor = starColor;
        repaint();
    }
    
    public Color getHoverColor() {
        return hoverColor;
    }
    
    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }
    
    public Color getEmptyColor() {
        return emptyColor;
    }
    
    public void setEmptyColor(Color emptyColor) {
        this.emptyColor = emptyColor;
        repaint();
    }
}