package ui.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import controller.DichVuBDController;
import model.DichVuBD;

public class DichVuBDPanel extends JPanel {
    private JTable tableDichVu;
    private DefaultTableModel modelDichVu;
    private JTextField txtSearch;
    private JButton btnAdd, btnRefresh, btnExport;
    private DichVuBDController dichVuBDController;
    private DecimalFormat currencyFormat;
    
    // Cột của bảng
    private final String[] COLUMNS = {
        "Mã DV", "Tên Dịch Vụ", "Giá Dịch Vụ", "Thao Tác"
    };

    public DichVuBDPanel() {
        dichVuBDController = new DichVuBDController();
        currencyFormat = new DecimalFormat("#,###");
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Panel tiêu đề
        JPanel pnlTitle = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ DỊCH VỤ BẢO DƯỠNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        pnlTitle.add(lblTitle, BorderLayout.WEST);
        
        // Panel tìm kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtSearch = new JTextField(20);
        btnRefresh = new JButton("Làm mới");
        btnExport = new JButton("Xuất Excel");
        
        pnlSearch.add(new JLabel("Tìm kiếm:"));
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnRefresh);
        pnlSearch.add(btnExport);
        
        pnlTitle.add(pnlSearch, BorderLayout.EAST);
        add(pnlTitle, BorderLayout.NORTH);
        
        // Panel thêm dịch vụ
        btnAdd = new JButton("Thêm dịch vụ");
        JPanel pnlAdd = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlAdd.add(btnAdd);
        add(pnlAdd, BorderLayout.SOUTH);
        
