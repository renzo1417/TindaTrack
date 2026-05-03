package com.bigo.tindatrack.Controller;

import com.bigo.tindatrack.Controller.Notification.NotificationItem;
import com.bigo.tindatrack.Controller.Notification.NotificationService;
import com.bigo.tindatrack.SQLite_Database.NotificationManagement.NotificationDAO;
import com.bigo.tindatrack.SQLite_Database.productsManagement.fetchDataFromTable;
import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.data.models.User;
import com.bigo.tindatrack.utils.utility;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId;
import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.loadUser;

public class DashboardController {


    @FXML private Label welcomeField, username_top, username_bottom, dateField;


    @FXML private VBox sellFirstList;

    @FXML private Button inventoryButton, insightButton,
            stockactivityButton, settingButton, viewAllerts;

    private User user = loadUser();


    @FXML
    public void initialize() {
        if (user == null) {
            System.out.println("Error: No user found!");
            return;
        }

        welcomeField.setText("Hello " + user.getUsername()
                + "! - Here's your inventory overview");
        username_top.setText(user.getUsername());
        username_bottom.setText(user.getUsername());

        LocalDate today     = LocalDate.now();
        String    dayName   = today.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String    formatted = today.format(
                DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        dateField.setText(dayName + ", " + formatted + " - ");

        NotificationService.evaluateAllProducts();
        populateSellFirst();
    }


    //populate to sellfirst
    private void populateSellFirst() {
        sellFirstList.getChildren().clear();

        List<NotificationItem> all = NotificationDAO.getAll();
        int ownerId = getCurrentUserId();
        ObservableList<Product> products = fetchDataFromTable.getAllProducts(ownerId);

       //list of expired
        List<NotificationItem> expired = all.stream()
                .filter(n -> n.type == NotificationItem.Type.CRITICAL
                        && (n.message.contains("has expired!")
                        ||  n.message.contains("expires in 1 day")))
                .limit(2)
                .collect(Collectors.toList());

      // list of near expiry
        List<NotificationItem> nearExpiry = all.stream()
                .filter(n -> n.type == NotificationItem.Type.WARNING
                        && n.message.contains("is nearing expiry"))
                .limit(2)
                .collect(Collectors.toList());

        // products already in expired/nearExpiry exclude from inStock
        Set<Integer> urgentProductIds = new HashSet<>();
        expired.forEach(n -> urgentProductIds.add(n.productId));
        nearExpiry.forEach(n -> urgentProductIds.add(n.productId));

        // safe producs or instock products=
        List<NotificationItem> inStock = all.stream()
                .filter(n -> n.type == NotificationItem.Type.INFO
                        && n.message.contains("added to inventory")
                        && !urgentProductIds.contains(n.productId))
                .limit(2)
                .collect(Collectors.toList());

        // this is for rows
        for (NotificationItem item : expired)
            sellFirstList.getChildren().add(buildRow(item, products));
        for (NotificationItem item : nearExpiry)
            sellFirstList.getChildren().add(buildRow(item, products));
        for (NotificationItem item : inStock)
            sellFirstList.getChildren().add(buildRow(item, products));

       //this will empty the its state in dashboard
        if (expired.isEmpty() && nearExpiry.isEmpty() && inStock.isEmpty()) {
            Label empty = new Label("No items to flag right now.");
            empty.setStyle(
                    "-fx-font-family: 'Segoe UI';" +
                            "-fx-font-size: 13;" +
                            "-fx-text-fill: #aaaaaa;"
            );
            empty.setPadding(new Insets(24, 0, 0, 20));
            sellFirstList.getChildren().add(empty);
        }
    }

    // builds rows
    private HBox buildRow(NotificationItem item, ObservableList<Product> products) {


        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(16, 20, 16, 20));
        row.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: transparent transparent #eeeeee transparent;" +
                        "-fx-border-width: 0 0 1 0;"
        );


        Pane iconPane = new Pane();
        iconPane.setPrefSize(40, 40);
        iconPane.setMinSize(40, 40);
        iconPane.setMaxSize(40, 40);
        iconPane.setStyle(
                "-fx-background-color: " + iconBgColor(item.type) + ";" +
                        "-fx-background-radius: 10;"
        );

