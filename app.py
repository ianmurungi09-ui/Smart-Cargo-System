from flask import Flask, request, jsonify
import joblib
import pandas as pd

app = Flask(__name__)

# Load the trained machine learning model
try:
    model = joblib.load('cargo_eta_model.pkl')
    print("Model loaded successfully into Flask API!")
except Exception as e:
    print(f"Error loading model: {e}")

@app.route('/predict', methods=['POST'])
def predict():
    try:
        # Get JSON data sent from the Java backend
        data = request.get_json()
        
        # Expecting JSON like: {"weight": 10.5, "distance_km": 150}
        weight = data['weight']
        distance_km = data['distance_km']
        
        # Create a dataframe for prediction
        input_data = pd.DataFrame([[weight, distance_km]], columns=['weight', 'distance_km'])
        
        # Predict ETA
        prediction = model.predict(input_data)
        
        # Return result as JSON
        return jsonify({
            'status': 'success',
            'predicted_delivery_time_hours': round(float(prediction[0]), 2)
        })
    except Exception as e:
        return jsonify({
            'status': 'error',
            'message': str(e)
        }), 400

if __name__ == '__main__':
    app.run(port=5000, debug=True)
