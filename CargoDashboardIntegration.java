import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CargoDashboardIntegration {

    public static void attachMlButton(JFrame parentFrame, JTextField weightField, JTextField distanceField, JLabel resultLabel) {
        JButton predictButton = new JButton("Calculate AI ETA");
        
        predictButton.addActionListener((ActionEvent e) -> {
            try {
                double weight = Double.parseDouble(weightField.getText());
                double distance = Double.parseDouble(distanceField.getText());
                
                // Call our ML client
                String jsonResponse = MLPredictionClient.getPredictedEta(weight, distance);
                
                // Display result
                resultLabel.setText("ETA Result: " + jsonResponse);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Please enter valid numbers for weight and distance.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