        Circle dot = new Circle(5);
        dot.setStyle("-fx-fill: " + textColor(item.type) + ";");
        dot.setLayoutX(20);
        dot.setLayoutY(20);
        iconPane.getChildren().add(dot);


        VBox textContent = new VBox(3);
        HBox.setHgrow(textContent, Priority.ALWAYS);

        String productName = extractName(item.message);
        Label nameLabel = new Label(productName);
        nameLabel.setStyle(
                "-fx-font-family: 'Segoe UI Bold';" +
                        "-fx-font-size: 16;" +
                        "-fx-text-fill: #000000d9;"
        );

        String category = products.stream()
                .filter(p -> p.getId() == item.productId)
                .map(Product::getCategory)
                .findFirst()
                .orElse("—");
        Label categoryLabel = new Label(category);
        categoryLabel.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 12;" +
                        "-fx-text-fill: #000000a9;"
        );

        textContent.getChildren().addAll(nameLabel, categoryLabel);


        Pane badgePane = new Pane();
        badgePane.setPrefSize(117, 40);
        badgePane.setMinSize(117, 40);
        badgePane.setStyle(
                "-fx-background-color: " + badgeBgColor(item) + ";" +
                        "-fx-background-radius: 10;"
        );

        Label badgeTop = new Label(badgeTopText(item));
        badgeTop.setLayoutX(11);
        badgeTop.setLayoutY(4);
        badgeTop.setPrefWidth(100);
        badgeTop.setStyle(
                "-fx-alignment: CENTER;" +
                        "-fx-font-family: 'System Bold';" +
                        "-fx-font-size: 13;" +
                        "-fx-text-fill: " + textColor(item.type) + ";"
        );

        Label badgeSub = new Label(badgeSubText(item, products));
        badgeSub.setLayoutX(12);
        badgeSub.setLayoutY(21);
        badgeSub.setPrefWidth(92);
        badgeSub.setStyle(
                "-fx-alignment: CENTER_RIGHT;" +
                        "-fx-font-size: 11;" +
                        "-fx-text-fill: " + textColor(item.type) + ";"
        );

        badgePane.getChildren().addAll(badgeTop, badgeSub);


        row.setOnMouseEntered(e -> row.setStyle(
                "-fx-background-color: #f9f9f9;" +
                        "-fx-border-color: transparent transparent #eeeeee transparent;" +
                        "-fx-border-width: 0 0 1 0;" +
                        "-fx-cursor: hand;"
        ));
        row.setOnMouseExited(e -> row.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: transparent transparent #eeeeee transparent;" +
                        "-fx-border-width: 0 0 1 0;"
        ));

        row.getChildren().addAll(iconPane, textContent, badgePane);
        return row;
    }


    // helperrs
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

       // for instock it will calculate the real expiry date
        return products.stream()
                .filter(p -> p.getId() == item.productId)
                .map(p -> {
                    if (p.getLocalExpiryDate() == null) return "";
                    long days = ChronoUnit.DAYS.between(
                            LocalDate.now(), p.getLocalExpiryDate());
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
        if (msg.contains("has expired!"))       return "#FEF2F2";
        if (msg.contains("expires in 1 day"))   return "#FFF7ED";
        if (msg.contains("is nearing expiry"))  return "#FFF7ED";
        return "#F0FDF4";
    }

    private String textColor(NotificationItem.Type type) {
        return switch (type) {
            case CRITICAL -> "#ef4444";
            case WARNING  -> "#c2410c";
            case INFO     -> "#16a34a";
        };
    }

    // this will refresh to show real time products
    public void refreshSellFirst() {
        NotificationService.evaluateAllProducts();
        populateSellFirst();
    }


    public void goToInventory(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Inventory-view.fxml");
    }

    public void goToInsightButton(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/insight-view.fxml");
    }

    public void goToStockactivityButton(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/StockActivity-view.fxml");
    }

    public void goToSettingButton(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Settings-view.fxml");
    }

    public void goTovVewAllerts(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Inventory-view.fxml");
    }

    public void goToDashboard(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Dashboard-view.fxml");
    }

    public void goToNotifications(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Notification-view.fxml");
    }

    public void setLogout(ActionEvent event) {
        com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.clearSession();
        this.user = null;
        utility.switchScene(event, "/com/bigo/tindatrack/Login-view.fxml");
    }
}