package com.bigo.tindatrack.Controller.Notification;

import com.bigo.tindatrack.SQLite_Database.NotificationManagement.NotificationDAO;
import com.bigo.tindatrack.SQLite_Database.userManagement.UserUIHelper;
import com.bigo.tindatrack.data.models.User;
import com.bigo.tindatrack.utils.utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.loadUser;

public class NotificationController {

    @FXML private VBox         notificationList;
    @FXML private Label        unreadCountLabel;
    @FXML private Button       markAllReadButton;
    @FXML private ToggleButton filterAll;
    @FXML private ToggleButton filterUnread;
    @FXML private ToggleButton filterCritical;
    @FXML private ToggleButton filterWarning;
    @FXML private ToggleButton filterInfo;
    @FXML private Label username_top, username_bottom, username_top_initial, username_bottom_initial;

    private List<NotificationItem> allItems;
    private String currentFilter = "ALL";
    private User user = loadUser();

    @FXML
    public void initialize() {
        NotificationService.evaluateAllProducts();

        if (user != null) {
            allItems = NotificationDAO.getAll(user.getId());
        } else {
            allItems = List.of();
        }

        renderList(allItems);
        refreshUnreadCount();
        setupFilterButtons();
        markAllReadButton.setOnAction(e -> handleMarkAllRead());

        setupUsername();
    }


    public void setupUsername(){
        UserUIHelper.setupUserUI(username_top_initial,
                username_bottom_initial,
                username_top,
                username_bottom,
                loadUser());
    }

    public void refresh() {
        if (user != null) {
            allItems = NotificationDAO.getAll(user.getId());
        }
        renderList(getCurrentFilteredList());
        refreshUnreadCount();
    }

    private void setupFilterButtons() {
        filterAll.setOnAction(e      -> applyFilter("ALL"));
        filterUnread.setOnAction(e   -> applyFilter("UNREAD"));
        filterCritical.setOnAction(e -> applyFilter("CRITICAL"));
        filterWarning.setOnAction(e  -> applyFilter("WARNING"));
        filterInfo.setOnAction(e     -> applyFilter("INFO"));
    }

    private void applyFilter(String filter) {
        currentFilter = filter;
        renderList(getCurrentFilteredList());
        styleFilterButtons(filter);
    }

    private List<NotificationItem> getCurrentFilteredList() {
        return switch (currentFilter) {
            case "UNREAD"   -> allItems.stream()
                    .filter(n -> !n.isRead)
                    .collect(Collectors.toList());
            case "CRITICAL" -> allItems.stream()
                    .filter(n -> n.type == NotificationItem.Type.CRITICAL)
                    .collect(Collectors.toList());
            case "WARNING"  -> allItems.stream()
                    .filter(n -> n.type == NotificationItem.Type.WARNING)
                    .collect(Collectors.toList());
            case "INFO"     -> allItems.stream()
                    .filter(n -> n.type == NotificationItem.Type.INFO)
                    .collect(Collectors.toList());
            default         -> allItems;
        };
    }

    private void handleMarkAllRead() {
        if (user != null) {
            NotificationDAO.markAllAsRead(user.getId());
            allItems.forEach(n -> n.isRead = true);
            renderList(getCurrentFilteredList());
            refreshUnreadCount();
        }
    }

    private void renderList(List<NotificationItem> items) {
        notificationList.getChildren().clear();

        if (items == null || items.isEmpty()) {
            showEmptyState();
            return;
        }

        for (NotificationItem item : items) {
            notificationList.getChildren().add(buildRow(item));
        }
    }

    private void showEmptyState() {
        VBox empty = new VBox(10);
        empty.setAlignment(Pos.CENTER);
        empty.setPrefHeight(220);

        Label msg = new Label("No notifications.");
        msg.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14; -fx-text-fill: #aaaaaa;");
        empty.getChildren().add(msg);
        notificationList.getChildren().add(empty);
    }

