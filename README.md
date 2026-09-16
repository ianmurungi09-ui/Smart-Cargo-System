# Smart Cargo Management System (SmartCargoDB)

A professional-grade Java Swing desktop application designed for streamlined cargo tracking, user authentication, inventory management, and real-time analytics, powered by a secure MariaDB backend.

---

## 🚀 Key Features

* **Secure Authentication & User Lifecycle:** Secure login portal paired with a robust staff registration dialog handling database constraints cleanly.
* **Full CRUD Operations:** 
  * **Create:** Add new shipments using customized modal dialog forms.
  * **Read:** Dynamic data loading directly from the database into a structured table.
  * **Update:** Easily update shipment statuses (*Pending*, *In Transit*, *Delivered*, *Cancelled*).
  * **Delete:** Secure removal of cargo records with confirmation prompts.
* **Real-Time Search & Filtering:** Instantly filter shipment rows on the fly using a responsive text search box.
* **Live Analytics Panel:** Dynamic top metric counters updating shipment totals, pending orders, and delivered items in real time.
* **CSV Export Reporting:** One-click functionality to export current table data and reports directly into a `.csv` file.
* **Database Security:** Utilizes JDBC with parameterized queries (`PreparedStatement`) to prevent SQL injection vulnerabilities.

---

## 🛠️ Tech Stack

* **Language:** Java (JDK 8+)
* **GUI Framework:** Java Swing (`JFrame`, `JTable`, `JDialog`)
* **Database:** MariaDB / MySQL
* **Connectivity:** JDBC Driver (`mysql-connector-j`)

---

## 📂 Project Structure

```text
SmartCargoSystem/
│
├── src/
│   └── smartcargosystem/
│       ├── Main.java                # Single application execution entry point
│       ├── CargoLoginFrame.java     # Authentication window
│       ├── CargoRegisterDialog.java # Staff registration dialog
│       ├── MainDashboardFrame.java  # Main control center (CRUD, Search, Stats, CSV)
│       ├── AddCargoDialog.java      # Modal form for adding shipments
│       └── DBConnection.java        # Centralized JDBC database connection handler
│
├── dist/                            # Compiled build artifacts & runtime libraries
└── README.md
