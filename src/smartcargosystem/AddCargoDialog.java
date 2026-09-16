package smartcargosystem;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCargoDialog extends JDialog {

    private JTextField txtTrackingNumber;
    private JTextField txtDestination;
    private JTextField txtWeight;
    private JButton btnSave;
    private JButton btnCancel;

    public AddCargoDialog(Frame parent) {
        super(parent, "Add Cargo", true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JPanel jPanelHeader = new JPanel();
        JLabel jLabelTitle = new JLabel("Enter Cargo Details");
        JPanel jPanelBody = new JPanel();
        
        JLabel jLabelTracking = new JLabel("Tracking Number:");
        txtTrackingNumber = new JTextField();
        
        JLabel jLabelDestination = new JLabel("Destination:");
        txtDestination = new JTextField();
        
        JLabel jLabelWeight = new JLabel("Weight (kg):");
        txtWeight = new JTextField();
        
        btnSave = new JButton("Save Cargo");
        btnCancel = new JButton("Cancel");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(350, 320);
        setResizable(false);

        jPanelHeader.setBackground(new Color(41, 128, 185));
        jLabelTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jLabelTitle.setForeground(Color.WHITE);
        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        GroupLayout jPanelHeaderLayout = new GroupLayout(jPanelHeader);
        jPanelHeader.setLayout(jPanelHeaderLayout);
        jPanelHeaderLayout.setHorizontalGroup(
            jPanelHeaderLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitle, GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelHeaderLayout.setVerticalGroup(
            jPanelHeaderLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHeaderLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabelTitle)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanelBody.setBackground(new Color(245, 247, 250));

        btnSave.setBackground(new Color(41, 128, 185));
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> btnSaveActionPerformed());

        btnCancel.addActionListener(e -> dispose());

        GroupLayout jPanelBodyLayout = new GroupLayout(jPanelBody);
        jPanelBody.setLayout(jPanelBodyLayout);
        jPanelBodyLayout.setHorizontalGroup(
            jPanelBodyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBodyLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanelBodyLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelWeight)
                    .addComponent(jLabelDestination)
                    .addComponent(jLabelTracking)
                    .addComponent(txtTrackingNumber)
                    .addComponent(txtDestination)
                    .addComponent(txtWeight)
                    .addGroup(jPanelBodyLayout.createSequentialGroup()
                        .addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanelBodyLayout.setVerticalGroup(
            jPanelBodyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBodyLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabelTracking)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTrackingNumber, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelDestination)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDestination, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelWeight)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtWeight, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanelBodyLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanelHeader, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelBody, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelHeader, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelBody, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void btnSaveActionPerformed() {
        String trackingNumber = txtTrackingNumber.getText().trim();
        String destination = txtDestination.getText().trim();
        String weightStr = txtWeight.getText().trim();

        if (trackingNumber.isEmpty() || destination.isEmpty() || weightStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all cargo fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double weight;
        try {
            weight = Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Weight must be a valid number.", "Format Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Includes both 'description' and 'status' fields to satisfy database table constraints
        String sql = "INSERT INTO cargo (tracking_number, destination, weight, description, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            pstmt.setString(1, trackingNumber);
            pstmt.setString(2, destination);
            pstmt.setDouble(3, weight);
            pstmt.setString(4, "Standard Cargo"); 
            pstmt.setString(5, "Pending"); // Default value for status field

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cargo saved successfully to database!");
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving cargo: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}