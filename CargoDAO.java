import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CargoDAO {

    public boolean updateShipmentStatus(String trackingNumber, String newStatus) {
        String sql = "UPDATE shipments SET status = ? WHERE tracking_number = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setString(2, trackingNumber);
            
            int rowsUpdated = pstmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                // Log the action to maintain your cryptographic audit trail
                AuditLogger.logAction("UPDATE_STATUS", "Updated shipment " + trackingNumber + " to " + newStatus);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.out.println("Database error during status update: " + e.getMessage());
            return false;
        }
    }
}