        // Bảng danh sách dịch vụ
        modelDichVu = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Chỉ cho phép chỉnh sửa cột "Thao tác"
            }
        };
        
        tableDichVu = new JTable(modelDichVu);
        tableDichVu.setRowHeight(40); // Chiều cao vừa phải
     
        // Thiết lập độ rộng cột
        TableColumnModel columnModel = tableDichVu.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);  // Mã DV
        columnModel.getColumn(1).setPreferredWidth(300); // Tên dịch vụ
        columnModel.getColumn(2).setPreferredWidth(150); // Giá dịch vụ
        columnModel.getColumn(3).setPreferredWidth(150); // Thao tác
        
        // Custom renderer cho cột thao tác
        tableDichVu.getColumnModel().getColumn(3).setCellRenderer(new ButtonRendererDichVuBD());
        tableDichVu.getColumnModel().getColumn(3).setCellEditor(new ButtonEditorDichVuBD(this));
        
        // Tùy chỉnh header
        JTableHeader header = tableDichVu.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(60, 60, 60));
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        // Tùy chỉnh grid lines
        tableDichVu.setShowGrid(true);
        tableDichVu.setGridColor(new Color(230, 230, 230));

        // Tạo hiệu ứng dòng sọc
        tableDichVu.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Thêm padding cho text trong ô
                if (comp instanceof JLabel) {
                    ((JLabel) comp).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                }

                // Màu nền dòng chẵn/lẻ nếu không được chọn và không phải cột thao tác
                if (!isSelected && column != 3) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    comp.setForeground(new Color(50, 50, 50));
                }

                // Căn phải cho cột giá tiền
                if (column == 2) { // Giá dịch vụ
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                return comp;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableDichVu);
        add(scrollPane, BorderLayout.CENTER);
        
        // Làm đẹp các nút
        styleButton(btnAdd, new Color(41, 121, 255));
        styleButton(btnRefresh, new Color(0, 150, 136));
        styleButton(btnExport, new Color(113, 85, 156));

        // Làm đẹp thanh tìm kiếm
        txtSearch.setPreferredSize(new Dimension(200, 30));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Thêm sự kiện
        btnAdd.addActionListener(e -> showDichVuDialog(null)); // null = thêm mới
        btnRefresh.addActionListener(e -> loadDataToTable());
        btnExport.addActionListener(e -> exportToExcel());
        
        txtSearch.addActionListener(e -> searchDichVu());
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
    }
    
    public void loadDataToTable() {
        modelDichVu.setRowCount(0); // Xóa dữ liệu cũ
        
        List<DichVuBD> danhSachDichVu = dichVuBDController.getAllDichVuBD();
        for (DichVuBD dv : danhSachDichVu) {
            modelDichVu.addRow(new Object[]{
                dv.getMaDV(),
                dv.getTenDV(),
                currencyFormat.format(dv.getGiaDV()) + " VNĐ",
                "" // Cột thao tác
            });
        }
    }

    private void searchDichVu() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadDataToTable();
            return;
        }
        
        modelDichVu.setRowCount(0);
        List<DichVuBD> danhSachDichVu = dichVuBDController.searchDichVuBD(keyword);
        
        for (DichVuBD dv : danhSachDichVu) {
            modelDichVu.addRow(new Object[]{
                dv.getMaDV(),
                dv.getTenDV(),
                currencyFormat.format(dv.getGiaDV()) + " VNĐ",
                "" // Cột thao tác
            });
        }
    }
    
    public void showDichVuDialog(DichVuBD dichVu) {
        DichVuDialog dialog = new DichVuDialog(SwingUtilities.getWindowAncestor(this), dichVu, this);
        dialog.setVisible(true);
    }
    
    private void exportToExcel() {
        JOptionPane.showMessageDialog(this, "Chức năng xuất Excel sẽ được phát triển sau!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Inner class for DichVu Dialog
    class DichVuDialog extends JDialog {
        private JTextField txtMaDV, txtTenDV, txtGiaDV;
        private JButton btnSave, btnCancel;
        private DichVuBD dichVu;
        private DichVuBDPanel parentPanel;
        
        public DichVuDialog(Window owner, DichVuBD dichVu, DichVuBDPanel parentPanel) {
            super(owner, dichVu == null ? "Thêm Dịch Vụ Mới" : "Cập Nhật Dịch Vụ", ModalityType.APPLICATION_MODAL);
            this.dichVu = dichVu;
            this.parentPanel = parentPanel;
            
            initComponents();
            if (dichVu != null) {
                loadDichVuData();
            }
            
            setSize(400, 300);
            setLocationRelativeTo(owner);
        }
        
        private void initComponents() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            
            // Mã dịch vụ
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("Mã dịch vụ:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            txtMaDV = new JTextField(20);
            txtMaDV.setEditable(false); // Không cho phép sửa mã
            panel.add(txtMaDV, gbc);
            
            // Tên dịch vụ
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0;
            panel.add(new JLabel("Tên dịch vụ:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.weightx = 1.0;
            txtTenDV = new JTextField(20);
            panel.add(txtTenDV, gbc);
            
            // Giá dịch vụ
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.weightx = 0;
            panel.add(new JLabel("Giá dịch vụ:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.weightx = 1.0;
            txtGiaDV = new JTextField(20);
            panel.add(txtGiaDV, gbc);
            
            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnSave = new JButton("Lưu");
            btnCancel = new JButton("Hủy");
            
            styleButton(btnSave, new Color(41, 121, 255));
            styleButton(btnCancel, new Color(150, 150, 150));
            
            buttonPanel.add(btnSave);
            buttonPanel.add(btnCancel);
            
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.weightx = 1.0;
            panel.add(buttonPanel, gbc);
            
            // Add event listeners
            btnSave.addActionListener(e -> saveDichVu());
            btnCancel.addActionListener(e -> dispose());
            
            getContentPane().add(panel);
        }
        
        private void loadDichVuData() {
            txtMaDV.setText(dichVu.getMaDV());
            txtTenDV.setText(dichVu.getTenDV());
            txtGiaDV.setText(String.valueOf(dichVu.getGiaDV()));
        }
        
        private void saveDichVu() {
            String tenDV = txtTenDV.getText().trim();
            String giaDVStr = txtGiaDV.getText().trim();
            
            if (tenDV.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên dịch vụ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double giaDV;
            try {
                giaDV = Double.parseDouble(giaDVStr);
                if (giaDV < 0) {
                    JOptionPane.showMessageDialog(this, "Giá dịch vụ không được âm", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Giá dịch vụ không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String result;
            if (dichVu == null) { // Thêm mới
                result = dichVuBDController.addDichVuBD(tenDV, giaDV);
            } else { // Cập nhật
                result = dichVuBDController.updateDichVuBD(dichVu.getMaDV(), tenDV, giaDV);
            }
            
            JOptionPane.showMessageDialog(this, result);
            
            if (result.contains("thành công")) {
                parentPanel.loadDataToTable();
                dispose();
            }
        }
    }
}