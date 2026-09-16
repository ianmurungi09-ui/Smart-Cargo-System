package smartcargosystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class MainDashboardFrame extends JFrame {

    private JTable tableCargo;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField txtSearch;
    
    // Live Stats Labels
    private JLabel lblTotalCount;
    private JLabel lblPendingCount;
    private JLabel lblDeliveredCount;

    public MainDashboardFrame() {
        initComponents();
        loadCargoData();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("SmartCargoDB - Dashboard");
        setSize(950, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(41, 128, 185));
        
        JLabel lblTitle = new JLabel("  Smart Cargo Management Dashboard");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        panelHeader.add(lblTitle, BorderLayout.WEST);
        
        add(panelHeader, BorderLayout.NORTH);

        // Live Statistics Panel (Analytics Header)
        JPanel panelStats = new JPanel(new GridLayout(1, 3, 10, 10));
        panelStats.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        panelStats.setBackground(new Color(230, 240, 250));

        lblTotalCount = new JLabel("Total Shipments: 0", SwingConstants.CENTER);
        lblPendingCount = new JLabel("Pending: 0", SwingConstants.CENTER);
        lblDeliveredCount = new JLabel("Delivered: 0", SwingConstants.CENTER);

        Font statFont = new Font("Segoe UI", Font.BOLD, 13);
        lblTotalCount.setFont(statFont);
        lblPendingCount.setFont(statFont);
        lblDeliveredCount.setFont(statFont);

        panelStats.add(lblTotalCount);
        panelStats.add(lblPendingCount);
        panelStats.add(lblDeliveredCount);

        // Search / Filter Panel
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelSearch.setBackground(new Color(245, 247, 250));
        JLabel lblSearch = new JLabel("Search Shipment:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtSearch = new JTextField(20);
        
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });

        panelSearch.add(lblSearch);
        panelSearch.add(txtSearch);

        // Combine Stats and Search into North Section
        JPanel panelTopContainer = new JPanel(new BorderLayout());
        panelTopContainer.add(panelStats, BorderLayout.NORTH);
        panelTopContainer.add(panelSearch, BorderLayout.CENTER);
        add(panelTopContainer, BorderLayout.CENTER);

        // Table Setup
        String[] columnNames = {"Tracking Number", "Destination", "Weight (kg)", "Description", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tableCargo = new JTable(tableModel);
        tableCargo.setRowHeight(25);
        
        rowSorter = new TableRowSorter<>(tableModel);
        tableCargo.setRowSorter(rowSorter);

        JScrollPane scrollPane = new JScrollPane(tableCargo);
        
        // Main structural wrapper for center
        JPanel panelCenterWrapper = new JPanel(new BorderLayout());
        panelCenterWrapper.add(panelTopContainer, BorderLayout.NORTH);
        panelCenterWrapper.add(scrollPane, BorderLayout.CENTER);
        add(panelCenterWrapper, BorderLayout.CENTER);

        // Button Panel (CRUD & Reporting Operations)
        JPanel panelButtons = new JPanel();
        panelButtons.setBackground(new Color(245, 247, 250));
        
        JButton btnAddCargo = new JButton("Add New Cargo");
        JButton btnUpdateStatus = new JButton("Update Status");
        JButton btnDeleteCargo = new JButton("Delete Cargo");
        JButton btnExportCsv = new JButton("Export CSV");
        JButton btnRefresh = new JButton("Refresh Table");
        JButton btnLogout = new JButton("Logout");

        btnAddCargo.setBackground(new Color(41, 128, 185));
        btnAddCargo.setForeground(Color.WHITE);
        btnAddCargo.setFocusPainted(false);

        btnAddCargo.addActionListener(e -> {
            AddCargoDialog dialog = new AddCargoDialog(this);
            dialog.setVisible(true);
            loadCargoData(); 
        });

        btnUpdateStatus.addActionListener(e -> handleUpdateStatus());
        btnDeleteCargo.addActionListener(e -> handleDeleteCargo());
        btnExportCsv.addActionListener(e -> exportTableToCSV());
        
        btnRefresh.addActionListener(e -> {
            txtSearch.setText(""); 
            loadCargoData();
        });

        btnLogout.addActionListener(e -> {
            new CargoLoginFrame().setVisible(true);
            dispose();
        });

        panelButtons.add(btnAddCargo);
        panelButtons.add(btnUpdateStatus);
        panelButtons.add(btnDeleteCargo);
        panelButtons.add(btnExportCsv);
        panelButtons.add(btnRefresh);
        panelButtons.add(btnLogout);
        add(panelButtons, BorderLayout.SOUTH);
    }

    private void filterTable() {
        String text = txtSearch.getText().trim();
        if (text.length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    public void loadCargoData() {
        tableModel.setRowCount(0); 
        String sql = "SELECT tracking_number, destination, weight, description, status FROM cargo";

        int totalCount = 0;
        int pendingCount = 0;
        int deliveredCount = 0;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            while (rs.next()) {
                totalCount++;
                String tracking = rs.getString("tracking_number");
                String destination = rs.getString("destination");
                double weight = rs.getDouble("weight");
                String description = rs.getString("description");
                String status = rs.getString("status");

                if ("Pending".equalsIgnoreCase(status)) {
                    pendingCount++;
                } else if ("Delivered".equalsIgnoreCase(status)) {
                    deliveredCount++;
                }

                Object[] row = {tracking, destination, weight, description, status};
                tableModel.addRow(row);
            }

            // Update Live Statistics Panel Labels
            lblTotalCount.setText("Total Shipments: " + totalCount);
            lblPendingCount.setText("Pending: " + pendingCount);
            lblDeliveredCount.setText("Delivered: " + deliveredCount);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading cargo data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportTableToCSV() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No data available to export.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Cargo Report as CSV");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".csv");
            }

            try (FileWriter writer = new FileWriter(fileToSave)) {
                // Write column headers
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    writer.write(tableModel.getColumnName(i) + (i == tableModel.getColumnCount() - 1 ? "" : ","));
                }
                writer.write("\n");

                // Write row data
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object val = tableModel.getValueAt(i, j);
                        writer.write((val != null ? val.toString() : "") + (j == tableModel.getColumnCount() - 1 ? "" : ","));
                    }
                    writer.write("\n");
                }

                JOptionPane.showMessageDialog(this, "CSV Report exported successfully to:\n" + fileToSave.getAbsolutePath(), "Export Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error writing file: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleUpdateStatus() {
        int selectedRow = tableCargo.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a cargo row from the table to update.", "Selection Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tableCargo.convertRowIndexToModel(selectedRow);
        String trackingNumber = (String) tableModel.getValueAt(modelRow, 0);
        String currentStatus = (String) tableModel.getValueAt(modelRow, 4);

        String[] options = {"Pending", "In Transit", "Delivered", "Cancelled"};
        String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Select new status for tracking: " + trackingNumber,
                "Update Cargo Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                currentStatus
        );

        if (newStatus != null && !newStatus.equals(currentStatus)) {
            String sql = "UPDATE cargo SET status = ? WHERE tracking_number = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                if (conn == null) {
                    JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                pstmt.setString(1, newStatus);
                pstmt.setString(2, trackingNumber);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Cargo status updated successfully!");
                loadCargoData();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating status: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleDeleteCargo() {
        int selectedRow = tableCargo.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a cargo row from the table to delete.", "Selection Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tableCargo.convertRowIndexToModel(selectedRow);
        String trackingNumber = (String) tableModel.getValueAt(modelRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete tracking number: " + trackingNumber + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM cargo WHERE tracking_number = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                if (conn == null) {
                    JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                pstmt.setString(1, trackingNumber);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Cargo record deleted successfully!");
                loadCargoData();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting cargo: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainDashboardFrame().setVisible(true));
    }
}