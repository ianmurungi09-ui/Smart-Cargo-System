package smartcargosystem;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuditLogger {

    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLastHash(Connection conn) {
        String query = "SELECT current_hash FROM cargo_audit_log ORDER BY log_id DESC LIMIT 1";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("current_hash");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0000000000000000000000000000000000000000000000000000000000000000";
    }

    public static void logEvent(Connection conn, String trackingNumber, String actionType, String details) {
        String previousHash = getLastHash(conn);
        String rawData = previousHash + trackingNumber + actionType + details + System.currentTimeMillis();
        String currentHash = applySha256(rawData);

        String insertQuery = "INSERT INTO cargo_audit_log (tracking_number, action_type, action_details, previous_hash, current_hash) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, trackingNumber);
            pstmt.setString(2, actionType);
            pstmt.setString(3, details);
            pstmt.setString(4, previousHash);
            pstmt.setString(5, currentHash);
            pstmt.executeUpdate();
            System.out.println("Audit log securely recorded with hash: " + currentHash);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Testing SmartCargoSystem AuditLogger Hashing...");
        String testInput = "GenesisBlockSampleData123";
        String generatedHash = applySha256(testInput);
        System.out.println("Input: " + testInput);
        System.out.println("SHA-256 Output: " + generatedHash);
    }
}
