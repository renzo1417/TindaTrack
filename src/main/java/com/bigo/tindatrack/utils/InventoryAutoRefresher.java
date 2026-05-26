package com.bigo.tindatrack.utils;

import com.bigo.tindatrack.Controller.Inventory.InventoryController;
import javafx.application.Platform;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InventoryAutoRefresher {
    private ScheduledExecutorService scheduler;
    private final InventoryController controller;

    // passes the controller from the InventoryController in order for this class to know which class is its working with
    public InventoryAutoRefresher(InventoryController controller) {
        this.controller = controller;
    }

    public void start() {
        scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });

        scheduler.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                try {
                    System.out.println("thread is refreshing the table" );
                    controller.refreshTable();
                } catch (Exception e) {
                    System.err.println("Silent Auto-Refresh Failed: " + e.getMessage());
                }
            });

        }, 5, 5, TimeUnit.SECONDS);
    }

    public void stop() {
        // Kills the thread safely when user leaves the current screen
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }
}