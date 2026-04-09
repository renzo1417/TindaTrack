# TindaTrack: A Smart Inventory and Expiry Monitoring System for Small Retail Stores

## 👥 Group Members
- Renzo Pogoy
- Joel Coyoca
- Johanne Froilan Cando
- Casidey Kirzteen Quibuyen
- Lei Sheldon Densing

## 📌 Project Description
TindaTrack is a Java-based desktop application designed for small retail businesses such as sari-sari stores to efficiently manage their inventory. The system tracks product quantities, monitors expiration dates, and automatically analyzes stock movement to generate smart inventory insights.
Unlike traditional systems that require manual sales input, TindaTrack uses stock change detection to estimate product movement. This allows store owners to understand which items are fast-moving, slow-moving, overstocked, or at risk of expiring—without additional effort. The system aims to reduce product waste, prevent stock shortages, and help store owners maintain optimal inventory levels through simple, actionable insights.

## ⚙️ Propose Features
🔐 User Management
- User Login and Registration
- Secure authentication system

📊 Dashboard (Navigation)
Overview of inventory status
Summary cards:
- Low Stock Items
- Expiring Soon
- Overstocked Items
- At-Risk Items

📦 Inventory Management
- Add, edit, and delete products
- Categorize items (e.g., canned goods, beverages, snacks)
- Track quantity and expiration dates

🔄 Stock Movement Tracking (Core Innovation)
Automatically records stock changes when quantity is updated
Detects:
- Stock decrease → estimated sales
- Stock increase → restocking
- Maintains stock history for analysis

🧠 Inventory Health Insights (Replaces “Phase-Out”)
Stock Status Indicators:
- 🟢 Balanced
- 🟡 Overstocked
- 🔴 At Risk of Expiry
Expiry Risk Analysis:
Identifies items likely to expire before being sold
Smart Suggestions:
- “Restock Soon”
- “Avoid Restocking”
- “Monitor Closely”

🔔 Notifications (Navigation (still undecided if it belongs to Navigation))
- Alerts for:
- Expiring items
- Low stock levels
= Visual indicators in dashboard

🔍 Inventory View (Navigation)
- TableView display of all products
- Search and filter functionality
- Sort by category, expiry, or stock level

⚙️ Settings (Navigation)
- User preferences
- Optional alert thresholds (e.g., low stock limit)

## 🧱 Planned Technologies
- Java (Core Logic)
- JavaFX (User Interface)
- Scene Builder (UI Design)
- JDBC (Database Connectivity)
- SQLite / MySQL (Database)

## Evaluation Criteria Mapping (Initial)
- OOP: Planned use of classes such as ...
- GUI: JavaFX with FXML views
- UML: Use Case and Class Diagram included
- Design Pattern

## SpreadSheet for Workload Distribution
https://docs.google.com/spreadsheets/d/1dZKlAPsaUubLA-WlDA5lmgklyEAya4bQHBr8akPBue0/edit?usp=sharing