    private HBox buildRow(NotificationItem item) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.TOP_LEFT);
        row.setPadding(new Insets(20, 32, 20, 32));
        setRowStyle(row, item);

        StackPane circle = new StackPane();
        circle.setPrefSize(40, 40);
        circle.setMinSize(40, 40);
        circle.setMaxSize(40, 40);
        circle.setStyle("-fx-background-color: " + bgColor(item.type) + "; -fx-background-radius: 20;");

        if (item.type == NotificationItem.Type.CRITICAL || item.type == NotificationItem.Type.WARNING) {
            try {
                javafx.scene.shape.SVGPath svgIcon = new javafx.scene.shape.SVGPath();
                svgIcon.setContent("M12 15H12.01M12 12V9M4.98207 19H19.0179C20.5615 19 21.5233 17.3256 20.7455 15.9923L13.7276 3.96153C12.9558 2.63852 11.0442 2.63852 10.2724 3.96153L3.25452 15.9923C2.47675 17.3256 3.43849 19 4.98207 19Z");
                svgIcon.setFill(javafx.scene.paint.Color.TRANSPARENT);
                svgIcon.setStrokeWidth(1.5);
                svgIcon.setStroke(item.type == NotificationItem.Type.CRITICAL ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.ORANGE);
                circle.getChildren().add(svgIcon);
            } catch (Exception ex) {
                System.err.println("Error rendering SVG path: " + ex.getMessage());
            }
        }

        if (item.type == NotificationItem.Type.INFO) {
            try {
                javafx.scene.shape.SVGPath svgIcon = new javafx.scene.shape.SVGPath();
                svgIcon.setContent("M12 7.01001V7.00002M12 17L12 10M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z");
                svgIcon.setFill(javafx.scene.paint.Color.TRANSPARENT);
                svgIcon.setStrokeWidth(1.5);
                svgIcon.setStroke(javafx.scene.paint.Color.DODGERBLUE);
                circle.getChildren().add(svgIcon);
            } catch (Exception ex) {
                System.err.println("Error rendering SVG path: " + ex.getMessage());
            }
        }

        VBox content = new VBox(6);
        HBox.setHgrow(content, Priority.ALWAYS);

        HBox badgeRow = new HBox(8);
        badgeRow.setAlignment(Pos.CENTER_LEFT);

        Label badge = new Label(item.type.name());
        badge.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 10; -fx-font-weight: bold; -fx-text-fill: " + textColor(item.type) + "; -fx-background-color: " + bgColor(item.type) + "; -fx-background-radius: 5; -fx-padding: 3 9 3 9;");
        badgeRow.getChildren().add(badge);

        if (!item.isRead) {
            StackPane dot = new StackPane();
            dot.setPrefSize(8, 8);
            dot.setMaxSize(8, 8);
            dot.setStyle("-fx-background-color: " + textColor(item.type) + "; -fx-background-radius: 4;");
            badgeRow.getChildren().add(dot);
        }

        Label message = new Label(item.message);
        message.setWrapText(true);
        message.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13.5;" + (item.isRead ? "-fx-text-fill: #444444;" : "-fx-font-weight: bold; -fx-text-fill: #1a1a1a;"));

        Label timestamp = new Label(item.timestamp);
        timestamp.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 11.5; -fx-text-fill: #aaaaaa;");

        content.getChildren().addAll(badgeRow, message, timestamp);
        row.getChildren().addAll(circle, content);

        row.setOnMouseClicked(e -> {
            if (!item.isRead) {
                item.isRead = true;
                NotificationDAO.markAsRead(item.id);
                renderList(getCurrentFilteredList());
                refreshUnreadCount();
            }
        });

        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: transparent transparent #eeeeee transparent; -fx-border-width: 0 0 1 0; -fx-cursor: hand;"));
        row.setOnMouseExited(e -> setRowStyle(row, item));

        return row;
    }

    private void setRowStyle(HBox row, NotificationItem item) {
        row.setStyle((item.isRead ? "-fx-background-color: #fafafa;" : "-fx-background-color: white;") + "-fx-border-color: transparent transparent #eeeeee transparent; -fx-border-width: 0 0 1 0; -fx-cursor: hand;");
    }

    private void refreshUnreadCount() {
        long count = allItems.stream().filter(n -> !n.isRead).count();
        unreadCountLabel.setText(count + " unread alert" + (count == 1 ? "" : "s"));
        filterUnread.setText("Unread (" + count + ")");
    }

    private void styleFilterButtons(String active) {
        String on = "-fx-background-color: #2e8b2e; -fx-background-radius: 20; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 13; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 6 22 6 22; -fx-border-color: transparent;";
        String off = "-fx-background-color: transparent; -fx-background-radius: 20; -fx-text-fill: #444444; -fx-font-family: 'Segoe UI'; -fx-font-size: 13; -fx-cursor: hand; -fx-padding: 6 22 6 22; -fx-border-color: #d0d0d0; -fx-border-radius: 20; -fx-border-width: 1;";

        filterAll.setStyle(active.equals("ALL")      ? on : off);
        filterUnread.setStyle(active.equals("UNREAD")    ? on : off);
        filterCritical.setStyle(active.equals("CRITICAL") ? on : off);
        filterWarning.setStyle(active.equals("WARNING")  ? on : off);
        filterInfo.setStyle(active.equals("INFO")        ? on : off);
    }

    private String bgColor(NotificationItem.Type type) {
        return switch (type) {
            case CRITICAL -> "#fdecea";
            case WARNING  -> "#fff8e1";
            case INFO     -> "#e8f0fe";
        };
    }

    private String textColor(NotificationItem.Type type) {
        return switch (type) {
            case CRITICAL -> "#d32f2f";
            case WARNING  -> "#e6a817";
            case INFO     -> "#1a73e8";
        };
    }

    public void onNotificationIconClick(MouseEvent mouseEvent) { utility.switchToNotification(mouseEvent); }
    public void goToInventory(ActionEvent event) { utility.switchScene(event, "/com/bigo/tindatrack/Inventory-view.fxml"); }
    public void goToInsight(ActionEvent event) { utility.switchScene(event, "/com/bigo/tindatrack/Insights-view.fxml"); }
    public void goToStockActivity(ActionEvent event) { utility.switchScene(event, "/com/bigo/tindatrack/StockActivity-view.fxml"); }
    public void goToSetting(ActionEvent event) { utility.switchScene(event, "/com/bigo/tindatrack/SettingsMarket-view.fxml"); }
    public void goToDashboard(ActionEvent event) { utility.switchScene(event, "/com/bigo/tindatrack/Dashboard-view.fxml"); }
    public void goToNotofication(ActionEvent event) { utility.switchScene(event, "/com/bigo/tindatrack/Notification-view.fxml"); }

    public void setLogout(ActionEvent event) {
        com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.clearSession();
        this.user = null;
        utility.switchScene(event, "/com/bigo/tindatrack/Login-view.fxml");
    }
}