package ui.admin.QLGNX;

import controller.GiaoNhanXeController;
import model.GiaoNhanXe;
import ui.admin.QLGNX.GiaoNhanXeDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GiaoNhanXePanel extends JPanel {
    private GiaoNhanXeController giaoNhanXeController;
    private GiaoNhanXeTablePanel tablePanel;
    private SearchFilterPanelGNX searchFilterPanel;
    private JButton btnThem, btnSua, btnXoa, btnXemChiTiet, btnRefresh;

    // Colors based on HopDongPanel styling
    private static final Color PRIMARY_COLOR = new Color(41, 121, 255); // Blue for Add
    private static final Color EDIT_COLOR = new Color(0, 150, 136); // Teal for Edit
    private static final Color DELETE_COLOR = new Color(211, 47, 47); // Red for Delete
    private static final Color VIEW_COLOR = new Color(33, 150, 243); // Light Blue for View
    private static final Color REFRESH_COLOR = new Color(96, 125, 139); // Grey for Refresh

    public GiaoNhanXePanel() {
        this.giaoNhanXeController = new GiaoNhanXeController();
        initComponents();
        loadDataToTable();
        setupTableInteractions();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        searchFilterPanel = new SearchFilterPanelGNX(this);
        add(searchFilterPanel, BorderLayout.NORTH);

        tablePanel = new GiaoNhanXeTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        JPanel pnlBottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        pnlBottomButtons.setBackground(Color.WHITE);

        btnThem = new JButton("Thêm Giao Nhận");
        styleButton(btnThem, PRIMARY_COLOR, 160);
        btnThem.addActionListener(e -> showGiaoNhanXeDialog(null, false));

        btnSua = new JButton("Sửa");
        styleButton(btnSua, EDIT_COLOR, 100);
        btnSua.addActionListener(e -> editSelectedGiaoNhan());
        btnSua.setEnabled(false);

        btnXoa = new JButton("Xóa");
        styleButton(btnXoa, DELETE_COLOR, 100);
        btnXoa.addActionListener(e -> deleteSelectedGiaoNhan());
        btnXoa.setEnabled(false);

        btnXemChiTiet = new JButton("Xem Chi Tiết");
        styleButton(btnXemChiTiet, VIEW_COLOR, 140);
        btnXemChiTiet.addActionListener(e -> viewSelectedGiaoNhan());
        btnXemChiTiet.setEnabled(false);

        btnRefresh = new JButton("Làm Mới");
        styleButton(btnRefresh, REFRESH_COLOR, 120);
        btnRefresh.addActionListener(e -> {
            searchFilterPanel.resetFilters();
            loadDataToTable();
        });

        // Thêm nút Demo Concurrency
        JButton btnDemoConcurrency = new JButton("Demo Concurrency");
        styleButton(btnDemoConcurrency, new Color(156, 39, 176), 140); // Purple color
        btnDemoConcurrency.addActionListener(e -> showConcurrencyDemo());

        pnlBottomButtons.add(btnThem);
        pnlBottomButtons.add(btnSua);
        pnlBottomButtons.add(btnXoa);
        pnlBottomButtons.add(btnXemChiTiet);
        pnlBottomButtons.add(btnRefresh);
        pnlBottomButtons.add(btnDemoConcurrency); // Thêm nút mới
        add(pnlBottomButtons, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button, Color color, int width) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(width, 35));
    }

    private void setupTableInteractions() {
        JTable tbl = tablePanel.getTable();
        tbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = tbl.getSelectedRow() != -1;
                btnSua.setEnabled(hasSelection);
                btnXoa.setEnabled(hasSelection);
                btnXemChiTiet.setEnabled(hasSelection);
            }
        });

        tbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewSelectedGiaoNhan();
                }
            }
        });
    }

    public void loadDataToTable() {
        List<GiaoNhanXe> danhSachGN = giaoNhanXeController.getAllGiaoNhanXe();
        tablePanel.updateData(danhSachGN);
        // After loading, selection is cleared, so disable buttons
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        btnXemChiTiet.setEnabled(false);
    }

    public void searchAndFilterGiaoNhan() {
        String keyword = searchFilterPanel.getSearchKeyword();
        String trangThaiFilter = searchFilterPanel.getSelectedTrangThaiFilter();

        List<GiaoNhanXe> fullList = giaoNhanXeController.searchGiaoNhanXe(keyword);
        List<GiaoNhanXe> filteredList = new ArrayList<>();

        if (fullList != null) {
            if ("Tất cả".equals(trangThaiFilter)) {
                filteredList.addAll(fullList);
            } else {
                filteredList = fullList.stream()
                        .filter(gn -> gn.getTrangThaiGN() != null && gn.getTrangThaiGN().equals(trangThaiFilter))
                        .collect(Collectors.toList());
            }
        }
        tablePanel.updateData(filteredList);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        btnXemChiTiet.setEnabled(false);
    }

    public void showGiaoNhanXeDialog(GiaoNhanXe gn, boolean readOnly) {
        GiaoNhanXeDialog dialog = new GiaoNhanXeDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                gn,
                this,
                readOnly);
        dialog.setVisible(true);
    }

    private void viewSelectedGiaoNhan() {
        String maGiaoNhan = tablePanel.getSelectedGiaoNhanXeId();
        if (maGiaoNhan != null) {
            GiaoNhanXe gn = giaoNhanXeController.getGiaoNhanXeByMa(maGiaoNhan);
            if (gn != null) {
                showGiaoNhanXeDialog(gn, true); // true for readOnly
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin giao nhận.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (tablePanel.getTable().getSelectedRow() != -1) { // If ID is null but a row is selected (should not
                                                                   // happen if ID is key)
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bản ghi giao nhận để xem.", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editSelectedGiaoNhan() {
        String maGiaoNhan = tablePanel.getSelectedGiaoNhanXeId();
        if (maGiaoNhan != null) {
            GiaoNhanXe gn = giaoNhanXeController.getGiaoNhanXeByMa(maGiaoNhan);
            if (gn != null) {

                showGiaoNhanXeDialog(gn, false); // false for editing
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin giao nhận để sửa.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (tablePanel.getTable().getSelectedRow() != -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bản ghi giao nhận để sửa.", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteSelectedGiaoNhan() {
        String maGiaoNhan = tablePanel.getSelectedGiaoNhanXeId();
        if (maGiaoNhan != null) {

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa bản ghi giao nhận này không? (Mã: " + maGiaoNhan + ")",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = giaoNhanXeController.deleteGiaoNhanXe(maGiaoNhan);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xóa bản ghi giao nhận thành công!", "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadDataToTable();
                } else {
                    String errMsg = giaoNhanXeController.getErrorMessage();
                    JOptionPane.showMessageDialog(this,
                            "Xóa thất bại: " + (errMsg.isEmpty() ? "Lỗi không xác định." : errMsg), "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (tablePanel.getTable().getSelectedRow() != -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bản ghi giao nhận để xóa.", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showConcurrencyDemo() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có muốn mở Demo Concurrency Control không?\n" +
                        "Chức năng này sẽ mở 2 giao dịch đồng thời để demo các vấn đề concurrency\n" +
                        "như Deadlock, Lock Wait, Dirty Read, Phantom Read, etc.",
                "Xác nhận Demo Concurrency",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            GiaoNhanXeConcurrencyDemo launcher = new GiaoNhanXeConcurrencyDemo();
            launcher.setVisible(true);
        }
    }
}