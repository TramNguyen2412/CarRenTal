package ui.admin.QLGNX;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DatabaseConfig;

public class GiaoNhanXeConcurrencyDemo extends JFrame {

    // Database connection
    private static Connection connection;

    // Shared data and locks between transactions
    private static final List<GiaoNhanXe> danhSachGiaoNhan = new ArrayList<>();
    private static final Map<String, Lock> recordLocks = new ConcurrentHashMap<>();
    private static final Map<String, String> lockedBy = new ConcurrentHashMap<>();

    // Demo scenarios
    private static boolean deadlockScenarioActive = false;
    private static boolean lostUpdateScenarioActive = false;

    // Timeout settings
    private static final int QUERY_TIMEOUT_SECONDS = 10;
    private static final int DEADLOCK_DETECTION_MS = 3000;

    // UI Colors - Match GiaoNhanXePanel exactly
    private static final Color PRIMARY_COLOR = new Color(41, 121, 255);
    private static final Color EDIT_COLOR = new Color(0, 150, 136);
    private static final Color DELETE_COLOR = new Color(211, 47, 47);
    private static final Color VIEW_COLOR = new Color(33, 150, 243);
    private static final Color REFRESH_COLOR = new Color(96, 125, 139);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color DEMO_COLOR = new Color(156, 39, 176);

    public GiaoNhanXeConcurrencyDemo() {
        initializeDatabase();
        loadDataFromDatabase();
        initializeUI();
    }

