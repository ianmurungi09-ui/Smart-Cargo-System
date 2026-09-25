import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class MLPredictionClient {

    private static final String API_URL = "http://127.0.0.1:5000/predict";

    public static String getPredictedEta(double weight, double distanceKm) {
        try {
            // Create JSON payload manually to avoid extra heavy dependencies
            String jsonInputString = String.format("{\"weight\": %.2f, \"distance_km\": %.2f}", weight, distanceKm);

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return "Error: Server returned status code " + response.statusCode();
            }

        } catch (Exception e) {
            return "Error connecting to ML API: " + e.getMessage();
        }
    }

    // Quick local test inside Java
    public static void main(String[] args) {
        System.out.println("Testing ML Prediction Client...");
        String result = getPredictedEta(15.0, 250.0);
        System.out.println("Response from Python API: " + result);
    }
}
