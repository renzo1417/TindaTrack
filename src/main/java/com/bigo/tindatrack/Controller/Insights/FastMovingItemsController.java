package com.bigo.tindatrack.Controller.Insights;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.Sales.Sales;
import com.bigo.tindatrack.SQLite_Database.SalesManagement.SalesManagement;
import com.bigo.tindatrack.SQLite_Database.StockManagement.StockFetchFromTable;
import com.bigo.tindatrack.SQLite_Database.productsManagement.fetchDataFromTable;
import com.bigo.tindatrack.data.StockDetails.StockDetails;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId;

public class FastMovingItemsController {

    // Label for pr progress bar
    private ProgressBar[] progressBars;


    private Label[] barNameLabels;


    private Label[] rankNameLabels;

    // labels for ranked list counts
    private Label[] rankCountLabels;

    public FastMovingItemsController(
            ProgressBar[] progressBars,
            Label[] barNameLabels,
            Label[] rankNameLabels,
            Label[] rankCountLabels
    ) {
        this.progressBars    = progressBars;
        this.barNameLabels   = barNameLabels;
        this.rankNameLabels  = rankNameLabels;
        this.rankCountLabels = rankCountLabels;
    }

    public void load() {
        // this hide everything first
        for (int i = 0; i < progressBars.length; i++) {
            progressBars[i].setVisible(false);
            progressBars[i].setManaged(false);
            barNameLabels[i].setVisible(false);
            barNameLabels[i].setManaged(false);
        }
        for (int i = 0; i < rankNameLabels.length; i++) {
            rankNameLabels[i].setVisible(false);
            rankNameLabels[i].setManaged(false);
            rankCountLabels[i].setVisible(false);
            rankCountLabels[i].setManaged(false);
        }

        int ownerId = getCurrentUserId();
        if (ownerId == -1) return;

        ObservableList<Product> products = fetchDataFromTable.getAllProducts(ownerId);
        ObservableList<Sales> rawSales   = SalesManagement.getSalesHistory(ownerId);
        ObservableList<StockDetails> allActivities = StockFetchFromTable.getActivitiesFromDB(ownerId);

        if (products.isEmpty()) return;

        //  build combined sales totals
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

        // date added logic
        Collections.reverse(allActivities);
        Map<String, LocalDate> addedDates      = new LinkedHashMap<>();
        List<String> insertionOrderList        = new ArrayList<>();
        for (StockDetails sd : allActivities) {
            if (sd.getOldQty() == 0 && !addedDates.containsKey(sd.getProductName())) {
                addedDates.put(sd.getProductName(), LocalDate.parse(sd.getDate()));
                insertionOrderList.add(sd.getProductName());
            }
        }

        for (ProductTotal pt : combinedTotals) {
            pt.dateAdded      = addedDates.get(pt.name);
            int idx           = insertionOrderList.indexOf(pt.name);
            pt.insertionOrder = (idx == -1) ? Integer.MAX_VALUE : idx;
        }

        // only keep active products not the one that are expired
        Set<String> activeNames = new HashSet<>();
        for (Product p : products) {
            LocalDate expiry = p.getLocalExpiryDate();
            if (expiry == null || !expiry.isBefore(LocalDate.now())) {
                activeNames.add(p.getProductName());
            }
        }
        combinedTotals.removeIf(pt -> !activeNames.contains(pt.name));

        // logic for product that has 0 sales to be display in ui
        Set<String> alreadyInTotals = new HashSet<>();
        for (ProductTotal pt : combinedTotals) alreadyInTotals.add(pt.name);
        for (Product p : products) {
            LocalDate expiry = p.getLocalExpiryDate();
            if (expiry != null && expiry.isBefore(LocalDate.now())) continue;
            if (!alreadyInTotals.contains(p.getProductName())) {
                ProductTotal zeroPt       = new ProductTotal(p.getProductName(), 0);
                zeroPt.dateAdded          = addedDates.get(p.getProductName());
                int idx                   = insertionOrderList.indexOf(p.getProductName());
                zeroPt.insertionOrder     = (idx == -1) ? Integer.MAX_VALUE : idx;
                combinedTotals.add(zeroPt);
            }
        }

        // sort by sales rate descending fastest moving items
        boolean hasAnySales = combinedTotals.stream().anyMatch(pt -> pt.totalSold > 0);
        if (hasAnySales) {
            combinedTotals.sort((a, b) -> {
                int cmp = Double.compare(b.getSalesRate(), a.getSalesRate());
                return cmp != 0 ? cmp : Integer.compare(a.insertionOrder, b.insertionOrder);
            });
        } else {
            combinedTotals.sort((a, b) -> Integer.compare(a.insertionOrder, b.insertionOrder));
        }


        double maxRate = combinedTotals.isEmpty() ? 1.0 : combinedTotals.get(0).getSalesRate();
        if (maxRate <= 0) maxRate = 1.0;

        int barLimit = Math.min(progressBars.length, combinedTotals.size());
        for (int i = 0; i < barLimit; i++) {
            ProductTotal pt = combinedTotals.get(i);


            String shortName = pt.name.contains(" ")
                    ? pt.name.substring(0, pt.name.indexOf(" "))
                    : pt.name;

            barNameLabels[i].setText(shortName);
            barNameLabels[i].setVisible(true);
            barNameLabels[i].setManaged(true);

            double progress = pt.getSalesRate() / maxRate;
            progressBars[i].setProgress(Math.max(0.0, Math.min(1.0, progress)));
            progressBars[i].setVisible(true);
            progressBars[i].setManaged(true);
        }

        //   ranked list
        int rankLimit = Math.min(rankNameLabels.length, combinedTotals.size());
        for (int i = 0; i < rankLimit; i++) {
            ProductTotal pt = combinedTotals.get(i);

            rankNameLabels[i].setText(pt.name);
            rankNameLabels[i].setVisible(true);
            rankNameLabels[i].setManaged(true);

            rankCountLabels[i].setText(pt.totalSold + "x");
            rankCountLabels[i].setVisible(true);
            rankCountLabels[i].setManaged(true);
        }
    }


    private static class ProductTotal {
        String    name;
        int       totalSold;
        LocalDate dateAdded;
        int       insertionOrder;

        ProductTotal(String name, int totalSold) {
            this.name           = name;
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