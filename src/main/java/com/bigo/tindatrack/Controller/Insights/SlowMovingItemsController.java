package com.bigo.tindatrack.Controller.Insights;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.Sales.Sales;
import com.bigo.tindatrack.SQLite_Database.SalesManagement.SalesManagement;
import com.bigo.tindatrack.SQLite_Database.StockManagement.StockFetchFromTable;
import com.bigo.tindatrack.SQLite_Database.productsManagement.fetchDataFromTable;
import com.bigo.tindatrack.data.StockDetails.StockDetails;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId;

public class SlowMovingItemsController {

    private Label[] nameLabels;
    private Label[] categoryLabels;
    private Label[] countLabels;

    public SlowMovingItemsController(
            Label[] nameLabels,
            Label[] categoryLabels,
            Label[] countLabels
    ) {
        this.nameLabels     = nameLabels;
        this.categoryLabels = categoryLabels;
        this.countLabels    = countLabels;
    }

    public void load() {
        for (int i = 0; i < nameLabels.length; i++) {
            nameLabels[i].setVisible(false);
            nameLabels[i].setManaged(false);
            categoryLabels[i].setVisible(false);
            categoryLabels[i].setManaged(false);
            countLabels[i].setVisible(false);
            countLabels[i].setManaged(false);
        }

        int ownerId = getCurrentUserId();
        if (ownerId == -1) return;

        ObservableList<Product> products           = fetchDataFromTable.getAllProducts(ownerId);
        ObservableList<Sales> rawSales             = SalesManagement.getSalesHistory(ownerId);
        ObservableList<StockDetails> allActivities = StockFetchFromTable.getActivitiesFromDB(ownerId);

        if (products.isEmpty()) return;

        // Category lookup
        Map<String, String> categoryLookup = new HashMap<>();
        for (Product p : products) {
            categoryLookup.put(p.getProductName(), p.getCategory());
        }

        // Build combined sales totals
        List<ProductTotal> combinedTotals = new ArrayList<>();
        for (Sales sale : rawSales) {
            boolean found = false;
            for (ProductTotal pt : combinedTotals) {
                if (pt.name.equals(sale.getName())) {
                    pt.totalSold += sale.getQuantity();
                    found = true;
                    break;
                }
            }
            if (!found) {
                combinedTotals.add(new ProductTotal(sale.getName(), sale.getQuantity()));
            }
        }

        // Date added logic
        Collections.reverse(allActivities);
        Map<String, LocalDate> addedDates = new LinkedHashMap<>();
        List<String> insertionOrderList   = new ArrayList<>();
        for (StockDetails sd : allActivities) {
            if (sd.getOldQty() == 0 && !addedDates.containsKey(sd.getProductName())) {
                addedDates.put(sd.getProductName(), LocalDate.parse(sd.getDate()));
                insertionOrderList.add(sd.getProductName());
            }
        }

        for (ProductTotal pt : combinedTotals) {
            pt.dateAdded      = addedDates.get(pt.name);
            pt.category       = categoryLookup.getOrDefault(pt.name, "—");
            int idx           = insertionOrderList.indexOf(pt.name);
            pt.insertionOrder = (idx == -1) ? Integer.MAX_VALUE : idx;
        }

        // Only active non-expired products
        Set<String> activeNames = new HashSet<>();
        for (Product p : products) {
            LocalDate expiry = p.getLocalExpiryDate();
            if (expiry == null || !expiry.isBefore(LocalDate.now())) {
                activeNames.add(p.getProductName());
            }
        }
        combinedTotals.removeIf(pt -> !activeNames.contains(pt.name));

        // Seed 0-sold active products
        Set<String> alreadyInTotals = new HashSet<>();
        for (ProductTotal pt : combinedTotals) alreadyInTotals.add(pt.name);
        for (Product p : products) {
            LocalDate expiry = p.getLocalExpiryDate();
            if (expiry != null && expiry.isBefore(LocalDate.now())) continue;
            if (!alreadyInTotals.contains(p.getProductName())) {
                ProductTotal zeroPt   = new ProductTotal(p.getProductName(), 0);
                zeroPt.dateAdded      = addedDates.get(p.getProductName());
                zeroPt.category       = categoryLookup.getOrDefault(p.getProductName(), "—");
                int idx               = insertionOrderList.indexOf(p.getProductName());
                zeroPt.insertionOrder = (idx == -1) ? Integer.MAX_VALUE : idx;
                combinedTotals.add(zeroPt);
            }
        }

        // Remove 0-sold AFTER seeding so none slip through
        combinedTotals.removeIf(pt -> pt.totalSold == 0);
        if (combinedTotals.isEmpty()) return;

        // Sort descending by sales rate — same order as FastMovingItemsController
        combinedTotals.sort((a, b) -> {
            int cmp = Double.compare(b.getSalesRate(), a.getSalesRate());
            return cmp != 0 ? cmp : Integer.compare(a.insertionOrder, b.insertionOrder);
        });

        // Skip the fast moving slice (top n), take from the bottom of what's left
        int fastCount = nameLabels.length;
        int total     = combinedTotals.size();

        if (fastCount >= total) return;

        List<ProductTotal> slowGroup  = combinedTotals.subList(fastCount, total);
        int takeFromEnd               = Math.min(nameLabels.length, slowGroup.size());

        // Take from the bottom of slowGroup, reverse so highest of slow group shows first
        List<ProductTotal> slowest = new ArrayList<>(
                slowGroup.subList(slowGroup.size() - takeFromEnd, slowGroup.size())
        );


        for (int i = 0; i < slowest.size(); i++) {
            ProductTotal pt = slowest.get(i);

            nameLabels[i].setText(pt.name);
            nameLabels[i].setVisible(true);
            nameLabels[i].setManaged(true);

            categoryLabels[i].setText(pt.category);
            categoryLabels[i].setVisible(true);
            categoryLabels[i].setManaged(true);

            countLabels[i].setText(pt.totalSold + "x");
            countLabels[i].setVisible(true);
            countLabels[i].setManaged(true);
        }
    }

    private static class ProductTotal {
        String    name;
        String    category;
        int       totalSold;
        LocalDate dateAdded;
        int       insertionOrder;

        ProductTotal(String name, int totalSold) {
            this.name           = name;
            this.category       = "—";
            this.totalSold      = totalSold;
            this.dateAdded      = null;
            this.insertionOrder = Integer.MAX_VALUE;
        }

        double getSalesRate() {
            if (dateAdded == null) return totalSold == 0 ? -0.0001 : totalSold;
            long daysActive = ChronoUnit.DAYS.between(dateAdded, LocalDate.now());
            if (daysActive <= 0) daysActive = 1;
            return totalSold == 0 ? -daysActive : (double) totalSold / daysActive;
        }
    }
}