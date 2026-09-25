package smartcargosystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CargoDAO {

    public static boolean addCargo(String trackingNumber, String senderName, String recipientName, String destination, String status, double weight) {
        String insertCargoSQL = "INSERT INTO shipments (tracking_number, sender_name, recipient_name, destination, status, weight) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(insertCargoSQL)) {
                pstmt.setString(1, trackingNumber);
                pstmt.setString(2, senderName);
                pstmt.setString(3, recipientName);
                pstmt.setString(4, destination);
                pstmt.setString(5, status);
                pstmt.setDouble(6, weight);
                
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    String actionDetails = "Created shipment to " + destination + " | Weight: " + weight + "kg | Status: " + status;
                    AuditLogger.logEvent(conn, trackingNumber, "INSERT", actionDetails);
                    
                    conn.commit();
                    System.out.println("Cargo added and securely logged successfully!");
                    return true;
                }
                
                conn.rollback();
                return false;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateCargoStatus(String trackingNumber, String newStatus) {
        String updateSQL = "UPDATE shipments SET status = ? WHERE tracking_number = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                pstmt.setString(1, newStatus);
                pstmt.setString(2, trackingNumber);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    String actionDetails = "Updated shipment status to: " + newStatus;
                    AuditLogger.logEvent(conn, trackingNumber, "UPDATE", actionDetails);

                    conn.commit();
                    System.out.println("Cargo status updated and audit chain extended!");
                    return true;
                }

                conn.rollback();
                return false;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
