package com.bigo.tindatrack.Controller;

import com.bigo.tindatrack.Controller.Insights.FastMovingItemsController;
import com.bigo.tindatrack.Controller.Insights.SlowMovingItemsController;
import com.bigo.tindatrack.Controller.Inventory.InventoryPresenter;
import com.bigo.tindatrack.Controller.Notification.NotificationItem;
import com.bigo.tindatrack.Controller.Notification.NotificationService;
import com.bigo.tindatrack.SQLite_Database.NotificationManagement.NotificationDAO;
import com.bigo.tindatrack.SQLite_Database.productsManagement.fetchDataFromTable;
import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.SQLite_Database.userManagement.UserUIHelper;
import com.bigo.tindatrack.data.models.User;
import com.bigo.tindatrack.utils.utility;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId;
import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.loadUser;

public class DashboardController {

    @FXML private Label welcomeField, dateField;
    @FXML private Label username_top, username_bottom, username_top_initial, username_bottom_initial;
    @FXML private VBox sellFirstList;
    @FXML private Button inventoryButton, insightButton,
            stockactivityButton, settingButton, viewAllerts;
    @FXML private Label total_items, expiring_soon, low_stock, total_expireUnits;
    @FXML private Label top_first_item, top_second_item, top_third_item;
    @FXML private Label top_first_item_counter, top_second_item_counter, top_third_item_counter;

    @FXML private ProgressBar top_first_item_progress, top_second_item_progress, top_third_item_progress;
    @FXML private Label least_first_item, least_second_item, least_third_item;
    @FXML private Label least_first_item_type, least_second_item_type, least_third_item_type;
    @FXML private Label least_first_item_counter, least_second_item_counter, least_third_item_counter;
    @FXML private Label wasted_first_item_type,  wasted_second_item_type, wasted_third_item_type;
    @FXML private Label wasted_first_item, wasted_second_item, wasted_third_item;
    @FXML private Label wasted_first_item_counter, wasted_second_item_counter, wasted_third_item_counter;
    @FXML private Label expired_label, out_of_stock_label, items_wasted_label;

    private User user = loadUser();
    private SlowMovingItemsController slowMovingController;
    private FastMovingItemsController  fastMovingController;

