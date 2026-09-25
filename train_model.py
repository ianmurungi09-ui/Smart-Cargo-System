import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
import joblib

try:
    df = pd.read_csv('shipments_training_data.csv')
    print("Data loaded successfully!")
except FileNotFoundError:
    print("Error: 'shipments_training_data.csv' not found. Please export your database table first.")
    exit()

X = df[['weight', 'distance_km']]
y = df['delivery_time_hours']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

model = RandomForestRegressor()
model.fit(X_train, y_train)

joblib.dump(model, 'cargo_eta_model.pkl')
print("Model trained successfully and saved as cargo_eta_model.pkl!")
