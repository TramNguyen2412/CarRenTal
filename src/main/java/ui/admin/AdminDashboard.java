package ui.admin;
import ui.admin.BaoDuong.BaoDuongPanel;
import ui.admin.CTBD.DichVuBDPanel;
import ui.admin.CongNo.CongNoPanel;
import java.net.URL;
import model.TaiKhoan;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import ui.admin.QLXe.XePanel;
import ui.admin.QLHD.HopDongPanel;
import ui.admin.ThongKePanel;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import ui.admin.QLTK.TaiKhoanPanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame; // Sửa đường dẫn package từ QLNV sang QLNhanVien
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import model.TaiKhoan;
import ui.admin.QLGNX.GiaoNhanXePanel;
import ui.admin.QLHD.HopDongPanel;
import ui.admin.QLKH.QuanLyKhachHangPanel;
import ui.admin.QLNV.NhanVienPanel;
import ui.admin.QLXe.XePanel;


public class AdminDashboard extends JFrame implements SidebarMenuPanel.MenuClickListener {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private TaiKhoan taiKhoan;
    
    // Các panel quản lý
    private TaiKhoanPanel taikhoanPanel;
    private QuanLyKhachHangPanel khachHangPanel;
    private NhanVienPanel nhanVienPanel; // Sửa kiểu dữ liệu từ JPanel sang NhanVienPanel
    private XePanel xePanel;
    private JPanel dichVuBDPanel;
    private HopDongPanel hopDongPanel;
    private JPanel baoDuongPanel;
    private JPanel congNoPanel;
    private ThongKePanel tkPanel;
    private GiaoNhanXePanel giaoNhanXePanel;
    private JPanel baoCaoPanel;
    
    public AdminDashboard(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Mở full màn hình
    }
    
    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Thuê Xe - CarRental");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        
        // Panel chính chứa tất cả
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Bỏ border
        
        SidebarMenuPanel menuPanel = new SidebarMenuPanel(taiKhoan, this);
        
        // Thiết lập content
        setupContentPanel();
        
        // Thêm vào main panel
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Thêm main panel vào frame
        getContentPane().add(mainPanel);
    }
    
    private void setupContentPanel() {
        // Thiết lập màu nền
        this.getContentPane().setBackground(new Color(240, 248, 255)); 
        
        contentPanel = new JPanel();
        contentPanel.setBackground(new Color(245, 245, 245));
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        
        // Tạo các panel chức năng
        taikhoanPanel = new TaiKhoanPanel();
        khachHangPanel = new QuanLyKhachHangPanel();
        nhanVienPanel = new NhanVienPanel();

        xePanel = new XePanel();
        dichVuBDPanel = new DichVuBDPanel();
        hopDongPanel = new HopDongPanel();
        baoDuongPanel = new BaoDuongPanel();
        congNoPanel = new CongNoPanel();

        giaoNhanXePanel = new GiaoNhanXePanel();
        tkPanel = new ThongKePanel();
        
        // Thêm các panel vào cardLayout
        contentPanel.add(taikhoanPanel, "taikhoan");
        contentPanel.add(khachHangPanel, "khachHang");
        contentPanel.add(nhanVienPanel, "nhanVien");
        contentPanel.add(xePanel, "xe");
     
     
        nhanVienPanel = new NhanVienPanel(); // Sửa tên biến từ NhanVienPanel sang nhanVienPanel
    
        contentPanel.add(dichVuBDPanel, "dichVuBD");
        contentPanel.add(hopDongPanel, "hopDong");
        contentPanel.add(baoDuongPanel, "baoDuong");
        contentPanel.add(congNoPanel, "congNo");
        contentPanel.add(giaoNhanXePanel, "giaoNhanXe");
        contentPanel.add(tkPanel, "baoCao");
        
        // Hiển thị panel mặc định
        cardLayout.show(contentPanel, "taikhoan");
    }
    
    // Xử lý khi click vào menu item
    @Override
    public void onMenuItemClicked(String panelName) {
        if ("logout".equals(panelName)) {
            logout();
        } else {
            cardLayout.show(contentPanel, panelName);
        }
    }
    
    // Phương thức đăng xuất
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn đăng xuất?", 
                "Xác nhận đăng xuất", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            try {
                
                ui.auth.LoginForm loginForm = new ui.auth.LoginForm();
                loginForm.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi đăng xuất: " + e.getMessage());
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
   
    // Main method cho testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set Look and Feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Test với tài khoản null
                AdminDashboard adminDashboard = new AdminDashboard(null);
                adminDashboard.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
