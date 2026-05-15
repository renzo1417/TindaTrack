package com.bigo.tindatrack.Controller.Insights;

import com.bigo.tindatrack.data.StockDetails.StockDetails;
import com.bigo.tindatrack.SQLite_Database.StockManagement.StockFetchFromTable;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.*;

import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId;

public class SlowMovingItemsController {
    //  labels from InsightsController
    private Label[] nameLabels;
    private Label[] categoryLabels;
    private Label[] countLabels;

    public SlowMovingItemsController(
            Label[] nameLabels,
            Label[] categoryLabels,
            Label[] countLabels
    ) {
        this.nameLabels    = nameLabels;
        this.categoryLabels = categoryLabels;
        this.countLabels   = countLabels;
    }

    public void load() {
        int ownerId = getCurrentUserId();

        for (int i = 0; i < 5; i++) {
            nameLabels[i].setVisible(false);
            nameLabels[i].setManaged(false);
            categoryLabels[i].setVisible(false);
            categoryLabels[i].setManaged(false);
            countLabels[i].setVisible(false);
            countLabels[i].setManaged(false);
        }

        if (ownerId == -1) return;
        // stock activity logs from DB
        // ObservableList

        ObservableList<StockDetails> logs = StockFetchFromTable.getActivitiesFromDB(ownerId);
        if (logs.isEmpty()) return;

        // count total stock change events per product name
        // Used HashMap for the logic
        Map<String, String> categoryMap = buildCategoryMap(ownerId);


        Map<String, Integer> movementCount = new LinkedHashMap<>();
        for (StockDetails s : logs) {
            String name = s.getProductName();

            if (!categoryMap.containsKey(name)) continue;
            movementCount.put(name, movementCount.getOrDefault(name, 0) + 1);
        }
        //  sorted to ascending by movement count the fewest changes = slowest moving
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(movementCount.entrySet());
        sorted.sort(Comparator.comparingInt(Map.Entry::getValue));

        // top 5 slowest
        int limit = Math.min(5, sorted.size());
        if (limit == 0) return;

        // category for building a name category map from the logs
        //  items for labels
        for (int i = 0; i < limit; i++) {
            String productName = sorted.get(i).getKey();
            int    count       = sorted.get(i).getValue();
            String category    = categoryMap.getOrDefault(productName, "—");

            nameLabels[i].setText(productName);
            categoryLabels[i].setText(category);
            countLabels[i].setText(count + "x");

            nameLabels[i].setVisible(true);
            nameLabels[i].setManaged(true);
            categoryLabels[i].setVisible(true);
            categoryLabels[i].setManaged(true);
            countLabels[i].setVisible(true);
            countLabels[i].setManaged(true);
        }
    }
    // builds the productName by category lookup from the table for product
    // this will exclude all the expired items
    private Map<String, String> buildCategoryMap(int ownerId) {
        Map<String, String> map = new HashMap<>();
        LocalDate today = LocalDate.now();

        ObservableList<com.bigo.tindatrack.Product.Product> products =
                com.bigo.tindatrack.SQLite_Database.productsManagement
                        .fetchDataFromTable.getAllProducts(ownerId);

        for (com.bigo.tindatrack.Product.Product p : products) {
            LocalDate expiry = p.getLocalExpiryDate();

            // this skip product that are already expired
            if (expiry != null && expiry.isBefore(today)) continue;

            //  only active the non-expired products put into the map
            map.put(p.getProductName(), p.getCategory());
        }

        return map;
    }
}