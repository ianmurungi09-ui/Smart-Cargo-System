cd C:\Users\HP\OneDrive\Desktop\Smart-Cargo-System

# Overwrite README.md with a comprehensive, professional project documentation
@"
# Smart Cargo System (Cargo Management System)

A professional-grade, hybrid cargo management application featuring a Java Swing desktop interface, a MariaDB persistent backend, and a Python-powered machine learning microservice for predictive analytics and anomaly detection.

---

## Problem Statement
Traditional cargo and logistics management systems often suffer from operational silos, relying heavily on manual status tracking and static data entry. This leads to several critical challenges:
- **Lack of Real-Time Visibility:** Operators struggle to track shipments dynamically, resulting in delayed updates and poor coordination between database states and user interfaces.
- **Vulnerability to Operational Anomalies:** Conventional systems fail to proactively identify suspicious or anomalous shipments (such as irregular weight-to-distance ratios or unusual transport times), exposing logistics networks to inefficiencies or errors.
- **Audit Deficiencies:** Many localized management tools lack secure accountability tracking, making it difficult to maintain an immutable audit trail of critical status changes and data modifications.

The **Smart Cargo System** solves these challenges by providing an integrated, hybrid architecture that unifies real-time database CRUD operations, cryptographic audit logging, and machine learning-powered anomaly detection into a single desktop solution.

---

## Architecture & Tech Stack
- **Client/UI:** Java Swing, JDBC, Java HTTP Client.
- **Persistence:** MariaDB relational database with full CRUD support (`CargoDAO.java`).
- **Machine Learning Microservice:** Python, Flask, `scikit-learn` (Linear Regression for ETA predictions, Isolation Forest for anomaly detection).
- **Security & Compliance:** Cryptographic audit logging (`AuditLogger.java`) for tracking critical status updates and data modifications.
- **Version Control & Automation:** Managed entirely via Git and PowerShell.

---

## Key Features
1. **Shipment Management:** Complete CRUD operations synchronized with MariaDB, including dynamic status updates through the UI dashboard.
2. **Predictive Analytics:** Real-time ETA calculations powered by custom machine learning regression models.
3. **Anomaly Detection:** Automated Isolation Forest checks (`MLAnomalyClient.java`) to flag unusual cargo weights or distances.
4. **Audit Trail:** Secure, immutable logging for tracking all operational actions and status adjustments.

---

## Project Structure
- `Main.java` / UI Components: Java Swing desktop dashboard interface.
- `CargoDAO.java`: Handles database interactions and status update queries against MariaDB.
- `MLAnomalyClient.java`: Communicates with the local Python Flask API for anomaly checks.
- `app.py`: Flask microservice exposing endpoints for `/predict` and `/detect-anomaly`.
- `train_model.py`: Training pipeline for generating ETA and Isolation Forest model binaries.

---

## Getting Started & Installation
1. **Clone the Repository:**
   ```powershell
   git clone [https://github.com/ianmurungi09-ui/Smart-Cargo-System.git](https://github.com/ianmurungi09-ui/Smart-Cargo-System.git)
   cd Smart-Cargo-System
