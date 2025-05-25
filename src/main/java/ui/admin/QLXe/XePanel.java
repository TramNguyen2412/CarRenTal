
package ui.admin.QLXe;

import controller.XeController;
import model.Xe;
import util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

public class XePanel extends JPanel {
    private XeController xeController;
    private SearchFilterPanel searchFilterPanel;
    private XeTablePanel tablePanel;
    private JButton btnAdd;
    
    public static String getImagePath(String fileName) {
        return ImageUtil.getImageDirPath() + fileName;
    }

    public XePanel() {
        xeController = new XeController();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
      //  setBackground(new Color(240, 248, 255)); // Màu nền xanh nhạt
        
        // 1. Panel tiêu đề
        JPanel pnlTitle = new JPanel(new BorderLayout());
     //   pnlTitle.setBackground(Color.WHITE);
        
        pnlTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel lblTitle = new JLabel("QUẢN LÝ XE");
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        
        pnlTitle.add(lblTitle, BorderLayout.WEST);
        
        
        // 2. Tạo SearchFilterPanel
        searchFilterPanel = new SearchFilterPanel();
        pnlTitle.add(searchFilterPanel, BorderLayout.EAST);
        add(pnlTitle, BorderLayout.NORTH);
        
        // 3. Tạo XeTablePanel
        tablePanel = new XeTablePanel(this);
        add(tablePanel, BorderLayout.CENTER);
        
        // 4. Panel nút thêm xe
        btnAdd = new JButton("Thêm xe");
        styleButton(btnAdd, new Color(41, 121, 255));
        
        JPanel pnlAdd = new JPanel(new FlowLayout(FlowLayout.LEFT));
      
        pnlAdd.add(btnAdd);
        add(pnlAdd, BorderLayout.SOUTH);
        
        // 5. Thêm các sự kiện
        btnAdd.addActionListener(e -> showXeDialog(null));
        
        searchFilterPanel.addSearchActionListener(e -> searchXe());
        searchFilterPanel.addFilterActionListener(e -> filterXe());
        searchFilterPanel.addRefreshActionListener(e -> loadDataToTable());
   
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
    }
    
    public void loadDataToTable() {
        List<Xe> danhSachXe = xeController.getAllXe();
        tablePanel.updateData(danhSachXe);
        searchFilterPanel.resetFilter();
    }

    private void searchXe() {
        String keyword = searchFilterPanel.getSearchText();
        if (keyword.isEmpty()) {
            loadDataToTable();
            return;
        }
        
        List<Xe> danhSachXe = xeController.searchXe(keyword);
        tablePanel.updateData(danhSachXe);
    }
    
    private void filterXe() {
        String filter = searchFilterPanel.getSelectedFilter();
        if (filter.equals("Tất cả")) {
            loadDataToTable();
            return;
        }
        
        List<Xe> danhSachXe = xeController.getXeByTrangThai(filter);
        tablePanel.updateData(danhSachXe);
    }
    
    public void showXeDialog(Xe xe) {
        XeDialog dialog = new XeDialog(SwingUtilities.getWindowAncestor(this), xe, this);
        dialog.setVisible(true);
    }

    public void showXeDetailDialog(Xe xe) {
        XeDetailDialog dialog = new XeDetailDialog(SwingUtilities.getWindowAncestor(this), xe, this);
        dialog.setVisible(true);
    }
    
    // Phương thức lấy xe từ DB theo mã
    public Xe getXeById(String maXe) {
        return xeController.getXeByMa(maXe);
    }
    public boolean deleteXe(String maXe) {
        return xeController.deleteXe(maXe);
    }
}