package smartcargosystem;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Ensure GUI runs safely on the Swing Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Start the application with the login screen
            CargoLoginFrame loginFrame = new CargoLoginFrame();
            loginFrame.setVisible(true);
        });
    }
}