from flask import Flask, request, jsonify
import joblib
import pandas as pd

app = Flask(__name__)

# Load models
try:
    eta_model = joblib.load('cargo_eta_model.pkl')
    anomaly_model = joblib.load('cargo_anomaly_model.pkl')
    print("ETA and Anomaly models loaded successfully into Flask API!")
except Exception as e:
    print(f"Error loading models: {e}")

@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.get_json()
        input_data = pd.DataFrame([[data['weight'], data['distance_km']]], columns=['weight', 'distance_km'])
        prediction = eta_model.predict(input_data)
        return jsonify({
            'status': 'success',
            'predicted_delivery_time_hours': round(float(prediction[0]), 2)
        })
    except Exception as e:
        return jsonify({'status': 'error', 'message': str(e)}), 400

@app.route('/detect-anomaly', methods=['POST'])
def detect_anomaly():
    try:
        data = request.get_json()
        input_data = pd.DataFrame([[data['weight'], data['distance_km']]], columns=['weight', 'distance_km'])
        
        # IsolationForest returns 1 for normal, -1 for anomaly
        result = anomaly_model.predict(input_data)
        is_anomaly = bool(result[0] == -1)
        
        return jsonify({
            'status': 'success',
            'is_anomaly': is_anomaly,
            'message': 'Shipment flagged as anomalous!' if is_anomaly else 'Shipment is normal.'
        })
    except Exception as e:
        return jsonify({'status': 'error', 'message': str(e)}), 400

if __name__ == '__main__':
    app.run(port=5000, debug=True)
