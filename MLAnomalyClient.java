import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MLAnomalyClient {

    private static final String FLASK_URL = "http://localhost:5000/detect-anomaly";

    public static String checkAnomaly(double weight, double distance) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String jsonInput = String.format("{\"weight\": %.2f, \"distance_km\": %.2f}", weight, distance);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FLASK_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return "{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}";
        }
    }
}