    private void initializeDatabase() {
        try {
            Class.forName(DatabaseConfig.DRIVER);
            connection = DriverManager.getConnection(
                    DatabaseConfig.URL,
                    DatabaseConfig.USERNAME,
                    DatabaseConfig.PASSWORD);
            connection.setAutoCommit(false); // Quan trọng cho transaction demo
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Không thể kết nối database: " + e.getMessage(),
                    "Lỗi Database",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDataFromDatabase() {
        danhSachGiaoNhan.clear();
        recordLocks.clear();
        lockedBy.clear();

        String sql = """
                    SELECT gn.MaGiaoNhan, gn.MaHD, kh.HoTen as TenKH,
                           gn.MaXe, x.TenXe || ' (' || x.BienSo || ')' as TenXe,
                           gn.MaNV, nv.HoTen as TenNV, gn.TrangThaiXe, gn.GhiChu, gn.TrangThaiGN
                    FROM GIAONHANXE gn
                    JOIN HOPDONG hd ON gn.MaHD = hd.MaHD
                    JOIN KHACHHANG kh ON hd.MaKH = kh.MaKH
                    JOIN XE x ON gn.MaXe = x.MaXe
                    JOIN NHANVIEN nv ON gn.MaNV = nv.MaNV
                    ORDER BY gn.MaGiaoNhan
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                GiaoNhanXe gnx = new GiaoNhanXe(
                        rs.getString("MaGiaoNhan"),
                        rs.getString("MaHD"),
                        rs.getString("TenKH"),
                        rs.getString("MaXe"),
                        rs.getString("TenXe"),
                        rs.getString("MaNV"),
                        rs.getString("TenNV"),
                        rs.getString("TrangThaiXe"),
                        rs.getString("GhiChu"),
                        rs.getString("TrangThaiGN"));
                danhSachGiaoNhan.add(gnx);

                // Initialize locks for each record
                recordLocks.put(gnx.getMaGiaoNhan(), new ReentrantLock());
            }

            System.out.println("Loaded " + danhSachGiaoNhan.size() + " records from database");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu: " + e.getMessage(),
                    "Lỗi Database",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeUI() {
        setTitle("Demo Concurrency Control - Quản Lý Giao Nhận Xe (Real Database)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main content with two transaction panels
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        TransactionPanel transaction1 = new TransactionPanel("Transaction-1", PRIMARY_COLOR);
        TransactionPanel transaction2 = new TransactionPanel("Transaction-2", DELETE_COLOR);

        mainPanel.add(transaction1);
        mainPanel.add(transaction2);

        add(mainPanel, BorderLayout.CENTER);

        // Bottom control panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);

        setSize(1800, 1000);
        setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Demo Concurrency Control - Giao Nhận Xe (Real Database)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);

        JLabel descLabel = new JLabel(
                "<html>Mô phỏng các vấn đề concurrency với Oracle Database thực tế - Lock detection và timeout handling</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(108, 117, 125));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.add(titleLabel);

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(descLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnDemoLostUpdate = new JButton("Demo Lost Update");
        styleButton(btnDemoLostUpdate, WARNING_COLOR, 160);
        btnDemoLostUpdate.addActionListener(e -> {
            lostUpdateScenarioActive = true;
            deadlockScenarioActive = false;
            showDemoInstructions("Lost Update", getLostUpdateInstructions());
        });

        JButton btnDemoDeadlock = new JButton("Demo Deadlock");
        styleButton(btnDemoDeadlock, DELETE_COLOR, 140);
        btnDemoDeadlock.addActionListener(e -> {
            deadlockScenarioActive = true;
            lostUpdateScenarioActive = false;
            showDemoInstructions("Deadlock", getDeadlockInstructions());
        });

        JButton btnResetDemo = new JButton("Làm Mới");
        styleButton(btnResetDemo, REFRESH_COLOR, 120);
        btnResetDemo.addActionListener(e -> {
            deadlockScenarioActive = false;
            lostUpdateScenarioActive = false;
            recordLocks.clear();
            lockedBy.clear();
            loadDataFromDatabase();
            JOptionPane.showMessageDialog(this, "Đã reset demo và tải lại dữ liệu từ database!", "Reset Demo",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnCloseDemo = new JButton("Đóng Demo");
        styleButton(btnCloseDemo, new Color(108, 117, 125), 120);
        btnCloseDemo.addActionListener(e -> {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            dispose();
        });

        panel.add(btnDemoLostUpdate);
        panel.add(btnDemoDeadlock);
        panel.add(btnResetDemo);
        panel.add(btnCloseDemo);

        return panel;
    }

    private void styleButton(JButton button, Color color, int baseWidth) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12)); // font lớn hơn
        int width = baseWidth + 20; // tăng thêm 20px chiều rộng
        button.setPreferredSize(new Dimension(width, 35)); // chiều cao 35px
        button.setMargin(new Insets(5, 10, 5, 10)); // padding trong nút
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private static void styleCompactButton(JButton button, Color color, int baseWidth) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12)); // font lớn hơn
        int width = baseWidth + 20; // tăng thêm 20px chiều rộng
        button.setPreferredSize(new Dimension(width, 35)); // chiều cao 35px
        button.setMargin(new Insets(5, 10, 5, 10)); // padding trong nút
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private String getLostUpdateInstructions() {
        return "<html><body style='width: 400px'>" +
                "<h3>Hướng Dẫn Demo Lost Update (Database):</h3>" +
                "<ol>" +
                "<li><b>Bắt đầu transaction</b> ở cả hai cửa sổ</li>" +
                "<li><b>Chọn cùng một bản ghi</b> ở cả hai transaction</li>" +
                "<li><b>Transaction-1:</b> Sửa đổi và nhấn UPDATE</li>" +
                "<li><b>Transaction-2:</b> Sửa đổi và nhấn UPDATE (bị block bởi database lock)</li>" +
                "<li><b>COMMIT Transaction-1</b> (giải phóng database lock)</li>" +
                "<li><b>Transaction-2:</b> Tiếp tục update</li>" +
                "<li><b>COMMIT Transaction-2</b></li>" +
                "<li><b>Quan sát:</b> Thay đổi từ Transaction-1 bị mất trong database</li>" +
                "</ol>" +
                "<p><b>Real Database:</b> Oracle lock timeout " + QUERY_TIMEOUT_SECONDS + " giây</p>" +
                "</body></html>";
    }

    private String getDeadlockInstructions() {
        return "<html><body style='width: 400px'>" +
                "<h3>Hướng Dẫn Demo Deadlock (Database):</h3>" +
                "<ol>" +
                "<li><b>Bắt đầu transaction</b> ở cả hai cửa sổ</li>" +
                "<li><b>Transaction-1:</b> Chọn GN001 → UPDATE (lock row trong DB)</li>" +
                "<li><b>Transaction-2:</b> Chọn GN002 → UPDATE (lock row trong DB)</li>" +
                "<li><b>Transaction-1:</b> Chọn GN002 → UPDATE (bị block)</li>" +
                "<li><b>Transaction-2:</b> Chọn GN001 → UPDATE (tạo deadlock!)</li>" +
                "<li><b>Kết quả:</b> Oracle ORA-00060 deadlock detected, một transaction rollback</li>" +
                "</ol>" +
                "<p><b>Real Oracle:</b> Automatic deadlock detection và victim selection</p>" +
                "</body></html>";
    }

    private void showDemoInstructions(String title, String instructions) {
        JOptionPane.showMessageDialog(this, instructions, "Hướng Dẫn " + title, JOptionPane.INFORMATION_MESSAGE);
    }

    // GiaoNhanXe model class
    static class GiaoNhanXe {
        private String maGiaoNhan, maHD, tenKH, maXe, tenXe, maNV, tenNV;
        private String trangThaiXe, ghiChu, trangThaiGN;
        private java.util.Date ngayGiaoNhan;
        private int version = 1;

        public GiaoNhanXe(String maGiaoNhan, String maHD, String tenKH, String maXe, String tenXe,
                String maNV, String tenNV, String trangThaiXe, String ghiChu, String trangThaiGN) {
            this.maGiaoNhan = maGiaoNhan;
            this.maHD = maHD;
            this.tenKH = tenKH;
            this.maXe = maXe;
            this.tenXe = tenXe;
            this.maNV = maNV;
            this.tenNV = tenNV;
            this.trangThaiXe = trangThaiXe;
            this.ghiChu = ghiChu;
            this.trangThaiGN = trangThaiGN;
            this.ngayGiaoNhan = new java.util.Date();
        }

        // Getters and setters
        public String getMaGiaoNhan() {
            return maGiaoNhan;
        }

        public String getMaHD() {
            return maHD;
        }

        public String getTenKH() {
            return tenKH;
        }

        public String getMaXe() {
            return maXe;
        }

        public String getTenXe() {
            return tenXe;
        }

        public String getMaNV() {
            return maNV;
        }

        public String getTenNV() {
            return tenNV;
        }

        public String getTrangThaiXe() {
            return trangThaiXe;
        }

        public void setTrangThaiXe(String trangThaiXe) {
            this.trangThaiXe = trangThaiXe;
        }

        public String getGhiChu() {
            return ghiChu;
        }

        public void setGhiChu(String ghiChu) {
            this.ghiChu = ghiChu;
        }

        public String getTrangThaiGN() {
            return trangThaiGN;
        }

        public void setTrangThaiGN(String trangThaiGN) {
            this.trangThaiGN = trangThaiGN;
        }

        public java.util.Date getNgayGiaoNhan() {
            return ngayGiaoNhan;
        }

        public int getVersion() {
            return version;
        }

        public void incrementVersion() {
            this.version++;
        }
    }

    // Transaction Panel class với database operations
    static class TransactionPanel extends JPanel {
        private final String transactionName;
        private final Color transactionColor;
        private boolean transactionActive = false;
        private String isolationLevel = "READ_COMMITTED";
        private Connection transactionConnection;

        // UI Components (giữ nguyên)
        private JLabel statusLabel;
        private JTextArea logArea;
        private JTable table;
        private DefaultTableModel tableModel;
        private JTextField txtSearch;
        private JComboBox<String> cboTrangThaiFilter;
        private JTextField txtMaGiaoNhan, txtMaHD, txtTenKH, txtTenXe;
        private JTextArea txtTrangThaiXe, txtGhiChu;
        private JComboBox<String> cboTrangThaiGN, cboIsolationLevel;
        private JButton btnStartTransaction, btnCommit, btnRollback;
        private JButton btnReadData, btnUpdateData, btnRefresh;

        // Transaction state
        private final List<GiaoNhanXe> workingCopy = new ArrayList<>();
        private final Set<String> lockedRecords = new HashSet<>();
        private GiaoNhanXe selectedRecord = null;

        public TransactionPanel(String transactionName, Color transactionColor) {
            this.transactionName = transactionName;
            this.transactionColor = transactionColor;
            initializeUI();
            setupEventHandlers();
            // lúc mới chỉ có nút Start bật, các nút khác tắt
            setOperationButtonsEnabled(false);
        }

        private void setOperationButtonsEnabled(boolean enabled) {
            btnReadData.setEnabled(enabled);
            btnUpdateData.setEnabled(enabled);
            btnRefresh.setEnabled(enabled);
        }

        // Database transaction methods
        private void startTransaction() {
            try {
                // Create new connection for this transaction
                transactionConnection = DriverManager.getConnection(
                        DatabaseConfig.URL,
                        DatabaseConfig.USERNAME,
                        DatabaseConfig.PASSWORD);

                transactionConnection.setAutoCommit(false);

                // Set isolation level
                int isolationInt = getIsolationLevel(isolationLevel);
                transactionConnection.setTransactionIsolation(isolationInt);

                transactionActive = true;
                statusLabel.setText("● Transaction ON (" + isolationLevel + ")");
                statusLabel.setForeground(SUCCESS_COLOR);

                btnStartTransaction.setEnabled(false);
                btnCommit.setEnabled(true);
                btnRollback.setEnabled(true);
                cboIsolationLevel.setEnabled(false);
                setOperationButtonsEnabled(true);

                log("=== TRANSACTION STARTED ===");
                readData();

            } catch (SQLException e) {
                log("ERROR: " + e.getMessage());
            }
        }

        private void commitTransaction() {
            if (!transactionActive)
                return;
            try {
                log("Committing…");
                transactionConnection.commit();
                log("=== TRANSACTION COMMITTED ===");
            } catch (SQLException e) {
                log("ERROR: " + e.getMessage());
            } finally {
                endTransaction();
            }
        }

        private void rollbackTransaction() {
            if (!transactionActive)
                return;
            try {
                log("Rolling back…");
                transactionConnection.rollback();
                log("=== TRANSACTION ROLLED BACK ===");
            } catch (SQLException e) {
                log("ERROR: " + e.getMessage());
            } finally {
                endTransaction();
            }
        }

        private void endTransaction() {
            transactionActive = false;
            try {
                transactionConnection.close();
            } catch (Exception ignore) {
            }
            statusLabel.setText("● Transaction OFF");
            statusLabel.setForeground(Color.GRAY);

            btnStartTransaction.setEnabled(true);
            btnCommit.setEnabled(false);
            btnRollback.setEnabled(false);
            cboIsolationLevel.setEnabled(true);
            setOperationButtonsEnabled(false);

            clearForm();
            refreshTable();
        }

        private void readData() {
            log("READ: tải dữ liệu…");
            loadWorkingCopy(); // hoặc loadDataFromDatabase() nếu chưa start
            refreshTable();
        }

        private void loadWorkingCopy() {
            workingCopy.clear();
            synchronized (danhSachGiaoNhan) {
                for (GiaoNhanXe gnx : danhSachGiaoNhan) {
                    workingCopy.add(new GiaoNhanXe(
                            gnx.getMaGiaoNhan(), gnx.getMaHD(), gnx.getTenKH(),
                            gnx.getMaXe(), gnx.getTenXe(), gnx.getMaNV(), gnx.getTenNV(),
                            gnx.getTrangThaiXe(), gnx.getGhiChu(), gnx.getTrangThaiGN()));
                }
            }
            log("Loaded " + workingCopy.size() + " records into working copy");
        }

        private void updateData() {
            if (selectedRecord == null) {
                log("ERROR: No record selected");
                return;
            }

            String maGN = selectedRecord.getMaGiaoNhan();
            log("ATTEMPTING DATABASE UPDATE: " + maGN);

            // Use SwingWorker for database update with timeout
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    String updateSql = """
                                UPDATE GIAONHANXE
                                SET TrangThaiXe = ?, GhiChu = ?, TrangThaiGN = ?
                                WHERE MaGiaoNhan = ?
                            """;

                    try (PreparedStatement stmt = transactionConnection.prepareStatement(updateSql)) {
                        // Set query timeout
                        stmt.setQueryTimeout(QUERY_TIMEOUT_SECONDS);

                        stmt.setString(1, txtTrangThaiXe.getText());
                        stmt.setString(2, txtGhiChu.getText());
                        stmt.setString(3, (String) cboTrangThaiGN.getSelectedItem());
                        stmt.setString(4, maGN);

                        int rowsUpdated = stmt.executeUpdate();
                        return rowsUpdated > 0;

                    } catch (SQLException e) {
                        // Check for Oracle deadlock error
                        if (e.getErrorCode() == 60) { // ORA-00060
                            throw new RuntimeException("ORA-00060: deadlock detected while waiting for resource");
                        } else if (e.getErrorCode() == 30006) { // ORA-30006 (timeout)
                            throw new RuntimeException(
                                    "ORA-30006: resource busy; acquire with NOWAIT specified or timeout expired");
                        }
                        throw e;
                    }
                }

                @Override
                protected void done() {
                    try {
                        Boolean success = get();
                        if (success != null && success) {
                            lockedRecords.add(maGN);
                            lockedBy.put(maGN, transactionName);

                            // Update working copy
                            updateWorkingCopy(maGN);

                            log("UPDATE: Successfully updated " + maGN + " (uncommitted in database)");
                            refreshTable();
                        } else {
                            log("UPDATE: No rows were updated");
                        }
                    } catch (Exception e) {
                        if (e.getCause() instanceof RuntimeException) {
                            RuntimeException re = (RuntimeException) e.getCause();
                            if (re.getMessage().contains("ORA-00060")) {
                                log("ORA-00060: deadlock detected while waiting for resource");
                                log("Current statement is being terminated");
                                rollbackTransaction();
                            } else if (re.getMessage().contains("ORA-30006")) {
                                log("ORA-30006: resource busy; acquire with NOWAIT specified or timeout expired");
                                log("Another transaction is locking this record");
                            } else {
                                log("UPDATE ERROR: " + re.getMessage());
                            }
                        } else {
                            log("UPDATE ERROR: " + e.getMessage());
                        }
                    }
                }
            };
            worker.execute();
        }

        private int getIsolationLevel(String level) {
            switch (level) {
                case "READ_UNCOMMITTED":
                    return Connection.TRANSACTION_READ_UNCOMMITTED;
                case "READ_COMMITTED":
                    return Connection.TRANSACTION_READ_COMMITTED;
                case "REPEATABLE_READ":
                    return Connection.TRANSACTION_REPEATABLE_READ;
                case "SERIALIZABLE":
                    return Connection.TRANSACTION_SERIALIZABLE;
                default:
                    return Connection.TRANSACTION_READ_COMMITTED;
            }
        }

        private void updateWorkingCopy(String maGN) {
            for (GiaoNhanXe gnx : workingCopy) {
                if (gnx.getMaGiaoNhan().equals(maGN)) {
                    gnx.setTrangThaiXe(txtTrangThaiXe.getText());
                    gnx.setGhiChu(txtGhiChu.getText());
                    gnx.setTrangThaiGN((String) cboTrangThaiGN.getSelectedItem());
                    gnx.incrementVersion();
                    break;
                }
            }
        }

        private void log(String message) {
            String timestamp = java.time.LocalTime.now().toString().substring(0, 8);
            String logMessage = "[" + timestamp + "] " + transactionName + ": " + message + "\n";
            logArea.append(logMessage);
            logArea.setCaretPosition(logArea.getDocument().getLength());
            System.out.println(logMessage.trim());
        }

        // UI Methods (giữ nguyên toàn bộ UI code từ version trước)
        private void initializeUI() {
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createLineBorder(transactionColor, 2),
                            transactionName + " (Real Database)",
                            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                            javax.swing.border.TitledBorder.DEFAULT_POSITION,
                            new Font("Segoe UI", Font.BOLD, 14),
                            transactionColor),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));

            JPanel topPanel = createTopPanel();
            add(topPanel, BorderLayout.NORTH);

            JPanel centerPanel = createCenterPanel();
            add(centerPanel, BorderLayout.CENTER);

            JPanel bottomPanel = createBottomPanel();
            add(bottomPanel, BorderLayout.SOUTH);

            refreshTable();
        }

        private JPanel createTopPanel() {
            JPanel panel = new JPanel(new BorderLayout(5, 5));
            panel.setBackground(Color.WHITE);
            panel.setPreferredSize(new Dimension(0, 100));

            JPanel statusPanel = new JPanel(new BorderLayout(5, 3));
            statusPanel.setBackground(BACKGROUND_COLOR);
            statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            statusLabel = new JLabel("● Chưa bắt đầu transaction [Database Connection]");
            statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            statusLabel.setForeground(Color.GRAY);

            JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            controlsPanel.setBackground(BACKGROUND_COLOR);

            JLabel lblIsolation = new JLabel("Isolation:");
            lblIsolation.setFont(new Font("Segoe UI", Font.BOLD, 11));
            controlsPanel.add(lblIsolation);

            cboIsolationLevel = new JComboBox<>(new String[] {
                    "READ_UNCOMMITTED", "READ_COMMITTED", "REPEATABLE_READ", "SERIALIZABLE"
            });
            cboIsolationLevel.setSelectedIndex(1);
            cboIsolationLevel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            cboIsolationLevel.setPreferredSize(new Dimension(120, 25));
            controlsPanel.add(cboIsolationLevel);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
            buttonPanel.setBackground(BACKGROUND_COLOR);

            btnStartTransaction = new JButton("Bắt Đầu");
            styleCompactButton(btnStartTransaction, PRIMARY_COLOR, 70);

            btnCommit = new JButton("COMMIT");
            styleCompactButton(btnCommit, SUCCESS_COLOR, 70);
            btnCommit.setEnabled(false);

            btnRollback = new JButton("ROLLBACK");
            styleCompactButton(btnRollback, DELETE_COLOR, 80);
            btnRollback.setEnabled(false);

            buttonPanel.add(btnStartTransaction);
            buttonPanel.add(btnCommit);
            buttonPanel.add(btnRollback);

            statusPanel.add(statusLabel, BorderLayout.WEST);
            statusPanel.add(controlsPanel, BorderLayout.CENTER);
            statusPanel.add(buttonPanel, BorderLayout.EAST);

            JPanel searchPanel = createCompactSearchPanel();

            panel.add(statusPanel, BorderLayout.NORTH);
            panel.add(searchPanel, BorderLayout.SOUTH);

            return panel;
        }

        private JPanel createCompactSearchPanel() {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
            panel.setBackground(BACKGROUND_COLOR);
            panel.setBorder(BorderFactory.createEmptyBorder(2, 10, 5, 10));

            JLabel lblSearch = new JLabel("Tìm:");
            lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 11));
            panel.add(lblSearch);

            txtSearch = new JTextField(15);
            txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            txtSearch.setPreferredSize(new Dimension(120, 25));
            panel.add(txtSearch);

            JLabel lblTrangThai = new JLabel("TT:");
            lblTrangThai.setFont(new Font("Segoe UI", Font.BOLD, 11));
            panel.add(lblTrangThai);

            cboTrangThaiFilter = new JComboBox<>(new String[] { "Tất cả", "Đã giao", "Đã nhận về" });
            cboTrangThaiFilter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            cboTrangThaiFilter.setPreferredSize(new Dimension(80, 25));
            panel.add(cboTrangThaiFilter);

            return panel;
        }

        private JPanel createCenterPanel() {
            JPanel panel = new JPanel(new BorderLayout(5, 5));
            panel.setBackground(Color.WHITE);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setResizeWeight(0.65);
            splitPane.setDividerSize(3);
            splitPane.setBorder(null);
            splitPane.setOneTouchExpandable(true);

            JPanel tablePanel = createCompactTablePanel();
            splitPane.setLeftComponent(tablePanel);

            JPanel formPanel = createCompactFormPanel();
            splitPane.setRightComponent(formPanel);

            panel.add(splitPane, BorderLayout.CENTER);

            return panel;
        }

        private JPanel createCompactTablePanel() {
            JPanel panel = new JPanel(new BorderLayout(2, 2));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createTitledBorder("Dữ Liệu Database"));

            String[] columns = { "Mã GN", "HĐ", "Khách Hàng", "Xe", "TT GN", "Ver", "Lock" };
            tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            table = new JTable(tableModel);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setRowHeight(30);
            table.setShowGrid(true);
            table.setGridColor(new Color(230, 230, 230));
            table.setFont(new Font("Segoe UI", Font.PLAIN, 11));

            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 11));
            header.setBackground(new Color(240, 240, 240));
            header.setForeground(new Color(60, 60, 60));
            header.setPreferredSize(new Dimension(0, 28));

            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(60);
            columnModel.getColumn(1).setPreferredWidth(50);
            columnModel.getColumn(2).setPreferredWidth(100);
            columnModel.getColumn(3).setPreferredWidth(120);
            columnModel.getColumn(4).setPreferredWidth(60);
            columnModel.getColumn(5).setPreferredWidth(35);
            columnModel.getColumn(6).setPreferredWidth(45);

            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                            column);

                    if (!isSelected) {
                        comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                        comp.setForeground(new Color(50, 50, 50));

                        if (column == 6 && value != null && !"Free".equals(value)) {
                            comp.setForeground(Color.WHITE);
                            comp.setBackground(DELETE_COLOR);
                        }
                    }

                    setHorizontalAlignment(SwingConstants.LEFT);
                    return comp;
                }
            });

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.getViewport().setBackground(Color.WHITE);
            scrollPane.setPreferredSize(new Dimension(0, 200));

            panel.add(scrollPane, BorderLayout.CENTER);

            return panel;
        }

        private JPanel createCompactFormPanel() {
            JPanel panel = new JPanel(new BorderLayout(5, 5));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createTitledBorder("Chi Tiết"));

            // Form fields
            JPanel formContent = new JPanel(new GridBagLayout());
            formContent.setBackground(Color.WHITE);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(3, 3, 3, 3);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Initialize form components
            txtMaGiaoNhan = new JTextField();
            txtMaGiaoNhan.setEditable(false);
            txtMaHD = new JTextField();
            txtMaHD.setEditable(false);
            txtTenKH = new JTextField();
            txtTenKH.setEditable(false);
            txtTenXe = new JTextField();
            txtTenXe.setEditable(false);

            txtTrangThaiXe = new JTextArea(2, 15);
            txtTrangThaiXe.setLineWrap(true);
            txtGhiChu = new JTextArea(2, 15);
            txtGhiChu.setLineWrap(true);
            cboTrangThaiGN = new JComboBox<>(new String[] { "Đã giao", "Đã nhận về" });

            // Add fields to form
            int row = 0;
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            formContent.add(new JLabel("Mã GN:"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            formContent.add(txtMaGiaoNhan, gbc);
            row++;

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            formContent.add(new JLabel("HĐ:"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            formContent.add(txtMaHD, gbc);
            row++;

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            formContent.add(new JLabel("KH:"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            formContent.add(txtTenKH, gbc);
            row++;

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            formContent.add(new JLabel("Xe:"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            formContent.add(txtTenXe, gbc);
            row++;

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            formContent.add(new JLabel("TT Xe:"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            formContent.add(new JScrollPane(txtTrangThaiXe), gbc);
            row++;

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            formContent.add(new JLabel("Ghi chú:"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            formContent.add(new JScrollPane(txtGhiChu), gbc);
            row++;

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            formContent.add(new JLabel("TT GN:"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            formContent.add(cboTrangThaiGN, gbc);

            panel.add(formContent, BorderLayout.CENTER);

            // Log area
            logArea = new JTextArea(5, 0);
            logArea.setEditable(false);
            logArea.setFont(new Font("Consolas", Font.PLAIN, 9));
            logArea.setBackground(new Color(248, 249, 250));

            JScrollPane logScrollPane = new JScrollPane(logArea);
            logScrollPane.setBorder(BorderFactory.createTitledBorder("Database Transaction Log"));
            logScrollPane.setPreferredSize(new Dimension(0, 100));

            panel.add(logScrollPane, BorderLayout.SOUTH);

            return panel;
        }

        private JPanel createBottomPanel() {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
            panel.setBackground(Color.WHITE);
            panel.setPreferredSize(new Dimension(0, 45)); // cao hơn để không cắt

            btnReadData = new JButton("Đọc");
            styleCompactButton(btnReadData, VIEW_COLOR, 80); // rộng 80px

            btnUpdateData = new JButton("Cập Nhật");
            styleCompactButton(btnUpdateData, EDIT_COLOR, 100); // rộng 100px

            btnRefresh = new JButton("Refresh");
            styleCompactButton(btnRefresh, REFRESH_COLOR, 90); // rộng 90px

            panel.add(btnReadData);
            panel.add(btnUpdateData);
            panel.add(btnRefresh);

            return panel;
        }

        private void setupEventHandlers() {
            btnStartTransaction.addActionListener(e -> startTransaction());
            btnCommit.addActionListener(e -> commitTransaction());
            btnRollback.addActionListener(e -> rollbackTransaction());

            btnUpdateData.addActionListener(e -> updateData());

            table.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        String maGN = (String) table.getValueAt(selectedRow, 0);
                        selectRecord(maGN);
                    }
                }
            });

            txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    performSearch();
                }

                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    performSearch();
                }

                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    performSearch();
                }
            });

            cboTrangThaiFilter.addActionListener(e -> performSearch());
        }

        private void selectRecord(String maGN) {
            List<GiaoNhanXe> dataSource = transactionActive ? workingCopy : danhSachGiaoNhan;

            for (GiaoNhanXe gnx : dataSource) {
                if (gnx.getMaGiaoNhan().equals(maGN)) {
                    selectedRecord = gnx;
                    populateForm(gnx);
                    break;
                }
            }
        }

        private void populateForm(GiaoNhanXe gnx) {
            txtMaGiaoNhan.setText(gnx.getMaGiaoNhan());
            txtMaHD.setText(gnx.getMaHD());
            txtTenKH.setText(gnx.getTenKH());
            txtTenXe.setText(gnx.getTenXe());
            txtTrangThaiXe.setText(gnx.getTrangThaiXe());
            txtGhiChu.setText(gnx.getGhiChu());
            cboTrangThaiGN.setSelectedItem(gnx.getTrangThaiGN());
        }

        private void clearForm() {
            selectedRecord = null;
            txtMaGiaoNhan.setText("");
            txtMaHD.setText("");
            txtTenKH.setText("");
            txtTenXe.setText("");
            txtTrangThaiXe.setText("");
            txtGhiChu.setText("");
            cboTrangThaiGN.setSelectedIndex(0);
        }

        private void refreshTable() {
            List<GiaoNhanXe> dataSource = transactionActive ? workingCopy : danhSachGiaoNhan;
            updateTableData(dataSource);
        }

        private void performSearch() {
            List<GiaoNhanXe> dataSource = transactionActive ? workingCopy : danhSachGiaoNhan;

            String keyword = txtSearch.getText().trim().toLowerCase();
            String statusFilter = (String) cboTrangThaiFilter.getSelectedItem();

            List<GiaoNhanXe> filteredData = new ArrayList<>();

            for (GiaoNhanXe gnx : dataSource) {
                boolean matchKeyword = keyword.isEmpty() ||
                        gnx.getMaGiaoNhan().toLowerCase().contains(keyword) ||
                        gnx.getMaHD().toLowerCase().contains(keyword) ||
                        gnx.getTenKH().toLowerCase().contains(keyword) ||
                        gnx.getTenXe().toLowerCase().contains(keyword);

                boolean matchStatus = "Tất cả".equals(statusFilter) ||
                        gnx.getTrangThaiGN().equals(statusFilter);

                if (matchKeyword && matchStatus) {
                    filteredData.add(gnx);
                }
            }

            updateTableData(filteredData);
        }

        private void updateTableData(List<GiaoNhanXe> data) {
            tableModel.setRowCount(0);

            for (GiaoNhanXe gnx : data) {
                String lockStatus = lockedBy.containsKey(gnx.getMaGiaoNhan()) ? lockedBy.get(gnx.getMaGiaoNhan())
                        : "Free";

                tableModel.addRow(new Object[] {
                        gnx.getMaGiaoNhan(), gnx.getMaHD(), gnx.getTenKH(),
                        gnx.getTenXe(), gnx.getTrangThaiGN(),
                        gnx.getVersion(), lockStatus
                });
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }

            GiaoNhanXeConcurrencyDemo demo = new GiaoNhanXeConcurrencyDemo();
            demo.setVisible(true);
        });
    }
}