    @FXML
    public void initialize() {
        if (user == null) {
            System.out.println("Error: No user found!");
            return;
        }

        int ownerId = getCurrentUserId(); // Unified user ID extraction

        welcomeField.setText("Hello " + user.getUsername() + "! - Here's your inventory overview");

        UserUIHelper.setupUserUI(username_top_initial,
                username_bottom_initial,
                username_top,
                username_bottom,
                loadUser());

        LocalDate today     = LocalDate.now();
        String    dayName   = today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String    formatted = today.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));

        NotificationService.evaluateAllProducts();
        populateSellFirst(ownerId); // FIX: Scoped to user

        ObservableList<Product> products = fetchDataFromTable.getAllProducts(ownerId);
        total_items.setText(products.size() + "");
        dateField.setText(dayName + ", " + formatted + " - " + products.size() + " items tracked");

        Set<String> activeProductNames = new HashSet<>();
        for (Product p : products) {
            LocalDate expiry = p.getLocalExpiryDate();
            if (expiry == null || !expiry.isBefore(LocalDate.now())) {
                activeProductNames.add(p.getProductName());
            }
        }

        fastMovingController = new FastMovingItemsController(
                new ProgressBar[]{ top_first_item_progress, top_second_item_progress, top_third_item_progress },
                new Label[]{ top_first_item, top_second_item, top_third_item },
                new Label[]{ top_first_item, top_second_item, top_third_item },
                new Label[]{ top_first_item_counter, top_second_item_counter, top_third_item_counter }
        );
        fastMovingController.load();

        slowMovingController = new SlowMovingItemsController(
                new Label[]{ least_first_item, least_second_item, least_third_item },
                new Label[]{ least_first_item_type, least_second_item_type, least_third_item_type },
                new Label[]{ least_first_item_counter, least_second_item_counter, least_third_item_counter }
        );
        slowMovingController.load();

        // Recently wasted products
        List<Product> expiredProducts = products.stream()
                .filter(p -> {
                    LocalDate expiry = p.getLocalExpiryDate();
                    return expiry != null && expiry.isBefore(LocalDate.now());
                })
                .sorted((a, b) -> b.getLocalExpiryDate().compareTo(a.getLocalExpiryDate()))
                .toList();

        if (!expiredProducts.isEmpty()) {
            Product w1 = expiredProducts.get(0);
            wasted_first_item.setText(w1.getProductName());
            wasted_first_item_counter.setText(w1.getQuantity() + " wasted");
            wasted_first_item_type.setText(InventoryPresenter.getProductCategory(w1.getProductName(), products));
        } else {
            wasted_first_item.setText("No wasted items");
            wasted_first_item_counter.setText("—");
            wasted_first_item_type.setText("—");
        }

        if (expiredProducts.size() > 1) {
            Product w2 = expiredProducts.get(1);
            wasted_second_item.setText(w2.getProductName());
            wasted_second_item_counter.setText(w2.getQuantity() + " wasted");
            wasted_second_item_type.setText(InventoryPresenter.getProductCategory(w2.getProductName(), products));
        } else {
            wasted_second_item.setText("—");
            wasted_second_item_counter.setText("—");
            wasted_second_item_type.setText("—");
        }

        if (expiredProducts.size() > 2) {
            Product w3 = expiredProducts.get(2);
            wasted_third_item.setText(w3.getProductName());
            wasted_third_item_counter.setText(w3.getQuantity() + " wasted");
            wasted_third_item_type.setText(InventoryPresenter.getProductCategory(w3.getProductName(), products));
        } else {
            wasted_third_item.setText("—");
            wasted_third_item_counter.setText("—");
            wasted_third_item_type.setText("—");
        }

        // FIX: Only fetch notifications for this logged-in account
        List<NotificationItem> allNotifs = NotificationDAO.getAll(ownerId);

        // Expiring within 7 days logic
        int expiringSoonCount = 0;
        for (Product p : products) {
            LocalDate expiry = p.getLocalExpiryDate();
            if (expiry != null) {
                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), expiry);
                if (daysLeft >= 0 && daysLeft <= 7) {
                    expiringSoonCount++;
                }
            }
        }
        expiring_soon.setText(String.valueOf(expiringSoonCount));

        // Low stock or out of stock count logic
        HashSet<Integer> uniqueLowStockIds = new HashSet<>();
        for (NotificationItem n : allNotifs) {
            if (n.message.contains("out of stock")) {
                uniqueLowStockIds.add(n.productId);
            }
        }

        // 25% lower quantity criteria
        for (Product p : products) {
            int current  = p.getQuantity();
            int original = p.getOriginalQuantity();
            if (original > 0 && current <= original * 0.25) {
                uniqueLowStockIds.add(p.getId());
            }
        }

        int lowStockCount = uniqueLowStockIds.size();
        low_stock.setText(String.valueOf(lowStockCount));

        // Total expired units logic
        int totalExpiredUnits = 0;
        for (Product p : products) {
            LocalDate expiry = p.getLocalExpiryDate();
            if (expiry != null && expiry.isBefore(LocalDate.now())) {
                totalExpiredUnits += p.getQuantity();
            }
        }

        total_expireUnits.setText(String.valueOf(totalExpiredUnits));
        setExpiredLabel();
    }

    public void setExpiredLabel() {
        int ownerId = getCurrentUserId();
        ObservableList<Product> products = fetchDataFromTable.getAllProducts(ownerId);

        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(7);
        LocalDate sevenDaysAhead = today.plusDays(7);

        Set<String> expiringSoonSet = new HashSet<>();
        Set<String> outOfStockSet = new HashSet<>();
        Set<String> wastedSet = new HashSet<>();

        for (Product p : products) {
            String name = p.getProductName();
            LocalDate expiry = p.getLocalExpiryDate();

            if (expiry != null && !expiry.isBefore(today) && !expiry.isAfter(sevenDaysAhead)) {
                expiringSoonSet.add(name);
            }

            if (p.getQuantity() <= 0) {
                outOfStockSet.add(name);
            }

            if (expiry != null && !expiry.isBefore(sevenDaysAgo) && expiry.isBefore(today)) {
                wastedSet.add(name);
            }
        }

        set_expired_label(expiringSoonSet.size());
        set_out_of_stock_label(outOfStockSet.size());
        set_item_wasted_label(wastedSet.size());
    }

    public void set_expired_label(int data){
        expired_label.setText(data + " expiring items");
    }

    public void set_out_of_stock_label(int data){
        out_of_stock_label.setText(data + " out of stock");
    }

    public void set_item_wasted_label(int data){
        items_wasted_label.setText(data + (data == 1 ? " item" : " items ") + " wasted this past 7 days");
    }

    class ProductTotal {
        String name;
        int totalSold;
        LocalDate dateAdded;
        int insertionOrder;

        public ProductTotal(String name, int totalSold) {
            this.name = name;
            this.totalSold = totalSold;
            this.dateAdded = null;
            this.insertionOrder = Integer.MAX_VALUE;
        }

        public double getSalesRate() {
            if (dateAdded == null) return totalSold == 0 ? -0.0001 : totalSold;
            long daysActive = ChronoUnit.DAYS.between(dateAdded, LocalDate.now());
            if (daysActive <= 0) daysActive = 1;
            if (totalSold == 0) return -daysActive;
            return (double) totalSold / daysActive;
        }
    }

    // FIX: Modified to receive active owner ID parameters
    private void populateSellFirst(int ownerId) {
        sellFirstList.getChildren().clear();

        // FIX: Isolated query
        List<NotificationItem> all = NotificationDAO.getAll(ownerId);
        ObservableList<Product> products = fetchDataFromTable.getAllProducts(ownerId);

        Map<Integer, LocalDate> expiryMap = new HashMap<>();
        for (Product p : products) {
            expiryMap.put(p.getId(), p.getLocalExpiryDate());
        }

        List<NotificationItem> expired = all.stream()
                .filter(n -> n.type == NotificationItem.Type.CRITICAL
                        && (n.message.contains("has expired!") ||  n.message.contains("expires in 1 day")))
                .sorted(Comparator.comparing(n -> {
                    LocalDate exp = expiryMap.get(n.productId);
                    if (exp == null) return Long.MAX_VALUE;
                    return ChronoUnit.DAYS.between(LocalDate.now(), exp);
                }))
                .collect(Collectors.toList());

        List<NotificationItem> nearExpiry = all.stream()
                .filter(n -> n.type == NotificationItem.Type.WARNING && n.message.contains("is nearing expiry"))
                .sorted(Comparator.comparing(n -> {
                    LocalDate exp = expiryMap.get(n.productId);
                    if (exp == null) return Long.MAX_VALUE;
                    return ChronoUnit.DAYS.between(LocalDate.now(), exp);
                }))
                .collect(Collectors.toList());

        Set<Integer> urgentProductIds = new HashSet<>();
        expired.forEach(n -> urgentProductIds.add(n.productId));
        nearExpiry.forEach(n -> urgentProductIds.add(n.productId));

        List<NotificationItem> inStock = all.stream()
                .filter(n -> n.type == NotificationItem.Type.INFO
                        && n.message.contains("added to inventory")
                        && !urgentProductIds.contains(n.productId))
                .sorted(Comparator.comparing(n -> {
                    LocalDate exp = expiryMap.get(n.productId);
                    if (exp == null) return Long.MAX_VALUE;
                    return ChronoUnit.DAYS.between(LocalDate.now(), exp);
                }))
                .collect(Collectors.toList());

        List<NotificationItem> finalList = new ArrayList<>();
        finalList.addAll(expired);
        finalList.addAll(nearExpiry);
        finalList.addAll(inStock);

        List<NotificationItem> toDisplay = finalList.stream()
                .limit(7)
                .collect(Collectors.toList());

        if (toDisplay.isEmpty()) {
            Label empty = new Label("No items to flag right now.");
            empty.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13; -fx-text-fill: #aaaaaa;");
            empty.setPadding(new Insets(24, 0, 0, 20));
            sellFirstList.getChildren().add(empty);
        } else {
            for (NotificationItem item : toDisplay) {
                sellFirstList.getChildren().add(buildRow(item, products));
            }
        }
    }

    private HBox buildRow(NotificationItem item, ObservableList<Product> products) {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(16, 20, 16, 20));
        row.setStyle("-fx-background-color: white; -fx-border-color: transparent transparent #eeeeee transparent; -fx-border-width: 0 0 1 0;");

        Pane iconPane = new Pane();
        iconPane.setPrefSize(40, 40);
        iconPane.setMinSize(40, 40);
        iconPane.setMaxSize(40, 40);
        iconPane.setStyle("-fx-background-color: " + iconBgColor(item.type) + "; -fx-background-radius: 10;");

        try {
            javafx.scene.shape.SVGPath svgIcon = new javafx.scene.shape.SVGPath();
            svgIcon.setFill(javafx.scene.paint.Color.TRANSPARENT);
            svgIcon.setStrokeWidth(1.5);

            boolean validShape = true;
            switch (item.type) {
                case CRITICAL -> {
                    svgIcon.setContent("M12 7V12L14.5 13.5M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z");
                    svgIcon.setStroke(javafx.scene.paint.Color.RED);
                }
                case WARNING -> {
                    svgIcon.setContent("M12 7V12L14.5 13.5M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z");
                    svgIcon.setStroke(javafx.scene.paint.Color.ORANGE);
                }
                case INFO -> {
                    svgIcon.setContent("M12 7.01001V7.00002M12 17L12 10M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z");
                    svgIcon.setStroke(javafx.scene.paint.Color.DODGERBLUE);
                }
                default -> validShape = false;
            }

            if (validShape) {
                double width = svgIcon.getBoundsInLocal().getWidth();
                double height = svgIcon.getBoundsInLocal().getHeight();
                if (width > 0 && height > 0) {
                    double scale = Math.min(20.0 / width, 20.0 / height);
                    svgIcon.setScaleX(scale);
                    svgIcon.setScaleY(scale);
                }
                svgIcon.setLayoutX(10);
                svgIcon.setLayoutY(10);
                iconPane.getChildren().add(svgIcon);
            }
        } catch (Exception ex) {
            System.err.println("Error rendering SVG path: " + ex.getMessage());
        }

        VBox textContent = new VBox(3);
        HBox.setHgrow(textContent, Priority.ALWAYS);

        Label nameLabel = new Label(extractName(item.message));
        nameLabel.setStyle("-fx-font-family: 'Segoe UI Bold'; -fx-font-size: 16; -fx-text-fill: #000000d9;");

        String category = products.stream()
                .filter(p -> p.getId() == item.productId)
                .map(Product::getCategory)
                .findFirst()
                .orElse("—");
        Label categoryLabel = new Label(category);
        categoryLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12; -fx-text-fill: #000000a9;");

        textContent.getChildren().addAll(nameLabel, categoryLabel);

        Pane badgePane = new Pane();
        badgePane.setPrefSize(117, 40);
        badgePane.setMinSize(117, 40);
        badgePane.setStyle("-fx-background-color: " + badgeBgColor(item) + "; -fx-background-radius: 10;");

        Label badgeTop = new Label(badgeTopText(item));
        badgeTop.setLayoutX(11);
        badgeTop.setLayoutY(4);
        badgeTop.setPrefWidth(100);
        badgeTop.setStyle("-fx-alignment: CENTER; -fx-font-family: 'System Bold'; -fx-font-size: 13; -fx-text-fill: " + textColor(item.type) + ";");

        Label badgeSub = new Label(badgeSubText(item, products));
        badgeSub.setLayoutX(12);
        badgeSub.setLayoutY(21);
        badgeSub.setPrefWidth(92);
        badgeSub.setStyle("-fx-alignment: CENTER_RIGHT; -fx-font-size: 11; -fx-text-fill: " + textColor(item.type) + ";");

        badgePane.getChildren().addAll(badgeTop, badgeSub);

        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: transparent transparent #eeeeee transparent; -fx-border-width: 0 0 1 0; -fx-cursor: hand;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: white; -fx-border-color: transparent transparent #eeeeee transparent; -fx-border-width: 0 0 1 0;"));

        row.getChildren().addAll(iconPane, textContent, badgePane);
        return row;
    }

    private String extractName(String message) {
        int cut = message.indexOf(" has");
        if (cut == -1) cut = message.indexOf(" is");
        if (cut == -1) cut = message.indexOf(" expires");
        return cut > 0 ? message.substring(0, cut) : message;
    }

    private String badgeTopText(NotificationItem item) {
        String msg = item.message;
        if (msg.contains("has expired!"))      return "Expired";
        if (msg.contains("expires in 1 day"))  return "Expires Today";
        if (msg.contains("is nearing expiry")) return "Near Expiry";
        if (msg.contains("out of stock"))      return "Empty";
        if (msg.contains("stock is low"))      return "Low Stock";
        return "In Stock";
    }

    private String badgeSubText(NotificationItem item, ObservableList<Product> products) {
        String msg = item.message;
        if (msg.contains("has expired!"))     return "- Expired";
        if (msg.contains("expires in 1 day")) return "- 1d left";

        if (msg.contains("is nearing expiry")) {
            int dash = msg.indexOf("— ");
            if (dash != -1) {
                String after = msg.substring(dash + 2).replace(".", "").trim();
                String[] parts = after.split(" ");
                if (parts.length >= 1) return "- " + parts[0] + "d left";
            }
            return "";
        }

        return products.stream()
                .filter(p -> p.getId() == item.productId)
                .map(p -> {
                    if (p.getLocalExpiryDate() == null) return "";
                    long days = ChronoUnit.DAYS.between(LocalDate.now(), p.getLocalExpiryDate());
                    return "- " + days + "d left";
                })
                .findFirst()
                .orElse("");
    }

    private String iconBgColor(NotificationItem.Type type) {
        return switch (type) {
            case CRITICAL -> "#FEF2F2";
            case WARNING  -> "#FFF7ED";
            case INFO     -> "#F0FDF4";
        };
    }

    private String badgeBgColor(NotificationItem item) {
        String msg = item.message;
        if (msg.contains("has expired!"))      return "#FEF2F2";
        if (msg.contains("expires in 1 day"))  return "#FFF7ED";
        if (msg.contains("is nearing expiry")) return "#FFF7ED";
        return "#F0FDF4";
    }

    private String textColor(NotificationItem.Type type) {
        return switch (type) {
            case CRITICAL -> "#ef4444";
            case WARNING  -> "#c2410c";
            case INFO     -> "#16a34a";
        };
    }


    public void onNotificationIconClick(MouseEvent mouseEvent) { utility.switchToNotification(mouseEvent); }
    public void goToInventory(ActionEvent event) { utility.switchScene(event, "/com/bigo/tindatrack/Inventory-view.fxml"); }
    public void goToInsightButton(ActionEvent event) { utility.switchScene(event, "/com/bigo/tindatrack/Insights-view.fxml"); }
    public void goToStockactivityButton(ActionEvent event) { utility.switchScene(event, "/com/bigo/tindatrack/StockActivity-view.fxml"); }
    public void goToSettingButton(ActionEvent event) { utility.switchScene(event, "/com/bigo/tindatrack/SettingsMarket-view.fxml"); }
    public void goTovVewInsights(ActionEvent event) { utility.switchScene(event, "/com/bigo/tindatrack/Insights-view.fxml"); }
    public void goToDashboard(ActionEvent event) { utility.switchScene(event, "/com/bigo/tindatrack/Dashboard-view.fxml"); }
    public void goToNotifications(ActionEvent event) { utility.switchScene(event, "/com/bigo/tindatrack/Notification-view.fxml"); }
    public void viewAllGotoInventory(ActionEvent event){ utility.switchScene(event,"/com/bigo/tindatrack/Inventory-view.fxml"); }

    public void setLogout(ActionEvent event) {
        com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.clearSession();
        this.user = null;
        utility.switchScene(event, "/com/bigo/tindatrack/Login-view.fxml");
    }
}