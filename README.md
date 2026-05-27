# TindaTrack: A Smart Inventory and Expiry Monitoring System for Small Retail Stores

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Object-Oriented Programming Principles](#object-oriented-programming-principles)
3. [Java Generics](#java-generics)
4. [Multithreading and Concurrency](#multithreading-and-concurrency)
5. [Graphical User Interface](#graphical-user-interface)
6. [Database Connectivity](#database-connectivity)
7. [Unified Modeling Language (UML)](#unified-modeling-language-uml)
8. [Design Patterns](#design-patterns)
9. [Code Quality and Documentation](#code-quality-and-documentation)
10. [Additional Features](#additional-features)
11. [Setup and Run Instructions](#setup-and-run-instructions)

---

## Project Overview

**Project Title:** TindaTrack: A Smart Inventory and Expiry Monitoring System for Small Retail Stores

**Description:**
TindaTrack is a Java-based desktop application designed specifically for small retail businesses such as sari-sari stores to efficiently manage their inventory and monitor product expiration dates. The system tracks product quantities, monitors expiration dates, and automatically analyzes stock movement to generate smart inventory insights.

Unlike traditional systems that require manual sales input, TindaTrack uses stock change detection to estimate product movement. This approach allows store owners to understand which items are fast-moving, slow-moving, overstocked, or at risk of expiring — without any additional effort on their part. The system aims to reduce product waste, prevent stock shortages, and help store owners maintain optimal inventory levels through simple and actionable insights.

**Members:**
- Renzo Pogoy
- Joel Coyoca
- Johanne Froilan Cando
- Casidey Kirzteen Quibuyen
- Lei Sheldon Densing

**Tech Stack:**
- Java 24
- JavaFX 21.0.6
- JDBC with SQLite 3
- Maven 3.x
- Ikonli 12.4.0 (FontAwesome5 and MaterialDesign2 icon packs)
- Jakarta Mail / Angus Mail 2.0.4
- JUnit Jupiter 5.12.1

---

## Object-Oriented Programming Principles

### Encapsulation
We encapsulate data by keeping classes and fields private, exposing only necessary access through public getters and setters. Model classes such as `User`, `Product`, `StockDetails`, and `NotificationPreferences` act as data entities with private fields and controlled access methods. Database DAOs encapsulate all SQL operations and connection management internally, and controllers manage their own UI state privately — preventing unintended external modifications and keeping internal workings hidden from other layers of the application.

### Inheritance
We leverage inheritance to promote code reuse and establish clear class hierarchies. Database management classes inherit common CRUD patterns, controller classes extend JavaFX base patterns, and DAO classes share common database operation structures. Settings controllers, for example, share a common user UI setup logic through inheritance, reducing code duplication and keeping the codebase maintainable and consistent across similar components.

### Polymorphism
Polymorphism enables flexible handling of different data types and behaviors throughout the system. Generic DAO implementations handle different entity types through a shared interface, notification types such as `EXPIRING_SOON`, `LOW_STOCK`, and `OUT_OF_STOCK` are treated polymorphically, and UI components dynamically adapt their appearance based on different product statuses — all without requiring explicit type checks at every point in the code.

### Abstraction
Complex implementation details are hidden behind clean, abstract interfaces. `ConnectionBridge` abstracts SQLite connection complexity, `SessionManager` abstracts user session serialization, and `NotificationService` abstracts alert generation logic. Presenters abstract complex business rules entirely away from controllers, exposing only what is necessary to drive the UI and keeping each layer focused on a single responsibility.

---

## Java Generics

### Generic Classes and Methods
Generics provide type-safe and reusable code throughout the application. Generic CRUD operations in DAO classes handle various entity types without duplication, and generic filtering and transformation operations applied across inventory lists preserve type information at compile time. This reduces the need for unsafe casts and ensures that type errors are caught early during development rather than at runtime.

### Generic Collections
We make use of Java's built-in generic collections such as `ArrayList<Product>` and `HashMap<Integer, StockDetails>` throughout the codebase. Observable lists in controllers maintain type safety while binding UI components to underlying data models. These generic collections ensure code flexibility, eliminate the risk of runtime `ClassCastException` errors, and keep the code clean, predictable, and reliable across all data-handling operations.

---

## Multithreading and Concurrency

TindaTrack implements multithreading in three primary areas to maintain UI responsiveness and handle background operations without blocking the JavaFX application thread.

### 1. Session Auto-Load Threading
**File:** `LoginController.java`

On application startup, the system checks for a previously saved user session and navigates to the dashboard automatically if one is found. This operation is executed asynchronously using `Platform.runLater()`, ensuring the JavaFX thread is never blocked during file I/O. The result is a smooth and instant login experience with no visible delay for returning users.

### 2. Inventory Auto-Refresh Threading
**File:** `InventoryAutoRefresher.java`

The inventory table is refreshed automatically every 5 seconds in the background using a `ScheduledExecutorService` running on a single daemon thread. All resulting UI updates are dispatched back to the JavaFX thread via `Platform.runLater()`, ensuring thread safety. The use of a daemon thread also guarantees automatic termination when the application closes, preventing resource leaks.

### 3. Email Notification Threading
**File:** `NotificationEmailSender.java`

Email notifications are sent asynchronously using a dedicated daemon thread, isolating Gmail SMTP communication entirely from the main application thread. This allows critical alerts to be dispatched instantly in the background without causing any noticeable lag or freezing in the user interface. HTML-formatted emails with branded styling are composed and transported entirely within the background thread.

**Summary:** The application uses 3 primary multithreading implementations — Session Auto-Load, Inventory Auto-Refresh, and Email Notification Sending — along with 2 supporting implementations: `Platform.runLater()` for thread-safe notification sound playback, and `Runnable` encapsulation for responsive inventory filtering on large product lists.

---

## Graphical User Interface

**Built With:** JavaFX 21.0.6 and Scene Builder

The user interface is built using JavaFX with FXML-based declarative layouts designed in Scene Builder. A global CSS stylesheet ensures visual consistency across all screens, while Ikonli icon packs provide uniform iconography throughout the application.

**Key Screens:**

- **Authentication Screens** — Includes a login screen with session persistence, a registration screen with full input validation, and a two-step forgot password recovery flow for account recovery.
- **Dashboard** — Displays a personalized greeting, real-time date and time, and key metrics including total items, items expiring soon, low stock count, and total units at risk. Also surfaces the top 3 fast-moving and slow-moving products with progress bars, and the top 3 wasted items.
- **Inventory Management** — A sortable and filterable product table with real-time search, status filters (Active, Expiring, Expired, Out of Stock), and action buttons for adding, modifying, selling, replenishing, and deleting products.
- **Insights Module** — Four integrated sub-views: Fast-Moving Items ranked by sales velocity with progress bars, Slow-Moving Items organized by category with sales counts, an Expiry Risk Grid showing card-based displays of items expiring within 10 days, and a Recommendations section offering up to 10 smart and actionable suggestions per product.
- **Stock Activity View** — A chronological history of all stock changes with color-coded reasons (Restocked in green, Sold in red), product-level search, and a statistics summary of total restock and sold unit counts.
- **Notifications Center** — Real-time alerts for expiring items, low stock, and out-of-stock products, with mark-as-read, individual and batch delete, optional notification sound, and optional email delivery.
- **Settings Module** — Three dedicated sections: Profile Settings for managing name, username, and email; Store Information for store name, owner, contact, and address; and Notification Preferences with five independent toggle controls.

**Event Handling:**
UI elements use JavaFX `ChangeListener` and `InvalidationListener` for real-time search and status-based filtering as users type, keeping the interface fully responsive without any manual refresh actions required.

**UX Design:**
Layouts use JavaFX panes with proper resizing behavior to adapt to different window sizes. Color-coded status indicators and progress bars provide at-a-glance inventory health information, and visual read/unread states in the notification center help users track alert activity efficiently.

---

## Database Connectivity

**Database Used:** SQLite 3

The application uses a lightweight local SQLite database, making it well-suited for offline sari-sari store environments that do not require a network server or external database infrastructure.

**Integration:**
The database is connected via the SQLite JDBC Driver (v3.45.2.0). All queries use `PreparedStatement` to prevent SQL injection and optimize repeated query execution. The `ConnectionBridge` singleton manages the single database connection instance consistently across the entire application lifecycle.

**CRUD Operations:**

- `NotificationDAO` — Handles create, read, update, and soft-delete operations for all system notifications.
- `UsersTableManagement` — Manages user account creation, credential validation, and profile updates.
- `ProductManagement` — Provides full CRUD support for all inventory product records.
- `StockTableManagement` — Inserts and retrieves the complete stock change history with reasons and timestamps.
- `SalesManagement` — Records and retrieves all sales transaction data linked to user accounts.
- `NotificationPreferencesDAO` — Saves and loads per-user notification toggle settings from the database.

**Database Schema:**

| Table | Key Columns |
|---|---|
| `users` | id, username, password, email, fullname, storeName, phoneNumber |
| `products` | id, ownerId, productName, category, quantity, originalQuantity, expiryDate, price |
| `stock_changes` | id, ownerId, productId, oldQty, newQty, reason, date |
| `notifications` | id, ownerId, productId, type, message, timestamp, isRead |
| `notification_preferences` | userId, expiryAlerts, lowStockAlerts, restockReminders, notificationSound, emailNotifications |
| `sales` | id, ownerId, productId, productName, quantitySold, saleDate |

---

## Unified Modeling Language (UML)

**Diagrams Submitted:** Class Diagram, Use Case Diagram

**Tools Used:** draw.io

**Files:**
- `diagrams/CLASS_DIAGRAM_BIGO.pdf`
- `diagrams/USE_CASE_DIAGRAM_BIGO.pdf`

---

## Design Patterns

### Creational Design Patterns

**Singleton Design Pattern**

`ConnectionBridge` centralizes SQLite connection management, ensuring a single consistent database connection is shared across the entire application. `SessionManager` maintains one user session throughout the application lifecycle, persisting authentication state via serialized `User` objects. `NotificationService` operates as a single instance that handles all notification creation and evaluation across every screen, guaranteeing consistent alert behavior regardless of where it is triggered.

**Factory Design Pattern**

`NotificationDAO` acts as a factory that creates appropriate notification records based on event type, such as `EXPIRING_SOON`, `LOW_STOCK`, or `OUT_OF_STOCK`. `StockDetailManager` instantiates styled stock change UI components based on the specific change reason, decoupling component creation logic from the controller layer and improving scalability.

**Builder Design Pattern**

The FXML Loader pattern constructs UI scenes declaratively with fluent controller assignment, applying a builder approach to view composition. `StockDetailManager` also follows the Builder pattern by assembling styled stock detail UI components step by step, allowing dynamic configuration of each component's visual properties before it is rendered in the view.

---

### Structural Design Patterns

**Composite Design Pattern**

The Dashboard composes multiple independent widgets — statistics panels, alert indicators, and product lists — into a single unified view. The Insights module similarly aggregates fast-moving, slow-moving, and expiry sub-views into one cohesive interface. The Notification Center groups individual notification items into a single scrollable panel, treating each item uniformly while presenting them as a composed whole.

**Facade Design Pattern**

`DashboardController` provides a simplified interface to complex underlying systems — including inventory analysis, notification evaluation, and user session management — through a single `initialize()` call that coordinates all subsystems. `InventoryPresenter` facades complex stock calculations and CRUD operations away from the controller, and `NotificationService` hides eligibility checks and timestamp logic behind a single `evaluateAllProducts()` method.

---

### Behavioral Design Patterns

**Observer Design Pattern**

`NotificationService` observes product state changes and automatically triggers alert creation whenever a product meets expiry or stock threshold conditions. JavaFX property binding wires UI components directly to data model changes via `ChangeListener` and `InvalidationListener`, so the interface always reflects the latest state without any manual synchronization.

**State Design Pattern**

Products transition through defined lifecycle states — `ACTIVE`, `EXPIRING_SOON`, `EXPIRED`, and `OUT_OF_STOCK` — and the system responds to each state with appropriate visual indicators and alert generation. Stock change UI components render differently depending on the change reason state, with Restocked entries displaying a green background and Sold entries displaying a red background to provide immediate visual clarity.

**Strategy Design Pattern**

Different algorithms are applied for calculating fast-moving, slow-moving, and overstocked items in the Insights module, with each strategy encapsulated independently to allow targeted analysis per inventory segment. Inventory filtering logic is encapsulated as a `Runnable` strategy, allowing it to be reused across multiple listener contexts and making it easy to extend or replace filtering behavior without affecting other parts of the codebase.

---

## Code Quality and Documentation

Our project follows a well-organized directory structure to maintain clarity, modularity, and ease of maintenance. Below is an overview of the source code and resource structure.

### Project Directory Structure

```
tindatrack/
├── src/main/java/com/bigo/tindatrack/
│   ├── Launcher.java                               # Application entry point
│   ├── TindaTrackApplication.java                  # JavaFX Application class
│   │
│   ├── Controller/                                 # JavaFX Controllers (View-ViewModel layer)
│   │   ├── DashboardController.java                # Main dashboard and navigation hub
│   │   ├── LoginController.java                    # Authentication with session threading
│   │   ├── RegisterController.java                 # New user registration
│   │   ├── ForgotPasswordController.java           # Password recovery
│   │   ├── Insights/                               # Inventory insights module
│   │   │   ├── InsightsController.java
│   │   │   ├── FastMovingItemsController.java
│   │   │   ├── SlowMovingItemsController.java
│   │   │   └── InsightsExpiryController.java
│   │   ├── Inventory/                              # Inventory management module
│   │   │   ├── InventoryController.java
│   │   │   ├── InventoryPresenter.java
│   │   │   ├── InventoryModel.java
│   │   │   ├── AddProductController/
│   │   │   ├── ModifyProductController/
│   │   │   └── InventorySellController/
│   │   ├── StockActivity/                          # Stock tracking module
│   │   │   ├── StockActivityController.java
│   │   │   ├── StockActivityPresenter.java
│   │   │   └── StockActivityModel.java
│   │   ├── Notification/                           # Notification module
│   │   │   ├── NotificationService.java
│   │   │   ├── NotificationEmailSender.java
│   │   │   ├── NotificationSoundPlayer.java
│   │   │   └── NotificationController.java
│   │   └── Settings/                               # Settings module
│   │       ├── SettingsProfileController.java
│   │       ├── SettingsMarketController.java
│   │       └── SettingsNotificationsController.java
│   │
│   ├── data/                                       # Model layer
│   │   ├── InventoryList/
│   │   ├── StockDetails/
│   │   │   ├── StockDetails.java
│   │   │   ├── StockDetailManager.java
│   │   │   └── StockDetailsList.java
│   │   └── models/
│   │       ├── User.java
│   │       └── NotificationPreferences.java
│   │
│   ├── Product/                                    # Product domain model
│   │   ├── Product.java
│   │   ├── Status.java
│   │   └── ProductStatusController.java
│   │
│   ├── Sales/
│   │   └── Sales.java
│   │
│   ├── SQLite_Database/                            # Data Access Layer (DAOs)
│   │   ├── ConnectionBridge.java
│   │   ├── userManagement/
│   │   ├── productsManagement/
│   │   ├── StockManagement/
│   │   ├── SalesManagement/
│   │   └── NotificationManagement/
│   │
│   └── utils/
│       ├── utility.java
│       └── InventoryAutoRefresher.java
│
├── src/main/resources/com/bigo/tindatrack/
│   ├── Dashboard-view.fxml
│   ├── Inventory-view.fxml
│   ├── Insights-view.fxml
│   ├── StockActivity-view.fxml
│   ├── Notification-view.fxml
│   ├── Settings*.fxml
│   ├── style.css
│   ├── icons/
│   ├── Images/
│   └── sounds/
│
├── pom.xml
├── README.md
├── USER_tindaTracker.db
├── current_user.ser
└── diagrams/
    ├── CLASS_DIAGRAM_BIGO.pdf
    └── USE_CASE_DIAGRAM_BIGO.pdf
```

This structure promotes separation of concerns, allowing the data, business logic, and presentation layers to remain fully decoupled and easy to navigate. Each package serves a specific purpose, contributing to the overall maintainability and scalability of the project.

---

## Additional Features

### MVVM Architecture
TindaTrack follows the Model-View-ViewModel (MVVM) architectural pattern to ensure a clean separation of concerns and improve code organization. The **Model** represents application data and business logic, including data classes, DAOs, and database operations. The **View** handles all user interface components such as JavaFX screens, FXML layouts, and user interactions. The **ViewModel** layer — implemented through Controllers and Presenters — acts as an intermediary that manages UI-related state and data transformations, exposing only what the View needs without leaking business logic into the presentation layer.

### Core Innovation: Stock Movement Tracking
TindaTrack's unique stock change detection system operates without requiring manual sales input from the store owner. All stock quantity modifications are recorded automatically in real time and classified by reason — Restocked, Sold, Damaged, Expired, or Adjusted. Movement velocity is then calculated from the full change history, and the Insights module uses this data to surface fast-moving items, slow-moving items, overstocked products, and items nearing expiration — along with specific, actionable recommendations for each.

### Session Management
User sessions are serialized to a local file (`current_user.ser`) for persistent login across application restarts. The `SessionManager` singleton provides a centralized session API with methods for saving, loading, clearing, and checking the active session, ensuring consistent authentication state management throughout the entire application lifecycle.

### Notification System
The system automatically generates alerts for items expiring within 30 days, items falling below minimum stock thresholds, and items that have reached zero quantity. Optional notification sound is supported via JavaFX Media, and optional email delivery is provided through Gmail SMTP. All notification preferences are stored per user in the database and reloaded on every session start, giving each store owner full control over their alert experience.

---

## Setup and Run Instructions

### Prerequisites
- Java 24 or higher
- Maven 3.x
- JavaFX 21.0.6
- SQLite 3

### Build and Run

```bash
# Clone the repository
git clone <repository-url>
cd tindatrack

# Build with Maven
mvn clean install

# Run the application
mvn javafx:run
```

### Database Initialization
The SQLite database (`USER_tindaTracker.db`) is created automatically on first run. All tables are initialized by the `initDatabase()` method in `TindaTrackApplication`. A session file (`current_user.ser`) is created on first login and used for automatic login on all subsequent startups.
